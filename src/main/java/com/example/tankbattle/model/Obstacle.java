package com.example.tankbattle.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Obstacle {

    private Canvas canvas;
    private GraphicsContext gc;
    public int x,y;
    public Shape rectangle;
    public Image image;

    public Obstacle(Canvas canvas, String imagePath, int x, int y){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        image = new Image(imagePath);
        this.x = x;
        this.y = y;
        // Creo el rectangulo en la misma posición y con el mismo size que el rectangulo que pinto de cada objeto
        rectangle = new Rectangle(this.x-20, this.y-20, 40,40);
    }

    public void draw(){
        gc.drawImage(image, x-20, y-20, 40, 40);
    }
}
