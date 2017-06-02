package edu.neumont.pro200.vpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;

import static edu.neumont.pro200.vpet.R.id.starSprite;

public class StarCatcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_catcher);
        Intent intent = getIntent();
        int petSprite = intent.getIntExtra("petSprite", R.drawable.notfound);
        findViewById(R.id.petSprite).setBackgroundResource(petSprite);
        spawnStars();
    }

    public void startGame (View view) {
        findViewById(R.id.Welcome).setVisibility(View.GONE);
        findViewById(R.id.GameMenu).setVisibility(View.VISIBLE);
    }

    public void spawnStars () {
        RelativeLayout field = (RelativeLayout)findViewById(R.id.starField);
        Random starX = new Random();
        for (int i = 1; i < 20; i++) {
            ImageView star = new ImageView(this);
            star.setBackgroundResource(R.drawable.star);
            star.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,200));
            star.setX((float)starX.nextInt(2000) + 1);

            field.addView(star);
        }
    }
}
