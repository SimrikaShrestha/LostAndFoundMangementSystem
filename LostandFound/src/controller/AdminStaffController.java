package controller;

import java.util.List;
import model.Staff;
import model.StaffCrud;

// This class handles all staff management tasks for the Admin
public class AdminStaffController {
    
    // Encapsulation
    private StaffCrud crud = new StaffCrud();

    // Returns a list of all staff members
    public List<Staff> getAllStaff() { 
        return crud.getAllStaff(); 
    }

    // Adds a new staff member by passing all details
    public void addStaff(String fullname, String email, String phone,
                         String department, String username, String password) {
        
        crud.createStaff(fullname, email, phone, department, username, password);
    }

    // Updates staff information using their ID
    public void updateStaff(int id, String fullname, String email, String phone,
                            String department, String status) {
        
        crud.updateStaff(id, fullname, email, phone, department, status);
    }

    // Deletes a staff member using their ID
    public void deleteStaff(int id) { 
        crud.deleteStaff(id); 
    }
}