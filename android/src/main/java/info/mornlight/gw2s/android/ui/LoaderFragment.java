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
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.view.View;

/**
 *
 *
 * @param <D>
 */
public abstract class LoaderFragment<D, V extends View> extends BaseFragment<D, V>
        implements LoaderCallbacks<D> {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
    }

    public void onLoadFinished(Loader<D> loader, D data) {
        Exception exception = getException(loader);
        if (exception != null) {
            showError(exception, getErrorMessage(exception));
            
            onLoadFailed(loader, exception);
            
            showContentView();
            return;
        } else {
            //this.data = data;

            onLoadSucceed(loader, data);

            showContentView();
        }
    }
    
    protected void onLoadFailed(Loader<D> loader, Exception error) {
    }
    
    protected void onLoadSucceed(Loader<D> loader, D data) {
    }

    @Override
    public void onLoaderReset(Loader<D> loader) {
        // Intentionally left blank
    }

    protected Exception getException(final Loader<D> loader) {
        if (loader instanceof ThrowableLoader)
            return ((ThrowableLoader<D>) loader).clearException();
        else
            return null;
    }
}
