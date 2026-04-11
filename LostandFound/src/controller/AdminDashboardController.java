package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.AdminUserCrud;
import model.CategoryCrud;
import model.DBConnection;
import model.StaffCrud;

public class AdminDashboardController {
    
    // Encapsulation
    private AdminUserCrud userCrud = new AdminUserCrud();
    private StaffCrud staffCrud = new StaffCrud();
    private CategoryCrud categoryCrud = new CategoryCrud();

    // Returns total number of users in the system
    public int getTotalUsers() { 
        return userCrud.countUsers(); 
    }

    // Returns total number of staff members
    public int getTotalStaff() { 
        return staffCrud.countStaff(); 
    }

    // Returns total number of categories
    public int getTotalCategories() { 
        return categoryCrud.countCategories(); 
    }

    // Returns total items (both Lost and Found)
    public int getTotalItems() {
        return countFromItems("SELECT COUNT(*) FROM items");
    }

    // Returns only Lost items count
    public int getTotalLostItems() {
        return countFromItems("SELECT COUNT(*) FROM items WHERE type = 'Lost'");
    }

    // Returns only Found items count
    public int getTotalFoundItems() {
        return countFromItems("SELECT COUNT(*) FROM items WHERE type = 'Found'");
    }

    // Returns number of claims that are still pending
    public int getPendingClaims() {
        return countFromItems("SELECT COUNT(*) FROM claims WHERE status = 'Pending'");
    }

    // Private helper method
    private int countFromItems(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);  
            }
        } catch (Exception e) {
            e.printStackTrace();   // Print error if something goes wrong
        }
        return 0;  
    }
}