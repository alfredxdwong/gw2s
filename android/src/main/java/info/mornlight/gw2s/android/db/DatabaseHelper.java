package info.mornlight.gw2s.android.db;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
    static final int DB_VERSION = 17;
    static final String DB_NAME = "gw2s";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        //force the database to upgrade to latest version
        setForcedUpgrade(DB_VERSION);
    }
}