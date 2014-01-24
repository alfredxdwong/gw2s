package info.mornlight.gw2s.android.db;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/*public class DatabaseHelper extends SQLiteOpenHelper {
    static final int DB_VERSION = 1;
    static final String DB_NAME = "GW2SDB";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try {
            //create items table
            String sql = String.format("CREATE TABLE %1$s (%2$s INT PRIMARY KEY, %3$s TEXT, %4$s TEXT, %5$s TEXT, %6$s TEXT, %7$s TEXT)",
                    TABLE_ITEMS,
                    COLUMN_ID, COLUMN_NAME, COLUMN_RARITY, COLUMN_TYPE, COLUMN_SUBTYPE, COLUMN_JSON);
            db.execSQL(sql);

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion) {
		// TODO Auto-generated method stub
	}
}*/
public class DatabaseHelper extends SQLiteAssetHelper {
    static final int DB_VERSION = 8;
    static final String DB_NAME = "gw2s";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        //force the database to upgrade to latest version
        setForcedUpgradeVersion(DB_VERSION);
    }
}