package com.example.tankbattle.control;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ThreadTank extends Thread{

    private GraphicsContext gc;
    private Canvas tankOneCanvas;

    public void paint(Canvas tankOneCanvas) throws FileNotFoundException {
        this.tankOneCanvas = tankOneCanvas;
        gc = this.tankOneCanvas.getGraphicsContext2D();

        Thread thread = new Thread(() -> {
            Image tankOne = null;

            try {
                tankOne = new Image(new FileInputStream("data/prueba.png"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            while(true){
                gc.drawImage(tankOne, 0,0, 50,50);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public Canvas getTankOneCanvas(){
        return this.tankOneCanvas;
    }
}
