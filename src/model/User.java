package model;

public class User {
    private int id;
    private String fullname, email, phone, address, username, password, role;

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

    public int getId()          { return id; }
    public String getFullname() { return fullname; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }
    public String getAddress()  { return address; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
}
