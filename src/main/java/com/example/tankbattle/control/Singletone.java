package com.example.tankbattle.control;

import com.example.tankbattle.model.Player;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Singletone {
    public static Singletone singletone;

    public String playerOne;
    public String playerTwo;
    public String winner;
    public static ArrayList<Player> scoreboard;

    public static Singletone getInstance(){
        if(singletone==null){
            scoreboard = new ArrayList<>();
            singletone = new Singletone();

            File file = new File("scoreboard.temp");
            file.exists();

            try{
                FileInputStream fis = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
                String line;

                while( (line = bufferedReader.readLine()) != null){
                    String[] parts = line.split("#");
                    String name = parts[0];
                    int victories = Integer.parseInt(parts[1]);
                    Player player = new Player(name, victories);
                    scoreboard.add(player);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return singletone;
        } else {
            return singletone;
        }
    }

    public void save(){
        File file = new File("scoreboard.temp");
        String text = "";

        for (Player player:
             scoreboard) {
            text += player.name + "#" + player.victories+"\n";
        }
        
        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException ioException) {
            throw new RuntimeException();
        }
    }
}
