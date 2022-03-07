package hu.elte.fi.progtech.snake.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesReader {

    private PropertiesReader(){}

    /**
     * Az adatbázishoz való csatlakozáshoz tartozó config-ot adja meg.
     * @param path a config file elérési útja.
     */
    public static Properties readProperties(String path) {
        try (final FileInputStream fileInputStream = new FileInputStream(path)){
            final Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read the properties file from the given path!", ex);
        }
    }
}
