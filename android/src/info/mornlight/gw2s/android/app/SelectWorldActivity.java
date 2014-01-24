package info.mornlight.gw2s.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import info.mornlight.gw2s.android.R;
import roboguice.inject.InjectFragment;

public class SelectWorldActivity extends BaseActivity {
    static public final String WORLD_ID = "world_id";

    @InjectFragment(R.id.fragment)
    private SelectWorldFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_world_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.select_world);

        fragment.setOnWorldSelectedListener(new SelectWorldFragment.OnWorldSelectedListener() {
            @Override
            public void onWorldSelected(int worldId) {
                Intent intent = new Intent();
                intent.putExtra(WORLD_ID, worldId);
                setResult(Activity.RESULT_OK, intent);

                finish();
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
