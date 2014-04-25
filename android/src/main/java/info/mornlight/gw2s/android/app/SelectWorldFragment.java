package info.mornlight.gw2s.android.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.IntName;
import info.mornlight.gw2s.android.ui.BaseFragment;
import info.mornlight.gw2s.android.ui.ItemListAdapter;
import info.mornlight.gw2s.android.ui.ItemView;
import info.mornlight.gw2s.android.util.IndicatedAsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectWorldFragment extends BaseFragment<List<IntName>, ListView> {
    public interface OnWorldSelectedListener {
        void onWorldSelected(int worldId);
    }

    private OnWorldSelectedListener listener;

    public void setOnWorldSelectedListener(OnWorldSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected ListView onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.select_world_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IndicatedAsyncTask<Void> task = new IndicatedAsyncTask<Void>(getSherlockActivity(), R.string.initializing) {
            @Override
            public Void call() throws Exception {
                App.instance().ensureWorldNames();
                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateView();
                    }
                });
            }
        };
        task.execute();
    }

    private void updateView() {
        data = new ArrayList<IntName>(App.instance().getWorldNames().values());
        Collections.sort(data, new Comparator<IntName>() {
            @Override
            public int compare(IntName lhs, IntName rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        final ListAdapter adapter = createAdatper(data);
        contentView.setAdapter(adapter);

        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    IntName item = (IntName) adapter.getItem(position);
                    listener.onWorldSelected(item.getId());
                }
            }
        });

        showContentView();
    }

    protected class WorldItemView extends ItemView<IntName> {
        private TextView name;

        public WorldItemView(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public void update(IntName item) {
            super.update(item);

            name.setText(item.getName());
        }
    }

    private ListAdapter createAdatper(List<IntName> names) {
        ItemListAdapter<IntName, WorldItemView> adapter = new ItemListAdapter<IntName, WorldItemView>(R.layout.select_world_item, getSherlockActivity().getLayoutInflater()) {
            @Override
            protected WorldItemView createView(int position, View view) {
                return new WorldItemView(view);
            }
        };
        adapter.setItems(names);

        return adapter;
    }
}
