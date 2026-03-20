package controller;

import java.util.List;

import model.Staff;
import model.StaffCrud;

public class AdminStaffController {
    private StaffCrud crud = new StaffCrud();

    public List<Staff> getAllStaff() { return crud.getAllStaff(); }

    public void addStaff(String fullname, String email, String phone,
                         String department, String username, String password) {
        crud.createStaff(fullname, email, phone, department, username, password);
    }

    public void updateStaff(int id, String fullname, String email, String phone,
                            String department, String status) {
        crud.updateStaff(id, fullname, email, phone, department, status);
    }

    public void deleteStaff(int id) { crud.deleteStaff(id); }
}