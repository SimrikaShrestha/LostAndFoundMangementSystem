package view;

import controller.AdminDashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// AdminDashboardPage shows main admin dashboard UI
// OOP concepts:
// - Encapsulation: private fields stage, adminName hide internal state
// - Abstraction: show() hides all UI setup
// - Polymorphism: Mouse click actions behave differently for each card
public class AdminDashboardPage {
    private Stage stage;      // Encapsulation: main window
    private String adminName; // Encapsulation: logged-in admin info

    // Constructor
    public AdminDashboardPage(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
    }

    // Abstraction: external code only calls show() to display dashboard
    public void show() {
        AdminDashboardController controller = new AdminDashboardController(); // Handles data operations

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5f7;");
        root.setLeft(AdminSidebar.build(stage, "overview", adminName)); // Modular sidebar

        VBox content = new VBox(0);

        // Top bar with padding and border
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(18, 28, 14, 28));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #e4eef0; -fx-border-width: 0 0 1 0;");
        HBox.setHgrow(topBar, Priority.ALWAYS);

        VBox main = new VBox(28); // Main content container
        main.setPadding(new Insets(32, 36, 36, 36));

        // Dashboard title + subtitle
        VBox titleBox = new VBox(4);
        Label pageTitle = new Label("Admin Dashboard");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        Label pageSub = new Label("Real-time monitoring and high-level performance analytics.");
        pageSub.setStyle("-fx-font-size: 13px; -fx-text-fill: #7a9ba3;");
        titleBox.getChildren().addAll(pageTitle, pageSub);

        Label controlsLabel = new Label("MANAGEMENT CONTROLS");
        controlsLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #9ab5bc;");
        controlsLabel.setPadding(new Insets(0, 0, 4, 0));

        HBox row1 = new HBox(20); // Row for cards
        HBox row2 = new HBox(20);

        // Management cards (polymorphic click behavior)
        VBox usersCard = buildManagementCard("Manage Users", "Total: " + controller.getTotalUsers() + " users");
        usersCard.setOnMouseClicked(_ -> new AdminManageUsersPage(stage, adminName).show());

        VBox staffCard = buildManagementCard("Manage Staff", "Total: " + controller.getTotalStaff() + " staff");
        staffCard.setOnMouseClicked(_ -> new AdminManageStaffPage(stage, adminName).show());

        VBox catCard = buildManagementCard("Categories", "Total: " + controller.getTotalCategories() + " categories");
        catCard.setOnMouseClicked(_ -> new AdminCategoriesPage(stage, adminName).show());

        VBox settingsCard = buildManagementCard("System Settings", "Configure system preferences");
        settingsCard.setOnMouseClicked(_ -> new AdminSettingsPage(stage, adminName).show());

        // Make cards expand equally
        HBox.setHgrow(usersCard, Priority.ALWAYS);
        HBox.setHgrow(staffCard, Priority.ALWAYS);
        HBox.setHgrow(catCard, Priority.ALWAYS);
        HBox.setHgrow(settingsCard, Priority.ALWAYS);

        row1.getChildren().addAll(usersCard, staffCard);
        row2.getChildren().addAll(catCard, settingsCard);

        main.getChildren().addAll(titleBox, controlsLabel, row1, row2);

        // Scrollable main area
        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f5f7; -fx-background: #f0f5f7;");

        content.getChildren().addAll(topBar, scroll);
        root.setCenter(content);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Lost & Found — Admin Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    // Modularity: reusable card for dashboard
    private VBox buildManagementCard(String title, String subtitle) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(36));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2); -fx-cursor: hand;");

        // polymorphism: same card reacts differently 
        card.setOnMouseEntered(_ -> card.setStyle("-fx-background-color: #f8fdfe; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(91,200,208,0.18), 14, 0, 0, 4); -fx-cursor: hand;"));
        card.setOnMouseExited(_ -> card.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2); -fx-cursor: hand;"));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        Label subLabel = new Label(subtitle);
        subLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #9ab5bc;");

        card.getChildren().addAll(titleLabel, subLabel);
        return card;
    }
}