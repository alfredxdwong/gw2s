package info.mornlight.gw2s.android.model.event;

import info.mornlight.gw2s.android.model.Point;
import info.mornlight.gw2s.android.model.Point3D;

public class Location {
    private String type;
    private Point3D center;
    private float radius;
    private int rotation;
    private Point zRange;
    private Point[] points;

    public String getType() {
        return type;
    }

    public Point3D getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public int getRotation() {
        return rotation;
    }

    public Point getzRange() {
        return zRange;
    }

    public Point[] getPoints() {
        return points;
    }
}
