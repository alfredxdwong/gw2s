package info.mornlight.gw2s.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import roboguice.util.RoboAsyncTask;


public abstract class IndicatedAsyncTask<D> extends RoboAsyncTask<D> {
    private int messageRes;
    private Activity activity;
    AlertDialog dialog;
    
    protected IndicatedAsyncTask(Activity activity, int messageRes) {
        super(activity);
        this.messageRes = messageRes;
        this.activity = activity;
    }
    
    @Override
    protected void onPreExecute() throws Exception {
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
    protected void onFinally() throws RuntimeException {
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}