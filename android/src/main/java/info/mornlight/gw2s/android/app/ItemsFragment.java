package info.mornlight.gw2s.android.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Loader;
import android.view.*;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.db.Database;
import info.mornlight.gw2s.android.db.DbUtils;
import info.mornlight.gw2s.android.model.item.ItemIndex;
import info.mornlight.gw2s.android.model.item.ItemType;
import info.mornlight.gw2s.android.model.item.Rarity;
import info.mornlight.gw2s.android.model.item.WeaponType;
import info.mornlight.gw2s.android.ui.DialogResultListener;
import info.mornlight.gw2s.android.ui.ItemView;
import info.mornlight.gw2s.android.ui.RefreshableFragment;
import info.mornlight.gw2s.android.ui.ThrowableLoader;

/**
 * Created by alfred on 5/28/13.
 */

public class ItemsFragment extends RefreshableFragment<Cursor, ListView> implements DialogResultListener {
    private ItemType type = ItemType.Weapon;
    private Object subtype = WeaponType.Axe;
    private Rarity rarityFilter;
    private int minLevel = 1;
    private int maxLevel = 80;

    public void setFilter(ItemType type, Object subType) {
        this.type = type;
        this.subtype = subType;
    }

    @Override
    protected ListView onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.items_fragment, null);
        ItemsCursorAdapter adapter = new ItemsCursorAdapter(getActivity(), null);
        view.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.items_fragment, menu);
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
                        SelectLevelRangeDialogFragment.newInstance(RequestCodes.SELECT_LEVEL_RANGE, getString(R.string.select_level_range), minLevel, maxLevel, 1, 80, this);
                fragment.show(getFragmentManager(), "TAG_SELECT_LEVEL_RANGE");
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRarityFilter(Rarity rarity) {
        if(rarityFilter == rarity) {
            return;
        }

        rarityFilter = rarity;

        refresh();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsCursorAdapter adapter = (ItemsCursorAdapter) contentView.getAdapter();
                ItemIndex item = DbUtils.readItemIndex((Cursor) adapter.getItem(position));

                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                intent.putExtra("item_id", item.getItemId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(data != null && !data.isClosed()) {
            data.close();
            data = null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ThrowableLoader<Cursor>(getActivity(), null) {
            @Override
            public Cursor loadData() throws Exception {
                Database db = App.instance().getDatabase();
                Cursor c = db.queryItemIndex(null, rarityFilter, type, subtype, minLevel, maxLevel);
                return c;
            }
        };
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

    class ItemsCursorAdapter extends ResourceCursorAdapter {
        ItemsCursorAdapter(Context context, Cursor c) {
            super(context, R.layout.items_item_index_item, c, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ItemIndex item = DbUtils.readItemIndex(cursor);
            ItemIndexView v = (ItemIndexView) view.getTag();

            if(v == null) {
                v = new ItemIndexView(view);
                view.setTag(v);
            }

            v.update(item);

            /*ImageView image = (ImageView) view.findViewById(R.id.image);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView rarity = (TextView) view.findViewById(R.id.rarity);
            TextView type = (TextView) view.findViewById(R.id.type);
            TextView subtype = (TextView) view.findViewById(R.id.subtype);
            TextView level = (TextView) view.findViewById(R.id.level);

            int color = getResources().getColor(ViewHelper.getRarityColor(item.getRarity()));
            ImageLoader.getInstance().displayImage(item.getImage(), image);
            name.setTextColor(color);
            name.setText(item.getName());

            rarity.setTextColor(color);
            rarity.setText(ViewHelper.getRarityName(item.getRarity()));

            type.setText(ViewHelper.getItemTypeName(item.getType()));
            subtype.setText(ViewHelper.getItemSubtypeName(item.getSubtype()));

            level.setText(Integer.toString(item.getLevel()));*/
        }
    }

    class ItemIndexView extends ItemView<ItemIndex> {
        ImageView image;
        TextView name;
        TextView rarity;
        TextView type;
        TextView subtype;
        TextView level;

        public ItemIndexView(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            rarity = (TextView) view.findViewById(R.id.rarity);
            type = (TextView) view.findViewById(R.id.type);
            subtype = (TextView) view.findViewById(R.id.subtype);
            level = (TextView) view.findViewById(R.id.level);
        }

        @Override
        public void update(ItemIndex item) {
            super.update(item);

            int color = getResources().getColor(ViewHelper.getRarityColor(item.getRarity()));
            ImageLoader.getInstance().displayImage(item.getImage(), image);
            name.setTextColor(color);
            name.setText(item.getName());

            rarity.setTextColor(color);
            rarity.setText(ViewHelper.getRarityName(item.getRarity()));

            type.setText(ViewHelper.getItemTypeName(item.getType()));
            subtype.setText(ViewHelper.getItemSubtypeName(item.getSubtype()));

            level.setText(Integer.toString(item.getLevel()));
        }
    }

    /*class ItemIndexAdapter extends ItemListAdapter<ItemIndex, ItemIndexView> {
        public ItemIndexAdapter(LayoutInflater inflater) {
            super(R.layout.items_item_index_item, inflater);
        }

        @Override
        protected ItemIndexView createView(int position, View view) {
            return new ItemIndexView(view);
        }
    }*/

    @Override
    protected void onLoadSucceed(Loader<Cursor> loader, Cursor data) {
        this.data = data;
        CursorAdapter adapter = (CursorAdapter) contentView.getAdapter();
        adapter.changeCursor(data);
    }
}