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

import java.net.URL;
import java.util.ResourceBundle;

public class StartMenuController implements Initializable {
    @FXML
    private Button startBtn;

    @FXML
    private ImageView startImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("file:" + TankBattleApplication.class.getResource("startBtnImage.png").getPath());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(209);
        imageView.setFitHeight(80);
        startBtn.setGraphic(imageView);
    }


    public void onStartBtnClick() {
        TankBattleApplication.showWindow("selectPlayers.fxml");
        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.close();
    }
}
