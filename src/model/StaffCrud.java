package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffCrud {

    // Get list of all staff from database
    public List<Staff> getAllStaff() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY id"; // SQL to fetch all staff
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs)); // convert each row to Staff object
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add new staff to database
    public void createStaff(String fullname, String email, String phone,
                            String department, String username, String password) {
        String sql = "INSERT INTO staff(fullname, email, phone, department, username, password, status) VALUES(?,?,?,?,?,?,'Active')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Set values for new staff
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, department);
            ps.setString(5, username);
            ps.setString(6, password);
            ps.executeUpdate(); // execute insert
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update existing staff info
    public void updateStaff(int id, String fullname, String email, String phone,
                            String department, String status) {
        String sql = "UPDATE staff SET fullname=?, email=?, phone=?, department=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, department);
            ps.setString(5, status);
            ps.setInt(6, id); // select staff by id
            ps.executeUpdate(); // execute update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove staff from database
    public void deleteStaff(int id) {
        String sql = "DELETE FROM staff WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); // specify staff to delete
            ps.executeUpdate(); // execute delete
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Count total staff in database
    public int countStaff() {
        String sql = "SELECT COUNT(*) FROM staff"; // SQL to count staff
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1); // return count
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // return 0 if error
    }

    // Convert a row from ResultSet to a Staff object
    // This shows encapsulation: Staff fields are private, accessed via constructor
    private Staff map(ResultSet rs) throws SQLException {
        return new Staff(
            rs.getInt("id"),
            rs.getString("fullname"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("department"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("status")
        );
    }
}