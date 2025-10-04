package bd.edu.seu.ticket_booking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginSelection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Movie Ticket Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void changeScene(String fxml)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setScene(scene);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}