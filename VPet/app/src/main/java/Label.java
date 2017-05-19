/**
 * Created by bwoods on 5/14/2017.
 */

public class Label extends UIElement {
    private String text;

    public String getText(){
        return text;
    }
    public Label(String text, int x, int y){
        this.text = text;
        this.x = x;
        this.y = y;
    }

}
