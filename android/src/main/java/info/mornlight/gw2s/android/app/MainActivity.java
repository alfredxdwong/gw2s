package info.mornlight.gw2s.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import info.mornlight.gw2s.android.R;
import roboguice.inject.InjectView;

public class MainActivity extends BaseActivity
{
    @InjectView(R.id.dynamic_events)
    private View dynamicEvent;

    @InjectView(R.id.wvw)
    private View wvw;

    @InjectView(R.id.items)
    private View items;

    @InjectView(R.id.recipes)
    private View recipes;

    @InjectView(R.id.map)
    private View map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        requestAd();

        dynamicEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DynamicEventsActivity.class));
            }
        });

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
        getSupportMenuInflater().inflate(R.menu.main_activity, optionMenu);

        return true;
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
        }

        return super.onOptionsItemSelected(item);
    }
}
