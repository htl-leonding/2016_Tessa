package at.htl.tessa.business;

import at.htl.tessa.entity.CookingRecipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Stefanie on 17.05.16.
 */
@Stateless
public class RecipeLoader {

    public static String vorspeisenPath = "Rezepte/vorspeisen.html";
    public static String hauptspeisenUrl = "localhost:8080/Tessa/Rezepte/hauptspeisen.htm";
    public static String nachspeisenUrl = "localhost:8080/Tessa/Rezepte/nachspeisen.htm";
    public static String drinksUrls = "/webapp/Rezepte/drinks.htm";
    private static String imageUrl = "/Rezepte/Bilder/klein/%s";

    private static final String contentClassName = "Inhaltsverzeichnis";

    @Inject
    private CookingRecipeFacade facade;

    public void loadVorspeisen() {

        Document vorspeisenPage;
        InputStream fileStream = null;
        BufferedReader reader = null;

        try{

            fileStream = RecipeLoader.class.getClassLoader().getResourceAsStream(vorspeisenPath);
            reader = new BufferedReader(new InputStreamReader(fileStream));
            StringBuilder htmlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line);
            }

            vorspeisenPage = Jsoup.parse(htmlBuilder.toString());
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

        Element body = vorspeisenPage.body();

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
            byte[] image = loadImage(imageName);

            recipes.add(new CookingRecipe(recipeName, "", image, null));
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
