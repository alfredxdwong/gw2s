package info.mornlight.gw2s.android.model.map;

import java.util.Map;

public class RegionFloor {
    private String name;
    private Coord labelCoord;
    private Map<String, MapFloor> maps;

    public String getName() {
        return name;
    }

    public Coord getLabelCoord() {
        return labelCoord;
    }

    public Map<String, MapFloor> getMaps() {
        return maps;
    }
}
