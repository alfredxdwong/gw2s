package info.mornlight.gw2s.android.db;

/**
 * Created by alfred on 5/28/13.
 */
public interface Consts {
    String TABLE_ITEMS = "gw2s_items";
    String TABLE_RECIPES = "gw2s_recipes";
    String TABLE_RECIPES_DISCIPLINES = "gw2s_recipes_disciplines";
    String TABLE_RECIPES_INGREDIENTS = "gw2s_recipes_ingredients";


    String CURSOR_ID = "_id";
    String COLUMN_ID = "id";
    String COLUMN_TYPE = "type";
    String COLUMN_SUBTYPE = "subtype";
    String COLUMN_NAME = "name";
    String COLUMN_RARITY = "rarity";
    String COLUMN_LEVEL = "level";
    String COLUMN_JSON = "json";
    String COLUMN_ICON_FILE_ID = "icon_file_id";
    String COLUMN_ICON_FILE_SIGNATURE = "icon_file_signature";

    String COLUMN_OUTPUT_ITEM_ID = "output_item_id";
}
