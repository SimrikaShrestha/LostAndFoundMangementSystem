package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DBClaims;
import model.SessionManager;

import java.util.List;

// This page shows all available found items that the logged-in user can claim
// It extends BasePage so it inherits the sidebar, stage, and showStage() method
// Inheritance: MyClaimsPage is a child of BasePage - shared logic lives there
public class MyClaimsPage extends BasePage {

    // Constructor - stores the window reference and builds the page right away
    // stage comes from BasePage (inherited field), we just assign it here
    public MyClaimsPage(Stage stage) {
        this.stage = stage;
        buildPage();
    }

    // Sets up the full page layout with sidebar on the left and content in the center
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // buildSidebar() is inherited from BasePage - no need to rewrite it here
        root.setLeft(buildSidebar("My Claims"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("My Claims");
        stage.setScene(scene);
    }

    // Builds the main content area with title, subtitle, and the found items table
    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("My Claims");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        Label subtitle = new Label("Browse found items and send a claim request.");
        subtitle.setTextFill(Color.GRAY);

        // White box that holds the full table of found items
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        // Column header row at the top of the table
        // Encapsulation: colHeader() hides the label styling, we just pass the text and width
        HBox colHeaders = new HBox();
        colHeaders.getChildren().addAll(
            colHeader("ITEM NAME",   220),
            colHeader("CATEGORY",    150),
            colHeader("DATE FOUND",  130),
            colHeader("LOCATION",    180),
            colHeader("",            120)
        );
        tableBox.getChildren().addAll(colHeaders, new Separator());

        // Get the current logged-in user's ID from the session
        // SessionManager is a singleton - only one instance exists for the whole app
        int currentUserId = SessionManager.getInstance().getCurrentUser().getId();

        // Ask the database for all found items that this user hasn't already claimed
        List<DBClaims.FoundItem> items = DBClaims.getFoundItems(currentUserId);

        if (items.isEmpty()) {
            // No found items available - show a simple message instead of empty table
            Label noData = new Label("No found items available to claim.");
            noData.setTextFill(Color.GRAY);
            noData.setPadding(new Insets(10, 0, 0, 0));
            tableBox.getChildren().add(noData);
        } else {
            // Build and add a row for each found item
            for (DBClaims.FoundItem item : items) {
                HBox row = buildItemRow(item, currentUserId, tableBox, items);
                tableBox.getChildren().addAll(row, new Separator());
            }
        }

        content.getChildren().addAll(title, subtitle, tableBox);
        return content;
    }

    // Builds one row for a found item, including a Claim button on the right
    // If the user already claimed this item, the button is grayed out and disabled
    // Encapsulation: all the row-building logic is hidden inside this method
    private HBox buildItemRow(DBClaims.FoundItem item, int userId, VBox tableBox, List<DBClaims.FoundItem> items) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        // Show a dash if any field is missing from the database
        Label name     = rowCell(item.name,                                        220);
        Label category = rowCell(item.category != null ? item.category : "-",      150);
        Label date     = rowCell(item.date     != null ? item.date     : "-",      130);
        Label location = rowCell(item.location != null ? item.location : "-",      180);

        // Spacer pushes the claim button all the way to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Check the DB to see if this user already sent a claim for this item
        boolean alreadyClaimed = DBClaims.alreadyClaimed(userId, item.id);

        // Button label and state changes based on whether a claim already exists
        Button claimBtn = new Button(alreadyClaimed ? "Requested" : "Claim");
        claimBtn.setPrefWidth(110);
        claimBtn.setDisable(alreadyClaimed);

        if (alreadyClaimed) {
            // Gray style for already-claimed items - user can't click it again
            claimBtn.setStyle(
                "-fx-background-color:#cccccc;" +
                "-fx-text-fill:#666666;" +
                "-fx-background-radius:8;"
            );
        } else {
            // Teal style for items that can still be claimed
            claimBtn.setStyle(
                "-fx-background-color:#7fd1d8;" +
                "-fx-text-fill:black;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;"
            );

            // When user clicks Claim, send the request to the DB and update the button immediately
            claimBtn.setOnAction(_ -> {
                boolean success = DBClaims.sendClaim(userId, item.id);
                if (success) {
                    // Disable the button right away so user can't claim twice
                    claimBtn.setText("Requested");
                    claimBtn.setDisable(true);
                    claimBtn.setStyle(
                        "-fx-background-color:#cccccc;" +
                        "-fx-text-fill:#666666;" +
                        "-fx-background-radius:8;"
                    );
                    showAlert(Alert.AlertType.INFORMATION, "Claim Sent",
                        "Your claim request for '" + item.name + "' has been submitted.");
                } else {
                    // Something went wrong on the DB side - tell the user to try again
                    showAlert(Alert.AlertType.ERROR, "Failed",
                        "Could not submit claim. Please try again.");
                }
            });
        }

        row.getChildren().addAll(name, category, date, location, spacer, claimBtn);
        return row;
    }

    // Shows a popup dialog with a given type, title, and message
    // Abstraction: callers don't deal with Alert setup, just pass what they need to say
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Creates a small gray label for table column headers
    // Encapsulation: styling is hidden here, callers just pass text and width
    private Label colHeader(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font-size:11px;");
        return l;
    }

    // Creates a regular text label for table data cells
    // If null is passed somehow, it still won't crash - the label just shows nothing
    private Label rowCell(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setStyle("-fx-font-size:13px;");
        return l;
    }

    // Overrides the abstract show() from BasePage
    // Every child page must implement this - this is how abstract methods work
    @Override
    public void show() {
        showStage();
    }
}