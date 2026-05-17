package config;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static Properties props = null;

    private static void load() {
        if (props != null) return;
        props = new Properties();
        try (InputStream in = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("WEB-INF/database.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                props.setProperty("db.host", "localhost");
                props.setProperty("db.port", "3306");
                props.setProperty("db.name", "hospital_final");
                props.setProperty("db.user", "root");
                props.setProperty("db.password", "jesus21");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUrl() {
        load();
        return "jdbc:mariadb://" + props.getProperty("db.host") + ":"
                + props.getProperty("db.port") + "/"
                + props.getProperty("db.name");
    }

    public static String getUser() {
        load();
        return props.getProperty("db.user");
    }

    public static String getPassword() {
        load();
        return props.getProperty("db.password");
    }
}
