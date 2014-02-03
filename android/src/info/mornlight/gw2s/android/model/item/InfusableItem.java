package info.mornlight.gw2s.android.model.item;

/**
 * Created by alfred on 5/29/13.
 */
public class InfusableItem extends ItemBase implements InfixUpgradable {
    protected InfusionSlot[] infusionSlots;
    protected InfixUpgrade infixUpgrade;

    public InfusionSlot[] getInfusionSlots() {
        return infusionSlots;
    }

    public InfixUpgrade getInfixUpgrade() {
        return infixUpgrade;
    }
}
