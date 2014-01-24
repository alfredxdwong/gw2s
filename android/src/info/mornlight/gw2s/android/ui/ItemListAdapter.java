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

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * List adapter for items of a specific type
 *
 * @param <I>
 * @param <V>
 */
public abstract class ItemListAdapter<I, V extends ItemView<I>> extends
        BaseAdapter {

    protected final LayoutInflater inflater;

    protected final int viewId;

    protected List<I> elements;

    /**
     * Create empty adapter
     *
     * @param viewId
     * @param inflater
     */
    public ItemListAdapter(final int viewId, final LayoutInflater inflater) {
        this(viewId, inflater, null);
    }

    /**
     * Create adapter
     *
     * @param viewId
     * @param inflater
     * @param elements
     */
    public ItemListAdapter(final int viewId, final LayoutInflater inflater,
            final List<I> elements) {
        this.viewId = viewId;
        this.inflater = inflater;
        if (elements != null)
            this.elements = elements;
        else
            this.elements = new ArrayList<I>();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public int getCount() {
        return elements.size();
    }

    @SuppressWarnings("unchecked")
    public I getItem(int position) {
        return elements.get(position);
    }

    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    /**
     * Set items
     *
     * @param items
     * @return items
     */
    public ItemListAdapter<I, V> setItems(List<I> items) {
        if (items != null)
            elements = items;
        else
            elements = new ArrayList<I>();
        notifyDataSetChanged();
        return this;
    }
    
    public List<I> getItems() {
        return elements;
    }

    /**
     * Update view to display item
     *
     * @param position
     * @param view
     * @param item
     */
    protected void update(int position, V view, I item) {
        view.update(item);
    }

    protected abstract V createView(int position, View view);

    public View getView(final int position, View convertView, final ViewGroup parent) {
        @SuppressWarnings("unchecked")
        V view = convertView != null ? (V) convertView.getTag() : null;
        if (view == null) {
            convertView = inflater.inflate(viewId, null);
            view = createView(position, convertView);
            convertView.setTag(view);
        }
        update(position, view, getItem(position));
        return convertView;
    }
}
