package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Encapsulation: DB details hidden as private constants
    private static final String URL      = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "NewStrongPassword";

    // Abstraction: external code just calls getConnection(), doesn't know DB details
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD); // Connect to database
        } catch (SQLException e) {
            System.out.println("DBConnection: connection failed!");
            e.printStackTrace();
            return null; // Return null if connection fails
        }
    }
}