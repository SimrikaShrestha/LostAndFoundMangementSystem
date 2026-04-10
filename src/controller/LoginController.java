package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.DBConnectionTest;
import model.SessionManager;
import model.User;

// This class handles user login functionality
public class LoginController {
    
    // This object is used to connect to the database
    DBConnectionTest connect = new DBConnectionTest();

    // Encapsulation
    private String currentUserRole;

    public boolean loginUser(String username, String password, String role) {
        
        String sql = "SELECT * FROM users WHERE username=? AND password=? AND role=?";
        
        try (Connection conn = connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);     // Check that the role also matches
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {   // If user is found in database
                
                currentUserRole = rs.getString("role");

                // Create a User object with all the user's information
                User loggedInUser = new User(
                    rs.getInt("id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                
                // Save the logged-in user so other parts of the program can use it
                SessionManager.getInstance().setCurrentUser(loggedInUser);

                return true;     // Login successful
            }
        } catch (Exception e) {
            e.printStackTrace();   // Print error if something goes wrong
        }
        
        return false;   // Login failed
    }

    // Getter method
    public String getCurrentUserRole() {
        return currentUserRole;
    }
}