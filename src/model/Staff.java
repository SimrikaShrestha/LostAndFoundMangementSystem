package model;

public class Staff {
    private int id;
    private String fullname, email, phone, department, username, password, status;

    public Staff(int id, String fullname, String email, String phone, String department, String username, String password, String status) {
        this.id = id; this.fullname = fullname; this.email = email; this.phone = phone;
        this.department = department; this.username = username; this.password = password; this.status = status;
    }

    public int getId() { return id; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}