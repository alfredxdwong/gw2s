package info.mornlight.gw2s.android.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class ItemListFragment<I, V extends ListView> extends RefreshableFragment<List<I>, V> {
    @Override
    protected V onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState) {
        return (V) new ListView(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configureList(getActivity(), contentView);
    }

    /**
     * Configure list after view has been created
     *
     * @param activity
     * @param listView
     */
    protected void configureList(Activity activity, ListView listView) {
        listView.setAdapter(createAdapter());
    }
    
    @Override
    protected void onLoadSucceed(Loader<List<I>> loader, List<I> data) {
        getListAdapter().getWrappedAdapter().setItems(data);
    }

    /**
     * Create adapter to display items
     *
     * @return adapter
     */
    protected HeaderFooterListAdapter<BaseAdapter> createAdapter() {
        BaseAdapter wrapped = createAdapter(data);
        return new HeaderFooterListAdapter<BaseAdapter>(contentView, wrapped);
    }

    /**
     * Create adapter to display items
     *
     * @param items
     * @return adapter
     */
    protected abstract BaseAdapter createAdapter(
            final List<I> items);

    /**
     * Get list adapter
     *
     * @return list adapter
     */
    @SuppressWarnings("unchecked")
    protected HeaderFooterListAdapter<ItemListAdapter<I, ? extends ItemView>> getListAdapter() {
        if (contentView != null)
            return (HeaderFooterListAdapter<ItemListAdapter<I, ? extends ItemView>>) contentView
                    .getAdapter();
        else
            return null;
    }
}
