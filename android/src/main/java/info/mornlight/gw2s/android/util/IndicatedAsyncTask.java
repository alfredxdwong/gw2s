package info.mornlight.gw2s.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

public abstract class IndicatedAsyncTask<Param, Result> extends DefaultAsyncTask<Param, Result> {
    private int messageRes;
    private Activity activity;
    AlertDialog dialog;
    
    protected IndicatedAsyncTask(Activity activity, int messageRes) {
        super(activity, messageRes);
    }
    
    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage(activity.getString(messageRes));
        //dialog = ProgressDialog.create(activity, messageRes);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                IndicatedAsyncTask.this.cancel(false);
            }
        });
        dialog.show();
    }

    @Override
    protected void onPostExecute(Result result) {
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        super.onPostExecute(result);
    }
}