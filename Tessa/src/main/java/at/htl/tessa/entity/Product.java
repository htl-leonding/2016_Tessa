package at.htl.tessa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Korti
 * @since 25.10.2015
 */

@Entity
@NamedQueries({
        //Liest alle Produkte aus dem Producttable
        @NamedQuery(name = "Product.GetAll", query = "select p from Product p where p.date is not null"),
        @NamedQuery(name = "Product.GetByBarcode", query = "select p from Product p where p.barcode = :BARCODE")
})
@Inheritance(strategy = InheritanceType.JOINED)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;
    private long barcode;
    private int stueck;
    private LocalDate date;

    public Product() {
    }

    public Product(long id, String name, long barcode, int stueck, LocalDate date) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.stueck = stueck;
        this.date = date;
    }

    //region Getter and Setter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public int getStueck() {
        return stueck;
    }

    public void setStueck(int stueck) {
        this.stueck = stueck;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate tage) {
        this.date = tage;
    }
    //endregion


    @Override
    public String toString() {
        return "[ ID: " + id + ", Name: " + name + ", Barcode: " + barcode + "]";
    }
}
