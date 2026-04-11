package view;

import controller.RegisterController;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

public class RegisterPage {

    private static final String BG_COLOR = "#dfeff2";
    private static final String TEAL = "#7fd1d8";
    private static final String TEAL_HOVER = "#5bbfc7";
    private static final String TEAL_PRESSED = "#3aa8b0";
    private static final String FIELD_BG = "#f5fbfc";
    private static final String FIELD_BORDER = "#dde8ea";
    private static final String TEXT_PRIMARY = "#1a1a1a";
    private static final String TEXT_MUTED = "#888888";

    private final Stage primaryStage;
    private final RegisterController registerController;

    public RegisterPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.registerController = new RegisterController();
    }

    private String fieldStyle() {
        return "-fx-background-color: " + FIELD_BG + ";" +
               "-fx-border-color: " + FIELD_BORDER + ";" +
               "-fx-border-radius: 8;" +
               "-fx-background-radius: 8;" +
               "-fx-font-size: 14px;" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";" +
               "-fx-padding: 8 12;";
    }

    private String primaryBtnStyle(boolean hover, boolean pressed) {
        String bg = pressed ? TEAL_PRESSED : hover ? TEAL_HOVER : TEAL;
        return "-fx-background-color: " + bg + ";" +
               "-fx-text-fill: black;" +
               "-fx-font-size: 14px;" +
               "-fx-font-weight: bold;" +
               "-fx-background-radius: 10;" +
               "-fx-cursor: hand;" +
               "-fx-padding: 10;";
    }

    private String linkBtnStyle(boolean hover) {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: #2a7a85;" +
               "-fx-font-size: 12px;" +
               "-fx-font-weight: bold;" +
               "-fx-cursor: hand;" +
               "-fx-underline: " + hover + ";" +
               "-fx-border-color: transparent;";
    }

    private TextField styledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(fieldStyle());
        return f;
    }

    public void show() {

        Label titleLabel = new Label("Lost and Found Management System");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Label subtitleLabel = new Label("Create Account");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2a7a85;");

        TextField fullnameField = styledField("Full Name");
        TextField usernameField = styledField("Username");
        TextField emailField = styledField("Email Address");
        TextField phoneField = styledField("Phone Number");
        TextField addressField = styledField("Address");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(fieldStyle());

        TextField passwordVisible = new TextField();
        passwordVisible.setPromptText("Password");
        passwordVisible.setStyle(fieldStyle());
        passwordVisible.setVisible(false);
        passwordVisible.setManaged(false);

        SVGPath eyeIcon = new SVGPath();
        eyeIcon.setContent("M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z M12 9a3 3 0 1 0 0 6 3 3 0 0 0 0-6z");
        eyeIcon.setFill(Color.TRANSPARENT);
        eyeIcon.setStroke(Color.web("#aac5c8"));
        eyeIcon.setStrokeWidth(1.6);

        Button eyeButton = new Button();
        eyeButton.setGraphic(eyeIcon);
        eyeButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 4;");

        final boolean[] pwVisible = {false};
        eyeButton.setOnAction(e -> {
            pwVisible[0] = !pwVisible[0];
            if (pwVisible[0]) {
                passwordVisible.setText(passwordField.getText());
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordVisible.setVisible(true);
                passwordVisible.setManaged(true);
                eyeIcon.setStroke(Color.web(TEAL));
            } else {
                passwordField.setText(passwordVisible.getText());
                passwordVisible.setVisible(false);
                passwordVisible.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                eyeIcon.setStroke(Color.web("#aac5c8"));
            }
        });

        StackPane pwStack = new StackPane(passwordField, passwordVisible, eyeButton);
        StackPane.setAlignment(eyeButton, Pos.CENTER_RIGHT);

        fullnameField.setPrefWidth(140);
        usernameField.setPrefWidth(140);
        phoneField.setPrefWidth(140);
        addressField.setPrefWidth(140);
        emailField.setPrefWidth(290);
        pwStack.setPrefWidth(290);

        HBox topRow = new HBox(6, fullnameField, usernameField);
        topRow.setAlignment(Pos.CENTER);

        HBox midRow = new HBox(6, phoneField, addressField);
        midRow.setAlignment(Pos.CENTER);

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(290);
        registerButton.setStyle(primaryBtnStyle(false, false));
        registerButton.setOnMouseEntered(e -> registerButton.setStyle(primaryBtnStyle(true, false)));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(primaryBtnStyle(false, false)));
        registerButton.setOnMousePressed(e -> registerButton.setStyle(primaryBtnStyle(false, true)));
        registerButton.setOnMouseReleased(e -> registerButton.setStyle(primaryBtnStyle(true, false)));

        Button loginButton = new Button("← Back to Login");
        loginButton.setPrefWidth(290);
        loginButton.setStyle(linkBtnStyle(false));
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(linkBtnStyle(true)));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(linkBtnStyle(false)));

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #c0392b; -fx-font-size: 12px;");

        registerButton.setOnAction(e -> {
            String fullname = fullnameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String username = usernameField.getText().trim();
            String password = pwVisible[0] ? passwordVisible.getText().trim() : passwordField.getText().trim();

            if (fullname.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("All fields are required!");
                return;
            }

            if (!fullname.matches("[a-zA-Z\\s]+")) {
                messageLabel.setText("Full name should contain only letters!");
                return;
            }

            if (!phone.matches("\\d+")) {
                messageLabel.setText("Phone number should contain only digits!");
                return;
            }

            if (password.length() < 4) {
                messageLabel.setText("Password must be at least 4 characters!");
                return;
            }

            User newUser = new User(0, fullname, email, phone, address, username, password, "User");
            boolean success = registerController.registerUser(newUser);

            if (success) {
                messageLabel.setStyle("-fx-text-fill: #1a7a4a;");
                messageLabel.setText("Registration Successful!");

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(ev -> new LoginPage(primaryStage).show());
                pause.play();
            } else {
                messageLabel.setText("Registration Failed!");
            }
        });

        loginButton.setOnAction(_ -> new LoginPage(primaryStage).show());

        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(350);
        formBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        formBox.getChildren().addAll(
            titleLabel, subtitleLabel,
            topRow, emailField, midRow, pwStack,
            messageLabel, registerButton, loginButton
        );

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");
        root.getChildren().add(formBox);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Register");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
}