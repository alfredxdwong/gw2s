package info.mornlight.gw2s.android.model;

/**
 * Created by alfred on 5/22/13.
 */
public class AttributeModifier {
    private Attribute attribute;
    private int modifier;

    public AttributeModifier() {
    }

    public AttributeModifier(Attribute attribute, int modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public int getModifier() {
        return modifier;
    }
}
