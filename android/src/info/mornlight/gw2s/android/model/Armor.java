package info.mornlight.gw2s.android.model;

/**
 * Created by alfred on 5/28/13.
 */
public class Armor extends InfusableItem {
    private ArmorType type;
    private WeightClass weightClass;
    private int defense;

    public ArmorType getType() {
        return type;
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public int getDefense() {
        return defense;
    }
}
