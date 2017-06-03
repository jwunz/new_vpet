package edu.neumont.pro200.vpet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.test.PerformanceTestCase;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Random;

public class StartupMenu extends AppCompatActivity implements Serializable {
    private static final boolean AUTO_HIDE = false;
    private Pet pet;
    private int money = 0;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private int ticks = 0;
    private Random r = new Random();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    public void healSickness(View view) {
        if(pet.isSick()){
            pet.setSick(false, ticks);
            pet.setIsEating(true);
            findViewById(R.id.pill).setVisibility(View.VISIBLE);
            toggleAllButtons(false);
            findViewById(R.id.sickBubble).setVisibility(View.GONE);
        }else{
            Toast.makeText(view.getContext(), " Pet is not currently sick ", Toast.LENGTH_SHORT).show();
        }
    }

    public void healInjury(View view) {
        if(pet.isInjured()){
            pet.setInjured(false, ticks);
            pet.setIsEating(true);
            findViewById(R.id.bandage).setVisibility(View.VISIBLE);
            toggleAllButtons(false);
            findViewById(R.id.injuryBubble).setVisibility(View.GONE);
        }else{
            Toast.makeText(view.getContext(), " Pet is not currently injured ", Toast.LENGTH_SHORT).show();
        }
    }

    public void healTiredness(View view) {
        if (pet.isTired()) {
            pet.setTired(false, ticks);
            findViewById(R.id.sleepBubble).setVisibility(View.GONE);
            findViewById(R.id.activity_ui).setBackgroundColor(Color.DKGRAY);
            pet.setIsSleeping(true);
            toggleAllButtons(false);
        }else{
            Toast.makeText(view.getContext(), " Pet is not currently tired ", Toast.LENGTH_SHORT).show();
        }
    }

    public void healDirtiness(View view){
        if(pet.isDirty()){
            pet.setDirty(false, ticks);
            findViewById(R.id.dirtyBubble).setVisibility(View.GONE);
            findViewById(R.id.mess).setVisibility(View.GONE);
        }else{
            Toast.makeText(view.getContext(), " Pet is not currently dirty ", Toast.LENGTH_SHORT).show();
        }
    }

    public void IncreaseHungerBar(View view) {
        if (pet.getHunger() < 5) {
            pet.setIsEating(true);
            findViewById(R.id.meat).setVisibility(View.VISIBLE);
            toggleAllButtons(false);
            pet.setHunger(pet.getHunger() + 1);
            pet.setWeight(pet.getWeight() + .5);
        }else{
            Toast.makeText(view.getContext(), " Pet is not currently hungry ", Toast.LENGTH_SHORT).show();
        }
        if(pet.getHunger() > 1){
            pet.setHungry(false, -1);
        }
    }

    public void changeMenu(View view) {
        findViewById(R.id.ChoosePetMenu).setVisibility(View.GONE);
        findViewById(R.id.GameMenu).setVisibility(View.VISIBLE);
        updateSkillShop();
        activateAnimation(view);
    }

    private void showChoosePetMenu() {
        findViewById(R.id.GameMenu).setVisibility(View.GONE);
        findViewById(R.id.ChoosePetMenu).setVisibility(View.VISIBLE);
    }

    public boolean playSound () {
            MediaPlayer player=MediaPlayer.create(this,R.raw.sound);
            player.start();
            return true;
    }

    public void displayStats(View view){
        Button tView = (Button) findViewById(R.id.stats_menu);
        String happyS = "Happiness: " + pet.getHappiness();
        String hungerS = "Hunger: " + pet.getHunger();
        String ageS = "Age: " + pet.getAge();
        String powerS = "Power: " + pet.getPower();
        String agilityS = "Agility: " + pet.getAgility();
        String speedS = "Speed: " + pet.getSpeed();
        String weightS = "Weight: " + pet.getWeight();
        String careS = "Care Mistakes: " + pet.getCareMistakes();
        String moneyS = "Money: " + money;
        tView.setText(happyS + "\n" + hungerS + "\n" + ageS + "\n" + powerS + "\n" + agilityS + "\n" + speedS + "\n" + weightS + "\n" + careS + "\n" + moneyS);
        tView.setVisibility(View.VISIBLE);
    }

    public void activateAnimation(View view){
        findViewById(R.id.petSprite).setBackgroundResource(pet.getSprite());
        final ImageView img = (ImageView) findViewById(R.id.petSprite);
        final LinearLayout pet_condition = (LinearLayout) findViewById(R.id.pet_condition);
        final Animation walkRight = AnimationUtils.loadAnimation(this, R.anim.walkingright);
        final Animation walkLeft = AnimationUtils.loadAnimation(this, R.anim.walkingleft);
        final Animation eat = AnimationUtils.loadAnimation(this,R.anim.eat);
        final Animation rest = AnimationUtils.loadAnimation(this,R.anim.rest);

        walkRight.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {
            }

            public void onAnimationRepeat(Animation a) {
                img.setBackgroundResource(pet.getSprite());

            }
            public void onAnimationEnd(Animation a) {
                img.setRotationY(180);
                incrementTime();
                pet_condition.startAnimation(walkLeft);
            }
        });

        walkLeft.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {
            }

            public void onAnimationRepeat(Animation a) {
                img.setBackgroundResource(pet.getSprite());
            }
            public void onAnimationEnd(Animation a) {
                img.setRotationY(0);
                if(pet.getIsEating()) {
                    img.startAnimation(eat);
                }else if(pet.getIsSleeping()){
                    img.startAnimation(rest);
                }
                else {
                    pet_condition.startAnimation(walkRight);
                }
            }
        });

        eat.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {
            }

            public void onAnimationRepeat(Animation a) {
                img.setBackgroundResource(pet.getSprite());
            }
            public void onAnimationEnd(Animation a) {
                pet.setIsEating(false);
                toggleOffConsumable();
                pet_condition.startAnimation(walkRight);
                toggleAllButtons(true);
            }
        });

        rest.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {
            }

            public void onAnimationRepeat(Animation a) {
                img.setBackgroundResource(pet.getSprite());
            }
            public void onAnimationEnd(Animation a) {
                pet.setIsSleeping(false);
                findViewById(R.id.activity_ui).setBackgroundColor(Color.WHITE);
                pet_condition.startAnimation(walkRight);
                toggleAllButtons(true);
            }
        });

        pet_condition.startAnimation(walkRight);
    }

    private void toggleOffConsumable(){
        findViewById(R.id.meat).setVisibility(View.GONE);
        findViewById(R.id.pill).setVisibility(View.GONE);
        findViewById(R.id.bandage).setVisibility(View.GONE);
    }

    public void incrementTime() {
        ticks += 1;
        increaseAge();
        checkStatus();
        inflictCareMistake();
        checkForAilment();
        savePrefs();
    }

    public boolean inflictCareMistake(){
        boolean inflicted = false;
        boolean isDirty = petIsInflicted(pet.isDirty(), pet.getDirtyTime());
        boolean isTired = petIsInflicted(pet.isTired(), pet.getTiredTime());
        boolean isSick = petIsInflicted(pet.isDirty(), pet.getDirtyTime());
        boolean isInjured = petIsInflicted(pet.isInjured(), pet.getInjuredTime());
        boolean isHungry = petIsInflicted(pet.isHungry(), pet.getHungryTime());
        boolean isSad = petIsInflicted(pet.isSad(), pet.getSadTime());
        if(isDirty || isTired || isSick || isInjured || isHungry || isSad) {
            pet.setCareMistakes(pet.getCareMistakes() + 1);
            inflicted = true;
        }

        return inflicted;
    }

    private boolean petIsInflicted(boolean hasAilment, int startOfAilment){
        int careThreshold = 20; //20
        startOfAilment -= 1;
        if(hasAilment && ((ticks - startOfAilment) % careThreshold == 0)){
            String a = "a";
            return true;
        }
        return false;
    }

    public boolean checkStatus() {
        boolean changed = false;

        if (!pet.isTired() && ticks >= pet.getLastTiredTime() + 80 ) { //80
            pet.setTired(true, ticks);
            findViewById(R.id.sleepBubble).setVisibility(View.VISIBLE);
            changed = true;
        }

        if (!pet.isDirty() && ticks >= pet.getLastDirtyTime() + 40 ) { //40
            pet.setDirty(true, ticks);
            findViewById(R.id.dirtyBubble).setVisibility(View.VISIBLE);
            findViewById(R.id.mess).setVisibility(View.VISIBLE);
            changed = true;
        }

        if (ticks % 30 == 0) { //30
            if (pet.getHunger() > 0) {
                pet.setHunger(pet.getHunger() - 1);
                changed = true;
            }
            if(pet.getHunger() <= 1){
                pet.setHungry(true, ticks);
            }
            if (pet.getHappiness() > 0) {
                pet.setHappiness(pet.getHappiness() - 1);
                changed = true;
            }
            if(pet.getHappiness() <= 1){
                pet.setSad(true, ticks);
            }
        }

        if (!pet.isSick() && ticks >= pet.getLastSickTime() + 100) { //100
            Random r = new Random();
            int roll = r.nextInt(100);

            if(roll > 90 - (10 * pet.getCareMistakes())) {
                pet.setSick(true, ticks);
                findViewById(R.id.sickBubble).setVisibility(View.VISIBLE);
                changed = true;
            }
        }

        return changed;
    }

    public boolean increaseAge() {
        if (ticks % 200 == 0) { //200
            pet.setAge(pet.getAge() + 1);
            evolvePet();
            updateSkillShop();
            petDeath();
            findViewById(R.id.activity_ui).setBackgroundColor(Color.WHITE);
            return true;
        }
        return false;
    }

    private boolean evolvePet() {
        if (pet.getAge() % 5 == 0) { //5
            pet.evolve(loadJSONFromAsset("pet.json"));
            findViewById(R.id.petSprite).setBackgroundResource(pet.getSprite());

            return true;
        }
        return false;
    }

    private void updateSkillShop(){
        Button[] skills = new Button[]{(Button)findViewById(R.id.skill1), (Button)findViewById(R.id.skill2), (Button)findViewById(R.id.skill3)};
        for(int i = 0; i < skills.length; i++){
            readSkillJson(skills[i]);
        }
    }

    private void petDeath() {
        Random randESavage = new Random();
        if (randESavage.nextInt(100) < pet.getCareMistakes() * pet.getAge()) {
            findViewById(R.id.sleepBubble).setVisibility(View.GONE);
            findViewById(R.id.sickBubble).setVisibility(View.GONE);
            findViewById(R.id.injuryBubble).setVisibility(View.GONE);
            findViewById(R.id.dirtyBubble).setVisibility(View.GONE);
            ticks = 0; money = 0;
            showChoosePetMenu();
        }
    }

    private boolean readSkillJson(Button skill){
        try{
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("skills.json"));
            int randomIndexNum = r.nextInt(jsonObject.length()-1);
            String randomIndex = Integer.toString(randomIndexNum);
            jsonObject = jsonObject.getJSONObject(randomIndex);

            String skillName = jsonObject.getString("name");
            int skillPower = jsonObject.getInt("power");
            int skillAgility = jsonObject.getInt("agility");
            int skillSpeed = jsonObject.getInt("speed");
            int skillPrice = jsonObject.getInt("price");

            String skillBuilder = "#" + randomIndex + " " + skillName + "\n" + "Power: " + skillPower + "\n Agility: " + skillAgility + "\n Speed: " + skillSpeed + "\n Price: $" + skillPrice;
            skill.setText(skillBuilder);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public void addSkillToList(View view){
        Button button = (Button) view;
        int index = button.getText().charAt(1);
        int price = Integer.parseInt(button.getText().toString().substring(button.getText().toString().indexOf('$')+1));
        for(int i = 0; i < pet.getSkills().length; i++){
            if((Integer)pet.getSkills()[i] != null){
                pet.getSkills()[i] = index;
                money -= price;
                break;
            }
        }
    }

    public void checkForAilment() {
        if ((pet.isHungry()) || (pet.isSad()) || (pet.isDirty()) || (pet.isInjured()) || (pet.isTired()) || (pet.isSick())) {
            playSound();
        }

    }

    public void toggleAllButtons(boolean bool){
        findViewById(R.id.pill_button).setEnabled(bool);
        findViewById(R.id.bandage_button).setEnabled(bool);
        findViewById(R.id.medicine_button).setEnabled(bool);
        findViewById(R.id.praise_button).setEnabled(bool);
        findViewById(R.id.scold_button).setEnabled(bool);
        findViewById(R.id.skill1).setEnabled(bool);
        findViewById(R.id.skill2).setEnabled(bool);
        findViewById(R.id.skill3).setEnabled(bool);
        findViewById(R.id.star_button).setEnabled(bool);
        findViewById(R.id.dance_button).setEnabled(bool);
        findViewById(R.id.sandbag_button).setEnabled(bool);
        findViewById(R.id.skill1).setEnabled(bool);
        findViewById(R.id.light_button).setEnabled(bool);
        findViewById(R.id.soap_button).setEnabled(bool);
        findViewById(R.id.food_button).setEnabled(bool);
        findViewById(R.id.stats_button).setEnabled(bool);
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void chooseAquanPet(View view) {
        String[] evolutions = new String[]{"2Aquan1", "2Aquan2", "2Aquan3"};
        this.pet = new Pet(R.drawable.one_aquan_one, 32, 30, 38, evolutions);
        changeMenu(view);
    }

    public void chooseForestPet(View view) {
        String[] evolutions = new String[]{"2Forest1", "2Forest2", "2Forest3"};
        this.pet = new Pet(R.drawable.one_forest_one, 30, 32, 38, evolutions);
        changeMenu(view);
    }

    public void chooseDesertPet(View view) {
        String[] evolutions = new String[]{"2Desert1", "2Desert2", "2Desert3"};
        this.pet = new Pet(R.drawable.one_desert_one, 38, 30, 32, evolutions);
        changeMenu(view);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startup_menu);
        ((RadioGroup) findViewById(R.id.menu_group)).setOnCheckedChangeListener(ToggleListener);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        if (loadPrefs()) {
            changeMenu(findViewById(R.id.petSprite));
        }
    }

    static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup group, final int i) {
            for (int j = 0; j < group.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) group.getChildAt(j);
                view.setChecked(view.getId() == i);
            }
        }
    };

    public void toggleAllMenusOff(View view) {
        findViewById(R.id.hand_menu).setVisibility(View.INVISIBLE);
        findViewById(R.id.game_menu).setVisibility(View.INVISIBLE);
        findViewById(R.id.shop_menu).setVisibility(View.INVISIBLE);
        findViewById(R.id.medicine_menu).setVisibility(View.INVISIBLE);
        findViewById(R.id.stats_menu).setVisibility(View.INVISIBLE);
        ((RadioGroup) findViewById(R.id.menu_group)).setOnCheckedChangeListener(ToggleListener);
    }

    public void toggleShopMenu(View view) {
        toggleAllMenusOff(view);
        ((RadioGroup) view.getParent()).check(view.getId());
        ToggleButton shop_button = (ToggleButton) findViewById(R.id.shop_button);
        if (shop_button.isChecked()) {
            findViewById(R.id.shop_menu).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.shop_menu).setVisibility(View.INVISIBLE);
        }
    }

    public void toggleMedicineMenu(View view) {
        toggleAllMenusOff(view);
        ((RadioGroup) view.getParent()).check(view.getId());
        ToggleButton pill_button = (ToggleButton) findViewById(R.id.pill_button);
        if (pill_button.isChecked()) {
            findViewById(R.id.medicine_menu).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.medicine_menu).setVisibility(View.INVISIBLE);
        }
    }

    public void toggleHandMenu(View view) {
        toggleAllMenusOff(view);
        ((RadioGroup) view.getParent()).check(view.getId());
        ToggleButton hand_button = (ToggleButton) findViewById(R.id.hand_button);
        if (hand_button.isChecked()) {
            findViewById(R.id.hand_menu).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.hand_menu).setVisibility(View.INVISIBLE);
        }
    }

    public void toggleGameMenu(View view) {
        toggleAllMenusOff(view);
        ((RadioGroup) view.getParent()).check(view.getId());
        ToggleButton game_button = (ToggleButton) findViewById(R.id.games_button);
        if (game_button.isChecked()) {
            findViewById(R.id.game_menu).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.game_menu).setVisibility(View.INVISIBLE);
        }
    }

    public void gameButtonHit(View view) {
            int statToIncrement = r.nextInt(3);
            pet.setHappiness(pet.getHappiness()+1);
            if(pet.getHappiness() > 1){
                pet.setSad(false, -1);
            }
            switch (statToIncrement) {
                case 0:
                    pet.setPower(pet.getPower() + 10);
                    break;
                case 1:
                    pet.setSpeed(pet.getSpeed() + 10);
                    break;
                case 2:
                    pet.setAgility(pet.getAgility() + 10);
                    break;
            }
            setPetInjury();
    }

    public void StartStarCatcher (View view) {
        if(pet.getHappiness()<5){
            gameButtonHit(view);
            Intent intent = new Intent(this, StarCatcher.class);
            intent.putExtra("petSprite", pet.getSprite());
            startActivity(intent);
        }else{
            Toast.makeText(view.getContext(), " Pet is at maximum happiness! ", Toast.LENGTH_SHORT).show();
        }
    }

    public void startBattle (View view) {
        Intent intent = new Intent(this, Battle.class);
        Bundle extras = new Bundle();
        extras.putInt("petSprite", pet.getSprite());
        extras.putInt("power", pet.getPower());
        extras.putInt("agility", pet.getAgility());
        extras.putInt("speed", pet.getSpeed());
        extras.putIntArray("skills", pet.getSkills());
        intent.putExtras(extras);
        startActivity(intent);
        battleAftermath();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public void battleAftermath() {
        //increase money
        money += 20;
        setPetInjury();
    }

    private boolean setPetInjury(){
        int getInjured = r.nextInt(3);
        if (getInjured >= 2) {
            findViewById(R.id.injuryBubble).setVisibility(View.VISIBLE);
            pet.setInjured(true, ticks);
            return true;
        }
        return false;

    }

    public void savePrefs() {
        SharedPreferences.Editor editor = getSharedPreferences("petSave", MODE_PRIVATE).edit();
        editor.putInt("happiness", pet.getHappiness());
        editor.putInt("hunger", pet.getHunger());
        editor.putFloat("weight", (float)pet.getWeight());
        editor.putFloat("discipline", pet.getDiscipline());
        editor.putInt("care_mistakes", pet.getCareMistakes());
        editor.putInt("age", pet.getAge());
        editor.putString("skills", pet.getSkillsString());
        editor.putBoolean("is_dirty", pet.isDirty());
        editor.putBoolean("is_tired", pet.isTired());
        editor.putBoolean("is_sick", pet.isSick());
        editor.putBoolean("is_injured", pet.isInjured());
        editor.putInt("sprite_path", pet.getSprite());
        editor.putInt("power", pet.getPower());
        editor.putInt("agility", pet.getAgility());
        editor.putInt("speed", pet.getSpeed());
        editor.putString("evolutions", pet.getEvolutionsString());
        editor.apply();
    }

    public boolean loadPrefs() {
        SharedPreferences petSave = getSharedPreferences("petSave", MODE_PRIVATE);
        int happiness = petSave.getInt("happiness", -1);
        int hunger = petSave.getInt("hunger", -1);
        double weight = petSave.getFloat("weight", -1);
        float discipline = petSave.getFloat("discipline", -1);
        int careMistakes = petSave.getInt("care_mistakes", -1);
        int age = petSave.getInt("age", -1);
        String[] skillsStr = loadArray(petSave.getString("skills", ""));
        boolean isDirty = petSave.getBoolean("is_dirty", false);
        boolean isTired = petSave.getBoolean("is_tired", false);
        boolean isSick = petSave.getBoolean("is_sick", false);
        boolean isInjured = petSave.getBoolean("is_injured", false);
        int spritePath = petSave.getInt("sprite_path", -1);
        int power = petSave.getInt("power", -1);
        int agility = petSave.getInt("agility", -1);
        int speed = petSave.getInt("speed", -1);
        String[] evolutions = loadArray(petSave.getString("evolutions", ""));

        int[] skills = new int[3];
        int i = 0;
        for (String skill:skillsStr) {
            skills[i] = Integer.parseInt(skill);
            i++;
        }

        pet = new Pet(spritePath, power, speed, agility, evolutions, happiness, hunger, weight, discipline, careMistakes, age, skills, isDirty, isTired, isSick, isInjured);
        return !(happiness == -1 || hunger == -1 || weight == -1 || discipline == -1 || careMistakes == -1 || age == -1 || spritePath == -1 || power == -1 || agility == -1 || speed == -1 );
    }

    private String[] loadArray(String str) {
        String[] strs = str.split(",");
        return strs;
    }
}