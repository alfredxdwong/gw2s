package info.mornlight.gw2s.android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.Loader;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.InjectView;
import com.nostra13.universalimageloader.core.ImageLoader;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.db.Database;
import info.mornlight.gw2s.android.model.item.*;
import info.mornlight.gw2s.android.ui.LoaderFragment;
import info.mornlight.gw2s.android.ui.ThrowableLoader;

public class ItemDetailsFragment extends LoaderFragment<ItemDetailsFragment.ItemInfo, View> {
    @InjectView(R.id.image)
    protected ImageView image;
    @InjectView(R.id.name)
    protected TextView name;
    @InjectView(R.id.type)
    protected TextView type;
    @InjectView(R.id.subtype)
    protected TextView subtype;
    @InjectView(R.id.specific_view)
    protected LinearLayout specificView;
    @InjectView(R.id.description)
    protected TextView description;
    @InjectView(R.id.game_type)
    protected TextView gameType;
    @InjectView(R.id.restrictions)
    protected TextView restrictions;
    @InjectView(R.id.flags)
    protected TextView flags;
    @InjectView(R.id.vendor_value_gold)
    protected TextView vendorValueGold;
    @InjectView(R.id.vendor_value_silver)
    protected TextView vendorValueSilver;
    @InjectView(R.id.vendor_value_copper)
    protected TextView vendorValueCopper;
    @InjectView(R.id.coin_gold)
    protected ImageView gold;
    @InjectView(R.id.coin_silver)
    protected ImageView silver;
    @InjectView(R.id.coin_copper)
    protected ImageView copper;
    @InjectView(R.id.required_level)
    protected TextView requiredLevel;
    @InjectView(R.id.infix_upgrade)
    protected LinearLayout infixUpgrade;

    //llRecipe
    @InjectView(R.id.recipe)
    protected LinearLayout llRecipe;
    @InjectView(R.id.min_rating)
    protected TextView minRating;
    @InjectView(R.id.recipe_disciplines)
    protected TextView disciplines;
    @InjectView(R.id.recipe_ingredients)
    protected LinearLayout ingredients;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_details_fragment, null);
    }

    static class ItemInfo {
        ItemDetails details;
        RecipeDetails recipe;
        ItemIndex[] ingredients;

        public ItemInfo(ItemDetails details, RecipeDetails recipe, ItemIndex[] ingredients) {
            this.details = details;
            this.recipe = recipe;
            this.ingredients = ingredients;
        }
    }

    @Override
    public Loader<ItemInfo> onCreateLoader(int i, Bundle bundle) {
        return new ThrowableLoader<ItemInfo>(getActivity(), null) {
            @Override
            public ItemInfo loadData() throws Exception {
                Database db = App.instance().getDatabase();

                //load item
                int itemId = getActivity().getIntent().getIntExtra("item_id", 0);
                ItemDetails item = db.readItemDetails(itemId);

                //load recipe
                RecipeDetails recipe = null;
                ItemIndex[] ingredients = null;

                recipe = db.readRecipeDetailsForItem(itemId);
                if(recipe != null) {
                    ingredients = new ItemIndex[recipe.getIngredients().length];
                    for(int i = 0; i < recipe.getIngredients().length; i++) {
                        ingredients[i] = db.readItemIndex(recipe.getIngredients()[i].getItemId());
                    }
                }

                return new ItemInfo(item, recipe, ingredients);
            }
        };
    }

    @Override
    protected void onLoadSucceed(Loader<ItemInfo> loader, ItemInfo data) {
        super.onLoadSucceed(loader, data);

        this.data = data;

        updateView();
        updateRecipeView();
    }

    private void updateRecipeView() {
        RecipeDetails recipe = data.recipe;

        if(recipe == null) {
            llRecipe.setVisibility(View.GONE);
            return;
        }

        String str = ViewHelper.makeString(recipe.getDisciplines(), getActivity());
        disciplines.setText(getString(R.string.disciplines) + ": " + str);

        minRating.setText(getString(R.string.min_rating) + ": " + recipe.getMinRating());

        ingredients.removeAllViews();
        for(int i = 0; i < recipe.getIngredients().length; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_details_fragment_ingredient, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView count = (TextView) view.findViewById(R.id.count);

            ItemIndex item = data.ingredients[i];
            int color = getResources().getColor(ViewHelper.getRarityColor(item.getRarity()));
            ImageLoader.getInstance().displayImage(item.getImage(), image);
            name.setTextColor(color);
            name.setText(item.getName());

            count.setText("x" + recipe.getIngredients()[i].getCount());

            view.setClickable(true);
            view.setBackgroundResource(R.drawable.clickable);
            view.setTag(item);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemIndex item = (ItemIndex) v.getTag();

                    Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                    intent.putExtra("item_id", item.getItemId());
                    startActivity(intent);
                }
            });
            ingredients.addView(view);
        }
    }

    private void updateView() {
        ItemDetails item = data.details;

        ImageLoader.getInstance().displayImage(item.getImage(), image);

        name.setTextColor(getResources().getColor(ViewHelper.getRarityColor(item.getRarity())));
        name.setText(item.getName());

        type.setText(ViewHelper.formatType(item.getType()));
        subtype.setText(ViewHelper.formatSubtype(item.getSubtype()));

        Object details = item.getDetails();
        View subView = createSubView(details);
        specificView.removeAllViews();
        if(subView != null) {
            specificView.addView(subView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        if(details != null && details instanceof UpgradableItem && ((UpgradableItem) details).getInfixUpgrade() != null) {
            UpgradableItem infix = (UpgradableItem)details;

            //buff description
            Buff buff = infix.getInfixUpgrade().getBuff();
            String desc = buff != null ? buff.getDescription() : "";
            TextView descView = (TextView) infixUpgrade.findViewById(R.id.buff_description);
            if(!desc.isEmpty()) {
                descView.setText(desc);
            } else {
                descView.setVisibility(View.GONE);
            }

            //attribute modifiers
            LinearLayout attrs = (LinearLayout) infixUpgrade.findViewById(R.id.infix_upgrade_attributes);
            attrs.removeAllViews();
            for(AttributeModifier attr : infix.getInfixUpgrade().getAttributes()) {
                TextView view = new TextView(getActivity());
                view.setText(ViewHelper.formatAttributeModifier(attr, getActivity()));
                view.setTextColor(getResources().getColor(R.color.attribute));
                attrs.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            //bonuses
            if(details instanceof UpgradeComponent) {
                UpgradeComponent component = (UpgradeComponent)details;
                if (component.getBonuses() != null && component.getBonuses().length > 0) {
                    LinearLayout bonuses = (LinearLayout) infixUpgrade.findViewById(R.id.bonuses);
                    attrs.removeAllViews();
                    for(String bonus : component.getBonuses()) {
                        TextView view = new TextView(getActivity());
                        view.setText(bonus);
                        view.setTextColor(getResources().getColor(R.color.bonus));
                        bonuses.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            }

        } else {
            infixUpgrade.setVisibility(View.GONE);
        }

        String desc = item.getDescription();
        if(desc != null && !desc.isEmpty()) {
            Spanned spanned = Html.fromHtml(desc);
            description.setText(spanned);
        } else {
            description.setVisibility(View.GONE);
        }

        gameType.setText(getString(R.string.game_type) + " " + ViewHelper.makeString(item.getGameTypes()));

        if(item.getRestrictions().length > 0) {
            restrictions.setText(getString(R.string.restrictions) + " " + ViewHelper.makeString(item.getRestrictions(), ", "));
        } else {
            restrictions.setVisibility(View.GONE);
        }

        if(item.getFlags().length > 0) {
            flags.setText(ViewHelper.makeString(item.getFlags(), getActivity()));
        } else {
            flags.setVisibility(View.GONE);
        }

        ViewHelper.Price price = ViewHelper.formatPrice(item.getVendorValue());
        updatePrice(price.gold, vendorValueGold, gold);
        updatePrice(price.silver, vendorValueSilver, silver);
        updatePrice(price.copper, vendorValueCopper, copper);

        requiredLevel.setText(getString(R.string.required_level) + " " + Integer.toString(item.getLevel()));
    }

    private View createSubView(Object details) {
        if(details instanceof Weapon) {
            WeaponView view = new WeaponView(getActivity());
            view.update((Weapon) details);
            return view;

        } else if (details instanceof Armor) {
            ArmorView view = new ArmorView(getActivity());
            view.update((Armor) details);
            return view;
        } else if (details instanceof Consumable) {
            ConsumableView view = new ConsumableView(getActivity());
            view.update((Consumable) details);
            return view;
        }

        return null;

    }

    private void updatePrice(int num, TextView text, ImageView image) {
        if(num != 0) {
            text.setText(Integer.toString(num));
        } else {
            text.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }
    }

    static class WeaponView extends LinearLayout {
        public WeaponView(Context context) {
            super(context);

            LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.item_details_weapon, this, true);
        }

        public void update(Weapon weapon) {
            TextView strength = (TextView) findViewById(R.id.strength);
            TextView damageType = (TextView) findViewById(R.id.damage_typye);

            strength.setText(ViewHelper.formatWeaponStrength(weapon.getMinPower(), weapon.getMaxPower()));
            damageType.setText(getResources().getString(R.string.damageType) + " " + ViewHelper.formatDamageType(weapon.getDamageType()));
        }
    }

    static class ArmorView extends LinearLayout {
        public ArmorView(Context context) {
            super(context);

            LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.item_details_armor, this, true);
        }

        public void update(Armor armor) {
            TextView weightClass = (TextView) findViewById(R.id.weight_class);
            TextView defense = (TextView) findViewById(R.id.defense);

            weightClass.setText(getResources().getString(R.string.weight_class) + " " + ViewHelper.toString(armor.getWeightClass()));
            defense.setText(Integer.toString(armor.getDefense()));
        }
    }

    static class ConsumableView extends LinearLayout {
        public ConsumableView(Context context) {
            super(context);

            LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.item_details_consumable, this, true);
        }

        public void update(Consumable item) {
            if (item.getDurationMs() == 0) {
                findViewById(R.id.duration_layout).setVisibility(View.GONE);
            } else {
                TextView duration = (TextView) findViewById(R.id.duration);
                duration.setText(Integer.toString(item.getDurationMs() / 1000));
            }

            if (item.getDescription() == null || item.getDescription().isEmpty()) {
                findViewById(R.id.description).setVisibility(View.GONE);
            } else {
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(item.getDescription());
            }
        }
    }
}
