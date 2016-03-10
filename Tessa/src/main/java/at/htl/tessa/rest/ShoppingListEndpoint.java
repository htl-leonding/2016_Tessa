package at.htl.tessa.rest;

import at.htl.tessa.business.ShoppingListFacade;
import at.htl.tessa.entity.ShoppingListEntry;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Stefanie on 17/12/15.
 */
@Stateless
@Path("shoppinglist")
public class ShoppingListEndpoint {

    @Inject
    private ShoppingListFacade facade;

    @Inject
    private HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShoppingListEntry> getAll(){
        return facade.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShoppingListEntry getEntry(@PathParam("id") long id) {
        return facade.find(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveEntry(ShoppingListEntry entry) throws URISyntaxException {
        long id = facade.save(entry).getId();
        StringBuffer url = request.getRequestURL();

        return Response.created(new URI(url.append("/" + id).toString())).build();

    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEntry(@PathParam("id") long id, ShoppingListEntry entry) {
        entry = facade.update(id, entry);
        return Response.accepted(entry).build();
    }

    @DELETE
    @Path("{id}")
    public void deleteEntry(@PathParam("id") long id) {
        facade.delete(id);
    }
}
