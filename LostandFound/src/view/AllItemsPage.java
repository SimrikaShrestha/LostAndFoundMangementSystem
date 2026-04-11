package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AllItemsPage {

    private Stage stage;          // Encapsulation: store stage for this page
    private String staffName;     // Encapsulation: keep staff name private

    // Constructor initializes page with stage and staff name
    public AllItemsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
    }

    // Show the page on screen
    public void show() {
        BorderPane root = new BorderPane();  // main layout
        root.setStyle("-fx-background-color:#f0f9fa;"); // light background
        // left sidebar from StaffDashboardPage (demonstrates reuse)
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("All Items"));
        root.setCenter(buildMainContent()); // center content
        stage.setScene(new Scene(root, 950, 650));
        stage.setTitle("All Items");
        stage.setMaximized(true); // maximize window
        stage.show();
    }

    // Build main content area (title + filter + table)
    private VBox buildMainContent() {
        VBox content = new VBox(20); // vertical layout with spacing
        content.setPadding(new Insets(30));

        Label title = new Label("All Items");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        // --- Filter bar ---
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField(); // search input
        searchField.setPromptText("Search items...");
        searchField.setPrefWidth(250);

        ComboBox<String> typeFilter = new ComboBox<>(); // filter by type
        typeFilter.getItems().addAll("All", "Lost", "Found");
        typeFilter.setValue("All");

        ComboBox<String> categoryFilter = new ComboBox<>(); // filter by category
        categoryFilter.getItems().addAll("All", "Electronics", "Accessories", "Bags", "Personal", "Documents", "Other");
        categoryFilter.setValue("All");

        Button filterBtn = new Button("Filter"); // button to apply filters
        filterBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-background-radius:8; -fx-padding:7 15;");

        filterBar.getChildren().addAll(searchField, typeFilter, categoryFilter, filterBtn);

        // --- Table area ---
        VBox tableBox = new VBox(0); // container for table rows
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");
        tableBox.setPadding(new Insets(20));

        // Column headers
        HBox colHeaders = new HBox();
        colHeaders.setPadding(new Insets(0, 0, 10, 0));
        colHeaders.getChildren().addAll(
            colHeader("ITEM NAME", 220),
            colHeader("CATEGORY", 140),
            colHeader("TYPE", 90),
            colHeader("STATUS", 110),
            colHeader("LOCATION", 170),
            colHeader("DATE", 110),
            colHeader("REPORTED BY", 140)
        );
        tableBox.getChildren().addAll(colHeaders, new Separator());

        // Load rows from database
        loadRows(tableBox, "", "All", "All");

        // Reload rows when filter button clicked (demonstrates abstraction)
        filterBtn.setOnAction(_ ->
            loadRows(tableBox,
                searchField.getText().trim(),
                typeFilter.getValue(),
                categoryFilter.getValue())
        );

        content.getChildren().addAll(title, filterBar, tableBox);
        return content;
    }

    // Load rows from database and add to table
    private void loadRows(VBox tableBox, String search, String type, String category) {
        // Remove old rows (after headers)
        if (tableBox.getChildren().size() > 2) {
            tableBox.getChildren().remove(2, tableBox.getChildren().size());
        }

        // Build SQL query dynamically (abstraction)
        StringBuilder sql = new StringBuilder(
            "SELECT i.name, i.category, i.type, i.status, i.location, i.date, u.fullname " +
            "FROM items i JOIN users u ON i.user_id = u.id WHERE 1=1"
        );
        if (!search.isEmpty())        sql.append(" AND (i.name LIKE ? OR i.location LIKE ?)");
        if (!type.equals("All"))      sql.append(" AND i.type = ?");
        if (!category.equals("All"))  sql.append(" AND i.category = ?");
        sql.append(" ORDER BY i.date DESC");

        try (Connection conn = DBConnection.getConnection(); // encapsulated DB connection
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (!search.isEmpty()) {
                ps.setString(idx++, "%" + search + "%");
                ps.setString(idx++, "%" + search + "%");
            }
            if (!type.equals("All"))     ps.setString(idx++, type);
            if (!category.equals("All")) ps.setString(idx++, category);

            ResultSet rs = ps.executeQuery();
            boolean hasRows = false;

            while (rs.next()) {
                hasRows = true;
                HBox row = new HBox(); // each table row
                row.setPadding(new Insets(10, 0, 10, 0));
                row.setStyle("-fx-border-color: transparent transparent #f0f0f0 transparent;");

                String typeVal   = rs.getString("type");
                String statusVal = rs.getString("status");

                // Add cells for each column
                row.getChildren().addAll(
                    rowCell(rs.getString("name"), 220),
                    rowCell(rs.getString("category"), 140),
                    typeChip(typeVal, 90),       // color chip for type
                    statusChip(statusVal, 110),  // color chip for status
                    rowCell(rs.getString("location"), 170),
                    rowCell(rs.getString("date"), 110),
                    rowCell(rs.getString("fullname"), 140)
                );
                tableBox.getChildren().add(row);
            }

            // Show message if no items
            if (!hasRows) {
                Label noData = new Label("No items found.");
                noData.setTextFill(Color.GRAY);
                noData.setPadding(new Insets(10, 0, 0, 0));
                tableBox.getChildren().add(noData);
            }

        } catch (Exception e) { // handle DB errors
            e.printStackTrace();
            Label err = new Label("Error loading items.");
            err.setTextFill(Color.RED);
            tableBox.getChildren().add(err);
        }
    }

    // Helper method to create column header (modularity)
    private Label colHeader(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font-size:11px;");
        return l;
    }

    // Helper method to create normal row cell
    private Label rowCell(String text, double width) {
        Label l = new Label(text == null ? "-" : text);
        l.setPrefWidth(width);
        l.setStyle("-fx-font-size:13px;");
        return l;
    }

    // Helper for type chip (Lost/Found) with color
    private Label typeChip(String type, double width) {
        Label l = new Label(type == null ? "-" : type);
        l.setPrefWidth(width);
        boolean isLost = "Lost".equalsIgnoreCase(type);
        l.setStyle("-fx-font-size:11px; -fx-font-weight:bold; -fx-text-fill:" +
            (isLost ? "#c0392b" : "#27ae60") + ";"); // red for lost, green for found
        return l;
    }

    // Helper for status chip (just display text)
    private Label statusChip(String status, double width) {
        Label l = new Label(status == null ? "-" : status);
        l.setPrefWidth(width);
        l.setStyle("-fx-font-size:11px; -fx-text-fill:#555;");
        return l;
    }
}