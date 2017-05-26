package edu.neumont.pro200.vpet;

import android.content.res.AssetManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by JWunz on 5/13/17.
 */

public class Pet extends Monster {
    private int happiness;
    private int hunger;
    private double weight;
    private float discipline;
    private int careMistakes;
    private int age;
    private List skills;
    private boolean isDirty;
    private boolean isTired;
    private boolean isSick;
    private boolean isInjured;
    private int dirtyTime = 0;
    private int tiredTime = 0;
    private int sickTime = 0;
    private int injuredTime = 0;
    private int lastDirtyTime = 0;
    private int lastTiredTime = 0;
    private int lastSickTime = 0;
    private int lastInjuredTime = 0;

    public int getHappiness() {
        return happiness;
    }

    public int getHunger() {
        return hunger;
    }

    public boolean setHunger(int hunger) {
        this.hunger = hunger;
        return true;
    }

    public double getWeight() {
        return weight;
    }

    public boolean setWeight(double weight) {
        this.weight = weight;
        return true;
    }

    public float getDiscipline() {
        return discipline;
    }

    public boolean setDiscipline(float discipline) {
        this.discipline = discipline;
        return true;
    }

    public int getCareMistakes() {
        return careMistakes;
    }

    public boolean setCareMistakes(int careMistakes) {
        this.careMistakes = careMistakes;
        return true;
    }

    public int getAge() {
        return age;
    }

    public boolean setAge(int age) {
        this.age = age;
        return true;
    }

    public List getSkills() {
        return skills;
    }

    public boolean setSkills(List skills) {
        this.skills = skills;
        return true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public boolean setDirty(boolean dirty, int time) {
        isDirty = dirty;
        setDirtyTime(time);
        return true;
    }

    public boolean isTired() {
        return isTired;
    }

    public boolean setTired(boolean tired, int time) {
        isTired = tired;
        setTiredTime(time);
        return true;
    }

    public boolean isSick() {
        return isSick;
    }

    public boolean setSick(boolean sick, int time) {
        isSick = sick;
        setSickTime(time);
        return true;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public boolean setInjured(boolean injured, int time) {
        isInjured = injured;
        setInjuredTime(time);
        return true;
    }

    public int getDirtyTime() {
        return dirtyTime;
    }

    private boolean setDirtyTime(int dirtyTime) {
        this.dirtyTime = dirtyTime;
        return true;
    }

    public int getTiredTime() {
        return tiredTime;
    }

    private boolean setTiredTime(int tiredTime) {
        this.tiredTime = tiredTime;
        return true;
    }

    public int getSickTime() {
        return sickTime;
    }

    private boolean setSickTime(int sickTime) {
        this.sickTime = sickTime;
        return true;
    }

    public int getInjuredTime() {
        return injuredTime;
    }

    private boolean setInjuredTime(int injuredTime) {
        this.injuredTime = injuredTime;
        return true;
    }

    public boolean giveAlert() {
        return true;
    }

    public boolean bounceForward() {
        return true;
    }

    public boolean turnAround() {
        return true;
    }

    public boolean interactWitItem() {
        return true;
    }

    public boolean evolve(String JSON) {
        int power = this.getPower();
        int agility = this.getAgility();
        int speed = this.getSpeed();
        final int WEIGHTY = 50;
        final int MISTAKE_THRESHOLD = 3;
        final int FINAL_EVOLUTION_BRANCH = 2;

        String[] evolArray = this.getEvolutions();
        String evolution = evolArray[0];

        if(evolArray.length != 0){
            if(this.getCareMistakes()>MISTAKE_THRESHOLD){
                evolution = evolArray[evolArray.length-1];
            }
            else if(this.getWeight()>=WEIGHTY || (power > agility && power > speed) || evolArray.length == FINAL_EVOLUTION_BRANCH){
                evolution = evolArray[0];
            }
            else if(agility > power && agility > speed || (agility == power && speed == agility)){
                evolution = evolArray[1];
            }
            else if(speed > power && speed > agility){
                evolution = evolArray[2];
            }
            readJSON(evolution, JSON);
            return true;
        }
        return false;
    }

    public boolean readJSON(String evolution, String JSON){

        try{
            JSONObject jsonObject = new JSONObject(JSON);
            jsonObject = jsonObject.getJSONObject(evolution);
            String spritePathString = jsonObject.getString("spritePath");
            int spritePath = Integer.parseInt(spritePathString);
            this.setSprite(spritePath);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public void checkStatus(int time) {
        if (!isTired() && time >= lastTiredTime + 200 ) {
            setTired(true, time);
        }

        if (!isDirty() && time >= lastDirtyTime + 60 ) {
            setDirty(true, time);
        }

        if (time % 50 == 0) {
            if (hunger > 0) {
                hunger--;
            }
            if (happiness > 0) {
                happiness--;
            }
        }

        if (!isSick() && time >= lastSickTime + 100) {
            Random r = new Random();
            int roll = r.nextInt(100);

            if(roll > 90 - (10 * getCareMistakes())) {
                setSick(true, time);
            }

            lastSickTime = time;
        }
    }

    public Pet(int sprite, int power, int speed, int agility) {
        super(sprite, power, speed, agility);
    }

    public Pet(int sprite, int power, int speed, int agility, String[] evolutions) {
        super(sprite, power, speed, agility, evolutions);
    }
}
