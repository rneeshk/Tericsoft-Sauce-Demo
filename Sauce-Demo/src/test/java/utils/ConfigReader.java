package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    // RECTIFIED: The static block now loads the properties file as a resource stream.
    // This is the standard, most reliable way to handle resource files in Maven.
    static {
        properties = new Properties();
        String configFileName = "config.properties";
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + configFileName);
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load configuration file.");
        }
    }

    public static String getProperty(String key) {
        String property = properties.getProperty(key);
        if (property != null) {
            return property.trim();
        }
        throw new RuntimeException("Property not found in config.properties: " + key);
    }
}