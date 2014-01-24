package info.mornlight.gw2s.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.*;
import info.mornlight.gw2s.android.ui.ItemListAdapter;
import info.mornlight.gw2s.android.ui.ItemView;
import roboguice.inject.InjectFragment;

import java.util.Arrays;

/**
 * Created by alfred on 5/24/13.
 */
public class RecipesActivity extends BaseActivity {
    private SlidingMenu menu;
    private ItemsMenuAdapter adapter;

    @InjectFragment(R.id.fragment)
    private RecipesFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipes_activity);

        requestAd();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.recipes);

        initializeSlidingMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.recipes_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.m_menu:
                menu.toggle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class ItemsMenuItem {
        Discipline discipline;
        String type;
        String text;

        ItemsMenuItem(Discipline discipline, String text) {
            this.discipline = discipline;
            this.text = text;
        }

        ItemsMenuItem(Discipline discipline, String type, String text) {
            this.discipline = discipline;
            this.text = text;
            this.type = type;
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
            if (view == null || view.getItem().type != item.type) {
                if(item.type == null) {
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

    /*
    use this to get all llRecipe discipline and types from database
    select distinct type, discipline from gw2s_recipes join gw2s_recipes_disciplines on gw2s_recipes.id=gw2s_recipes_disciplines.id order by discipline
     */

    private void initializeSlidingMenu() {
        ListView menuView = (ListView) getLayoutInflater().inflate(R.layout.items_activity_sliding_menu, null);
        ItemsMenuItem[] menuItems = new ItemsMenuItem[] {
            new ItemsMenuItem(Discipline.Armorsmith, getString(R.string.armorsmith)),
                new ItemsMenuItem(Discipline.Armorsmith, "Bag", getString(R.string.bag)),
                new ItemsMenuItem(Discipline.Armorsmith, "Boots", getString(R.string.boots)),
                new ItemsMenuItem(Discipline.Armorsmith, "Bulk", getString(R.string.bulk)),
                new ItemsMenuItem(Discipline.Armorsmith, "Coat", getString(R.string.coat)),
                new ItemsMenuItem(Discipline.Armorsmith, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Armorsmith, "Gloves", getString(R.string.gloves)),
                new ItemsMenuItem(Discipline.Armorsmith, "Helm", getString(R.string.helm)),
                new ItemsMenuItem(Discipline.Armorsmith, "Insignia", getString(R.string.insignia)),
                new ItemsMenuItem(Discipline.Armorsmith, "Leggings", getString(R.string.leggings)),
                new ItemsMenuItem(Discipline.Armorsmith, "Refinement", getString(R.string.refinement)),
                new ItemsMenuItem(Discipline.Armorsmith, "Shoulders", getString(R.string.shoulders)),
                new ItemsMenuItem(Discipline.Armorsmith, "UpgradeComponent", getString(R.string.upgrade_component)),
            new ItemsMenuItem(Discipline.Artificer, getString(R.string.artificer)),
                new ItemsMenuItem(Discipline.Artificer, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Artificer, "Consumable", getString(R.string.consumable)),
                new ItemsMenuItem(Discipline.Artificer, "Focus", getString(R.string.focus)),
                new ItemsMenuItem(Discipline.Artificer, "Inscription", getString(R.string.inscription)),
                new ItemsMenuItem(Discipline.Artificer, "Potion", getString(R.string.potion)),
                new ItemsMenuItem(Discipline.Artificer, "Refinement", getString(R.string.refinement)),
                new ItemsMenuItem(Discipline.Artificer, "Scepter", getString(R.string.scepter)),
                new ItemsMenuItem(Discipline.Artificer, "Staff", getString(R.string.staff)),
                new ItemsMenuItem(Discipline.Artificer, "Trident", getString(R.string.trident)),
                new ItemsMenuItem(Discipline.Artificer, "UpgradeComponent", getString(R.string.upgrade_component)),
            new ItemsMenuItem(Discipline.Chef, getString(R.string.chef)),
                new ItemsMenuItem(Discipline.Chef, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Chef, "Dessert", getString(R.string.dessert)),
                new ItemsMenuItem(Discipline.Chef, "Dye", getString(R.string.dye)),
                new ItemsMenuItem(Discipline.Chef, "Feast", getString(R.string.feast)),
                new ItemsMenuItem(Discipline.Chef, "IngredientCooking", getString(R.string.ingredient_cooking)),
                new ItemsMenuItem(Discipline.Chef, "Meal", getString(R.string.meal)),
                new ItemsMenuItem(Discipline.Chef, "Seasoning", getString(R.string.seasoning)),
                new ItemsMenuItem(Discipline.Chef, "Snack", getString(R.string.snack)),
                new ItemsMenuItem(Discipline.Chef, "Soup", getString(R.string.soup)),
            new ItemsMenuItem(Discipline.Huntsman, getString(R.string.huntsman)),
                new ItemsMenuItem(Discipline.Huntsman, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Huntsman, "Consumable", getString(R.string.consumable)),
                new ItemsMenuItem(Discipline.Huntsman, "Inscription", getString(R.string.inscription)),
                new ItemsMenuItem(Discipline.Huntsman, "LongBow", getString(R.string.longbow)),
                new ItemsMenuItem(Discipline.Huntsman, "Pistol", getString(R.string.pistol)),
                new ItemsMenuItem(Discipline.Huntsman, "Refinement", getString(R.string.refinement)),
                new ItemsMenuItem(Discipline.Huntsman, "Rifle", getString(R.string.rifle)),
                new ItemsMenuItem(Discipline.Huntsman, "ShortBow", getString(R.string.short_bow)),
                new ItemsMenuItem(Discipline.Huntsman, "Speargun", getString(R.string.spear_gun)),
                new ItemsMenuItem(Discipline.Huntsman, "Torch", getString(R.string.torch)),
                new ItemsMenuItem(Discipline.Huntsman, "UpgradeComponent", getString(R.string.upgrade_component)),
                new ItemsMenuItem(Discipline.Huntsman, "Warhorn", getString(R.string.warhorn)),
            new ItemsMenuItem(Discipline.Jeweler, getString(R.string.jeweler)),
                new ItemsMenuItem(Discipline.Jeweler, "Amulet", getString(R.string.amulet)),
                new ItemsMenuItem(Discipline.Jeweler, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Jeweler, "Earring", getString(R.string.earring)),
                new ItemsMenuItem(Discipline.Jeweler, "Refinement", getString(R.string.refinement)),
                new ItemsMenuItem(Discipline.Jeweler, "Ring", getString(R.string.ring)),
                new ItemsMenuItem(Discipline.Jeweler, "UpgradeComponent", getString(R.string.upgrade_component)),
            new ItemsMenuItem(Discipline.Leatherworker, getString(R.string.leatherworker)),
                new ItemsMenuItem(Discipline.Leatherworker, "Bag", getString(R.string.bag)),
                new ItemsMenuItem(Discipline.Leatherworker, "Boots", getString(R.string.boots)),
                new ItemsMenuItem(Discipline.Leatherworker, "Bulk", getString(R.string.bulk)),
                new ItemsMenuItem(Discipline.Leatherworker, "Coat", getString(R.string.coat)),
                new ItemsMenuItem(Discipline.Leatherworker, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Leatherworker, "Gloves", getString(R.string.gloves)),
                new ItemsMenuItem(Discipline.Leatherworker, "Helm", getString(R.string.helm)),
                new ItemsMenuItem(Discipline.Leatherworker, "Insignia", getString(R.string.insignia)),
                new ItemsMenuItem(Discipline.Leatherworker, "Leggings", getString(R.string.leggings)),
                new ItemsMenuItem(Discipline.Leatherworker, "Refinement", getString(R.string.refinement)),
                new ItemsMenuItem(Discipline.Leatherworker, "Shoulders", getString(R.string.shoulders)),
                new ItemsMenuItem(Discipline.Leatherworker, "UpgradeComponent", getString(R.string.upgrade_component)),
            new ItemsMenuItem(Discipline.Tailor, getString(R.string.tailor)),
                new ItemsMenuItem(Discipline.Tailor, "Bag", getString(R.string.bag)),
                new ItemsMenuItem(Discipline.Tailor, "Boots", getString(R.string.boots)),
                new ItemsMenuItem(Discipline.Tailor, "Bulk", getString(R.string.bulk)),
                new ItemsMenuItem(Discipline.Tailor, "Coat", getString(R.string.coat)),
                new ItemsMenuItem(Discipline.Tailor, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Tailor, "Gloves", getString(R.string.gloves)),
                new ItemsMenuItem(Discipline.Tailor, "Helm", getString(R.string.helm)),
                new ItemsMenuItem(Discipline.Tailor, "Insignia", getString(R.string.insignia)),
                new ItemsMenuItem(Discipline.Tailor, "Leggings", getString(R.string.leggings)),
                new ItemsMenuItem(Discipline.Tailor, "Refinement", getString(R.string.refinement)),
                new ItemsMenuItem(Discipline.Tailor, "Shoulders", getString(R.string.shoulders)),
                new ItemsMenuItem(Discipline.Tailor, "UpgradeComponent", getString(R.string.upgrade_component)),
            new ItemsMenuItem(Discipline.Weaponsmith, getString(R.string.weaponsmith)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Axe", getString(R.string.axe)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Component", getString(R.string.component)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Consumable", getString(R.string.consumable)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Dagger", getString(R.string.dagger)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Greatsword", getString(R.string.great_sword)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Hammer", getString(R.string.hammer)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Harpoon", getString(R.string.harpoon)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Inscription", getString(R.string.inscription)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Mace", getString(R.string.mace)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Refinement", getString(R.string.refinement)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Shield", getString(R.string.shield)),
                new ItemsMenuItem(Discipline.Weaponsmith, "Sword", getString(R.string.sword)),
                new ItemsMenuItem(Discipline.Weaponsmith, "UpgradeComponent", getString(R.string.upgrade_component)),
        };

        adapter = new ItemsMenuAdapter(getLayoutInflater());
        adapter.setItems(Arrays.asList(menuItems));
        menuView.setAdapter(adapter);
        menuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemsMenuItem item = adapter.getItem(position);

                onMenuItemClick(item);

                menu.showContent();
            }
        });

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //menu.setShadowWidthRes(100);
       // menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindWidth(300);
        menu.setBehindScrollScale(0.1f);
        //menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(menuView);
    }

    private void onMenuItemClick(ItemsMenuItem item) {
        fragment.setFilter(item.discipline, item.type);
        fragment.refresh();
    }


}
