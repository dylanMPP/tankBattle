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
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

public class MapController implements Initializable {
    //Variables globales de la ventana
    @FXML
    public Label playerTwoLbl;

    @FXML
    public Label playerOneLbl;

    @FXML
    public Canvas mapCanvas;
    @FXML
    public ImageView background;

    @FXML
    public ImageView playerOneLivesImageView;

    @FXML
    public ImageView playerTwoLivesImageView;

    @FXML
    public ImageView playerThreeLivesImageView;

    @FXML
    public ImageView playerOneBulletsImageView;

    @FXML
    public ImageView playerTwoBulletsImageView;

    @FXML
    public ImageView playerThreeBulletsImageView;

    private GraphicsContext gc;
    private boolean isRunning = true;
    //Elementos gráficos
    private Avatar avatar;
    private Avatar avatar2;
    private Avatar avatar3;
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
    boolean canMoveAvatar3 = true;
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
    Image zeroBulletImage;

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
        uri = "file:" + TankBattleApplication.class.getResource("0bullet.png").getPath();
        zeroBulletImage = new Image(uri);

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
        avatar3 = new Avatar(mapCanvas, "file:" + TankBattleApplication.class.getResource("yellowTank.png").getPath());

        avatars.add(avatar);
        avatars.add(avatar2);
        avatars.add(avatar3);

        playerOneLbl.setText(Singletone.getInstance().playerOne);
        playerTwoLbl.setText(Singletone.getInstance().playerTwo);

        draw();
    }

    //public void assignPlayersNames(String player1, String player2){
    //    playerOneLbl = new Label(player1);
    //    playerTwoLbl = new Label(player2);
    //}

    public void draw() {
        // Añado los enemies u obstáculos al arreglo de shapes del mapa
        // Este arreglo de shapes del mapa me sirve para enviarlo como parámetro al verificar
        // colisiones para cada obstáculo con el fin de que desaparezca
        for (Obstacle obstacle : obstacles) {
            Shape rectangle1 = obstacle.rectangle;
            obstaclesShapes.add(rectangle1);
        }

        System.out.println(playerOneLbl.getText());

        drawLives();
        drawBullets();
        cpuMove();

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
                            gc.setFill(Color.BLACK);
                            gc.fillRect(mapCanvas.getWidth(),0,10,10);

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
                            avatar3.bulletThread();
                            // (int i = 0; i < avatars.size(); i++) {
                             //   avatars.get(i).bulletThread();
                            //}

                            //Colisiones

                            // Colision de avatar con obstaculos
                            detectObstacleCollisionsAvatar();
                            detectObstacleCollisionsAvatar2();
                            detectObstacleCollisionsAvatar3();
                            // Colision de balas con obstáculos
                            detectCollisions();
                            // Colisión de avatares con balas
                            detectAvatarCollisions();
                            doKeyboardActions();

                            if(avatar.lives < 0 && avatar2.lives < 0 && avatar3.lives > 0){
                                Singletone.getInstance().winner = "CPU";
                                isRunning = false;
                            } else if(avatar.lives < 0 && avatar2.lives > 0 && avatar3.lives < 0){
                                Singletone.getInstance().winner = "player2";
                                isRunning = false;
                            } else if(avatar.lives > 0 && avatar2.lives < 0 && avatar3.lives < 0){
                                Singletone.getInstance().winner = "player1";
                                isRunning = false;
                            }

                            if(!isRunning){
                                TankBattleApplication.showWindow("");
                            }
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
            Shape leftShape = new Rectangle(-1,0,1,mapCanvas.getHeight());
            Shape topShape = new Rectangle(0, 1, mapCanvas.getWidth(), 1);
            Shape rightShape = new Rectangle(mapCanvas.getWidth(), 0, 1, mapCanvas.getHeight());
            Shape downShape = new Rectangle(0, mapCanvas.getHeight(), mapCanvas.getWidth(), 1);

            for(int j=0;j<obstaclesShapes.size();j++){
                Shape shape = null;

                if (DownPressed) {
                    shape=new Rectangle(avatar2.pos.x-avatar.direction.x-25,avatar2.pos.y-avatar2.direction.y-25,50,50);
                } else if (UpPressed){
                    shape=new Rectangle(avatar2.pos.x+avatar.direction.x-25,avatar2.pos.y+avatar2.direction.y-25,50,50);
                }

                if(shape!=null){
                    if(shape.intersects(leftShape.getBoundsInParent()) || shape.intersects(topShape.getBoundsInParent()) ||
                    shape.intersects(rightShape.getBoundsInParent()) || shape.intersects(downShape.getBoundsInParent())){
                        canMoveAvatar2 = false;
                        break;
                    }

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
            Shape leftShape = new Rectangle(-1,0,1,mapCanvas.getHeight());
            Shape topShape = new Rectangle(0, 1, mapCanvas.getWidth(), 1);
            Shape rightShape = new Rectangle(mapCanvas.getWidth(), 0, 1, mapCanvas.getHeight());
            Shape downShape = new Rectangle(0, mapCanvas.getHeight(), mapCanvas.getWidth(), 1);

            for(int j=0;j<obstaclesShapes.size();j++){
                Shape shape = null;

                if (Spressed) {
                    shape = new Rectangle(avatar.pos.x-avatar.direction.x-25,avatar.pos.y-avatar.direction.y-25,50,50);

                } else if (Wpressed){
                    shape=new Rectangle(avatar.pos.x+avatar.direction.x-25,avatar.pos.y+avatar.direction.y-25,50,50);
                }

                if(shape!=null){
                    if(shape.intersects(leftShape.getBoundsInParent()) || shape.intersects(topShape.getBoundsInParent()) ||
                            shape.intersects(rightShape.getBoundsInParent()) || shape.intersects(downShape.getBoundsInParent())){
                        canMoveAvatar = false;
                        break;
                    }

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

    public void detectObstacleCollisionsAvatar3() {
        new Thread(()->{
            Shape leftShape = new Rectangle(-1,0,1,mapCanvas.getHeight());
            Shape topShape = new Rectangle(0, 1, mapCanvas.getWidth(), 1);
            Shape rightShape = new Rectangle(mapCanvas.getWidth(), 0, 1, mapCanvas.getHeight());
            Shape downShape = new Rectangle(0, mapCanvas.getHeight(), mapCanvas.getWidth(), 1);

            for(int j=0;j<obstaclesShapes.size();j++){
                Shape shape = new Rectangle(avatar3.pos.x+avatar.direction.x-25,avatar3.pos.y+avatar.direction.y-25,50,50);

                if(shape.intersects(leftShape.getBoundsInParent()) || shape.intersects(topShape.getBoundsInParent()) ||
                        shape.intersects(rightShape.getBoundsInParent()) || shape.intersects(downShape.getBoundsInParent())){
                    canMoveAvatar3 = false;
                    break;
                }

                if(obstaclesShapes.get(j).intersects(shape.getBoundsInParent())) {
                    canMoveAvatar3 = false;
                    break;
                } else {
                    canMoveAvatar3 = true;
                }
            }
            //Sleep
            try{
                Thread.sleep(40);
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }).start();
    }//detect avatar 3

    private void detectCollisions() {
        Pair<ArrayList<Shape>, ArrayList<Obstacle>> pair = avatar.detectCollision(obstaclesShapes, obstacles);
        this.obstaclesShapes = pair.getKey();
        this.obstacles = pair.getValue();

        Pair<ArrayList<Shape>, ArrayList<Obstacle>> pair2 = avatar2.detectCollision(obstaclesShapes, obstacles);
        this.obstaclesShapes = pair2.getKey();
        this.obstacles = pair2.getValue();

        Pair<ArrayList<Shape>, ArrayList<Obstacle>> pair3 = avatar3.detectCollision(obstaclesShapes, obstacles);
        this.obstaclesShapes = pair3.getKey();
        this.obstacles = pair3.getValue();
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

        // avatar 2
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

        // avatar 3
        for (int i = 0; i < avatars.size(); i++) {
            if (avatars.get(i).equals(avatar3)) {
                avatarPos = i;
                break;
            }
        }

        Pair<ArrayList<Shape>, ArrayList<Avatar>> pair3 = avatar3.
                detectAvatarCollisions(avatarShapes, avatars, avatarPos);
        this.avatarShapes = pair3.getKey();
        this.avatars = pair3.getValue();
    }

    public void cpuMove(){
        avatar3AddBullet();

        new Thread(() -> {
            while (isRunning && avatar3.lives > 0){
                double distanceAvatar1 = distanceBetweenAvatar(avatar.pos.x, avatar.pos.y);
                double distanceAvatar2 = distanceBetweenAvatar(avatar2.pos.x, avatar2.pos.y);

                boolean toAvatar = false;
                boolean toAvatar2 = false;

                if( (distanceAvatar1 < distanceAvatar2) && avatar.lives > 0){
                    toAvatar = true;
                } else if( (distanceAvatar1 > distanceAvatar2) && avatar2.lives > 0 ){
                    toAvatar2 = true;
                } else {
                    toAvatar = true;
                }

                if(toAvatar && avatar.lives > 0){
                    // Si la distancia es muy corta, no hago que se mueva, sino que simplemente setee su dirección
                    // mirando al avatar a seguir y me dispare cada cierto tiempo.
                    if(distanceAvatar1 <= 120){
                        avatar3.direction.x = (avatar.pos.x - avatar3.pos.x) / distanceAvatar1;
                        avatar3.direction.y = (avatar.pos.y - avatar3.pos.y) / distanceAvatar1;
                    } else {
                        // Lo importante es la dirección del avatar 3, para que siempre su dirección esté dirigida
                        // hacia donde se encuentra el avatar a seguir (el más corto). Realizamos la operación necesaria
                        // entre vectores y lo dividimos entre la distancia total (para que se mueva entre intervalos
                        // y no se teletransporte)
                        avatar3.direction.x = (avatar.pos.x - avatar3.pos.x) / distanceAvatar1;
                        avatar3.direction.y = (avatar.pos.y - avatar3.pos.y) / distanceAvatar1;
                        // Siempre lo muevo hacia adelante, pues siempre me está mirando

                        if(canMoveAvatar3){
                            avatar3.moveForward();
                        }
                    }

                } else if(toAvatar2 && avatar2.lives > 0){
                    //avatar3.direction.x = avatar2.direction.x - avatar3.direction.x;
                    //avatar3.direction.y = avatar2.direction.y - avatar3.direction.y;
                    if(distanceAvatar2 <= 120){
                        avatar3.direction.x = (avatar2.pos.x - avatar3.pos.x) / distanceAvatar2;
                        avatar3.direction.y = (avatar2.pos.y - avatar3.pos.y) / distanceAvatar2;
                    } else {
                        avatar3.direction.x = (avatar2.pos.x - avatar3.pos.x) / distanceAvatar2;
                        avatar3.direction.y = (avatar2.pos.y - avatar3.pos.y) / distanceAvatar2;

                        if(canMoveAvatar3){
                            avatar3.moveForward();
                        }
                    }
                }

                try{
                    Thread.sleep(20);
                }catch(InterruptedException e){
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public double distanceBetweenAvatar(double x, double y){
        double distanceX = avatar3.pos.x - x;
        double distanceY = avatar3.pos.y - y;
        return Math.sqrt( (distanceX*distanceX) + (distanceY*distanceY) );
    }

    public void avatar3AddBullet(){
        new Thread(() -> {
            while (isRunning){
                if((avatar3.lives > 0)){
                    try{
                        Thread.sleep(3000);
                    }catch(InterruptedException e){
                        throw new RuntimeException(e);
                    }

                    Platform.runLater(() -> {
                        avatar3.addBullet(bulletImage);
                    });

                    if(avatar3.ammo<=0){
                        avatar3.reload();
                    }
                }
            }
        }).start();
    }

    private void doKeyboardActions() {
        // cambio el ángulo cuando muevo al tanque a derecha o izquierda
        // usando el método changeAngle del avatar o del enemigo, le sumo o le resto
        // el ángulo que yo desee, en este caso 6, para que no gire tanto

        if (canMoveAvatar && avatar.lives > 0) {
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

        if (canMoveAvatar2 && avatar2.lives > 0) {
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

        obstacles.add(new Obstacle(mapCanvas, path, 345, 20));
        obstacles.add(new Obstacle(mapCanvas, path, 345, (20 + height)));
        obstacles.add(new Obstacle(mapCanvas, path, 345, (20 + height * 2)));
        obstacles.add(new Obstacle(mapCanvas, path, 345, (20 + height * 3)));


        obstacles.add(new Obstacle(mapCanvas, path, 500, (200 + height)));

        obstacles.add(new Obstacle(mapCanvas, path, 500, (280 + height)));

        obstacles.add(new Obstacle(mapCanvas, path, 400, ((int) mapCanvas.getHeight() - 20)));
        obstacles.add(new Obstacle(mapCanvas, path, 400, ((int) mapCanvas.getHeight() - 60)));
        obstacles.add(new Obstacle(mapCanvas, path, 400, ((int) mapCanvas.getHeight() - 100)));

        obstacles.add(new Obstacle(mapCanvas, path, 100, ((int) mapCanvas.getHeight() - 210)));
        obstacles.add(new Obstacle(mapCanvas, path, 100, ((int) mapCanvas.getHeight() - 250)));
        obstacles.add(new Obstacle(mapCanvas, path, 100, ((int) mapCanvas.getHeight() - 290)));
    }

    public void drawLives(){
        new Thread(() -> {
            while(isRunning){
                    if(avatar.lives == 5){
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

                    if(avatar3.lives ==5){
                        playerThreeLivesImageView.setImage(fiveHeartsImage);
                    } else if(avatar3.lives ==4){
                        playerThreeLivesImageView.setImage(fourHeartsImage);
                    } else if(avatar3.lives ==3){
                        playerThreeLivesImageView.setImage(threeHeartsImage);
                    } else if(avatar3.lives ==2){
                        playerThreeLivesImageView.setImage(twoHeartsImage);
                    } else if(avatar3.lives ==1){
                        playerThreeLivesImageView.setImage(oneHeartImage);
                    } else {
                        playerThreeLivesImageView.setImage(zeroHeartImage);
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
                        playerOneBulletsImageView.setImage(zeroBulletImage);
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
                        playerTwoBulletsImageView.setImage(zeroBulletImage);
                    }

                    if(avatar3.ammo ==5){
                        playerThreeBulletsImageView.setImage(fiveBulletsImage);
                    } else if(avatar3.ammo ==4){
                        playerThreeBulletsImageView.setImage(fourBulletsImage);
                    } else if(avatar3.ammo ==3){
                        playerThreeBulletsImageView.setImage(threeBulletsImage);
                    } else if(avatar3.ammo ==2){
                        playerThreeBulletsImageView.setImage(twoBulletsImage);
                    } else if(avatar3.ammo ==1){
                        playerThreeBulletsImageView.setImage(oneBulletImage);
                    } else {
                        playerThreeBulletsImageView.setImage(zeroBulletImage);
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