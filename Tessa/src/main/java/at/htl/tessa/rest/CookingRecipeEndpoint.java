package at.htl.tessa.rest;

import at.htl.tessa.business.CookingRecipeFacade;
import at.htl.tessa.entity.CookingRecipe;

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
 * @author Korti
 * @since 21.04.2016
 */
@Stateless
@Path("cooking")
public class CookingRecipeEndpoint {

    @Inject
    private CookingRecipeFacade facade;

    @Inject
    private HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CookingRecipe> getAll(){
        return facade.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CookingRecipe getRecipe(@PathParam("id") long id) {
        return facade.find(id);
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public CookingRecipe getRecipe(@PathParam("name") String name) {
        return facade.findByName(name);
    }

    @GET
    @Path("s={query}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CookingRecipe> searchRecipes(@PathParam("query") String query) {
        return facade.searchRecipes(query);
    }

    @GET
    @Path("random/{count}")
    @Produces(MediaType.APPLICATION_JSON)
    public List getRandomProducts(@PathParam("count") int count) {
        return facade.getRandomRecipes(count);
    }

    @GET
    @Path("c={category}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CookingRecipe> getCategoryRecipes(@PathParam("category") String category) {
        return facade.findByCategory(category);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveRecipe(CookingRecipe recipe) throws URISyntaxException {
        long id = facade.save(recipe).getId();
        StringBuffer url = request.getRequestURL();
        return Response.created(new URI(url.append("/" + id).toString())).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CookingRecipe updateRecipe(@PathParam("id") long id, CookingRecipe recipe) {
        return facade.update(id, recipe);
    }

    @PUT
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public CookingRecipe updateRecipe(@PathParam("name") String name, CookingRecipe recipe) {
        return facade.update(name, recipe);
    }

}
