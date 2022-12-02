package com.example.tankbattle.control;

import com.example.tankbattle.TankBattleApplication;
import com.example.tankbattle.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WinnerController implements Initializable {
    @FXML
    public Label playerWinnerLbl;
    public String winner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        winner = Singletone.getInstance().winner;

        boolean isWinner = false;

        for (int i = 0; i < Singletone.scoreboard.size(); i++) {
            if(Singletone.scoreboard.get(i).name.equals(winner)){
                Singletone.scoreboard.get(i).victories += 1;
                isWinner = true;
                break;
            }
        }

        if(!isWinner){
            Player player = new Player(winner, 1);
            Singletone.scoreboard.add(player);
            Singletone.getInstance().save();
        }

        playerWinnerLbl.setText(winner);
    }

    public void onScoreboardBtnClick(ActionEvent actionEvent) {
        TankBattleApplication.showWindow("scoreboard.fxml");
        Stage stage = (Stage) playerWinnerLbl.getScene().getWindow();
        stage.close();
    }
}
