package at.htl.barcodedaemon.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Korti on 27.04.2016.
 */
public class Config {

    private Properties prop;
    private File file;

    public Config(File file) {
        prop = new Properties();
        this.file = file;
    }

    public Config(String path) {
        this(new File(path));
    }

    public void load() {
        try {
            prop.load(new FileInputStream(file));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            prop.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return prop.getProperty(key);
    }

}
