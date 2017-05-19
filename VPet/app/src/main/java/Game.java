import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.neumont.pro200.vpet.R;

/**
 * Created by bwoods on 5/14/2017.
 */

public class Game extends AppCompatActivity{
    private Pet pet = new Pet("", 0, 0, 0, null);
    private Screen screen = new Screen();
    private Menu menu = new Menu(0,0,"",0,false,null);
    private android.widget.Button pill_button = (Button) findViewById(R.id.pill_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_menu);
        System.out.println("Bloop : " + pill_button);

        //Pill Button Code
        pill_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pill_button.setBackgroundColor(Color.BLUE);
            }
        });
    }

    public void main(){

    }

    public boolean run(){
        return true;
    }
    public boolean chooseAPet(){
        return true;
    }
}
