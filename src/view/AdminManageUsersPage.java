package view;

import controller.AdminUserController;
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
import model.User;

public class AdminManageUsersPage {
    private Stage stage;
    private String adminName;
    private AdminUserController controller = new AdminUserController();
    private VBox tableBody;

    public AdminManageUsersPage(Stage stage, String adminName) {
        this.stage = stage;
        this.adminName = adminName;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f5f7;");
        root.setLeft(AdminSidebar.build(stage, "users", adminName));

        VBox content = new VBox(20);
        content.setPadding(new Insets(30, 36, 36, 36));

        HBox topBar = new HBox(14);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Manage Users");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");
        HBox.setHgrow(title, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Search users...");
        search.setPrefWidth(200);
        search.setStyle(fieldStyle());
        search.textProperty().addListener((_, _, n) -> refresh(n.trim()));

        Button addBtn = new Button("+ Add User");
        addBtn.setStyle(primaryBtn());
        addBtn.setCursor(javafx.scene.Cursor.HAND);
        addBtn.setOnAction(_ -> showDialog(null));
        topBar.getChildren().addAll(title, search, addBtn);

        VBox table = new VBox(0);
        table.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        HBox header = new HBox();
        header.setPadding(new Insets(12, 20, 12, 20));
        header.setStyle("-fx-background-color: #f8fbfc; -fx-background-radius: 12 12 0 0; " +
                "-fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;");
        header.getChildren().addAll(
                hcol("USER", 220), hcol("EMAIL", 200),
                hcol("PHONE", 140), hcol("ROLE", 120), hcol("ACTIONS", 160));

        tableBody = new VBox(0);
        refresh("");
        table.getChildren().addAll(header, tableBody);
        content.getChildren().addAll(topBar, table);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f5f7; -fx-background: #f0f5f7;");
        root.setCenter(scroll);

        stage.setScene(new Scene(root, 1100, 700));
        stage.setTitle("Admin — Manage Users");
        stage.show();
    }

    private void refresh(String search) {
        tableBody.getChildren().clear();
        for (User u : controller.getAllUsers()) {
            if (!search.isEmpty()
                    && !ns(u.getFullname()).toLowerCase().contains(search.toLowerCase())
                    && !ns(u.getUsername()).toLowerCase().contains(search.toLowerCase())) continue;
            tableBody.getChildren().add(buildRow(u));
        }
        if (tableBody.getChildren().isEmpty()) {
            Label e = new Label("No users found.");
            e.setPadding(new Insets(20));
            e.setStyle("-fx-font-size: 13px; -fx-text-fill: #9ab5bc;");
            tableBody.getChildren().add(e);
        }
    }

    private HBox buildRow(User user) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(13, 20, 13, 20));
        row.setStyle("-fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;");
        row.setOnMouseEntered(_ -> row.setStyle(
                "-fx-background-color: #f8fbfc; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;"));
        row.setOnMouseExited(_ -> row.setStyle(
                "-fx-background-color: transparent; -fx-border-color: #eaf1f3; -fx-border-width: 0 0 1 0;"));

        String name = ns(user.getFullname());
        String[] parts = name.trim().split(" ");
        String initials = parts.length >= 2
                ? (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase()
                : name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();

        Circle av = new Circle(15, Color.web("#d0eef1"));
        Label ini = new Label(initials);
        ini.setStyle("-fx-font-size: 9px; -fx-font-weight: 700; -fx-text-fill: #2a7a85;");
        HBox userCell = new HBox(10, new StackPane(av, ini),
                col(name.isEmpty() ? user.getUsername() : name, 180, true));
        userCell.setPrefWidth(220);
        userCell.setAlignment(Pos.CENTER_LEFT);

        Label email = col(ns(user.getEmail()), 200, false);
        Label phone = col(ns(user.getPhone()), 140, false);

        String roleVal = ns(user.getRole());
        Label role = new Label(roleVal.isEmpty() ? "user" : roleVal);
        role.setPrefWidth(120);
        role.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; " +
                "-fx-background-color: #e8f7f8; -fx-text-fill: #2a7a85; " +
                "-fx-background-radius: 20; -fx-padding: 3 10;");

        HBox actions = new HBox(8);
        actions.setPrefWidth(160);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button edit = new Button("Edit");
        edit.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: #e8f7f8; " +
                "-fx-text-fill: #2a7a85; -fx-background-radius: 6; -fx-padding: 5 10; -fx-cursor: hand;");
        edit.setOnAction(_ -> showDialog(user));

        Button del = new Button("Delete");
        del.setStyle("-fx-font-size: 11px; -fx-font-weight: 600; -fx-background-color: #fee2e2; " +
                "-fx-text-fill: #991b1b; -fx-background-radius: 6; -fx-padding: 5 10; -fx-cursor: hand;");
        del.setOnAction(_ -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete user \"" + user.getUsername() + "\"?", ButtonType.YES, ButtonType.NO);
            a.setHeaderText(null);
            a.showAndWait().ifPresent(b -> {
                if (b == ButtonType.YES) {
                    controller.deleteUser(user.getId());
                    refresh("");
                }
            });
        });

        actions.getChildren().addAll(edit, del);
        row.getChildren().addAll(userCell, email, phone, role, actions);
        return row;
    }

    private void showDialog(User existing) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(existing == null ? "Add User" : "Edit User");

        VBox box = new VBox(12);
        box.setPadding(new Insets(28));
        box.setStyle("-fx-background-color: white;");
        box.setPrefWidth(400);

        Label title = new Label(existing == null ? "Add New User" : "Edit User");
        title.setStyle("-fx-font-size: 17px; -fx-font-weight: 700; -fx-text-fill: #1a2e35;");

        TextField fn  = df("Full Name", existing != null ? ns(existing.getFullname()) : "");
        TextField em  = df("Email",     existing != null ? ns(existing.getEmail())    : "");
        TextField ph  = df("Phone",     existing != null ? ns(existing.getPhone())    : "");
        TextField ad  = df("Address",   existing != null ? ns(existing.getAddress())  : "");
        TextField un  = df("Username",  existing != null ? existing.getUsername()     : "");
        if (existing != null) un.setDisable(true);

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("user", "staff", "admin");
        roleBox.setValue(existing != null && !ns(existing.getRole()).isEmpty()
                ? existing.getRole() : "user");
        roleBox.setMaxWidth(Double.MAX_VALUE);
        Label roleLbl = new Label("Role");
        roleLbl.setStyle(lblStyle());

        PasswordField pw = new PasswordField();
        pw.setPromptText(existing != null ? "Leave blank to keep password" : "Password");
        pw.setStyle(dfStyle());
        Label pwLbl = new Label("Password");
        pwLbl.setStyle(lblStyle());

        Label err = new Label("");
        err.setStyle("-fx-font-size: 12px; -fx-text-fill: #cc0000;");

        Button save = new Button(existing == null ? "Add User" : "Save Changes");
        save.setMaxWidth(Double.MAX_VALUE);
        save.setStyle(primaryBtn());
        save.setOnAction(_ -> {
            if (un.getText().trim().isEmpty()) { err.setText("Username required."); return; }
            if (existing == null) {
                if (pw.getText().trim().isEmpty()) { err.setText("Password required."); return; }
                controller.addUser(fn.getText().trim(), em.getText().trim(), ph.getText().trim(),
                        ad.getText().trim(), un.getText().trim(), pw.getText().trim(), roleBox.getValue());
            } else {
                controller.updateUser(existing.getId(), fn.getText().trim(), em.getText().trim(),
                        ph.getText().trim(), ad.getText().trim(), roleBox.getValue());
            }
            dialog.close();
            refresh("");
        });

        box.getChildren().addAll(title, fn, em, ph, ad, un,
                new VBox(6, roleLbl, roleBox),
                new VBox(6, pwLbl, pw),
                err, save);
        dialog.setScene(new Scene(box));
        dialog.showAndWait();
    }

    private TextField df(String prompt, String val) {
        TextField f = new TextField(val);
        f.setPromptText(prompt);
        f.setStyle(dfStyle());
        return f;
    }

    private String dfStyle()    { return "-fx-font-size: 13px; -fx-background-radius: 8; -fx-border-color: #c5d8dc; -fx-border-radius: 8; -fx-padding: 9 12;"; }
    private String lblStyle()   { return "-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #4a6b73;"; }
    private String fieldStyle() { return "-fx-font-size: 13px; -fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #c5d8dc; -fx-border-radius: 8; -fx-padding: 8 12;"; }
    private String primaryBtn() { return "-fx-font-size: 13px; -fx-font-weight: 600; -fx-background-color: #5bc8d0; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 9 16;"; }

    private Label hcol(String t, double w) {
        Label l = new Label(t); l.setPrefWidth(w);
        l.setStyle("-fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #7a9ba3;");
        return l;
    }

    private Label col(String t, double w, boolean b) {
        Label l = new Label(t); l.setPrefWidth(w);
        l.setStyle("-fx-font-size: 13px; -fx-font-weight: " + (b ? "600" : "400") +
                "; -fx-text-fill: " + (b ? "#1a2e35" : "#4a6b73") + ";");
        return l;
    }

    private String ns(String s) { return s != null ? s : ""; }
}