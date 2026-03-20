package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.DBConnectionTest;
import model.Item;
import model.SessionManager;
import model.User;

public class DashboardController {

    private final User currentUser;

    public DashboardController() {
        this.currentUser = SessionManager.getInstance().getCurrentUser();
    }

    public String getUserFullName() {
        if (currentUser == null) return "Guest";
        return currentUser.getFullname(); 
    }

    public int getItemsReported() {
        if (currentUser == null) return 0;
        try (Connection conn = new DBConnectionTest().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM items WHERE user_id = ?")) {
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getItemsFound() {
        if (currentUser == null) return 0;
        try (Connection conn = new DBConnectionTest().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM items WHERE user_id = ? AND status = 'Returned'")) {
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getActiveClaims() {
        if (currentUser == null) return 0;
        try (Connection conn = new DBConnectionTest().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM claims WHERE user_id = ? AND status != 'Closed'")) {
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<Item> getRecentActivities() {
        List<Item> items = new ArrayList<>();
        if (currentUser == null) return items;
        try (Connection conn = new DBConnectionTest().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT name, category, date, status FROM items " +
                "WHERE user_id = ? ORDER BY date DESC LIMIT 5")) {
            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new Item(
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("date"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }
}