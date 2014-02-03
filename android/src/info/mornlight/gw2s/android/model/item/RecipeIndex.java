package info.mornlight.gw2s.android.model.item;


public class RecipeIndex {
    protected int recipeId;
    protected String type;
    protected int outputItemId;
    protected int minRating;
    private Discipline discipline;

    transient private Rarity rarity;
    transient private String name;
    transient private int iconFileId;
    transient private String iconFileSignature;

    public RecipeIndex() {
    }

    public RecipeIndex(int recipeId, String type, int outputItemId, int minRating, int iconFileId, String iconFileSignature) {
        this.recipeId = recipeId;
        this.type = type;
        this.outputItemId = outputItemId;
        this.minRating = minRating;
        this.iconFileId = iconFileId;
        this.iconFileSignature = iconFileSignature;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getType() {
        return type;
    }

    public int getOutputItemId() {
        return outputItemId;
    }

    public int getMinRating() {
        return minRating;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public String getImage() {
        return String.format("https://render.guildwars2.com/file/%s/%d.png", iconFileSignature, iconFileId);
    }
}
