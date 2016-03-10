package at.htl.tessa.rest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Korti on 25.10.2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductIT {

    private static final String DATE_PATERN = "dd.MM.yyyy";

    private Client client;
    private WebTarget target;

    private final long barcode = 9002490206550l;

    @Before
    public void setupClient() {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/FridgePi/rs/product");
    }

    private JsonObject createProduct(String name, long barcode, int stueck, LocalDate date) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", name);
        builder.add("barcode", barcode);
        builder.add("stueck", stueck);
        builder.add("date", date.format(DateTimeFormatter.ofPattern(DATE_PATERN)));
        return builder.build();
    }

    private JsonObject createProduct(long id, String name, long barcode, int stueck, int tage) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", id);
        builder.add("name", name);
        builder.add("barcode", barcode);
        builder.add("stueck", stueck);
        builder.add("tage", tage);
        return builder.build();
    }

    @Test
    public void t001createProduct(){
        JsonObject product = createProduct("VÖSLAUER Balance Juicy Blutorange", 9009700307333L, 1, LocalDate.of(2016, 9, 13));
        Response response = target.request().post(Entity.entity(product, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(201));
        product = response.readEntity(JsonObject.class);
        System.out.println(product);
    }

    @Test
    public void t002getProduct() {
        Response response = target.path("1").request().get();
        assertThat(response.getStatus(), is(200));
        JsonObject product = response.readEntity(JsonObject.class);
        System.out.println(product);
    }

    @Test
    public void t003updateProduct() {
        JsonObject product = createProduct("VÖSLAUER Balance Juicy Blutorange", 9009700307335L, 1, LocalDate.of(2016, 10, 20));
        Response response = target.path("1").request().put(Entity.entity(product, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(202));
        product = response.readEntity(JsonObject.class);
        assertThat(product.getInt("id"), is(1));
        System.out.println(product);
    }

    @Test
    public void t004increaseCountProduct() {
        Response response = target.path("1/increase").request().put(Entity.json(Json.createObjectBuilder().build()));
        assertThat(response.getStatus(), is(200));
        JsonObject product = response.readEntity(JsonObject.class);
        assertThat(product.getInt("stueck"), is(2));
        System.out.println(product);
    }

    @Test
    public void t005decreaseCountProduct() {
        Response response = target.path("1/decrease").request().put(Entity.json(Json.createObjectBuilder().build()));
        assertThat(response.getStatus(), is(200));
        JsonObject product = response.readEntity(JsonObject.class);
        assertThat(product.getInt("stueck"), is(1));
        System.out.println(product);
    }

    @Test
    public void t006findProductByBarcode() {
        Response response = target.path("barcode=" + barcode).request().get();
        assertThat(response.getStatus(), is(200));
        JsonObject product = response.readEntity(JsonObject.class);
        System.out.println(product);
    }

    @Test
    public void t500deleteProduct() {
        Response response = target.path("1").request().delete();
        assertThat(response.getStatus(), is(204));
    }

}
