package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AdminSidebar {

    public static VBox build(Stage stage, String activePage, String adminName) {
        VBox sidebar = new VBox(4);
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setStyle("-fx-background-color: #f8fbfc;");
        sidebar.setPadding(new Insets(0));

        HBox logo = new HBox(10);
        logo.setPadding(new Insets(24, 20, 20, 20));
        logo.setAlignment(Pos.CENTER_LEFT);
        logo.setStyle("-fx-border-color: #e4eef0; -fx-border-width: 0 0 1 0;");
        Label icon = new Label("❋");
        icon.setStyle("-fx-font-size: 20px; -fx-text-fill: #5bc8d0;");
        VBox titleBox = new VBox(1);
        Label t1 = new Label("Lost and Found");
        t1.setStyle("-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        Label t2 = new Label("Management System");
        t2.setStyle("-fx-font-size: 11px; -fx-text-fill: #7a9ba3;");
        titleBox.getChildren().addAll(t1, t2);
        logo.getChildren().addAll(icon, titleBox);

        VBox nav = new VBox(2);
        nav.setPadding(new Insets(16, 10, 16, 10));
        VBox.setVgrow(nav, Priority.ALWAYS);

        nav.getChildren().addAll(
            navItem("⊞", "System Overview", "overview", activePage, stage, adminName),
            navItem("👥", "Manage Users",    "users",    activePage, stage, adminName),
            navItem("🪪", "Manage Staff",    "staff",    activePage, stage, adminName),
            navItem("▲", "Categories",       "categories", activePage, stage, adminName)
        );

        Label systemLabel = new Label("SYSTEM");
        systemLabel.setPadding(new Insets(16, 14, 6, 14));
        systemLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: #9ab5bc;");

        VBox system = new VBox(2);
        system.getChildren().addAll(
            systemLabel,
            navItem("⚙", "System Settings", "settings",  activePage, stage, adminName),
            navItem("💾", "Backup",          "backup",    activePage, stage, adminName)
        );

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox profileBox = new HBox(10);
        profileBox.setPadding(new Insets(16, 14, 20, 14));
        profileBox.setAlignment(Pos.CENTER_LEFT);
        profileBox.setStyle("-fx-border-color: #e4eef0; -fx-border-width: 1 0 0 0;");
        Circle av = new Circle(18, Color.web("#d0eef1"));
        Label ini = new Label(adminName.substring(0, 1).toUpperCase());
        ini.setStyle("-fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #2a7a85;");
        StackPane avPane = new StackPane(av, ini);
        VBox nameBox = new VBox(1);
        Label nameLabel = new Label(adminName);
        nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        Label roleLabel = new Label("Admin");
        roleLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #5bc8d0;");
        nameBox.getChildren().addAll(nameLabel, roleLabel);
        profileBox.getChildren().addAll(avPane, nameBox);

        sidebar.getChildren().addAll(logo, nav, system, spacer, profileBox);
        return sidebar;
    }

    private static HBox navItem(String icon, String label, String page, String activePage, Stage stage, String adminName) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setCursor(javafx.scene.Cursor.HAND);
        boolean active = activePage.equals(page);
        row.setStyle("-fx-background-color: " + (active ? "#e8f7f8" : "transparent") + "; -fx-background-radius: 8;");
        String color = active ? "#2a7a85" : "#4a6b73";
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 13px; -fx-text-fill: " + (active ? "#5bc8d0" : "#7a9ba3") + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-family: 'System'; -fx-font-size: 13px; -fx-font-weight: " + (active ? "700" : "400") + "; -fx-text-fill: " + color + ";");
        row.getChildren().addAll(ic, lbl);
        if (!active) {
            row.setOnMouseEntered(_ -> row.setStyle("-fx-background-color: #f0f9fa; -fx-background-radius: 8;"));
            row.setOnMouseExited(_ -> row.setStyle("-fx-background-color: transparent; -fx-background-radius: 8;"));
        }
        row.setOnMouseClicked(_ -> navigate(page, stage, adminName));
        return row;
    }

    private static void navigate(String page, Stage stage, String adminName) {
        switch (page) {
            case "overview"    -> new AdminDashboardPage(stage, adminName).show();
            case "users"       -> new AdminManageUsersPage(stage, adminName).show();
            case "staff"       -> new AdminManageStaffPage(stage, adminName).show();
            case "categories"  -> new AdminCategoriesPage(stage, adminName).show();
            case "settings"    -> new AdminSettingsPage(stage, adminName).show();
            case "backup"      -> new AdminBackupPage(stage, adminName).show();
        }
    }
}