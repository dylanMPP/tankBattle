package com.example.tankbattle.control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class WinnerController implements Initializable {
    @FXML
    public Label playerWinnerLbl;
    public String winner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        winner = Singletone.getInstance().winner;
        playerWinnerLbl.setText(winner);
    }
}
