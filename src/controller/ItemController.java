package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import model.DBConnection;
import model.SessionManager;
import model.User;

// Handles reporting of Lost and Found items
public class ItemController {

    public boolean reportItem(String name, String category, String date,
                              String location, String description, String type) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        
        
        if (currentUser == null) return false;


        
        String sql = "INSERT INTO items (user_id, name, category, date, location, description, type, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Searching')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Fill in the values in the SQL query
            ps.setInt(1, currentUser.getId());
            ps.setString(2, name);
            ps.setString(3, category);
            ps.setString(4, date);
            ps.setString(5, location);
            ps.setString(6, description);
            ps.setString(7, type);        
            
            ps.executeUpdate();   
            return true;          
            
        } catch (Exception e) {
            e.printStackTrace();  // Print error details if something goes wrong
            return false;         // Return false if failed
        }
    }
}