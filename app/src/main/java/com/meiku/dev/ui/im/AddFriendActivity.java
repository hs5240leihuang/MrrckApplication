package com.meiku.dev.ui.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.meiku.dev.R;
import com.meiku.dev.ui.chat.AddFriendsFromPhoneBookActivity;
import com.zxing.CaptureActivity;

/**
 * 添加朋友页面（搜索，添加手机联系人，扫一扫）
 */
public class AddFriendActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addfriend);
		findViewById(R.id.linsao).setOnClickListener(this);
		findViewById(R.id.linaddfriend).setOnClickListener(this);
		findViewById(R.id.linsearch).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.linsao:
			Intent linsaoIntent = new Intent(this, CaptureActivity.class);
			startActivity(linsaoIntent);
			break;
		case R.id.linaddfriend:
			Intent linaddIntent = new Intent(this,
					AddFriendsFromPhoneBookActivity.class);
			startActivity(linaddIntent);
			break;
		case R.id.linsearch:
			Intent linsearchIntent = new Intent(this,
					SearchFriendActivity.class);
			startActivity(linsearchIntent);
			break;
		default:
			break;
		}
	}

}
