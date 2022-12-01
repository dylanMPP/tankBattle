package com.example.tankbattle.model;

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
    public int lives;
    public int ammo;
    public String pathImage;

    public Avatar(Canvas avatarCanvas, String path) {
        pathImage = path;
        bullets = new ArrayList<>();
        bulletsShapes = new ArrayList<>();
        lives = 5;
        ammo = 5;
        this.canvas = avatarCanvas;
        gc = canvas.getGraphicsContext2D();
        tank = new Image(path);

        if(pathImage.contains("red")) {
            pos = new Vector(50, 50);
        } else if(pathImage.contains("blue")){
            pos = new Vector(600, ((int) canvas.getHeight() - 100));
        } else if(pathImage.contains("yellow")){
            pos = new Vector(60, 100);
        }

        // la pos del canvas debe ser pequeña para que no gire rápido
        direction = new Vector(2, 2);
        rectangle = new Rectangle(pos.x, pos.y, 50, 50);
    }

    public void draw() {
        gc.save();
        // Es el canvas quien se gira y se mueve, no el tanque.
        // Lo trasladamos para que el tanque se traslade de posición
        gc.translate(pos.x, pos.y);
        rectangle = new Rectangle(pos.x-25, pos.y-25, 50,50);
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

    public void addBullet(Image bulletImage) {
        if(!(ammo-1 < 0)){
            ammo -= 1;
            Bullet bullet = new Bullet(canvas, new Vector(pos.x, pos.y), new Vector(direction.x, direction.y), bulletImage);
            Shape circle = new Circle(bullet.x, bullet.y, 5);
            bulletsShapes.add(circle);
            bullets.add(bullet);
        }
    }

    public void reload(){
        ammo = 5;
    }

    public void bulletThread() {
        new Thread(() -> {
            for (int i = 0; i < bullets.size(); i++) {
                System.out.println("bullets size:"+bullets.size());
                // dibujo las balas con el .draw y verifico que no se salió de la pantalla

                // Seteo las shapes de las bullets en su posición usando el .draw, ya que me devuelve
                // la shape en la posición hacia donde se movió
                try{
                    bulletsShapes.set(i, bullets.get(i).draw(pathImage));
                } catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    System.out.println("No bullets");
                }


                if (bullets.get(i).pos.x > canvas.getWidth() + 20 || bullets.get(i).pos.y > canvas.getHeight() + 20 ||
                        bullets.get(i).pos.y < -20 ||
                        bullets.get(i).pos.x < -20
                ) {
                    System.out.println("removed");
                    bullets.remove(i);
                }

                try{
                    Thread.sleep(10);
                }catch(InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    // Recibo las shapes y enemies del controller, para luego devolverlas en un pair para que sean seteadas
    // cada vez qe en el hilo se ejecute el detect collision y, entonces, si elimino algun sahpe porque ya chocó
    // entonces tambien se elimina de los enemies y se actualiza en el controller. Las balas están acá, asi que no
    // hay problema
    public Pair<ArrayList<Shape>, ArrayList<Obstacle>> detectCollision(ArrayList<Shape> obstaclesShapes,
                                                                       ArrayList<Obstacle> obstacles){
        try{
            for (int i = 0; i < obstaclesShapes.size(); i++) {
                for (int j = 0; j < bulletsShapes.size(); j++) {
                    if(bulletsShapes.get(j).intersects(obstaclesShapes.get(i).getBoundsInParent())){
                        bullets.remove(j);
                        bulletsShapes.remove(j);
                        obstaclesShapes.remove(i);
                        obstacles.remove(i);
                    } // if
                } // for
            } // for

            return new Pair<>(obstaclesShapes, obstacles);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException){
            throw new RuntimeException();
        } catch (RuntimeException runtimeException){
            throw new RuntimeException();
        }
    } // detect collision

    public Pair<ArrayList<Shape>, ArrayList<Avatar>> detectAvatarCollisions(ArrayList<Shape> avatarShapes,
                                                                            ArrayList<Avatar> avatars, int avatarPos){
        try{
            // Lo mismo de arriba pero para avatars
            for (int i = 0; i < avatarShapes.size(); i++) {
                for (int j = 0; j < bulletsShapes.size(); j++) {
                    // verifico que no sea él mismo
                    if(i!=avatarPos){
                        if(bulletsShapes.get(j).intersects(avatarShapes.get(i).getBoundsInParent())){
                            bullets.remove(j);
                            bulletsShapes.remove(j);

                            avatars.get(i).lives -= 1;

                            if(avatars.get(i).lives ==0){
                                avatarShapes.remove(i);
                                avatars.remove(i);
                            }
                        } // if
                    }
                } // for
            } // for

            return new Pair<>(avatarShapes, avatars);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException){
            throw new RuntimeException();
        } catch (RuntimeException runtimeException){
            throw new RuntimeException();
        }
    }
}

