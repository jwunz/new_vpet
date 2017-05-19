import java.util.List;

/**
 * Created by JWunz on 5/13/17.
 */

public abstract class Monster {
    private String img;
    private int power;
    private int speed;
    private int agility;
    private List<Monster> evolutions; //List isn't going to work because it's abstract.

    public String getImg(){
        return img;
    }

    public boolean setImg(String img) {
        return true;
    }

    public int getPower() {
        return power;
    }

    public boolean setPower(int power) {
        return true;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean setSpeed(int speed) {
        return true;
    }

    public int getAgility() {
        return agility;
    }

    public boolean setAgility(int agility) {
        return true;
    }

    public List<Monster> getEvolutions() {
        return null;
    }

    public boolean setEvolution(List<Monster> evolutions) {
        return true;
    }

    public Monster(String img, int power, int speed, int agility, List<Monster> evolutions) {
        this.img = img;
        this.power = power;
        this.speed = speed;
        this.agility = agility;
        this.evolutions = evolutions;
    }
}
