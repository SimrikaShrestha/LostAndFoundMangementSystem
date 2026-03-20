package controller;

import model.SessionManager;
import model.User; 
public class RegisterController {

    private UserCrud crud;

    public RegisterController() {
        crud = new UserCrud();
    }

    public void registerUser(User user) {
        crud.createUser(user.getFullname(), user.getEmail(), user.getPhone(),
                        user.getAddress(), user.getUsername(), user.getPassword());

        // ✅ Fetch from DB and set session
        User registeredUser = crud.getUserByUsername(user.getUsername());
        if (registeredUser != null) {
            SessionManager.getInstance().setCurrentUser(registeredUser);
        }
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