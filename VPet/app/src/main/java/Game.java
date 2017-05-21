import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.neumont.pro200.vpet.R;

/**
 * Created by bwoods on 5/14/2017.
 */

public abstract class Game extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private Screen screen = new Screen();
    private Menu menu = new Menu(0,0,"",0,false,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_menu);

    }

    public boolean run(){
        return true;
    }
    public boolean chooseAPet(Pet pet){
        findViewById(R.id.petSprite).setBackgroundResource(pet.getSprite());
        findViewById(R.id.ChoosePetMenu).setVisibility(View.GONE);
        findViewById(R.id.game_menu).setVisibility(View.VISIBLE);
        return true;
    }
}
