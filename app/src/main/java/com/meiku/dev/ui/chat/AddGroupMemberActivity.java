package com.meiku.dev.ui.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CreateGroupChatAdapter;
import com.meiku.dev.bean.CreateGroupChatUser;
import com.meiku.dev.bean.Friend;
import com.meiku.dev.bean.GroupUserDTO;
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
 * 邀请群成员
 * 
 * @author nathan
 * 
 */
public class AddGroupMemberActivity extends BaseActivity {

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

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_add_group_member;
	}

	@Override
	public void initView() {
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
				List<GroupUserDTO> tempList = new ArrayList<GroupUserDTO>();
				for (int i = 0; i < sortUserList.size(); i++) {
					if (sortUserList.get(i).getIsCheck()
							&& !sortUserList.get(i).getIsAddGroup()) {
						GroupUserDTO groupUserDTO = new GroupUserDTO();
						groupUserDTO.setUserId(sortUserList.get(i)
								.getFriendId() + "");
						groupUserDTO.setNickName(sortUserList.get(i)
								.getMkNickname());
						tempList.add(groupUserDTO);
					}
				}
				map.put("groupUserList", tempList);
				req.setHeader(new ReqHead(AppConfig.GP_18044));
				req.setBody(JsonUtil.Map2JsonObj(map));
				httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req);
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
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("groupId", groupId);
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_NEARBY_GROUPMEMBERCHECK));
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
						if (sortUserList.get(arg2).getIsAddGroup()) {

						} else {
							if (sortUserList.get(arg2).getIsCheck()) {
								sortUserList.get(arg2).setIsCheck(false);
								setChoose(--chooseNum);
							} else {
								sortUserList.get(arg2).setIsCheck(true);
								setChoose(++chooseNum);
							}
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
						if (tempSortUserList.get(arg2).getIsAddGroup()) {

						} else {
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
						}
						adapter.notifyDataSetChanged();
						et_search.setText("");
					}
				});
		layout_empty.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
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

	public class PinyinComparator implements Comparator<Friend> {

		public int compare(Friend o1, Friend o2) {
			// 这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
			if (o2.getNameFirstChar().equals("#")) {
				return -1;
			} else if (o1.getNameFirstChar().equals("#")) {
				return 1;
			} else {
				return o1.getNameFirstChar().compareTo(o2.getNameFirstChar());
			}
		}
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			sortUserList.clear();
			String jsonstr = resp.getBody().get("friend").toString();
			List<Friend> data = new ArrayList<Friend>();
			try {
				if (!Tool.isEmpty(jsonstr) && jsonstr.length() > 2) {
					data = (List<Friend>) JsonUtil.jsonToList(jsonstr,
							new TypeToken<List<Friend>>() {
							}.getType());
					Collections.sort(data, new PinyinComparator());
					for (int i = 0; i < data.size(); i++) {
						CreateGroupChatUser tempCreateGroupChatUser = new CreateGroupChatUser();
						tempCreateGroupChatUser.setFriendId(data.get(i)
								.getFriendId());
						if (data.get(i).getAddGroupFlag().equals("1")) {
							tempCreateGroupChatUser.setIsAddGroup(true);
							tempCreateGroupChatUser.setIsCheck(true);
						} else {
							tempCreateGroupChatUser.setIsCheck(false);
						}
						tempCreateGroupChatUser.setMkHeadurl(data.get(i)
								.getClientThumbHeadPicUrl());
						tempCreateGroupChatUser.setSortLetters(data.get(i)
								.getNameFirstChar().equals("") ? data.get(i)
								.getNameFirstChar() : PinyinUtil.getTerm(data
								.get(i).getAliasName()));
						tempCreateGroupChatUser.setMkNickname(data.get(i)
								.getAliasName());
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
			// ToastUtil.showShortToast("创建成功");
			// startActivity(new
			// Intent(AddGroupMemberActivity.this,ChooseGroupActivity.class));
			// GroupEntity bean = new GroupEntity();
			// bean.setId((int)(Math.random()*10000));
			// AsmarkMethod.createRoom(bean);
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
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
			right_txt_title.setTextColor(Color.BLACK);
			right_txt_title.setText("确定(" + chooseNum + ")");
			right_txt_title.setEnabled(true);
		} else {
			right_txt_title.setText("确定");
			right_txt_title.setEnabled(false);
			right_txt_title.setTextColor(Color.WHITE);
		}
		layout_headimage.removeAllViews();
		for (int i = 0; i < sortUserList.size(); i++) {
			if (sortUserList.get(i).getIsCheck()
					&& !sortUserList.get(i).getIsAddGroup()) {
				MySimpleDraweeView imageView = new MySimpleDraweeView(this);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ScreenUtil.dip2px(this, 37),
						ScreenUtil.dip2px(this, 37));
				params.setMargins(0, 0, ScreenUtil.dip2px(this, 10), 0);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageView.setLayoutParams(params);
				if (!Tool.isEmpty(sortUserList.get(i).getMkHeadurl())) {
					imageView.setUrlOfImage(sortUserList.get(i).getMkHeadurl());
				}
				layout_headimage.addView(imageView);
			}
		}
		scrollView.fullScroll(ScrollView.FOCUS_RIGHT);
	}
}
