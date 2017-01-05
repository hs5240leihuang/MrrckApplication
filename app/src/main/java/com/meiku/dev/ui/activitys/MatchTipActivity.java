package com.meiku.dev.ui.activitys;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.StartDiagramVersionEntity;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.ListPostActivity;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.findjob.FindJobMainActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.NewMatchActivity;
import com.meiku.dev.ui.myshow.NewWorkDetailActivity;
import com.meiku.dev.ui.product.ProductDetailActivity;
import com.meiku.dev.ui.recruit.RecruitMainActivity;
import com.meiku.dev.utils.FileConstant;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.Tool;

/**
 * 显示比赛提示的广告页面
 */
public class MatchTipActivity extends Activity {

	private Button exit, btn;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			LogUtil.d("hlhl", "timeup___exit");
			finish();
			exitWithAnim();
			super.handleMessage(msg);
		}
	};
	/** 倒计时时间 */
	private Integer startSeconds = 2;
	private ImageView iv_matchAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MrrckApplication.getInstance().addActivity(getClass().getName(), this);
		setContentView(R.layout.activity_guanggao);
		btn = (Button) findViewById(R.id.btn_join);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MatchTipActivity.this,
						NewMatchActivity.class).putExtra("matchId", getIntent()
						.getIntExtra("matchId", -1)));
				finish();
			}
		});
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				exit.setEnabled(false);
				handler.removeMessages(0);
				finish();
				exitWithAnim();
				LogUtil.d("hlhl", "onClick___exit");
			}
		});
		iv_matchAd = (ImageView) findViewById(R.id.iv_matchAd);
		String tipADUrl = (String) PreferHelper.getSharedParam(
				ConstantKey.SP_TIPAD_URL, "");
		// 如果当前版本号的赛事图存在则使用
		if (!Tool.isEmpty(tipADUrl)) {
			String matchADPath = FileConstant.SD_PATH
					+ FileConstant.MATCHAD_PATH + "home_ad.png";
			if (FileHelper.isFileExist(new File(matchADPath))) {// 赛事广告图存在
				// iv_matchAd.setImageURI(Uri.parse("file://" + matchADPath));
				try {
					BitmapDrawable logoDrawable = new BitmapDrawable(
							FileHelper.getLoacalBitmap(matchADPath));
					iv_matchAd.setImageDrawable(logoDrawable);
				} catch (Exception e) {
					e.printStackTrace();
					iv_matchAd.setBackgroundResource(R.drawable.guanggao);
				}
			} else {
				PreferHelper.setSharedParam(ConstantKey.SP_TIPAD_URL, "");
				iv_matchAd.setBackgroundResource(R.drawable.guanggao);
			}
		} else {
			iv_matchAd.setBackgroundResource(R.drawable.guanggao);
		}

		// 获取赛事广告按钮详细配置、
		String startDiagramVersionMatchInfo = (String) PreferHelper
				.getSharedParam(ConstantKey.SP_MATCHAD_INFO, "");
		LogUtil.d("hl", "-->" + startDiagramVersionMatchInfo);
		if (!Tool.isEmpty(startDiagramVersionMatchInfo)) {
			final StartDiagramVersionEntity sdv = (StartDiagramVersionEntity) JsonUtil
					.jsonToObj(StartDiagramVersionEntity.class,
							startDiagramVersionMatchInfo);
			if (sdv != null) {
				int showFlag = sdv.getButtonFlag();
				startSeconds = sdv.getStartSeconds();
				if (1 == showFlag) {// 0 不显示 1是显示，2点击图片跳转
					btn.setVisibility(View.VISIBLE);
					btn.setText(sdv.getButtonName());
					btn.setOnClickListener(new DoGotoClick(sdv));
				} else if (2 == showFlag) {
					btn.setVisibility(View.GONE);
					iv_matchAd.setOnClickListener(new DoGotoClick(sdv));
				} else {
					btn.setVisibility(View.GONE);
				}
			}
		}
		handler.sendEmptyMessageDelayed(0, startSeconds * 1000);
	}

	private void exitWithAnim() {
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		if (version > 5) {
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
	}

	public class DoGotoClick implements OnClickListener {

		private StartDiagramVersionEntity sdv;

		public DoGotoClick(StartDiagramVersionEntity sdv) {
			this.sdv = sdv;
		}

		@Override
		public void onClick(View arg0) {
			if (1 == sdv.getIsClientApp()) {// 1:H5网页打开
				Intent i = new Intent(MatchTipActivity.this,
						WebViewActivity.class);
				i.putExtra("webUrl", sdv.getFunctionUrl());
				startActivity(i);
			} else {// 0:客户端应用打开
				String FunctionUrl = sdv.getFunctionUrl();
				if (FunctionUrl.contains("APP_MODEL_MATCH_LIST")) {// APP_MODEL_MATCH_LIST?matchId=5比赛
					startActivity(new Intent(MatchTipActivity.this,
							NewMatchActivity.class).putExtra("matchId",
							getIntent().getIntExtra("matchId", -1)));
				} else if (FunctionUrl.contains("APP_MODEL_POSTS_DETAIL")) {// APP_MODEL_POSTS_DETAIL?postsId=5跳转帖子详情
					if (FunctionUrl.contains("=")) {
						String postId = FunctionUrl.split("=")[1];
						startActivity(new Intent(MatchTipActivity.this,
								PostDetailNewActivity.class).putExtra(
								ConstantKey.KEY_POSTID, postId));
					}
				} else if (FunctionUrl.contains("APP_MODEL_BOARD")) {// APP_MODEL_BOARD?boardId=5跳转板块
					if (FunctionUrl.contains("=")) {
						String boardId = FunctionUrl.split("=")[1];
						startActivity(new Intent(MatchTipActivity.this,
								ListPostActivity.class).putExtra(
								ConstantKey.KEY_BOARDID, boardId));
					}
				} else if (FunctionUrl.contains("APP_MODEL_SIGNUP_DETAIL")) {// APP_MODEL_SIGNUP_DETAIL?signupId=5跳转参赛作品详情
					if (FunctionUrl.contains("=")) {
						String SignupId = FunctionUrl.split("=")[1];
						startActivity(new Intent(MatchTipActivity.this,
								NewWorkDetailActivity.class).putExtra(
								"SignupId", Integer.parseInt(SignupId)));
					}
				} else if (FunctionUrl.contains("APP_MODEL_PRODUCT_DETAIL")) {// APP_MODEL_PRODUCT_DETAIL?productId=5
					// 找产品详情
					if (FunctionUrl.contains("=")) {
						String productId = FunctionUrl.split("=")[1];
						startActivity(new Intent(MatchTipActivity.this,
								ProductDetailActivity.class).putExtra(
								"productId", productId));
					}
				} else if (FunctionUrl.contains("APP_MODEL_MAP_LIST")) {
					// 招聘主页面
					startActivity(new Intent(MatchTipActivity.this,
							RecruitMainActivity.class));
				} else {

				}

			}
			finish();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MrrckApplication.getInstance().removeActivity(getClass().getName());
	}

}
