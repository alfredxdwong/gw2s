package info.mornlight.gw2s.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import info.mornlight.gw2s.android.R;
import info.mornlight.gw2s.android.util.ToastUtils;
import info.mornlight.gw2s.android.util.ViewUtils;

/***
 * The base fragment provides content view, progress bar, and empty view support
 * @author alfred
 *
 * @param <D>
 * @param <V>
 */
public abstract class BaseFragment<D, V extends View> extends RoboSherlockFragment {
    protected V contentView;
	protected D data;
	
	protected TextView emptyView;
	protected boolean contentShown;
	
	protected ProgressBar progressBar;
	
	protected boolean hasContent() {
		return data != null;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (hasContent())
            setContentShown(true, false);
    }
	
	protected abstract V onCreateContentView(LayoutInflater inflater, Bundle savedInstanceState);
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup v = (ViewGroup) inflater.inflate(R.layout.base_fragment, null);
        
        //setup inner view
    	contentView = onCreateContentView(inflater, savedInstanceState);
    	
    	contentView.setVisibility(View.GONE);
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        v.addView(contentView, 0, params);
        
        return v;
    }
	
	@Override
    public void onDestroyView() {
        contentShown = false;
        emptyView = null;
        progressBar = null;
        contentView = null;

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        
        emptyView = (TextView) view.findViewById(android.R.id.empty);
    }
    
    protected boolean isUsable() {
        return getActivity() != null;
    }
    
    protected String getErrorMessage(Exception exception) {
    	return exception.getMessage();
    }
    
    protected void showError(Exception exception, String message) {
        ToastUtils.show(getActivity(), message);
    }
	
	protected void showContentView() {
        setContentShown(true, isResumed());
    }
	
	public BaseFragment<D, V> setContentShown(final boolean shown) {
        return setContentShown(shown, true);
    }
	
	public V getContentView() {
        return contentView;
    }
	
	private BaseFragment<D, V> fadeIn(final View view, final boolean animate) {
        if (view != null)
            if (animate)
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
            else
                view.clearAnimation();
        return this;
    }

    private BaseFragment<D, V> show(final View view) {
        ViewUtils.setGone(view, false);
        return this;
    }

    private BaseFragment<D, V> hide(final View view) {
        ViewUtils.setGone(view, true);
        return this;
    }
    
    public BaseFragment<D, V> setContentShown(final boolean shown, final boolean animate) {
        if (!isUsable())
            return this;

        if (shown == contentShown) {
            if (shown)
                // List has already been shown so hide/show the empty view with
                // no fade effect
                if (!hasContent())
                    hide(contentView).show(emptyView);
                else
                    hide(emptyView).show(contentView);
            return this;
        }

        contentShown = shown;

        if (shown)
            if (hasContent())
                hide(progressBar).hide(emptyView).fadeIn(contentView, animate)
                        .show(contentView);
            else
                hide(progressBar).hide(contentView).fadeIn(emptyView, animate)
                        .show(emptyView);
        else
            hide(contentView).hide(emptyView).fadeIn(progressBar, animate)
                    .show(progressBar);

        return this;
}
    
    protected BaseFragment<D, V> setEmptyText(final String message) {
        if (emptyView != null)
            emptyView.setText(message);
        return this;
    }

    /**
     * Set empty text on list fragment
     *
     * @param resId
     * @return this fragment
     */
    protected BaseFragment<D, V> setEmptyText(final int resId) {
        if (emptyView != null)
            emptyView.setText(resId);
        return this;
    }
}
