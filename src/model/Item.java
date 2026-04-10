package model;

public class Item {

    // Private fields → encapsulation & data hiding
    private String name;
    private String category;
    private String date;
    private String status;

    // Constructor to create Item object
    // Abstraction: outside code just provides values, internal storage hidden
    public Item(String name, String category, String date, String status) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.status = status;
    }

    // Getters → access private fields safely
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}