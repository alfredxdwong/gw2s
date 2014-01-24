package info.mornlight.gw2s.android.model;

public class EventDetails {
    private String eventId;
    private String name;
    private int level;
    private int mapId;
    private String[] flags;
    private Location location;

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getMapId() {
        return mapId;
    }

    public String[] getFlags() {
        return flags;
    }

    public Location getLocation() {
        return location;
    }
}
