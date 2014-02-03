package info.mornlight.gw2s.android.model.map;

public class Sector extends EntityWithCoord {
    private int sectorId;
    private String name;
    private int level;

    public int getSectorId() {
        return sectorId;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
