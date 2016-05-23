package htl.at.shoppinglist;

/**
 * Created by pautzi on 15.05.16.
 */
public class Product implements Comparable<Product> {
    String pieces;
    String title;

    public Product(String title,String pieces) {
        this.pieces = pieces;
        this.title = title;
    }

    public String getPieces() {
        return " "+pieces+" pieces";
    }

    public String getTitle() {
        return title;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Product another) {
       return this.getTitle().compareTo(another.getTitle());
    }
}
