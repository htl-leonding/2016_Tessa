package at.htl.tessa.business;

import at.htl.tessa.entity.Product;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

/**
 * @author Korti
 * @since 25.10.2015
 */
@Stateless
public class ProductFacade {

    private static final String DATE_PATTERN = "dd MMMM, yyyy";

    @PersistenceContext(unitName = "FridgePiPU")
    private EntityManager entityManager;

    @Inject
    private ProductFinder productFinder;

    /**
     * Speichert ein Produkt in die Datenbank.
     * @param product
     * @return
     */
    private Product save(Product product) {
        return entityManager.merge(product);
    }

    public Product save(JsonObject jsonObject) {
        return save(parseJson(jsonObject, 0));
    }

    private Product find(long id) {
        return entityManager.find(Product.class, id);
    }

    private Product findByBarcode(long barcode) {
        try {
            return entityManager.createNamedQuery("Product.GetByBarcode", Product.class).setParameter("BARCODE", barcode).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Zur Suche eines Produktes in der Datenbank über den Barcode.
     * @param barcode Barcode des gesuchten Producktes.
     * @return Gesuchtes Produkt.
     * @since 19.01.2016
     * @author Korti
     */
    public JsonObject findJsonByBarcode(long barcode) {
        Product product = findByBarcode(barcode);
        if(product == null) {
            String name = productFinder.findProductName(barcode);
            return parseJson(new Product(0, name, barcode, 1, null));
        } else {
            product.setStueck(product.getStueck() + 1);
            return Json.createObjectBuilder().build();
        }
    }

    public JsonObject findJson(long id) {
        return parseJson(find(id));
    }

    //Ruft NamedQuery von Product-Entity auf (Product.GetAll)
    public JsonArray getAll() {
        List<Product> products = entityManager.createNamedQuery("Product.GetAll", Product.class).getResultList();
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (Product product : products) {
            builder.add(parseJson(product));
        }
        return builder.build();
    }

    public JsonObject update(long id, JsonObject updateProduct) {
        return parseJson(save(parseJson(updateProduct, id)));
    }

    public void delete(long id){
        entityManager.remove(find(id));
    }

    /**
     * Setzt die Stückanzahl eines Produktes um 1 höher.
     * @param id ID des Produktes.
     * @since 09.12.2015
     * @author Korti
     */
    public void increaseCount(long id) {
        Product product = find(id);
        product.setStueck(product.getStueck() + 1);
    }

    /**
     * Setzt die Stückanzahl eines Produktes um 1 herab.
     * @param id ID des Produktes.
     * @since 09.12.2015
     * @author Korti
     */
    public void decreaseCount(long id) {
        Product product = find(id);
        if(product.getStueck() > 1) {
            product.setStueck(product.getStueck() - 1);
        }
    }

    public boolean isEnough(long barcode, int stueck){

        Product product = findByBarcode(barcode);
        if((stueck/2) >=  product.getStueck()){
            return  false;
        }

        return true;
    }

    /**
     * Konvertiert ein Json Object zu einem Produkt.
     * @param jsonObject Produkt als Json Object.
     * @param id ID des Produktes.
     * @return Konvertiertes Produkt.
     * @since 17.01.2016
     * @author Korti
     */
    private Product parseJson(JsonObject jsonObject, long id) {
        if (!(jsonObject.get("barcode") instanceof JsonNumber) && !(jsonObject.get("stueck") instanceof JsonNumber)) {
            return parseStringJson(jsonObject, id);
        }
        long barcode = jsonObject.getJsonNumber("barcode").longValue();
        int stueck = jsonObject.getInt("stueck");
        LocalDate date = LocalDate.parse(jsonObject.getString("date"), DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.US));
        String name = jsonObject.getString("name");

        return new Product(id, name, barcode, stueck, date);
    }

    private Product parseStringJson(JsonObject jsonObject, long id) {
        long barcode = 0;
        if(jsonObject.containsKey("barcode")) {
            Long.valueOf(jsonObject.getString("barcode"));
        }
        int stueck = Integer.valueOf(jsonObject.getString("stueck"));
        LocalDate date = LocalDate.parse(jsonObject.getString("date"), DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.US));
        String name = jsonObject.getString("name");

        return new Product(id, name, barcode, stueck, date);
    }

    /**
     * Konvertiert ein Produkt zu einem Json Object.
     * @param product Produkt das zu einem Json Object konvertiert werden soll.
     * @return Konvertiertes Produkt als Json Object.
     * @since 17.01.2016
     * @author Korti
     */
    private JsonObject parseJson(Product product) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", product.getId());
        builder.add("barcode", product.getBarcode());
        builder.add("name", product.getName());
        builder.add("stueck", product.getStueck());
        if(product.getDate() != null) {
            builder.add("tage", ChronoUnit.DAYS.between(LocalDate.now(), product.getDate()));
        }
        return builder.build();
    }

}
