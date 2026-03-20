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

public class AllItemsPage {

    private Stage stage;
    private String staffName;

    public AllItemsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("All Items"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("All Items");
        stage.setScene(scene);
    }

    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("All Items");
        title.setStyle("-fx-font-size:22px; -fx-font-weight:bold;");

        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search items...");
        searchField.setPrefWidth(250);
        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All", "Lost", "Found");
        statusFilter.setValue("All");
        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("All", "Electronics", "Accessories", "Bags", "Personal", "Documents");
        categoryFilter.setValue("All");
        Button filterBtn = actionButton("Filter");
        filterBar.getChildren().addAll(searchField, statusFilter, categoryFilter, filterBtn);

        VBox tableBox = new VBox(0);
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");
        tableBox.setPadding(new Insets(20));

        HBox colHeaders = new HBox();
        colHeaders.setPadding(new Insets(0, 0, 10, 0));
        colHeaders.getChildren().addAll(
                colHeader("ITEM NAME", 220),
                colHeader("CATEGORY", 150),
                colHeader("STATUS", 100),
                colHeader("LOCATION", 180),
                colHeader("DATE", 130),
                colHeader("REPORTED BY", 130)
        );
        tableBox.getChildren().addAll(colHeaders, new Separator());

        String[][] items = {
                {"Black Dell Backpack", "Bags", "Lost", "Library", "Jan 15, 2026", "James Wilson"},
                {"HydroFlask Blue", "Personal", "Found", "Cafeteria", "Jan 28, 2026", "Alice Chen"},
                {"MacBook Pro 14\"", "Electronics", "Found", "Lab Room 3", "Feb 05, 2026", "Ryan Smith"},
                {"iPhone 13", "Electronics", "Lost", "Gym", "Feb 10, 2026", "Simrika Shrestha"},
                {"Leather Wallet", "Accessories", "Found", "Parking Lot", "Feb 12, 2026", "Unisha Balami"}
        };

        for (String[] item : items) {
            tableBox.getChildren().add(buildItemRow(item));
        }

        content.getChildren().addAll(title, filterBar, tableBox);
        return content;
    }

    private HBox buildItemRow(String[] data) {
        HBox row = new HBox();
        row.setPadding(new Insets(12, 0, 12, 0));
        row.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(data[0]);
        name.setPrefWidth(220);
        name.setStyle("-fx-font-weight:bold;");

        Label category = new Label(data[1]);
        category.setPrefWidth(150);
        category.setTextFill(Color.GRAY);

        Label status = new Label(data[2]);
        status.setPrefWidth(100);
        status.setPadding(new Insets(3, 8, 3, 8));
        status.setStyle(data[2].equals("Lost")
                ? "-fx-background-color:#ffe0e0; -fx-text-fill:#cc0000; -fx-background-radius:5;"
                : "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;"
        );

        Label location = new Label(data[3]);
        location.setPrefWidth(180);
        location.setTextFill(Color.GRAY);

        Label date = new Label(data[4]);
        date.setPrefWidth(130);
        date.setTextFill(Color.GRAY);

        Label reporter = new Label(data[5]);
        reporter.setPrefWidth(130);
        reporter.setTextFill(Color.GRAY);

        row.getChildren().addAll(name, category, status, location, date, reporter);
        return row;
    }

    private Label colHeader(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font-size:11px;");
        return l;
    }

    private Button actionButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-background-radius:8; -fx-padding:7 15 7 15;");
        return btn;
    }

    public void show() {
        stage.show();
    }
}