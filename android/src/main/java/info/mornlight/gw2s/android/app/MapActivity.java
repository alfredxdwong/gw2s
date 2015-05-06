package info.mornlight.gw2s.android.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.map.Continent;
import info.mornlight.gw2s.android.ui.ItemListAdapter;
import info.mornlight.gw2s.android.ui.ItemView;
import info.mornlight.gw2s.android.ui.ThrowableLoader;

import java.util.List;

public class MapActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Continent>>, ActionBar.OnNavigationListener {
    private MapFragment fragment;

    private ContinentAdapter adapter;

    class ContinentItemView extends ItemView<Continent> {
        private TextView tvName;

        public ContinentItemView(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void update(Continent item) {
            super.update(item);

            tvName.setText(item.getName());
        }
    }

    class ContinentAdapter extends ItemListAdapter<Continent, ContinentItemView> {
        public ContinentAdapter(LayoutInflater inflater) {
            super(R.layout.continent_item, inflater);
        }

        @Override
        protected ContinentItemView createView(int position, View view) {
            return new ContinentItemView(view);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.map);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_activity, menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public Loader<List<Continent>> onCreateLoader(int id, Bundle args) {
        return new ThrowableLoader<List<Continent>>(this, null) {
            @Override
            public List<Continent> loadData() throws Exception {
                App app = App.instance();
                return app.getClient().listContinents();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Continent>> listLoader, List<Continent> continents) {
        if(continents == null) {
            return;
        }

        ActionBar actionBar = getActionBar();
        adapter = new ContinentAdapter(getLayoutInflater());
        adapter.setItems(continents);
        actionBar.setListNavigationCallbacks(adapter, this);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Continent>> listLoader) {

    }

    @Override
    public boolean onNavigationItemSelected(int index, long l) {
        Continent continent = adapter.getItem(index);
        fragment.updateView(continent);

        return true;
    }
}
