package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// This page shows all claim requests made by users
// Staff can approve or reject each claim from here
public class ClaimRequestsPage {

    // stage is the main window we display everything on
    // staffName keeps track of which staff member is logged in
    private Stage stage;
    private String staffName;

    // Constructor - we receive the window and staff name from wherever this page is opened
    // This is encapsulation: we keep stage and staffName private and only set them through this constructor
    public ClaimRequestsPage(Stage stage, String staffName) {
        this.stage = stage;
        this.staffName = staffName;
    }

    // This method builds and shows the full page on screen
    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // We reuse StaffDashboardPage just to get the sidebar - this is inheritance/reuse in action
        // We don't rewrite the sidebar, we just call it from the existing class
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Claim Requests"));

        // The main content (table of claims) goes in the center
        root.setCenter(buildMainContent());

        stage.setScene(new Scene(root, 950, 650));
        stage.setTitle("Claim Requests");
        stage.setMaximized(true);
        stage.show();
    }

    // This builds the center section of the page
    // It has a title at the top and the claims table below
    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Claim Requests");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        // This white box holds the entire table
        VBox tableBox = new VBox(0);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        // We manually create the column header row
        HBox colHeaders = new HBox();
        colHeaders.setPadding(new Insets(0, 0, 10, 0));
        colHeaders.getChildren().addAll(
            colHeader("ITEM",     220),
            colHeader("CLAIMANT", 160),
            colHeader("DATE",     120),
            colHeader("STATUS",   120),
            colHeader("ACTIONS",  200)
        );

        // Add headers and a divider line, then load the actual data rows
        tableBox.getChildren().addAll(colHeaders, new Separator());
        loadRows(tableBox);

        content.getChildren().addAll(title, tableBox);
        return content;
    }

    // This method fetches all claims from the database and adds a row for each one
    // It also clears old rows first so we can safely refresh the table
    private void loadRows(VBox tableBox) {

        // Remove everything after the header and separator line before reloading
        if (tableBox.getChildren().size() > 2) {
            tableBox.getChildren().remove(2, tableBox.getChildren().size());
        }

        // SQL joins three tables: claims, items, and users
        // We get the item name, the claimant's full name, the date, and the claim status
        String sql =
            "SELECT c.id, i.name AS item_name, u.fullname AS claimant, " +
            "       c.claimed_date, c.status " +
            "FROM claims c " +
            "JOIN items i ON c.item_id = i.id " +
            "JOIN users u ON c.user_id = u.id " +
            "ORDER BY c.claimed_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean hasRows = false;

            // Loop through each claim result and build a row for it
            while (rs.next()) {
                hasRows = true;
                int    claimId  = rs.getInt("id");
                String itemName = rs.getString("item_name");
                String claimant = rs.getString("claimant");
                String date     = rs.getString("claimed_date");
                String status   = rs.getString("status");

                HBox row = new HBox(0);
                row.setPadding(new Insets(10, 0, 10, 0));

                // Light bottom border to separate rows visually
                row.setStyle("-fx-border-color: transparent transparent #f0f0f0 transparent;");

                // Approve button - green color
                Button approveBtn = new Button("Approve");
                approveBtn.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white; -fx-background-radius:6; -fx-padding:5 10;");

                // Reject button - red color
                Button rejectBtn = new Button("Reject");
                rejectBtn.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-background-radius:6; -fx-padding:5 10;");

                // If the claim is already decided, disable both buttons so staff can't change it again
                approveBtn.setDisable("Approved".equals(status) || "Rejected".equals(status));
                rejectBtn.setDisable("Approved".equals(status) || "Rejected".equals(status));

                HBox actions = new HBox(8, approveBtn, rejectBtn);
                actions.setPrefWidth(200);

                // When clicked, update the claim status in the database then refresh the table
                // We pass claimId so we know exactly which claim to update
                approveBtn.setOnAction(_ -> updateClaimStatus(claimId, "Approved", tableBox));
                rejectBtn.setOnAction(_  -> updateClaimStatus(claimId, "Rejected", tableBox));

                // Put all the cells together into one row
                row.getChildren().addAll(
                    rowCell(itemName, 220),
                    rowCell(claimant, 160),
                    rowCell(date,     120),
                    statusChip(status, 120), // colored label based on status
                    actions
                );
                tableBox.getChildren().add(row);
            }

            // If no claims exist in the database, show a message instead of an empty table
            if (!hasRows) {
                Label noData = new Label("No claim requests found.");
                noData.setTextFill(Color.GRAY);
                noData.setPadding(new Insets(10, 0, 0, 0));
                tableBox.getChildren().add(noData);
            }

        } catch (Exception e) {
            e.printStackTrace();

            // Show an error message in the table if the database query fails
            Label err = new Label("Error loading claims.");
            err.setTextFill(Color.RED);
            tableBox.getChildren().add(err);
        }
    }

    // This method handles what happens when staff clicks Approve or Reject
    // It updates the claim status and if approved, also marks the item as Returned
    private void updateClaimStatus(int claimId, String newStatus, VBox tableBox) {

        // First query updates the claim's own status
        String updateClaim = "UPDATE claims SET status = ? WHERE id = ?";

        // Second query finds the item linked to this claim and marks it as Returned
        // This only runs if the claim was approved
        String updateItem  = "UPDATE items SET status = 'Returned' " +
                             "WHERE id = (SELECT item_id FROM claims WHERE id = ?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Update claim status to either Approved or Rejected
            PreparedStatement ps1 = conn.prepareStatement(updateClaim);
            ps1.setString(1, newStatus);
            ps1.setInt(2, claimId);
            ps1.executeUpdate();

            // Only update the item status if the claim was approved, not rejected
            if ("Approved".equals(newStatus)) {
                PreparedStatement ps2 = conn.prepareStatement(updateItem);
                ps2.setInt(1, claimId);
                ps2.executeUpdate();
            }

            // Reload the table to show the updated status immediately
            loadRows(tableBox);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Creates a gray header label with a fixed width for the column headers row
    // Abstraction: we wrap the label styling logic into one reusable method
    private Label colHeader(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font-size:11px;");
        return l;
    }

    // Creates a normal text label for data cells in each row
    // If the value is null (missing from DB), we show a dash instead
    // Encapsulation: the cell styling is hidden inside this method, callers just pass text and width
    private Label rowCell(String text, double width) {
        Label l = new Label(text == null ? "-" : text);
        l.setPrefWidth(width);
        l.setStyle("-fx-font-size:13px;");
        return l;
    }

    // Creates a colored status label (chip) based on the claim's current status
    // Green = Approved, Red = Rejected, Orange = Pending
    // Polymorphism-like behavior: same method, different output based on the status value passed in
    private Label statusChip(String status, double width) {
        Label l = new Label(status == null ? "Pending" : status);
        l.setPrefWidth(width);

        // Pick color based on status - default falls to orange (Pending)
        String color = switch (status == null ? "Pending" : status) {
            case "Approved" -> "#27ae60";
            case "Rejected" -> "#c0392b";
            default         -> "#e67e22";
        };
        l.setStyle("-fx-font-size:11px; -fx-font-weight:bold; -fx-text-fill:" + color + ";");
        return l;
    }
}