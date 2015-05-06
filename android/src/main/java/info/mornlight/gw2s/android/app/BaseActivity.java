package info.mornlight.gw2s.android.app;

import android.app.Activity;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import info.mornlight.gw2s.android.R;

public class BaseActivity extends Activity {
    private static Tracker tracker;

    private Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.analytics);
        }

        return tracker;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Tracker t = getTracker();
        t.setScreenName(this.getClass().getName());

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
