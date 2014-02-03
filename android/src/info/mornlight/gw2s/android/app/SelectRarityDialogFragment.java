package info.mornlight.gw2s.android.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.item.Rarity;
import info.mornlight.gw2s.android.ui.BaseDialogFragment;
import info.mornlight.gw2s.android.ui.DialogResultListener;
import roboguice.inject.InjectView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alfred on 6/2/13.
 */
public class SelectRarityDialogFragment extends BaseDialogFragment {
    /**
     * Arguments key for the selected item
     */
    public static final String ARG_SELECTED = "selected";

    static Map<Rarity, Integer> valueMap;

    static {
        valueMap = new HashMap<Rarity, Integer>();
        valueMap.put(Rarity.Basic, R.id.rarity_basic);
        valueMap.put(Rarity.Fine, R.id.rarity_fine);
        valueMap.put(Rarity.Masterwork, R.id.rarity_masterwork);
        valueMap.put(Rarity.Rare, R.id.rarity_rare);
        valueMap.put(Rarity.Exotic, R.id.rarity_exotic);
        valueMap.put(Rarity.Ascended, R.id.rarity_ascended);
        valueMap.put(Rarity.Legendary, R.id.rarity_legendary);
    }

    @InjectView(R.id.rarity)
    RadioGroup rarity;

    public static SelectRarityDialogFragment newInstance(int requestCode, String title, Rarity selected, DialogResultListener listener) {
        Bundle args = createArguments(title, "", requestCode);
        args.putSerializable(ARG_SELECTED, selected);

        SelectRarityDialogFragment fragment = new SelectRarityDialogFragment();
        fragment.listener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    public static Rarity getSelected(Bundle arguments) {
        return (Rarity) arguments.getSerializable(ARG_SELECTED);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                getArguments().putSerializable(ARG_SELECTED, getChoice());
                onResult(Activity.RESULT_OK);
                break;
        }

        super.onClick(dialog, which);
    }

    @Override
    public void onResume() {
        super.onResume();

        Rarity value = getSelected(getArguments());
        if(value == null) {
            rarity.check(R.id.rarity_all);
        } else {
            rarity.check(valueMap.get(value));
        }
    }

    @Override
    public View onCreateContentView(AlertDialog dialog, LayoutInflater inflater) {
        View view =  inflater.inflate(R.layout.select_rarity_fragment, null);
        rarity = (RadioGroup) view.findViewById(R.id.rarity);

        return view;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getText(android.R.string.ok), this);

        return dialog;
    }

    private Rarity getChoice() {
        int checkedId = rarity.getCheckedRadioButtonId();

        if(checkedId == R.id.rarity_all)
            return null;

        for(Map.Entry<Rarity, Integer> entry : valueMap.entrySet()) {
            if(entry.getValue() == checkedId) {
                return entry.getKey();
            }
        }

        return null;
    }
}
