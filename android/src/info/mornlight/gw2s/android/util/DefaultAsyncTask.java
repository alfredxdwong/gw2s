package info.mornlight.gw2s.android.util;

import roboguice.util.RoboAsyncTask;
import android.app.Activity;


public abstract class DefaultAsyncTask<D> extends RoboAsyncTask<D> {
    private int errorRes;
    private Activity activity;
    
    protected DefaultAsyncTask(Activity activity, int errorRes) {
        super(activity);
        this.errorRes = errorRes;
        this.activity = activity;
    }
    
    @Override
    protected void onException(Exception e) throws RuntimeException {
        ToastUtils.show(activity, e, errorRes);
    }

    @Override
    protected void onSuccess(D t) throws Exception {
    }
}