package at.htl.tessa.entity;

import javax.persistence.*;
import java.util.List;

/**
 * @author Korti
 * @since 21.04.2016
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "CookingRecipe.FindByName", query = "select r from CookingRecipe r where r.name = :NAME"),
        @NamedQuery(name = "CookingRecipe.GetAll", query = "select r from CookingRecipe r"),
        @NamedQuery(name = "CookingRecipe.FindByCategory", query = "select r from CookingRecipe r where r.category = :CATEGORY")
})
public class CookingRecipe {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String name;
    private String description;
    private String category;
    @Lob
    private byte[] picture;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> ingredients;

    public CookingRecipe() {

    }

    public CookingRecipe(String name, String description, String category, byte[] picture, List<Product> ingredients) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.picture = picture;
        this.ingredients = ingredients;
    }

    //region Getter and Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<Product> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Product> ingredients) {
        this.ingredients = ingredients;
    }
    //endregion
}
