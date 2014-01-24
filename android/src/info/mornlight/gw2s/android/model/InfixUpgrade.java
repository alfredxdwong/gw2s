package info.mornlight.gw2s.android.model;

/**
 * Created by alfred on 5/22/13.
 */
public class InfixUpgrade {
    Buff buff;
    AttributeModifier[] attributes;
    int suffixItemId;

    public InfixUpgrade() {
    }

    public InfixUpgrade(Buff buff, AttributeModifier[] attributes, int suffixItemId) {
        this.buff = buff;
        this.attributes = attributes;
        this.suffixItemId = suffixItemId;
    }

    public Buff getBuff() {
        return buff;
    }

    public AttributeModifier[] getAttributes() {
        return attributes;
    }

    public int getSuffixItemId() {
        return suffixItemId;
    }
}
