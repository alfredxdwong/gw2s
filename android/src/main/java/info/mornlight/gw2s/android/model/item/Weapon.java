package info.mornlight.gw2s.android.model.item;

/**
 * Created by alfred on 5/22/13.
 */
public class Weapon extends UpgradableItem {
    private WeaponType type;
    private DamageType damageType;
    private int minPower;
    private int maxPower;
    private int defense;

    public Weapon() {
    }

    public WeaponType getType() {
        return type;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public int getMinPower() {
        return minPower;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public int getDefense() {
        return defense;
    }


}
