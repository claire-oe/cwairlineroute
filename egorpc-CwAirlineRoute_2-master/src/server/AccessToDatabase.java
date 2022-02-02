package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class AccessToDatabase {
    /**
     * Contains connection configurations for SQLite database
     * @author Claire Egorp
     */
    public static final String DB_URL = "jdbc:sqlite:src/database/flight-data.sqlite";
    public static final String DB_USERNAME = "";
    public static final String DB_PASSWORD = "";

    /**
     * Gets Connection to our SQLite database
     * @return Connection object for accessing the database
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        System.out.println("Database Connection established.");
        return connection;

    }
}



