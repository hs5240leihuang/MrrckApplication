package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.DefriendEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.login.MkLoginActivity;
import com.meiku.dev.ui.login.ReloginTipActivity;
import com.meiku.dev.ui.morefun.MrrckResumeActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.recruit.RecruitMainActivity;
import com.meiku.dev.ui.service.CompanyCertificationActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.QRcodeUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.HintDialogwk.DoOneClickListenerInterface;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.MyRoundDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.zxing.CaptureActivity;

public class PersonFragment extends BaseFragment implements OnClickListener {

	private View layout_view;
	private ImageView img_qrcode;
	/** userPrivacy隐私设置 */
	private TextView txt_name, txt_job;
	private LinearLayout layout_logined, layout_editcomp;
	private ImageView scanCode;// 扫码
	private LinearLayout layout_perfectInfo;
	private LinearLayout lin_head;

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_person, null);
		registerBoradcastReceiver();
		initView();
		initAnim();
		return layout_view;
	}

	private void initAnim() {
		Animation animation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.item_list_anim);
		LayoutAnimationController layoutAnimationController = new LayoutAnimationController(
				animation);
		layoutAnimationController.setInterpolator(new AccelerateInterpolator());
		layoutAnimationController.setDelay(0.2f);
		layoutAnimationController
				.setOrder(LayoutAnimationController.ORDER_NORMAL);
		((LinearLayout) layout_view.findViewById(R.id.layoutBody))
				.setLayoutAnimation(layoutAnimationController);
	}

	private void initView() {
		layout_perfectInfo = (LinearLayout) layout_view
				.findViewById(R.id.layout_perfectInfo);
		layout_perfectInfo.setOnClickListener(this);
		img_qrcode = (ImageView) layout_view.findViewById(R.id.img_qrcode);
		lin_head = (LinearLayout) layout_view.findViewById(R.id.lin_head);
		lin_head.setOnClickListener(this);
		txt_name = (TextView) layout_view.findViewById(R.id.txt_name);
		txt_job = (TextView) layout_view.findViewById(R.id.txt_job);

		layout_logined = (LinearLayout) layout_view
				.findViewById(R.id.layout_logined);
		scanCode = (ImageView) layout_view.findViewById(R.id.iv_code);
		scanCode.setOnClickListener(this);
		layout_view.findViewById(R.id.layout_myqrcode).setOnClickListener(this);
		layout_view.findViewById(R.id.layout_myhome).setOnClickListener(this);
		layout_view.findViewById(R.id.layout_citiao).setOnClickListener(this);
		layout_view.findViewById(R.id.layout_resume).setOnClickListener(this);
		layout_view.findViewById(R.id.layout_woyaoqiuzhi).setOnClickListener(
				this);
		layout_editcomp = (LinearLayout) layout_view
				.findViewById(R.id.layout_editcomp);
		layout_editcomp.setOnClickListener(this);
		layout_view.findViewById(R.id.layout_myshow).setOnClickListener(this);
		layout_view.findViewById(R.id.layout_myshequ).setOnClickListener(this);
		layout_view.findViewById(R.id.layout_mycol).setOnClickListener(this);
		layout_view.findViewById(R.id.layout_myorder).setOnClickListener(this);
	}

	@Override
	public void initValue() {
		if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
			setViewValue();
		}
		getBlackLisyData();
	}

	// 设置当前界面用户已登录时展示数据
	private void setViewValue() {
		isShowPerfectInfoBar();
		MyRoundDraweeView img_head = new MyRoundDraweeView(getActivity());
		lin_head.removeAllViews();
		lin_head.addView(img_head, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		img_head.setUrlOfImage(AppContext.getInstance().getUserInfo()
				.getClientHeadPicUrl());
		txt_name.setText(AppContext.getInstance().getUserInfo().getNickName());
		txt_job.setText(AppContext.getInstance().getUserInfo()
				.getPositionName()
				+ "");
		// 生成二维码
		String qrcode = AppContext.getInstance().getUserInfo().getqRCode();
		if (null != qrcode && !"".equals(qrcode)) {
			try {
				String code = ConstantKey.QR_CODE_USER
						+ String.valueOf(AppContext.getInstance().getUserInfo()
								.getId());
				// 加载二维码
				// Bitmap bitMap = QRcodeUtil.createQRCode(code, 600);
				img_qrcode.setImageBitmap(QRcodeUtil.createQRCode(code, 600));
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				ToastUtil.showShortToast("加载二维码失败");
			}
		}

		// 企业信息为空则不显示
		boolean noCompany = AppContext.getInstance().getUserInfo()
				.getCompanyEntity() == null;
		layout_editcomp.setVisibility(noCompany ? View.GONE : View.VISIBLE);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase req = (ReqBase) arg0;
		LogUtil.d(req.getBody().toString());
		switch (requestCode) {
		case reqCodeTwo:
			String jsonstr = req.getBody().get("defriend").toString();
			List<DefriendEntity> datas = new ArrayList<DefriendEntity>();
			try {
				datas = (List<DefriendEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<DefriendEntity>>() {
						}.getType());
				AppContext.getBlackSet().clear();
				for (DefriendEntity bean : datas) {
					LogUtil.d("hl", "blackList=" + bean.getDeFriendId());
					AppContext.getBlackSet().add(bean.getDeFriendId());
				}
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}

			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * 是否显示完善资料提示栏
	 */
	private void isShowPerfectInfoBar() {
		layout_perfectInfo.setVisibility(AppContext.getInstance()
				.isLoginedAndInfoPerfect() ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_perfectInfo:
			// startActivity(new Intent(getActivity(),
			// PerfectMyInfoActivity.class));
			startActivity(new Intent(getActivity(), MkLoginActivity.class));
			break;
		case R.id.layout_myhome:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				startActivity(new Intent(getActivity(),
						EditPersonalInfoActivity.class));
			} else {
				startActivity(new Intent(getActivity(),
						PersonShowActivity.class));
			}
			// startActivity(new Intent(getActivity(),
			// PersonalMainActivity.class));
			break;
		case R.id.layout_myqrcode:
			startActivity(new Intent(getActivity(),
					PersonShareInfoActivity.class));
			break;

		case R.id.layout_mycol:
			// if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
			startActivity(new Intent(getActivity(), SettingActivity.class));
			// } else {
			// ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
			// }
			break;
		case R.id.iv_code:
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				Intent intent3 = new Intent();
				intent3.setClass(getActivity(), CaptureActivity.class);// 跳转页面
				startActivity(intent3);
			} else {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
			}
			break;
		case R.id.lin_head:
			if (!"".equals(AppContext.getInstance().getUserInfo()
					.getClientHeadPicUrl())) {
				Intent intentImage = new Intent();
				intentImage.setClass(getActivity(), ImagePagerActivity.class);
				intentImage.putExtra("showOnePic", AppContext.getInstance()
						.getUserInfo().getClientHeadPicUrl());
				startActivity(intentImage);
				// startActivity(new Intent(getActivity(),
				// MyAvatarActivity.class)
				// .putExtra("picUrl", AppContext.getInstance()
				// .getUserInfo().getClientHeadPicUrl()));
			}
			break;

		case R.id.layout_resume:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			if (AppContext.getInstance().getUserInfo().getResumeId() == -1) {
				final HintDialogwk dialog = new HintDialogwk(
						getActivity(), "哇，你知道吗，你上传的简历是可以保存在《中国美容人才库》里，随时可以转发使用，下次找工作不用重新填写简历啦！~", "去填写");
				dialog.setOneClicklistener(new DoOneClickListenerInterface() {

					@Override
					public void doOne() {
						startActivity(new Intent(getActivity(),
								MrrckResumeActivity.class));
						dialog.dismiss();
					}
				});
				dialog.show();
			} else {
				startActivity(new Intent(getActivity(),
						ResumeLookActivity.class));
			}
			break;
		case R.id.layout_woyaoqiuzhi:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			if (!TextUtils.isEmpty(AppContext.getInstance().getUserInfo()
					.getMyJobUrl())) {
				Intent i = new Intent(getActivity(), WebViewActivity.class);
				i.putExtra("webUrl", AppContext.getInstance().getUserInfo()
						.getMyJobUrl());
				i.putExtra("flag", 1);
				startActivity(i);
			}
			break;
		case R.id.layout_editcomp:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(),
					CompanyInformationActivity.class));
			break;
		case R.id.layout_myshow:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(), MyShowWorksActivity.class));
			break;
		case R.id.layout_myshequ:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(), MyCommunityActivity.class));
			break;
		case R.id.layout_citiao:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(), MyEntryActivity.class));
			break;
		case R.id.layout_myorder:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(), MyOrderActivity.class));
			break;
		default:
			break;
		}
	}

	// 定义广播接收器
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BroadCastAction.ACTION_MAIN_LOGOUT)) {
				startActivity(new Intent(getActivity(), MkLoginActivity.class).putExtra("needGoToHome", true));
				getActivity().finish();
				// MrrckApplication.getInstance().stopMsgServices();
				// Intent launchIntent = context.getPackageManager()
				// .getLaunchIntentForPackage(context.getPackageName());
				// launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				// | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				// launchIntent.putExtra("showLoginFirst", true);
				// context.startActivity(launchIntent);
				// getActivity().finish();
			} else if (action.equals(BroadCastAction.ACTION_IM_BLACKLIST)) {
				if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
					getBlackLisyData();// 获取黑名单（聊天消息要使用）
				}
			} else if (action.equals(BroadCastAction.ACTION_IM_REFRESH_COMPANY)) {
				// boolean noCompany = AppContext.getInstance().getUserInfo()
				// .getCompanyEntity() == null;
				// layout_editcomp.setVisibility(noCompany ? View.GONE :
				// View.VISIBLE);
				// layout_view.findViewById(R.id.view_editcomp_line).setVisibility(
				// noCompany ? View.GONE : View.VISIBLE);
				layout_editcomp.setVisibility(View.VISIBLE);
			} else if (action.equals(BroadCastAction.ACTION_CHANGE_AVATAR)) {// 改变头像
				MyRectDraweeView img_head = new MyRectDraweeView(getActivity());
				lin_head.removeAllViews();
				lin_head.addView(img_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(AppContext.getInstance().getUserInfo()
						.getClientThumbHeadPicUrl());

			} else if (action.equals(BroadCastAction.ACTION_RELOGINT_DOLOUOUT)) {// 重复登录需退出
				AppContext.getInstance().setLocalUserEmpty();
//				MrrckApplication.getInstance().stopMsgServices();

				// 弹出重复登陆页面
				startActivity(new Intent(getActivity(),
						ReloginTipActivity.class));
			} else if (BroadCastAction.ACTION_PERFECT_INFO.equals(intent
					.getAction())) {
				setViewValue();
			} else if (BroadCastAction.ACTION_LOGIN_SUCCESS.equals(intent
					.getAction())) {
				setViewValue();
			}
		}

	};

	/**
	 * 获取黑名单
	 */
	public void getBlackLisyData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_DEFRIEND));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req, false);
	}

	// 用户注册广播
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(BroadCastAction.ACTION_MAIN_LOGOUT);
		myIntentFilter.addAction(BroadCastAction.ACTION_IM_BLACKLIST);
		myIntentFilter.addAction(BroadCastAction.ACTION_IM_REFRESH_COMPANY);
		myIntentFilter.addAction(BroadCastAction.ACTION_CHANGE_AVATAR);
		myIntentFilter.addAction(BroadCastAction.ACTION_RELOGINT_DOLOUOUT);
		myIntentFilter.addAction(BroadCastAction.ACTION_PERFECT_INFO);
		myIntentFilter.addAction(BroadCastAction.ACTION_LOGIN_SUCCESS);
		getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Tool.releaseImageViewResouce(img_qrcode);
		getActivity().unregisterReceiver(mBroadcastReceiver);
	}

}
