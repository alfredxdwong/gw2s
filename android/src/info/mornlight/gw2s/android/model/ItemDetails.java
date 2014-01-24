package info.mornlight.gw2s.android.model;

/**
 * Created by alfred on 5/22/13.
 */
public class ItemDetails extends ItemIndex {
    private String description;
    private int vendorValue;
    private GameType[] gameTypes;
    private ItemFlag[] flags;
    private Restriction[] restrictions;

    private Weapon weapon;
    private Armor armor;
    private Consumable consumable;
    private UpgradeComponent upgradeComponent;
    private Trinket trinket;
    private CraftingMaterial craftingMaterial;
    private Container container;
    private Trophy trophy;
    private Gizmo gizmo;
    private Tool tool;
    private Gathering gathering;
    private Bag bag;
    private Back back;
    private MiniPet miniPet;

    public ItemDetails() {
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Object getSubtype() {
        switch (getType()) {
            case Armor: return armor.getType();
            case Bag: return null;
            case Consumable: return consumable.getType();
            case Container: return container.getType();
            case CraftingMaterial: return null;
            case Gathering: return gathering.getType();
            case Gizmo: return gizmo.getType();
            case MiniPet: return null;
            case Tool: return tool.getType();
            case Trinket: return trinket.getType();
            case Trophy: return null;
            case UpgradeComponent: return upgradeComponent.getType();
            case Weapon: return weapon.getType();
            default: return null;
        }
    }

    public int getVendorValue() {
        return vendorValue;
    }

    public GameType[] getGameTypes() {
        return gameTypes;
    }

    public ItemFlag[] getFlags() {
        return flags;
    }

    public Restriction[] getRestrictions() {
        return restrictions;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Consumable getConsumable() {
        return consumable;
    }

    public Armor getArmor() {
        return armor;
    }

    public UpgradeComponent getUpgradeComponent() {
        return upgradeComponent;
    }

    public Object getDetails() {
        switch (getType()) {
            case Armor: return armor;
            case Bag: return bag;
            case Consumable: return consumable;
            case Container: return container;
            case CraftingMaterial: return craftingMaterial;
            case Gathering: return gathering;
            case Gizmo: return gizmo;
            case MiniPet: return miniPet;
            case Tool: return tool;
            case Trinket: return trinket;
            case Trophy: return trophy;
            case UpgradeComponent: return upgradeComponent;
            case Weapon: return weapon;
            default: return null;
        }
    }
}
