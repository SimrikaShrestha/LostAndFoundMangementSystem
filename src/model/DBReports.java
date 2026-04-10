package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBReports {

    // Encapsulation + Abstraction: external code just calls this method
    // Returns total items reported in the system
    public static int getTotalItemsReported() {
        String sql = "SELECT COUNT(*) FROM items";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1); // Get count from DB
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0; // Return 0 if DB fails
    }

    // Returns number of items returned (approved claims)
    public static int getItemsReturned() {
        String sql = "SELECT COUNT(*) FROM claims WHERE status = 'Approved'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0;
    }

    // Returns number of pending claims
    public static int getPendingCases() {
        String sql = "SELECT COUNT(*) FROM claims WHERE status = 'Pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0;
    }

    // Returns success rate (% of approved claims)
    public static double getSuccessRate() {
        String sql = "SELECT (SUM(CASE WHEN status='Approved' THEN 1 ELSE 0 END) / COUNT(*)) * 100 FROM claims";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0.0;
    }

    // Inner class to represent monthly summary report
    // Encapsulation: groups related monthly data
    public static class MonthlyReport {
        public String month;
        public int lost, found, returned, pending;

        public MonthlyReport(String month, int lost, int found, int returned, int pending) {
            this.month    = month;
            this.lost     = lost;
            this.found    = found;
            this.returned = returned;
            this.pending  = pending;
        }
    }

    // Returns list of MonthlyReport objects for summary table
    // Abstraction: hides SQL aggregation and grouping details
    public static List<MonthlyReport> getMonthlyReport() {
        List<MonthlyReport> list = new ArrayList<>();
        String sql = """
            SELECT
                DATE_FORMAT(i.date, '%M %Y') AS month,
                SUM(CASE WHEN i.type = 'Lost'          THEN 1 ELSE 0 END) AS lost,
                SUM(CASE WHEN i.type = 'Found'         THEN 1 ELSE 0 END) AS found,
                SUM(CASE WHEN c.status = 'Approved'    THEN 1 ELSE 0 END) AS returned,
                SUM(CASE WHEN c.status = 'Pending'     THEN 1 ELSE 0 END) AS pending
            FROM items i
            LEFT JOIN claims c ON c.item_id = i.id
            GROUP BY DATE_FORMAT(i.date, '%M %Y')
            ORDER BY MIN(i.date) DESC
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Map DB row to MonthlyReport object
                list.add(new MonthlyReport(
                    rs.getString("month"),
                    rs.getInt("lost"),
                    rs.getInt("found"),
                    rs.getInt("returned"),
                    rs.getInt("pending")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }
}