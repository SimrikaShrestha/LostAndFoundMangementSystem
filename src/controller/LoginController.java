package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.DBConnectionTest;
import model.SessionManager;
import model.User;

public class LoginController {
    DBConnectionTest connect = new DBConnectionTest();
    private String currentUserRole;

    public boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentUserRole = rs.getString("role");

                
                User loggedInUser = new User(
                    rs.getInt("id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")      
                );
                SessionManager.getInstance().setCurrentUser(loggedInUser);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }
}