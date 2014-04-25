package info.mornlight.gw2s.android.model.map;

public class Task extends EntityWithCoord {
    private int taskId;
    private String objective;
    private int level;

    public int getTaskId() {
        return taskId;
    }

    public String getObjective() {
        return objective;
    }

    public int getLevel() {
        return level;
    }
}
