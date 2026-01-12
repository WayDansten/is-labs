package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HikariPropertiesLoader {
    private HikariPropertiesLoader(){}

    public static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = HikariPropertiesLoader.class.getClassLoader()
                .getResourceAsStream("hikari.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find hikari.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
        return props;
    }
}
