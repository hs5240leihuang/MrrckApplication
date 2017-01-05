package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyListView;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 新的库群推荐页面
 * 
 */
public class NewKuGroupFragment extends BaseFragment {

	private View layout_view;
	private ClearEditText etSearch;
	private PullToRefreshScrollView pull_refreshSV;
	private CommonAdapter<GroupEntity> topListAdapter, botttomListAdapter;
	private List<GroupEntity> recommendTopListData = new ArrayList<GroupEntity>();
	private List<GroupEntity> recommendBottomListData = new ArrayList<GroupEntity>();
	private int groupPage = 1;
	private int isPush = 0;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_kugroup, null);
		initView();
		return layout_view;
	}

	private void initView() {
		regisBroadcast();
		initSeatchBar();
		initPullView();
		initTopList();
		initBottomList();
	}

	private void initTopList() {
		MyListView myTopListView = (MyListView) layout_view
				.findViewById(R.id.myTopListView);
		topListAdapter = new CommonAdapter<GroupEntity>(getActivity(),
				R.layout.item_im_recommendgroupnew, recommendTopListData) {

			@Override
			public void convert(ViewHolder viewHolder, final GroupEntity t) {
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(getActivity());
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientThumbGroupPhoto());
				viewHolder.setText(R.id.tv_name, t.getGroupName());
				viewHolder.setText(R.id.tv_intro, t.getRemark());
				LinearLayout layout_tags = viewHolder.getView(R.id.layout_tags);
				layout_tags.removeAllViews();
				if (!Tool.isEmpty(t.getTagsName())) {
					Iterator<Entry<String, String>> iter = t.getTagsName()
							.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						View tagView = LayoutInflater.from(getActivity())
								.inflate(R.layout.view_kugrouptag, null, false);
						TextView tagTv = (TextView) tagView
								.findViewById(R.id.tag);
						tagTv.setText(entry.getKey() + "");
						Drawable drawable = tagTv.getBackground();
						int color = Color.parseColor(entry.getValue() + "");
						drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
						layout_tags.addView(tagView);
					}
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (!AppContext.getInstance()
										.isLoginedAndInfoPerfect()) {
									ShowLoginDialogUtil
											.showTipToLoginDialog(getActivity());
									return;
								}
								if (!NetworkTools
										.isNetworkAvailable(getActivity())) {
									ToastUtil.showShortToast(getResources()
											.getString(R.string.netNoUse));
									return;
								}
								Intent i = new Intent(getActivity(),
										GroupInfoActivity.class);
								i.putExtra(ConstantKey.KEY_IM_MULTI_CHATROOMID,
										t.getId() + "");
								startActivity(i);
							}
						});
			}

		};
		myTopListView.setAdapter(topListAdapter);

	}

	private void initBottomList() {
		MyListView myBottomListView = (MyListView) layout_view
				.findViewById(R.id.myBottomListView);
		botttomListAdapter = new CommonAdapter<GroupEntity>(getActivity(),
				R.layout.item_im_recommendgroupnew, recommendBottomListData) {

			@Override
			public void convert(ViewHolder viewHolder, final GroupEntity t) {
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(getActivity());
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientThumbGroupPhoto());
				viewHolder.setText(R.id.tv_name, t.getGroupName());
				viewHolder.setText(R.id.tv_intro, t.getRemark());
				LinearLayout layout_tags = viewHolder.getView(R.id.layout_tags);
				layout_tags.removeAllViews();
				if (!Tool.isEmpty(t.getTagsName())) {
					Iterator<Entry<String, String>> iter = t.getTagsName()
							.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						View tagView = LayoutInflater.from(getActivity())
								.inflate(R.layout.view_kugrouptag, null, false);
						TextView tagTv = (TextView) tagView
								.findViewById(R.id.tag);
						tagTv.setText(entry.getKey() + "");
						Drawable drawable = tagTv.getBackground();
						int color = Color.parseColor(entry.getValue() + "");
						drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
						layout_tags.addView(tagView);
					}
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (!AppContext.getInstance()
										.isLoginedAndInfoPerfect()) {
									ShowLoginDialogUtil
											.showTipToLoginDialog(getActivity());
									return;
								}
								if (!NetworkTools
										.isNetworkAvailable(getActivity())) {
									ToastUtil.showShortToast(getResources()
											.getString(R.string.netNoUse));
									return;
								}
								Intent i = new Intent(getActivity(),
										GroupInfoActivity.class);
								i.putExtra(ConstantKey.KEY_IM_MULTI_CHATROOMID,
										t.getId() + "");
								startActivity(i);
							}
						});
			}

		};
		myBottomListView.setAdapter(botttomListAdapter);

	}

	private void initSeatchBar() {
		etSearch = (ClearEditText) layout_view.findViewById(R.id.et_msg_search);
		etSearch.setKeyListener(null);
		etSearch.setHint("请输入群组关键词搜索");
		etSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
					return;
				}
				startActivity(new Intent(getActivity(),
						GroupSearchActivity.class));
			}
		});
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullView() {
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
		groupPage++;
		isPush = 1;
		getTuijianGroupList();
	}

	protected void downRefreshData() {
		isPush = 0;
		groupPage = 1;
		recommendTopListData.clear();
		recommendBottomListData.clear();
		getTuijianGroupList();
	}

	@Override
	public void initValue() {
		getTuijianGroupList();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==ku group==>" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (isPush == 0) {
				if (!Tool.isEmpty(resp.getBody())
						&& !Tool.isEmpty(resp.getBody().get("libraryGroup"))) {
					String groupJsonStr = resp.getBody().get("libraryGroup")
							.toString();
					List<GroupEntity> libraryGroupList = (List<GroupEntity>) JsonUtil
							.jsonToList(groupJsonStr,
									new TypeToken<List<GroupEntity>>() {
									}.getType());
					if (!Tool.isEmpty(libraryGroupList)) {
						recommendTopListData.addAll(libraryGroupList);
					}
				}
			}
			if (topListAdapter != null) {
				topListAdapter.notifyDataSetChanged();
			}
			String groupJsonStr = resp.getBody().get("group").toString();
			List<GroupEntity> groupList = (List<GroupEntity>) JsonUtil
					.jsonToList(groupJsonStr,
							new TypeToken<List<GroupEntity>>() {
							}.getType());
			if (!Tool.isEmpty(groupList)) {
				recommendBottomListData.addAll(groupList);
			} else {
				ToastUtil.showShortToast("无更多推荐群");
			}
			if (botttomListAdapter != null) {
				botttomListAdapter.notifyDataSetChanged();
			}
			pull_refreshSV.onRefreshComplete();
			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (pull_refreshSV != null) {
			pull_refreshSV.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("获取库群数据异常!");
		}
	}

	/**
	 * 获取推荐群组
	 */
	private void getTuijianGroupList() {
		int libGroupId = -1;
		if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
			libGroupId = MKDataBase.getInstance().getFirstJobIDByPositionId(
					AppContext.getInstance().getUserInfo().getPosition());
		}
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("libGroupId", libGroupId);
		map.put("provinceCode", MrrckApplication.provinceCode);
		map.put("cityCode", MrrckApplication.cityCode);
		map.put("isPush", isPush);// 是否需要推荐数据 0 需要推荐数据 1 不需要推荐数据
		map.put("page", groupPage);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.d("hl", "推荐群" + map);
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_IM_GROUP_TUIJIANNEW));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req, false);
	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_LOGIN_SUCCESS);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_LOGIN_SUCCESS.equals(intent.getAction())) {// 登陆成功，刷新数据
				downRefreshData();
			}
		}
	};

}
