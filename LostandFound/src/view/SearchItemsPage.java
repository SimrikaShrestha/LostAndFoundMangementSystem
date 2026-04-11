package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.DBConnectionTest;

// This page lets users search for lost or found items by name, category, or type
// It extends BasePage to inherit the sidebar, stage, and showStage()
// Inheritance: SearchItemsPage is a child of BasePage - shared logic lives there
public class SearchItemsPage extends BasePage {

    // Constructor - stores the window and builds the page right away
    // stage is assigned to the inherited field from BasePage
    public SearchItemsPage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    // Sets up the page layout with sidebar on the left and search content in the center
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // buildSidebar() is inherited from BasePage - highlights "Search Items" in the menu
        root.setLeft(buildSidebar("Search Items"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Search Items");
        stage.setScene(scene);
    }

    // Builds the center section with title, search bar, filter dropdown, and results table
    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Search Items");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        Label subtitle = new Label("Search for lost or found items.");
        subtitle.setTextFill(Color.GRAY);

        // Search bar row - has a text field, a filter dropdown, and a search button
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by item name, category...");
        searchField.setPrefWidth(400);

        // Dropdown to filter by All, Lost, or Found
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Lost", "Found");
        filterBox.setValue("All"); // default shows everything

        Button searchBtn = new Button("Search");
        searchBtn.setStyle(
            "-fx-background-color:#7fd1d8;" +
            "-fx-text-fill:black;" +
            "-fx-background-radius:8;" +
            "-fx-padding:8 16 8 16;"
        );

        searchBox.getChildren().addAll(searchField, filterBox, searchBtn);

        // White results box that holds the column headers and search results
        VBox resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(20));
        resultsBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label resultsTitle = new Label("Results");
        resultsTitle.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");

        // Column headers for the results table
        // Encapsulation: colHeader() hides the label styling, we just pass text and width
        HBox colHeaders = new HBox();
        colHeaders.getChildren().addAll(
            colHeader("ITEM NAME", 250),
            colHeader("CATEGORY", 180),
            colHeader("STATUS",   120),
            colHeader("DATE",     150),
            colHeader("LOCATION", 150)
        );

        resultsBox.getChildren().addAll(resultsTitle, colHeaders, new Separator());

        // Load all items by default when the page first opens
        loadResults(resultsBox, "", "All");

        // Trigger a new search whenever the button is clicked or Enter is pressed in the field
        searchBtn.setOnAction(_   -> loadResults(resultsBox, searchField.getText().trim(), filterBox.getValue()));
        searchField.setOnAction(_ -> loadResults(resultsBox, searchField.getText().trim(), filterBox.getValue()));

        content.getChildren().addAll(title, subtitle, searchBox, resultsBox);
        return content;
    }

    // Clears the old results and loads fresh ones based on the search query and filter
    // Abstraction: the caller just passes a query and filter, this method handles the rest
    private void loadResults(VBox resultsBox, String query, String filter) {

        // Remove only the data rows (HBox with 5 children) - keep the headers and separator
        resultsBox.getChildren().removeIf(node -> node instanceof HBox && ((HBox) node).getChildren().size() == 5);

        // Fetch matching items from the database
        List<String[]> results = fetchFromDatabase(query, filter);

        if (results.isEmpty()) {
            // No matches found - show a labeled message instead of empty space
            Label noResults = new Label("No items found.");
            noResults.setTextFill(Color.GRAY);
            noResults.setPadding(new Insets(10, 0, 0, 0));
            noResults.setId("no-results"); // ID lets us find and remove this label later
            resultsBox.getChildren().add(noResults);
        } else {
            // Remove the "no results" label if it was shown before
            resultsBox.getChildren().removeIf(node -> node instanceof Label && "no-results".equals(node.getId()));

            // Build and add a row for each item returned from the database
            for (String[] row : results) {
                resultsBox.getChildren().add(buildResultRow(row));
            }
        }
    }

    // Queries the database for items matching the search text and selected filter
    // Builds the SQL dynamically depending on what the user typed and selected
    // Encapsulation: all the SQL logic is locked inside this method
    private List<String[]> fetchFromDatabase(String query, String filter) {
        List<String[]> list = new ArrayList<>();

        // Start with a base query that always works, then add conditions as needed
        StringBuilder sql = new StringBuilder("SELECT name, category, type, date, location FROM items WHERE 1=1");
        if (!filter.equals("All")) sql.append(" AND type = ?");
        if (!query.isEmpty())      sql.append(" AND (name LIKE ? OR category LIKE ?)");
        sql.append(" ORDER BY id DESC");

        try (Connection conn = new DBConnectionTest().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Fill in the placeholders in order based on which conditions were added
            int idx = 1;
            if (!filter.equals("All")) ps.setString(idx++, filter);
            if (!query.isEmpty()) {
                // Use % wildcards so partial matches also show up in results
                ps.setString(idx++, "%" + query + "%");
                ps.setString(idx++, "%" + query + "%");
            }

            ResultSet rs = ps.executeQuery();

            // Store each row as a String array: [name, category, type, date, location]
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("type"),
                    rs.getString("date"),
                    rs.getString("location")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Creates a small gray label for column headers in the results table
    // Encapsulation: the header styling is hidden inside this method
    private Label colHeader(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setTextFill(Color.GRAY);
        label.setStyle("-fx-font-size:11px;");
        return label;
    }

    // Builds one row in the results table from a String array of item data
    // The status label gets red styling for Lost items and green for Found items
    // Polymorphism-like: same method, different color output based on the status value
    private HBox buildResultRow(String[] data) {
        HBox row = new HBox();
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setAlignment(Pos.CENTER_LEFT);

        // Item name shown in bold
        Label name = new Label(data[0]);
        name.setPrefWidth(250);
        name.setStyle("-fx-font-weight:bold;");

        Label category = new Label(data[1]);
        category.setPrefWidth(180);
        category.setTextFill(Color.GRAY);

        // Status badge - red background for Lost, green for Found
        Label status = new Label(data[2]);
        status.setPrefWidth(120);
        status.setPadding(new Insets(3, 8, 3, 8));
        status.setStyle(data[2].equalsIgnoreCase("Lost")
            ? "-fx-background-color:#ffe0e0; -fx-text-fill:#cc0000; -fx-background-radius:5;"
            : "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;"
        );

        Label date = new Label(data[3]);
        date.setPrefWidth(150);
        date.setTextFill(Color.GRAY);

        Label location = new Label(data[4]);
        location.setPrefWidth(150);
        location.setTextFill(Color.GRAY);

        row.getChildren().addAll(name, category, status, date, location);
        return row;
    }

    // Overrides the abstract show() method required by BasePage
    // Every child page must provide this - that's how abstract methods work
    @Override
    public void show() {
        showStage();
    }
}