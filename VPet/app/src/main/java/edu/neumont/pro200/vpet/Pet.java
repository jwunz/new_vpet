package edu.neumont.pro200.vpet;

import android.content.res.AssetManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JWunz on 5/13/17.
 */

public class Pet extends Monster {
    private int x;
    private int y;
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
    private int dirtyTime; // -
    private int tiredTime; // -These may be DateTimes or something of when the pet first became dirty/tired/sick by the end
    private int sickTime;  // -
    private int injuredTime;

    public int getX() {
        return x;
    }

    public boolean setX(int x) {
        this.x = x;
        return true;
    }

    public int getY() {
        return y;
    }

    public boolean setY(int y) {
        this.y = y;
        return true;
    }

    public int getHappiness() {
        return happiness;
    }

    public boolean setHappiness(int happiness) {
        this.happiness = happiness;
        return true;
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

    public boolean setDirty(boolean dirty) {
        isDirty = dirty;
        return true;
    }

    public boolean isTired() {
        return isTired;
    }

    public boolean setTired(boolean tired) {
        isTired = tired;
        return true;
    }

    public boolean isSick() {
        return isSick;
    }

    public boolean setSick(boolean sick) {
        isSick = sick;
        return true;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public boolean setInjured(boolean injured) {
        isInjured = injured;
        return true;
    }

    public int getDirtyTime() {
        return dirtyTime;
    }

    public boolean setDirtyTime(int dirtyTime) {
        this.dirtyTime = dirtyTime;
        return true;
    }

    public int getTiredTime() {
        return tiredTime;
    }

    public boolean setTiredTime(int tiredTime) {
        this.tiredTime = tiredTime;
        return true;
    }

    public int getSickTime() {
        return sickTime;
    }

    public boolean setSickTime(int sickTime) {
        this.sickTime = sickTime;
        return true;
    }

    public int getInjuredTime() {
        return injuredTime;
    }

    public boolean setInjuredTime(int injuredTime) {
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
        String evolution = "";

         double weight = this.getWeight();
        weight = weight;

        int evollength = evolArray.length;
        evollength = evollength;

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
            this.setSprite(jsonObject.getInt("spritePath"));
            this.setPower((this.getPower()+jsonObject.getInt("power"))/2);
            this.setAgility((this.getAgility()+jsonObject.getInt("agility"))/2);
            this.setSpeed((this.getSpeed()+jsonObject.getInt("speed"))/2);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public Pet(int sprite, int power, int speed, int agility, String[] evolutions) {
        super(sprite, power, speed, agility, evolutions);
    }
}
