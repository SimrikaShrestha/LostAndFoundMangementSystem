package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryCrud {

    // Get all categories from database
    // Abstraction: hides SQL details, returns list of Category objects
    // Encapsulation: DB access handled inside method
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Map SQL row to Category object (code reuse)
                list.add(new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("item_count")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Create new category
    // Encapsulation: hides insert SQL, external code just calls method
    // Inheritance potential: could be inherited in a BaseCrud class
    public void createCategory(String name, String description) {
        String sql = "INSERT INTO categories(name, description) VALUES(?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.executeUpdate(); // Execute insert
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update existing category
    // Abstraction: other code doesn't know SQL details
    public void updateCategory(int id, String name, String description) {
        String sql = "UPDATE categories SET name=?, description=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, id);
            ps.executeUpdate(); // Apply update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a category by id
    // Encapsulation: hides SQL delete logic
    public void deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate(); // Execute delete
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Count total categories
    // Abstraction: just returns number, SQL hidden
    public int countCategories() {
        String sql = "SELECT COUNT(*) FROM categories";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}