package view;

import java.util.List;
import controller.DashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Item;


// It extends BasePage - that means it inherits common things like stage and showStage()
// Inheritance: DashboardPage is a child of BasePage, so we don't rewrite shared logic
public class DashboardPage extends BasePage {

    // controller handles all the data logic for this page
    // We keep it private - encapsulation, so nothing outside this class can touch it directly
    private DashboardController controller;

    // Constructor - sets up the window and controller, then builds the page
    // We pass stage here and store it in the parent class (BasePage) variable
    public DashboardPage(Stage stage) {
        this.stage = stage;
        this.controller = new DashboardController();
        buildPage();
    }

    // Builds the full page layout and puts it on the screen
    // Called once when the page is first created
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // buildSidebar() comes from BasePage - we inherit it, no need to rewrite it here
        root.setLeft(buildSidebar("Dashboard"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("User Dashboard");
        stage.setScene(scene);
    }

    
    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        
        Label welcome = new Label("Welcome back, " + controller.getUserFullName() + "!");
        welcome.setStyle("-fx-font-size:24px; -fx-font-weight:bold;");

        Label subtitle = new Label("Here's what's happening with your reported items today.");
        subtitle.setTextFill(Color.GRAY);

        // Pull the 3 key numbers from the controller
        int reported = controller.getItemsReported();
        int found    = controller.getItemsFound();
        int claims   = controller.getActiveClaims();

       
        String successRate = reported > 0 ? (found * 100 / reported) + "% success rate" : "No items yet";
        String weeklyNote  = reported > 0 ? "+" + reported + " total reported" : "No reports yet";
        String claimsNote  = claims  > 0 ? "Action required" : "No active claims";

        // Create the 3 stat cards side by side
        HBox statsBox = new HBox(15);
        statsBox.getChildren().addAll(
            buildStatCard("ITEMS REPORTED", String.valueOf(reported), weeklyNote),
            buildStatCard("ITEMS FOUND",    String.valueOf(found),    successRate),
            buildStatCard("ACTIVE CLAIMS",  String.valueOf(claims),   claimsNote)
        );

        // White box that holds the recent activities table
        VBox tableBox = new VBox(10);
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-padding:20;");

        // Table heading row with title on the left
        HBox tableHeader = new HBox();
        Label tableTitle = new Label("Recent Activities");
        tableTitle.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");

        // Spacer pushes anything after it to the right side
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        tableHeader.getChildren().addAll(tableTitle, spacer);

        // Column labels at the top of the table
        HBox colHeaders = new HBox();
        colHeaders.setPadding(new Insets(5, 0, 5, 0));
        colHeaders.getChildren().addAll(
            tableColHeader("ITEM NAME", 250),
            tableColHeader("CATEGORY",  200),
            tableColHeader("DATE",      150),
            tableColHeader("STATUS",    100)
        );

        tableBox.getChildren().addAll(tableHeader, colHeaders, new Separator());

        // Ask the controller for the list of recent items to show
        List<Item> recentItems = controller.getRecentActivities();

        if (recentItems.isEmpty()) {
            // No data found - show a simple message instead of an empty table
            Label noItems = new Label("No recent activities found.");
            noItems.setTextFill(Color.GRAY);
            noItems.setPadding(new Insets(10, 0, 0, 0));
            tableBox.getChildren().add(noItems);
        } else {
            // Loop through each item and add a row for it in the table
            for (Item item : recentItems) {
                tableBox.getChildren().add(buildTableRow(item));
            }
        }

        content.getChildren().addAll(welcome, subtitle, statsBox, tableBox);
        return content;
    }

    // Builds one stat card (white box with a title, big number, and small note)
    // Abstraction: the caller just passes 3 strings and gets back a ready-made card
    private VBox buildStatCard(String title, String value, String note) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.GRAY);
        titleLabel.setStyle("-fx-font-size:11px;");

        // Big bold number in the middle of the card
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size:28px; -fx-font-weight:bold;");

        Label noteLabel = new Label(note);
        noteLabel.setTextFill(Color.GRAY);
        noteLabel.setStyle("-fx-font-size:11px;");

        card.getChildren().addAll(titleLabel, valueLabel, noteLabel);
        return card;
    }

    // Creates a small gray header label for the table columns
    // Encapsulation: the styling details are hidden in here, callers just pass text and width
    private Label tableColHeader(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setTextFill(Color.GRAY);
        label.setStyle("-fx-font-size:11px;");
        return label;
    }

    // Builds one row in the recent activities table using an Item object
    // Each row shows the item name, category, date, and a colored status badge
    private HBox buildTableRow(Item item) {
        HBox row = new HBox();
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(item.getName());
        name.setPrefWidth(250);
        name.setStyle("-fx-font-weight:bold;");

        Label category = new Label(item.getCategory());
        category.setPrefWidth(200);
        category.setTextFill(Color.GRAY);

        Label date = new Label(item.getDate());
        date.setPrefWidth(150);
        date.setTextFill(Color.GRAY);

        // Status label gets colored styling based on what the status value is
        Label status = new Label(item.getStatus());
        status.setPadding(new Insets(4, 10, 4, 10));
        status.setStyle(getStatusStyle(item.getStatus()));

        row.getChildren().addAll(name, category, date, status);
        return row;
    }

    // Returns the right color style string depending on the item's status
    // Polymorphism-like behavior: same method, different result based on what status is passed in
    private String getStatusStyle(String status) {
        return switch (status) {
            case "Processing" -> "-fx-background-color:#dce8ff; -fx-text-fill:#3366cc; -fx-background-radius:5;";
            case "Returned"   -> "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;";
            case "In Review"  -> "-fx-background-color:#fff3cd; -fx-text-fill:#856404; -fx-background-radius:5;";
            case "Searching"  -> "-fx-background-color:#f0f0f0; -fx-text-fill:#333333; -fx-background-radius:5;";
            case "Approved"   -> "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;";
            case "Rejected"   -> "-fx-background-color:#fde8e8; -fx-text-fill:#c0392b; -fx-background-radius:5;";
            default           -> "-fx-background-color:#f0f0f0; -fx-text-fill:black; -fx-background-radius:5;";
        };
    }

    // This overrides the abstract show() method from BasePage
    // Every page must have its own show() - this is how abstract methods work
    // We just call showStage() which is already defined in BasePage
    @Override
    public void show() {
        showStage();
    }
}