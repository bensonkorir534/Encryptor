package MrEncrypter;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class Tutorial implements Initializable {

    @FXML
    private MediaView mediaME;
    private MediaPlayer mediaP;
    private Media media;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String path = new File("UserGuide.mp4").getAbsolutePath();
        media = new Media(new File(path).toURI().toString());
        mediaP = new MediaPlayer(media);
        mediaME.setMediaPlayer(mediaP);
        mediaP.setAutoPlay(true);

    }

    public void BtnPlay(javafx.event.ActionEvent event) {
        mediaP.play();
    }

    public void BtnStop(javafx.event.ActionEvent event) {
        mediaP.stop();

    }

    public void BtnPause(javafx.event.ActionEvent event) {
        mediaP.pause();
    }

//    public void timeSlider(MouseEvent mouseEvent) {
//        timeSlider.setOnDragDetected(new EventHandler<MouseEvent>(){
//            @Override
//            public void handle(MouseEvent event) {
//                mediaP.seek(Duration.seconds(timeSlider.getValue()));
//            }
//        });
//    }
}