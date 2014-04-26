package info.mornlight.gw2s.android.app;

import android.view.View;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.billing.InAppProducts;

public class BaseActivity extends RoboSherlockFragmentActivity {
    private static Tracker tracker;

    protected void updateAd() {
        AdView adView = (AdView)this.findViewById(R.id.adView);
        if (adView == null) return;

        App app = App.instance();
        if(app.getSkuState(InAppProducts.AdRemoval) == SkuState.Purchased) {
            adView.setVisibility(View.GONE);
        } else {
            adView.setVisibility(View.VISIBLE);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                            //.addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // My Galaxy Nexus test phone
                    .build();
            adView.loadAd(request);
        }
    }

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
