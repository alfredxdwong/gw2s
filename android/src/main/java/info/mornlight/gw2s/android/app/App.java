package info.mornlight.gw2s.android.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import info.mornlight.gw2s.android.billing.InAppProducts;
import info.mornlight.gw2s.android.billing.PurchasingHelper;
import info.mornlight.gw2s.android.client.ApiClient;
import info.mornlight.gw2s.android.db.Database;
import info.mornlight.gw2s.android.db.DatabaseHelper;
import info.mornlight.gw2s.android.model.IntName;
import info.mornlight.gw2s.android.model.StringName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by alfred on 5/22/13.
 */
public class App {
    private static App instance = new App();

    public static App instance() {
        return instance;
    }

    private Context context;
    private Database db;
    //private DatabaseCrawler crawler;
    private ApiClient client;
    private String lang = "en";
    private int activeWorldId;

    public int getActiveWorldId() {
        return activeWorldId;
    }

    public void setActiveWorldId(int activeWorldId) {
        this.activeWorldId = activeWorldId;
    }

    private Map<String, StringName> eventNames = new HashMap<String, StringName>();
    private Map<Integer, IntName> mapNames = new HashMap<Integer, IntName>();
    private Map<Integer, IntName> worldNames = new HashMap<Integer, IntName>();
    private Map<Integer, IntName> objectiveNames = new HashMap<Integer, IntName>();
    private Map<String, SkuState> skuStates = new ConcurrentHashMap<>();

    public App() {
        client = new ApiClient();
    }

    public void ensureEventNames() throws IOException {
        if(eventNames.isEmpty()) {
            List<StringName> stringNames = client.listEventNames(lang);
            for(StringName name : stringNames) {
                eventNames.put(name.getId(), name);
            }
        }
    }

    public void ensureMapNames() throws IOException {
        if(mapNames.isEmpty()) {
            List<IntName> intNames = client.listMapNames(lang);
            for(IntName name : intNames) {
                mapNames.put(name.getId(), name);
            }
        }
    }

    public void ensureObjectiveNames() throws IOException {
        if(objectiveNames.isEmpty()) {
            List<IntName> intNames = client.listObjectiveNames(lang);
            for(IntName name : intNames) {
                objectiveNames.put(name.getId(), name);
            }
        }
    }

    public void ensureWorldNames() throws IOException {
        if(worldNames.isEmpty()) {
            List<IntName> intNames = client.listWorldNames(lang);
            for(IntName name : intNames) {
                worldNames.put(name.getId(), name);
            }
        }
    }

    public void initialize(Context context) throws IOException {
        this.context = context;

        db = new Database(context);
        db.open();

        //crawler = new DatabaseCrawler(db, lang);

        loadPrefs();
        loadSkuStates();
    }

    public void loadSkuStates() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        skuStates.put(InAppProducts.AdRemoval,
                SkuState.valueOf(prefs.getString("sku." + InAppProducts.AdRemoval, SkuState.Unknown.name())));
    }

    private void loadPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        activeWorldId = prefs.getInt(Prefs.ACTIVE_WORLD_ID, 0);
    }

    public void uninitialize() {
        db.close();
    }

    /*public void startDatabaseCrawler() {
        crawler.start();
    }

    public void stopDatabaseCrawler() {
        crawler.stop();
    }*/

    public Map<String, StringName> getEventNames() {
        return eventNames;
    }

    public Map<Integer, IntName> getMapNames() {
        return mapNames;
    }

    public Map<Integer, IntName> getWorldNames() {
        return worldNames;
    }

    public Map<Integer, IntName> getObjectiveNames() {
        return objectiveNames;
    }

    public ApiClient getClient() {
        return client;
    }

    public Database getDatabase() {
        return db;
    }

    public boolean needPurchaseAdRemoval() {
        return getSkuState(InAppProducts.AdRemoval) == SkuState.NotPurchased;
    }

    public SkuState getSkuState(String sku) {
        return skuStates.get(sku);
    }

    public void updatePurchasedSkus(Set<String> skus) {
        //first mark all items in skuStates as no purchased
        for(Map.Entry<String, SkuState> entry : skuStates.entrySet()) {
            entry.setValue(SkuState.NotPurchased);
        }

        //then update the map
        for (String sku : skus) {
            skuStates.put(sku, SkuState.Purchased);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        for(Map.Entry<String, SkuState> entry : skuStates.entrySet()) {
            editor.putString("sku." + entry.getKey(), entry.getValue().toString());
        }
        editor.commit();
    }

    public void putSkuState(String sku, SkuState state) {
        skuStates.put(sku, state);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("sku." + sku, state.toString());
        editor.commit();

    }
}
