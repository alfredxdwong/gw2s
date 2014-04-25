package info.mornlight.gw2s.android.ui;

import static android.app.Activity.RESULT_CANCELED;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import roboguice.fragment.RoboDialogFragment;

/**
 * Base dialog fragment helper
 */
public abstract class BaseDialogFragment extends RoboDialogFragment implements
        OnClickListener {

    /**
     * Dialog message
     */
    protected static final String ARG_TITLE = "title";

    /**
     * Dialog message
     */
    protected static final String ARG_MESSAGE = "message";

    /**
     * Request code
     */
    protected static final String ARG_REQUEST_CODE = "requestCode";

    protected DialogResultListener listener;

    protected static void show(RoboSherlockFragmentActivity activity,
                               BaseDialogFragment fragment, String tag) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment current = manager.findFragmentByTag(tag);
        if (current != null)
            transaction.remove(current);
        transaction.addToBackStack(null);

        fragment.show(manager, tag);
    }

    /**
     * Create bundle with standard arguments
     *
     * @param title
     * @param message
     * @param requestCode
     * @return bundle
     */
    protected static Bundle createArguments(final String title,
                                            final String message, final int requestCode) {
        Bundle args = new Bundle();
        args.putInt(ARG_REQUEST_CODE, requestCode);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        return args;
    }

    /**
     * Call back to the activity with the dialog result
     *
     * @param resultCode
     */
    protected void onResult(int resultCode) {
        DialogResultListener listener = this.listener;
        if(listener != null) {
            listener.onDialogResult(getArguments()
                    .getInt(ARG_REQUEST_CODE), resultCode, getArguments());
        } else {
            ((DialogResultListener) getActivity()).onDialogResult(getArguments()
                    .getInt(ARG_REQUEST_CODE), resultCode, getArguments());
        }
    }

    /**
     * Get title
     *
     * @return title
     */
    protected String getTitle() {
        return getArguments().getString(ARG_TITLE);
    }

    /**
     * Get message
     *
     * @return mesage
     */
    protected String getMessage() {
        return getArguments().getString(ARG_MESSAGE);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onResult(RESULT_CANCELED);
    }

    protected abstract View onCreateContentView(AlertDialog dialog, LayoutInflater inflater);

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getTitle())
                .setMessage(getMessage())
                .setCancelable(true)
                .setOnCancelListener(this);
        AlertDialog dialog = builder.create();

        View view = onCreateContentView(dialog, getActivity().getLayoutInflater());
        dialog.setView(view);

        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
