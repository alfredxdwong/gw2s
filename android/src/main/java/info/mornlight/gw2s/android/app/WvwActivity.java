package info.mornlight.gw2s.android.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.wvw.WvwMatch;

/**
 * Created by alfred on 5/24/13.
 */
public class WvwActivity extends BaseActivity {
    private WvwFragment fragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wvw_activity);

        fragment = (WvwFragment) getFragmentManager().findFragmentById(R.id.fragment);

        ActionBar actionBar = getActionBar();
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
        getMenuInflater().inflate(R.menu.wvw_activity, menu);
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
