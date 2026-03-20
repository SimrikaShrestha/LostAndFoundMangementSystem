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

public class ProfilePage {

    private Stage stage;

    public ProfilePage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(new DashboardPage(stage).buildSidebar("Profile"));
        root.setCenter(buildMainContent());
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Profile");
        stage.setScene(scene);
    }

    private ScrollPane buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("My Profile");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold;");

        Label subtitle = new Label("Manage your personal information.");
        subtitle.setTextFill(Color.GRAY);

        VBox form = new VBox(15);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        TextField fullNameField = createField("Simrika Shrestha");
        TextField emailField = createField("simrika@gmail.com");
        TextField phoneField = createField("9800000000");
        TextField addressField = createField("Kathmandu, Nepal");
        TextField usernameField = createField("simrika");
        usernameField.setDisable(true);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        Label statusLabel = new Label();

        Button saveBtn = new Button("Save Changes");
        saveBtn.setPrefWidth(200);
        saveBtn.setStyle(
                "-fx-background-color:#7fd1d8;" +
                "-fx-text-fill:black;" +
                "-fx-font-size:14px;" +
                "-fx-background-radius:8;" +
                "-fx-padding:10;"
        );

        saveBtn.setOnAction(_ -> {
            if (!passwordField.getText().isEmpty() &&
                    !passwordField.getText().equals(confirmPasswordField.getText())) {
                statusLabel.setText("Passwords do not match.");
                statusLabel.setTextFill(Color.RED);
            } else {
                statusLabel.setText("Profile updated successfully!");
                statusLabel.setTextFill(Color.GREEN);
            }
        });

        form.getChildren().addAll(
                formLabel("Full Name"), fullNameField,
                formLabel("Email"), emailField,
                formLabel("Phone"), phoneField,
                formLabel("Address"), addressField,
                formLabel("Username (cannot change)"), usernameField,
                formLabel("New Password"), passwordField,
                formLabel("Confirm Password"), confirmPasswordField,
                statusLabel, saveBtn
        );

        content.getChildren().addAll(title, subtitle, form);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:#f0f9fa;");
        return scrollPane;
    }

    private TextField createField(String value) {
        TextField field = new TextField(value);
        return field;
    }

    private Label formLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        return label;
    }

    public void show() {
        stage.show();
    }
}