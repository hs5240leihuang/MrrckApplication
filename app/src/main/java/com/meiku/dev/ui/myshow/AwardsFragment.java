package com.meiku.dev.ui.myshow;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.RuleBean;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;

/**
 * 奖项设置
 * 
 * 
 */
public class AwardsFragment extends BaseFragment {

	private WebView webView;
	private WebSettings settings;
	private TextView errorTip;
	private String postsId;
	private String clientActiveRuleUrl;
	private View layout_view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_awardssetting, null);
		initView();
		return layout_view;
	}

	// JS调用本地方法
	final class JavaScriptInterface {
		@JavascriptInterface
		public void getDatasFromJS(String datas) {
		}
	}

	public void initView() {
		layout_view.findViewById(R.id.title_bar).setVisibility(View.GONE);
		webView = (WebView) layout_view.findViewById(R.id.webview);
		settings = webView.getSettings();
		settings.setDomStorageEnabled(true);
		settings.setAllowFileAccess(true);
		settings.setPluginState(WebSettings.PluginState.ON);
		settings.setJavaScriptEnabled(true);
		// settings.setSupportZoom(true);//支持缩放
		// settings.setBuiltInZoomControls(true);//缩放控件
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setTextSize(WebSettings.TextSize.NORMAL);
		webView.addJavascriptInterface(new JavaScriptInterface(), "mrrck");// 供JS调用本地方法的接口
		webView.setWebChromeClient(new WebChromeClient());

		errorTip = (TextView) layout_view.findViewById(R.id.error);
		errorTip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getData();
			}
		});
	}

	private void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("postsId", postsId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_VOTE_RULE));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(0, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	public void initValue() {
		Bundle bundle = getArguments();
		postsId = bundle.getInt("postsId") + "";
		LogUtil.d("hl", "奖项设置postsId=" + postsId);
		getData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody());
		if ((resp.getBody().get("postsGuide") + "").length() > 2) {
			RuleBean data = (RuleBean) JsonUtil.jsonToObj(RuleBean.class, resp
					.getBody().get("postsGuide").toString());
			clientActiveRuleUrl = data.getClientActivePrizeUrl();
		}

		if (!Tool.isEmpty(clientActiveRuleUrl)
				&& clientActiveRuleUrl.contains("postsId")) {
			webView.loadUrl(clientActiveRuleUrl);
			LogUtil.d("hl", "奖项设置的完整地址  " + clientActiveRuleUrl);
		} else {
			errorTip.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		errorTip.setVisibility(View.VISIBLE);
	}

}
