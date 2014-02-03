package info.mornlight.gw2s.android.model.item;

public class RecipeDetails extends RecipeIndex {
    private int outputItemCount;
    private int timeToCraftMs;
    private Ingredient[] ingredients;
    private Discipline[] disciplines;
    private String[] flags;

    public RecipeDetails() {
    }

    public int getOutputItemCount() {
        return outputItemCount;
    }

    public int getTimeToCraftMs() {
        return timeToCraftMs;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public Discipline[] getDisciplines() {
        return disciplines;
    }

    public String[] getFlags() {
        return flags;
    }
}
