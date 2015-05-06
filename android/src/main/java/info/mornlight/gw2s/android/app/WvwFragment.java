package info.mornlight.gw2s.android.app;

import android.os.Bundle;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.IntName;
import info.mornlight.gw2s.android.model.wvw.WvwMatch;
import info.mornlight.gw2s.android.ui.ItemListAdapter;
import info.mornlight.gw2s.android.ui.ItemView;
import info.mornlight.gw2s.android.ui.RefreshableFragment;
import info.mornlight.gw2s.android.ui.ThrowableLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class WvwFragment extends RefreshableFragment<List<WvwMatch>, ListView> {
    static private DateFormat format = new SimpleDateFormat("d MMM, hh:mm a");

    public interface OnWvwMatchSelectedListener {
        void onMatchSelected(WvwMatch match);
    }

    private OnWvwMatchSelectedListener listener;

    public void setListener(OnWvwMatchSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected ListView onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        return (ListView) inflater.inflate(R.layout.wvw_fragment, null);
    }

    @Override
    public Loader<List<WvwMatch>> onCreateLoader(int i, Bundle bundle) {
        return new ThrowableLoader<List<WvwMatch>>(getActivity(), null) {
            @Override
            public List<WvwMatch> loadData() throws Exception {
                App.instance().ensureWorldNames();

                return App.instance().getClient().listWvwMatches();
            }
        };
    }

    class WvwMatchItemView extends ItemView<WvwMatch> {
        private TextView redWorld;
        private TextView blueWorld;
        private TextView greenWorld;
        private TextView startTime;
        private TextView endTime;

        public WvwMatchItemView(View view) {
            super(view);

            redWorld = (TextView) view.findViewById(R.id.red_world);
            blueWorld = (TextView) view.findViewById(R.id.blue_world);
            greenWorld = (TextView) view.findViewById(R.id.green_world);
            startTime = (TextView) view.findViewById(R.id.start_time);
            endTime = (TextView) view.findViewById(R.id.end_time);
        }

        @Override
        public void update(WvwMatch item) {
            super.update(item);

            Map<Integer, IntName> worldNames = App.instance().getWorldNames();
            redWorld.setText(worldNames.get(item.getRedWorldId()).getName());
            blueWorld.setText(worldNames.get(item.getBlueWorldId()).getName());
            greenWorld.setText(worldNames.get(item.getGreenWorldId()).getName());
            startTime.setText(getString(R.string.start) + " " + format.format(item.getStartTime()));
            endTime.setText(getString(R.string.end) + " "+ format.format(item.getEndTime()));
        }
    }

    @Override
    protected void onLoadSucceed(Loader<List<WvwMatch>> loader, List<WvwMatch> data) {
        this.data = data;
        
        moveActiveWorldToFirst(this.data, App.instance().getActiveWorldId());

        ItemListAdapter<WvwMatch, WvwMatchItemView> adapter = new ItemListAdapter<WvwMatch, WvwMatchItemView>(R.layout.wvw_item, getActivity().getLayoutInflater()) {
            @Override
            protected WvwMatchItemView createView(int position, View view) {
                return new WvwMatchItemView(view);
            }
        };
        adapter.setItems(data);
        contentView.setAdapter(adapter);

        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener != null) {
                    WvwMatch match = (WvwMatch) contentView.getAdapter().getItem(position);
                    listener.onMatchSelected(match);
                }
            }
        });
    }

    private void moveActiveWorldToFirst(List<WvwMatch> data, int activeWorldId) {
        if(activeWorldId == 0)
            return;

        int foundMatch = -1;
        for(int i = 0; i < data.size(); i++) {
            WvwMatch match = data.get(i);
            if(match.getBlueWorldId() == activeWorldId
                    || match.getGreenWorldId() == activeWorldId
                    || match.getRedWorldId() == activeWorldId) {
                foundMatch = i;
                break;
            }
        }

        if(foundMatch != -1) {
            WvwMatch match = data.remove(foundMatch);
            data.add(0, match);
        }
    }
}
