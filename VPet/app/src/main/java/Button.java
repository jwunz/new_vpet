/**
 * Created by bwoods on 5/14/2017.
 */

public class Button extends UIElement {
    private String img;

    public String getImg(){
        return img;
    }
    public Button(String img, int x, int y){
        this.img = img;
        this.x = x;
        this.y = y;
    }
}
