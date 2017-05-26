package edu.neumont.pro200.vpet;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonWriter;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class StartupMenu extends AppCompatActivity implements Serializable {
    private static final boolean AUTO_HIDE = false;
    private Pet pet;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private int ticks = 0;
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

    ;

    public void healInjury(View view) {
        pet.setInjured(false, -1);
    }

    ;

    public void healTiredness(View view) {
        pet.setTired(false, -1);
        findViewById(R.id.activity_ui).setBackgroundColor(Color.DKGRAY);
    }

    ;

    public void IncreaseHungerBar(View view) {
        if (pet.getHunger() < 5) {
            pet.setHunger(pet.getHunger() + 1);
            pet.setWeight(pet.getWeight() + .5);
        }
    }

    ;

    public void changeMenu(View view) {
        findViewById(R.id.ChoosePetMenu).setVisibility(View.GONE);
        findViewById(R.id.GameMenu).setVisibility(View.VISIBLE);
        activateAnimation(view);
    }

    public void activateAnimation(final View view) {
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
        if (pet.checkStatus(ticks)) {

        }
        autoSave();
    }

    public boolean autoSave() {
        if (ticks % 20 == 0) {

            return true;
        }
        return false;
    }

    public boolean increaseAge() {
        if (ticks >= 1) {
            pet.setAge(pet.getAge() + 1);
            evolvePet();
            return true;
        }
        return false;
    }

    public boolean evolvePet() {
        if (pet.getAge() > 1) {
            pet.evolve(loadJSONFromAsset("pet.json"));
            findViewById(R.id.petSprite).setBackgroundResource(pet.getSprite());
            return true;
        }
        return false;
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
    }

    public void onToggle(View view) {
        ((RadioGroup) view.getParent()).check(view.getId());
        toggleHandMenu(view);
        toggleGameMenu(view);
    }

    public void toggleMedicineMenu(View view) {
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
        Random randESavage = new Random();
        int statToIncrement = randESavage.nextInt(3);

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

        int getInjured = randESavage.nextInt(3);

        if (getInjured >= 2) {
            pet.setInjured(true, ticks);
        }
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