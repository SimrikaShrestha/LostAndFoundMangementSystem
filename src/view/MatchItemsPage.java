package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MatchItemsPage {

    private Stage stage;
    private String staffName;

    public MatchItemsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Match Items"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Match Items");
        stage.setScene(scene);
    }

    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Match Items");
        title.setStyle("-fx-font-size:22px; -fx-font-weight:bold;");
        Label subtitle = new Label("Match lost items with found items.");
        subtitle.setTextFill(Color.GRAY);

        HBox matchArea = new HBox(20);
        VBox lostBox = buildItemListBox("Lost Items", new String[][]{
                {"iPhone 13", "Electronics", "Gym", "Feb 10, 2026"},
                {"Black Dell Backpack", "Bags", "Library", "Jan 15, 2026"},
                {"Dorm Keys", "Personal", "Dorm A", "Feb 18, 2026"}
        });
        VBox foundBox = buildItemListBox("Found Items", new String[][]{
                {"iPhone 13 Black", "Electronics", "Gym Locker", "Feb 11, 2026"},
                {"Dell Backpack", "Bags", "Library Desk", "Jan 16, 2026"},
                {"Key Ring", "Personal", "Dorm B", "Feb 19, 2026"}
        });

        VBox matchResultBox = new VBox(10);
        matchResultBox.setPrefWidth(250);
        matchResultBox.setPadding(new Insets(20));
        matchResultBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");
        Label matchTitle = new Label("Match Result");
        matchTitle.setStyle("-fx-font-size:15px; -fx-font-weight:bold;");
        Label matchInfo = new Label("Select one item from each list and click Match.");
        matchInfo.setWrapText(true);
        matchInfo.setTextFill(Color.GRAY);
        Label matchScore = new Label();
        Button matchBtn = new Button("Match Selected");
        matchBtn.setPrefWidth(200);
        matchBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-background-radius:8; -fx-padding:10;");
        matchBtn.setOnAction(_ -> {
            matchScore.setText(" 94% Confidence Match!\nReady to approve claim.");
            matchScore.setTextFill(Color.GREEN);
            matchScore.setStyle("-fx-font-size:13px;");
        });
        matchResultBox.getChildren().addAll(matchTitle, matchInfo, matchScore, matchBtn);
        matchArea.getChildren().addAll(lostBox, foundBox, matchResultBox);
        content.getChildren().addAll(title, subtitle, matchArea);
        return content;
    }

    private VBox buildItemListBox(String heading, String[][] items) {
        VBox box = new VBox(8);
        box.setPrefWidth(300);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color:white; -fx-background-radius:10;");
        Label title = new Label(heading);
        title.setStyle("-fx-font-size:15px; -fx-font-weight:bold;");
        box.getChildren().add(title);
        box.getChildren().add(new Separator());
        ToggleGroup group = new ToggleGroup();
        for (String[] item : items) {
            RadioButton rb = new RadioButton(item[0]);
            rb.setToggleGroup(group);
            VBox itemBox = new VBox(2);
            Label sub = new Label(item[1] + " • " + item[2] + " • " + item[3]);
            sub.setTextFill(Color.GRAY);
            sub.setStyle("-fx-font-size:11px;");
            itemBox.getChildren().addAll(rb, sub);
            box.getChildren().add(itemBox);
        }
        return box;
    }

    public void show() {
        stage.show();
    }
}