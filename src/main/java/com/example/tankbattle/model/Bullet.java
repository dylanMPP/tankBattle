package com.example.tankbattle.model;

import com.example.tankbattle.TankBattleApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Bullet {
    private Canvas canvas;
    private GraphicsContext gc;
    public Vector pos;
    public Vector direction;

    public int x,y;

    public Image image;

    public Bullet(Canvas canvas,Vector pos, Vector direction) {
        this.canvas = canvas;

        //String path = "file:" + TankBattleApplication.class.getResource("circleBullet.png").getPath();
        try{
            System.out.println(System.getProperty("user.dir"));
            this.image = new Image(new FileInputStream("src/data/circleBullet.png"));
        } catch (FileNotFoundException fileNotFoundException){
            throw new RuntimeException();
        }


        gc = canvas.getGraphicsContext2D();
        this.pos = pos;
        this.direction = direction;
    }

    public Circle draw(String pathImage){
        double xAux = pos.x-2;
        double yAux = pos.y-2;

        // String path = "file:" + TankBattleApplication.class.getResource("circleBullet.png").getPath();
        // Image image = new Image(path);
        gc.drawImage(this.image, pos.x, pos.y, 10, 10);

        //if(pathImage.contains("red")){
        //    gc.setFill(Color.RED);
        //} else if(pathImage.contains("blue")){
        //    gc.setFill(Color.BLUE);
        //} else if(pathImage.contains("yellow")){
        //    gc.setFill(Color.YELLOW);
        //}

        gc.fillOval(pos.x, pos.y,10,10);
        // Sumo los dos vectores, es como si fuesen paralelos, entonces inicio desde la posición
        // y a ese vector le voy sumando cada que dibujo la dirección, para que se vaya extendiendo
        // la bala
        pos.x += (direction.x*3.5);
        pos.y += (direction.y*3.5);
        // Retorno un circulo para ir actualizando las balas en el array list en la controller
        // pues necesito su posición cuando se está pintando
        return new Circle(xAux, yAux, 5);
    }
}
