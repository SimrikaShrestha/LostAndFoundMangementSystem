package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBClaims {

    // Inner class to represent found items
    // Encapsulation: keeps related data together
    public static class FoundItem {
        public int id;
        public String name, category, date, location, description;

        // Constructor to create a FoundItem object
        // Abstraction: outside code just provides values
        public FoundItem(int id, String name, String category, String date, String location, String description) {
            this.id          = id;
            this.name        = name;
            this.category    = category;
            this.date        = date;
            this.location    = location;
            this.description = description;
        }
    }

    // Get all found items that do NOT belong to current user
    // Abstraction: hides SQL, returns list of FoundItem objects
    // Encapsulation: DB logic is inside method
    public static List<FoundItem> getFoundItems(int currentUserId) {
        List<FoundItem> list = new ArrayList<>();
        String sql = "SELECT id, name, category, date, location, description " +
                     "FROM items WHERE type = 'Found' AND user_id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Map SQL row to FoundItem object (code reuse)
                list.add(new FoundItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // Check if user already sent a claim for a specific item
    // Abstraction: returns true/false without exposing SQL details
    public static boolean alreadyClaimed(int userId, int itemId) {
        String sql = "SELECT COUNT(*) FROM claims WHERE user_id = ? AND item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0; // Return true if already claimed
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    // Insert a new claim request for a found item
    // Encapsulation: hides SQL insert details
    // Abstraction: returns true if claim inserted successfully
    public static boolean sendClaim(int userId, int itemId) {
        String sql = "INSERT INTO claims (user_id, item_id, status, claimed_date) " +
                     "VALUES (?, ?, 'Pending', CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, itemId);
            return ps.executeUpdate() > 0; // True if insert worked
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return false;
    }
}