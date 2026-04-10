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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

// This is the login page 
// It lets them pick a role, enter credentials, and sign in
public class LoginPage {

    // stage is the main window - kept private so only this class can use it
    private Stage stage;

    // Tracks whether the password is currently visible
    private boolean passwordVisible = false;

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

        // App title
        Label title = new Label("Lost and Found Management System");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
        title.setWrapText(true);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        
        Label subtitle = new Label("Login");
        subtitle.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:#2a7a85;");

       
        Label roleLabel = new Label("Select Role");
        roleLabel.setStyle("-fx-font-size:13px; -fx-font-weight:bold; -fx-text-fill:#2a7a85;");

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

        // Username field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(Double.MAX_VALUE);

        // Password field with eye toggle button
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Visible text field (shown when eye is toggled on)
        TextField passwordVisible_field = new TextField();
        passwordVisible_field.setPromptText("Password");
        passwordVisible_field.setVisible(false);
        passwordVisible_field.setManaged(false);

        // Eye icon button
        Button eyeBtn = new Button();
        eyeBtn.setStyle(
            "-fx-background-color:transparent;" +
            "-fx-cursor:hand;" +
            "-fx-padding:4;"
        );
        eyeBtn.setGraphic(makeEyeIcon(false));

        eyeBtn.setOnAction(_ -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                // Show plain text, sync value
                passwordVisible_field.setText(passwordField.getText());
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordVisible_field.setVisible(true);
                passwordVisible_field.setManaged(true);
            } else {
                // Hide plain text, sync value
                passwordField.setText(passwordVisible_field.getText());
                passwordVisible_field.setVisible(false);
                passwordVisible_field.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
            }
            eyeBtn.setGraphic(makeEyeIcon(passwordVisible));
        });

        // Stack the password field and eye button in an HBox
        HBox passwordBox = new HBox();
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.setStyle(
            "-fx-border-color:#cccccc;" +
            "-fx-border-radius:4;" +
            "-fx-background-color:white;" +
            "-fx-background-radius:4;"
        );

        passwordField.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
        passwordVisible_field.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");

        HBox.setHgrow(passwordField, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(passwordVisible_field, javafx.scene.layout.Priority.ALWAYS);

        passwordBox.getChildren().addAll(passwordField, passwordVisible_field, eyeBtn);
        passwordBox.setMaxWidth(Double.MAX_VALUE);

        // Sign in button
        Button loginBtn = new Button("Sign In");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color:#7fd1d8; -fx-text-fill:black; -fx-font-weight:bold; -fx-background-radius:8; -fx-padding:10;");

        Button registerBtn = new Button("Go to Register");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setStyle("-fx-background-color:transparent; -fx-text-fill:#2a7a85; -fx-font-size:12px;");

        Label message = new Label();

        loginBtn.setOnAction(_ -> {
            String username = usernameField.getText().trim();
            // Get password from whichever field is currently active
            String password = passwordVisible
                ? passwordVisible_field.getText().trim()
                : passwordField.getText().trim();
            String role = selectedRole[0];

            if (username.isEmpty() || password.isEmpty()) {
                message.setText("Please enter username and password.");
                message.setTextFill(Color.RED);
                return;
            }

            boolean success = loginController.loginUser(username, password, role);

            if (success) {
                switch (role) {
                    case "admin" -> new AdminDashboardPage(stage, username).show();
                    case "staff" -> new StaffDashboardPage(stage, username).show();
                    default      -> new DashboardPage(stage).show();
                }
            } else {
                message.setText("Invalid username, password, or role.");
                message.setTextFill(Color.RED);
            }
        });

        registerBtn.setOnAction(_ -> new RegisterPage(stage).show());

        card.getChildren().addAll(
            title, subtitle,
            roleLabel, roleBox,
            usernameField, passwordBox,
            loginBtn, registerBtn,
            message
        );

        root.getChildren().add(card);

        stage.setScene(new Scene(root));
        stage.setTitle("Login — Lost and Found");
        stage.setMaximized(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

   
    private SVGPath makeEyeIcon(boolean isVisible) {
        SVGPath svg = new SVGPath();
        if (isVisible) {
            // Eye OPEN icon
            svg.setContent("M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z");
        } else {
            // Eye CLOSED (slash) icon
            svg.setContent("M12 7c2.76 0 5 2.24 5 5 0 .65-.13 1.26-.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74.25-3.98.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28.46.46C3.08 8.3 1.78 10.02 1 12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-.3 4.38-.84l.42.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-.05.21-.08.43-.08.65 0 1.66 1.34 3 3 3 .22 0 .44-.03.65-.08l1.55 1.55c-.67.33-1.41.53-2.2.53-2.76 0-5-2.24-5-5 0-.79.2-1.53.53-2.2zm4.31-.78l3.15 3.15.02-.16c0-1.66-1.34-3-3-3l-.17.01z");
        }
        svg.setScaleX(0.8);
        svg.setScaleY(0.8);
        svg.setFill(Color.GRAY);
        return svg;
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