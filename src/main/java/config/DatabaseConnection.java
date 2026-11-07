/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//Membuka koneksi MySQL. Setiap pemanggilan menghasilkan koneksi baru. */
public final class DatabaseConnection {
    private static final Properties PROPERTIES = loadProperties();

    private DatabaseConnection() { }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) properties.load(input);
        } catch (IOException ex) {
            throw new IllegalStateException("Gagal membaca database.properties.", ex);
        }
        return properties;
    }

    public static Connection bukaKoneksi() throws SQLException {
        String url = System.getenv().getOrDefault("SPRING_DB_URL",
                PROPERTIES.getProperty("db.url", "jdbc:mysql://localhost:3306/db_amdk_spring?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true"));
        String user = System.getenv().getOrDefault("SPRING_DB_USER", PROPERTIES.getProperty("db.username", "root"));
        String password = System.getenv().getOrDefault("SPRING_DB_PASSWORD", PROPERTIES.getProperty("db.password", ""));
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean tesKoneksi() {
        try (Connection connection = bukaKoneksi()) {
            return connection != null && connection.isValid(3);
        } catch (SQLException ex) {
            return false;
        }
    }
}
