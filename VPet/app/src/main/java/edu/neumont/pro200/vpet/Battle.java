package edu.neumont.pro200.vpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Battle extends AppCompatActivity {

    private String[] skillName = new String[4];
    private int[] skillPower = new int[4];
    private int[] skillAgility = new int[4];
    private int[] skillSpeed = new int[4];
    private int earnings = 0;
    private int playerHPTotal = 0;
    private int enemyHPTotal = 0;
    private int currentPlayerHP = 0;
    private int currentEnemyHP = 0;
    private Random r = new Random();
    private String message = "";
    private int statToIncrease = 4;


    private int petPower;
    private int petAgility;
    private int petSpeed;

    private int enemyPower;
    private int enemyAgility;
    private int enemySpeed;

    private final String[] enemyNames = {"1Aquan1", "1Forest1", "1Desert1", "2Bad1", "2Bad2", "2Bad3",
            "2Aquan1", "2Aquan2", "2Aquan3", "2Forest1", "2Forest2", "2Forest3", "2Desert1", "2Desert2", "2Desert3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int petSprite = extras.getInt("petSprite", R.drawable.notfound);
        findViewById(R.id.petSprite).setBackgroundResource(petSprite);
        petPower = extras.getInt("power", 0);
        petAgility = extras.getInt("agility", 0);
        petSpeed = extras.getInt("speed", 0);
        updateSkills(extras);
        readEnemyJSON();
        initiateHP(extras);
        updateHealth();
    }

    public void startGame (View view) {
        findViewById(R.id.Welcome).setVisibility(View.GONE);
        findViewById(R.id.GameMenu).setVisibility(View.VISIBLE);
    }

    public boolean readEnemyJSON(){

        try{
            String JSON = loadJSONFromAsset("pet.json");
            JSONObject jsonObject = new JSONObject(JSON);
            Random random = new Random();
            int index = random.nextInt(enemyNames.length);
            String randomEnemyName = enemyNames[index];
            jsonObject = jsonObject.getJSONObject(randomEnemyName);
            JSONObject statsObject = jsonObject.getJSONObject("stats");
            findViewById(R.id.enemySprite).setBackgroundResource(jsonObject.getInt("spritePath"));
            enemyPower = statsObject.getInt("power");
            enemyAgility = statsObject.getInt("agility");
            enemySpeed = statsObject.getInt("speed");
            enemyHPTotal = enemyPower + enemyAgility + enemySpeed;
        }catch(Exception e){
            return false;
        }
        return true;
    }

    private void updateSkills(Bundle extras){
        Button[] skills = new Button[]{(Button)findViewById(R.id.skill1), (Button)findViewById(R.id.skill2), (Button)findViewById(R.id.skill3)};
        fillDefaultAttack();
        int[] skillList = extras.getIntArray("skills");
            for(int i = 0; i < skillList.length; i++){
                int skill = skillList[i];
                if(skill!=0){
                    skills[i].setEnabled(true);
                    readSkillJson(skills[i], skill, i+1);
                }
        }
    }

    private void fillDefaultAttack(){
        skillName[0] = "Tackle";
        skillPower[0] = 1;
        skillAgility[0] = 1;
        skillSpeed[0] = 1;
        Button defaultSkill = (Button) findViewById(R.id.skill0);
        setButtonText(skillName[0], skillPower[0], skillAgility[0], skillSpeed[0], defaultSkill);
    }

    private boolean readSkillJson(Button skillButton, int skill, int ind){
        try{
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("skills.json"));
            String index = Integer.toString(skill);
            jsonObject = jsonObject.getJSONObject(index);

            skillName[ind] = jsonObject.getString("name");
            skillPower[ind] = jsonObject.getInt("power");
            skillAgility[ind] = jsonObject.getInt("agility");
            skillSpeed[ind] = jsonObject.getInt("speed");

            setButtonText(skillName[ind], skillPower[ind], skillAgility[ind], skillSpeed[ind], skillButton);
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

    public void updateHealth() {
        TextView playerHPText = (TextView) findViewById(R.id.playerHP);
        TextView enemyHPText = (TextView) findViewById(R.id.enemyHP);
        playerHPText.setText(currentPlayerHP + " / " + playerHPTotal);
        enemyHPText.setText(currentEnemyHP + " / " + enemyHPTotal);
    }

    public void TakeDamageStep(View view) {
        int currentButton = view.getId();
        int turnSpeed = 0;
        int turnAgility = 0;
        int turnPower = 0;
        String skillNameInput ="";


        switch (currentButton) {
            case (R.id.skill0):
                turnSpeed = petSpeed + skillSpeed[0];
                turnAgility = petAgility + skillAgility[0];
                turnPower = petPower + skillPower[0];
                skillNameInput = "Tackle";
                break;
            case (R.id.skill1):
                turnSpeed = petSpeed + skillSpeed[1];
                turnAgility = petAgility + skillAgility[1];
                turnPower = petPower + skillPower[1];
                skillNameInput = skillName[1];
                break;
            case (R.id.skill2):
                turnSpeed = petSpeed + skillSpeed[2];
                turnAgility = petAgility + skillAgility[2];
                turnPower = petPower + skillPower[2];
                skillNameInput = skillName[2];
                break;
            case (R.id.skill3):
                turnSpeed = petSpeed + skillSpeed[3];
                turnAgility = petAgility + skillAgility[3];
                turnPower = petPower + skillPower[3];
                skillNameInput = skillName[3];
                break;
        }
        int[] order;
        if (turnSpeed > enemySpeed) {
            order = damageCalculation(currentPlayerHP, currentEnemyHP, turnPower, turnAgility, enemyPower, enemyAgility);
            if(currentEnemyHP == order[1]){
                message += "\nThe enemy pet dodged your attack!";
            }else{
                currentEnemyHP = order[1];
                message += "\nYour pet used " + skillNameInput + "." + " It did " + turnPower + " damage!";
            }
            if(currentPlayerHP == order[0]){
                message += "\nYour pet has dodged their attack!";
            }else{
                currentPlayerHP = order[0];
                message += "\nThe opponent attacks! It dealt " + enemyPower + " damage!";
            }
        }else{
            order = damageCalculation(currentEnemyHP, currentPlayerHP, enemyPower, enemyAgility, turnPower, turnAgility);
            if(currentPlayerHP == order[1]){
                message += "\nYour pet has dodged their attack!";
            }else{
                currentPlayerHP = order[1];
                message += "\nThe opponent attacks! It dealt " + enemyPower + " damage!";
            }
            if(currentEnemyHP == order[0]) {
                message += "\nThe enemy pet dodged your attack!";
            }
            else {
                currentEnemyHP = order[0];
                message += "\nYour pet used " + skillNameInput + "." + " It did " + turnPower + " damage!";
            }
        }
        if(currentEnemyHP == 0){
            finishScreen(true);
        }else if(currentPlayerHP == 0){
            finishScreen(false);
        }
        updateHealth();
        updateUI(message);
    }

    private void updateUI(String comment) {
        TextView uiComments = (TextView) findViewById(R.id.statusMessages);
        uiComments.setText(comment);
        message = "";
    }

    private int[] damageCalculation(int yourHP, int theirHP, int yourPower, int yourAgility, int theirPower, int theirAgility){
        int randNum = r.nextInt(100);
        int randNum2 = r.nextInt(100);
        if (randNum > yourAgility / 3) {
            theirHP -= yourPower;
        }
        if (theirHP > 0) {
            if (randNum2 > theirAgility / 3) {
                yourHP -= theirPower;
            }
        } else {
            theirHP = 0;
        }
        if(yourHP < 0){
            yourHP = 0;
        }
        return new int[]{yourHP, theirHP};
    }

    public void initiateHP (Bundle b) {
        playerHPTotal = (b.getInt("power", 0) + b.getInt("agility", 0) + (b.getInt("speed", 0)));
        currentPlayerHP = playerHPTotal;
        currentEnemyHP = enemyHPTotal;
    }

    public void returnResult(View view){

        Bundle extras = new Bundle();
        Intent intent = new Intent();
        extras.putInt("earnings", earnings);
        extras.putInt("statIndex", statToIncrease);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int findHighestStat(int power, int speed, int agility){
        int curStatIndex;
        if(power > agility && power > speed){
            curStatIndex = 0;
        }else if(speed > power && speed>agility){
            curStatIndex = 1;
        }else{
            curStatIndex = 2;
        }
        return curStatIndex;
    }

    private void finishScreen(boolean Victory){
        TextView details = (TextView) findViewById(R.id.finishedDetails);
        findViewById(R.id.skillsList).setVisibility(View.GONE);
        findViewById(R.id.skillList2).setVisibility(View.GONE);
        findViewById(R.id.finishScreen).setVisibility(View.VISIBLE);
        String finishText = "";
        TextView tView = (TextView) findViewById(R.id.finishText);
        if(Victory){
            earnings = enemyHPTotal/3;
            statToIncrease = findHighestStat(enemyPower, enemySpeed, enemyAgility);
            tView.setText("You Won "+ earnings + " dollars.");
        }else{
            tView.setText("Aw, you Lost.");
        }
        details.setText(finishText);
    }
}

