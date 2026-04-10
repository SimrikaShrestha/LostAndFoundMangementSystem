package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {

    // Encapsulation: DB details hidden as private constants
    private static final String URL = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "NewStrongPassword";

    // Abstraction: other code can get a connection without knowing DB details
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null; // Return null if connection fails
        }
    }

    // Main method to test database connection
    // Abstraction: user just runs the program, doesn't need to know JDBC details
    public static void main(String[] args) {
        Connection conn = getConnection(); // Try to connect

        if (conn != null) {
            System.out.println("Connection test successful");
        } else {
            System.out.println("Connection test failed");
        }
    }
}