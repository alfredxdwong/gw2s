package info.mornlight.gw2s.android.app;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import info.mornlight.gw2s.android.R;

public class WvwDetailsActivity extends BaseActivity {
    private WvwDetailsFragment fragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wvw_details_activity);

        fragment = (WvwDetailsFragment) getFragmentManager().findFragmentById(R.id.fragment);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.world_vs_world);

        Intent intent = getIntent();
        String matchId = intent.getStringExtra("wvw_match_id");
        int redWorldId = intent.getIntExtra("red_world_id", 0);
        int blueWorldId = intent.getIntExtra("blue_world_id", 0);
        int greenWorldId = intent.getIntExtra("green_world_id", 0);
        fragment.update(matchId, redWorldId, blueWorldId, greenWorldId);
        fragment.refresh();
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
