package info.mornlight.gw2s.android.model.map;

public class MapFloor {
    private String name;
    private int minLevel;
    private int maxLevel;
    private int defaultFloor;
    private Rect mapRect;
    private Rect continentRect;
    private PointOfInterest[] pointsOfInterest;
    private Task[] tasks;
    private SkillChallenge[] skillChallenges;
    private Sector[] sectors;

    public String getName() {
        return name;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getDefaultFloor() {
        return defaultFloor;
    }

    public Rect getMapRect() {
        return mapRect;
    }

    public Rect getContinentRect() {
        return continentRect;
    }

    public PointOfInterest[] getPointsOfInterest() {
        return pointsOfInterest;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public SkillChallenge[] getSkillChallenges() {
        return skillChallenges;
    }

    public Sector[] getSectors() {
        return sectors;
    }
}
