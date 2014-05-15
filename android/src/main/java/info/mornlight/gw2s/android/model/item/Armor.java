package info.mornlight.gw2s.android.model.item;

public class Armor extends UpgradableItem {
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
