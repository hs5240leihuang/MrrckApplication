package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.ContactFragmentAdapter;
import com.meiku.dev.bean.FriendEntity;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.ui.chat.ChooseGroupActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinComparator;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.EnLetterView;
import com.umeng.analytics.MobclickAgent;

/**
 * 通讯录主页
 * 
 */
public class AddressListFragment extends BaseFragment implements
		OnClickListener {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private EnLetterView rightLetter;
	private TextView dialogTextView;
	private View listHeaderView;
	private ContactFragmentAdapter adapter;
	private ClearEditText etSearch;
	private ArrayList<GroupEntity> groupList = new ArrayList<GroupEntity>();

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
		layout_view = inflater.inflate(R.layout.fragment_addresslist, null);
		listHeaderView = inflater.inflate(
				R.layout.contact_fragment_header_layout, null, false);
		regisBroadcast();
		initView();
		return layout_view;
	}

	private void initView() {
		etSearch = (ClearEditText) layout_view.findViewById(R.id.et_msg_search);
		etSearch.setHint("请输入关键词搜索");
		etSearch.setKeyListener(null);
		etSearch.setOnClickListener(this);
		listHeaderView.findViewById(R.id.layout_new).setOnClickListener(this);
		listHeaderView.findViewById(R.id.layout_group).setOnClickListener(this);
		listHeaderView.findViewById(R.id.layout_groupNotify)
				.setOnClickListener(this);
		initListView();
		initRightLetterViewAndSearchEdit();
	}

	private void initListView() {
		mPullRefreshListView = (PullToRefreshListView) layout_view
				.findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.getRefreshableView().addHeaderView(listHeaderView,
				null, false);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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
					}
				});
		adapter = new ContactFragmentAdapter(getActivity());
		mPullRefreshListView.setAdapter(adapter);
		getFriendList();
	}

	protected void downRefreshData() {
		getFriendList();
		getGroupList();
	}

	public void getFriendList() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_NEARBY_FRIENDLIST));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req, false);
	}

	public void getGroupList() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("loginUserId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_SEARCH_GROUP));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, false);
	}

	private void initRightLetterViewAndSearchEdit() {
		rightLetter = (EnLetterView) layout_view
				.findViewById(R.id.right_letter);
		dialogTextView = (TextView) layout_view.findViewById(R.id.dialog);
		rightLetter.setTextView(dialogTextView);
		rightLetter
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	private class LetterListViewListener implements
			EnLetterView.OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(String s) {
			int position = adapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				mPullRefreshListView.getRefreshableView()
						.setSelection(position);
			}
		}
	}

	@Override
	public void initValue() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("friend") != null
					&& resp.getBody().get("friend").toString().length() > 4) {
				List<FriendEntity> data = new ArrayList<FriendEntity>();
				try {
					data = (List<FriendEntity>) JsonUtil.jsonToList(resp
							.getBody().get("friend").toString(),
							new TypeToken<List<FriendEntity>>() {
							}.getType());
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
				if (Tool.isEmpty(data)) {
					// ToastUtil.showShortToast("暂无好友");
				} else {
					Collections.sort(SetListFirstChar(data),
							new PinyinComparator());
					adapter.setData(data);
				}
				adapter.notifyDataSetChanged();
			}
			mPullRefreshListView.onRefreshComplete();
			break;
		case reqCodeTwo:
			if (resp.getBody().get("group") != null
					&& resp.getBody().get("group").toString().length() > 4) {
				groupList = (ArrayList<GroupEntity>) JsonUtil.jsonToList(resp
						.getBody().get("group").toString(),
						new TypeToken<List<GroupEntity>>() {
						}.getType());
				if (!Tool.isEmpty(groupList)) {
					AppContext.setGroupList(groupList);
					new AsyncTask<Integer, Integer, Boolean>() {
						@Override
						protected void onPostExecute(Boolean result) {
							super.onPostExecute(result);
						}

						@Override
						protected Boolean doInBackground(Integer... params) {
							for (int i = 0; i < groupList.size(); i++) {
								if (groupList.get(i).getGroupUserList() != null) {
									List<GroupUserEntity> userList = groupList
											.get(i).getGroupUserList();
									for (GroupUserEntity user : userList) {// 群成员存DB
										IMYxUserInfo userInfo = new IMYxUserInfo();
										userInfo.setNickName(user.getNickName());
										userInfo.setUserHeadImg(user
												.getClientThumbHeadPicUrl());
										userInfo.setYxAccount(user
												.getLeanCloudUserName());
										userInfo.setUserId(user.getUserId());
										MsgDataBase.getInstance()
												.saveOrUpdateYxUser(userInfo);
									}
								}
							}
							return null;
						}
					}.execute();

				} else {
					AppContext.getGroupMap().clear();
				}

			} else {
				AppContext.getGroupMap().clear();
			}
			break;
		default:
			break;
		}
	}

	private List<FriendEntity> SetListFirstChar(List<FriendEntity> data) {
		for (int i = 0, size = data.size(); i < size; i++) {
			String sortString = PinyinUtil.getTerm(data.get(i).getAliasName());
			data.get(i).setNameFirstChar(
					sortString.matches("[A-Z]") ? sortString : "#");
		}
		return data;
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.post(new Runnable() {

				@Override
				public void run() {
					mPullRefreshListView.onRefreshComplete();
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_new:// 新的朋友
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			Intent newIntent = new Intent(getActivity(),
					NewFriendActivity.class);
			startActivity(newIntent);
			break;

		case R.id.layout_group:// 群组
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(), ChooseGroupActivity.class));
			break;
		case R.id.et_msg_search:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(),
					AddressListSearchActivity.class).putExtra("useType",
					ConstantKey.SEARCHPAGE_UESTYPE_SEARCH));
			break;
		case R.id.layout_groupNotify:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			}
			startActivity(new Intent(getActivity(), GroupNotifyActivity.class));
			break;
		}

	}

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_IM_REFRESH_CHANGEBEIZHU);
		filter.addAction(BroadCastAction.ACTION_IM_REFRESHRECENT_LEAVEGROUP);
		filter.addAction(BroadCastAction.ACTION_LOGIN_SUCCESS);
		filter.addAction(BroadCastAction.ACTION_IM_GROUP_UPDATE);
		filter.addAction(BroadCastAction.ACTION_IM_TXL_UPDATE);
		getActivity().registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				if (BroadCastAction.ACTION_IM_TXL_UPDATE.equals(intent)) {
					getFriendList();
				} else {
					downRefreshData();
				}
			} else {
				adapter.getSortUserList().clear();
				adapter.notifyDataSetChanged();
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
			}
		}
	};

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}
}
