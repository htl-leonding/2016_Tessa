package at.htl.tessa.business;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.ejb.Stateless;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Korti on 18.01.2016.
 */
@Stateless
public class ProductFinder {

    private final String barcodeUrl = "";

    public String findProductName(long barcode) {
        Document productPage;
        try {
            productPage = Jsoup.connect(getProductPageLink(barcode)).get();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        Element element = productPage.select("h1").first();
        return element.text();
    }

    private String getProductPageLink(long barcode) {
        try(final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(false);
            final HtmlPage mainPage = webClient.getPage(barcodeUrl);
            final HtmlForm searchForm = findFormByAction(mainPage, "product.search");
            final HtmlTextInput searchField = searchForm.getInputByName("q");
            final HtmlSubmitInput searchButton = searchForm.getInputByName("OK");
            searchField.setValueAttribute(String.valueOf(barcode));
            return searchButton.click().getUrl().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private HtmlForm findFormByAction(HtmlPage page, String action) {
        List<HtmlForm> forms = page.getForms();
        for (HtmlForm form : forms) {
            if (form.getActionAttribute().equals(action)) {
                return form;
            }
        }
        return null;
    }

}
