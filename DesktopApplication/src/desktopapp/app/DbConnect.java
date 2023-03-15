package desktopapp.app;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnect {
    public Connection databaseLink;
    public Connection getConnection() {
        String DBName = "pracainz";
        String DBUser = "root";
        String DBPass = "";
        String DBURL = "jdbc:mysql://localhost:3306/" + DBName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(DBURL, DBUser, DBPass);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
