package com.example.tankbattle.control;

import com.example.tankbattle.TankBattleApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartMenuController {

    @FXML
    private Button startBtn;

    public void onStartBtnClick(){
        TankBattleApplication.showWindow("selectPlayers.fxml");
        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.close();
    }
}
