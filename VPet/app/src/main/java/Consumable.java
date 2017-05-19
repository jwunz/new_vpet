/**
 * Created by bwoods on 5/14/2017.
 */

public abstract class Consumable {
    private int x;
    private int y;
    private String img;

    public int getX(){
        return x;
    };
    public int getY(){
        return y;
    }
    public String getImg(){
        return img;
    }
    public boolean setImg(String img){
        this.img = img;
        return true;
    }
    public Consumable(String img){

    }
}
