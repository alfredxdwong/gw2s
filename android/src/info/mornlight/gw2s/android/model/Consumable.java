package info.mornlight.gw2s.android.model;

/**
 * Created by alfred on 5/22/13.
 */
public class Consumable extends ItemBase {
    private ConsumableType type;
    private int durationMs;
    private String description;


    public Consumable() {
    }

    public ConsumableType getType() {
        return type;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public String getDescription() {
        return description;
    }
}
