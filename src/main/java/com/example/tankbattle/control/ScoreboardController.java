package com.example.tankbattle.control;

import com.example.tankbattle.TankBattleApplication;
import com.example.tankbattle.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreboardController implements Initializable {

    @FXML
    public TableView<Player> tableView;

    public ObservableList<Player> scoreboard;

    @FXML
    public TableColumn<Player, String> nameColumn;

    @FXML
    public TableColumn<Player, Integer> victoryColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scoreboard = FXCollections.observableArrayList();
        scoreboard.addAll(Singletone.scoreboard);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        victoryColumn.setCellValueFactory(new PropertyValueFactory<>("victories"));
        tableView.setItems(scoreboard);
    }

    public void onBackToMenuBtn(){
        TankBattleApplication.showWindow("startMenu.fxml");
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }
}
