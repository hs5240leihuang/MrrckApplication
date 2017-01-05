package com.meiku.dev.ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.meiku.dev.R;

public class AgreeMentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_agreement);
		WebView webView = (WebView) this.findViewById(R.id.webview);
		int type = getIntent().getIntExtra("type", 0);
		if (type == 0) {
			webView.loadUrl("file:///android_asset/agreement.html");
		} else if (type == 1) {
			webView.loadUrl("file:///android_asset/decorationagreements.html");
		}
	}
}
