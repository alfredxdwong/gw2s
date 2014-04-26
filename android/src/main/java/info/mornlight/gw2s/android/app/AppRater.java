package info.mornlight.gw2s.android.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by alfred on 4/26/14.
 */
public class AppRater {
    private final static int DAYS_UNTIL_PROMPT = 5;
    private final static int LAUNCH_UNTIL_PROMPT = 7;

    private final String title;
    private final String packageName;

    public AppRater(String title, String packageName) {
        this.title = title;
        this.packageName = packageName;
    }

    public void appLaunched(Context context){
        SharedPreferences prefs = context.getSharedPreferences("rate_app", 0);
        if (prefs.getBoolean("dontshowagain", false)){return;}

        SharedPreferences.Editor editor = prefs.edit();

        //Add to launch Counter
        long launchCount = prefs.getLong("launch_count", 0) +1;
        editor.putLong("launch_count", launchCount);

        //Get Date of first launch
        Long dateFirstLaunch = prefs.getLong("date_first_launch",0);
        if (dateFirstLaunch == 0){
            dateFirstLaunch = System.currentTimeMillis();
            editor.putLong("date_first_launch", dateFirstLaunch);
        }

        //Wait at least X days to launch
        if (launchCount >= LAUNCH_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= dateFirstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)){
                showRateDialog(context, editor);
            }
        }

        editor.commit();
    }

    public void showRateDialog(final Context context, final SharedPreferences.Editor editor){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            String message = "If you enjoy using "
                    + title
                    + ", please take a moment to rate the app. Thank you for your support!";

        builder.setMessage(message)
                .setTitle("Rate " + title)
                .setIcon(context.getApplicationInfo().icon)
                .setCancelable(false)
                .setPositiveButton("Rate Now",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                editor.putBoolean("dontshowagain", true);
                                editor.commit();
                                context.startActivity(new Intent(
                                        Intent.ACTION_VIEW, Uri
                                        .parse("market://details?id="
                                                + packageName)));
                                dialog.dismiss();
                            }
                        })
                .setNeutralButton("Later",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        })
                .setNegativeButton("No, Thanks",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (editor != null) {
                                    editor.putBoolean("dontshowagain", true);
                                    editor.commit();
                                }
                                dialog.dismiss();

                            }
                        });
        Dialog dialog = builder.create();

        dialog.show();
    }
}
