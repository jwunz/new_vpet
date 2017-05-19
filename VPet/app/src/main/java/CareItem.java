/**
 * Created by bwoods on 5/14/2017.
 */

public class CareItem extends Consumable implements IRemedy {
    private AilmentType ailment;

    public CareItem(String img, AilmentType ailment) {
        super(img);
        setAilment(ailment);
    }

    public boolean setAilment(AilmentType ailment){
        this.ailment = ailment;
        return true;
    }
    @Override
    public boolean remedyAilment(){
        return true;
    }
}
