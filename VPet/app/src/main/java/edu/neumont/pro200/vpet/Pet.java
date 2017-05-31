package edu.neumont.pro200.vpet;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by JWunz on 5/13/17.
 */

public class Pet extends Monster {
    private int happiness = 3;
    private int hunger = 3;
    private double weight = 12;
    private float discipline;
    private int careMistakes;
    private int age;
    private int[] skills = new int[2];
    private boolean isDirty;
    private boolean isTired;
    private boolean isSick;
    private boolean isInjured;
    private boolean isHungry;
    private boolean isSad;
    private int dirtyTime = 0;

    public void setHungryTime(int hungryTime) {
        this.hungryTime = hungryTime;
    }

    public void setSadTime(int sadTime) {
        this.sadTime = sadTime;
    }

    private int tiredTime = 0;
    private int sickTime = 0;
    private int injuredTime = 0;
    private int hungryTime = 0;
    private int sadTime = 0;

    public int getLastDirtyTime() {
        return lastDirtyTime;
    }

    public int getLastTiredTime() {
        return lastTiredTime;
    }

    public int getLastSickTime() {
        return lastSickTime;
    }

    public void setLastSickTime(int lastSickTime) {
        this.lastSickTime = lastSickTime;
    }

    private int lastDirtyTime = 0;
    private int lastTiredTime = 0;
    private int lastSickTime = 0;

    public int getLastHungerTime() {
        return lastHungerTime;
    }

    public void setLastHungerTime(int lastHungerTime) {
        this.lastHungerTime = lastHungerTime;
    }

    public int getLastSadTime() {
        return lastSadTime;
    }

    public void setLastSadTime(int lastSadTime) {
        this.lastSadTime = lastSadTime;
    }

    private int lastHungerTime = 0;
    private int lastSadTime = 0;

    public int getLastInjuredTime() {
        return lastInjuredTime;
    }

    private int lastInjuredTime = 0;

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

    public int[] getSkills() {
        return skills;
    }

    public boolean setSkills(int[] skills) {
        this.skills = skills;
        return true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public boolean setDirty(boolean dirty, int time) {
        isDirty = dirty;
        if(dirty){

            setDirtyTime(time);
        }else{
            setLastDirtyTime(time);
        }
        return true;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public boolean setHungry(boolean hungry, int time){
        isHungry = hungry;
        if(hungry){
            setHungryTime(time);
        }else{
            setLastHungerTime(time);
        }
        return true;
    }

    public boolean isSad() {
        return isSad;
    }

    public boolean setSad(boolean sad, int time){
        isSad = sad;
        if(sad){
            setSadTime(time);
        }else{
            setLastSadTime(time);
        }
        return true;
    }

    public boolean isTired() {
        return isTired;
    }

    public void setLastDirtyTime(int lastDirtyTime) {
        this.lastDirtyTime = lastDirtyTime;
    }

    public void setLastTiredTime(int lastTiredTime) {
        this.lastTiredTime = lastTiredTime;
    }

    public void setLastInjuredTime(int lastInjuredTime) {
        this.lastInjuredTime = lastInjuredTime;
    }

    public boolean setTired(boolean tired, int time) {
        isTired = tired;
        if(tired){
            setTiredTime(time);
        }else{
            setLastTiredTime(time);
        }
        return true;
    }

    public boolean isSick() {
        return isSick;
    }

    public boolean setSick(boolean sick, int time) {
        isSick = sick;
        if(sick){
            setSickTime(time);
        }else{
            setLastSickTime(time);
        }
        return true;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public boolean setInjured(boolean injured, int time) {
        isInjured = injured;
        if(injured){
            setInjuredTime(time);
        }else{
            setLastInjuredTime(time);
        }
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

    public boolean evolve(String JSON) {
        int power = this.getPower();
        int agility = this.getAgility();
        int speed = this.getSpeed();
        final int WEIGHTY = 50;
        final int MISTAKE_THRESHOLD = 3;
        final int FINAL_EVOLUTION_BRANCH = 2;

        String[] evolArray = this.getEvolutions();
        String evolution = "";

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
            this.setEvolutions(toStringArray(jsonObject.getJSONArray("evolutions")));
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }

    public Pet(int sprite, int power, int speed, int agility, String[] evolutions) {
        super(sprite, power, speed, agility, evolutions);
    }
}
