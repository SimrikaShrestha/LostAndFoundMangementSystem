package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StaffSettingsPage {

    private Stage stage;
    private String staffName;

    public StaffSettingsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Settings"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Settings");
        stage.setScene(scene);
    }

    private ScrollPane buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Settings");
        title.setStyle("-fx-font-size:22px; -fx-font-weight:bold;");

        VBox form = new VBox(15);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        TextField nameField = new TextField(staffName);
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        PasswordField passField = new PasswordField();
        passField.setPromptText("New Password");
        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirm Password");
        Label statusLabel = new Label();

        Button saveBtn = new Button("Save Changes");
        saveBtn.setPrefWidth(200);
        saveBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-font-size:14px; -fx-background-radius:8; -fx-padding:10;");
        saveBtn.setOnAction(_ -> {
            if (!passField.getText().isEmpty() && !passField.getText().equals(confirmField.getText())) {
                statusLabel.setText("Passwords do not match.");
                statusLabel.setTextFill(Color.RED);
            } else {
                statusLabel.setText("Settings saved successfully!");
                statusLabel.setTextFill(Color.GREEN);
            }
        });

        form.getChildren().addAll(
                label("Full Name"), nameField,
                label("Email"), emailField,
                label("Phone"), phoneField,
                label("New Password"), passField,
                label("Confirm Password"), confirmField,
                statusLabel, saveBtn
        );

        content.getChildren().addAll(title, form);
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#f0f9fa;");
        return sp;
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        return l;
    }

    public void show() { stage.show(); }
}