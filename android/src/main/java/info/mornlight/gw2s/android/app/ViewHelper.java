package info.mornlight.gw2s.android.app;

import android.content.Context;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.item.*;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by alfred on 5/29/13.
 */
public class ViewHelper {
    public static String formatAttributeModifier(AttributeModifier attr, Context ctx) {
        return "+" + attr.getModifier() + " " + toString(attr.getAttribute(), ctx);
    }

    private static String toString(Attribute attribute, Context ctx) {
        int resId = R.string.empty;
        switch (attribute) {
            case Power: resId = R.string.attribute_power; break;
            case Toughness: resId = R.string.attribute_toughness; break;
            case Vitality: resId = R.string.attribute_vitality; break;
            case Precision: resId = R.string.attribute_precision; break;
            case CritDamage: resId = R.string.attribute_critical_damage; break;
            case ConditionDamage: resId = R.string.attribute_condition_damage; break;
            case Healing: resId = R.string.attribute_healing; break;
        }

        return ctx.getString(resId);
    }

    public static String toString(WeaponType type) {
        return type.toString();
    }

    public static String formatWeaponStrength(int minPower, int maxPower) {
        return minPower + " - " + maxPower;
    }

    public static String formatDamageType(DamageType type) {
        return type.toString();
    }

    public static String toString(ArmorType type) {
        return type.toString();
    }

    public static String toString(WeightClass weightClass) {
        return weightClass.toString();
    }

    public static String formatType(ItemType type) {
        return type.toString();
    }

    public static String formatSubtype(Object subtype) {
        if (subtype == null)
            return "";

        return subtype.toString();
    }

    public static String getDisciplineTypeName(Discipline discipline) {
        return discipline.toString();
    }

    public static class Price {
        public int gold;
        public int silver;
        public int copper;

        public Price(int gold, int silver, int copper) {
            this.gold = gold;
            this.silver = silver;
            this.copper = copper;
        }
    }

    static int getRarityColor(Rarity rarity) {
        switch (rarity) {
            case Basic:
                return R.color.rarity_basic;
            case Fine:
                return R.color.rarity_fine;
            case Ascended:
                return R.color.rarity_ascended;
            case Exotic:
                return R.color.rarity_exotic;
            case Junk:
                return R.color.rarity_junk;
            case Legendary:
                return R.color.rarity_legendary;
            case Masterwork:
                return R.color.rarity_masterwork;
            case Rare:
                return R.color.rarity_rare;
            default:
                return R.color.rarity_basic;
        }
    }

    public static String getRarityName(Rarity rarity) {
        return rarity.toString();
    }

    public static String getItemTypeName(ItemType type) {
        return type.toString();
    }

    public static String getItemSubtypeName(Object subtype) {
        if (subtype == null) return "";

        return subtype.toString();
    }

    public static Price formatPrice(int value) {
        int copper = value % 100;
        int silver = value / 100 % 100;
        int gold = silver / 100;
        return new Price(gold, silver, copper);
    }

    public static String makeString(Object[] items) {
        return StringUtils.join(items, " ");
    }

    public static String makeString(Object[] items, String separator) {
        return StringUtils.join(items, separator);
    }

    public static String getString(ItemFlag flag, Context ctx) {
        int resId = R.string.empty;
        switch (flag) {
            case HideSuffix:
                resId = R.string.item_flag_hide_suffix;
                break;
            case NoSell:
                resId = R.string.item_flag_no_sell;
                break;
            case AccountBound:
                resId = R.string.item_flag_account_bound;
                break;
            case SoulBindOnUse:
                resId = R.string.item_flag_soul_bind_on_use;
                break;
            case NoSalvage:
                resId = R.string.item_flag_no_salvage;
                break;
            case SoulbindOnAcquire:
                resId = R.string.item_flag_soul_bind_on_acquire;
                break;
            case NotUpgradeable:
                resId = R.string.item_flag_not_upgradeable;
                break;
            case NoMysticForge:
                resId = R.string.item_flag_no_mystic_forge;
                break;
            case NoUnderwater:
                resId = R.string.item_flag_no_underwater;
                break;
            case Unique:
                resId = R.string.item_flag_unique;
                break;
        }

        return ctx.getString(resId);
    }

    public static String makeString(ItemFlag[] flags, final Context ctx) {
        List<String> names = CollectionUtils.transform(Arrays.asList(flags), new Transformer() {
            @Override
            public Object transform(Object o) {
                return getString((ItemFlag)o, ctx);
            }
        });
        return StringUtils.join(names, "  ");
    }

    public static String makeString(Discipline[] disciplines, final Context ctx) {
        List<String> names = CollectionUtils.transform(Arrays.asList(disciplines), new Transformer() {
            @Override
            public Object transform(Object o) {
                return o.toString();
            }
        });
        return StringUtils.join(names, "  ");
    }
}
