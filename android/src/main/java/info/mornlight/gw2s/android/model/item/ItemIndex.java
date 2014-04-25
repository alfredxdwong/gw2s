package info.mornlight.gw2s.android.model.item;

/*
The index part of item details, it will be stored in database and indexed for search.
 */


public class ItemIndex {
    protected ItemType type;
    private int itemId;
    private String name;
    private int level;
    private Rarity rarity;
    private int iconFileId;
    private String iconFileSignature;
    transient private Object subtype;

    public ItemIndex() {
    }

    public ItemIndex(int id, String name, Rarity rarity, ItemType type, int iconFileId, String iconFileSignature) {
        this.itemId = id;
        this.name = name;
        this.rarity = rarity;
        this.type = type;
        this.iconFileId = iconFileId;
        this.iconFileSignature = iconFileSignature;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Object getSubtype() {
        return subtype;
    }

    public void setSubtype(Object subtype) {
        this.subtype = subtype;
    }

    public String getImage() {
        return String.format("https://render.guildwars2.com/file/%s/%d.png", iconFileSignature, iconFileId);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIconFileId() {
        return iconFileId;
    }

    public String getIconFileSignature() {
        return iconFileSignature;
    }
}
