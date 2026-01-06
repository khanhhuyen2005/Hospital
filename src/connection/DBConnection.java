package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL =
            "jdbc:sqlserver://DESKTOP-5Q8KCPO\\KTEAM;"
                    + "databaseName=BenhVienBachMai;"
                    + "integratedSecurity=true;"
                    + "trustServerCertificate=true";


    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
