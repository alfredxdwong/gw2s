package info.mornlight.gw2s.android.model.map;

public class PointOfInterest extends EntityWithCoord {
    private int poiId;
    private String name;
    private String type;
    private int floor;

    public int getPoiId() {
        return poiId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getFloor() {
        return floor;
    }
}
