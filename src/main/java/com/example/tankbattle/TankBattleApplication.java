package com.example.tankbattle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TankBattleApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TankBattleApplication.class.getResource("map.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tank Battle");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void showWindow(String fxml){
        FXMLLoader fxmlLoader = new FXMLLoader(TankBattleApplication.class.getResource(fxml));

        try{
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioException){
            throw new RuntimeException();
        }
    }
}