package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_gui.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 480, 640);
        primaryStage.setTitle("OpenDaVINCI_desktop_control");
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setFullScreen(true);

        //primaryStage.setAlwaysOnTop(true);


        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}