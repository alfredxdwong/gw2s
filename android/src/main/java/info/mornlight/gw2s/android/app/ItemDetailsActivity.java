package info.mornlight.gw2s.android.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import info.mornlight.gw2s.android.R;

public class ItemDetailsActivity extends BaseActivity
{
    ItemDetailsFragment fragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details_activity);

        fragment = (ItemDetailsFragment) getFragmentManager().findFragmentById(R.id.fragment);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.item_details);
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