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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.JobClassEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ResumeEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.morefun.SelectPositionActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 搜素简历
 * 
 */
public class SearchResumeFragment extends BaseFragment implements
		OnClickListener {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<ResumeEntity> showAdapter;
	private List<ResumeEntity> showlist = new ArrayList<ResumeEntity>();
	private int page = 1;
	private Drawable arrowDown, arrowUp;
	private TextView[] topTitleTv = new TextView[4];
	private LinearLayout selectLayout;
	private CommonAdapter<String> showSelectAdapter;
	private ListView selectListview;
	private List<String> selectShowList = new ArrayList<String>();
	private int selectTag;
	private final int TAG_AREA = 0;
	private final int TAG_POSITION = 1;
	private final int TAG_AGE = 2;
	private final int TAG_MAJOR = 3;
	private ArrayList<String> area_StrList, position_StrList, age_StrList,
			major_StrList;
	private String area = "-1";
	private Integer position = -1;
	private String age = "";
	private String major = "-1";
	private List<DataconfigEntity> age_DBData, major_DBData;
	private List<JobClassEntity> position_DBData;
	private final int INTENTPOS = 100;
	private String isVipCompany;
	private String companyResumeDetailUrl;
	private String url;
	private boolean firstLoad = true;

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
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_searchresume, null);
		initTopSelect();
		initPullListView();
		return layout_view;

	}

	@Override
	public void initValue() {
		regisBroadcast();
		downRefreshData();
		// // 处理地区数据
		// bossTypes_DBData = MKDataBase.getInstance().getBossTypes();
		// DataconfigEntity dfe = new DataconfigEntity();
		// dfe.setCodeId("-1");
		// dfe.setValue("全部类型");
		// bossTypes_DBData.add(0, dfe);
		// bossType_StrList = new ArrayList<String>();
		// for (int i = 0; i < bossTypes_DBData.size(); i++) {
		// bossType_StrList.add(bossTypes_DBData.get(i).getValue());
		// }

		// // 处理职位数据
		// position_DBData = MKDataBase.getInstance().getJobClassIntent();
		// JobClassEntity je = new JobClassEntity();
		// je.setId(-1);
		// je.setName("职位不限");
		// position_DBData.add(0, je);
		// position_StrList = new ArrayList<String>();
		// for (int i = 0; i < position_DBData.size(); i++) {
		// position_StrList.add(position_DBData.get(i).getName());
		// }

		// 处理年龄数据
		age_DBData = MKDataBase.getInstance().getAge();
		DataconfigEntity dfe_xz = new DataconfigEntity();
		// age_DBData.add(0, dfe_xz);
		age_StrList = new ArrayList<String>();
		for (int i = 0; i < age_DBData.size(); i++) {
			age_StrList.add(age_DBData.get(i).getValue());
		}

		// // 处理专业数据
		major_DBData = MKDataBase.getInstance().getWorkSkill();
		DataconfigEntity dfe_bnf = new DataconfigEntity();
		// major_DBData.add(0, dfe_bnf);
		major_StrList = new ArrayList<String>();
		for (int i = 0; i < major_DBData.size(); i++) {
			major_StrList.add(major_DBData.get(i).getValue());
		}

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("00000", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("resume").toString().length() > 2) {
				List<ResumeEntity> listData = (List<ResumeEntity>) JsonUtil
						.jsonToList(resp.getBody().get("resume").toString(),
								new TypeToken<List<ResumeEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
				if (!firstLoad) {
					ToastUtil.showShortToast("无更多简历");
				}

			}
			if (resp.getBody().get("isVipCompany").toString().length() > 2) {
				isVipCompany = resp.getBody().get("isVipCompany").getAsString();
			}
			if (resp.getBody().get("companyResumeDetailUrl").toString()
					.length() > 2) {
				companyResumeDetailUrl = resp.getBody()
						.get("companyResumeDetailUrl").getAsString();
			}

			showAdapter.notifyDataSetChanged();
			firstLoad = false;
			break;
		default:
			break;
		}
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (PullToRefreshListView) layout_view
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						upRefreshData();
					}
				});

		showAdapter = new CommonAdapter<ResumeEntity>(getActivity(),
				R.layout.item_fragment_searchresume, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final ResumeEntity t) {
				if ("1".equals(t.getIsVoiceResume())) {
					viewHolder.getView(R.id.img_yinping).setVisibility(
							View.VISIBLE);
					viewHolder.setImage(R.id.img_yinping,
							R.drawable.searchresume_yinping);
				} else {
					viewHolder.getView(R.id.img_yinping).setVisibility(
							View.INVISIBLE);
				}
				if ("1".equals(t.getIsVideoResume())) {
					viewHolder.getView(R.id.img_shiping).setVisibility(
							View.VISIBLE);
					viewHolder.setImage(R.id.img_shiping,
							R.drawable.searchresume_shipin);
				} else {
					viewHolder.getView(R.id.img_shiping).setVisibility(
							View.GONE);
				}
				viewHolder.setImage(R.id.img_head, t.getClientResumePhoto());
				viewHolder.setText(R.id.tv_nickname, t.getRealName());
				TextView tv_genderandage = viewHolder
						.getView(R.id.tv_genderandage);
				if (Tool.isEmpty(t.getAgeValue())) {
					tv_genderandage.setText("未知");
				} else {
					tv_genderandage.setText(t.getAgeValue());
				}
				if ("1".equals(t.getGender())) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.nan_white);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					tv_genderandage.setCompoundDrawables(drawable, null, null,
							null);
					tv_genderandage.setBackgroundResource(R.drawable.nanxing);
				} else {
					Drawable drawable = getResources().getDrawable(
							R.drawable.nv_white);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					tv_genderandage.setCompoundDrawables(drawable, null, null,
							null);
					tv_genderandage.setBackgroundResource(R.drawable.nvxing);
				}
				viewHolder.setText(R.id.tv_working_age,
						"从业年龄:" + t.getJobAgeValue());
				viewHolder.setText(R.id.tv_skill, "技能：" + t.getKnowledge());
				viewHolder.setText(R.id.tv_updatetime, "刷新时间：" + t.getClientRefreshDate());
				viewHolder.setText(R.id.tv_status,
						"状态：" + t.getResumeStatusName());
				viewHolder.getView(R.id.img_yinping).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// startActivity(new Intent(getActivity(),
								// TestAudioActivity.class).putExtra(
								// "filePath", "").putExtra(
								// "recordingTime", ""));

							}
						});
				viewHolder.getView(R.id.img_shiping).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// startActivity(new Intent(getActivity(),
								// TestVideoActivity.class).putExtra(
								// "mrrck_videoPath", ""));
							}
						});
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								url = companyResumeDetailUrl
										+ AppContext.getInstance()
												.getInstance().getUserInfo()
												.getId() + "&resumeId="
										+ t.getId() + "&isVipCompany="
										+ isVipCompany;
								LogUtil.d("333333", url);
								startActivity(new Intent(getActivity(),
										ResumeNoHfive.class).putExtra(
										"webUrl", url).putExtra("resumeId", t.getId()));
							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);

	}

	protected void upRefreshData() {
		page++;
		GetData();
	}

	protected void downRefreshData() {
		showlist.clear();
		page = 1;
		GetData();
	}

	/**
	 * 初始化选择栏
	 */
	private void initTopSelect() {
		arrowDown = getResources().getDrawable(R.drawable.arrowdown);
		arrowDown.setBounds(0, 0, ScreenUtil.dip2px(getActivity(), 9),
				ScreenUtil.dip2px(getActivity(), 5));
		arrowUp = getResources().getDrawable(R.drawable.arrow_r_up);
		arrowUp.setBounds(0, 0, ScreenUtil.dip2px(getActivity(), 9),
				ScreenUtil.dip2px(getActivity(), 5));
		topTitleTv[0] = (TextView) layout_view.findViewById(R.id.tv_area);
		topTitleTv[0].setCompoundDrawables(null, null, arrowDown, null);
		topTitleTv[1] = (TextView) layout_view.findViewById(R.id.tv_position);
		topTitleTv[1].setCompoundDrawables(null, null, arrowDown, null);
		topTitleTv[2] = (TextView) layout_view.findViewById(R.id.tv_age);
		topTitleTv[2].setCompoundDrawables(null, null, arrowDown, null);
		topTitleTv[3] = (TextView) layout_view.findViewById(R.id.tv_major);
		topTitleTv[3].setCompoundDrawables(null, null, arrowDown, null);
		layout_view.findViewById(R.id.bottomEmpty).setOnClickListener(this);
		topTitleTv[0].setOnClickListener(this);
		topTitleTv[1].setOnClickListener(this);
		topTitleTv[2].setOnClickListener(this);
		topTitleTv[3].setOnClickListener(this);
		selectLayout = (LinearLayout) layout_view
				.findViewById(R.id.selectLayout);
		selectListview = (ListView) layout_view.findViewById(R.id.messagelist);
		showSelectAdapter = new CommonAdapter<String>(getActivity(),
				R.layout.item_dialogliststr, selectShowList) {

			@Override
			public void convert(ViewHolder viewHolder, String t) {
				viewHolder.setText(R.id.messagetext, t);
			}
		};
		selectListview.setAdapter(showSelectAdapter);
		selectListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (selectTag) {
				case TAG_AREA:
					topTitleTv[0].setText(area_StrList.get(arg2));
					// area = bossTypes_DBData.get(arg2).getCodeId();
					break;
				case TAG_POSITION:
					topTitleTv[1].setText(position_StrList.get(arg2));
					position = position_DBData.get(arg2).getId();
					break;
				case TAG_AGE:
					topTitleTv[2].setText(age_StrList.get(arg2));
					age = age_DBData.get(arg2).getExtra2();
					break;
				case TAG_MAJOR:
					topTitleTv[3].setText(major_StrList.get(arg2));
					major = major_DBData.get(arg2).getCodeId();
					break;

				default:
					break;
				}
				setSelectPageVisible(false);
				downRefreshData();
			}
		});
	}

	private void setSelectPageVisible(boolean showSelectPage) {
		selectLayout.setVisibility(showSelectPage ? View.VISIBLE : View.GONE);
		if (!showSelectPage) {
			topTitleTv[selectTag].setTextColor(Color.parseColor("#666666"));
			topTitleTv[selectTag].setCompoundDrawables(null, null, arrowDown,
					null);
		}
	}

	private boolean isSelectPageShow() {
		return selectLayout.getVisibility() == View.VISIBLE;
	}

	/**
	 * 显示下拉选择
	 * 
	 * @param tag
	 */
	private void setTagAndShowSelect(int tag) {
		for (int i = 0; i < topTitleTv.length; i++) {
			topTitleTv[i].setTextColor(Color.parseColor("#666666"));
			topTitleTv[i].setCompoundDrawables(null, null, arrowDown, null);
		}
		selectTag = tag;
		topTitleTv[tag].setTextColor(getResources().getColor(R.color.mrrck_bg));
		topTitleTv[tag].setCompoundDrawables(null, null, arrowUp, null);
		selectShowList.clear();
		switch (tag) {
		case TAG_AREA:
			selectShowList.addAll(area_StrList);
			break;
		case TAG_POSITION:
			selectShowList.addAll(position_StrList);
			break;
		case TAG_AGE:
			selectShowList.addAll(age_StrList);
			break;
		case TAG_MAJOR:
			selectShowList.addAll(major_StrList);
			break;

		default:
			break;
		}
		setSelectPageVisible(true);
		showSelectAdapter.notifyDataSetInvalidated();
	}

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		map.put("cityCode", area);
		map.put("positionId", position);
		map.put("knowledgeId", major);
		map.put("ageId", age);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.e(map + "");
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_RESUME));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req,false);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.bottomEmpty:
			setSelectPageVisible(false);
			break;
		case R.id.tv_area:
			new WheelSelectCityDialog(getActivity(), true,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							topTitleTv[0].setText(cityName);
							area = cityCode+"";
							downRefreshData();
						}

					}).show();
			break;
		case R.id.tv_position:
			startActivity(new Intent(getActivity(),
					SelectPositionActivity.class).putExtra("flag", 1)
					.putExtra("hasUnlimited", true)
					.putExtra("hasJobFlag", true));
			break;
		case R.id.tv_age:
			if (selectTag == TAG_AGE && isSelectPageShow()) {
				setSelectPageVisible(false);
				return;
			}
			setTagAndShowSelect(TAG_AGE);
			break;
		case R.id.tv_major:
			if (selectTag == TAG_MAJOR && isSelectPageShow()) {
				setSelectPageVisible(false);
				return;
			}
			setTagAndShowSelect(TAG_MAJOR);
			break;
		default:
			break;
		}
	}

	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_PUBLIC_SEARCH_RESUME);
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_COMPANY);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_PUBLIC_SEARCH_RESUME.equals(intent
					.getAction())) {
				topTitleTv[1].setText(intent.getStringExtra("name"));
				position = intent.getIntExtra("id", 0);
				downRefreshData();
			}
			if (BroadCastAction.ACTION_IM_REFRESH_COMPANY.equals(intent
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

}
