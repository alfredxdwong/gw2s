package info.mornlight.gw2s.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.billing.InAppProducts;
import info.mornlight.gw2s.android.billing.PurchasingHelper;
import info.mornlight.gw2s.android.util.DefaultAsyncTask;
import info.mornlight.gw2s.android.util.ToastUtils;

import java.util.Set;

public class MainActivity extends BaseActivity
{
    private static final String TAG = "MainActivity";

    @InjectView(R.id.wvw)
    protected View wvw;

    @InjectView(R.id.items)
    protected View items;

    @InjectView(R.id.recipes)
    protected View recipes;

    @InjectView(R.id.map)
    protected View map;

    private PurchasingHelper purchasingHelper = new PurchasingHelper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);

        App.instance().loadSkuStates();

        wvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WvwActivity.class));
            }
        });

        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ItemsActivity.class));
            }
        });

        recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecipesActivity.class));
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        purchasingHelper.init(this, new PurchasingHelper.ServiceListener() {
            @Override
            public void onServiceConnected() {
                refreshSkuStates();
            }

            @Override
            public void onServiceDisconnected() {
            }
        });

        AppRater rater = new AppRater(getResources().getText(R.string.app_name).toString(), getPackageName());
        rater.appLaunched(this);
    }

    private void refreshSkuStates() {
        DefaultAsyncTask task = new DefaultAsyncTask<Void, Set<String>>(this, R.string.refresh_purchasing_error) {
            @Override
            public Set<String> call() throws Exception {
                return purchasingHelper.queryOwnedItems();
            }

            @Override
            protected void onSuccess(Set<String> skus) {
                App app = App.instance();
                app.updatePurchasedSkus(skus);
            }
        };

        task.execute();
    }

    /*private void checkCrawler() {
        ConnectionManager conMgr = new ConnectionManager(this);

        if(!conMgr.hasInternet()) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean onlyWithWifi = prefs.getBoolean(Prefs.UPDATE_DATABASE_ONLY_WITH_WIFI, true);

        if(!conMgr.isWifi() && onlyWithWifi) {
            return;
        }

        App.instance().startDatabaseCrawler();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getMenuInflater().inflate(R.menu.main_activity, optionMenu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem miRemoveAd = menu.findItem(R.id.m_removead);
        miRemoveAd.setVisible(App.instance().needPurchaseAdRemoval());
        miRemoveAd.setEnabled(App.instance().needPurchaseAdRemoval());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.m_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.m_removead:
                try {
                    purchasingHelper.purchaseItem(this, InAppProducts.AdRemoval);
                } catch (Exception e) {
                    Log.e(TAG, "Start purchase ad_removal error", e);
                    ToastUtils.show(this, R.string.unknown_error);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCodes.PURCHASE_SKU) {
                try {
                    purchasingHelper.processPurchaseResult(data);

                    //TODO send purchase info to google analytics
                    /*getTracker().send(
                    new HitBuilders.ItemBuilder()
                            .setTransactionId(getOrderId())
                            .setName(getItemName(1))
                            .setSku(getItemSku(1))
                            .setCategory(getItemCategory(1))
                            .setPrice(getItemPrice(getView(), 1))
                            .setQuantity(getItemQuantity(getView(), 1))
                            .setCurrencyCode("USD")
                            .build());)*/
                } catch (Exception e) {
                    ToastUtils.show(this, R.string.unknown_error);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        purchasingHelper.uninit();
    }
}
