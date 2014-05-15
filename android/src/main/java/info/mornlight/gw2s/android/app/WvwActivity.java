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
import info.mornlight.gw2s.android.model.wvw.WvwMatch;
import roboguice.inject.InjectFragment;

/**
 * Created by alfred on 5/24/13.
 */
public class WvwActivity extends BaseActivity {
    @InjectFragment(R.id.fragment)
    WvwFragment fragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wvw_activity);

        updateAd();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.world_vs_world);

        fragment.setListener(new WvwFragment.OnWvwMatchSelectedListener() {
            @Override
            public void onMatchSelected(WvwMatch match) {
                Intent intent = new Intent(WvwActivity.this, WvwDetailsActivity.class);
                intent.putExtra("wvw_match_id", match.getWvwMatchId());
                intent.putExtra("red_world_id", match.getRedWorldId());
                intent.putExtra("blue_world_id", match.getBlueWorldId());
                intent.putExtra("green_world_id", match.getGreenWorldId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.wvw_activity, menu);
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

                    fragment.refresh();
                }
            }
        }
    }
}
