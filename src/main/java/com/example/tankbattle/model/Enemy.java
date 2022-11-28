package com.example.tankbattle.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Enemy {

    private Canvas canvas;
    private GraphicsContext gc;
    public int x,y;
    public Rectangle rectangle;

    public Enemy(Canvas canvas, int x, int y){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.x = x;
        this.y = y;
        rectangle = new Rectangle(this.x, this.y, 50,50);
    }

    public void draw(){
        gc.setFill(Color.BLUE);
        gc.fillRect(x-12.5,y-12.5,25,25);
    }
}
