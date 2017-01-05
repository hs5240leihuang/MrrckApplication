package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateNeedEntity;
import com.meiku.dev.bean.MkDecorateCategory;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.NewPopupwindows;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 我要装修列表
 */
public class IneedDecActivity extends BaseActivity implements OnClickListener {

	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<DecorateNeedEntity> showAdapter;
	private List<DecorateNeedEntity> showList = new ArrayList<DecorateNeedEntity>();
	private TextView right_txt_title;
	private List<MkDecorateCategory> typeList = new ArrayList<MkDecorateCategory>();
	protected Integer shopTypeCode = -1, yusuanCode = -1;
	protected String roomsizeCode = "-1";
	private List<MkDecorateCategory> yusuanMoneyList;
	private List<MkDecorateCategory> roomsizeList;
	private TextView tv_area, tv_type, tv_yusuan, tv_mianji;
	protected int selectCityCode = -1, selectProvinceCode = -1;
	protected String selectCityName, selectProvinceName;
	private LinearLayout lin_buju;
	private List<PopupData> typeShowList = new ArrayList<PopupData>();
	private List<PopupData> yusuanShowList = new ArrayList<PopupData>();
	private List<PopupData> sizeShowList = new ArrayList<PopupData>();

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_ineeddec;
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
		lin_buju = (LinearLayout) findViewById(R.id.lin_buju);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_yusuan = (TextView) findViewById(R.id.tv_yusuan);
		tv_mianji = (TextView) findViewById(R.id.tv_mianji);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setTextColor(getResources()
				.getColor(R.color.clo_ff3499));
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult((new Intent(IneedDecActivity.this,
						DecorationMypublishNeedActivity.class)),reqCodeOne);
			}
		});
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
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
		// 适配器
		showAdapter = new CommonAdapter<DecorateNeedEntity>(
				IneedDecActivity.this, R.layout.item_needdec, showList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateNeedEntity t) {
				LinearLayout lin_thumbpicture = viewHolder
						.getView(R.id.lin_thumbpicture);
				lin_thumbpicture.removeAllViews();
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MySimpleDraweeView imgpicture = new MySimpleDraweeView(
						IneedDecActivity.this);
				lin_thumbpicture.addView(imgpicture, params);
				imgpicture.setUrlOfImage(t.getClientMainThumbPicUrl());
				LinearLayout lin_showimg = viewHolder.getView(R.id.layout_head);
				MyRoundDraweeView showImgview = new MyRoundDraweeView(
						IneedDecActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.tv_name, t.getNickName());
				viewHolder.setText(R.id.tv_needname, t.getNeedName());
				viewHolder.setText(R.id.tv_needdetail, t.getClientNeedDetail());
				viewHolder.setText(R.id.tv_cost, t.getClientCostBudgetName());
				viewHolder.setText(R.id.tv_date, t.getClientCreateDate());
				viewHolder.setText(R.id.tv_liulanliang, t.getViewNum());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(IneedDecActivity.this,
										ShopNeedDetailActivity.class);
								intent.putExtra("loadUrl", t.getLoadUrl());
								intent.putExtra(ConstantKey.KEY_POSTID,
										t.getPostsId());
								intent.putExtra("needId", t.getId());
								startActivity(intent);
							}
						});
				lin_showimg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// startActivity(new Intent(getActivity(),
						// PersonShowActivity.class).putExtra(
						// PersonShowActivity.TO_USERID_KEY, t.getUserId()
						// + ""));
					}
				});

			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
		downRefreshData();
	}

	// 上拉加载更多数据
	private void upRefreshData() {
		page++;
		getData();
	}

	private void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		map.put("shopCategory", shopTypeCode);
		map.put("costBudget", yusuanCode);
		map.put("areaSize", roomsizeCode);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300045));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		page = 1;
		showList.clear();
		getData();
	}

	@Override
	public void initValue() {
		typeList = MKDataBase.getInstance().getDecorateCategoryList(0);
		if (!Tool.isEmpty(typeList)) {
			int size = typeList.size();
			for (int i = 0; i < size; i++) {
				typeShowList.add(new PopupData(typeList.get(i).getName(), 0));
			}
		}
		roomsizeList = MKDataBase.getInstance().getDecorateCategoryList(6);
		if (!Tool.isEmpty(roomsizeList)) {
			int size = roomsizeList.size();
			for (int i = 0; i < size; i++) {
				sizeShowList
						.add(new PopupData(roomsizeList.get(i).getName(), 0));

			}
		}
		yusuanMoneyList = MKDataBase.getInstance().getDecorateCategoryList(5);
		if (!Tool.isEmpty(yusuanMoneyList)) {
			int size = yusuanMoneyList.size();
			for (int i = 0; i < size; i++) {
				yusuanShowList.add(new PopupData(yusuanMoneyList.get(i)
						.getName(), 0));
			}
		}
	}

	@Override
	public void bindListener() {
		tv_area.setOnClickListener(this);
		tv_type.setOnClickListener(this);
		tv_yusuan.setOnClickListener(this);
		tv_mianji.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<DecorateNeedEntity> listData = (List<DecorateNeedEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateNeedEntity>>() {
								}.getType());
				showList.addAll(listData);
			} else {
			}
			if (showList.size() > 0) {
				lin_buju.setVisibility(View.INVISIBLE);
			} else {
				lin_buju.setVisibility(View.VISIBLE);
			}
			showAdapter.notifyDataSetChanged();
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
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						IneedDecActivity.this, "提示", resp.getHeader()
								.getRetMessage(), "确定");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_area:
			new WheelSelectCityDialog(IneedDecActivity.this, true,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_area.setText(cityName);
							selectCityCode = cityCode;
							selectProvinceCode = provinceCode;
							selectProvinceName = provinceName;
							selectCityName = cityName;
							downRefreshData();
						}
					}).show();
			break;
		case R.id.tv_type:
			if (Tool.isEmpty(typeShowList)) {
				ToastUtil.showShortToast("获取选项数据失败");
				return;
			}
			new NewPopupwindows(IneedDecActivity.this, typeShowList,
					new popwindowListener() {

						@Override
						public void doChoose(int position) {
							tv_type.setText(typeShowList.get(position)
									.getName());
							shopTypeCode = typeList.get(position).getCode();
							downRefreshData();
						}
					}, 0).show(v);
			break;
		case R.id.tv_yusuan:
			if (Tool.isEmpty(yusuanShowList)) {
				ToastUtil.showShortToast("获取选项数据失败");
				return;
			}
			new NewPopupwindows(IneedDecActivity.this, yusuanShowList,
					new popwindowListener() {

						@Override
						public void doChoose(int position) {
							tv_yusuan.setText(yusuanShowList.get(position)
									.getName());
							yusuanCode = yusuanMoneyList.get(position)
									.getCode();
							downRefreshData();
						}
					}, 0).show(v);
			break;
		case R.id.tv_mianji:
			if (Tool.isEmpty(sizeShowList)) {
				ToastUtil.showShortToast("获取选项数据失败");
				return;
			}
			new NewPopupwindows(IneedDecActivity.this, sizeShowList,
					new popwindowListener() {

						@Override
						public void doChoose(int position) {
							tv_mianji.setText(roomsizeList.get(position)
									.getName());
							roomsizeCode = roomsizeList.get(position)
									.getRemark();
							downRefreshData();
						}
					}, 0).show(v);
			break;

		default:
			break;
		}

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode==reqCodeOne) {
				downRefreshData();
			}
		}
	}

}
