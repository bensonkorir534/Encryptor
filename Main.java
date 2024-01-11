package MrEncrypter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
        primaryStage.setTitle("Mr.Encrypter");
        primaryStage.setScene(new Scene(root, 520, 390));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream( "logo.png" )));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
