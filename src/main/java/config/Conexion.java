package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static Connection con = null;

    public static Connection conectar() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("org.mariadb.jdbc.Driver");
                con = DriverManager.getConnection(
                        DatabaseConfig.getUrl(),
                        DatabaseConfig.getUser(),
                        DatabaseConfig.getPassword());
            }
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return con;
    }
}
