package at.htl.tessa.business;

import at.htl.tessa.entity.ShoppingListEntry;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Stefanie on 17/12/15.
 */
@Stateless
public class ShoppingListFacade {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ProductFacade facade;

    public ShoppingListEntry save(ShoppingListEntry entry) {
        return em.merge(entry);
    }

    public ShoppingListEntry find(long id) {
        return em.find(ShoppingListEntry.class, id);
    }

    public List<ShoppingListEntry> findAll() {
        List<ShoppingListEntry> entries = em.createNamedQuery("ShoppingListEntry.findAll", ShoppingListEntry.class).getResultList();
        return entries;
    }

    public ShoppingListEntry update(long id, ShoppingListEntry updateEntry) {
        ShoppingListEntry entry = find(id);
        if (entry != null) {
            entry.setName(updateEntry.getName());
            entry.setBarcode(updateEntry.getBarcode());
            entry.setStueck(updateEntry.getStueck());
            entry.setPermanent(updateEntry.isPermanent());
        }

        return save(entry);
    }

    /**
     * Die Stückanzahl eines Eintrages wird um 1 erhöht.
     * @param id ID des Listeintrages.
     * @return Aktualisierter Eintrag.
     * @since 14.06.2016
     * @author Korti
     */
    public ShoppingListEntry increaseCount(long id){
        ShoppingListEntry entry = find(id);
        entry.setStueck(entry.getStueck() + 1);
        return entry;
    }

    /**
     * Die Stückanzahl eines Eintrages wird um 1 veringert.
     * @param id ID des Listeintrages.
     * @return Aktualisierter Eintrag.
     * @since 14.06.2016
     * @author Korti
     */
    public ShoppingListEntry decreaseCount(long id){
        ShoppingListEntry entry = find(id);
        if(entry.getStueck() > 1) {
            entry.setStueck(entry.getStueck() - 1);
        }
        return entry;
    }

    /**
     * Löscht einen Shopping List Eintrag aus der Datenbank über die ID.
     * @param id ID des List Eintrages.
     * @since 03.01.2016
     * @author Korti
     */
    public void delete(long id) {
        em.remove(find(id));
    }
}
