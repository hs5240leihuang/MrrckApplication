package com.meiku.dev.ui.community;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.meiku.dev.R;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.views.ClearEditText;
import com.umeng.analytics.MobclickAgent;

public class SearchPostActivity extends BaseActivity {
	
	private ClearEditText et_msg_search;
	
	private TextView search_text;
	
	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_search_post;
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }
    
	@Override
	public void initView() {
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		search_text = (TextView) findViewById(R.id.search_text);
	}

	@Override
	public void initValue() {
		
	}

	@Override
	public void bindListener() {
		et_msg_search.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					Intent intent = new Intent();
					intent.putExtra("title", et_msg_search.getText().toString());
					setResult(RESULT_OK, intent);
					finish();
				}
				return true;

			}
		});
		search_text.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("title", et_msg_search.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
