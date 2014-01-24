package info.mornlight.gw2s.android.model;

import java.io.Serializable;

/**
 * Created by alfred on 5/22/13.
 */
public class StringName implements Serializable {
    String id;
    String name;

    public StringName() {
    }

    public StringName(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
