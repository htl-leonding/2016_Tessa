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
    public ShoppingListEntry saveEntry(ShoppingListEntry entry) throws URISyntaxException {
        return facade.save(entry);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEntry(@PathParam("id") long id, ShoppingListEntry entry) {
        entry = facade.update(id, entry);
        return Response.accepted(entry).build();
    }

    /**
     * REST Service um die Stückanzahl eines Eintrages um 1 zu erhöhen
     * @param id ID des Listeintrages
     * @return Response mit dem aktualisierten Eintrag.
     * @since 14.06.2016
     * @author Korti
     */
    @PUT
    @Path("{id}/increase")
    @Produces(MediaType.APPLICATION_JSON)
    public Response increaseEntryCount(@PathParam("id") long id){
        return Response.accepted(facade.increaseCount(id)).build();
    }

    /**
     * REST Service um die Stückanzahl eines Eintrages um 1 zu verringern.
     * @param id ID des Listeintrages.
     * @return Response mit dem aktualisierten Eintrag.
     * @since 14.06.2016
     * @author Korti
     */
    @PUT
    @Path("{id}/decrease")
    @Produces(MediaType.APPLICATION_JSON)
    public Response decreaseEntryCount(@PathParam("id") long id){
        return Response.accepted(facade.decreaseCount(id)).build();
    }

    /**
     * REST Service zum löschen eines Shopping List Eintrages über die ID.
     * @param id ID des List Eintrages.
     * @since 03.01.2016
     * @author Korti
     */
    @DELETE
    @Path("{id}")
    public void deleteEntry(@PathParam("id") long id) {
        facade.delete(id);
    }
}
