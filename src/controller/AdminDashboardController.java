package controller;

import model.AdminUserCrud;
import model.CategoryCrud;
import model.StaffCrud;

public class AdminDashboardController {
    private AdminUserCrud userCrud = new AdminUserCrud();
    private StaffCrud staffCrud = new StaffCrud();
    private CategoryCrud categoryCrud = new CategoryCrud();

    public int getTotalUsers() { return userCrud.countUsers(); }
    public int getTotalStaff() { return staffCrud.countStaff(); }
    public int getTotalCategories() { return categoryCrud.countCategories(); }
}