package model;

public class Category {
    // Fields are private → encapsulation & data hiding
    private int id;
    private String name, description;
    private int itemCount;

    // Constructor to create a Category object
    // Abstraction
    public Category(int id, String name, String description, int itemCount) {
        this.id = id; 
        this.name = name; 
        this.description = description; 
        this.itemCount = itemCount;
    }

    // Getter for id → access private field safely
    public int getId() { return id; }

    // Getter for name → access private field safely
    public String getName() { return name; }

    // Getter for description → access private field safely
    public String getDescription() { return description; }

    // Getter for item count → access private field safely
    public int getItemCount() { return itemCount; }
}