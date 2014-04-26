package info.mornlight.gw2s.android.app;

import android.view.View;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.billing.PurchasingHelper;

public class BaseActivity extends RoboSherlockFragmentActivity {
    protected void updateAd() {
        AdView adView = (AdView)this.findViewById(R.id.adView);
        if (adView == null) return;

        App app = App.instance();
        if(app.getSkuState(PurchasingHelper.SKU_AD_REMOVAL) == SkuState.Purchased) {
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

    @Override
    protected void onStart() {
        super.onStart();

        EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EasyTracker.getInstance().activityStop(this);
    }
}
