package at.htl.barcodedaemon;

import at.htl.barcodedaemon.config.Config;

import javax.json.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Korti on 27.04.2016.
 */
public class Main {

    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String PATTERN_KEY = "date_pattern";
    private static final String CONFIG_FILE = "config.properties";
    private static final String REST_PATH = "Tessa/rs/product";
    private static final String PARAM = "barcode=%s";
    private static final String BASE_URL = "http://%s:%s/%s";
    private static final String SEARCH_KEY = "id";
    private static final String DATE_KEY = "date";

    private static Client client;
    private static Config config;
    private static WebTarget target;
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            init();
            loop();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private static void init() {
        config = new Config(CONFIG_FILE);
        config.load();
        scanner = new Scanner(System.in);
        client = ClientBuilder.newClient();
        target = client.target(String.format(BASE_URL, config.get(HOST_KEY), config.get(PORT_KEY), REST_PATH));
    }

    private static void loop() throws IOException {
        while (true) {
            String barcode = scanner.next();
            Response response = target.path(String.format(PARAM, barcode)).request().get();
            if (response.getStatus() != Status.OK.getStatusCode()) {
                System.out.println(String.format("Response status code: %d", response.getStatus()));
                System.out.println(response.getStatusInfo().getReasonPhrase());
            } else {
                JsonObject object = response.readEntity(JsonObject.class);
                if (object.containsKey(SEARCH_KEY)) {
                    target.request().post(Entity.json(recreateJsonObject(object)));
                }
            }
            response.close();
        }
    }

    private static JsonObject recreateJsonObject(JsonObject object) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        for (Map.Entry<String, JsonValue> entry : object.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        builder.add(DATE_KEY, LocalDate.now().plusDays(30).
                format(DateTimeFormatter.ofPattern(config.get(PATTERN_KEY))));
        return builder.build();
    }

    private static void close() {
        client.close();
        config.save();
    }

}
