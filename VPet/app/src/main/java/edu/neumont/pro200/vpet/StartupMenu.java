package edu.neumont.pro200.vpet;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import static android.R.attr.animation;

public class StartupMenu extends AppCompatActivity {
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

    public void healSickness(View view){
        pet.setSick(false);
        pet.setSickTime(0);
    };

    public void healInjury(View view){
        pet.setInjured(false);
        pet.setInjuredTime(0);
    };

    public void IncreaseHungerBar(View view) {
        if (pet.getHunger() < 5) {
            pet.setHunger(1);
        }
    }

    public void changeMenu(View view){
        findViewById(R.id.ChoosePetMenu).setVisibility(View.GONE);
        findViewById(R.id.GameMenu).setVisibility(View.VISIBLE);
        activateAnimation(view);
    }

    public void activateAnimation(final View view){
        findViewById(R.id.petSprite).setBackgroundResource(pet.getSprite());
        final ImageView img = (ImageView) findViewById(R.id.petSprite);
        final Animation walkright = AnimationUtils.loadAnimation(this, R.anim.walkingright);
        final Animation walkleft = AnimationUtils.loadAnimation(this, R.anim.walkingleft);

        walkright.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a){
            }
            public void onAnimationRepeat(Animation a){}
            public void onAnimationEnd(Animation a) {
                img.setBackgroundResource(pet.getSprite());
                img.setRotationY(180);
                img.startAnimation(walkleft);
            }
        });

        walkleft.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a){
            }
            public void onAnimationRepeat(Animation a){}
            public void onAnimationEnd(Animation a) {
                img.setBackgroundResource(pet.getSprite());
                img.setRotationY(0);
                incrementTime();
                img.startAnimation(walkright);
            }
        });

        img.startAnimation(walkright);
    }

    public void incrementTime(){
        ticks+=1;
        increaseAge();
    }

    public boolean increaseAge(){
        if(ticks>=2){
            pet.setAge(pet.getAge()+1);
            pet.setSprite(R.drawable.two_aquan_one);
            findViewById(R.id.petSprite).setBackgroundResource(pet.getSprite());
            return true;
        }
        return false;
    }

    public void chooseAquanPet(View view){
        this.pet = new Pet(R.drawable.one_aquan_one, 32, 30, 38);
        changeMenu(view);
    }

    public void chooseForestPet(View view){
        this.pet = new Pet(R.drawable.one_forest_one, 30, 32, 38);
        changeMenu(view);
    }

    public void chooseDesertPet(View view){
        this.pet = new Pet(R.drawable.one_desert_one, 38, 30, 32);
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

    public void toggleAllMenusOff(View view){
        findViewById(R.id.hand_menu).setVisibility(View.INVISIBLE);
        findViewById(R.id.game_menu).setVisibility(View.INVISIBLE);
    }

    public void onToggle(View view){
        ((RadioGroup)view.getParent()).check(view.getId());
        toggleHandMenu(view);
        toggleGameMenu(view);
    }

    public void toggleMedicineMenu(View view){
        ToggleButton pill_button = (ToggleButton) findViewById(R.id.pill_button);
        if(pill_button.isChecked()){
            findViewById(R.id.medicine_menu).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.medicine_menu).setVisibility(View.INVISIBLE);
        }
    }

    public void toggleHandMenu(View view) {
        toggleAllMenusOff(view);
        ((RadioGroup)view.getParent()).check(view.getId());
        ToggleButton hand_button = (ToggleButton) findViewById(R.id.hand_button);
        if(hand_button.isChecked()){
            findViewById(R.id.hand_menu).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.hand_menu).setVisibility(View.INVISIBLE);
        }
    }

    public void toggleGameMenu(View view) {
        toggleAllMenusOff(view);
        ((RadioGroup)view.getParent()).check(view.getId());
        ToggleButton game_button = (ToggleButton) findViewById(R.id.games_button);
        if(game_button.isChecked()){
            findViewById(R.id.game_menu).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.game_menu).setVisibility(View.INVISIBLE);
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
}
