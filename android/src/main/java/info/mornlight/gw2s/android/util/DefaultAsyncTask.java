package info.mornlight.gw2s.android.util;

import android.app.Activity;
import android.os.AsyncTask;


public abstract class DefaultAsyncTask<Param, Result> extends AsyncTask<Param, Integer, Result> {
    private int errorRes;
    private Activity activity;
    private Throwable error;
    
    protected DefaultAsyncTask(Activity activity, int errorRes) {
        this.errorRes = errorRes;
        this.activity = activity;
    }

    public abstract Result call() throws Exception;
    protected void onSuccess(Result result) {};
    protected void onError(Throwable error) {};

    @Override
    protected Result doInBackground(Param... params) {
        try {
            return call();
        } catch(Throwable e) {
            error = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if(error == null) {
            onSuccess(result);
        } else {
            onError(error);
        }
    }
}