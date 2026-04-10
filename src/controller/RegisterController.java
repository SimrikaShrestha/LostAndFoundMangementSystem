package controller;

import model.SessionManager;
import model.User;

public class RegisterController {

    private UserCrud crud;

    public RegisterController() {
        crud = new UserCrud();
    }

    // Registers new user with validation - prevents blank registration
    public boolean registerUser(User user) {

        // Validation
        if (user == null ||
            user.getFullname() == null || user.getFullname().trim().isEmpty() ||
            user.getEmail() == null || user.getEmail().trim().isEmpty() ||
            user.getUsername() == null || user.getUsername().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            
            return false; // Registration failed due to blank fields
        }

        // Save new user to database
        crud.createUser(user.getFullname(), user.getEmail(), user.getPhone(),
                        user.getAddress(), user.getUsername(), user.getPassword());

        return true; // Registration successful
    }

    public void updateUser(User user) {
        crud.updateUser(user.getUsername(), user.getFullname(), user.getEmail(),
                        user.getPhone(), user.getAddress(), user.getPassword());
    }

    public void deleteUser(String username) {
        crud.deleteUser(username);
    }

    public void viewUsers() {
        crud.viewUser();
    }
}