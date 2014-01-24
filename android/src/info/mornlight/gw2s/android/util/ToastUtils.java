package info.mornlight.gw2s.android.util;

import static android.widget.Toast.LENGTH_LONG;
import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Utilities for displaying toast notifications
 */
public class ToastUtils {

    /**
     * Show the given message in a {@link android.widget.Toast}
     * <p>
     * This method may be called from any thread
     *
     * @param activity
     * @param message
     */
    public static void show(final Activity activity, final String message) {
        if (activity == null)
            return;

        if (TextUtils.isEmpty(message))
            return;

        final Application application = activity.getApplication();
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(application, message, LENGTH_LONG).show();
            }
        });
    }

    /**
     * Show the message with the given resource id in a {@link android.widget.Toast}
     * <p>
     * This method may be called from any thread
     *
     * @param activity
     * @param resId
     */
    public static void show(final Activity activity, final int resId) {
        if (activity == null)
            return;

        show(activity, activity.getString(resId));
    }

    /**
     * Show {@link android.widget.Toast} for exception
     * <p>
     * This given default message will be used if an message can not be derived
     * from the given {@link Exception}
     * <p>
     * This method may be called from any thread
     *
     * @param activity
     * @param e
     * @param defaultMessage
     */
    public static void show(final Activity activity, final Exception e,
            final int defaultMessage) {
        if (activity == null)
            return;

        String message = e.getMessage();
        //if (e instanceof RequestException)
        //    message = ((RequestException) e).formatErrors();
        //else
        //    message = null;

        if (TextUtils.isEmpty(message))
            message = activity.getString(defaultMessage);

        show(activity, message);
    }
}
