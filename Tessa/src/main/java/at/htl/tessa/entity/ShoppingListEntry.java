package at.htl.tessa.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Created by Stefanie on 17/12/15.
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "ShoppingListEntry.findAll", query = "select e from ShoppingListEntry e")
})
public class ShoppingListEntry extends Product{

    boolean isPermanent = false;

    public ShoppingListEntry() {
    }

    public ShoppingListEntry(long id, String name, long barcode, int stueck, boolean isPermanent) {
        super(id, name, barcode, stueck, null);
        this.isPermanent = isPermanent;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }
}
