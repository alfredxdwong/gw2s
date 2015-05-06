package info.mornlight.gw2s.android.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.InjectView;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.ui.BaseDialogFragment;
import info.mornlight.gw2s.android.ui.DialogResultListener;
import info.mornlight.gw2s.android.widget.RangeSeekBar;

/**
 * Created by alfred on 6/2/13.
 */
public class SelectLevelRangeDialogFragment extends BaseDialogFragment {
    public static final String ARG_MIN = "min";
    public static final String ARG_MAX = "max";
    public static final String ARG_RANGE_MIN = "range_min";
    public static final String ARG_RANGE_MAX = "range_max";

    @InjectView(R.id.seekbar)
    protected RangeSeekBar<Integer> seekbar;
    @InjectView(R.id.min)
    protected TextView min;
    @InjectView(R.id.max)
    protected TextView max;

    public static SelectLevelRangeDialogFragment newInstance(int requestCode, String title, int min, int max, int rangeMin, int rangeMax, DialogResultListener listener) {
        Bundle args = createArguments(title, "", requestCode);
        args.putInt(ARG_MIN, min);
        args.putInt(ARG_MAX, max);
        args.putInt(ARG_RANGE_MIN, rangeMin);
        args.putInt(ARG_RANGE_MAX, rangeMax);

        SelectLevelRangeDialogFragment fragment = new SelectLevelRangeDialogFragment();
        fragment.listener = listener;
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected View onCreateContentView(AlertDialog dialog, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.select_level_fragment, null);

        seekbar = (RangeSeekBar<Integer>) view.findViewById(R.id.seekbar);
        min = (TextView) view.findViewById(R.id.min);
        max = (TextView) view.findViewById(R.id.max);

        return view;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getText(android.R.string.ok), this);

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                min.setText(minValue.toString());
                max.setText(maxValue.toString());
            }
        });

        Bundle args = getArguments();
        min.setText(Integer.toString(args.getInt(ARG_MIN)));
        max.setText(Integer.toString(args.getInt(ARG_MAX)));

        seekbar.setAbsoluteMinValue(args.getInt(ARG_RANGE_MIN));
        seekbar.setAbsoluteMaxValue(args.getInt(ARG_RANGE_MAX));
        seekbar.setSelectedMinValue(args.getInt(ARG_MIN));
        seekbar.setSelectedMaxValue(args.getInt(ARG_MAX));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                getArguments().putInt(ARG_MIN, seekbar.getSelectedMinValue());
                getArguments().putInt(ARG_MAX, seekbar.getSelectedMaxValue());
                onResult(Activity.RESULT_OK);
                break;
        }
        super.onClick(dialog, which);
    }
}
