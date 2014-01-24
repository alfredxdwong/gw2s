package info.mornlight.gw2s.android.app;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.Event;
import info.mornlight.gw2s.android.model.IntName;
import info.mornlight.gw2s.android.model.StringName;
import info.mornlight.gw2s.android.ui.*;

import java.util.*;

public class DynamicEventsFragment extends RefreshableFragment<Map<String, Event>, ExpandableListView> {
    private Timer autoUpdateTimer;

    @Override
    public Loader<Map<String, Event>> onCreateLoader(int i, Bundle bundle) {
        return new ThrowableLoader<Map<String,Event>>(getActivity(), null) {
            @Override
            public Map<String, Event> loadData() throws Exception {
                int worldId = App.instance().getActiveWorldId();
                if(worldId == 0) {
                    return null;
                }

                App.instance().ensureMapNames();
                App.instance().ensureEventNames();

                List<Event> events = App.instance().getClient().listEvents(worldId);
                Map<String, Event> eventMap = new HashMap<String, Event>();
                for(Event event : events) {
                    eventMap.put(event.getEventId(), event);
                }

                return eventMap;
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean autoUpdate = prefs.getBoolean(Prefs.DYNAMIC_EVENTS_AUTO_UPDATE, false);
        if(autoUpdate) {
            int updateInterval = prefs.getInt(Prefs.DYNAMIC_EVENTS_AUTO_UPDATE_INTERVAL, 30) * 1000;
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
    protected void onLoadSucceed(Loader<Map<String, Event>> loader, Map<String, Event> data) {
        if(data == null)
            return;

        if(this.data == null) {
            this.data = data;
            ExpandableListAdapter adapter = createAdatper(data);
            contentView.setAdapter(adapter);
        } else {
            updateEventStates(data);
            DynamicEventsAdapter adapter = (DynamicEventsAdapter) contentView.getExpandableListAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    private void updateEventStates(Map<String, Event> newData) {
        for(Event e : newData.values()) {
            Event event = data.get(e.getEventId());
            if(event != null) {
                event.setState(e.getState());
            } else {
                //data.put(e.getEventId(), e);
            }
        }
    }

    public void clear() {
        data = null;
        contentView.setAdapter((ExpandableListAdapter) null);
    }

    class EventGroup {
        String name;
        List<Event> events = new ArrayList<Event>();
    }

    class DynamicEventsAdapter extends BaseExpandableListAdapter {
        List<EventGroup> groups;
        LayoutInflater inflater;

        DynamicEventsAdapter(LayoutInflater inflater, List<EventGroup> groups) {
            this.groups = groups;
            this.inflater = inflater;
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups.get(groupPosition).events.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups.get(groupPosition).events.get(childPosition);
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
            GroupItemView v = convertView != null ? (GroupItemView) convertView.getTag() : null;
            if(v == null) {
                convertView = inflater.inflate(R.layout.dynamic_events_group_item, null);
                v = new GroupItemView(convertView);
                convertView.setTag(v);
            }

            v.update((EventGroup) getGroup(groupPosition));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            EventItemView v = convertView != null ? (EventItemView) convertView.getTag() : null;
            if(v == null) {
                convertView = inflater.inflate(R.layout.dynamic_events_item, null);
                v = new EventItemView(convertView);
                convertView.setTag(v);
            }

            v.update((Event) getChild(groupPosition, childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    private ExpandableListAdapter createAdatper(Map<String, Event> data) {
        List<EventGroup> groups = makeGroups(data);

        ExpandableListAdapter adapter = new DynamicEventsAdapter(getActivity().getLayoutInflater(), groups);
        return adapter;
    }

    private List<EventGroup> makeGroups(Map<String, Event> events) {
        Map<Integer, EventGroup> groupMap = new HashMap<Integer, EventGroup>();

        Map<Integer, IntName> mapNames = App.instance().getMapNames();
        Map<String, StringName> eventNames = App.instance().getEventNames();
        for(Event event : events.values()) {
            EventGroup group = groupMap.get(event.getMapId());
            if(group == null) {
                group = new EventGroup();
                group.name = mapNames.get(event.getMapId()).getName();

                groupMap.put(event.getMapId(), group);
            }

            event.setMapName(group.name);
            event.setEventName(eventNames.get(event.getEventId()).getName());
            group.events.add(event);
        }

        ArrayList<EventGroup> groups = new ArrayList<EventGroup>(groupMap.values());
        Collections.sort(groups, new Comparator<EventGroup>()
        {
            @Override
            public int compare(EventGroup lhs, EventGroup rhs)
            {
                return lhs.name.compareTo(rhs.name);
            }
        });

        for(EventGroup group : groups) {
            Collections.sort(group.events, new Comparator<Event>()
            {
                @Override
                public int compare(Event lhs, Event rhs)
                {
                    return lhs.getEventName().compareTo(rhs.getEventName());
                }
            });
        }

        return groups;
    }

    @Override
    protected ExpandableListView onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        ExpandableListView view = (ExpandableListView) inflater.inflate(R.layout.dynamic_events_fragment, null);

        //view.setBackgroundResource(R.drawable.background);

        return view;
    }

    class EventItemAdapter extends ItemListAdapter<Event, EventItemView> {

        public EventItemAdapter(int viewId, LayoutInflater inflater) {
            super(viewId, inflater);
        }

        @Override
        protected EventItemView createView(int position, View view) {
            return new EventItemView(view);
        }
    }

    class EventItemView extends ItemView<Event> {
        private TextView mapName;
        private TextView eventName;
        private TextView eventState;

        public EventItemView(View view) {
            super(view);

            mapName = (TextView) view.findViewById(R.id.map_name);
            eventName = (TextView) view.findViewById(R.id.event_name);
            eventState = (TextView) view.findViewById(R.id.event_state);
        }

        @Override
        public void update(Event item) {
            super.update(item);

            mapName.setText(item.getMapName());
            eventName.setText(item.getEventName());

            int color = R.color.event_active;
            switch (item.getState()) {
                case Active:
                    color = R.color.event_active;
                    break;
                case Fail:
                    color = R.color.event_fail;
                    break;
                case Invalid:
                    color = R.color.event_invalid;
                    break;
                case Preparation:
                    color = R.color.event_preparation;
                    break;
                case Success:
                    color = R.color.event_success;
                    break;
                case Warmup:
                    color = R.color.event_warmup;
                    break;

            }
            eventState.setTextColor(getResources().getColor(color));
            eventState.setText(item.getState().toString());
        }
    }

    class GroupItemView extends ItemView<EventGroup> {
        private TextView name;
        private TextView eventNum;

        public GroupItemView(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            eventNum = (TextView) view.findViewById(R.id.event_num);
        }

        @Override
        public void update(EventGroup item) {
            super.update(item);

            name.setText(item.name);
            eventNum.setText("(" + item.events.size() + ")");
        }
    }
}
