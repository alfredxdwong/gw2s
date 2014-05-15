package info.mornlight.gw2s.android.model.item;

/**
 * Created by alfred on 5/29/13.
 */
public class UpgradableItem extends ItemBase {
    protected InfusionSlot[] infusionSlots;
    protected InfixUpgrade infixUpgrade;
    protected int suffixItemId;
    protected int secondarySuffixItemId;

    public int getSuffixItemId() {
        return suffixItemId;
    }

    public int getSecondarySuffixItemId() { return secondarySuffixItemId; }

    public InfusionSlot[] getInfusionSlots() {
        return infusionSlots;
    }

    public InfixUpgrade getInfixUpgrade() {
        return infixUpgrade;
    }
}
