package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminUserCrud {

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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) users.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return users;
    }

    public void createUser(String fullname, String email, String phone, String address,
                           String username, String password, String role) {
        String sql = "INSERT INTO users(fullname, email, phone, address, username, password, role) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullname); ps.setString(2, email); ps.setString(3, phone);
            ps.setString(4, address); ps.setString(5, username);
            ps.setString(6, password); ps.setString(7, role);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateUser(int id, String fullname, String email, String phone,
                           String address, String role) {
        String sql = "UPDATE users SET fullname=?, email=?, phone=?, address=?, role=? WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullname); ps.setString(2, email); ps.setString(3, phone);
            ps.setString(4, address); ps.setString(5, role); ps.setInt(6, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

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