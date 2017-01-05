package com.meiku.dev.ui.recruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.ui.activitys.BaseFragmentActivity;
import com.meiku.dev.ui.mine.EditCompInfoActivity;
import com.meiku.dev.ui.morefun.MapFragment;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.HintDialogwk.DoOneClickListenerInterface;
import com.meiku.dev.views.MyViewpager;
import com.umeng.analytics.MobclickAgent;

/**
 * 招聘主页面
 * 
 */
public class RecruitMainActivity extends BaseFragmentActivity implements
		OnClickListener {
	//
	// /** tab图片未选中 */
	// private int[] imgUnSelected = new int[] { R.drawable.zp_fujinjianli,
	// R.drawable.zp_sousuojianli, R.drawable.zp_fabuzhiwei,
	// R.drawable.zp_wodezhaopin };
	// /** tab图片选中 */
	// private int[] imgSelected = new int[] {
	// R.drawable.zp_fujinjianlixuanzhong,
	// R.drawable.zp_sousuojianlixuanzhong,
	// R.drawable.zp_fabuzhiwei_xuanzhong, R.drawable.xuanzhong };

	/** 各标题 */
	private int[] titles = new int[] { R.string.tab_recruit_nearresume,
			R.string.tab_recruit_searchresume,
			R.string.tab_recruit_publishresume, R.string.tab_recruit_myresume };
	/** tab文字textview 的id */
	private int[] tvIDs = new int[] { R.id.txt_main_bottom_comm,
			R.id.txt_main_bottom_IM, R.id.txt_main_bottom_show,
			R.id.txt_main_bottom_mine };
	/** tab图片imageview 的id */
	private int[] ivIDs = new int[] { R.id.id_img_tab_comm,
			R.id.id_img_tab_Circle, R.id.id_img_tab_show, R.id.id_img_tab_mine };
	/** tab布局的id */
	private int[] layoutIDs = new int[] { R.id.layout_nearResume,
			R.id.layout_searchResume, R.id.layout_publishResume,
			R.id.layout_myResume };
	private TextView[] tab_tvs = new TextView[4];
	private ImageView[] tab_ivs = new ImageView[4];
	private RelativeLayout[] tab_layouts = new RelativeLayout[4];
	private int tabsize;
	private MyViewpager viewpager;
	private List<Object> listFragment = new ArrayList<Object>(4);
	private MyViewPagerAdapter adapter;
	private ClearEditText et_msg_search;
	private boolean hasCompany = false;
	/** 0：审核中, 1：已完成,2:不通过 */
	private String authPassFlag;
	private String authResult;

	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计activity
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 友盟统计activity
		MobclickAgent.onPause(this);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_recruitmain;
	}

	@Override
	public void initView() {
		regisBroadcast();
		initTitle();
		initTabs();
		initViewpager();
		int selectIndex = getIntent().getIntExtra("index", 0);
		showCurrentPageByIndex(selectIndex);
	}

	private void initTitle() {
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		findViewById(R.id.tv_productcatagory).setVisibility(View.GONE);
		et_msg_search.setHint("请输入职位、岗位、专业技能搜索");
		et_msg_search.setKeyListener(null);
		et_msg_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		findViewById(R.id.iv_exit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void initTabs() {
		tabsize = tvIDs.length;
		for (int i = 0; i < tabsize; i++) {
			tab_tvs[i] = (TextView) findViewById(tvIDs[i]);
			tab_ivs[i] = (ImageView) findViewById(ivIDs[i]);
			tab_layouts[i] = (RelativeLayout) findViewById(layoutIDs[i]);
			tab_layouts[i].setOnClickListener(this);
		}

	}

	private void initViewpager() {
		listFragment.add(new MapFragment());
		listFragment.add(new SearchResumeFragment());
		listFragment.add(new PublicPositionFragment());
		listFragment.add(new MyRecruitFragment());
		viewpager = (MyViewpager) findViewById(R.id.content_viewpager);
		adapter = new MyViewPagerAdapter(getSupportFragmentManager(),
				listFragment);
		viewpager.setAdapter(adapter);
		viewpager.setOffscreenPageLimit(4);
	}

	public class MyViewPagerAdapter extends FragmentPagerAdapter {

		private List<Object> arraylist;

		public MyViewPagerAdapter(FragmentManager fm, List<Object> listFragment) {
			super(fm);
			this.arraylist = listFragment;
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) arraylist
					.get(position);
			return fragment;
		}

		@Override
		public int getCount() {
			return arraylist == null ? 0 : arraylist.size();
		}
	}

	private void showCurrentPageByIndex(int index) {
		for (int i = 0; i < tabsize; i++) {
			tab_tvs[i].setText(titles[i]);
			tab_tvs[i].setTextColor(index == i ? Color.parseColor("#FF3499")
					: Color.parseColor("#000000"));
			tab_ivs[i]
					.setVisibility(index == i ? View.VISIBLE : View.INVISIBLE);
			tab_layouts[i].setBackgroundColor(index == i ? getResources()
					.getColor(R.color.transparent) : getResources().getColor(
					R.color.transparent));
		}
		viewpager.setCurrentItem(index, false);
	}

	@Override
	public void initValue() {
		CheckCompanyIsCertificate();
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "#验证公司#" + resp.getBody());
		switch (requestCode) {
		case reqCodeTwo:
			String companyEntityStr = resp.getBody().get("company").toString();
			LogUtil.d("hl", "检测认证company__" + companyEntityStr);
			if (Tool.isEmpty(companyEntityStr)
					|| companyEntityStr.length() == 2) {
				hasCompany = false;
			} else {
				CompanyEntity companyEntity = (CompanyEntity) JsonUtil
						.jsonToObj(CompanyEntity.class, companyEntityStr);
				hasCompany = true;
				authPassFlag = companyEntity.getAuthPass();// 0:审核中,1:已完成,2:不通过
				authResult = companyEntity.getAuthResult();// 提示语
			}
			break;
		default:
			break;
		}

	}

	private void gotoYanzheng() {
		final HintDialogwk dialog = new HintDialogwk(RecruitMainActivity.this,
				"您还不是企业用户，马上去认证", "Go >");
		dialog.setOneClicklistener(new DoOneClickListenerInterface() {

			@Override
			public void doOne() {
				startActivityForResult(new Intent(RecruitMainActivity.this,
						CompanyCertificationActivity.class).putExtra("flag",
						"3"), reqCodeOne);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_nearResume:
			showCurrentPageByIndex(0);
			break;
		case R.id.layout_searchResume:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(RecruitMainActivity.this);
				return;
			}
			if (hasCompany) {
				if (authPassFlag.equals("1")) {
					showCurrentPageByIndex(1);
				} else if (authPassFlag.equals("0")) {
					gotoCheckCompanyInfo();
				} else {
					showNotPassDialog();
				}
			} else {
				gotoYanzheng();
			}

			break;
		case R.id.layout_publishResume:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(RecruitMainActivity.this);
				return;
			}
			if (hasCompany) {
				if (authPassFlag.equals("1")) {
					showCurrentPageByIndex(2);
				} else if (authPassFlag.equals("0")) {
					gotoCheckCompanyInfo();
				} else {
					showNotPassDialog();
				}
			} else {
				gotoYanzheng();
			}
			break;
		case R.id.layout_myResume:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(RecruitMainActivity.this);
				return;
			}
			if (hasCompany) {
				if (authPassFlag.equals("1")) {
					showCurrentPageByIndex(3);
				} else if (authPassFlag.equals("0")) {
					gotoCheckCompanyInfo();
				} else {
					showNotPassDialog();
				}
			} else {
				gotoYanzheng();
			}
			break;
		default:
			break;
		}

	}

	private void showNotPassDialog() {
		final CommonDialog commonDialog = new CommonDialog(
				RecruitMainActivity.this, "提示", "您的企业信息未通过审核！\n 原因："
						+ authResult, "修改", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						commonDialog.dismiss();
						Intent intent = new Intent(RecruitMainActivity.this,
								EditCompInfoActivity.class);
						startActivity(intent);
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
	}

	private void gotoCheckCompanyInfo() {
		Intent intent = new Intent(RecruitMainActivity.this,
				EditCompInfoActivity.class);
		startActivity(intent);
	}

	/**
	 * 检测公司是否认证
	 */
	private void CheckCompanyIsCertificate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setBody(JsonUtil.Map2JsonObj(map));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_VERIFY));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, req, true);
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_COMPANY);
		filter.addAction(BroadCastAction.ACTION_RECRUIT_CHANGETAB);
		registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_IM_REFRESH_COMPANY.equals(intent
					.getAction())) {
				CheckCompanyIsCertificate();
			} else if (BroadCastAction.ACTION_RECRUIT_CHANGETAB.equals(intent
					.getAction())) {
				onClick(tab_layouts[1]);
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
