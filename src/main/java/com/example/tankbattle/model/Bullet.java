package com.example.tankbattle.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class Bullet {
    private Canvas canvas;
    private GraphicsContext gc;
    public Vector pos;
    public Vector direction;

    public int x,y;

    public Bullet(Canvas canvas,Vector pos, Vector direction) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.pos = pos;
        this.direction = direction;
    }

    public Circle draw(){
        double xAux = pos.x-2;
        double yAux = pos.y-2;
        gc.setFill(Color.BLUE);
        gc.fillOval(pos.x-2, pos.y-2,10,10);
        // Sumo los dos vectores, es como si fuesen paralelos, entonces inicio desde la posición
        // y a ese vector le voy sumando cada que dibujo la dirección, para que se vaya extendiendo
        // la bala
        pos.x += direction.x;
        pos.y += direction.y;
        return new Circle(xAux, yAux, 5);
    }
}
