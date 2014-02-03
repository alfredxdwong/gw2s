package info.mornlight.gw2s.android.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.*;
import info.mornlight.gw2s.android.model.wvw.WvwMap;
import info.mornlight.gw2s.android.model.wvw.WvwMapType;
import info.mornlight.gw2s.android.model.wvw.WvwMatchDetails;
import info.mornlight.gw2s.android.model.wvw.WvwObjective;
import info.mornlight.gw2s.android.ui.*;
import roboguice.inject.InjectView;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WvwDetailsFragment extends RefreshableFragment<WvwMatchDetails, View> {
    private String matchId;
    private int redWorldId;
    private int blueWorldId;
    private int greenWorldId;

    private Timer autoUpdateTimer;

    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int GREEN = 2;

    @InjectView(R.id.score_bar_red)
    private TextView scoreBarRed;
    @InjectView(R.id.score_bar_blue)
    private TextView scoreBarBlue;
    @InjectView(R.id.score_bar_green)
    private TextView scoreBarGreen;
    @InjectView(R.id.red_world)
    private TextView redWorld;
    @InjectView(R.id.blue_world)
    private TextView blueWorld;
    @InjectView(R.id.green_world)
    private TextView greenWorld;
    @InjectView(R.id.score_red)
    private TextView scoreRed;
    @InjectView(R.id.score_blue)
    private TextView scoreBlue;
    @InjectView(R.id.score_green)
    private TextView scoreGreen;
    @InjectView(R.id.maps)
    private ExpandableListView maps;

    public void update(String matchId, int redWorldId, int blueWorldId, int greenWorldId) {
        this.matchId = matchId;
        this.redWorldId = redWorldId;
        this.blueWorldId = blueWorldId;
        this.greenWorldId = greenWorldId;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wvw_details_fragment, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean autoUpdate = prefs.getBoolean(Prefs.WVW_AUTO_UPDATE, false);
        if(autoUpdate) {
            int updateInterval = prefs.getInt(Prefs.WVW_AUTO_UPDATE_INTERVAL, 30) * 1000;
            autoUpdateTimer = new Timer();
            autoUpdateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }
                    });
                }
            }, updateInterval, updateInterval);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(autoUpdateTimer != null) {
            autoUpdateTimer.cancel();
            autoUpdateTimer = null;
        }
    }

    @Override
    public Loader<WvwMatchDetails> onCreateLoader(int i, Bundle bundle) {
        return new ThrowableLoader<WvwMatchDetails>(getActivity(), null) {
            @Override
            public WvwMatchDetails loadData() throws Exception {
                if(matchId == null)
                    return null;

                App.instance().ensureWorldNames();
                App.instance().ensureObjectiveNames();

                return App.instance().getClient().readWvwMatchDetails(matchId);
            }
        };
    }

    @Override
    protected void onLoadSucceed(Loader<WvwMatchDetails> loader, WvwMatchDetails data) {
        this.data = data;

        updateView();
    }
    private void updateView() {
        scoreBarRed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, data.getScores().get(RED) / 10));
        scoreBarBlue.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, data.getScores().get(BLUE) / 10));
        scoreBarGreen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, data.getScores().get(GREEN) / 10));

        Map<Integer, IntName> worldNames = App.instance().getWorldNames();
        redWorld.setText(worldNames.get(redWorldId).getName());
        blueWorld.setText(worldNames.get(blueWorldId).getName());
        greenWorld.setText(worldNames.get(greenWorldId).getName());

        scoreRed.setText(Integer.toString(data.getScores().get(RED)));
        scoreBlue.setText(Integer.toString(data.getScores().get(BLUE)));
        scoreGreen.setText(Integer.toString(data.getScores().get(GREEN)));

        updateMapsList();
    }

    class WvwMapAdapter extends BaseExpandableListAdapter {
        List<WvwMap> maps;
        LayoutInflater inflater;

        WvwMapAdapter(LayoutInflater inflater, List<WvwMap> maps) {
            this.maps = maps;
            this.inflater = inflater;
        }

        @Override
        public int getGroupCount() {
            return maps.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return maps.get(groupPosition).getObjectives().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return maps.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return maps.get(groupPosition).getObjectives().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition * 100;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * 100 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            MapItemView v = convertView != null ? (MapItemView) convertView.getTag() : null;
            if(v == null) {
                convertView = inflater.inflate(R.layout.wvw_details_map_item, null);
                v = new MapItemView(convertView);
                convertView.setTag(v);
            }

            v.update((WvwMap) getGroup(groupPosition));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ObjectiveItemView v = convertView != null ? (ObjectiveItemView) convertView.getTag() : null;
            if(v == null) {
                convertView = inflater.inflate(R.layout.wvw_details_objective_item, null);
                v = new ObjectiveItemView(convertView);
                convertView.setTag(v);
            }

            v.update((WvwObjective) getChild(groupPosition, childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        public void update(List<WvwMap> maps) {
            this.maps = maps;
        }
    }

    class ObjectiveItemView extends ItemView<WvwObjective> {
        private TextView name;
        private TextView owner;
        //private TextView ownerGuild;

        public ObjectiveItemView(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            owner = (TextView) view.findViewById(R.id.owner);
            //ownerGuild = (TextView) view.findViewById(R.id.owner_guild);
        }

        @Override
        public void update(WvwObjective item) {
            super.update(item);
            Map<Integer, IntName> objectiveNames = App.instance().getObjectiveNames();

            name.setText(objectiveNames.get(item.getId()).getName());
            owner.setText(item.getOwner().toString());

            int color = R.color.world_red;
            switch (item.getOwner()) {
                case Red:
                    color = R.color.world_red;
                    break;
                case Blue:
                    color = R.color.world_blue;
                    break;
                case Green:
                    color = R.color.world_green;
                    break;
                default:
                    color = R.color.world_neutral;
                    break;
            }
            owner.setTextColor(getResources().getColor(color));
            //ownerGuild
        }
    }
    class MapItemView extends ItemView<WvwMap> {
        private TextView name;
        private TextView objectiveNum;
        private TextView scoreRed;
        private TextView scoreBlue;
        private TextView scoreGreen;

        public MapItemView(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            objectiveNum = (TextView) view.findViewById(R.id.objective_num);
            scoreRed = (TextView) view.findViewById(R.id.score_red);
            scoreBlue = (TextView) view.findViewById(R.id.score_blue);
            scoreGreen = (TextView) view.findViewById(R.id.score_green);
        }

        @Override
        public void update(WvwMap item) {
            super.update(item);

            name.setText(getMapName(item.getType()));
            objectiveNum.setText("(" + item.getObjectives().size() + ")");
            scoreRed.setText(Integer.toString(item.getScores().get(RED)));
            scoreBlue.setText(Integer.toString(item.getScores().get(BLUE)));
            scoreGreen.setText(Integer.toString(item.getScores().get(GREEN)));
        }

        private String getMapName(WvwMapType type) {
            switch(type) {
                case RedHome: return getResources().getString(R.string.red_home);
                case BlueHome: return getResources().getString(R.string.blue_home);
                case GreenHome: return getResources().getString(R.string.green_home);
                case Center: return getResources().getString(R.string.center);
                default: return "";
            }
        }
    }

    private void updateMapsList() {
        if(maps.getExpandableListAdapter() == null) {
            WvwMapAdapter adapter = new WvwMapAdapter(getSherlockActivity().getLayoutInflater(), data.getMaps());
            maps.setAdapter(adapter);
        } else {
            WvwMapAdapter adapter = (WvwMapAdapter) maps.getExpandableListAdapter();
            adapter.update(data.getMaps());
            adapter.notifyDataSetChanged();
        }
    }
}
