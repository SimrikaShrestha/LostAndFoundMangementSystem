package view;

import controller.AdminDashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboardPage {
    private Stage stage;
    private String adminName;

    public AdminDashboardPage(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
    }

    public void show() {
        AdminDashboardController controller = new AdminDashboardController();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5f7;");
        root.setLeft(AdminSidebar.build(stage, "overview", adminName));

        VBox content = new VBox(0);

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(18, 28, 14, 28));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #e4eef0; -fx-border-width: 0 0 1 0;");
        HBox.setHgrow(topBar, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Search system logs, users, or reports...");
        search.setPrefWidth(320);
        search.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f5f7; -fx-background-radius: 20; " +
                "-fx-border-color: #ddeaed; -fx-border-radius: 20; -fx-padding: 8 14;");
        HBox.setHgrow(search, Priority.ALWAYS);

        Label bell = new Label("🔔");
        bell.setStyle("-fx-font-size: 16px; -fx-cursor: hand;");
        StackPane bellPane = new StackPane(bell);
        Label dot = new Label("•");
        dot.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 18px;");
        dot.setTranslateX(6); dot.setTranslateY(-6);
        StackPane bellStack = new StackPane(bellPane, dot);

        Label help = new Label("?");
        help.setStyle("-fx-background-color: #e4eef0; -fx-background-radius: 20; -fx-padding: 4 10; -fx-font-weight: 700; -fx-text-fill: #4a6b73; -fx-cursor: hand;");

        Button exportBtn = new Button("Export ↓");
        exportBtn.setStyle("-fx-background-color: white; -fx-text-fill: #1a2e35; -fx-font-weight: 600; " +
                "-fx-background-radius: 8; -fx-border-color: #c5d8dc; -fx-border-radius: 8; -fx-padding: 7 14; -fx-cursor: hand;");

        HBox right = new HBox(12, bellStack, help, exportBtn);
        right.setAlignment(Pos.CENTER_RIGHT);
        topBar.getChildren().addAll(search, right);

        VBox main = new VBox(28);
        main.setPadding(new Insets(32, 36, 36, 36));

        VBox titleBox = new VBox(4);
        Label pageTitle = new Label("Admin Dashboard");
        pageTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        Label pageSub = new Label("Real-time monitoring and high-level performance analytics.");
        pageSub.setStyle("-fx-font-size: 13px; -fx-text-fill: #7a9ba3;");
        titleBox.getChildren().addAll(pageTitle, pageSub);

        Label controlsLabel = new Label("MANAGEMENT CONTROLS");
        controlsLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #9ab5bc; -fx-letter-spacing: 1;");
        controlsLabel.setPadding(new Insets(0, 0, 4, 0));

        HBox row1 = new HBox(20);
        HBox row2 = new HBox(20);

        VBox usersCard = buildManagementCard("👥", "Manage Users",
            "Total: " + controller.getTotalUsers() + " users", "#d0eef1", "#2a7a85");
        usersCard.setOnMouseClicked(_ -> new AdminManageUsersPage(stage, adminName).show());

        VBox staffCard = buildManagementCard("🪪", "Manage Staff",
            "Total: " + controller.getTotalStaff() + " staff", "#d0eef1", "#2a7a85");
        staffCard.setOnMouseClicked(_ -> new AdminManageStaffPage(stage, adminName).show());

        VBox catCard = buildManagementCard("▲●■", "Categories",
            "Total: " + controller.getTotalCategories() + " categories", "#d0eef1", "#2a7a85");
        catCard.setOnMouseClicked(_ -> new AdminCategoriesPage(stage, adminName).show());

        VBox settingsCard = buildManagementCard("⚙", "System Settings",
            "Configure system preferences", "#d0eef1", "#2a7a85");
        settingsCard.setOnMouseClicked(_ -> new AdminSettingsPage(stage, adminName).show());

        HBox.setHgrow(usersCard, Priority.ALWAYS);
        HBox.setHgrow(staffCard, Priority.ALWAYS);
        HBox.setHgrow(catCard, Priority.ALWAYS);
        HBox.setHgrow(settingsCard, Priority.ALWAYS);

        row1.getChildren().addAll(usersCard, staffCard);
        row2.getChildren().addAll(catCard, settingsCard);

        main.getChildren().addAll(titleBox, controlsLabel, row1, row2);

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f5f7; -fx-background: #f0f5f7;");

        content.getChildren().addAll(topBar, scroll);
        root.setCenter(content);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Lost & Found — Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private VBox buildManagementCard(String icon, String title, String subtitle, String iconBg, String iconColor) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(36));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2); -fx-cursor: hand;");
        card.setOnMouseEntered(_ -> card.setStyle("-fx-background-color: #f8fdfe; -fx-background-radius: 14; " +
                "-fx-effect: dropshadow(gaussian, rgba(91,200,208,0.18), 14, 0, 0, 4); -fx-cursor: hand;"));
        card.setOnMouseExited(_ -> card.setStyle("-fx-background-color: white; -fx-background-radius: 14; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2); -fx-cursor: hand;"));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #5bc8d0; -fx-background-color: " + iconBg +
                "; -fx-background-radius: 14; -fx-padding: 16 20;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        Label subLabel = new Label(subtitle);
        subLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #9ab5bc;");

        card.getChildren().addAll(iconLabel, titleLabel, subLabel);
        return card;
    }
}