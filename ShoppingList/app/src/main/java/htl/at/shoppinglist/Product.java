package htl.at.shoppinglist;

/**
 * Created by pautzi on 15.05.16.
 */
public class Product {
    int pieces;
    String title;

    public Product(String title,int pieces) {
        this.pieces = pieces;
        this.title = title;
    }

    public String getPieces() {
        return " "+Integer.toString(pieces)+" Stück";
    }

    public String getTitle() {
        return title;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
