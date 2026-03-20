package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.DBConnectionTest;
import model.SessionManager;
import model.User;

public class ItemController {

    public boolean reportItem(String name, String category, String date,
                               String location, String description, String type) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) return false;

        String sql = "INSERT INTO items (user_id, name, category, date, location, description, type, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'Searching')";

        try (Connection conn = new DBConnectionTest().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
            e.printStackTrace();
            return false;
        }
    }
}