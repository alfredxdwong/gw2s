package info.mornlight.gw2s.android.model.item;

/**
 * Created by alfred on 5/28/13.
 */
public class UpgradeComponent extends ItemBase implements InfixUpgradable {
    private UpgradeComponentType type;
    private UpgradeComponentFlag[] flags;
    private InfusionFlag[] infusionUpgradeFlags;
    private InfixUpgrade infixUpgrade;
    private String suffix;

    private String[] bonuses;

    public UpgradeComponentType getType() {
        return type;
    }

    public UpgradeComponentFlag[] getFlags() {
        return flags;
    }

    public InfusionFlag[] getInfusionUpgradeFlags() {
        return infusionUpgradeFlags;
    }

    public InfixUpgrade getInfixUpgrade() {
        return infixUpgrade;
    }

    public String getSuffix() {
        return suffix;
    }

    public String[] getBonuses() { return bonuses; }
}
