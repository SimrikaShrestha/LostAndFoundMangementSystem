package controller;

import java.util.List;
import model.AdminUserCrud;
import model.User;

// This class handles all user management tasks for the Admin
public class AdminUserController {
    
    // Encapsulation
    private AdminUserCrud crud = new AdminUserCrud();

    // Returns a list of all users in the system
    public List<User> getAllUsers() {
        return crud.getAllUsers();
    }

    // Adds a new user with all their details
    public void addUser(String fullname, String email, String phone, String address,
                        String username, String password, String role) {
        
        crud.createUser(fullname, email, phone, address, username, password, role);
    }

    // Updates an existing user's information
    public void updateUser(int id, String fullname, String email, String phone,
                           String address, String role) {
        
        crud.updateUser(id, fullname, email, phone, address, role);
    }

    // Deletes a user using their ID
    public void deleteUser(int id) {
        crud.deleteUser(id);
    }
}