package info.mornlight.gw2s.android.billing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.vending.billing.IInAppBillingService;
import info.mornlight.gw2s.android.app.App;
import info.mornlight.gw2s.android.app.RequestCodes;
import info.mornlight.gw2s.android.app.SkuState;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alfred on 4/24/14.
 */
public class PurchasingHelper {
    private static final String TAG = "PurchasingHelper";
    private IInAppBillingService service;
    ServiceConnection serviceConn;
    private String publicKey;
    private Context context;
    private static final int API_VERSION = 3;
    private static final String IN_APP = "inapp"; //for one-time purchases

    public static final String developerPayload = "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ";

    // Keys for the responses from InAppBillingService
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";

    // Billing response codes
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;

    public interface ServiceListener {
        void onServiceConnected();
        void onServiceDisconnected();
    }

    private ServiceListener listener;

    public void init(Context ctx, final ServiceListener listener) {
        context = ctx;
        publicKey = retrievePublicKey();
        this.listener = listener;

        serviceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
                if (listener != null) {
                    listener.onServiceDisconnected();
                }
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = IInAppBillingService.Stub.asInterface(binder);

                if (listener != null) {
                    listener.onServiceConnected();
                }
            }
        };

        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        context.bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
    }

    public void uninit() {
        if (service != null) {
            context.unbindService(serviceConn);
        }
    }

    public Set<String> queryOwnedItems() throws RemoteException {
        Bundle ownedItems = service.getPurchases(API_VERSION, context.getPackageName(), IN_APP, null);

        int response = ownedItems.getInt(RESPONSE_CODE);
        if (response == BILLING_RESPONSE_RESULT_OK) {
            ArrayList<String> ownedSkus = ownedItems.getStringArrayList(RESPONSE_INAPP_ITEM_LIST);
            return new HashSet<String>(ownedSkus);
        } else {
            throw new RuntimeException("Billing service error: " + response);
        }
    }

    public void purchaseItem(Activity activity, String sku) throws RemoteException, IntentSender.SendIntentException {
        Bundle buyIntentBundle = service.getBuyIntent(API_VERSION, context.getPackageName(),
                sku, IN_APP, developerPayload);
        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
        activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
                RequestCodes.PURCHASE_SKU, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                Integer.valueOf(0));
    }

    public void processPurchaseResult(Intent data) throws JSONException {
        int responseCode = data.getIntExtra(RESPONSE_CODE, 0);
        String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
        String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);

            try {
                JSONObject jo = new JSONObject(purchaseData);
                String sku = jo.getString("productId");
                App app = App.instance();
                app.putSkuState(sku, SkuState.Purchased);
            }
            catch (JSONException e) {
                Log.e(TAG, "Parse purchase ata failed.", e);
                throw e;
            }
    }

    private String retrievePublicKey() {
        //TODO encrypt the key
        return "fakepublickey";
    }
}
