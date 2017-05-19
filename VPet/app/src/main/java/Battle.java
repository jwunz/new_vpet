/**
 * Created by JWunz on 5/13/17.
 */

public class Battle {
    public Pet userPet;
    public Monster opponentPet;
    public enum Difficulty{
        EASY, MEDIUM, HARD;
    }
    private Difficulty difficulty;

    private boolean beginBattle() {
        return true;
    }

    private boolean awardMoney() {
        return true;
    }

    private boolean generateOpponent() {
        return true;
    }

    private boolean giveInjury() {
        return true;
    }

    private boolean attack() {
        return  true;
    }
}
