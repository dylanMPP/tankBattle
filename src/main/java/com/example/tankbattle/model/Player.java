package com.example.tankbattle.model;

public class Player {

    public String name;
    public int victories;

    public Player(String name, int victories){
        this.name = name;
        this.victories = victories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }
}
