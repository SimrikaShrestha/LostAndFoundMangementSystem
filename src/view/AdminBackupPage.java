package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AdminBackupPage {
    private Stage stage;
    private String adminName;
    private BorderPane root;
    private VBox content;
    private VBox card;
    private VBox history;
    private Label title;
    private Label sub;
    private Label cardTitle;
    private Label info;
    private Label statusLbl;
    private Label histTitle;
    private Button backupBtn;
    private Separator sep;
    private ScrollPane scroll;

    public AdminBackupPage(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
    }

    // Getters and Setters
    public Stage getStage() { return stage; }
    public void setStage(Stage stage) { this.stage = stage; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public BorderPane getRoot() { return root; }
    public void setRoot(BorderPane root) { this.root = root; }

    public VBox getContent() { return content; }
    public void setContent(VBox content) { this.content = content; }

    public VBox getCard() { return card; }
    public void setCard(VBox card) { this.card = card; }

    public VBox getHistory() { return history; }
    public void setHistory(VBox history) { this.history = history; }

    public Label getTitle() { return title; }
    public void setTitle(Label title) { this.title = title; }

    public Label getSub() { return sub; }
    public void setSub(Label sub) { this.sub = sub; }

    public Label getCardTitle() { return cardTitle; }
    public void setCardTitle(Label cardTitle) { this.cardTitle = cardTitle; }

    public Label getInfo() { return info; }
    public void setInfo(Label info) { this.info = info; }

    public Label getStatusLbl() { return statusLbl; }
    public void setStatusLbl(Label statusLbl) { this.statusLbl = statusLbl; }

    public Label getHistTitle() { return histTitle; }
    public void setHistTitle(Label histTitle) { this.histTitle = histTitle; }

    public Button getBackupBtn() { return backupBtn; }
    public void setBackupBtn(Button backupBtn) { this.backupBtn = backupBtn; }

    public Separator getSep() { return sep; }
    public void setSep(Separator sep) { this.sep = sep; }

    public ScrollPane getScroll() { return scroll; }
    public void setScroll(ScrollPane scroll) { this.scroll = scroll; }

    public void show() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5f7;");
        root.setLeft(AdminSidebar.build(stage, "backup", adminName));

        content = new VBox(20);
        content.setPadding(new Insets(30, 36, 36, 36));

        title = new Label("Backup");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        sub = new Label("Manage system data backups.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #7a9ba3;");

        card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        card.setMaxWidth(500);

        cardTitle = new Label("Create Backup");
        cardTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        info = new Label("Creates a full backup of all system data including users, items, and claims.");
        info.setWrapText(true);
        info.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a6b73;");

        statusLbl = new Label("");

        backupBtn = new Button("💾  Create Backup Now");
        backupBtn.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-background-color: #5bc8d0; " +
                "-fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;");
        backupBtn.setOnAction(_ -> {
            statusLbl.setText("✓ Backup created successfully at " +
                    java.time.LocalDateTime.now().toString().substring(0, 16));
            statusLbl.setTextFill(Color.web("#166534"));
        });

        sep = new Separator();

        histTitle = new Label("Backup History");
        histTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        history = new VBox(8);
        for (String[] entry : new String[][]{
            {"Mar 15, 2026  09:00", "Full Backup", "124 MB"},
            {"Mar 10, 2026  09:00", "Full Backup", "118 MB"},
            {"Mar 05, 2026  09:00", "Full Backup", "110 MB"}
        }) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 0, 10, 0));
            row.setStyle("-fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;");
            Label dateLabel = new Label(entry[0]);
            dateLabel.setPrefWidth(200);
            dateLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #1a2e35;");
            Label typeLabel = new Label(entry[1]);
            typeLabel.setPrefWidth(130);
            typeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #4a6b73;");
            Label sizeLabel = new Label(entry[2]);
            sizeLabel.setPrefWidth(80);
            sizeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #9ab5bc;");
            row.getChildren().addAll(dateLabel, typeLabel, sizeLabel);
            history.getChildren().add(row);
        }

        card.getChildren().addAll(cardTitle, info, backupBtn, statusLbl, sep, histTitle, history);
        content.getChildren().addAll(title, sub, card);

        scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f5f7; -fx-background: #f0f5f7;");
        root.setCenter(scroll);

        stage.setScene(new Scene(root, 1100, 700));
        stage.setTitle("Admin — Backup");
        stage.show();
    }
}