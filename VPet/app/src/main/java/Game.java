import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Pet pet = new Pet("", 0, 0, 0, null);
    private Screen screen = new Screen();
    private Menu menu = new Menu(0,0,"",0,false,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_menu);
     //   pill_button.setOnCheckedChangeListener(togglePillMenu);
    }

    final static CompoundButton.OnCheckedChangeListener togglePillMenu = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    //        buttonView.setVisibility(View.INVISIBLE);
           // pill_button.setText("bloop");
           // medicine_menu.setVisibility(View.VISIBLE);

        }
    };

    public boolean run(){
        return true;
    }
    public boolean chooseAPet(){
        return true;
    }
}
