package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DBReports;

import java.util.List;

// This page shows the staff a summary report of everything happening in the system
// It includes stat cards at the top and a monthly breakdown table below
public class ReportsPage {

    // stage is the main window, staffName tracks who is logged in
    // Both are private - encapsulation keeps them from being accessed outside this class
    private Stage  stage;
    private String staffName;

    // Constructor - stores the window and staff name, then builds the page right away
    public ReportsPage(Stage stage, String staffName) {
        this.stage     = stage;
        this.staffName = staffName;
        buildPage();
    }

    // Sets up the full page layout with sidebar on the left and report content in the center
    private void buildPage() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");

        // Reuse StaffDashboardPage just to get its sidebar - no need to rewrite it here
        // This is composition/reuse: we borrow one method from another class
        root.setLeft(new StaffDashboardPage(stage, staffName).buildSidebar("Reports"));
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Reports");
        stage.setScene(scene);
    }

    // Builds the center section with stat cards and the monthly summary table
    // All the live data is pulled from the database through DBReports
    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label title = new Label("Reports");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold; -fx-text-fill:#1a2e35;");

        Label subtitle = new Label("Overview of system activity.");
        subtitle.setTextFill(Color.GRAY);

        // Fetch the 4 key numbers live from the database
        // Abstraction: DBReports hides all the SQL, we just call the methods we need
        int    totalReported = DBReports.getTotalItemsReported();
        int    returned      = DBReports.getItemsReturned();
        int    pending       = DBReports.getPendingCases();
        String successRate   = String.format("%.1f%%", DBReports.getSuccessRate());

        // Build 4 stat cards in a row using the same helper method each time
        // Reuse: buildStatCard() is called 4 times with different data - no repeated code
        HBox statsBox = new HBox(15);
        statsBox.getChildren().addAll(
            buildStatCard("Total Items Reported", String.valueOf(totalReported)),
            buildStatCard("Items Returned",        String.valueOf(returned)),
            buildStatCard("Pending Cases",          String.valueOf(pending)),
            buildStatCard("Success Rate",           successRate)
        );

        // White box that holds the monthly summary table
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label tableTitle = new Label("Monthly Summary");
        tableTitle.setStyle("-fx-font-size:15px; -fx-font-weight:bold;");

        // Column headers for the monthly table
        // Encapsulation: colHeader() hides the label styling, we just pass text and width
        HBox colHeaders = new HBox();
        colHeaders.getChildren().addAll(
            colHeader("MONTH",    150),
            colHeader("LOST",     120),
            colHeader("FOUND",    120),
            colHeader("RETURNED", 130),
            colHeader("PENDING",  120)
        );

        tableBox.getChildren().addAll(tableTitle, colHeaders, new Separator());

        // Fetch the monthly breakdown data from the database
        // DBReports.MonthlyReport is an inner class that holds one row of monthly data
        List<DBReports.MonthlyReport> monthlyData = DBReports.getMonthlyReport();

        if (monthlyData.isEmpty()) {
            // No data available yet - show a simple message instead of empty table
            Label noData = new Label("No report data available.");
            noData.setTextFill(Color.GRAY);
            noData.setPadding(new Insets(10, 0, 0, 0));
            tableBox.getChildren().add(noData);
        } else {
            // Loop through each monthly record and build a row for it
            // Each row accesses fields directly from the MonthlyReport object
            for (DBReports.MonthlyReport row : monthlyData) {
                HBox rowBox = new HBox();
                rowBox.getChildren().addAll(
                    rowCell(row.month,                    150),
                    rowCell(String.valueOf(row.lost),     120),
                    rowCell(String.valueOf(row.found),    120),
                    rowCell(String.valueOf(row.returned), 130),
                    rowCell(String.valueOf(row.pending),  120)
                );
                // Add the row and a thin line below it to separate rows visually
                tableBox.getChildren().addAll(rowBox, new Separator());
            }
        }

        content.getChildren().addAll(title, subtitle, statsBox, tableBox);
        return content;
    }

    // Builds one white stat card with a small gray title and a big bold number
    // Abstraction: the card layout is hidden here, callers just pass title and value
    private VBox buildStatCard(String title, String value) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setPrefWidth(180);
        card.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        // Small gray label at the top of the card
        Label t = new Label(title);
        t.setTextFill(Color.GRAY);
        t.setStyle("-fx-font-size:11px;");
        t.setWrapText(true); // wrap in case the title is long

        // Big bold number shown below the title
        Label v = new Label(value);
        v.setStyle("-fx-font-size:26px; -fx-font-weight:bold;");

        card.getChildren().addAll(t, v);
        return card;
    }

    // Creates a small gray label for the column headers row
    // Encapsulation: the header styling is locked inside this method
    private Label colHeader(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font-size:11px;");
        return l;
    }

    // Creates a regular label for data cells inside each table row
    // Encapsulation: cell styling is handled here so it's consistent across all rows
    private Label rowCell(String text, double width) {
        Label l = new Label(text);
        l.setPrefWidth(width);
        l.setStyle("-fx-font-size:13px;");
        return l;
    }

    // Shows the page on screen in maximized mode
    public void show() {
        stage.setMaximized(true);
        stage.show();
    }
}