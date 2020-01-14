package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public Properties prop;

    public PropertyLoader() {

        this.prop = new Properties();

        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/simplinize.properties"));
        } catch (IOException io) {
            System.err.println("File could not be loaded!");
        }

    }
}
