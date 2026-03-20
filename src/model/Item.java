package model;

public class Item {
    private String name;
    private String category;
    private String date;
    private String status;

    public Item(String name, String category, String date, String status) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.status = status;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}