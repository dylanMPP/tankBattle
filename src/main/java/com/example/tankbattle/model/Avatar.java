package com.example.tankbattle.model;

import com.example.tankbattle.TankBattleApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.util.ArrayList;

public class Avatar {

    // Cada tanque tiene su propio canvas, ya que cuando se mueve
    // cada tanque, el canvas se traslada y se gira.
    private Canvas canvas;
    private GraphicsContext gc;
    private Image tank;
    public Vector pos;
    public Vector direction;
    public Rectangle rectangle;
    public ArrayList<Bullet> bullets;
    public ArrayList<Shape> bulletsShapes;

    public Avatar(Canvas avatarCanvas) {
        bullets = new ArrayList<>();
        bulletsShapes = new ArrayList<>();
        this.canvas = avatarCanvas;
        gc = canvas.getGraphicsContext2D();
        String uri = "file:" + TankBattleApplication.class.getResource("redTank.png").getPath();
        tank = new Image(uri);
        pos = new Vector(50, 50);
        // la pos del canvas debe ser pequeña para que no gire rápido
        direction = new Vector(2, 2);
        rectangle = new Rectangle(pos.x, pos.y, 50, 50);
    }

    public void draw() {
        gc.save();
        // Es el canvas quien se gira y se mueve, no el tanque.
        // Lo trasladamos para que el tanque se traslade de posición
        gc.translate(pos.x, pos.y);
        // 90 para que el canvas gire bien
        gc.rotate(90 + direction.getAngle());
        gc.drawImage(tank, -25, -25, 50, 50);
        // restore para que el canvas vuelva a su estado original
        gc.restore();
    }

    public void setPosition(double x, double y) {
        pos.x = (int) x - 25;
        pos.y = (int) y - 25;
    }

    public void changeAngle(double a) {
        double amp = direction.getAmplitude();
        double angle = direction.getAngle();
        angle += a;
        direction.x = amp * Math.cos(Math.toRadians(angle));
        direction.y = amp * Math.sin(Math.toRadians(angle));
    }

    public void moveForward() {
        pos.x += direction.x;
        pos.y += direction.y;
    }

    public void moveBackward() {
        pos.x -= direction.x;
        pos.y -= direction.y;
    }

    public void addBullet() {
        Bullet bullet = new Bullet(canvas, new Vector(pos.x, pos.y), new Vector(direction.x, direction.y));
        Shape circle = new Circle(bullet.x, bullet.y, 5);
        bulletsShapes.add(circle);
        bullets.add(bullet);
    }

    public void bulletThread() {
        new Thread(() -> {
            for (int i = 0; i < bullets.size(); i++) {
                // dibujo las balas con el .draw y verifico que no se salió de la pantalla
                bulletsShapes.set(i, bullets.get(i).draw());

                if (bullets.get(i).pos.x > canvas.getWidth() + 20 || bullets.get(i).pos.y > canvas.getHeight() + 20 ||
                        bullets.get(i).pos.y < -20 ||
                        bullets.get(i).pos.x < -20
                ) {
                    bullets.remove(i);
                }
            }
        }).start();
    }

    // Recibo las shapes y enemies del controller, para luego devolverlas en un pair para que sean seteadas
    // cada vez qe en el hilo se ejecute el detect collision y, entonces, si elimino algun sahpe porque ya chocó
    // entonces tambien se elimina de los enemies y se actualiza en el controller. Las balas están acá, asi que no
    // hay problema
    public Pair<ArrayList<Shape>, ArrayList<Obstacle>> detectCollision(ArrayList<Shape> shapes, ArrayList<Obstacle> enemies){
        try{
            for (int i = 0; i < shapes.size(); i++) {
                for (int j = 0; j < bulletsShapes.size(); j++) {
                    if(bulletsShapes.get(j).intersects(shapes.get(i).getBoundsInParent())){
                        bullets.remove(j);
                        bulletsShapes.remove(j);
                        shapes.remove(i);
                        enemies.remove(i);
                    } // if
                } // for
            } // for

            return new Pair<>(shapes, enemies);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException){
            throw new RuntimeException();
        }
    } // detect collision
}

