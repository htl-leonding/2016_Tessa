package htl.at.shoppinglist;

/**
 * Created by pautzi on 15.05.16.
 */
public class Product implements Comparable<Product> {
    String pieces;
    String title;
    private long dbID;

    public Product(long dbID,String title,String pieces) {
        this.dbID = dbID;
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

    public long getDbID() {
        return dbID;
    }

    public void setDbID(long dbID) {
        this.dbID = dbID;
    }

    @Override
    public int compareTo(Product another) {
       return this.getTitle().compareTo(another.getTitle());
    }

    public String toJsonString() {
        return "{"+"\"name\":\""+title+"\"," + "\"stueck\":"+pieces+","+"\"permanent\":"+"true"+"}";
    }
}
