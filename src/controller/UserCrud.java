package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;

public class UserCrud {

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

    public void createUser(String fullname, String email, String phone, String address, String username, String password) {
        try {
            Connection conn = connect();
            String sql = "INSERT INTO users(fullname, email, phone, address, username, password) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, username);
            ps.setString(6, password);
            ps.executeUpdate();
            System.out.println("User Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUserByUsername(String username) {
        try {
            Connection conn = connect();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void viewUser() {
        try {
            Connection conn = connect();
            String sql = "SELECT * FROM users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(
                        rs.getString("fullname") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("phone") + " | " +
                        rs.getString("address") + " | " +
                        rs.getString("username") + " | " +
                        rs.getString("password")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(String username, String fullname, String email, String phone, String address, String password) {
        try {
            Connection conn = connect();
            String sql = "UPDATE users SET fullname=?, email=?, phone=?, address=?, password=? WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, password);
            ps.setString(6, username);
            ps.executeUpdate();
            System.out.println("User Updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        try {
            Connection conn = connect();
            String sql = "DELETE FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.executeUpdate();
            System.out.println("User Deleted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}