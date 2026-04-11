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

// This page lets a user report an item they found
// It extends BasePage to inherit the sidebar, stage, and showStage()
// Inheritance: ReportFoundPage is a child of BasePage - shared logic lives there
public class ReportFoundPage extends BasePage {

    // Constructor - stores the window and builds the page immediately
    // stage is assigned to the inherited field from BasePage
    public ReportFoundPage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    // Sets up the full page with sidebar on the left and the form in the center
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // buildSidebar() is inherited from BasePage - no need to rewrite it here
        root.setLeft(buildSidebar("Report Found"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Report Found Item");
        stage.setScene(scene);
    }

    // Builds the scrollable form for reporting a found item
    // Returns a ScrollPane so the form doesn't get cut off on small screens
    private ScrollPane buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Report Found Item");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        Label subtitle = new Label("Fill in the details of the item you found.");
        subtitle.setTextFill(Color.GRAY);

        // White form box that holds all the input fields
        VBox form = new VBox(15);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        // Text field for the item name
        // createField() is reused here - abstraction hides the field setup details
        TextField itemNameField = createField("Item Name");

        // Dropdown for picking the item category
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Electronics", "Accessories", "Bags", "Personal", "Documents", "Other");
        categoryBox.setPromptText("Select Category");
        categoryBox.setPrefWidth(Double.MAX_VALUE);

        // Where the user found the item
        TextField locationField = createField("Where Found");

        // Date picker so user can select the exact date they found the item
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date Found");
        datePicker.setPrefWidth(Double.MAX_VALUE);

        // Multi-line text box for extra details like color, brand, size
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description (color, brand, size, etc.)");
        descriptionArea.setPrefRowCount(4);

        // Optional contact number field
        TextField contactField = createField("Contact Number");

        // Status label shows success or error after form submission
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

            // Check that all required fields are filled before trying to save
            if (itemNameField.getText().isEmpty() || categoryBox.getValue() == null
                    || locationField.getText().isEmpty() || datePicker.getValue() == null) {
                statusLabel.setText("Please fill in all required fields.");
                statusLabel.setTextFill(Color.RED);
            } else {
                // Pass the form data to the controller to save in the database
                // Abstraction: we don't know how it saves, we just call reportItem()
                // "Found" is passed as the type so the DB knows this is a found item report
                ItemController itemController = new ItemController();
                boolean success = itemController.reportItem(
                    itemNameField.getText(),
                    categoryBox.getValue(),
                    datePicker.getValue().toString(),
                    locationField.getText(),
                    descriptionArea.getText(),
                    "Found"
                );

                if (success) {
                    // Show success message and clear all fields so user can report another item
                    statusLabel.setText("Found item reported successfully!");
                    statusLabel.setTextFill(Color.GREEN);
                    itemNameField.clear();
                    categoryBox.setValue(null);
                    locationField.clear();
                    datePicker.setValue(null);
                    descriptionArea.clear();
                    contactField.clear();
                } else {
                    // Something went wrong on the DB side
                    statusLabel.setText("Failed to submit. Please try again.");
                    statusLabel.setTextFill(Color.RED);
                }
            }
        });

        // Add all field labels and inputs to the form in order
        // formLabel() is reused for every label above a field - keeps styling consistent
        form.getChildren().addAll(
            formLabel("Item Name *"),    itemNameField,
            formLabel("Category *"),     categoryBox,
            formLabel("Where Found *"),  locationField,
            formLabel("Date Found *"),   datePicker,
            formLabel("Description"),    descriptionArea,
            formLabel("Contact Number"), contactField,
            statusLabel, submitBtn
        );

        content.getChildren().addAll(title, subtitle, form);

        // Wrap the content in a scroll pane for smaller screens
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:#f0f9fa;");
        return scrollPane;
    }

    // Creates a text field with a placeholder prompt text
    // Encapsulation: field creation is hidden here so we don't repeat setup code
    private TextField createField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    // Creates a bold label shown above each form input field
    // Abstraction: styling is locked inside here, not repeated for every single field
    private Label formLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        return label;
    }

    // Overrides the abstract show() method required by BasePage
    // Every child class must provide its own show() - this is how abstract methods work
    @Override
    public void show() {
        showStage();
    }
}