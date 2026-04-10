package view;

import controller.ItemController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// This page lets a user report an item they lost
// It extends BasePage - same structure as ReportFoundPage but for lost items
// Inheritance: ReportLostPage is a child of BasePage, inheriting sidebar and stage
public class ReportLostPage extends BasePage {

    // Constructor - stores the window and builds the page right away
    // stage goes into the inherited field from BasePage
    public ReportLostPage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    // Sets up the page layout with sidebar on the left and form in the center
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // buildSidebar() is inherited from BasePage - highlights "Report Lost" in the menu
        root.setLeft(buildSidebar("Report Lost"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Report Lost Item");
        stage.setScene(scene);
    }

    // Builds the scrollable center form for reporting a lost item
    // Very similar to ReportFoundPage but the item type passed to the DB is "Lost"
    private ScrollPane buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Report Lost Item");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        Label subtitle = new Label("Fill in the details of the item you lost.");
        subtitle.setTextFill(Color.GRAY);

        // White form box holding all the input fields
        VBox form = new VBox(15);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        // Text field for the item name
        // createField() is reused - abstraction hides the basic field setup
        TextField itemNameField = createField("Item Name");

        // Dropdown to pick which category the lost item belongs to
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Electronics", "Accessories", "Bags", "Personal", "Documents", "Other");
        categoryBox.setPromptText("Select Category");
        categoryBox.setPrefWidth(Double.MAX_VALUE);

        // Where the user last had the item before losing it
        TextField locationField = createField("Last Seen Location");

        // Date picker for when the item was lost
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date Lost");
        datePicker.setPrefWidth(Double.MAX_VALUE);

        // Multi-line box for extra details like color, brand, or identifying marks
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description (color, brand, size, etc.)");
        descriptionArea.setPrefRowCount(4);

        // Optional contact number if someone finds the item and wants to reach the owner
        TextField contactField = createField("Contact Number");

        // Status label updates after submission to show success or error
        Label statusLabel = new Label();

        Button submitBtn = new Button("Submit Report");
        submitBtn.setPrefWidth(200);
        submitBtn.setStyle(
            "-fx-background-color:#7fd1d8;" +
            "-fx-text-fill:black;" +
            "-fx-font-size:14px;" +
            "-fx-background-radius:8;" +
            "-fx-padding:10;"
        );

        // What happens when the user clicks Submit
        submitBtn.setOnAction(_ -> {

            // Validate that all required fields are filled before saving
            if (itemNameField.getText().isEmpty() || categoryBox.getValue() == null
                    || locationField.getText().isEmpty() || datePicker.getValue() == null) {
                statusLabel.setText("Please fill in all required fields.");
                statusLabel.setTextFill(Color.RED);
            } else {
                // Send the form data to the controller to save in the database
                // "Lost" is passed as the type - this is what tells the DB it's a lost item
                // Abstraction: the controller handles all the SQL, we just call reportItem()
                ItemController itemController = new ItemController();
                boolean success = itemController.reportItem(
                    itemNameField.getText(),
                    categoryBox.getValue(),
                    datePicker.getValue().toString(),
                    locationField.getText(),
                    descriptionArea.getText(),
                    "Lost"
                );

                if (success) {
                    // Report saved - show green message and clear the form for next use
                    statusLabel.setText("Lost item reported successfully!");
                    statusLabel.setTextFill(Color.GREEN);
                    itemNameField.clear();
                    categoryBox.setValue(null);
                    locationField.clear();
                    datePicker.setValue(null);
                    descriptionArea.clear();
                    contactField.clear();
                } else {
                    // DB save failed for some reason - ask user to try again
                    statusLabel.setText("Failed to submit. Please try again.");
                    statusLabel.setTextFill(Color.RED);
                }
            }
        });

        // Add all labels and input fields to the form in order
        // formLabel() is reused for each field label - same style every time
        form.getChildren().addAll(
            formLabel("Item Name *"),          itemNameField,
            formLabel("Category *"),           categoryBox,
            formLabel("Last Seen Location *"), locationField,
            formLabel("Date Lost *"),          datePicker,
            formLabel("Description"),          descriptionArea,
            formLabel("Contact Number"),       contactField,
            statusLabel, submitBtn
        );

        content.getChildren().addAll(title, subtitle, form);

        // Wrap in ScrollPane so the form is still usable on smaller windows
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:#f0f9fa;");
        return scrollPane;
    }

    // Creates a basic text field with placeholder text
    // Encapsulation: field creation is hidden in one place, not repeated throughout the class
    private TextField createField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    // Creates a bold label to go above each input field in the form
    // Abstraction: callers just pass the text, styling is handled inside here
    private Label formLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        return label;
    }

    // Overrides the abstract show() method from BasePage
    // BasePage forces every child page to have this method - that's abstraction in action
    @Override
    public void show() {
        showStage();
    }
}