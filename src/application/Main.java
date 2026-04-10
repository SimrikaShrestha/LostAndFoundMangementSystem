package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginPage;


public class Main extends Application {  // Inheritance: Main class is inheriting from Application class

    @Override
    public void start(Stage primaryStage) {   
        try {
            // Create login page and pass the main window (stage) to it
            LoginPage login = new LoginPage(primaryStage);
            
            login.show();   // Show the login page 
            
        } catch (Exception e) {
            e.printStackTrace();   // Print error details if something goes wrong
        }
    }

    public static void main(String[] args) {
        launch(args);   // Starts the JavaFX application
    }
}