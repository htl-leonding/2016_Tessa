package at.htl.tessa.business;

import at.htl.tessa.entity.CookingRecipe;
import at.htl.tessa.entity.Product;
import at.htl.tessa.util.Util;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Korti on 21.04.2016.
 * @author Korti
 * @since 21.04.2016
 */
@Stateless
public class CookingRecipeFacade {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Speichert ein Kochrezept in die Datenbank.
     * @param recipe Das zu speichernde Rezept.
     * @return Kochrezept mit der ID aus der Datenbank.
     * @since 21.04.2016
     * @author Korti
     */
    public CookingRecipe save(CookingRecipe recipe) {
        return entityManager.merge(recipe);
    }

    public void save(List<CookingRecipe> recipes) {
        recipes.forEach(this::save);
    }

    /**
     * Zur Suche eines Rezeptes in der Datenbank über die ID.
     * @param id ID des gesuchten Rezeptes.
     * @return Gesuchte Rezept.
     * @since 21.04.2016
     * @author Korti
     */
    public CookingRecipe find(long id) {
        return entityManager.find(CookingRecipe.class, id);
    }

    /**
     * Zur Suche eines Rezeptes in der Datenbank über den Namen.
     * @param name Name des gesuchten Rezeptes.
     * @return Gesuchte Rezept.
     * @since 21.04.2016
     * @author Korti
     */
    public CookingRecipe findByName(String name) {
        try {
            return entityManager.createNamedQuery("CookingRecipe.FindByName", CookingRecipe.class)
                    .setParameter("NAME", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Zur Suche eines Rezeptes in der Datenbank über den Teil eines Namens.
     * @param query Teil des Namens des gesuchten Rezeptes.
     * @return Gefundene Rezepte.
     * @since 18.05.2016
     * @author Korti
     */
    public List<CookingRecipe> searchRecipes(String query) {
        try {
            return entityManager.createNamedQuery("CookingRecipe.Search", CookingRecipe.class)
                    .setParameter("SEARCH", "%" + query.toLowerCase() + "%").getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Gibt alle Rezpte die in der Datenbank gespeichert sind.
     * @return Liste aller Kochrezepte.
     * @since 21.04.2016
     * @author Korti
     */
    public List<CookingRecipe> findAll() {
        try {
            return entityManager.createNamedQuery("CookingRecipe.GetAll", CookingRecipe.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Gibt alle Favourisierten Rezepte aus der Datenbank zurück.
     * @return Liste aller Favourisierten Rezepte.
     * @since 14.06.2016
     * @author Korti
     */
    public List<CookingRecipe> findFavourites(){
        try {
            return entityManager.createNamedQuery("CookingRecipe.Favourites", CookingRecipe.class).getResultList();
        } catch (NoResultException e){
            return null;
        }
    }

    /**
     * Gibt alle Rezpte aus der gewählten Kategorie, die in der Datenbank gespeichert sind.
     * @return Liste aller Kochrezepte einer Kategorie.
     * @since 18.05.2016
     * @author Saiz
     */
    public List<CookingRecipe> findByCategory(String category) {
        try {
            return entityManager.createNamedQuery("CookingRecipe.FindByCategory", CookingRecipe.class)
                    .setParameter("CATEGORY", category).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Aktualisiert ein Rezept in der Datenbank über die ID.
     * @param id ID des zu aktualisierenden Rezeptes.
     * @param recipe Aktualisiertes Rezept ohne ID.
     * @return Aktualisiertes Rezept mit ID.
     * @since 21.04.2016
     * @author Korti
     */
    public CookingRecipe update(long id, CookingRecipe recipe) {
        recipe.setId(id);
        return save(recipe);
    }

    /**
     * Aktualisiert ein Rezept in der Datenbank über den Namen.
     * @param name Name des zu aktualisierenden Rezeptes.
     * @param recipe Aktualisiertes Rezept ohne ID.
     * @return Aktualisiertes Rezept mit ID.
     * @since 21.04.2016
     * @author Korti
     */
    public CookingRecipe update(String name, CookingRecipe recipe) {
        CookingRecipe oldRecipe = findByName(name);
        recipe.setId(oldRecipe.getId());
        return save(recipe);
    }

    /**
     * Gibt eine Liste mit Zufälligen Rezepten zurück.
     * @param count Anzahl der Produkte.
     * @return Liste mit Zufälligen Rezepten.
     * @since 09.06.2016
     * @author Korti
     */
    public List getRandomRecipes(int count) {
        Random random = new Random(System.currentTimeMillis());
        long recipeCount = countRecipes() - 1L;
        List<CookingRecipe> recipes = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            long randomId = Util.randomLong(random, recipeCount);
            CookingRecipe recipe = find(randomId);
            if(!recipes.contains(recipe)) {
                recipes.add(find(randomId));
            }
        }
        return recipes;
    }

    public long countRecipes() {
        try {
            return entityManager.createNamedQuery("CookingRecipe.Count", Long.class).getSingleResult();
        } catch (NoResultException e) {
            return -1;
        }
    }
}