package edu.neumont.pro200.vpet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JWunz on 5/13/17.
 */

public abstract class Monster{
    private int sprite;
    private int power;
    private int speed;
    private int agility;
    private String[] evolutions;

    public int getSprite(){
        return sprite;
    }

    public boolean setSprite(int sprite) {
        this.sprite = sprite;
        return true;
    }

    public int getPower() {
        return power;
    }

    public boolean setPower(int power) {
        return true;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean setSpeed(int speed) {
        return true;
    }

    public int getAgility() {
        return agility;
    }

    public boolean setAgility(int agility) {
        return true;
    }

    public String[] getEvolutions() {
        return null;
    }

    public boolean setEvolutions(String[] evos) {
        for (String evo :
                evos) {
            addEvolution(evo);
        }
        return true;
    }

    private boolean addEvolution(String evo) {
        if (evolutions.length < 4) {
            evolutions[evolutions.length] = evo;
            return true;
        }
        return false;
    }

    public Monster(int sprite, int power, int speed, int agility) {
        this.sprite = sprite;
        this.power = power;
        this.speed = speed;
        this.agility = agility;
    }

    public Monster(int sprite, int power, int speed, int agility, String[] evolutions) {
        this.sprite = sprite;
        this.power = power;
        this.speed = speed;
        this.agility = agility;
        this.evolutions = evolutions;
    }
}
