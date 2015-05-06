package info.mornlight.gw2s.android.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.item.*;
import info.mornlight.gw2s.android.ui.ItemListAdapter;
import info.mornlight.gw2s.android.ui.ItemView;

import java.util.Arrays;

public class ItemsActivity extends BaseActivity {
    protected ItemsFragment fragment;

    @InjectView(R.id.menu)
    protected ListView menu;

    private ItemsMenuAdapter adapter;

    @InjectView(R.id.drawer)
    protected DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_activity);

        fragment = (ItemsFragment) getFragmentManager().findFragmentById(R.id.fragment);
        ButterKnife.inject(this);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.items);

        initializeSlidingMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.m_menu:
                if(drawer.isDrawerOpen(menu)) {
                    drawer.closeDrawer(menu);
                } else {
                    drawer.openDrawer(menu);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class ItemsMenuItem {
        ItemType type;
        Object subType;
        String text;

        ItemsMenuItem(ItemType type, String text) {
            this.type = type;
            this.text = text;
        }

        ItemsMenuItem(ItemType type, Object subType, String text) {
            this.type = type;
            this.text = text;
            this.subType = subType;
        }
    }

    class ItemsMenuView extends ItemView<ItemsMenuItem> {
        private TextView text;

        public ItemsMenuView(View view) {
            super(view);

            text = (TextView) view.findViewById(R.id.text);
        }

        @Override
        public void update(ItemsMenuItem item) {
            super.update(item);

            text.setText(item.text);
        }
    }

    class ItemsMenuAdapter extends ItemListAdapter<ItemsMenuItem, ItemsMenuView> {
        public ItemsMenuAdapter(LayoutInflater inflater) {
            super(0, inflater);
        }

        @Override
        protected ItemsMenuView createView(int position, View view) {
            return new ItemsMenuView(view);
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            @SuppressWarnings("unchecked")
            ItemsMenuView view = convertView != null ? (ItemsMenuView) convertView.getTag() : null;
            ItemsMenuItem item = getItem(position);
            if (view == null || view.getItem().subType != item.subType) {
                if(item.subType == null) {
                    convertView = inflater.inflate(R.layout.slidingmenu_group, null);
                } else {
                    convertView = inflater.inflate(R.layout.slidingmenu_item, null);
                }

                view = createView(position, convertView);
                convertView.setTag(view);
            }
            update(position, view, getItem(position));
            return convertView;
        }
    }

    private void initializeSlidingMenu() {
        //ListView menuView = (ListView) getLayoutInflater().inflate(R.layout.items_activity_sliding_menu, null);
        ItemsMenuItem[] menuItems = new ItemsMenuItem[] {
            new ItemsMenuItem(ItemType.Weapon, getString(R.string.weapon)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Axe, getString(R.string.axe)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Dagger, getString(R.string.dagger)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Focus, getString(R.string.focus)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Greatsword, getString(R.string.great_sword)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Hammer, getString(R.string.hammer)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Harpoon, getString(R.string.harpoon)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.LongBow, getString(R.string.longbow)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Mace, getString(R.string.mace)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Pistol, getString(R.string.pistol)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Rifle, getString(R.string.rifle)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Scepter, getString(R.string.scepter)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Shield, getString(R.string.shield)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.ShortBow, getString(R.string.short_bow)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Speargun, getString(R.string.spear_gun)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Staff, getString(R.string.staff)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Sword, getString(R.string.sword)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Torch, getString(R.string.torch)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Toy, getString(R.string.toy)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Trident, getString(R.string.trident)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.TwoHandedToy, getString(R.string.two_handed_toy)),
                new ItemsMenuItem(ItemType.Weapon, WeaponType.Warhorn, getString(R.string.warhorn)),
            new ItemsMenuItem(ItemType.Armor, getString(R.string.armor)),
                new ItemsMenuItem(ItemType.Armor, ArmorType.Boots, getString(R.string.boots)),
                new ItemsMenuItem(ItemType.Armor, ArmorType.Coat, getString(R.string.coat)),
                new ItemsMenuItem(ItemType.Armor, ArmorType.Gloves, getString(R.string.gloves)),
                new ItemsMenuItem(ItemType.Armor, ArmorType.Helm, getString(R.string.helm)),
                new ItemsMenuItem(ItemType.Armor, ArmorType.HelmAquatic, getString(R.string.helm_aquatic)),
                new ItemsMenuItem(ItemType.Armor, ArmorType.Leggings, getString(R.string.leggings)),
                new ItemsMenuItem(ItemType.Armor, ArmorType.Shoulders, getString(R.string.shoulders)),
            new ItemsMenuItem(ItemType.Bag, getString(R.string.bag)),
            new ItemsMenuItem(ItemType.Back, getString(R.string.back)),
            new ItemsMenuItem(ItemType.Consumable, getString(R.string.consumable)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.AppearanceChange, getString(R.string.appearance_change)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Booze, getString(R.string.booze)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.ContractNpc, getString(R.string.contract_npc)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Food, getString(R.string.food)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Generic, getString(R.string.generic)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Halloween, getString(R.string.halloween)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Immediate, getString(R.string.immediate)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Transmutation, getString(R.string.transmutation)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Unlock, getString(R.string.unlock)),
                new ItemsMenuItem(ItemType.Consumable, ConsumableType.Utility, getString(R.string.utility)),
            new ItemsMenuItem(ItemType.Container, getString(R.string.container)),
            new ItemsMenuItem(ItemType.CraftingMaterial, getString(R.string.crafting_material)),
            new ItemsMenuItem(ItemType.Gathering, getString(R.string.gathering)),
            new ItemsMenuItem(ItemType.Gizmo, getString(R.string.gizmo)),
            new ItemsMenuItem(ItemType.MiniPet, getString(R.string.mini)),
            new ItemsMenuItem(ItemType.Tool, getString(R.string.tool)),
            new ItemsMenuItem(ItemType.Trinket, getString(R.string.trinket)),
            new ItemsMenuItem(ItemType.Trophy, getString(R.string.trophy)),
            new ItemsMenuItem(ItemType.UpgradeComponent, getString(R.string.upgrade_component)),
                new ItemsMenuItem(ItemType.UpgradeComponent, UpgradeComponentType.Default, getString(R.string.str_default)),
                new ItemsMenuItem(ItemType.UpgradeComponent, UpgradeComponentType.Gem, getString(R.string.gem)),
                new ItemsMenuItem(ItemType.UpgradeComponent, UpgradeComponentType.Rune, getString(R.string.rune)),
                new ItemsMenuItem(ItemType.UpgradeComponent, UpgradeComponentType.Sigil, getString(R.string.sigil))
        };
        adapter = new ItemsMenuAdapter(getLayoutInflater());
        adapter.setItems(Arrays.asList(menuItems));
        menu.setAdapter(adapter);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsMenuItem item = adapter.getItem(position);

                onMenuItemClick(item);
            }
        });
    }

    private void onMenuItemClick(ItemsMenuItem item) {
        drawer.closeDrawer(menu);

        fragment.setFilter(item.type, item.subType);
        fragment.refresh();
    }


}
