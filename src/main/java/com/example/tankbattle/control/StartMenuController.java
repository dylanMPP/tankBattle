package com.example.tankbattle.control;

import com.example.tankbattle.TankBattleApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable {
    @FXML
    private Button startBtn;

    @FXML
    private ImageView startImageView;
    Clip clip;

    public void reproduceSong(){
        String uri = TankBattleApplication.class.getResource("song.wav").getPath();
        File musicPath = new File(uri);

        if(musicPath.exists()){

            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(3);

            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Doesn't exist .WAV");
        }

        new Thread(() -> {
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(startBtn.getScene().getWindow().onCloseRequestProperty().isNotNull().getValue()){
                clip.close();
            }
        }).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reproduceSong();

        Image image = new Image("file:" + TankBattleApplication.class.getResource("startBtnImage.png").getPath());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(209);
        imageView.setFitHeight(80);
        startBtn.setGraphic(imageView);
    }


    public void onStartBtnClick() {
        TankBattleApplication.showWindow("selectPlayers.fxml");
        clip.close();
        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.close();
    }
}
