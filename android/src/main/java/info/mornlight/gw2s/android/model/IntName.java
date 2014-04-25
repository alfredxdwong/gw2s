package info.mornlight.gw2s.android.model;

import java.io.Serializable;

/**
 * Created by alfred on 5/22/13.
 */
public class IntName implements Serializable {
    int id;
    String name;

    public IntName() {
    }

    public IntName(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
