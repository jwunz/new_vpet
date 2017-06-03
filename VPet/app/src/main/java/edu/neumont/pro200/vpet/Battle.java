package edu.neumont.pro200.vpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Battle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int petSprite = extras.getInt("petSprite", R.drawable.notfound);
        findViewById(R.id.petSprite).setBackgroundResource(petSprite);
        updateSkills(extras);
    }

    public void startGame (View view) {
        findViewById(R.id.Welcome).setVisibility(View.GONE);
        findViewById(R.id.GameMenu).setVisibility(View.VISIBLE);
    }

    private void gameOver() {

    }

    private String lossMessage() {
        return "You lose, better luck next time.";
    }

    private String victoryMessage() {
        return "You won and gained " + "*REPLACE WITH MONEY AMOUNT*" + " money!";
    }

    private void updateSkills(Bundle extras){
        Button[] skills = new Button[]{(Button)findViewById(R.id.skill1), (Button)findViewById(R.id.skill2), (Button)findViewById(R.id.skill3)};
        for(int i = 0; i < skills.length; i++){
            try{
                int[] skillList = extras.getIntArray("skills");
                int skill = skillList[i];
                readSkillJson(skills[i], skill);
            }catch(Exception e){

            }
        }
    }

    private boolean readSkillJson(Button skillButton, int skill){
        try{
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("skills.json"));
            String index = Integer.toString(skill);
            jsonObject = jsonObject.getJSONObject(index);

            String skillName = jsonObject.getString("name");
            int skillPower = jsonObject.getInt("power");
            int skillAgility = jsonObject.getInt("agility");
            int skillSpeed = jsonObject.getInt("speed");

            String skillBuilder = skillName + "\n" + "Po: " + skillPower + "/Ag: " + skillAgility + "/Sp: " + skillSpeed;
            skillButton.setText(skillBuilder);
        }catch (Exception e){
            return false;
        }
        return true;
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
}
