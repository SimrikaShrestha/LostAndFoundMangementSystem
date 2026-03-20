package view;

import controller.AdminCategoryController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Category;

public class AdminCategoriesPage {
    private Stage stage;
    private String adminName;
    private AdminCategoryController controller = new AdminCategoryController();
    private VBox tableBody;

    public AdminCategoriesPage(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5f7;");
        root.setLeft(AdminSidebar.build(stage, "categories", adminName));

        VBox content = new VBox(20);
        content.setPadding(new Insets(30, 36, 36, 36));

        HBox topBar = new HBox(14);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Categories");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        HBox.setHgrow(title, Priority.ALWAYS);

        Button addBtn = new Button("+ Add Category");
        addBtn.setStyle(primaryBtn());
        addBtn.setCursor(javafx.scene.Cursor.HAND);
        addBtn.setOnAction(_ -> showDialog(null));
        topBar.getChildren().addAll(title, addBtn);

        VBox table = new VBox(0);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        HBox header = new HBox();
        header.setPadding(new Insets(12, 20, 12, 20));
        header.setStyle("-fx-background-color: #f8fbfc; -fx-background-radius: 12 12 0 0; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;");
        header.getChildren().addAll(hcol("CATEGORY NAME", 200), hcol("DESCRIPTION", 320), hcol("ITEMS", 100), hcol("ACTIONS", 160));

        tableBody = new VBox(0);
        refresh();
        table.getChildren().addAll(header, tableBody);
        content.getChildren().addAll(topBar, table);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f5f7; -fx-background: #f0f5f7;");
        root.setCenter(scroll);

        stage.setScene(new Scene(root, 1100, 700));
        stage.setTitle("Admin — Categories");
        stage.show();
    }

    private void refresh() {
        tableBody.getChildren().clear();
        for (Category cat : controller.getAllCategories()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(13, 20, 13, 20));
            row.setStyle("-fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;");
            row.setOnMouseEntered(_ -> row.setStyle("-fx-background-color: #f8fbfc; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;"));
            row.setOnMouseExited(_ -> row.setStyle("-fx-background-color: transparent; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;"));

            Label name = col(cat.getName(), 200, true);
            Label desc = col(cat.getDescription(), 320, false);
            Label count = col(String.valueOf(cat.getItemCount()), 100, false);

            HBox actions = new HBox(8); actions.setPrefWidth(160); actions.setAlignment(Pos.CENTER_LEFT);
            Button edit = new Button("Edit");
            edit.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: #e8f7f8; -fx-text-fill: #2a7a85; -fx-background-radius: 6; -fx-padding: 5 10; -fx-cursor: hand;");
            edit.setOnAction(_ -> showDialog(cat));
            Button del = new Button("Delete");
            del.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-background-radius: 6; -fx-padding: 5 10; -fx-cursor: hand;");
            del.setOnAction(_ -> {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete category \"" + cat.getName() + "\"?", ButtonType.YES, ButtonType.NO);
                a.setHeaderText(null);
                a.showAndWait().ifPresent(b -> { if (b == ButtonType.YES) { controller.deleteCategory(cat.getId()); refresh(); } });
            });
            actions.getChildren().addAll(edit, del);
            row.getChildren().addAll(name, desc, count, actions);
            tableBody.getChildren().add(row);
        }
        if (tableBody.getChildren().isEmpty()) {
            Label e = new Label("No categories found."); e.setPadding(new Insets(20));
            e.setStyle("-fx-font-size: 13px; -fx-text-fill: #9ab5bc;");
            tableBody.getChildren().add(e);
        }
    }

    private void showDialog(Category existing) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(existing == null ? "Add Category" : "Edit Category");

        VBox box = new VBox(14);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-background-color: white;");
        box.setPrefWidth(380);

        Label title = new Label(existing == null ? "Add Category" : "Edit Category");
        title.setStyle("-fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        TextField nameField = df("Category Name", existing != null ? existing.getName() : "");
        TextField descField = df("Description", existing != null ? existing.getDescription() : "");

        Label err = new Label(""); err.setStyle("-fx-font-size: 12px; -fx-text-fill: #cc0000;");

        Button save = new Button(existing == null ? "Add Category" : "Save Changes");
        save.setMaxWidth(Double.MAX_VALUE);
        save.setStyle(primaryBtn());
        save.setOnAction(_ -> {
            String n = nameField.getText().trim(), d = descField.getText().trim();
            if (n.isEmpty()) { err.setText("Category name required."); return; }
            if (existing == null) controller.addCategory(n, d);
            else controller.updateCategory(existing.getId(), n, d);
            dialog.close(); refresh();
        });

        box.getChildren().addAll(title, nameField, descField, err, save);
        dialog.setScene(new Scene(box));
        dialog.showAndWait();
    }

    private TextField df(String p, String v) { TextField f = new TextField(v); f.setPromptText(p); f.setStyle("-fx-font-size: 13px; -fx-background-radius: 8; -fx-border-color: #c5d8dc; -fx-border-radius: 8; -fx-padding: 9 12;"); return f; }
    private Label hcol(String t, double w) { Label l = new Label(t); l.setPrefWidth(w); l.setStyle("-fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #7a9ba3;"); return l; }
    private Label col(String t, double w, boolean b) { Label l = new Label(t); l.setPrefWidth(w); l.setStyle("-fx-font-size: 13px; -fx-font-weight: " + (b?"600":"400") + "; -fx-text-fill: " + (b?"#1a2e35":"#4a6b73") + ";"); return l; }
    private String primaryBtn() { return "-fx-font-size: 13px; -fx-font-weight: 600; -fx-background-color: #5bc8d0; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 9 16;"; }
}