package info.mornlight.gw2s.android.model.map;

public class Continent {
    private String id;
    private String name;
    private int[] continentDims;
    private int minZoom;
    private int maxZoom;
    private int[] floors;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getContinentDims() {
        return continentDims;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public int[] getFloors() {
        return floors;
    }

    public void setId(String id) {
        this.id = id;
    }
}
