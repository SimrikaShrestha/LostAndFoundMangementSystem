package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.SessionManager;

public class AdminSidebar {

    // Build the sidebar layout for admin pages
    // Using encapsulation: the method hides the internal details of sidebar creation
    public static VBox build(Stage stage, String activePage, String adminName) {
        VBox sidebar = new VBox(4); // vertical layout with spacing
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setStyle("-fx-background-color: #f8fbfc;"); // light background
        sidebar.setPadding(new Insets(0));

        // Logo section at the top
        HBox logo = new HBox(10); // horizontal layout for icon + text
        logo.setPadding(new Insets(24, 20, 20, 20));
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.setStyle("-fx-border-color: #e4eef0; -fx-border-width: 0 0 1 0;"); // bottom border
        Label icon = new Label("❋"); // small icon
        icon.setStyle("-fx-font-size: 20px; -fx-text-fill: #5bc8d0;"); // color styling
        VBox titleBox = new VBox(1); // vertical box for title text
        Label t1 = new Label("Lost and Found");
        t1.setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        Label t2 = new Label("Management System");
        t2.setStyle("-fx-font-size: 11px; -fx-text-fill: #7a9ba3;");
        titleBox.getChildren().addAll(t1, t2);
        logo.getChildren().addAll(icon, titleBox); // combine icon and text

        // Navigation links section
        VBox nav = new VBox(2); 
        nav.setPadding(new Insets(16, 10, 16, 10));
        VBox.setVgrow(nav, Priority.ALWAYS); // expand nav section if needed

        // Add menu items using abstraction: navItem hides UI creation details
        nav.getChildren().addAll(
            navItem("⊞", "System Overview", "overview", activePage, stage, adminName),
            navItem("👥", "Manage Users", "users", activePage, stage, adminName),
            navItem("🪪", "Manage Staff", "staff", activePage, stage, adminName),
            navItem("▲", "Categories", "categories", activePage, stage, adminName)
        );

        // System label for settings section
        Label systemLabel = new Label("SYSTEM");
        systemLabel.setPadding(new Insets(16, 14, 6, 14));
        systemLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: #9ab5bc;");

        // System settings section
        VBox system = new VBox(2);
        system.getChildren().addAll(
            systemLabel,
            navItem("⚙", "Profile", "settings", activePage, stage, adminName)
            );

        // Spacer to push logout button to bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setPadding(new Insets(10, 14, 10, 14));
        logoutBtn.setStyle(
        	    "-fx-background-color:#ffcccc;" +
        	    "-fx-text-fill:#cc0000;" +
        	    "-fx-font-weight:bold;" +
        	    "-fx-font-size:13px;" +
        	    "-fx-background-radius:8;" +
        	    "-fx-cursor:hand;"
        	);
        logoutBtn.setOnAction(_ -> {
            SessionManager.getInstance().clearSession(); // encapsulated session clearing
            new LoginPage(stage).show(); // navigate to login page
        });

        // Bottom profile section
        VBox bottomBox = new VBox(0);
        bottomBox.setStyle("-fx-border-color: #e4eef0; -fx-border-width: 1 0 0 0;");
        bottomBox.setPadding(new Insets(10, 10, 10, 10));

        // Profile info with initials
        HBox profileBox = new HBox(10);
        profileBox.setPadding(new Insets(6, 4, 6, 4));
        profileBox.setAlignment(Pos.CENTER_LEFT);
        Circle av = new Circle(18, Color.web("#d0eef1")); // avatar background
        Label ini = new Label(adminName.substring(0, 1).toUpperCase()); // first letter
        ini.setStyle("-fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #2a7a85;");
        StackPane avPane = new StackPane(av, ini); // stack initials over circle
        VBox nameBox = new VBox(1);
        Label nameLabel = new Label(adminName);
        nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        Label roleLabel = new Label("Admin");
        roleLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #5bc8d0;");
        nameBox.getChildren().addAll(nameLabel, roleLabel);
        profileBox.getChildren().addAll(avPane, nameBox);

        bottomBox.getChildren().addAll(profileBox, logoutBtn);

        // Combine all sections in sidebar
        sidebar.getChildren().addAll(logo, nav, system, spacer, bottomBox);

        return sidebar; // return complete sidebar
    }

    // Create a single navigation item
    // Demonstrates reusability and modularity (OOP principle)
    private static HBox navItem(String icon, String label, String page, String activePage, Stage stage, String adminName) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setCursor(javafx.scene.Cursor.HAND);

        boolean active = activePage.equals(page); // check if this is current page
        row.setStyle("-fx-background-color: " + (active ? "#e8f7f8" : "transparent") + "; -fx-background-radius: 8;");
        String color = active ? "#2a7a85" : "#4a6b73";

        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 13px; -fx-text-fill: " + (active ? "#5bc8d0" : "#7a9ba3") + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px; -fx-font-weight: " + (active ? "700" : "400") + "; -fx-text-fill: " + color + ";");
        row.getChildren().addAll(ic, lbl);

        // hover effect for inactive items
        if (!active) {
            row.setOnMouseEntered(_ -> row.setStyle("-fx-background-color: #f0f9fa; -fx-background-radius: 8;"));
            row.setOnMouseExited(_ -> row.setStyle("-fx-background-color: transparent; -fx-background-radius: 8;"));
        }

        // click event navigates to selected page
        row.setOnMouseClicked(_ -> navigate(page, stage, adminName));
        return row;
    }

    // Handle page navigation
    // Demonstrates abstraction: hides page-switching details
    private static void navigate(String page, Stage stage, String adminName) {
        switch (page) {
            case "overview"   -> new AdminDashboardPage(stage, adminName).show();
            case "users"      -> new AdminManageUsersPage(stage, adminName).show();
            case "staff"      -> new AdminManageStaffPage(stage, adminName).show();
            case "categories" -> new AdminCategoriesPage(stage, adminName).show();
            case "settings"   -> new AdminSettingsPage(stage, adminName).show();
        }
    }
}