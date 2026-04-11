package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminUserCrud {

    // Encapsulation
    private Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mydb",
                    "root",
                    "NewStrongPassword"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Abstraction
    // Encapsulation
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='user' ORDER BY id";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) users.add(map(rs)); // Reuse map() to convert ResultSet -> User (code reuse)
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return users;
    }

   
    // Encapsulation
    public void createUser(String fullname, String email, String phone, String address,
                           String username, String password, String role) {
        String sql = "INSERT INTO users(fullname, email, phone, address, username, password, role) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, username);
            ps.setString(6, password);
            ps.setString(7, role);
            ps.executeUpdate(); // Execute insert
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
    // Encapsulation
    public void updateUser(int id, String fullname, String email, String phone,
                           String address, String role) {
        String sql = "UPDATE users SET fullname=?, email=?, phone=?, address=?, role=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, role);
            ps.setInt(6, id);
            ps.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

  
    // Abstraction
    // Encapsulation
    public void deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connect();
            conn.setAutoCommit(false); // Begin transaction

            // Delete claims related to user's items
            String sql1 = "DELETE FROM claims WHERE item_id IN (SELECT id FROM items WHERE user_id = ?)";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, userId);
            ps.executeUpdate();

            // Delete claims made by user
            String sql2 = "DELETE FROM claims WHERE user_id = ?";
            ps = conn.prepareStatement(sql2);
            ps.setInt(1, userId);
            ps.executeUpdate();

            // Delete user's items
            String sql3 = "DELETE FROM items WHERE user_id = ?";
            ps = conn.prepareStatement(sql3);
            ps.setInt(1, userId);
            ps.executeUpdate();

            // Delete user
            String sql4 = "DELETE FROM users WHERE id = ?";
            ps = conn.prepareStatement(sql4);
            ps.setInt(1, userId);
            ps.executeUpdate();

            conn.commit(); // Commit all changes
            System.out.println("User deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close(); // Close resources
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // Abstraction
    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE role='user'";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0;
    }
    // Map ResultSet row to User object
    // Encapsulation
    private User map(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("fullname"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role")
        );
    }
}