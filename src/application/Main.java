package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginPage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            LoginPage login = new LoginPage(primaryStage);
            login.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}