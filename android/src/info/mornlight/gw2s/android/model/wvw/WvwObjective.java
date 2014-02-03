package info.mornlight.gw2s.android.model.wvw;

/**
 * Created by alfred on 5/26/13.
 */
public class WvwObjective {
    int id;
    WvwOwner owner;
    String ownerGuild;

    public WvwObjective() {
    }

    public WvwObjective(int id, WvwOwner owner, String ownerGuild) {
        this.id = id;
        this.owner = owner;
        this.ownerGuild = ownerGuild;
    }

    public int getId() {
        return id;
    }

    public WvwOwner getOwner() {
        return owner;
    }

    public String getOwnerGuild() {
        return ownerGuild;
    }
}
