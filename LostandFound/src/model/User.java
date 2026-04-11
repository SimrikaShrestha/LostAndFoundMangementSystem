package model;

public class User {

    // Private fields hold user data (encapsulation: only accessible via methods)
    private int id;
    private String fullname, email, phone, address, username, password, role;

    // Constructor to create a new User object with all data
    public User(int id, String fullname, String email, String phone, String address,
                String username, String password, String role) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter methods to access private fields (safe way to get data)
    public int getId()          { return id; }          // get user id
    public String getFullname() { return fullname; }    // get full name
    public String getEmail()    { return email; }       // get email
    public String getPhone()    { return phone; }       // get phone number
    public String getAddress()  { return address; }     // get address
    public String getUsername() { return username; }    // get username
    public String getPassword() { return password; }    // get password
    public String getRole()     { return role; }        // get user role (like admin/user)
}