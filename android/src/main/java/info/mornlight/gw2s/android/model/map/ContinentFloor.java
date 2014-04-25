package info.mornlight.gw2s.android.model.map;

import java.util.Map;

public class ContinentFloor {
    private int[] textureDims;
    private Map<String, RegionFloor> regions;

    public int[] getTextureDims() {
        return textureDims;
    }

    public Map<String, RegionFloor> getRegions() {
        return regions;
    }
}
