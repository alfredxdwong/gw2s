package info.mornlight.gw2s.android.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.model.map.*;
import info.mornlight.gw2s.android.ui.LoaderFragment;
import info.mornlight.gw2s.android.ui.ThrowableLoader;
import info.mornlight.gw2s.android.util.ToastUtils;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends LoaderFragment<ContinentFloor, View> {
    @InjectView(R.id.mapview)
    private MapView mapview;

    private Continent continent;

    private ItemizedOverlay<OverlayItem> overlay;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, null);
    }

    @Override
    public Loader<ContinentFloor> onCreateLoader(int i, Bundle bundle) {
        return new ThrowableLoader<ContinentFloor>(getActivity(), null) {
            @Override
            public ContinentFloor loadData() throws Exception {
                    if(continent == null) return null;

                App app = App.instance();
                return app.getClient().readContinentFloor(Integer.parseInt(continent.getId()), 1);
            }
        };
    }

    @Override
    protected void onLoadSucceed(Loader<ContinentFloor> loader, ContinentFloor data) {
        super.onLoadSucceed(loader, data);

        this.data = data;
        overlay = generateOverlays();
        displayOverlays();
    }

    private ItemizedOverlay<OverlayItem> generateOverlays() {
        if(data == null) return null;

        List<OverlayItem> taskOverlayItems = new ArrayList<OverlayItem>();
        List<OverlayItem> waypointsOverlayItems = new ArrayList<OverlayItem>();
        List<OverlayItem> landmarkOverlayItems = new ArrayList<OverlayItem>();
        List<OverlayItem> vistaOverlayItems = new ArrayList<OverlayItem>();
        List<OverlayItem> skillChallengeOverlayItems = new ArrayList<OverlayItem>();

        for(RegionFloor region : data.getRegions().values()) {
            for(MapFloor map : region.getMaps().values()) {
                for(Task task : map.getTasks()) {
                    OverlayItem overlayItem = new OverlayItem(task.getObjective() + " (" + task.getLevel() + ")", null,
                            TileSystem.PixelXYToLatLong(task.getCoord().getX(), task.getCoord().getY(),
                                    continent.getMaxZoom(), null));
                    overlayItem.setMarker(getResources().getDrawable(R.drawable.marker_task));
                    taskOverlayItems.add(overlayItem);
                }

                for(PointOfInterest poi : map.getPointsOfInterest()) {
                    OverlayItem overlayItem = new OverlayItem(poi.getName(), null,
                            TileSystem.PixelXYToLatLong(poi.getCoord().getX(), poi.getCoord().getY(),
                                    continent.getMaxZoom(), null));

                    if ("waypoint".equals(poi.getType())) {
                        overlayItem.setMarker(getResources()
                                .getDrawable(R.drawable.marker_waypoint));
                        waypointsOverlayItems.add(overlayItem);
                    } else if ("landmark".equals(poi.getType())) {
                        overlayItem.setMarker(getResources()
                                .getDrawable(R.drawable.marker_poi));
                        landmarkOverlayItems.add(overlayItem);
                    } else if ("vista".equals(poi.getType())) {
                        overlayItem.setMarker(getResources()
                                .getDrawable(R.drawable.marker_vista));
                        vistaOverlayItems.add(overlayItem);
                    } else if ("unlock".equals(poi.getType())) {
                        overlayItem.setMarker(getResources()
                                .getDrawable(R.drawable.marker_dungeon));
                        vistaOverlayItems.add(overlayItem);
                    } else {
                        Log.i("MapFragment", "Unsupported POI " + poi.getType());
                    }
                }

                for (SkillChallenge skillChallenge : map.getSkillChallenges()) {
                    OverlayItem overlayItem = new OverlayItem(null, null,
                            TileSystem.PixelXYToLatLong(skillChallenge.getCoord().getX(), skillChallenge.getCoord().getY(),
                                    continent.getMaxZoom(), null));
                    overlayItem.setMarker(getResources()
                            .getDrawable(R.drawable.marker_skillpoint));
                    skillChallengeOverlayItems.add(overlayItem);
                }
            }
        }

        List<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
        overlayItems.addAll(taskOverlayItems);
        overlayItems.addAll(waypointsOverlayItems);
        overlayItems.addAll(landmarkOverlayItems);
        overlayItems.addAll(vistaOverlayItems);
        overlayItems.addAll(skillChallengeOverlayItems);

        ItemizedIconOverlay<OverlayItem> overlay = new ItemizedIconOverlay<OverlayItem>(getActivity(), overlayItems, null) {
            @Override
            protected boolean onTap(int index) {
                showItemInfo(getItem(index).getTitle());
                return true;
            }
        };

        return overlay;
    }

    private void showItemInfo(String info) {
        ToastUtils.show(this.getActivity(), info);
    }


    public void updateView(Continent continent) {
        data = null;
        overlay = null;

        this.continent = continent;
        //https://tiles.guildwars2.com/{continent_id}/{floor}/{zoom}/{x}/{y}.jpg
        ITileSource tileSource = new XYTileSource(continent.getName() + " tiles",
                null, continent.getMinZoom(), continent.getMaxZoom(), 256, ".jpg",
                new String[] { String.format("https://tiles.guildwars2.com/%s/1/", continent.getId()) });

        mapview.setTileSource(tileSource);
        mapview.setMultiTouchControls(true);
        mapview.setUseDataConnection(true);
        mapview.setBuiltInZoomControls(true);
        //mapview.getController().setZoom(2);
        mapview.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent scrollEvent) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent zoomEvent) {
                displayOverlays();
                return false;
            }
        });
        mapview.getOverlayManager().clear();

        //load the continent floor
        getSherlockActivity().getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //SparseArray<Parcelable> arr = new SparseArray<Parcelable>();
        //mapview.saveHierarchyState(arr);
        //utState.putSparseParcelableArray("mapview", arr);

        outState.putInt("mapview_zoom", mapview.getZoomLevel());
        outState.putParcelable("mapview_center", (Parcelable) mapview.getMapCenter());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MapView view = (MapView) getActivity().findViewById(R.id.mapview);
        //if(savedInstanceState != null) {
        //    view.restoreHierarchyState(savedInstanceState.getSparseParcelableArray("mapview"));
        //}
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null) {
            mapview.getController().setZoom(savedInstanceState.getInt("mapview_zoom"));
            mapview.getController().setCenter((IGeoPoint) savedInstanceState.getParcelable("mapview_center"));
        } else {
            mapview.getController().setZoom(2);
        }
    }

    private void displayOverlays() {
        if(overlay == null) return;

        try {
            if (mapview.getZoomLevel() >= 4) {
                if (mapview.getOverlayManager().isEmpty()) {
                    mapview.getOverlayManager().add(overlay);
                }
            } else {
                mapview.getOverlayManager().remove(overlay);
            }
        } catch (Exception e) {
            Log.e(getTag(), "Error displaying markers", e);
        }
        mapview.invalidate();
    }
}
