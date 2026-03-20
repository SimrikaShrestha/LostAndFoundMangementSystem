package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReportsPage {

    private Stage stage;
    private String staffName;

    public ReportsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Reports"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Reports");
        stage.setScene(scene);
    }

    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Reports");
        title.setStyle("-fx-font-size:22px; -fx-font-weight:bold;");
        Label subtitle = new Label("Overview of system activity.");
        subtitle.setTextFill(Color.GRAY);

        HBox statsBox = new HBox(15);
        statsBox.getChildren().addAll(
                buildStatCard("Total Items Reported", "382"),
                buildStatCard("Items Returned", "214"),
                buildStatCard("Pending Cases", "42"),
                buildStatCard("Success Rate", "75%")
        );

        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");
        Label tableTitle = new Label("Monthly Summary");
        tableTitle.setStyle("-fx-font-size:15px; -fx-font-weight:bold;");

        HBox colHeaders = new HBox();
        colHeaders.getChildren().addAll(
                colHeader("MONTH", 150), colHeader("LOST", 120),
                colHeader("FOUND", 120), colHeader("RETURNED", 130), colHeader("PENDING", 120)
        );
        tableBox.getChildren().addAll(tableTitle, colHeaders, new Separator());

        for (String[] row : new String[][]{
                {"January 2026","45","62","38","12"},
                {"February 2026","38","55","41","10"},
                {"March 2026","22","30","18","8"}}) {
            HBox tableRow = new HBox();
            tableRow.setPadding(new Insets(10, 0, 10, 0));
            double[] widths = {150,120,120,130,120};
            for (int i = 0; i < row.length; i++) {
                Label cell = new Label(row[i]);
                cell.setPrefWidth(widths[i]);
                cell.setTextFill(i == 0 ? Color.BLACK : Color.GRAY);
                if (i == 0) cell.setStyle("-fx-font-weight:bold;");
                tableRow.getChildren().add(cell);
            }
            tableBox.getChildren().add(tableRow);
        }

        content.getChildren().addAll(title, subtitle, statsBox, tableBox);
        return content;
    }

    private VBox buildStatCard(String title, String value) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setPrefWidth(180);
        card.setStyle("-fx-background-color:white; -fx-background-radius:10;");
        Label t = new Label(title); t.setTextFill(Color.GRAY); t.setStyle("-fx-font-size:11px;"); t.setWrapText(true);
        Label v = new Label(value); v.setStyle("-fx-font-size:26px; -fx-font-weight:bold;");
        card.getChildren().addAll(t, v);
        return card;
    }

    private Label colHeader(String text, double width) {
        Label l = new Label(text); l.setPrefWidth(width);
        l.setTextFill(Color.GRAY); l.setStyle("-fx-font-size:11px;");
        return l;
    }

    public void show() { stage.show(); }
}