package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.SessionManager;
import model.User;


// Demonstrates abstraction
public abstract class BasePage {
    protected Stage stage; // Encapsulation: store stage privately for use in child pages

    // Logout button colors and styles (easy to modify)
    private static final String LOGOUT_DEFAULT_BG  = "#fff0f0";
    private static final String LOGOUT_HOVER_BG    = "#ffe0e0";
    private static final String LOGOUT_TEXT        = "#c0392b";

    // Show the stage maximized (reusable across pages)
    protected void showStage() {
        stage.setMaximized(true);
        stage.show();
    }

    // Build a sidebar with buttons for navigation
    // Demonstrates modularity: can be reused in all pages
    public VBox buildSidebar(String activePage) {
        VBox sidebar = new VBox(10); // vertical layout with spacing
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color:#ffffff;"); // white background

        // App name at the top
        Label appName = new Label("Lost and Found\nManagement System");
        appName.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");

        // Show current user info (encapsulation: SessionManager hides login details)
        User currentUser = SessionManager.getInstance().getCurrentUser();
        String idText = (currentUser != null) ? "User ID: " + currentUser.getId() : "User ID: N/A";
        Label userId = new Label(idText);
        userId.setTextFill(Color.GRAY);
        userId.setStyle("-fx-font-size:11px;");

        Separator sep = new Separator(); // visual separator

        // Create sidebar buttons, mark active page
        Button dashboardBtn   = sidebarButton("Dashboard",    activePage.equals("Dashboard"));
        Button reportLostBtn  = sidebarButton("Report Lost",  activePage.equals("Report Lost"));
        Button reportFoundBtn = sidebarButton("Report Found", activePage.equals("Report Found"));
        Button searchBtn      = sidebarButton("Search Items", activePage.equals("Search Items"));
        Button claimsBtn      = sidebarButton("My Claims",    activePage.equals("My Claims"));
        Button profileBtn     = sidebarButton("Profile",      activePage.equals("Profile"));

        // Add click actions for buttons (abstraction: hide page-switching logic)
        dashboardBtn.setOnAction(_   -> new DashboardPage(stage).show());
        reportLostBtn.setOnAction(_  -> new ReportLostPage(stage).show());
        reportFoundBtn.setOnAction(_ -> new ReportFoundPage(stage).show());
        searchBtn.setOnAction(_      -> new SearchItemsPage(stage).show());
        claimsBtn.setOnAction(_      -> new MyClaimsPage(stage).show());
        profileBtn.setOnAction(_     -> new ProfilePage(stage).show());

        // Spacer pushes logout button and report button to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // ── Logout button ──────────────────────────────────────────────────────
        Button logoutBtn = new Button("⏻  Logout");
        logoutBtn.setPrefWidth(180);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setPadding(new Insets(10, 15, 10, 15));
        logoutBtn.setStyle(logoutDefaultStyle()); // default style

        // Hover effect: gives user feedback
        logoutBtn.setOnMouseEntered(_ -> logoutBtn.setStyle(logoutHoverStyle()));
        logoutBtn.setOnMouseExited(_  -> logoutBtn.setStyle(logoutDefaultStyle()));

        // Press effect: simulates button press
        logoutBtn.setOnMousePressed(_  -> logoutBtn.setStyle(logoutPressedStyle()));
        logoutBtn.setOnMouseReleased(_ -> logoutBtn.setStyle(logoutHoverStyle()));

        // Logout action: clears session and goes to login page
        logoutBtn.setOnAction(_ -> {
            SessionManager.getInstance().clearSession(); // encapsulated session handling
            new LoginPage(stage).show();
        });

        // Report New Item button at bottom
        Button reportNewBtn = new Button("+ Report New Item");
        reportNewBtn.setPrefWidth(180);
        reportNewBtn.setStyle(
            "-fx-background-color:#7fd1d8;" +
            "-fx-text-fill:black;" +
            "-fx-font-size:13px;" +
            "-fx-background-radius:20;" +
            "-fx-padding:10;"
        );
        reportNewBtn.setOnAction(_ -> new ReportLostPage(stage).show());

        // Add all components to sidebar
        sidebar.getChildren().addAll(
            appName, userId, sep,
            dashboardBtn, reportLostBtn, reportFoundBtn,
            searchBtn, claimsBtn, profileBtn,
            spacer, logoutBtn, reportNewBtn
        );
        return sidebar;
    }

    // Helper to create sidebar buttons (modularity & reuse)
    protected Button sidebarButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 15, 10, 15));

        if (active) {
            // Active button style
            btn.setStyle(
                "-fx-background-color:#7fd1d8;" +
                "-fx-text-fill:black;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;"
            );
        } else {
            
            btn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:black;" +
                "-fx-background-radius:8;"
            );
        }
        return btn;
    }

  
    // Encapsulation

    private String logoutDefaultStyle() {
        return  "-fx-background-color:" + LOGOUT_DEFAULT_BG + ";" +
                "-fx-text-fill:" + LOGOUT_TEXT + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;" +
                "-fx-padding:10 15;";
    }

    private String logoutHoverStyle() {
        return  "-fx-background-color:" + LOGOUT_HOVER_BG + ";" +
                "-fx-text-fill:" + LOGOUT_TEXT + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;" +
                "-fx-padding:10 15;";
    }

    private String logoutPressedStyle() {
        return  "-fx-background-color:#ffd0d0;" +
                "-fx-text-fill:" + LOGOUT_TEXT + ";" +
                "-fx-font-size:13px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:6;" +
                "-fx-cursor:hand;" +
                "-fx-padding:11 15 9 15;";
    }

    
    // Demonstrates abstraction
    public abstract void show();
}