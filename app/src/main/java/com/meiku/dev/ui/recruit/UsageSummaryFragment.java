package com.meiku.dev.ui.recruit;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.CompanyInfoCountEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;

/**
 * 使用汇总
 */
public class UsageSummaryFragment extends BaseFragment implements
		OnClickListener {
	private View layout_view;
	private PullToRefreshScrollView pull_refreshSV;// 下拉刷新
	private CompanyInfoCountEntity companyinfocountentity;
	private TextView tv_fabuzhiwei, tv_jianlishu, tv_alljianlishu,
			tv_shengyujianli, tv_isvip, tv_phone;
	private LinearLayout lin_xiaofeimingxi;
	private Button btn_kaitong;
	private TextView tv_isopen;
	private ImageView img_levelvip;
	private LinearLayout lin_detail;
	private TextView tv_usestarttime;
	private TextView tv_usestoptime;
	private boolean firstLoad = true;
	private LinearLayout lin_addcity;
	private TextView tv_opencity;
	private ImageView img_detailtubiao;
	private TextView tv_detailtype;
	private TextView tv_detailmonth;
	private int vipType;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_usagesummary, null);
		btn_kaitong = (Button) layout_view.findViewById(R.id.btn_kaitong);
		btn_kaitong.setOnClickListener(this);
		tv_isopen = (TextView) layout_view.findViewById(R.id.tv_isopen);
		lin_addcity = (LinearLayout) layout_view.findViewById(R.id.lin_addcity);
		lin_addcity.setOnClickListener(this);
		tv_usestarttime = (TextView) layout_view
				.findViewById(R.id.tv_usestarttime);
		tv_usestoptime = (TextView) layout_view
				.findViewById(R.id.tv_usestoptime);
		lin_detail = (LinearLayout) layout_view.findViewById(R.id.lin_detail);
		img_detailtubiao = (ImageView) layout_view
				.findViewById(R.id.img_detailtubiao);
		tv_detailtype = (TextView) layout_view.findViewById(R.id.tv_detailtype);
		tv_detailmonth = (TextView) layout_view
				.findViewById(R.id.tv_detailmonth);
		img_levelvip = (ImageView) layout_view.findViewById(R.id.img_levelvip);
		tv_opencity = (TextView) layout_view.findViewById(R.id.tv_opencity);
		tv_fabuzhiwei = (TextView) layout_view.findViewById(R.id.tv_fabuzhiwei);
		tv_jianlishu = (TextView) layout_view.findViewById(R.id.tv_jianlishu);
		tv_alljianlishu = (TextView) layout_view
				.findViewById(R.id.tv_alljianlishu);
		tv_phone = (TextView) layout_view.findViewById(R.id.tv_phone);
		tv_phone.setOnClickListener(this);
		tv_shengyujianli = (TextView) layout_view
				.findViewById(R.id.tv_shengyujianli);
		tv_isvip = (TextView) layout_view.findViewById(R.id.tv_isvip);
		lin_xiaofeimingxi = (LinearLayout) layout_view
				.findViewById(R.id.lin_xiaofeimingxi);
		lin_xiaofeimingxi.setOnClickListener(this);
		initPullListView();
		regisBroadcast();
		return layout_view;

	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_COMPANY);
		filter.addAction(BroadCastAction.ACTION_RECRUIT_ADD_CITY);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_IM_REFRESH_COMPANY.equals(intent
					.getAction())) {
				downRefreshData();
			} else if (BroadCastAction.ACTION_RECRUIT_ADD_CITY.equals(intent
					.getAction())) {
				downRefreshData();
			}
		}
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}
	
	private String url;
	private String vipTypeName;
	private String monthname;
	private String orderFlag;
	private String orderFlagMsg;

	@Override
	public void initValue() {
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("123456", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("company").toString().length() > 2) {
				String json = resp.getBody().get("company").toString();
				companyinfocountentity = (CompanyInfoCountEntity) JsonUtil
						.jsonToObj(CompanyInfoCountEntity.class, json);
			} else {
				if (!firstLoad) {
					ToastUtil.showShortToast("无数据");
				}
			}
			if (null != companyinfocountentity) {
				orderFlag = companyinfocountentity.getOrderFlag();
				orderFlagMsg = companyinfocountentity.getOrderFlagMsg();
				vipType = companyinfocountentity.getVipType();
				url = companyinfocountentity.getVipTypeImgUrl();
				monthname = companyinfocountentity.getMonthName();
				Integer jobNum = companyinfocountentity.getJobNum();
				Integer sevenReceiveResumeNum = companyinfocountentity
						.getSevenReceiveResumeNum();
				Integer receiveResumeNum = companyinfocountentity
						.getReceiveResumeNum();
				Integer inviteNum = companyinfocountentity.getInviteNum();
				vipTypeName = companyinfocountentity.getVipTypeName();
				String fabuzhiwei = jobNum + "";
				tv_usestarttime.setText(companyinfocountentity
						.getStartVipDate());
				tv_usestoptime.setText(companyinfocountentity.getEndVipDate());
				tv_fabuzhiwei.setText(fabuzhiwei);

				String jianlishu = sevenReceiveResumeNum + "";
				tv_jianlishu.setText(jianlishu);

				String alljianlishu = receiveResumeNum + "";
				tv_alljianlishu.setText(alljianlishu);

				String shengyujianli = inviteNum + "";
				tv_shengyujianli.setText(shengyujianli);

				tv_isvip.setText(vipTypeName);
				String openCityname = companyinfocountentity.getOpenCityName();
				if (!Tool.isEmpty(openCityname)) {
					tv_opencity.setText(openCityname.replaceAll(",", "  "));
				}
				BitmapUtils bitmapUtils4 = new BitmapUtils(getActivity());
				bitmapUtils4.display(img_detailtubiao, url);
				tv_detailmonth.setText(monthname);
				tv_detailtype.setText(vipTypeName);
				switch (vipType) {
				case 0:
					img_levelvip.setVisibility(View.GONE);
					tv_isvip.setVisibility(View.INVISIBLE);
					tv_isopen.setText("暂未开通会员");
					btn_kaitong.setText("立即开通");
					lin_detail.setVisibility(View.GONE);
					break;
				case 1:
					img_levelvip.setVisibility(View.VISIBLE);
					BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.display(img_levelvip, url);
					tv_isvip.setVisibility(View.VISIBLE);
					tv_isopen.setText("已开通");
					btn_kaitong.setText("续费");
					lin_detail.setVisibility(View.VISIBLE);
					break;
				case 2:
					img_levelvip.setVisibility(View.VISIBLE);
					BitmapUtils bitmapUtils1 = new BitmapUtils(getActivity());
					bitmapUtils1.display(img_levelvip, url);
					tv_isvip.setVisibility(View.VISIBLE);
					tv_isopen.setText("已开通");
					btn_kaitong.setText("续费");
					lin_detail.setVisibility(View.VISIBLE);
					break;
				case 3:
					img_levelvip.setVisibility(View.VISIBLE);
					BitmapUtils bitmapUtils2 = new BitmapUtils(getActivity());
					bitmapUtils2.display(img_levelvip, url);
					tv_isvip.setVisibility(View.VISIBLE);
					tv_isopen.setText("已开通");
					btn_kaitong.setText("续费");
					lin_detail.setVisibility(View.VISIBLE);
					break; 
				case 4:
					img_levelvip.setVisibility(View.VISIBLE);
					BitmapUtils bitmapUtils3 = new BitmapUtils(getActivity());
					bitmapUtils3.display(img_levelvip, url);
					tv_isvip.setVisibility(View.VISIBLE);
					tv_isopen.setText("已开通");
					btn_kaitong.setText("续费");
					lin_detail.setVisibility(View.VISIBLE);
					break;
				case 5:
					img_levelvip.setVisibility(View.VISIBLE);
					BitmapUtils bitmapUtils5 = new BitmapUtils(getActivity());
					bitmapUtils5.display(img_levelvip, url);
					tv_isvip.setVisibility(View.VISIBLE);
					tv_isopen.setText("已过期");
					btn_kaitong.setText("立即开通");
					lin_detail.setVisibility(View.GONE);
					break;
				default:
					break;
				}

			}
			pull_refreshSV.onRefreshComplete();
			firstLoad = false;
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != pull_refreshSV) {
			pull_refreshSV.onRefreshComplete();
		}
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		pull_refreshSV = (PullToRefreshScrollView) layout_view
				.findViewById(R.id.pull_refresh);
		pull_refreshSV.setMode(PullToRefreshBase.Mode.BOTH);
		pull_refreshSV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						upRefreshData();
					}
				});
	}

	protected void upRefreshData() {
		GetData();
	}

	protected void downRefreshData() {
		GetData();
	}

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		req.setHeader(new ReqHead(AppConfig.NEWUSESUMMARY_90062));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req, false);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_xiaofeimingxi:
			startActivity(new Intent(getActivity(),
					ConsumerDetailsActivity.class));
			break;
		case R.id.btn_kaitong:
			Intent intentvip = new Intent(getActivity(),
					OpenZhaopinbaoPersonActivity.class);
			if (0 == vipType||5==vipType) {
				intentvip.putExtra("flag", 1);
			} else {
				intentvip.putExtra("flag", 0);
			}
			startActivity(intentvip);
			break;
		case R.id.tv_phone:
			Uri uri = Uri.parse("tel:" + "4006886800");
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(uri);
			startActivity(intent);
			break;
		case R.id.lin_addcity:
			if ("0".equals(orderFlag)) {
				Intent addIntent = new Intent(getActivity(),
						BuyAddRecruitCityActivity.class);
//				addIntent.putExtra("vipTypeName", vipTypeName);
//				addIntent.putExtra("vipMonthStr", monthname);
//				addIntent.putExtra("startTime", tv_usestarttime.getText()
//						.toString());
//				addIntent.putExtra("endtime", tv_usestoptime.getText()
//						.toString());
//				addIntent.putExtra("vipTypeImgUrl", url);
				startActivity(addIntent);
			} else {
				ToastUtil.showShortToast(orderFlagMsg);
			}

			break;
		default:
			break;
		}
	}
}
