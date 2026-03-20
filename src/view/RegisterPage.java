package view;

import controller.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.User;

public class RegisterPage {
    private Stage stage;

    public RegisterPage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        RegisterController controller = new RegisterController();

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

        Label title = new Label("Register New User");
        title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");

        Label subtitle = new Label("Lost and Found System");
        subtitle.setTextFill(Color.GRAY);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");
        Label message = new Label();

        registerBtn.setOnAction(_ -> {
            User user = new User(
                0,
                fullNameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                addressField.getText(),
                usernameField.getText(),
                passwordField.getText(),
                "user"
            );
            controller.registerUser(user); 
            message.setText("User Registered Successfully");
            message.setTextFill(Color.GREEN);
            new DashboardPage(stage).show(); 
        });

        backBtn.setOnAction(_ -> {
            LoginPage loginPage = new LoginPage(stage);
            loginPage.show();
        });

        card.getChildren().addAll(
                title,
                subtitle,
                fullNameField,
                emailField,
                phoneField,
                addressField,
                usernameField,
                passwordField,
                registerBtn,
                backBtn,
                message
        );

        root.getChildren().add(card);
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("Register Page");
        stage.setScene(scene);
        stage.show();
    }
}