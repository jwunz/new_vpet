package edu.neumont.pro200.vpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class StarCatcher extends AppCompatActivity implements View.OnTouchListener {
    private ImageView star;
    private int starCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_catcher);
        Intent intent = getIntent();
        int petSprite = intent.getIntExtra("petSprite", R.drawable.notfound);
        findViewById(R.id.petSprite).setBackgroundResource(petSprite);

        final View touchView = findViewById(R.id.petSprite);
        touchView.setOnTouchListener(this);
    }

    public boolean onTouch(View v, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            View petSprite = findViewById(R.id.petSprite);
            petSprite.setX(event.getRawX()-petSprite.getWidth()/2);
        }
        return true;
    }

    public void startGame (View view) {
        findViewById(R.id.Welcome).setVisibility(View.GONE);
        findViewById(R.id.GameMenu).setVisibility(View.VISIBLE);
        spawnStar();
    }

    private void spawnStar(){
        RelativeLayout field = (RelativeLayout)findViewById(R.id.starField);
        Random starX = new Random();
        star = new ImageView(this);
        star.setBackgroundResource(R.drawable.star);
        star.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,200));
        star.setX((float)starX.nextInt(1000) + 1);
        field.addView(star);
        if(starCount < 20){
            starCount++;
            activateAnimation(field);
        }
    }

    public void activateAnimation(RelativeLayout field){
        final Animation starfall = AnimationUtils.loadAnimation(this, R.anim.starfall);
        final RelativeLayout starField = field;
        starfall.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {}
            public void onAnimationRepeat(Animation a) {}
            public void onAnimationEnd(Animation a) {
                starField.removeView(star);
                spawnStar();
            }
        });
        star.startAnimation(starfall);
    }
}

