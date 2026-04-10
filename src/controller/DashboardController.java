package controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DBConnection;
import model.Item;
import model.SessionManager;
import model.User;
// Handles everything shown on the user's Dashboard
public class DashboardController {
    
    // Encapsulation 
    private final User currentUser;
    // Constructor
    public DashboardController() {
        this.currentUser = SessionManager.getInstance().getCurrentUser();
    }
    // Returns the full name of the logged in user
    public String getUserFullName() {
        if (currentUser == null) return "Guest";
        return currentUser.getFullname();
    }
    // Returns total number of items this user has reported (Lost + Found)
    public int getItemsReported() {
        if (currentUser == null) return 0;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM items WHERE user_id = ?")) {
            
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) return rs.getInt(1);
            
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0;
    }
    // Returns how many items this user reported that were later marked as Returned
    // OR items the user claimed that got approved
    public int getItemsFound() {
        if (currentUser == null) return 0;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM items WHERE user_id = ? AND status = 'Returned' " +
                "UNION ALL " +
                "SELECT COUNT(*) FROM claims WHERE user_id = ? AND status = 'Approved'")) {
            
            ps.setInt(1, currentUser.getId());
            ps.setInt(2, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            
            int total = 0;
            while (rs.next()) total += rs.getInt(1);
            return total;
            
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0;
    }
    // Returns number of active (not closed) claims made by this user
    public int getActiveClaims() {
        if (currentUser == null) return 0;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM claims WHERE user_id = ? AND status != 'Closed'")) {
            
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) return rs.getInt(1);
            
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0;
    }
    public List<Item> getRecentActivities() {
        
        List<Item> items = new ArrayList<>();
        
        if (currentUser == null) return items;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                // Own reported items
                "SELECT i.name, i.category, i.date, i.status FROM items i " +
                "WHERE i.user_id = ? " +
                "UNION ALL " +
                // Items the user claimed - show the claim status instead of item status
                "SELECT i.name, i.category, c.claimed_date, c.status FROM claims c " +
                "JOIN items i ON c.item_id = i.id " +
                "WHERE c.user_id = ? " +
                "ORDER BY date DESC LIMIT 5")) {
            
            ps.setInt(1, currentUser.getId());
            ps.setInt(2, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(new Item(
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("date"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return items;
    }
}