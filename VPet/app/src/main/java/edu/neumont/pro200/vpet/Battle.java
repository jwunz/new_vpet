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
    private int[] skillPower = new int[3];
    private int[] skillAgility = new int[3];
    private int[] skillSpeed = new int[3];
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
        fillDefaultAttack();
        int[] skillList = extras.getIntArray("skills");
            for(int i = 0; i < skillList.length; i++){
                int skill = skillList[i];
                if(skill!=0){
                    readSkillJson(skills[i], skill, i);
                }
            }
    }

    private void fillDefaultAttack(){
        String skillName = "Tackle";
        skillPower[0] = 1;
        skillAgility[0] = 1;
        skillSpeed[0] = 1;
        Button defaultSkill = (Button) findViewById(R.id.skill0);
        setButtonText(skillName, skillPower[0], skillAgility[0], skillSpeed[0], defaultSkill);
    }

    private boolean readSkillJson(Button skillButton, int skill, int ind){
        try{
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("skills.json"));
            String index = Integer.toString(skill);
            jsonObject = jsonObject.getJSONObject(index);

            String skillName = jsonObject.getString("name");
            skillPower[ind] = jsonObject.getInt("power");
            skillAgility[ind] = jsonObject.getInt("agility");
            skillSpeed[ind] = jsonObject.getInt("speed");

            setButtonText(skillName, skillPower[ind], skillAgility[ind], skillSpeed[ind], skillButton);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private void setButtonText(String name, int pow, int agi, int spe, Button skillButton){
        String skillBuilder = name + "\n" + "Pow:" + pow + " | Agi:" + agi + " | Spe:" + spe;
        skillButton.setText(skillBuilder);
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
