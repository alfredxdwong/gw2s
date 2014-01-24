package info.mornlight.gw2s.android.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import info.mornlight.gw2s.android.R;
import roboguice.inject.InjectFragment;

public class DynamicEventsActivity extends BaseActivity
{
    @InjectFragment(R.id.fragment)
    DynamicEventsFragment fragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_events_activity);

        requestAd();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.dynamic_events);

        if(App.instance().getActiveWorldId() == 0) {
            startActivityForResult(new Intent(this, SelectWorldActivity.class), RequestCodes.SELECT_WORLD);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.dynamic_events_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.m_select_world:
                startActivityForResult(new Intent(this, SelectWorldActivity.class), RequestCodes.SELECT_WORLD);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK)
            return;

        switch(requestCode) {
            case RequestCodes.SELECT_WORLD: {
                int worldId = data.getIntExtra(SelectWorldActivity.WORLD_ID, 0);
                if(worldId != 0) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(Prefs.ACTIVE_WORLD_ID, worldId);
                    editor.commit();

                    App.instance().setActiveWorldId(worldId);

                    fragment.clear();
                    fragment.refresh();
                }
            }
        }
    }
}
