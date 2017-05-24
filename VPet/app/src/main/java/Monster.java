import java.util.List;

/**
 * Created by JWunz on 5/13/17.
 */

public abstract class Monster {
    private int sprite;
    private int power;
    private int speed;
    private int agility;
    private List<Monster> evolutions; //List isn't going to work because it's abstract.

    public int getSprite(){
        return sprite;
    }

    public boolean setSprite(int sprite) {
        this.sprite = sprite;
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

    public Monster(int sprite, int power, int speed, int agility, List<Monster> evolutions) {
        this.sprite = sprite;
        this.power = power;
        this.speed = speed;
        this.agility = agility;
        this.evolutions = evolutions;
    }
}
