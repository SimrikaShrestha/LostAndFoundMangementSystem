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


public class DashboardPage extends BasePage {

    private DashboardController controller;

    public DashboardPage(Stage stage) {
        
        this.stage = stage;
        this.controller = new DashboardController();
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

       
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

        int reported = controller.getItemsReported();
        int found    = controller.getItemsFound();
        int claims   = controller.getActiveClaims();

        String successRate = reported > 0 ? (found * 100 / reported) + "% success rate" : "No items yet";
        String weeklyNote  = reported > 0 ? "+" + reported + " total reported" : "No reports yet";
        String claimsNote  = claims  > 0 ? "Action required" : "No active claims";

        HBox statsBox = new HBox(15);
        statsBox.getChildren().addAll(
            buildStatCard("ITEMS REPORTED", String.valueOf(reported), weeklyNote),
            buildStatCard("ITEMS FOUND",    String.valueOf(found),    successRate),
            buildStatCard("ACTIVE CLAIMS",  String.valueOf(claims),   claimsNote)
        );

        VBox tableBox = new VBox(10);
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10; -fx-padding:20;");

        HBox tableHeader = new HBox();
        Label tableTitle = new Label("Recent Activities");
        tableTitle.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label viewAll = new Label("View all");
        viewAll.setTextFill(Color.web("#7fd1d8"));
        tableHeader.getChildren().addAll(tableTitle, spacer, viewAll);

        HBox colHeaders = new HBox();
        colHeaders.setPadding(new Insets(5, 0, 5, 0));
        colHeaders.getChildren().addAll(
            tableColHeader("ITEM NAME", 250),
            tableColHeader("CATEGORY",  200),
            tableColHeader("DATE",      150),
            tableColHeader("STATUS",    100)
        );

        tableBox.getChildren().addAll(tableHeader, colHeaders, new Separator());

        List<Item> recentItems = controller.getRecentActivities();
        if (recentItems.isEmpty()) {
            Label noItems = new Label("No recent activities found.");
            noItems.setTextFill(Color.GRAY);
            noItems.setPadding(new Insets(10, 0, 0, 0));
            tableBox.getChildren().add(noItems);
        } else {
            for (Item item : recentItems) {
                tableBox.getChildren().add(buildTableRow(item));
            }
        }

        content.getChildren().addAll(welcome, subtitle, statsBox, tableBox);
        return content;
    }

    private VBox buildStatCard(String title, String value, String note) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.GRAY);
        titleLabel.setStyle("-fx-font-size:11px;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size:28px; -fx-font-weight:bold;");

        Label noteLabel = new Label(note);
        noteLabel.setTextFill(Color.GRAY);
        noteLabel.setStyle("-fx-font-size:11px;");

        card.getChildren().addAll(titleLabel, valueLabel, noteLabel);
        return card;
    }

    private Label tableColHeader(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setTextFill(Color.GRAY);
        label.setStyle("-fx-font-size:11px;");
        return label;
    }

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

        Label status = new Label(item.getStatus());
        status.setPadding(new Insets(4, 10, 4, 10));
        status.setStyle(getStatusStyle(item.getStatus()));

        row.getChildren().addAll(name, category, date, status);
        return row;
    }

    private String getStatusStyle(String status) {
        return switch (status) {
            case "Processing" -> "-fx-background-color:#dce8ff; -fx-text-fill:#3366cc; -fx-background-radius:5;";
            case "Returned"   -> "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;";
            case "In Review"  -> "-fx-background-color:#fff3cd; -fx-text-fill:#856404; -fx-background-radius:5;";
            case "Searching"  -> "-fx-background-color:#f0f0f0; -fx-text-fill:#333333; -fx-background-radius:5;";
            default           -> "-fx-background-color:#f0f0f0; -fx-text-fill:black;   -fx-background-radius:5;";
        };
    }

   
    @Override
    public void show() {
        stage.show();
    }
}