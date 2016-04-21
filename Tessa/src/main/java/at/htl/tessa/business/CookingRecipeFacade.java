package at.htl.tessa.business;

import at.htl.tessa.entity.CookingRecipe;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Korti on 21.04.2016.
 */
@Stateless
public class CookingRecipeFacade {

    @PersistenceContext
    private EntityManager entityManager;

    public CookingRecipe save(CookingRecipe recipe) {
        return entityManager.merge(recipe);
    }

    public CookingRecipe find(long id) {
        return entityManager.find(CookingRecipe.class, id);
    }

    public CookingRecipe findByName(String name) {
        try {
            return entityManager.createNamedQuery("CookingRecipe.FindByName", CookingRecipe.class)
                    .setParameter("NAME", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CookingRecipe> findAll() {
        try {
            return entityManager.createNamedQuery("CookingRecipe.GetAll", CookingRecipe.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public CookingRecipe update(long id, CookingRecipe recipe) {
        recipe.setId(id);
        return save(recipe);
    }

    public CookingRecipe update(String name, CookingRecipe recipe) {
        CookingRecipe oldRecipe = findByName(name);
        recipe.setId(oldRecipe.getId());
        return save(recipe);
    }
}
