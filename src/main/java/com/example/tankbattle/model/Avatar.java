package com.example.tankbattle.model;

import com.example.tankbattle.TankBattleApplication;
import com.example.tankbattle.TankBattleApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Avatar {

    // Cada tanque tiene su propio canvas, ya que cuando se mueve
    // cada tanque, el canvas se traslada y se gira.
    private Canvas canvas;
    private GraphicsContext gc;
    private Image tank;
    public Vector pos;
    public Vector direction;
    public Rectangle rectangle;

    public Avatar(Canvas canvas){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        String uri = "file:"+ TankBattleApplication.class.getResource("redTank.png").getPath();
        tank = new Image(uri);
        pos = new Vector(50,50);
        // la pos del canvas debe ser pequeña para que no gire rápido
        direction = new Vector(2,2);
        rectangle = new Rectangle(pos.x, pos.y, 50, 50);
    }

    public void draw(){
        gc.save();
        // Es el canvas quien se gira y se mueve, no el tanque.
        // Lo trasladamos para que el tanque se traslade de posición
        gc.translate(pos.x, pos.y);
        // 90 para que el canvas gire bien
        gc.rotate(90+direction.getAngle());
        gc.drawImage(tank, -25,-25, 50,50);
        // restore para que el canvas vuelva a su estado original
        gc.restore();
    }
    public void setPosition(double x, double y) {
        pos.x = (int) x - 25;
        pos.y = (int) y - 25;
    }

    public void changeAngle(double a){
        double amp = direction.getAmplitude();
        double angle = direction.getAngle();
        angle += a;
        direction.x = amp*Math.cos(Math.toRadians(angle));
        direction.y = amp*Math.sin(Math.toRadians(angle));
    }

    public void moveForward(){
        pos.x += direction.x;
        pos.y += direction.y;
    }

    public void moveBackward() {
        pos.x -= direction.x;
        pos.y -= direction.y;
    }
}

