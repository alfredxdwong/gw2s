/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.mornlight.gw2s.android.ui;

import static android.app.Activity.RESULT_OK;
import static android.content.DialogInterface.BUTTON_NEGATIVE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import info.mornlight.gw2s.android.R;

import java.io.Serializable;

/**
 * Helper to display a single choice dialog
 */
public class SingleChoiceDialogFragment<T extends Serializable> extends BaseDialogFragment implements
        OnClickListener {

    /**
     * Arguments key for the selected item
     */
    public static final String ARG_SELECTED = "selected";

    /**
     * Choices arguments
     */
    protected static final String ARG_CHOICES = "choices";

    /**
     * Selected choice argument
     */
    protected static final String ARG_SELECTED_CHOICE = "selectedChoice";

    /**
     * Tag
     */
    protected static final String TAG = "single_choice_dialog";

    public static <T extends Serializable> SingleChoiceDialogFragment<T> newInstance(int requestCode, String title, T[] choices, T selected) {
        Bundle args = createArguments(title, "", requestCode);
        args.putSerializable(ARG_CHOICES, choices);
        args.putSerializable(ARG_SELECTED, selected);

        SingleChoiceDialogFragment fragment = new SingleChoiceDialogFragment<T>();
        fragment.setArguments(args);
        return fragment;
    }
    
    public static String getSelected(Bundle arguments) {
        return (String) arguments.getString(ARG_SELECTED);
    }

    protected T[] getChoices() {
        return (T[]) getArguments().getSerializable(ARG_CHOICES);
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        switch (which) {
        case BUTTON_NEGATIVE:
            break;
        default:
            getArguments().putSerializable(ARG_SELECTED,
                    getChoices()[which]);
            onResult(RESULT_OK);
        }
    }

    @Override
    protected View onCreateContentView(final AlertDialog dialog, LayoutInflater inflater) {
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        //AbsListView view = (ListView) inflater.inflate(R.layout.dialog_list_view, null);
        ListView view = new ListView(getActivity());

        view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onClick(dialog, position);
            }
        });

        T[] choices = getChoices();

        ListAdapter adapter = createAdapter(getActivity(), choices);
        view.setAdapter(adapter);

        return view;

    }

    @Override
    public AlertDialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setButton(BUTTON_NEGATIVE, getActivity().getString(R.string.cancel), this);

        return dialog;
    }

    /**
     * overwrite this to create your own list adapter
     * @param choices
     * @return
     */
    protected ListAdapter createAdapter(Context context, T[] choices) {
        return new ArrayAdapter<T>(context, android.R.layout.simple_expandable_list_item_1, choices);
    }
}
