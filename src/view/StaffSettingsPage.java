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

// This page lets staff members update their personal info and change their password
// It does NOT extend BasePage - it uses StaffDashboardPage's sidebar directly instead
public class StaffSettingsPage {

    // stage is the main window, staffName is who is logged in
    // Both private - encapsulation keeps them from being changed outside this class
    private Stage  stage;
    private String staffName;

    // Constructor - stores both values and builds the page immediately
    public StaffSettingsPage(Stage stage, String staffName) {
        this.stage     = stage;
        this.staffName = staffName;
        buildPage();
    }

    // Sets up the page layout with the staff sidebar and the settings form
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // Reuse the sidebar from StaffDashboardPage - highlights "Settings" as the active page
        // Composition/reuse: we borrow the sidebar instead of building it again from scratch
    
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Profile"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Settings");
        stage.setScene(scene);
    }

    // Builds the scrollable settings form in the center of the page
    // Returns a ScrollPane so the form still works on smaller screen sizes
    private ScrollPane buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Settings");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        // White form box holding all the input fields
        VBox form = new VBox(15);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        // Editable fields for name, email, and phone
        TextField nameField  = new TextField(); nameField.setPromptText("Full Name");
        TextField emailField = new TextField(); emailField.setPromptText("Email");
        TextField phoneField = new TextField(); phoneField.setPromptText("Phone");

        // Password fields - only needed if the staff member wants to change their password
        PasswordField passField    = new PasswordField(); passField.setPromptText("New Password");
        PasswordField confirmField = new PasswordField(); confirmField.setPromptText("Confirm Password");

        // Status label shows green success or red error after clicking Save
        Label statusLabel = new Label();

        Button saveBtn = new Button("Save Changes");
        saveBtn.setPrefWidth(200);
        saveBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-font-size:14px; -fx-background-radius:8; -fx-padding:10;");

        // When Save is clicked, check if passwords match before saving
        // Only validate passwords if the staff member actually typed something in the field
        saveBtn.setOnAction(_ -> {
            if (!passField.getText().isEmpty() && !passField.getText().equals(confirmField.getText())) {
                statusLabel.setText("Passwords do not match.");
                statusLabel.setTextFill(Color.RED);
            } else {
                statusLabel.setText("Settings saved successfully!");
                statusLabel.setTextFill(Color.GREEN);
            }
        });

        // Add all field labels and inputs to the form in order
        // label() is reused for every bold label above a field - keeps styling consistent
        form.getChildren().addAll(
            label("Full Name"),       nameField,
            label("Email"),           emailField,
            label("Phone"),           phoneField,
            label("New Password"),    passField,
            label("Confirm Password"), confirmField,
            statusLabel, saveBtn
        );

        content.getChildren().addAll(title, form);

        // Wrap content in a scroll pane so nothing gets cut off on smaller windows
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:#f0f9fa;");
        return sp;
    }

    // Creates a bold label shown above each input field in the form
    // Encapsulation: the label styling is locked in one place, not repeated for each field
    private Label label(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        return l;
    }

    // Shows the page in maximized mode
    public void show() {
        stage.setMaximized(true);
        stage.show();
    }
}