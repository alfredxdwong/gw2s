package info.mornlight.gw2s.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.mornlight.gw2s.android.model.*;
import info.mornlight.gw2s.android.model.item.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static info.mornlight.gw2s.android.db.Consts.*;

/**
 * Created by alfred on 5/28/13.
 */
public class Database {
    private ObjectMapper mapper;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public Database(Context context) {
        helper = new DatabaseHelper(context);
        mapper = Json.getMapper();
    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
        db = null;
    }

    public List<Integer> getItemIds() {
        Cursor c = db.query(TABLE_ITEMS, new String[] { COLUMN_ID }, null, null, null, null, null);

        List<Integer> ids = new ArrayList<Integer>();

        int idIndex = c.getColumnIndex(COLUMN_ID);
        while (c.moveToNext()) {
            int id = c.getInt(idIndex);
            ids.add(id);
        }
        c.close();

        return ids;
    }

    public void insertItem(ItemDetails item) throws JsonProcessingException {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, item.getItemId());
        cv.put(COLUMN_NAME, item.getName());
        cv.put(COLUMN_RARITY, item.getRarity().toString());
        cv.put(COLUMN_TYPE, item.getType().toString());

        Object subType = item.getSubtype();
        cv.put(COLUMN_SUBTYPE, subType == null ? null : subType.toString());

        String json = mapper.writeValueAsString(item);
        cv.put(COLUMN_JSON, json);

        db.insertWithOnConflict(TABLE_ITEMS, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Cursor queryItemIndex(String name, Rarity rarity, ItemType type, Object subType, int minLevel, int maxLevel) {
        String where = "";
        ArrayList<String> params = new ArrayList<String>();
        if(name != null) {
            where += COLUMN_NAME + "LIKE ?";
            params.add(name);
        }

        if(rarity != null) {
            if(!where.isEmpty()) where += " AND ";
            where += COLUMN_RARITY + "=?";
            params.add(rarity.toString());
        }

        if(type != null) {
            if(!where.isEmpty()) where += " AND ";
            where += COLUMN_TYPE + "=?";
            params.add(type.toString());
        }

        if(subType != null) {
            if(!where.isEmpty()) where += " AND ";
            where += COLUMN_SUBTYPE + "=?";
            params.add(subType.toString());
        }

        where += " AND ((" + COLUMN_LEVEL + ">=? AND " + COLUMN_LEVEL + "<=?) OR " + COLUMN_LEVEL + "=0)";
        params.add(Integer.toString(minLevel));
        params.add(Integer.toString(maxLevel));

        List<ItemIndex> items = new ArrayList<ItemIndex>();

        String sql = String.format("select %s as %s, %s, %s, %s, %s, %s, %s, %s from gw2s_items where %s order by %s asc",
                COLUMN_ID, CURSOR_ID, COLUMN_NAME, COLUMN_RARITY, COLUMN_TYPE, COLUMN_SUBTYPE, COLUMN_ICON_FILE_ID, COLUMN_ICON_FILE_SIGNATURE, COLUMN_LEVEL, where, COLUMN_NAME);
        Cursor c = db.rawQuery(sql, params.toArray(new String[params.size()]));

        return c;
    }

    public Cursor queryRecipeIndex(String name, Rarity rarity, Discipline discipline, String type, int minLevel, int maxLevel) {
        String where = "";
        ArrayList<String> params = new ArrayList<String>();
        if(name != null) {
            where += "gw2s_recipes.name LIKE ?";
            params.add(name);
        }

        if(rarity != null) {
            if(!where.isEmpty()) where += " AND ";
            where += COLUMN_RARITY + "=?";
            params.add(rarity.toString());
        }

        if(discipline != null) {
            if(!where.isEmpty()) where += " AND ";
            where += "gw2s_recipes_disciplines.discipline=?";
            params.add(discipline.toString());
        }

        if(type != null) {
            if(!where.isEmpty()) where += " AND ";
            where += "gw2s_recipes.type=?";
            params.add(type.toString());
        }

        where += " AND (gw2s_recipes.min_rating >= ? AND gw2s_recipes.min_rating <=?)";
        params.add(Integer.toString(minLevel));
        params.add(Integer.toString(maxLevel));

        List<ItemIndex> items = new ArrayList<ItemIndex>();

        //select gw2s_recipes.id as _id, gw2s_items.name, gw2s_recipes.type, gw2s_recipes.min_rating from gw2s_items join gw2s_recipes on gw2s_recipes.output_item_id=gw2s_items.id join gw2s_recipes_disciplines on gw2s_recipes_disciplines.id=gw2s_recipes.id where gw2s_recipes_disciplines.discipline='Chef' order by gw2s_items.name asc

        String sql = String.format("select gw2s_recipes.id as _id, gw2s_items.name, gw2s_items.icon_file_id, gw2s_items.icon_file_signature, gw2s_items.rarity, gw2s_recipes.type, gw2s_recipes.min_rating, gw2s_recipes.output_item_id, discipline from gw2s_items"
                + " join gw2s_recipes on gw2s_recipes.output_item_id = gw2s_items.id"
                + " join gw2s_recipes_disciplines on gw2s_recipes_disciplines.id=gw2s_recipes.id"
                + " where %s order by gw2s_items.name asc",
                where);
        Cursor c = db.rawQuery(sql, params.toArray(new String[params.size()]));

        return c;
    }

    public ItemDetails readItemDetails(int id) throws IOException {
        Cursor c = db.query(TABLE_ITEMS, new String[] { COLUMN_JSON }, COLUMN_ID + "=?", new String[] { Integer.toString(id) }, null, null, null);
        try {
            if(c.getCount() != 1)
                return null;

            c.moveToFirst();
            String json = c.getString(c.getColumnIndex(COLUMN_JSON));
            ItemDetails item = mapper.readValue(json, ItemDetails.class);
            return item;
        } finally {
            c.close();
        }
    }

    public RecipeDetails readRecipeDetails(int id) throws IOException {
        Cursor c = db.query(TABLE_RECIPES, new String[] { COLUMN_JSON }, COLUMN_ID + "=?", new String[] { Integer.toString(id) }, null, null, null);
        try {
            if(c.getCount() != 1)
                return null;

            c.moveToFirst();
            String json = c.getString(c.getColumnIndex(COLUMN_JSON));
            RecipeDetails item = mapper.readValue(json, RecipeDetails.class);
            return item;
        } finally {
            c.close();
        }
    }

    public RecipeDetails readRecipeDetailsForItem(int itemId) throws IOException {
        Cursor c = db.query(TABLE_RECIPES, new String[] { COLUMN_JSON }, COLUMN_OUTPUT_ITEM_ID + "=?", new String[] { Integer.toString(itemId) }, null, null, null);
        try {
            if(c.getCount() != 1)
                return null;

            c.moveToFirst();
            String json = c.getString(c.getColumnIndex(COLUMN_JSON));
            RecipeDetails item = mapper.readValue(json, RecipeDetails.class);
            return item;
        } finally {
            c.close();
        }
    }

    public ItemIndex readItemIndex(int itemId) {
        String where = "id=?";
        String sql = String.format("select %s as %s, %s, %s, %s, %s, %s, %s, %s from gw2s_items where %s",
                COLUMN_ID, CURSOR_ID, COLUMN_NAME, COLUMN_RARITY, COLUMN_TYPE, COLUMN_SUBTYPE, COLUMN_ICON_FILE_ID, COLUMN_ICON_FILE_SIGNATURE, COLUMN_LEVEL, where);
        Cursor c = db.rawQuery(sql, new String[] { Integer.toString(itemId) });
        try {
            if(c.getCount() != 1)
                return null;

            c.moveToFirst();
            ItemIndex item = DbUtils.readItemIndex(c);
            return item;
        } finally {
            c.close();
        }
    }
}
