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

public class SearchItemsPage {

    private Stage stage;

    public SearchItemsPage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new DashboardPage(stage).buildSidebar("Search Items"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Search Items");
        stage.setScene(scene);
    }

    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Search Items");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold;");

        Label subtitle = new Label("Search for lost or found items.");
        subtitle.setTextFill(Color.GRAY);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by item name, category...");
        searchField.setPrefWidth(400);

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Lost", "Found");
        filterBox.setValue("All");

        Button searchBtn = new Button("Search");
        searchBtn.setStyle(
                "-fx-background-color:#7fd1d8;" +
                "-fx-text-fill:black;" +
                "-fx-background-radius:8;" +
                "-fx-padding:8 16 8 16;"
        );

        searchBox.getChildren().addAll(searchField, filterBox, searchBtn);

        VBox resultsBox = new VBox(10);
        resultsBox.setPadding(new Insets(20));
        resultsBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label resultsTitle = new Label("Results");
        resultsTitle.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");

        HBox colHeaders = new HBox();
        colHeaders.getChildren().addAll(
                colHeader("ITEM NAME", 250),
                colHeader("CATEGORY", 180),
                colHeader("STATUS", 120),
                colHeader("DATE", 150),
                colHeader("LOCATION", 150)
        );

        resultsBox.getChildren().addAll(resultsTitle, colHeaders, new Separator());

        // ✅ Load all items from database on page open
        loadResults(resultsBox, "", "All");

        // ✅ Search button fetches fresh data from DB every time
        searchBtn.setOnAction(_ -> {
            String query  = searchField.getText().trim();
            String filter = filterBox.getValue();
            loadResults(resultsBox, query, filter);
        });

        // ✅ Also search when pressing Enter in the search field
        searchField.setOnAction(_ -> {
            String query  = searchField.getText().trim();
            String filter = filterBox.getValue();
            loadResults(resultsBox, query, filter);
        });

        content.getChildren().addAll(title, subtitle, searchBox, resultsBox);
        return content;
    }

    /**
     * Fetches items from MySQL database and displays them in resultsBox.
     * Filters by keyword and type (Lost / Found / All).
     * This replaces the old hardcoded sample data.
     */
    private void loadResults(VBox resultsBox, String query, String filter) {
        // Remove old result rows (keep title, headers, separator = first 3 children)
        resultsBox.getChildren().removeIf(node ->
            node instanceof HBox && ((HBox) node).getChildren().size() == 5
        );

        List<String[]> results = fetchFromDatabase(query, filter);

        if (results.isEmpty()) {
            Label noResults = new Label("No items found.");
            noResults.setTextFill(Color.GRAY);
            noResults.setPadding(new Insets(10, 0, 0, 0));
            noResults.setId("no-results");
            resultsBox.getChildren().add(noResults);
        } else {
            // Remove "No items found" label if present
            resultsBox.getChildren().removeIf(node ->
                node instanceof Label && "no-results".equals(node.getId())
            );
            for (String[] row : results) {
                resultsBox.getChildren().add(buildResultRow(row));
            }
        }
    }

    /**
     * Queries the MySQL database for items matching the search keyword and filter.
     * Returns a list of String arrays: [name, category, type, date, location]
     */
    private List<String[]> fetchFromDatabase(String query, String filter) {
        List<String[]> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT name, category, type, date, location FROM items WHERE 1=1"
        );

        // Filter by type (Lost / Found)
        if (!filter.equals("All")) {
            sql.append(" AND type = ?");
        }

        // Filter by keyword in name or category
        if (!query.isEmpty()) {
            sql.append(" AND (name LIKE ? OR category LIKE ?)");
        }

        sql.append(" ORDER BY id DESC");  // newest first

        try (Connection conn = new DBConnectionTest().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;

            if (!filter.equals("All")) {
                ps.setString(idx++, filter);
            }

            if (!query.isEmpty()) {
                ps.setString(idx++, "%" + query + "%");
                ps.setString(idx++, "%" + query + "%");
            }

            ResultSet rs = ps.executeQuery();
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

    private Label colHeader(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setTextFill(Color.GRAY);
        label.setStyle("-fx-font-size:11px;");
        return label;
    }

    private HBox buildResultRow(String[] data) {
        HBox row = new HBox();
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(data[0]);
        name.setPrefWidth(250);
        name.setStyle("-fx-font-weight:bold;");

        Label category = new Label(data[1]);
        category.setPrefWidth(180);
        category.setTextFill(Color.GRAY);

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

    public void show() {
        stage.show();
    }
}