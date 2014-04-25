package info.mornlight.gw2s.android.model.item;

public class Buff {
    private int skillId;
    private String description;

    public Buff() {
    }

    public Buff(int skillId, String description) {
        this.skillId = skillId;
        this.description = description;
    }

    public int getSkillId() {
        return skillId;
    }

    public String getDescription() {
        return description;
    }
}
