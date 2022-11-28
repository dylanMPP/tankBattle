package com.example.tankbattle.control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class ThreadMap extends Thread implements Initializable{

    @FXML
    private Canvas mapCanvas;
    private GraphicsContext gc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void paint(Canvas mapCanvas){
        this.mapCanvas = mapCanvas;
        gc = this.mapCanvas.getGraphicsContext2D();

        Thread thread = new Thread(() -> {
            gc.setFill(Color.rgb(0,0,0));
            gc.fillRect(0,0,mapCanvas.getWidth(), mapCanvas.getHeight());
        });
        thread.setDaemon(true);
        thread.start();
    }
}
