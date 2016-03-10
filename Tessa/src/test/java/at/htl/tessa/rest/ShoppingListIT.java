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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Korti on 03.01.2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShoppingListIT {

    private Client client;
    private WebTarget target;

    @Before
    public void setupClient() {
        client = ClientBuilder.newClient();
        target = client.target("http://localhost:8080/FridgePi/rs/shoppinglist");
    }

    private JsonObject createProduct(String name, long barcode, int stueck, boolean isPermanent) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("name", name);
        builder.add("barcode", barcode);
        builder.add("stueck", stueck);
        builder.add("permanent", isPermanent);
        return builder.build();
    }

    private JsonObject createProduct(long id, String name, long barcode, int stueck, boolean isPermanent) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", id);
        builder.add("name", name);
        builder.add("barcode", barcode);
        builder.add("stueck", stueck);
        builder.add("permanent", isPermanent);
        return builder.build();
    }

    @Test
    public void t001createListEntry(){
        JsonObject product = createProduct("VÖSLAUER Balance Juicy Blutorange", 9009700307333L, 1, false);
        Response response = target.request().post(Entity.entity(product, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(201));
        product = response.readEntity(JsonObject.class);
        System.out.println(product);
    }

    @Test
    public void t002getListEntry() {
        Response response = target.path("1").request().get();
        assertThat(response.getStatus(), is(200));
        JsonObject product = response.readEntity(JsonObject.class);
        System.out.println(product);
    }

    @Test
    public void t006updateListEntry() {
        JsonObject product = createProduct(1, "VÖSLAUER Balance Juicy Blutorange", 9009700307335L, 1, true);
        Response response = target.path("1").request().put(Entity.entity(product, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus(), is(202));
        product = response.readEntity(JsonObject.class);
        assertThat(product.getInt("id"), is(1));
        System.out.println(product);
    }

    @Test
    public void t500deleteProduct() {
        Response response = target.path("1").request().delete();
        assertThat(response.getStatus(), is(204));
    }

}
