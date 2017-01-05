package com.meiku.dev.ui.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CreateGroupChatAdapter;
import com.meiku.dev.bean.CreateGroupChatUser;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.EnLetterView;
import com.meiku.dev.views.MaxWidthHorizontalSrollView;
import com.meiku.dev.views.MySimpleDraweeView;

/**
 * 删除群成员
 * 
 * @author nathan
 * 
 */
public class DeleteMemberActivity extends BaseActivity {

	private ListView friendListView, list_friends_search;
	private EnLetterView rightLetter;
	private TextView dialogTextView, right_txt_title;
	private CreateGroupChatAdapter adapter, searchAdapter;
	private List<CreateGroupChatUser> sortUserList = new ArrayList<CreateGroupChatUser>();
	private List<CreateGroupChatUser> tempSortUserList = new ArrayList<CreateGroupChatUser>();
	private LinearLayout layout_headimage, layout_empty;
	private MaxWidthHorizontalSrollView scrollView;
	private EditText et_search;
	private RelativeLayout layout_list;

	private int chooseNum = 0;// 右上角button的选中人数
	private String groupId;
	protected String userIds = "";

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_delete_member;
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		// scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
		et_search = (EditText) findViewById(R.id.et_search);
		layout_list = (RelativeLayout) findViewById(R.id.layout_list);
		list_friends_search = (ListView) findViewById(R.id.list_friends_search);
		layout_empty = (LinearLayout) findViewById(R.id.layout_empty);

		scrollView = (MaxWidthHorizontalSrollView) findViewById(R.id.scrollView);

		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackground(null);
		right_txt_title.setEnabled(false);
		layout_headimage = (LinearLayout) findViewById(R.id.layout_headimage);
		friendListView = (ListView) findViewById(R.id.list_friends);
		adapter = new CreateGroupChatAdapter(this);
		friendListView.setAdapter(adapter);

		searchAdapter = new CreateGroupChatAdapter(this);
		list_friends_search.setAdapter(searchAdapter);
		searchAdapter.setData(tempSortUserList);

		initRightLetterViewAndSearchEdit();

		right_txt_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("groupId", groupId);
				userIds = "";
				// List<GroupUserDTO> tempList = new ArrayList<GroupUserDTO>();
				for (int i = 0; i < sortUserList.size(); i++) {
					if (sortUserList.get(i).getIsCheck()) {
						// GroupUserDTO groupUserDTO = new GroupUserDTO();
						// groupUserDTO.setUserId(sortUserList.get(i).getFriendId()+"");
						// groupUserDTO.setNickName(sortUserList.get(i).getMkNickname());
						// tempList.add(groupUserDTO);
						userIds = userIds + sortUserList.get(i).getFriendId()
								+ ",";
					}
				}
				if (!TextUtils.isEmpty(userIds)) {
					userIds = userIds.substring(0, userIds.length() - 1);
					map.put("userIds", userIds);
					map.put("leanCloudUserName", AppContext.getInstance().getUserInfo().getLeanCloudUserName());
					req.setHeader(new ReqHead(
							AppConfig.BUSINESS_NEARBY_DELMEMBER));
					req.setBody(JsonUtil.Map2JsonObj(map));
					httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req);
				} else {
					ToastUtil.showShortToast("请选择要踢除的联系人");
				}

			}
		});
	}

	@Override
	public void initValue() {
		groupId = getIntent().getStringExtra("groupId");
		getFriendList();
	}

	public void getFriendList() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_NEARBY_GROUPMEMBER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}

	private void show(int flag) {
		if (flag == 1) {
			layout_list.setVisibility(View.GONE);
			list_friends_search.setVisibility(View.VISIBLE);
		} else {
			layout_list.setVisibility(View.VISIBLE);
			list_friends_search.setVisibility(View.GONE);
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	public void bindListener() {
		friendListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						if (sortUserList.get(arg2).getIsCheck()) {
							sortUserList.get(arg2).setIsCheck(false);
							setChoose(--chooseNum);
						} else {
							sortUserList.get(arg2).setIsCheck(true);
							setChoose(++chooseNum);
						}
						adapter.notifyDataSetChanged();
					}
				});

		list_friends_search
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						show(0);

						if (tempSortUserList.get(arg2).getIsCheck()) {
							sortUserList.get(
									sortUserList.indexOf(tempSortUserList
											.get(arg2))).setIsCheck(false);
							setChoose(--chooseNum);
						} else {
							sortUserList.get(
									sortUserList.indexOf(tempSortUserList
											.get(arg2))).setIsCheck(true);
							setChoose(++chooseNum);
						}
						adapter.notifyDataSetChanged();
						et_search.setText("");
					}
				});

		// et_search.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		//
		// }
		// });

		layout_empty.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				et_search.setText("");
				show(0);
				// layout_list.setVisibility(View.VISIBLE);
				// list_friends_search.setVisibility(View.GONE);
				// layout_choosegroup.setVisibility(View.VISIBLE);
				// InputMethodManager imm = (InputMethodManager)
				// getSystemService(INPUT_METHOD_SERVICE);
				// imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				// 0);
			}
		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				tempSortUserList.clear();
				if (!arg0.toString().equals("")) {
					show(1);
					for (int i = 0; i < sortUserList.size(); i++) {
						if (sortUserList.get(i).getMkNickname()
								.contains(arg0.toString())) {
							tempSortUserList.add(sortUserList.get(i));
						}
					}
				}
				searchAdapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
//		Iterator<Entry<Integer, GroupChatDTO>> iter;
		switch (requestCode) {
		case reqCodeOne:
			sortUserList.clear();
			String jsonstr = resp.getBody().get("groupUser").toString();
			List<GroupUserEntity> data = new ArrayList<GroupUserEntity>();
			try {
				data = (List<GroupUserEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<GroupUserEntity>>() {
						}.getType());
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).getUserId() != AppContext.getInstance()
							.getUserInfo().getUserId()) {
						CreateGroupChatUser tempCreateGroupChatUser = new CreateGroupChatUser();
						tempCreateGroupChatUser.setFriendId(data.get(i)
								.getUserId());
						tempCreateGroupChatUser.setIsCheck(false);
						tempCreateGroupChatUser.setMkHeadurl(data.get(i)
								.getClientHeadPicUrl());
						tempCreateGroupChatUser.setSortLetters(PinyinUtil
								.getTerm(data.get(i).getNickName()));
						tempCreateGroupChatUser.setMkNickname(data.get(i)
								.getNickName());
						sortUserList.add(tempCreateGroupChatUser);
					}
				}
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(data)) {
				ToastUtil.showShortToast("暂无好友");
			} else {
				adapter.setData(sortUserList);
			}
			adapter.notifyDataSetChanged();
			break;
		case reqCodeTwo:
//			// iter = IMChatService.groupChatMap
//			// .entrySet().iterator();
//			// while (iter.hasNext()) {
//			// Map.Entry entry = (Map.Entry) iter.next();
//			// LogUtil.d("hl", "我的群组id===" + entry.getKey());
//			// }
//			LogUtil.d("hl", "_" + groupId + "选中ids====" + userIds);
//			if (IMChatService.groupChatMap.containsKey(groupId)) {
//				try {
//					LogUtil.d("hl", "选中ids_" + userIds);
//					MultiUserChat muc = IMChatService.groupChatMap.get(groupId)
//							.getMultiUserChat();
//					String[] ids = userIds.split(",");
//					for (int i = 0, size = ids.length; i < size; i++) {
//						muc.kickParticipant(ids[i], "群主踢人");
//					}
//				} catch ( Exception e) {
//					e.printStackTrace();
//				}
//			}

			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeTwo:
			ToastUtil.showShortToast("失败，请重新删除");
			getFriendList();
			break;

		default:
			break;
		}
	}

	private void initRightLetterViewAndSearchEdit() {
		rightLetter = (EnLetterView) findViewById(R.id.right_letter);
		dialogTextView = (TextView) findViewById(R.id.dialog);
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
				friendListView.setSelection(position);
			}
		}
	}

	private void setChoose(int chooseNum) {
		if (chooseNum > 0) {
			right_txt_title
					.setTextColor(getResources().getColor(R.color.black));
			right_txt_title.setEnabled(true);
			right_txt_title.setText("删除(" + chooseNum + ")");
		} else {
			right_txt_title.setText("删除");
			right_txt_title.setTextColor(getResources().getColor(
					R.color.white));
			right_txt_title.setEnabled(false);
		}
		layout_headimage.removeAllViews();
		for (int i = 0; i < sortUserList.size(); i++) {
			if (sortUserList.get(i).getIsCheck()) {
				MySimpleDraweeView imageView = new MySimpleDraweeView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ScreenUtil.dip2px(this, 37),
						ScreenUtil.dip2px(this, 37));
				params.setMargins(0, 0, ScreenUtil.dip2px(this, 10), 0);
				imageView.setLayoutParams(params);
				imageView.setUrlOfImage(sortUserList.get(i).getMkHeadurl());
				layout_headimage.addView(imageView);
			}
		}
		scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
	}
}
