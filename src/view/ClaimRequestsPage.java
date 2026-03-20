package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClaimRequestsPage {

    private Stage stage;
    private String staffName;

    public ClaimRequestsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Claim Requests"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Claim Requests");
        stage.setScene(scene);
    }

    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Claim Requests");
        title.setStyle("-fx-font-size:22px; -fx-font-weight:bold;");

        VBox tableBox = new VBox(0);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        HBox colHeaders = new HBox();
        colHeaders.setPadding(new Insets(0, 0, 10, 0));
        colHeaders.getChildren().addAll(
                colHeader("ITEM", 220),
                colHeader("CLAIMANT", 160),
                colHeader("DATE", 130),
                colHeader("MATCH", 120),
                colHeader("STATUS", 120),
                colHeader("ACTIONS", 160)
        );
        tableBox.getChildren().addAll(colHeaders, new Separator());

        String[][] claims = {
                {"Black Dell Backpack", "James Wilson", "Jan 15, 2026", "94%", "Pending"},
                {"HydroFlask Blue", "Alice Chen", "Jan 28, 2026", "78%", "Pending"},
                {"MacBook Pro 14\"", "Ryan Smith", "Feb 05, 2026", "98%", "Approved"},
                {"iPhone 13", "Simrika Shrestha", "Feb 10, 2026", "85%", "Rejected"},
                {"Leather Wallet", "Unisha Balami", "Feb 12, 2026", "91%", "Pending"}
        };

        for (String[] claim : claims) {
            tableBox.getChildren().add(buildClaimRow(claim));
        }

        content.getChildren().addAll(title, tableBox);
        return content;
    }

    private HBox buildClaimRow(String[] data) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(12, 0, 12, 0));
        row.setAlignment(Pos.CENTER_LEFT);

        Label item = new Label(data[0]);
        item.setPrefWidth(220);
        item.setStyle("-fx-font-weight:bold;");

        Label claimant = new Label(data[1]);
        claimant.setPrefWidth(160);
        claimant.setTextFill(Color.GRAY);

        Label date = new Label(data[2]);
        date.setPrefWidth(130);
        date.setTextFill(Color.GRAY);

        Label match = new Label(data[3]);
        match.setPrefWidth(120);
        match.setTextFill(Color.GRAY);

        Label status = new Label(data[4]);
        status.setPrefWidth(120);
        status.setPadding(new Insets(3, 8, 3, 8));
        status.setStyle(switch (data[4]) {
            case "Approved" -> "-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;";
            case "Rejected" -> "-fx-background-color:#ffe0e0; -fx-text-fill:#cc0000; -fx-background-radius:5;";
            default -> "-fx-background-color:#fff3cd; -fx-text-fill:#856404; -fx-background-radius:5;";
        });

        HBox actions = new HBox(8);

        if (data[4].equals("Pending")) {
            Button approveBtn = new Button("Approve");
            approveBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-background-radius:6; -fx-padding:4 10 4 10;");
            Button rejectBtn = new Button("Reject");
            rejectBtn.setStyle("-fx-background-color:transparent; -fx-text-fill:#cc0000; -fx-background-radius:6; -fx-padding:4 10 4 10;");

            approveBtn.setOnAction(_ -> {
                status.setText("Approved");
                status.setStyle("-fx-background-color:#d4edda; -fx-text-fill:#155724; -fx-background-radius:5;");
                actions.getChildren().clear();
            });
            rejectBtn.setOnAction(_ -> {
                status.setText("Rejected");
                status.setStyle("-fx-background-color:#ffe0e0; -fx-text-fill:#cc0000; -fx-background-radius:5;");
                actions.getChildren().clear();
            });
            actions.getChildren().addAll(approveBtn, rejectBtn);
        }

        row.getChildren().addAll(item, claimant, date, match, status, actions);
        return row;
    }

    private Label colHeader(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font-size:11px;");
        return l;
    }

    public void show() {
        stage.show();
    }
}