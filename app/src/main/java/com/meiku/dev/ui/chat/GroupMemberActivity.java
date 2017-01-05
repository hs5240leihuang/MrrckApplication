package com.meiku.dev.ui.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

public class GroupMemberActivity extends BaseActivity {

	private TextView center_txt_title, right_txt_title;
	private ListView lv_member;
	private String groupId;
	private CommonAdapter<GroupUserEntity> showAdapter;
	private List<GroupUserEntity> data = new ArrayList<GroupUserEntity>();
	private List<GroupUserEntity> searchList = new ArrayList<GroupUserEntity>();

	private ClearEditText et_msg_search;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_group_member;
	}

	@Override
	public void initView() {
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		lv_member = (ListView) findViewById(R.id.lv_member);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_txt_title.setBackground(null);

		// 适配器
		showAdapter = new CommonAdapter<GroupUserEntity>(
				GroupMemberActivity.this, R.layout.item_group_member,
				searchList) {

			@Override
			public void convert(ViewHolder viewHolder, final GroupUserEntity t) {
				// viewHolder.setImageWithNewSize(R.id.iv_head,
				// t.getClientThumbHeadPicUrl(), 150, 150);
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(
						GroupMemberActivity.this);
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.tv_name, t.getNickName());
				viewHolder.getView(R.id.layout_groupmember).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										GroupMemberActivity.this,
										PersonShowActivity.class);
								intent.putExtra(
										PersonShowActivity.TO_USERID_KEY,
										t.getUserId() + "");
								intent.putExtra("nickName", t.getNickName());
								startActivity(intent);
							}
						});
			}
		};
		lv_member.setAdapter(showAdapter);
	}

	@Override
	public void initValue() {
		groupId = getIntent().getStringExtra("groupId");
		getWebData();
	}

	private void getWebData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupId", groupId);
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_NEARBY_GROUPMEMBER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}

	@Override
	public void bindListener() {
		right_txt_title.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(GroupMemberActivity.this,
						AddGroupMemberActivity.class);
				intent.putExtra("groupId", groupId);
				startActivityForResult(intent, 1);
			}
		});
		findViewById(R.id.goback).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(RESULT_OK);
				finish();
			}
		});
		et_msg_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (!Tool.isEmpty(data)) {
					String currentContent = et_msg_search.getText().toString()
							.trim();
					searchList.clear();
					if (Tool.isEmpty(currentContent)) {
						searchList.addAll(data);
						showAdapter.notifyDataSetChanged();
						return;
					}
					for (int i = 0, size = data.size(); i < size; i++) {
						GroupUserEntity fe = data.get(i);
						String aliasName = fe.getNickName();
						String aliasName_pinyin = PinyinUtil.spell(aliasName);// 名字全拼音
						String nameForShort = PinyinUtil.getForShort(aliasName);// 名字缩写
						if (aliasName.contains(currentContent)
								|| aliasName_pinyin.contains(currentContent)
								|| nameForShort.contains(currentContent
										.toUpperCase())) {
							searchList.add(fe);
						}
					}
					showAdapter.notifyDataSetChanged();
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			getWebData();
		}
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			LogUtil.e(resp.getBody().toString());
			data.clear();
			String jsonstr = resp.getBody().get("groupUser").toString();
			if (!Tool.isEmpty(jsonstr) && jsonstr.length() > 2) {
				data = (List<GroupUserEntity>) JsonUtil.jsonToList(jsonstr,
						new TypeToken<List<GroupUserEntity>>() {
						}.getType());
				searchList.addAll(data);
				showAdapter.setmDatas(searchList);
			}
			showAdapter.notifyDataSetChanged();
			center_txt_title.setText("全部群成员（" + data.size() + "）");
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}
}
