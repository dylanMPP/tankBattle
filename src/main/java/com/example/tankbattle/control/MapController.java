package com.example.tankbattle.control;

import com.example.tankbattle.TankBattleApplication;
import com.example.tankbattle.model.Avatar;
import com.example.tankbattle.model.Obstacle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class MapController implements Initializable {

    //Variables globales de la ventana
    @FXML
    public Canvas mapCanvas;

    @FXML
    public ImageView background;
    private GraphicsContext gc;
    private boolean isRunning = true;


    //Elementos gráficos
    private Avatar avatar;
    private Avatar avatar2;

    private ArrayList<Obstacle> enemies;
    private ArrayList<Shape> shapes;


    //Estados de las teclas
    // Avatar 1
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;

    boolean spacePressed = false;

    // Avatar 2
    boolean UpPressed = false;
    boolean DownPressed = false;
    boolean LefPressed = false;
    boolean RightPressed = false;

    boolean PPressed = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = mapCanvas.getGraphicsContext2D();
        String uri = "file:" + TankBattleApplication.class.getResource("redTank.png").getPath();
        background = new ImageView(uri);

        mapCanvas.setFocusTraversable(true);
        mapCanvas.setOnKeyPressed(this::onKeyPressed);
        mapCanvas.setOnKeyReleased(this::onKeyReleased);

        enemies = new ArrayList<>();
        shapes = new ArrayList<>();

        enemies.add(new Obstacle(mapCanvas,300,100));
        enemies.add(new Obstacle(mapCanvas,300,300));

        avatar = new Avatar(mapCanvas);
        avatar2 = new Avatar(mapCanvas);

        draw();
    }

    public void draw() {
        // Añado los enemies u obstáculos al arreglo de shapes del mapa
        // Este arreglo de shapes del mapa me sirve para enviarlo como parámetro al verificar
        // colisiones para cada obstáculo con el fin de que desaparezca
        for (int i = 0; i < enemies.size(); i++) {
            Shape rectangle1 = enemies.get(i).rectangle;
            shapes.add(rectangle1);
        }

        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {
                            gc.setFill(Color.BLACK);
                            gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
                            avatar.draw();
                            avatar2.draw();
                            //Pintar enemigos
                            for (int i = 0; i < enemies.size(); i++) {
                                enemies.get(i).draw();
                            }
                            avatar.bulletThread();
                            avatar2.bulletThread();
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
        this.shapes = avatar.detectCollision(shapes, enemies).getKey();
        this.enemies = avatar2.detectCollision(shapes, enemies).getValue();
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

        // Avatar 2
        if (UpPressed) {
            avatar2.moveForward();
        }
        if (LefPressed) {
            avatar2.changeAngle(-6);
        }
        if (DownPressed) {
            avatar2.moveBackward();
        }
        if (RightPressed) {
            avatar2.changeAngle(6);
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

        // Avatar 2
        if (keyEvent.getCode() == KeyCode.UP) {
            UpPressed = false;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LefPressed = false;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DownPressed = false;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RightPressed = false;
        }

        if (keyEvent.getCode() == KeyCode.P){
            PPressed = false;
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
            avatar.addBullet();
        }

        // Avatar 2
        if (keyEvent.getCode() == KeyCode.UP) {
            UpPressed = true;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LefPressed = true;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DownPressed = true;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RightPressed = true;
        }

        if(keyEvent.getCode() == KeyCode.P){
            avatar2.addBullet();
        }
    }
}