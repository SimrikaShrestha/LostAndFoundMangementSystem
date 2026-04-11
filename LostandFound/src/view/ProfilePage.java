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

// This page lets the logged-in user view and update their personal profile info
// It extends BasePage to inherit the sidebar, stage, and showStage()
// Inheritance: ProfilePage is a child of BasePage, reusing shared page structure
public class ProfilePage extends BasePage {

    // Constructor - takes the window, assigns it to the inherited stage field, then builds the page
    public ProfilePage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    // Sets up the page layout with the sidebar on the left and profile form in the center
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // buildSidebar() comes from BasePage - inherited, not rewritten
        root.setLeft(buildSidebar("Profile"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Profile");
        stage.setScene(scene);
    }

    // Builds the scrollable center section with the profile form
    // Returns a ScrollPane so the form doesn't get cut off on smaller screens
    private ScrollPane buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("My Profile");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        Label subtitle = new Label("Manage your personal information.");
        subtitle.setTextFill(Color.GRAY);

        // White form box that holds all the input fields
        VBox form = new VBox(15);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        // Text fields for editable profile information
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        // Username is shown but locked - users are not allowed to change it
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setDisable(true);

        // Password fields - only filled in if user wants to change their password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New Password");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        // Status label shows success or error message after saving
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

        // When Save is clicked, check if the two password fields match before saving
        // If passwords don't match, show red error - otherwise show green success
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

        // Add all labels and fields to the form in order
        // formLabel() is reused for every field label - abstraction keeps it clean
        form.getChildren().addAll(
            formLabel("Full Name"),                  fullNameField,
            formLabel("Email"),                      emailField,
            formLabel("Phone"),                      phoneField,
            formLabel("Address"),                    addressField,
            formLabel("Username (cannot change)"),   usernameField,
            formLabel("New Password"),               passwordField,
            formLabel("Confirm Password"),           confirmPasswordField,
            statusLabel, saveBtn
        );

        content.getChildren().addAll(title, subtitle, form);

        // Wrap everything in a scroll pane in case the screen is too short
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:#f0f9fa;");
        return scrollPane;
    }

    // Creates a bold label used above each form field
    // Encapsulation: the label styling is locked in here, not repeated for every field
    private Label formLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        return label;
    }

    // Overrides the abstract show() method required by BasePage
    // Abstract methods force every child class to provide their own version of show()
    @Override
    public void show() {
        showStage();
    }
}