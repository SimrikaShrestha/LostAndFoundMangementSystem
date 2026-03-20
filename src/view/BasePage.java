package view;

import controller.DashboardController;
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

/**
 * BasePage - Abstract base class for all user-facing pages.
 *
 * OOP PRINCIPLE: Inheritance + Abstraction
 * - Provides the shared buildSidebar() method to ALL subclasses.
 * - Declares show() as abstract, forcing every subclass to implement it.
 * - Subclasses: DashboardPage, ReportLostPage, ReportFoundPage,
 *               MyClaimsPage, ProfilePage, SearchItemsPage
 */
public abstract class BasePage {

    // Protected so all subclasses can access stage directly
    protected Stage stage;
    /**
     * Builds the shared left sidebar used across all user pages.
     * Subclasses call this method via inheritance — no duplication needed.
     */
    public VBox buildSidebar(String activePage) {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color:#ffffff;");

        Label appName = new Label("Lost and Found\nManagement System");
        appName.setStyle("-fx-font-size:13px; -fx-font-weight:bold;");

        // Dynamic User ID from session
        User currentUser = SessionManager.getInstance().getCurrentUser();
        String idText = (currentUser != null) ? "User ID: " + currentUser.getId() : "User ID: N/A";
        Label userId = new Label(idText);
        userId.setTextFill(Color.GRAY);
        userId.setStyle("-fx-font-size:11px;");

        Separator sep = new Separator();

        Button dashboardBtn   = sidebarButton("Dashboard",    activePage.equals("Dashboard"));
        Button reportLostBtn  = sidebarButton("Report Lost",  activePage.equals("Report Lost"));
        Button reportFoundBtn = sidebarButton("Report Found", activePage.equals("Report Found"));
        Button searchBtn      = sidebarButton("Search Items", activePage.equals("Search Items"));
        Button claimsBtn      = sidebarButton("My Claims",    activePage.equals("My Claims"));
        Button profileBtn     = sidebarButton("Profile",      activePage.equals("Profile"));

        // Navigation — each button creates the target page
        dashboardBtn.setOnAction(_   -> new DashboardPage(stage).show());
        reportLostBtn.setOnAction(_  -> new ReportLostPage(stage).show());
        reportFoundBtn.setOnAction(_ -> new ReportFoundPage(stage).show());
        searchBtn.setOnAction(_      -> new SearchItemsPage(stage).show());
        claimsBtn.setOnAction(_      -> new MyClaimsPage(stage).show());
        profileBtn.setOnAction(_     -> new ProfilePage(stage).show());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

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

        sidebar.getChildren().addAll(
            appName, userId, sep,
            dashboardBtn, reportLostBtn, reportFoundBtn,
            searchBtn, claimsBtn, profileBtn,
            spacer, reportNewBtn
        );

        return sidebar;
    }

    /**
     * Helper method for styling sidebar buttons.
     * Also inherited by all subclasses.
     */
    protected Button sidebarButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 15, 10, 15));
        if (active) {
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

    /**
     * Abstract method — every subclass MUST implement this.
     * OOP PRINCIPLE: Abstraction
     */
    public abstract void show();
}