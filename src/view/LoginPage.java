package view;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginPage {

    private Stage stage;

    public LoginPage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        LoginController loginController = new LoginController();

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#dfeff2;");

        VBox card = new VBox(15);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(350);
        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:10;" +
                "-fx-effect:dropshadow(three-pass-box, rgba(0,0,0,0.1),10,0,0,5);"
        );

        Label title = new Label("Lost and Found Management System");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
        title.setWrapText(true);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label subtitle = new Label("Login");
        subtitle.setTextFill(Color.GRAY);

        Label roleLabel = new Label("Select Role");
        roleLabel.setStyle("-fx-font-size:12px; -fx-text-fill:gray;");

        Button userBtn  = roleButton("User");
        Button staffBtn = roleButton("Staff");
        Button adminBtn = roleButton("Admin");

        final String[] selectedRole = {"user"};
        highlightRole(userBtn, staffBtn, adminBtn, "user");

        userBtn.setOnAction(_ -> {
            selectedRole[0] = "user";
            highlightRole(userBtn, staffBtn, adminBtn, "user");
        });

        staffBtn.setOnAction(_ -> {
            selectedRole[0] = "staff";
            highlightRole(userBtn, staffBtn, adminBtn, "staff");
        });

        adminBtn.setOnAction(_ -> {
            selectedRole[0] = "admin";
            highlightRole(userBtn, staffBtn, adminBtn, "admin");
        });

        HBox roleBox = new HBox(10, userBtn, staffBtn, adminBtn);
        roleBox.setAlignment(Pos.CENTER);
        // ────────────────────────────────────────────────────────

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(Double.MAX_VALUE);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(Double.MAX_VALUE);

        Button loginBtn = new Button("Sign In");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-font-weight:bold; -fx-background-radius:8; -fx-padding:10;");

        Button registerBtn = new Button("Go to Register");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle("-fx-background-color:transparent; -fx-text-fill:#2a7a85; -fx-font-size:12px;");

        Label message = new Label();

        loginBtn.setOnAction(_ -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                message.setText("Please enter username and password.");
                message.setTextFill(Color.RED);
                return;
            }

            boolean success = loginController.loginUser(username, password);
            if (success) {
                // Role still comes from database — role buttons are visual only
                String role = loginController.getCurrentUserRole();
                switch (role != null ? role.toLowerCase() : "user") {
                    case "admin" -> new AdminDashboardPage(stage, username).show();
                    case "staff" -> new StaffDashboardPage(stage, username).show();
                    default      -> new DashboardPage(stage).show();
                }
            } else {
                message.setText("Invalid username or password.");
                message.setTextFill(Color.RED);
            }
        });

        registerBtn.setOnAction(_ -> new RegisterPage(stage).show());

        card.getChildren().addAll(
                title, subtitle,
                roleLabel, roleBox,        // ← new role buttons added here
                usernameField, passwordField,
                loginBtn, registerBtn,
                message
        );

        root.getChildren().add(card);
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Login — Lost and Found");
        stage.show();
    }

    private Button roleButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(90);
        btn.setStyle(
            "-fx-background-color:#f0f0f0;" +
            "-fx-text-fill:#555555;" +
            "-fx-background-radius:20;" +
            "-fx-font-size:12px;" +
            "-fx-padding:6 12;"
        );
        return btn;
    }

    private void highlightRole(Button userBtn, Button staffBtn, Button adminBtn, String role) {
        String active   = "-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-font-weight:bold; -fx-background-radius:20; -fx-font-size:12px; -fx-padding:6 12;";
        String inactive = "-fx-background-color:#f0f0f0; -fx-text-fill:#555555; -fx-background-radius:20; -fx-font-size:12px; -fx-padding:6 12;";

        userBtn.setStyle( role.equals("user")  ? active : inactive);
        staffBtn.setStyle(role.equals("staff") ? active : inactive);
        adminBtn.setStyle(role.equals("admin") ? active : inactive);
    }
}