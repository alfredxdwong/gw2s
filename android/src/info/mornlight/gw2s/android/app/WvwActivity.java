package info.mornlight.gw2s.android.app;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
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

        requestAd();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
