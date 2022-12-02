package com.example.tankbattle.control;

public class Singletone {
    public static Singletone singletone;

    public String playerOne;
    public String playerTwo;
    public String winner;

    public static Singletone getInstance(){
        if(singletone==null){
            singletone = new Singletone();
            return singletone;
        } else {
            return singletone;
        }
    }
}
