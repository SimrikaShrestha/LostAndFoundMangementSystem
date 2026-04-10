package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.DBConnection;
import model.SessionManager;

// This is the main dashboard page for staff members after they log in
// It also provides the shared sidebar that all other staff pages reuse
// This class does NOT extend BasePage - it manages its own stage and sidebar directly
public class StaffDashboardPage {

    // stage is the main window, staffName is who is currently logged in
    // Both are private - encapsulation keeps them protected from outside access
    private Stage  stage;
    private String staffName;

    // Constructor - stores the window and staff name
    // buildAndShow() is called separately so other pages can reuse just the sidebar
    public StaffDashboardPage(Stage stage, String staffName) {
        this.stage     = stage;
        this.staffName = staffName;
    }

    // Builds the full staff dashboard and shows it on screen
    // Called when navigating to the dashboard from any other page
    public void buildAndShow() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f9fa;");
        root.setLeft(buildSidebar("Dashboard"));
        root.setTop(buildTopBar());
        root.setCenter(buildMainContent());

        Scene scene = new Scene(root, 950, 650);
        stage.setTitle("Staff Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    // Builds the left sidebar navigation panel
    // This method is PUBLIC so other staff pages can call it and reuse the same sidebar
    // Reuse/Composition: every staff page borrows this sidebar instead of building their own
    public VBox buildSidebar(String activePage) {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(230);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color:#ffffff;");

        // App name at the very top of the sidebar
        Label appName = new Label("Lost and Found\nManagement System");
        appName.setStyle("-fx-font-size:14px; -fx-font-weight:bold;");

        Separator sep1 = new Separator();

        // Staff profile info shown below the app name
        VBox profile = new VBox(2);
        Label staffNameLabel = new Label(staffName);
        staffNameLabel.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");
        Label staffRole = new Label("Staff");
        staffRole.setTextFill(Color.GRAY);
        staffRole.setStyle("-fx-font-size:11px;");
        profile.getChildren().addAll(staffNameLabel, staffRole);

        Separator sep2 = new Separator();

        // Navigation buttons - the active page gets a highlighted teal background
        // sidebarBtn() is reused for all 5 buttons - encapsulation hides the styling
        Button dashboardBtn     = sidebarBtn("Dashboard",      activePage.equals("Dashboard"));
        Button allItemsBtn      = sidebarBtn("All Items",      activePage.equals("All Items"));
        Button matchItemsBtn    = sidebarBtn("Match Items",    activePage.equals("Match Items"));
        Button claimRequestsBtn = sidebarBtn("Claim Requests", activePage.equals("Claim Requests"));
        Button reportsBtn       = sidebarBtn("Reports",        activePage.equals("Reports"));

        // Each button creates and opens the matching page when clicked
        // Polymorphism-like: each button calls show() on a different page object
        dashboardBtn.setOnAction(_     -> new StaffDashboardPage(stage, staffName).buildAndShow());
        allItemsBtn.setOnAction(_      -> new AllItemsPage(stage, staffName).show());
        matchItemsBtn.setOnAction(_    -> new MatchItemsPage(stage, staffName).show());
        claimRequestsBtn.setOnAction(_ -> new ClaimRequestsPage(stage, staffName).show());
        reportsBtn.setOnAction(_       -> new ReportsPage(stage, staffName).show());

        // Spacer pushes the settings and logout buttons to the bottom of the sidebar
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button settingsBtn = sidebarBtn("Profile", activePage.equals("Profile"));
        settingsBtn.setOnAction(_ -> new StaffSettingsPage(stage, staffName).show());

        // Logout button clears the session and goes back to the login page
        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(190);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setPadding(new Insets(10, 15, 10, 15));
        logoutBtn.setStyle(
        	    "-fx-background-color:#ffcccc;" +
        	    "-fx-text-fill:#cc0000;" +
        	    "-fx-font-weight:bold;" +
        	    "-fx-font-size:13px;" +
        	    "-fx-background-radius:8;"
        	);
        logoutBtn.setOnAction(_ -> {
            // Clear the logged-in user's session data before going back to login
            SessionManager.getInstance().clearSession();
            new LoginPage(stage).show();
        });

        sidebar.getChildren().addAll(
            appName, sep1, profile, sep2,
            dashboardBtn, allItemsBtn, matchItemsBtn, claimRequestsBtn, reportsBtn,
            spacer, settingsBtn, logoutBtn
        );
        return sidebar;
    }

    // Creates a sidebar navigation button with active or inactive styling
    // Encapsulation: all button styling is locked inside here
    // The active flag decides whether the button gets the teal highlight or stays plain
    private Button sidebarBtn(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(190);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 15, 10, 15));
        btn.setStyle(active
            ? "-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-font-weight:bold; -fx-background-radius:8;"
            : "-fx-background-color:transparent; -fx-text-fill:black; -fx-background-radius:8;"
        );
        return btn;
    }

    // Builds the top bar that shows the dashboard title and a search field
    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color:#f0f9fa;");

        Label title = new Label("Staff Dashboard");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");

        // Spacer pushes the search field all the way to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Search claims...");
        searchField.setPrefWidth(220);

        topBar.getChildren().addAll(title, spacer, searchField);
        return topBar;
    }

    // Builds the center section with 3 live stat cards showing key numbers
    private VBox buildMainContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20, 30, 30, 30));

        // Fetch live counts from the database for each stat
        int totalLost     = countItems("Lost");
        int totalFound    = countItems("Found");
        int pendingClaims = countPendingClaims();

        // Build 3 stat cards in a row using the same helper method
        // Reuse: buildStatCard() is called 3 times with different data
        HBox statsBox = new HBox(15);
        statsBox.getChildren().addAll(
            buildStatCard("TOTAL LOST",     String.valueOf(totalLost),     "Live from database"),
            buildStatCard("TOTAL FOUND",    String.valueOf(totalFound),    "Live from database"),
            buildStatCard("PENDING CLAIMS", String.valueOf(pendingClaims), "Action required")
        );

        content.getChildren().add(statsBox);
        return content;
    }

    // Counts how many items in the DB have the given type ("Lost" or "Found")
    // Encapsulation: the SQL is hidden here, callers just pass the type they want counted
    private int countItems(String type) {
        String sql = "SELECT COUNT(*) FROM items WHERE type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // return 0 if query fails so the app doesn't crash
    }

    // Counts how many claims are still sitting as Pending in the database
    // Used to show the staff how many claims need their attention
    private int countPendingClaims() {
        String sql = "SELECT COUNT(*) FROM claims WHERE status = 'Pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // return 0 safely if anything goes wrong
    }

    // Builds one white stat card with a small title, big number, and a small note below
    // Abstraction: the card layout details are hidden here, callers just pass 3 strings
    private VBox buildStatCard(String title, String value, String note) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color:white; -fx-background-radius:10;");

        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.GRAY);
        titleLabel.setStyle("-fx-font-size:11px;");

        // Big bold number in the center of the card
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size:28px; -fx-font-weight:bold;");

        Label noteLabel = new Label(note);
        noteLabel.setTextFill(Color.GRAY);
        noteLabel.setStyle("-fx-font-size:11px;");

        card.getChildren().addAll(titleLabel, valueLabel, noteLabel);
        return card;
    }

    // Shows the staff dashboard - just calls buildAndShow() to set everything up
    public void show() {
        buildAndShow();
    }
}