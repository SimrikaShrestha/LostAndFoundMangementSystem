package model;

public class Category {
    private int id;
    private String name, description;
    private int itemCount;

    public Category(int id, String name, String description, int itemCount) {
        this.id = id; this.name = name; this.description = description; this.itemCount = itemCount;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getItemCount() { return itemCount; }
}