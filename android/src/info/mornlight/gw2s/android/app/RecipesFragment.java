package info.mornlight.gw2s.android.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.db.Database;
import info.mornlight.gw2s.android.db.DbUtils;
import info.mornlight.gw2s.android.model.item.Discipline;
import info.mornlight.gw2s.android.model.item.Rarity;
import info.mornlight.gw2s.android.model.item.RecipeIndex;
import info.mornlight.gw2s.android.ui.DialogResultListener;
import info.mornlight.gw2s.android.ui.ItemView;
import info.mornlight.gw2s.android.ui.RefreshableFragment;
import info.mornlight.gw2s.android.ui.ThrowableLoader;

/**
 * Created by alfred on 5/28/13.
 */

public class RecipesFragment extends RefreshableFragment<Cursor, ListView> implements DialogResultListener {
    private Discipline discipline = Discipline.Armorsmith;
    private String type = null;

    private Rarity rarityFilter;
    private int minLevel = 1;
    private int maxLevel = 400;

    public void setFilter(Discipline discipline, String type) {
        this.discipline = discipline;
        this.type = type;
    }

    @Override
    protected ListView onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        ListView view =  (ListView) inflater.inflate(R.layout.recipes_fragment, null);
        RecipesCursorAdapter adapter = new RecipesCursorAdapter(getActivity(), null);
        view.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.recipes_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_rarity: {
                SelectRarityDialogFragment fragment =
                        SelectRarityDialogFragment.newInstance(RequestCodes.SELECT_RARITY, getString(R.string.select_rarity), rarityFilter, this);
                fragment.show(getFragmentManager(), "TAG_SELECT_RARITY");
            }
            break;
            case R.id.m_level: {
                SelectLevelRangeDialogFragment fragment =
                        SelectLevelRangeDialogFragment.newInstance(RequestCodes.SELECT_LEVEL_RANGE, getString(R.string.select_level_range), minLevel, maxLevel, 1, 400, this);
                fragment.show(getFragmentManager(), "TAG_SELECT_LEVEL_RANGE");
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipesCursorAdapter adapter = (RecipesCursorAdapter) contentView.getAdapter();
                RecipeIndex recipe = DbUtils.readRecipeIndex((Cursor) adapter.getItem(position));

                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                intent.putExtra("item_id", recipe.getOutputItemId());
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ThrowableLoader<Cursor>(getActivity(), null) {
            @Override
            public Cursor loadData() throws Exception {
                Database db = App.instance().getDatabase();
                Cursor c = db.queryRecipeIndex(null, rarityFilter, discipline, type, minLevel, maxLevel);
                return c;
            }
        };
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(data != null && !data.isClosed()) {
            data.close();
            data = null;
        }
    }

    @Override
    public void onDialogResult(int requestCode, int resultCode, Bundle args) {
        if(resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case RequestCodes.SELECT_RARITY:
                rarityFilter = SelectRarityDialogFragment.getSelected(args);
                refresh();
                break;
            case RequestCodes.SELECT_LEVEL_RANGE:
                minLevel = args.getInt(SelectLevelRangeDialogFragment.ARG_MIN);
                maxLevel = args.getInt(SelectLevelRangeDialogFragment.ARG_MAX);
                refresh();
                break;
        }
    }

    class RecipesCursorAdapter extends ResourceCursorAdapter {
        RecipesCursorAdapter(Context context, Cursor c) {
            super(context, R.layout.recipes_recipe_index_item, c, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            RecipeIndex item = DbUtils.readRecipeIndex(cursor);
            RecipeIndexView v = (RecipeIndexView) view.getTag();

            if(v == null) {
                v = new RecipeIndexView(view);
                view.setTag(v);
            }

            v.update(item);
        }
    }

    class RecipeIndexView extends ItemView<RecipeIndex> {
        ImageView image;
        TextView name;
        TextView type;
        TextView discipline;
        TextView rating;
        TextView rarity;

        public RecipeIndexView(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            rarity = (TextView) view.findViewById(R.id.rarity);
            discipline = (TextView) view.findViewById(R.id.discipline);
            type = (TextView) view.findViewById(R.id.type);
            rating = (TextView) view.findViewById(R.id.min_rating);
        }

        @Override
        public void update(RecipeIndex item) {
            super.update(item);

            int color = getResources().getColor(ViewHelper.getRarityColor(item.getRarity()));
            ImageLoader.getInstance().displayImage(item.getImage(), image);
            name.setTextColor(color);
            name.setText(item.getName());

            rarity.setTextColor(color);
            rarity.setText(ViewHelper.getRarityName(item.getRarity()));

            discipline.setText(ViewHelper.getDisciplineTypeName(item.getDiscipline()));
            type.setText(ViewHelper.getItemSubtypeName(item.getType()));

            rating.setText(Integer.toString(item.getMinRating()));
        }
    }

    @Override
    protected void onLoadSucceed(Loader<Cursor> loader, Cursor data) {
        this.data = data;
        CursorAdapter adapter = (CursorAdapter) contentView.getAdapter();
        adapter.changeCursor(data);
    }
}