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

import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import info.mornlight.gw2s.android.R;

/**
 * Base fragment for fragments in main activity
 * 
 *
 * @param <D>
 */
public abstract class RefreshableFragment<D, V extends View> extends LoaderFragment<D, V> {
	
	protected static final String FORCE_REFRESH = "forceRefresh";

    /**
     * @param args
     *            bundle passed to the loader by the LoaderManager
     * @return true if the bundle indicates a requested forced refresh of the
     *         items
     */
    protected static boolean isForceRefresh(Bundle args) {
        return args != null && args.getBoolean(FORCE_REFRESH, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isUsable())
            return false;
        switch (item.getItemId()) {
        case R.id.m_refresh:
            forceRefresh();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Force a refresh of the items displayed ignoring any cached items
     */
    protected void forceRefresh() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(FORCE_REFRESH, true);
        refresh(bundle);
    }

    /**
     * Refresh the fragment's list
     */
    public void refresh() {
        refresh(null);
    }

    protected void refresh(final Bundle args) {
        if (!isUsable())
            return;

        getLoaderManager().restartLoader(0, args, this);
    }

    /**
     * Refresh the list with the progress bar showing
     */
    protected void refreshWithProgress() {
        //clearContent();
        
        setContentShown(false);
        refresh();
    }
}
