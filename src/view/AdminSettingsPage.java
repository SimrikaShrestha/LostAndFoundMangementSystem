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

public class AdminSettingsPage {
    private Stage stage;
    private String adminName;

    public AdminSettingsPage(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5f7;");
        root.setLeft(AdminSidebar.build(stage, "settings", adminName));

        VBox content = new VBox(20);
        content.setPadding(new Insets(30, 36, 36, 36));

        Label title = new Label("System Settings");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        VBox form = new VBox(14);
        form.setPadding(new Insets(24));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        form.setMaxWidth(500);

        Label formTitle = new Label("Admin Profile");
        formTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        TextField nameField = new TextField(adminName);
        nameField.setStyle(fieldStyle());
        TextField emailField = new TextField();
        emailField.setPromptText("Admin Email");
        emailField.setStyle(fieldStyle());

        PasswordField passField = new PasswordField();
        passField.setPromptText("New Password");
        passField.setStyle(fieldStyle());
        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirm Password");
        confirmField.setStyle(fieldStyle());

        Label statusLabel = new Label();

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-background-color: #5bc8d0; " +
                "-fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
        saveBtn.setOnAction(_ -> {
            if (!passField.getText().isEmpty() && !passField.getText().equals(confirmField.getText())) {
                statusLabel.setText("Passwords do not match.");
                statusLabel.setTextFill(Color.RED);
            } else {
                statusLabel.setText("Settings saved successfully!");
                statusLabel.setTextFill(Color.web("#166534"));
            }
        });

        form.getChildren().addAll(
            formTitle,
            lbl("Full Name"), nameField,
            lbl("Email"), emailField,
            lbl("New Password"), passField,
            lbl("Confirm Password"), confirmField,
            statusLabel, saveBtn
        );

        content.getChildren().addAll(title, form);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f5f7; -fx-background: #f0f5f7;");
        root.setCenter(scroll);

        stage.setScene(new Scene(root, 1100, 700));
        stage.setTitle("Admin — System Settings");
        stage.show();
    }

    private Label lbl(String t) { Label l = new Label(t); l.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #4a6b73;"); return l; }
    private String fieldStyle() { return "-fx-font-size: 13px; -fx-background-radius: 8; -fx-border-color: #c5d8dc; -fx-border-radius: 8; -fx-padding: 9 12;"; }
}