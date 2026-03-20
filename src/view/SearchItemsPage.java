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
                colHeader("CONTACT", 150)
        );

        resultsBox.getChildren().addAll(resultsTitle, colHeaders, new Separator());

        String[][] sampleData = {
                {"iPhone 13", "Electronics", "Lost", "Feb 24, 2026", "9800000001"},
                {"Leather Wallet", "Accessories", "Found", "Feb 22, 2026", "9800000002"},
                {"Blue Backpack", "Bags", "Lost", "Feb 20, 2026", "9800000003"},
                {"Dorm Keys", "Personal", "Found", "Feb 18, 2026", "9800000004"}
        };

        for (String[] row : sampleData) {
            resultsBox.getChildren().add(buildResultRow(row));
        }

        searchBtn.setOnAction(_ -> {
            resultsBox.getChildren().removeIf(node -> node instanceof HBox && ((HBox) node).getChildren().size() == 5);
            String query = searchField.getText().toLowerCase();
            for (String[] row : sampleData) {
                if (query.isEmpty() || row[0].toLowerCase().contains(query) || row[1].toLowerCase().contains(query)) {
                    resultsBox.getChildren().add(buildResultRow(row));
                }
            }
        });

        content.getChildren().addAll(title, subtitle, searchBox, resultsBox);
        return content;
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
        status.setStyle(data[2].equals("Lost")
                ? "-fx-background-color:#ffe0e0; -fx-text-fill:#cc0000; -fx-background-radius:5;"
                : "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;"
        );

        Label date = new Label(data[3]);
        date.setPrefWidth(150);
        date.setTextFill(Color.GRAY);

        Label contact = new Label(data[4]);
        contact.setPrefWidth(150);
        contact.setTextFill(Color.GRAY);

        row.getChildren().addAll(name, category, status, date, contact);
        return row;
    }

    public void show() {
        stage.show();
    }
}