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


public class ReportFoundPage extends BasePage {

    public ReportFoundPage(Stage stage) {
        // 'stage' is inherited from BasePage — no need to redeclare it
        this.stage = stage;
        buildPage();
    }

    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        
        root.setLeft(buildSidebar("Report Found"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Report Found Item");
        stage.setScene(scene);
    }

    private ScrollPane buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Report Found Item");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold;");

        Label subtitle = new Label("Fill in the details of the item you found.");
        subtitle.setTextFill(Color.GRAY);

        VBox form = new VBox(15);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        TextField itemNameField = createField("Item Name");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Electronics", "Accessories", "Bags", "Personal", "Documents", "Other");
        categoryBox.setPromptText("Select Category");
        categoryBox.setPrefWidth(Double.MAX_VALUE);

        TextField locationField = createField("Where Found");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date Found");
        datePicker.setPrefWidth(Double.MAX_VALUE);

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description (color, brand, size, etc.)");
        descriptionArea.setPrefRowCount(4);

        TextField contactField = createField("Contact Number");

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

        submitBtn.setOnAction(_ -> {
            if (itemNameField.getText().isEmpty() || categoryBox.getValue() == null
                    || locationField.getText().isEmpty() || datePicker.getValue() == null) {
                statusLabel.setText("Please fill in all required fields.");
                statusLabel.setTextFill(Color.RED);
            } else {
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
                    statusLabel.setText("Found item reported successfully!");
                    statusLabel.setTextFill(Color.GREEN);
                    itemNameField.clear();
                    categoryBox.setValue(null);
                    locationField.clear();
                    datePicker.setValue(null);
                    descriptionArea.clear();
                    contactField.clear();
                } else {
                    statusLabel.setText("Failed to submit. Please try again.");
                    statusLabel.setTextFill(Color.RED);
                }
            }
        });

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

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:#f0f9fa;");
        return scrollPane;
    }

    private TextField createField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    private Label formLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        return label;
    }

   
    @Override
    public void show() {
        stage.show();
    }
}