package MrEncrypter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SplashScreenController implements Initializable {

    @FXML
    StackPane splash;

    class ShowSplashScreen extends Thread{
        @Override
        public void run(){
            try {
//                Set time for the splash screen (currently 3 seconds)
                Thread.sleep(3000);

                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    Parent root = null;
                    try {
//                        After 3 seconds call our main file
                        root = FXMLLoader.load(getClass().getResource("Home.fxml"));
                    } catch (IOException ex) {
                        Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Scene scene = new Scene(root, 520, 390);
                    stage.setResizable(false);
//                    stage.getIcons().add(new Image("./MrEncrypter/logo.png"));
                    stage.setScene(scene);
                    stage.show();

                    splash.getScene().getWindow().hide();
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new ShowSplashScreen().start();
    }
}
