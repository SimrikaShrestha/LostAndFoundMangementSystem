package model;

public class Staff {

    // Private fields → encapsulation & data hiding
    private int id;
    private String fullname, email, phone, department, username, password, status;

    // Constructor to create Staff object
    // Abstraction: outside code just provides values, internal storage hidden
    public Staff(int id, String fullname, String email, String phone, String department, String username, String password, String status) {
        this.id = id; 
        this.fullname = fullname; 
        this.email = email; 
        this.phone = phone;
        this.department = department; 
        this.username = username; 
        this.password = password; 
        this.status = status;
    }

    // Getters → controlled access to private fields
    public int getId() { return id; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getStatus() { return status; }

    // Setter → controlled way to update staff status
    public void setStatus(String status) { this.status = status; }
}