package at.htl.tessa.business;

import at.htl.tessa.entity.CookingRecipe;
import at.htl.tessa.entity.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Stefanie on 17.05.16.
 */
@Stateless
public class RecipeLoader {

    private static String vorspeisenPath = "Rezepte/vorspeisen.html";
    private static String hauptspeisenPath = "Rezepte/hauptspeisen.html";
    private static String nachspeisenPath = "Rezepte/nachspeisen.html";
    private static String drinksPath = "Rezepte/drinks.html";
    private static String imageUrl = "/Rezepte/Bilder/klein/%s";

    private static final String contentClassName = "Inhaltsverzeichnis";

    @Inject
    private CookingRecipeFacade facade;

    public void loadVorspeisen(){
        loadPage("Vorspeise", vorspeisenPath);
    }

    public void loadHauptspeisen(){
        loadPage("Hauptspeise", hauptspeisenPath);
    }

    public void loadNachspeisen(){
        loadPage("Nachspeise", nachspeisenPath);
    }

    public void loadDrinks(){
        loadPage("Drink", drinksPath);
    }

    public void loadPage(String category, String path) {

        Document htmlPage;
        InputStream fileStream = null;
        BufferedReader reader = null;

        try{

            fileStream = RecipeLoader.class.getClassLoader().getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(fileStream, StandardCharsets.UTF_8));
            StringBuilder htmlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line);
            }

            htmlPage = Jsoup.parse(htmlBuilder.toString());
        } catch (IOException e){
            e.printStackTrace();
            return;
        } finally {
            try {
                if(fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Element body = htmlPage.body();

        Elements contents = body.getElementsByClass(contentClassName);
        Element content;

        if (contents.size() > 1) {
            return;
        } else {
            content = contents.get(0);
        }

        List<String> recipeIds = new ArrayList<>(content.children().size());
        List<CookingRecipe> recipes = new LinkedList<>();

        for (Element element : content.children()) {
            Element link = element.select("a").first();
            String id = link.attr("href");
            id = id.substring(id.indexOf("#") + 1);
            recipeIds.add(id);
        }

        for (String recipeId : recipeIds) {
            Element recipe = body.getElementById(recipeId);
            String recipeName = recipe.select("h2").first().text();
            String imageName = recipe.select("img").first().attr("src");
            imageName = imageName.substring(imageName.lastIndexOf("/") + 1);
            String description = "";
            List<String> ingredientList = new ArrayList<>();
            description = recipe.select("div").last().text();
            String workingtime = description.substring(description.indexOf("Arbeitszeit")+17);
            description = description.substring(description.indexOf("Zubereitung:")+12, description.indexOf("Arbeitszeit")-1);
            String ingredients = recipe.select("div").html();
            ingredients = ingredients.substring(ingredients.indexOf("<h3>Zutaten:</h3>") + 17, ingredients.indexOf("</div>\n" + "<div class=\"Zubereitung\">"));
            String ingredient = "";
            for (int i = 0; i < ingredients.length(); i++) {

                if(ingredients.charAt(i) != '\n'){
                    ingredient += ingredients.charAt(i);
                } else {
                    if(ingredientList.size() != 0) {
                        ingredientList.add(ingredient.substring(5));
                    } else {
                        ingredientList.add(ingredient);
                    }
                    ingredient = "";
                }

            }

            byte[] image = loadImage(imageName);

            for (String str:ingredientList) {
                ingredient += str + ";";
            }

            recipes.add(new CookingRecipe(recipeName, description, category, image, ingredient, false));
        }

        facade.save(recipes);
    }

    private byte[] loadImage(String imageName) {
        String strPath = String.format(imageUrl, imageName);
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = RecipeLoader.class.getResourceAsStream(strPath);
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) > 0){
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}