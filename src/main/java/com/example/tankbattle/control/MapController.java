package com.example.tankbattle.control;

import com.example.tankbattle.model.Avatar;
import com.example.tankbattle.model.Bullet;
import com.example.tankbattle.model.Enemy;
import com.example.tankbattle.model.Vector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class MapController implements Initializable {

    //Variables globales de la ventana
    @FXML
    private Canvas mapCanvas;
    private GraphicsContext gc;
    private boolean isRunning = true;


    //Elementos gráficos
    private Avatar avatar;

    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;


    //Estados de las teclas
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;

    boolean spacePressed = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = mapCanvas.getGraphicsContext2D();
        mapCanvas.setFocusTraversable(true);

        mapCanvas.setOnKeyPressed(this::onKeyPressed);
        mapCanvas.setOnKeyReleased(this::onKeyReleased);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies.add(new Enemy(mapCanvas,300,100));
        enemies.add(new Enemy(mapCanvas,300,300));
        avatar = new Avatar(mapCanvas);

        draw();
    }

    public void draw() {
        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {
                            gc.setFill(Color.BLACK);
                            gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
                            avatar.draw();
                            //Pintar enemigos
                            for (int i = 0; i < enemies.size(); i++) {
                                enemies.get(i).draw();
                            }
                            for (int i = 0; i < bullets.size(); i++) {
                                // dibujo las balas con el .draw y verifico que no se salió de la pantalla
                                bullets.get(i).draw();
                                if(bullets.get(i).pos.x > mapCanvas.getWidth()+20 ||
                                        bullets.get(i).pos.y > mapCanvas.getHeight()+20 ||
                                        bullets.get(i).pos.y < -20 ||
                                        bullets.get(i).pos.x < -20
                                ){
                                    bullets.remove(i);
                                }

                            }

                            //Colisiones
                            detectCollisions();
                            doKeyboardActions();

                        });
                        //Sleep
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
    }

    private void detectCollisions() {
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                Enemy e = enemies.get(i);
                double cateto1 = b.pos.x - e.x;
                double cateto2 = b.pos.y - e.y;
                double distance = Math.sqrt(Math.pow(cateto1,2)+Math.pow(cateto2,2));
                // menor a 20 porque esta distancia depende del size de los tanques, ya está calculada
                // si es menor a 20 es porque choca y debo remover al enemigo y a la bala
                if (distance < 20){
                    bullets.remove(j);
                    enemies.remove(i);
                    return;
                }
            }
        }
    }

    private void doKeyboardActions() {
        // cambio el ángulo cuando muevo al tanque a derecha o izquierda
        // usando el método changeAngle del avatar o del enemigo, le sumo o le resto
        // el ángulo que yo desee, en este caso 6, para que no gire tanto
        if (Wpressed) {
            avatar.moveForward();
        }
        if (Apressed) {
            avatar.changeAngle(-6);
        }
        if (Spressed) {
            avatar.moveBackward();
        }
        if (Dpressed) {
            avatar.changeAngle(6);
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = false;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = false;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = false;
        }

        if (keyEvent.getCode() == KeyCode.SPACE){
            spacePressed = false;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = true;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = true;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = true;
        }

        if(keyEvent.getCode() == KeyCode.SPACE){
            Bullet bullet = new Bullet(mapCanvas,new Vector(avatar.pos.x,avatar.pos.y) ,new Vector(avatar.direction.x,avatar.direction.y));
            bullets.add(bullet);
        }
    }
}