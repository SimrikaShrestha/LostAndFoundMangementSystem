package controller;

import java.util.List;

import model.AdminUserCrud;
import model.User;

public class AdminUserController {

    private AdminUserCrud crud = new AdminUserCrud();

    public List<User> getAllUsers() {
        return crud.getAllUsers();
    }

    public void addUser(String fullname, String email, String phone, String address,
                        String username, String password, String role) {
        crud.createUser(fullname, email, phone, address, username, password, role);
    }

    public void updateUser(int id, String fullname, String email, String phone,
                           String address, String role) {
        crud.updateUser(id, fullname, email, phone, address, role);
    }

    public void deleteUser(int id) {
        crud.deleteUser(id);
    }
}