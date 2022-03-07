package hu.elte.fi.progtech.snake.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class PropertiesDataSource {

    /**
     * Az adatbázis elérése. (Minta Derby esetén: jdbc:derby://localhost:1527/BandViewer )
     */
    private final String url;
    /**
     * A felhasználó neve.
     */
    private final String user;
    /**
     * A felhasználóhoz tartozó jelszó.
     * (Megjegyzés: Enterprise alkalmazásban raw jelszót nem tárolunk Stringként biztonsági problémák miatt!)
     */
    private final String password;

    PropertiesDataSource(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("The given properties file should not be null!");
        }
        url = properties.getProperty("db.url");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");
    }

    /**
     * Metódus, mely segítségével egy munkamenetet tudunk kérni az adatbázisunkhoz.
     * Ezen a kapcsolaton keresztül tudunk lekérdezéseket futtatni.
     *
     * Érdemes megjegyezni, hogy enterprise alkalmazások esetén általában gyártó által biztosított DataSource-ot
     * szoktunk használni, illetve alkalmazás szervert / külső könyvtárat használunk, mely segítségével pool-ozni tudjuk a connection-öket.
     * Ez utóbbi azért hasznos, mert egy munkamenet létrehozása költséges tud lenni.
     * A pool segítségével pedig újra felhasználhatóvá tudunk tenni egy már létrehozott kapcsolatot.
     * Érdekesség: https://www.baeldung.com/java-connection-pooling
     *
     *
     * @return egy munkamenetet hoz létre az adott eléréshez.
     * @throws SQLException ha nem sikerült elérni az adatbázist vagy valamilyen hozzáférési probléma adódott, illetve ha
     *                      az url nem volt töltve.
     */
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
