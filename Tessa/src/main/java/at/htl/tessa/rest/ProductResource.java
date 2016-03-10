package at.htl.tessa.rest;

import at.htl.tessa.business.ProductFacade;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Korti on 25.10.2015.
 */
@Stateless
@Path("product")
public class ProductResource {

    @Inject
    private ProductFacade productFacade;
    @Inject
    private HttpServletRequest request;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(JsonObject product) throws URISyntaxException {
        long id = productFacade.save(product).getId();
        StringBuffer urlBuffer = request.getRequestURL();
        return Response.created(new URI(urlBuffer.append("/" + id).toString())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProduct(@PathParam("id") long id) {
        return productFacade.findJson(id);
    }

    @GET
    @Path("barcode={barcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getProductByBarcode(@PathParam("barcode") long barcode) {
        return productFacade.findJsonByBarcode(barcode);
    }

    //Methode, die den Rest-Service aufruft
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List getAllProducts() {
        return productFacade.getAll();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") long id, JsonObject product) {
        return Response.accepted(productFacade.update(id, product)).build();
    }

    @PUT
    @Path("{id}/increase")
    @Produces(MediaType.APPLICATION_JSON)
    public Response increaseProductCount(@PathParam("id") long id) {
        productFacade.increaseCount(id);
        return Response.ok(productFacade.findJson(id)).build();
    }

    @PUT
    @Path("{id}/decrease")
    @Produces(MediaType.APPLICATION_JSON)
    public Response decreaseProductCount(@PathParam("id") long id) {
        productFacade.decreaseCount(id);
        return Response.ok(productFacade.findJson(id)).build();
    }

    @DELETE
    @Path("{id}")
    public void deleteProduct(@PathParam("id") long id) {
        productFacade.delete(id);
    }
}
