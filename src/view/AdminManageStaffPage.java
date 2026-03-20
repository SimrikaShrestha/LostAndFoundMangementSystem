package view;

import controller.AdminStaffController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Staff;

public class AdminManageStaffPage {
    private Stage stage;
    private String adminName;
    private AdminStaffController controller = new AdminStaffController();
    private VBox tableBody;

    public AdminManageStaffPage(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5f7;");
        root.setLeft(AdminSidebar.build(stage, "staff", adminName));

        VBox content = new VBox(20);
        content.setPadding(new Insets(30, 36, 36, 36));

        HBox topBar = new HBox(14);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Manage Staff");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        HBox.setHgrow(title, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Search staff...");
        search.setPrefWidth(200);
        search.setStyle(fieldStyle());
        search.textProperty().addListener((_, _, n) -> refresh(n.trim()));

        Button addBtn = new Button("+ Add Staff");
        addBtn.setStyle(primaryBtn());
        addBtn.setCursor(javafx.scene.Cursor.HAND);
        addBtn.setOnAction(_ -> showDialog(null));
        topBar.getChildren().addAll(title, search, addBtn);

        VBox table = new VBox(0);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        HBox header = new HBox();
        header.setPadding(new Insets(12, 20, 12, 20));
        header.setStyle("-fx-background-color: #f8fbfc; -fx-background-radius: 12 12 0 0; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;");
        header.getChildren().addAll(hcol("STAFF", 220), hcol("EMAIL", 190), hcol("DEPARTMENT", 160), hcol("STATUS", 110), hcol("ACTIONS", 160));

        tableBody = new VBox(0);
        refresh("");
        table.getChildren().addAll(header, tableBody);
        content.getChildren().addAll(topBar, table);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f5f7; -fx-background: #f0f5f7;");
        root.setCenter(scroll);

        stage.setScene(new Scene(root, 1100, 700));
        stage.setTitle("Admin — Manage Staff");
        stage.show();
    }

    private void refresh(String search) {
        tableBody.getChildren().clear();
        for (Staff s : controller.getAllStaff()) {
            if (!search.isEmpty() && !ns(s.getFullname()).toLowerCase().contains(search.toLowerCase())
                && !ns(s.getDepartment()).toLowerCase().contains(search.toLowerCase())) continue;
            tableBody.getChildren().add(buildRow(s));
        }
        if (tableBody.getChildren().isEmpty()) {
            Label e = new Label("No staff found.");
            e.setPadding(new Insets(20)); e.setStyle("-fx-font-size: 13px; -fx-text-fill: #9ab5bc;");
            tableBody.getChildren().add(e);
        }
    }

    private HBox buildRow(Staff staff) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(13, 20, 13, 20));
        row.setStyle("-fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;");
        row.setOnMouseEntered(_ -> row.setStyle("-fx-background-color: #f8fbfc; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;"));
        row.setOnMouseExited(_ -> row.setStyle("-fx-background-color: transparent; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;"));

        String name = ns(staff.getFullname());
        String[] parts = name.split(" ");
        String initials = parts.length >= 2 ? (parts[0].substring(0,1) + parts[parts.length-1].substring(0,1)).toUpperCase() : name.isEmpty() ? "?" : name.substring(0,1).toUpperCase();
        Circle av = new Circle(15, Color.web("#d0eef1"));
        Label ini = new Label(initials); ini.setStyle("-fx-font-size: 9px; -fx-font-weight: 700; -fx-text-fill: #2a7a85;");
        HBox staffCell = new HBox(10, new StackPane(av, ini), col(name.isEmpty() ? staff.getUsername() : name, 180, true));
        staffCell.setPrefWidth(220); staffCell.setAlignment(Pos.CENTER_LEFT);

        Label email = col(ns(staff.getEmail()), 190, false);
        Label dept = col(ns(staff.getDepartment()), 160, false);

        boolean active = "Active".equals(staff.getStatus());
        Label status = new Label(staff.getStatus());
        status.setPrefWidth(110);
        status.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; " +
                "-fx-background-color: " + (active ? "#dcfce7" : "#fee2e2") + "; " +
                "-fx-text-fill: " + (active ? "#166534" : "#991b1b") + "; -fx-background-radius: 20; -fx-padding: 3 10;");

        HBox actions = new HBox(8); actions.setPrefWidth(160); actions.setAlignment(Pos.CENTER_LEFT);
        Button edit = new Button("Edit");
        edit.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: #e8f7f8; -fx-text-fill: #2a7a85; -fx-background-radius: 6; -fx-padding: 5 10; -fx-cursor: hand;");
        edit.setOnAction(_ -> showDialog(staff));
        Button del = new Button("Delete");
        del.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-background-radius: 6; -fx-padding: 5 10; -fx-cursor: hand;");
        del.setOnAction(_ -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete \"" + staff.getFullname() + "\"?", ButtonType.YES, ButtonType.NO);
            a.setHeaderText(null);
            a.showAndWait().ifPresent(b -> { if (b == ButtonType.YES) { controller.deleteStaff(staff.getId()); refresh(""); } });
        });
        actions.getChildren().addAll(edit, del);
        row.getChildren().addAll(staffCell, email, dept, status, actions);
        return row;
    }

    private void showDialog(Staff existing) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(existing == null ? "Add Staff" : "Edit Staff");

        VBox box = new VBox(14);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-background-color: white;");
        box.setPrefWidth(400);

        Label title = new Label(existing == null ? "Add New Staff" : "Edit Staff");
        title.setStyle("-fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        TextField fn = df("Full Name", existing != null ? ns(existing.getFullname()) : "");
        TextField em = df("Email", existing != null ? ns(existing.getEmail()) : "");
        TextField ph = df("Phone", existing != null ? ns(existing.getPhone()) : "");
        TextField dept = df("Department", existing != null ? ns(existing.getDepartment()) : "");
        TextField un = df("Username", existing != null ? existing.getUsername() : "");
        if (existing != null) un.setDisable(true);

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Active", "Inactive");
        statusBox.setValue(existing != null ? existing.getStatus() : "Active");
        statusBox.setMaxWidth(Double.MAX_VALUE);
        Label statusLbl = new Label("Status"); statusLbl.setStyle(lblStyle());

        PasswordField pw = new PasswordField();
        pw.setPromptText(existing != null ? "Leave blank to keep password" : "Password");
        pw.setStyle(dfStyle());
        Label pwLbl = new Label("Password"); pwLbl.setStyle(lblStyle());

        Label err = new Label(""); err.setStyle("-fx-font-size: 12px; -fx-text-fill: #cc0000;");

        Button save = new Button(existing == null ? "Add Staff" : "Save Changes");
        save.setMaxWidth(Double.MAX_VALUE);
        save.setStyle(primaryBtn());
        save.setOnAction(_ -> {
            if (un.getText().trim().isEmpty()) { err.setText("Username required."); return; }
            if (existing == null) {
                if (pw.getText().trim().isEmpty()) { err.setText("Password required."); return; }
                controller.addStaff(fn.getText().trim(), em.getText().trim(), ph.getText().trim(), dept.getText().trim(), un.getText().trim(), pw.getText().trim());
            } else {
                controller.updateStaff(existing.getId(), fn.getText().trim(), em.getText().trim(), ph.getText().trim(), dept.getText().trim(), statusBox.getValue());
            }
            dialog.close(); refresh("");
        });

        box.getChildren().addAll(title, fn, em, ph, dept, un, new VBox(6, statusLbl, statusBox), new VBox(6, pwLbl, pw), err, save);
        dialog.setScene(new Scene(box));
        dialog.showAndWait();
    }

    private TextField df(String p, String v) { TextField f = new TextField(v); f.setPromptText(p); f.setStyle(dfStyle()); return f; }
    private String dfStyle() { return "-fx-font-size: 13px; -fx-background-radius: 8; -fx-border-color: #c5d8dc; -fx-border-radius: 8; -fx-padding: 9 12;"; }
    private String lblStyle() { return "-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #4a6b73;"; }
    private Label hcol(String t, double w) { Label l = new Label(t); l.setPrefWidth(w); l.setStyle("-fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #7a9ba3;"); return l; }
    private Label col(String t, double w, boolean b) { Label l = new Label(t); l.setPrefWidth(w); l.setStyle("-fx-font-size: 13px; -fx-font-weight: " + (b?"600":"400") + "; -fx-text-fill: " + (b?"#1a2e35":"#4a6b73") + ";"); return l; }
    private String fieldStyle() { return "-fx-font-size: 13px; -fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #c5d8dc; -fx-border-radius: 8; -fx-padding: 8 12;"; }
    private String primaryBtn() { return "-fx-font-size: 13px; -fx-font-weight: 600; -fx-background-color: #5bc8d0; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 9 16;"; }
    private String ns(String s) { return s != null ? s : ""; }
}