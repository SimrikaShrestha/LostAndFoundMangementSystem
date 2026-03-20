package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MyClaimsPage {

    private Stage stage;

    public MyClaimsPage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new DashboardPage(stage).buildSidebar("My Claims"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("My Claims");
        stage.setScene(scene);
    }

    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("My Claims");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold;");

        Label subtitle = new Label("Track the status of your claims.");
        subtitle.setTextFill(Color.GRAY);

        VBox claimsBox = new VBox(10);
        claimsBox.setPadding(new Insets(20));
        claimsBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        HBox colHeaders = new HBox();
        colHeaders.getChildren().addAll(
                colHeader("ITEM NAME", 250),
                colHeader("CATEGORY", 180),
                colHeader("CLAIMED ON", 150),
                colHeader("STATUS", 120)
        );

        claimsBox.getChildren().addAll(colHeaders, new Separator());

        String[][] claims = {
                {"iPhone 13", "Electronics", "Feb 25, 2026", "Processing"},
                {"Leather Wallet", "Accessories", "Feb 23, 2026", "Returned"},
                {"Blue Backpack", "Bags", "Feb 21, 2026", "In Review"}
        };

        for (String[] claim : claims) {
            claimsBox.getChildren().add(buildClaimRow(claim));
        }

        content.getChildren().addAll(title, subtitle, claimsBox);
        return content;
    }

    private Label colHeader(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setTextFill(Color.GRAY);
        label.setStyle("-fx-font-size:11px;");
        return label;
    }

    private HBox buildClaimRow(String[] data) {
        HBox row = new HBox();
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(data[0]);
        name.setPrefWidth(250);
        name.setStyle("-fx-font-weight:bold;");

        Label category = new Label(data[1]);
        category.setPrefWidth(180);
        category.setTextFill(Color.GRAY);

        Label date = new Label(data[2]);
        date.setPrefWidth(150);
        date.setTextFill(Color.GRAY);

        Label status = new Label(data[3]);
        status.setPadding(new Insets(3, 8, 3, 8));
        status.setStyle(switch (data[3]) {
            case "Processing" -> "-fx-background-color:#dce8ff; -fx-text-fill:#3366cc; -fx-background-radius:5;";
            case "Returned" -> "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;";
            case "In Review" -> "-fx-background-color:#fff3cd; -fx-text-fill:#856404; -fx-background-radius:5;";
            default -> "-fx-background-color:#f0f0f0; -fx-text-fill:black; -fx-background-radius:5;";
        });

        row.getChildren().addAll(name, category, date, status);
        return row;
    }

    public void show() {
        stage.show();
    }
}