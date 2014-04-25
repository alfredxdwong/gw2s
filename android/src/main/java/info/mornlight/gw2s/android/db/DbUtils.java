package info.mornlight.gw2s.android.db;

import android.database.Cursor;
import info.mornlight.gw2s.android.model.item.*;

import static info.mornlight.gw2s.android.db.Consts.*;

/**
 * Created by alfred on 6/4/13.
 */
public class DbUtils {
    public static ItemIndex readItemIndex(Cursor c) {
        int idIndex = c.getColumnIndex(CURSOR_ID);
        int nameIndex = c.getColumnIndex(COLUMN_NAME);
        int rarityIndex = c.getColumnIndex(COLUMN_RARITY);
        int typeIndex = c.getColumnIndex(COLUMN_TYPE);
        int subTypeIndex = c.getColumnIndex(COLUMN_SUBTYPE);
        int iconFileIdIndex = c.getColumnIndex(COLUMN_ICON_FILE_ID);
        int iconFileSignatureIndex = c.getColumnIndex(COLUMN_ICON_FILE_SIGNATURE);
        int levelIndex = c.getColumnIndex(COLUMN_LEVEL);


        int id = c.getInt(idIndex);
        String theName = c.getString(nameIndex);
        Rarity theRarity = Rarity.valueOf(c.getString(rarityIndex));
        ItemType theType = ItemType.valueOf(c.getString(typeIndex));
        String theSubtype = c.getString(subTypeIndex);
        int iconFileId = c.getInt(iconFileIdIndex);
        String iconFileSignature = c.getString(iconFileSignatureIndex);
        int level = c.getInt(levelIndex);

        ItemIndex item = new ItemIndex(id, theName, theRarity, theType, iconFileId, iconFileSignature);
        item.setSubtype(theSubtype);
        item.setLevel(level);

        return item;
    }

    public static RecipeIndex readRecipeIndex(Cursor c) {
        int id = c.getInt(c.getColumnIndex(CURSOR_ID));
        String type = c.getString(c.getColumnIndex(COLUMN_TYPE));
        int outputItemId = c.getInt(c.getColumnIndex("output_item_id"));
        int minRating = c.getInt(c.getColumnIndex("min_rating"));
        int iconFileIdIndex = c.getColumnIndex(COLUMN_ICON_FILE_ID);
        int iconFileSignatureIndex = c.getColumnIndex(COLUMN_ICON_FILE_SIGNATURE);

        String name = c.getString(c.getColumnIndex(COLUMN_NAME));
        Rarity rarity = Rarity.valueOf(c.getString(c.getColumnIndex(COLUMN_RARITY)));
        int iconFileId = c.getInt(iconFileIdIndex);
        String iconFileSignature = c.getString(iconFileSignatureIndex);
        Discipline discipline = Discipline.valueOf(c.getString(c.getColumnIndex("discipline")));

        RecipeIndex recipe = new RecipeIndex(id, type, outputItemId, minRating, iconFileId, iconFileSignature);
        recipe.setName(name);
        recipe.setRarity(rarity);
        recipe.setDiscipline(discipline);

        return recipe;
    }
}
