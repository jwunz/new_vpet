package edu.neumont.pro200.vpet;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonWriter;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    public void healSickness(View view) {
        pet.setSick(false, -1);
    }

    public void healInjury(View view) {
        pet.setInjured(false, -1);
    }


    public void healTiredness(View view) {
        pet.setTired(false, -1);
        findViewById(R.id.activity_ui).setBackgroundColor(Color.DKGRAY);
    }

    public void healDirtiness(View view){
        pet.setDirty(false, -1);
        findViewById(R.id.dirtyBubble).setVisibility(View.GONE);
        findViewById(R.id.mess).setVisibility(View.GONE);

    }

    public void IncreaseHungerBar(View view) {
        if (pet.getHunger() < 5) {
            pet.setHunger(pet.getHunger() + 1);
            pet.setWeight(pet.getWeight() + .5);
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

    public void playSound () {
        MediaPlayer player=MediaPlayer.create(this,R.raw.sound);
        player.start();
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
                pet_condition.startAnimation(walkRight);
            }
        });

        pet_condition.startAnimation(walkRight);
    }

    public void incrementTime() {
        ticks += 1;
        increaseAge();
        evolvePet();
        checkStatus();
        inflictCareMistake();
        autoSave();
    }

    public boolean inflictCareMistake(){
        boolean inflicted = false;
        boolean isDirty = petIsInflicted(pet.isDirty(), pet.getLastDirtyTime());
        boolean isTired = petIsInflicted(pet.isTired(), pet.getLastTiredTime());
        boolean isSick = petIsInflicted(pet.isDirty(), pet.getLastDirtyTime());
        boolean isInjured = petIsInflicted(pet.isInjured(), pet.getLastInjuredTime());
        boolean isHungry = petIsInflicted(pet.isHungry(), pet.getLastHungerTime());
        boolean isSad = petIsInflicted(pet.isSad(), pet.getLastSadTime());
        if(isDirty || isTired || isSick || isInjured || isHungry || isSad){
            pet.setCareMistakes(pet.getCareMistakes()+1);
            playSound();
            inflicted = true;
        }
        return inflicted;
    }

    private boolean petIsInflicted(boolean hasAilment, int durationOfAilment){
        int careThreshold = 20;
        if(hasAilment && ((ticks - durationOfAilment) % careThreshold == 0)){
            return true;
        }
        return false;
    }

    public boolean checkStatus() {
        boolean changed = false;

        if (!pet.isTired() && ticks >= pet.getLastTiredTime() + 200 ) { //200
            pet.setTired(true, ticks);
            findViewById(R.id.sleepBubble).setVisibility(View.VISIBLE);
            changed = true;
        }

        if (!pet.isDirty() && ticks >= pet.getLastDirtyTime() + 60 ) { //60
            pet.setDirty(true, ticks);
            findViewById(R.id.dirtyBubble).setVisibility(View.VISIBLE);
            findViewById(R.id.mess).setVisibility(View.VISIBLE);
            changed = true;
        }

        if (ticks % 50 == 0) { //50
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

            pet.setLastSickTime(ticks);
        }

        return changed;
    }

    public boolean autoSave() {
        if (ticks % 20 == 0) {

            return true;
        }
        return false;
    }

    public boolean increaseAge() {
        if (ticks >= 200) {
            pet.setAge(pet.getAge() + 1);
            evolvePet();
            updateSkillShop();
            return true;
        }
        return false;
    }

    private boolean evolvePet() {
        if (pet.getAge() > 5) {
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
        if ((pet.getHunger() <=1) || (pet.isDirty()) || (pet.isInjured()) || (pet.isTired()) || (pet.isSick())) {
            playSound();
        }

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
        // mContentView = findViewById(R.id.fullscreen_content);

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

    public void onToggle(View view) {
        ((RadioGroup) view.getParent()).check(view.getId());
        toggleHandMenu(view);
        toggleGameMenu(view);
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
            pet.setInjured(true, ticks);
            return true;
        }
        return false;

    }
    //JSON WRITE CODE IS BELOW
    //
    //
    //
    //
    //
    //

    public void writeJsonStream(OutputStream out) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeMessagesArray(writer);
        writer.close();
    }

    public void writeMessagesArray(JsonWriter writer) throws IOException {
        writer.beginArray();
        writeAutoSave(writer);
        writer.endArray();
    }

    public void writeAutoSave(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("happiness").value(pet.getHappiness());
        writer.name("hunger").value(pet.getHunger());
        writer.name("weight").value(pet.getWeight());
        writer.name("discipline").value(pet.getDiscipline());
        writer.name("care_mistakes").value(pet.getCareMistakes());
        writer.name("age").value(pet.getAge());
        writer.name("skills");
        writeIntArray(writer, pet.getSkills());
        writer.name("is_dirty").value(pet.isDirty());
        writer.name("is_tired").value(pet.isTired());
        writer.name("is_sick").value(pet.isSick());
        writer.name("dirty_time").value(pet.getDirtyTime());
        writer.name("tired_time").value(pet.getTiredTime());
        writer.name("sick_time").value(pet.getSickTime());
        writer.name("injured_time").value(pet.getInjuredTime());
        writer.name("sprite_path").value(pet.getSprite());
        writer.name("power").value(pet.getPower());
        writer.name("agility").value(pet.getAgility());
        writer.name("speed").value(pet.getSpeed());
        writer.name("evolutions");
        writeStringArray(writer, pet.getEvolutions());
    }

    public void writeIntArray(JsonWriter writer, int[] ints) throws IOException {
        writer.beginArray();
        for (int value : ints) {
            writer.value(value);
        }
        writer.endArray();
    }
    public void writeStringArray(JsonWriter writer, String[] strings) throws IOException {
        writer.beginArray();
        for (String value : strings) {
            writer.value(value);
        }
        writer.endArray();
    }
}