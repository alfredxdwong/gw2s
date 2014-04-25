package info.mornlight.gw2s.android.model.event;

public class Event {
    private int worldId;
    private int mapId;
    private String eventId;
    private EventState state;

    private transient String eventName;
    private transient String mapName;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getMapName()
    {
        return mapName;
    }

    public void setMapName(String mapName)
    {
        this.mapName = mapName;
    }

    public Event() {
    }

    public Event(int worldId, int mapId, String eventId, EventState state) {
        this.worldId = worldId;
        this.mapId = mapId;
        this.eventId = eventId;
        this.state = state;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventState getState() {
        return state;
    }

    public void setState(EventState state) {
        this.state = state;
    }
}
