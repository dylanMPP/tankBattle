package com.example.tankbattle.control;

import com.example.tankbattle.TankBattleApplication;
import com.example.tankbattle.model.Avatar;
import com.example.tankbattle.model.Obstacle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

public class MapController implements Initializable {
    //Variables globales de la ventana
    @FXML
    public Canvas mapCanvas;
    @FXML
    public ImageView background;

    @FXML
    public ImageView playerOneLivesImageView;

    @FXML
    public ImageView playerTwoLivesImageView;

    @FXML
    public ImageView playerOneBulletsImageView;

    @FXML
    public ImageView playerTwoBulletsImageView;

    private GraphicsContext gc;
    private boolean isRunning = true;
    //Elementos gráficos
    private Avatar avatar;
    private Avatar avatar2;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Avatar> avatars;
    private ArrayList<Shape> obstaclesShapes;
    private ArrayList<Shape> avatarShapes;
    //Estados de las teclas
    // Avatar 1
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;
    boolean spacePressed = false;
    boolean RPressed = false;
    // Avatar 2
    boolean UpPressed = false;
    boolean DownPressed = false;
    boolean LefPressed = false;
    boolean RightPressed = false;
    boolean PPressed = false;
    boolean EnterPressed = false;
    boolean canMoveAvatar = true;
    boolean canMoveAvatar2 = true;
    Image backgroundImage;
    Image bulletImage;
    Image fiveHeartsImage;
    Image fourHeartsImage;
    Image threeHeartsImage;
    Image twoHeartsImage;
    Image oneHeartImage;
    Image zeroHeartImage;
    Image fiveBulletsImage;
    Image fourBulletsImage;
    Image threeBulletsImage;
    Image twoBulletsImage;
    Image oneBulletImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = mapCanvas.getGraphicsContext2D();
        // cargue de imágenes
        String uri = "file:" + TankBattleApplication.class.getResource("map-snow.png").getPath();
        backgroundImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("circleBullet.png").getPath();
        bulletImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("5hearts.png").getPath();
        fiveHeartsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("4hearts.png").getPath();
        fourHeartsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("3hearts.png").getPath();
        threeHeartsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("2hearts.png").getPath();
        twoHeartsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("1heart.png").getPath();
        oneHeartImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("skull-0 lifes.png").getPath();
        zeroHeartImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("5bullet.png").getPath();
        fiveBulletsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("4bullet.png").getPath();
        fourBulletsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("3bullet.png").getPath();
        threeBulletsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("2bullet.png").getPath();
        twoBulletsImage = new Image(uri);
        uri = "file:" + TankBattleApplication.class.getResource("1bullet.png").getPath();
        oneBulletImage = new Image(uri);

        mapCanvas.setFocusTraversable(true);
        mapCanvas.setOnKeyPressed(this::onKeyPressed);
        mapCanvas.setOnKeyReleased(this::onKeyReleased);

        obstacles = new ArrayList<>();
        avatars = new ArrayList<>();
        obstaclesShapes = new ArrayList<>();
        avatarShapes = new ArrayList<>();

        createMap();

        avatar = new Avatar(mapCanvas, "file:" + TankBattleApplication.class.getResource("redTank.png").getPath());
        avatar2 = new Avatar(mapCanvas, "file:" + TankBattleApplication.class.getResource("blueTank.png").getPath());
        avatars.add(avatar);
        avatars.add(avatar2);

        draw();
    }

    public void draw() {
        // Añado los enemies u obstáculos al arreglo de shapes del mapa
        // Este arreglo de shapes del mapa me sirve para enviarlo como parámetro al verificar
        // colisiones para cada obstáculo con el fin de que desaparezca
        for (Obstacle obstacle : obstacles) {
            Shape rectangle1 = obstacle.rectangle;
            obstaclesShapes.add(rectangle1);
        }

        drawLives();
        drawBullets();

        // Añado los rectangulos (hitbox) de cada avatar al array list de shapes
        // de la controladora, para más adelante verificar si estos son tocados por alguna bala

        new Thread(
                () -> {
                    while (isRunning) {
                        // dentro del for constantemente voy a estar actualizando los shapes del avatar
                        // pues las coordenadas del shape (rectángulo) constantemente también se está
                        // actualizando porque el tanque se mueve
                        avatarShapes.clear();
                        for (int i = 0; i < avatars.size(); i++) {
                            avatarShapes.add(avatars.get(i).rectangle);
                        }

                        Platform.runLater(() -> {
                            gc.drawImage(backgroundImage, 0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

                            // dependiendo de los avatars que hayan los dibujo
                            // estos se actualizan en la collision de avatars, van disminuyendo
                            // si ya fueron intersectados por una bullet
                            for (Avatar avatar : avatars) {
                                avatar.draw();
                            }

                            //Pintar enemigos
                            for (int i = 0; i < obstacles.size(); i++) {
                                obstacles.get(i).draw();
                            }

                            avatar.bulletThread();
                            avatar2.bulletThread();
                            // (int i = 0; i < avatars.size(); i++) {
                             //   avatars.get(i).bulletThread();
                            //}

                            //Colisiones

                            // Colision de avatar con obstaculos
                            detectObstacleCollisionsAvatar();
                            detectObstacleCollisionsAvatar2();
                            // Colision de balas con obstáculos
                            detectCollisions();
                            // Colisión de avatares con balas
                            detectAvatarCollisions();

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

    public void detectObstacleCollisionsAvatar2() {
        // Hago un hilo que me verifique si el avatar (1 o 2) choca con algún obstáculo (sin incluir las balas), estás
        // después son verificadas con otro hilo.

        // Entonces, lo que hago es crear una shape auxiliar, que tenga el movimiento propuesto por el jugador,
        // es decir, yo hago el auxiliar y este tiene como coordenadas las coordenadas que tendría el tanque si se pudiése
        // mover, para verificar si sí puedo moverme o no y para que el tanque no quede dentro del objeto.

        // Asimismo, recorro el arreglo de las shapes de los objetos (aquí están los bloques y paredes), cuando encuentre
        // que choca con alguno, el for hace break (esto con el fin de que no siga y que si encuentra que no choca
        // con un bloque pues la variable de si se puede mover se actualice a true). Entonces, si pasó por todos los objetos
        // y no encontró con ninguno que choca, la variable de canMoveAvatar1 o 2, se queda en true, y más adelante
        // cuando ejecuto el método de doKeyboardActions, las acciones se realicen. En caso de que la variable
        // quede falsa, las acciones no se realizan.
        new Thread(()->{
            for(int j=0;j<obstaclesShapes.size();j++){
                Shape shape = null;

                if (DownPressed) {
                    shape=new Rectangle(avatar2.pos.x-avatar.direction.x-25,avatar2.pos.y-avatar2.direction.y-25,50,50);
                } else if (UpPressed){
                    shape=new Rectangle(avatar2.pos.x+avatar.direction.x-25,avatar2.pos.y+avatar2.direction.y-25,50,50);
                }

                if(shape!=null){
                    if(obstaclesShapes.get(j).intersects(shape.getBoundsInParent())) {
                        canMoveAvatar2 = false;
                        break;
                    } else {
                        canMoveAvatar2 = true;
                    }
                }
            }
            //Sleep
            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void detectObstacleCollisionsAvatar() {
        new Thread(()->{
            for(int j=0;j<obstaclesShapes.size();j++){
                Shape shape = null;

                if (Spressed) {
                    shape=new Rectangle(avatar.pos.x-avatar.direction.x-25,avatar.pos.y-avatar.direction.y-25,50,50);
                } else if (Wpressed){
                    shape=new Rectangle(avatar.pos.x+avatar.direction.x-25,avatar.pos.y+avatar.direction.y-25,50,50);
                }

                if(shape!=null){
                    if(obstaclesShapes.get(j).intersects(shape.getBoundsInParent())) {
                        canMoveAvatar = false;
                        break;
                    } else {
                        canMoveAvatar = true;
                    }
                }
            }
            //Sleep
            try{
                Thread.sleep(40);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }).start();
    }//detect

    private void detectCollisions() {
        Pair<ArrayList<Shape>, ArrayList<Obstacle>> pair = avatar.detectCollision(obstaclesShapes, obstacles);
        this.obstaclesShapes = pair.getKey();
        this.obstacles = pair.getValue();

        Pair<ArrayList<Shape>, ArrayList<Obstacle>> pair2 = avatar2.detectCollision(obstaclesShapes, obstacles);
        this.obstaclesShapes = pair2.getKey();
        this.obstacles = pair2.getValue();
    }

    private void detectAvatarCollisions() {
        // mando la posición (si encuentra) del objeto avatar, para que solo evalue los demás avatares aparte de él
        // pues no quiero que por su propia bala sea eliminado. Si no encuentra, manda -1
        int avatarPos = -1;

        for (int i = 0; i < avatars.size(); i++) {
            if (avatars.get(i).equals(avatar)) {
                avatarPos = i;
                break;
            }
        }

        // Guardo el resultado de detect collisions en una variable pair, donde tengo los shapes y los avatares
        Pair<ArrayList<Shape>, ArrayList<Avatar>> pair = avatar.detectAvatarCollisions(avatarShapes, avatars, avatarPos);
        this.avatarShapes = pair.getKey();
        this.avatars = pair.getValue();

        for (int i = 0; i < avatars.size(); i++) {
            if (avatars.get(i).equals(avatar2)) {
                avatarPos = i;
                break;
            }
        }

        Pair<ArrayList<Shape>, ArrayList<Avatar>> pair2 = avatar2.
                detectAvatarCollisions(avatarShapes, avatars, avatarPos);
        this.avatarShapes = pair2.getKey();
        this.avatars = pair2.getValue();
    }

    private void doKeyboardActions() {
        // cambio el ángulo cuando muevo al tanque a derecha o izquierda
        // usando el método changeAngle del avatar o del enemigo, le sumo o le resto
        // el ángulo que yo desee, en este caso 6, para que no gire tanto

        if (canMoveAvatar) {
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

        if (canMoveAvatar2 && avatar2.lives >0) {
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

        if (keyEvent.getCode() == KeyCode.R) {
            RPressed= false;
        }

        if (keyEvent.getCode() == KeyCode.SPACE) {
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

        if(keyEvent.getCode() == KeyCode.ENTER){
            EnterPressed = false;
        }

        if (keyEvent.getCode() == KeyCode.P) {
            PPressed = false;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {

        if(avatar.lives >0){
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

            if(keyEvent.getCode() == KeyCode.R){
                avatar.reload();
            }

            if (keyEvent.getCode() == KeyCode.SPACE) {
                avatar.addBullet(bulletImage);
            }
        }

        // Avatar 2
        if(avatar2.lives >0){
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

            if(keyEvent.getCode() == KeyCode.ENTER){
                avatar2.reload();
            }

            if (keyEvent.getCode() == KeyCode.P) {
                avatar2.addBullet(bulletImage);
            }
        }
    }

    public void createMap() {
        String path = "file:" + TankBattleApplication.class.getResource("ice-block.png").getPath();
        // Pinto los obstáculos. Y es la altura de cada uno, si pongo 20 (que es la mitad de su altura)
        // entonces se me pinta arriba al borde. Para bajarlo, le a ese 20 el total de la altura
        int height = 40;
        obstacles.add(new Obstacle(mapCanvas, path, 300, 20));
        obstacles.add(new Obstacle(mapCanvas, path, 300, (20 + height)));
        obstacles.add(new Obstacle(mapCanvas, path, 300, (20 + height * 2)));
        obstacles.add(new Obstacle(mapCanvas, path, 300, (20 + height * 3)));


        obstacles.add(new Obstacle(mapCanvas, path, 500, (200 + height)));

        obstacles.add(new Obstacle(mapCanvas, path, 500, (280 + height)));
        obstacles.add(new Obstacle(mapCanvas, path, 500, (320 + height)));

        obstacles.add(new Obstacle(mapCanvas, path, 400, ((int) mapCanvas.getHeight() - 20)));
        obstacles.add(new Obstacle(mapCanvas, path, 400, ((int) mapCanvas.getHeight() - 60)));
        obstacles.add(new Obstacle(mapCanvas, path, 400, ((int) mapCanvas.getHeight() - 100)));
    }

    public void drawLives(){
        new Thread(() -> {
            while(isRunning){
                if(avatar.lives ==5){
                    playerOneLivesImageView.setImage(fiveHeartsImage);
                } else if(avatar.lives ==4){
                    playerOneLivesImageView.setImage(fourHeartsImage);
                } else if(avatar.lives ==3){
                    playerOneLivesImageView.setImage(threeHeartsImage);
                } else if(avatar.lives ==2){
                    playerOneLivesImageView.setImage(twoHeartsImage);
                } else if(avatar.lives ==1){
                    playerOneLivesImageView.setImage(oneHeartImage);
                } else {
                    playerOneLivesImageView.setImage(zeroHeartImage);
                }

                // Avatar 2
                if(avatar2.lives ==5){
                    playerTwoLivesImageView.setImage(fiveHeartsImage);
                } else if(avatar2.lives ==4){
                    playerTwoLivesImageView.setImage(fourHeartsImage);
                } else if(avatar2.lives ==3){
                    playerTwoLivesImageView.setImage(threeHeartsImage);
                } else if(avatar2.lives ==2){
                    playerTwoLivesImageView.setImage(twoHeartsImage);
                } else if(avatar2.lives ==1){
                    playerTwoLivesImageView.setImage(oneHeartImage);
                } else {
                    playerTwoLivesImageView.setImage(zeroHeartImage);
                }
            }

            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void drawBullets(){
        new Thread(() -> {
            while(isRunning){
                if(avatar.ammo ==5){
                    playerOneBulletsImageView.setImage(fiveBulletsImage);
                } else if(avatar.ammo ==4){
                    playerOneBulletsImageView.setImage(fourBulletsImage);
                } else if(avatar.ammo ==3){
                    playerOneBulletsImageView.setImage(threeBulletsImage);
                } else if(avatar.ammo ==2){
                    playerOneBulletsImageView.setImage(twoBulletsImage);
                } else if(avatar.ammo ==1){
                    playerOneBulletsImageView.setImage(oneBulletImage);
                } else{

                }
                // Avatar 2
                if(avatar2.ammo ==5){
                    playerTwoBulletsImageView.setImage(fiveBulletsImage);
                } else if(avatar2.ammo ==4){
                    playerTwoBulletsImageView.setImage(fourBulletsImage);
                } else if(avatar2.ammo ==3){
                    playerTwoBulletsImageView.setImage(threeBulletsImage);
                } else if(avatar2.ammo ==2){
                    playerTwoBulletsImageView.setImage(twoBulletsImage);
                } else if(avatar2.ammo ==1){
                    playerTwoBulletsImageView.setImage(oneBulletImage);
                } else {

                }
            }

            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }).start();
    }
}