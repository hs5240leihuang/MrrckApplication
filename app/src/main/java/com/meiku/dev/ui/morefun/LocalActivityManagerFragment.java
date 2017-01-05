package com.meiku.dev.ui.morefun;

import android.app.LocalActivityManager;
import android.os.Bundle;

import com.meiku.dev.ui.fragments.BaseFragment;

public class LocalActivityManagerFragment extends BaseFragment {

    private static final String KEY_STATE_BUNDLE = "localActivityManagerState";
    private LocalActivityManager mLocalActivityManager;
    
    protected LocalActivityManager getLocalActivityManager() {
        return mLocalActivityManager;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle state = null;
        if(savedInstanceState != null) {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }
        mLocalActivityManager = new LocalActivityManager(getActivity(), true);
        mLocalActivityManager.dispatchCreate(state);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_STATE_BUNDLE, mLocalActivityManager.saveInstanceState());
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(getActivity().isFinishing());
    }    
    
    @Override
    public void onStop() {
        super.onStop();
        mLocalActivityManager.dispatchStop();
    }
    
    @Override
    public void onDestroy() {        
        super.onDestroy();        
        mLocalActivityManager.dispatchDestroy(getActivity().isFinishing());
    }

	@Override
	public void initValue() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}
}
