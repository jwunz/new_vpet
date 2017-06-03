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
import android.widget.TextView;

import java.util.Random;

public class StarCatcher extends AppCompatActivity implements View.OnTouchListener {
    private ImageView star;
    private int starCount = 0;
    private int score = 0;
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
        if(starCount < 10){
            starCount++;
            activateAnimation(field);
        }else{
            finishScreen();
        }
    }

    private void finishScreen(){
        //display score
        //display next button
        //next button should link to the following method:
        returnResult();
    }

    private void returnResult(){
        Intent intent = new Intent();
        intent.putExtra("score", score);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void incrementScore() {
        score++;
        TextView scoreLabel = (TextView)findViewById(R.id.scoreNum);
        scoreLabel.setText(Integer.toString(score));
    }

    public void activateAnimation(RelativeLayout field){
        final RelativeLayout starField = field;
        final Animation starfall = AnimationUtils.loadAnimation(this, R.anim.starfall);
        starfall.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation a) {}
            public void onAnimationRepeat(Animation a) {}
            public void onAnimationEnd(Animation a) {
                if (collidedWithPet()) {
                    incrementScore();
                }
                starField.removeView(star);
                spawnStar();
            }
        });
        star.startAnimation(starfall);

    }

    private boolean collidedWithPet() {
        ImageView pet = (ImageView)findViewById(R.id.petSprite);
        float lowRange = star.getX() - 200;
        float highRange = star.getX() + 200;
        return ((pet.getX() > lowRange) && (pet.getX() < highRange));
    }
}



