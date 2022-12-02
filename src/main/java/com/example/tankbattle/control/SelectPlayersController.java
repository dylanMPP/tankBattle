package com.example.tankbattle.control;

import com.example.tankbattle.TankBattleApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectPlayersController implements Initializable {
    @FXML
    public Button startBtn;

    @FXML
    public TextField playerOneTxtField;

    @FXML
    public TextField playerTwoTxtField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("file:" + TankBattleApplication.class.getResource("startBtnImage.png").getPath());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(209);
        imageView.setFitHeight(80);
        startBtn.setGraphic(imageView);
    }

    public void onStartBtnClick(){
        if(playerOneTxtField.getText()!=null && !(playerOneTxtField.getText().equals("")) &&
                playerTwoTxtField.getText()!=null && !(playerTwoTxtField.getText().equals(""))){
            Singletone.getInstance().playerOne = playerOneTxtField.getText();
            Singletone.getInstance().playerTwo = playerTwoTxtField.getText();
            TankBattleApplication.showWindow("map.fxml");
            Stage stage = (Stage) playerOneTxtField.getScene().getWindow();
            stage.close();
        }
    }
}
