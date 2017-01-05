package com.meiku.dev.ui.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.BoardUserDTO;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 版块成员列表页面
 * 
 */
public class MemberListActivity extends BaseActivity {

	private PullToRefreshListView mPullToRefreshListView;
	private CommonAdapter<BoardUserDTO> showAdapter;
	private List<BoardUserDTO> showList = new ArrayList<BoardUserDTO>();
	private String boardId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_member_list;
	}

	@Override
	public void initView() {
		initPullListView();
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullToRefreshListView
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
		showAdapter = new CommonAdapter<BoardUserDTO>(MemberListActivity.this,
				R.layout.community_member_item, showList) {

			@Override
			public void convert(ViewHolder viewHolder, BoardUserDTO t) {
				viewHolder.setText(R.id.tv_name, t.getNickName());
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(
						MemberListActivity.this);
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				String intro = t.getIntroduce();
				if (Tool.isEmpty(intro)) {
					intro = "我期望通过手艺交同行朋友";
				}
				viewHolder.setText(R.id.tv_intro, intro);
				viewHolder.setText(R.id.tv_major, t.getPositionName());
				TextView txt_age = viewHolder.getView(R.id.txt_age);
				if (Tool.isEmpty(t.getAgeValue())) {
					txt_age.setText("未知");
				} else {
					txt_age.setText(t.getAgeValue());
				}
				// ** 性别1男2女 */
				if ("1".equals(t.getGender())) {
					Drawable drawable = ContextCompat.getDrawable(
							MemberListActivity.this, R.drawable.nan_white);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					txt_age.setCompoundDrawables(drawable, null, null, null);
					txt_age.setBackgroundResource(R.drawable.nanxing);
				} else {
					Drawable drawable = ContextCompat.getDrawable(
							MemberListActivity.this, R.drawable.nv_white);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					txt_age.setCompoundDrawables(drawable, null, null, null);
					txt_age.setBackgroundResource(R.drawable.nvxing);
				}
			}

		};
		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent i = new Intent(MemberListActivity.this,
								PersonShowActivity.class);
						i.putExtra("toUserId", showList.get(arg2 - 1).getId());
						startActivity(i);
					}
				});
		mPullToRefreshListView.setAdapter(showAdapter);
	}

	private void upRefreshData() {
		++page;
		getData();
	}

	private void downRefreshData() {
		page = 1;
		showList.clear();
		getData();
	}

	private void getData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_MEMBER));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, reqBase, true);
	}

	@Override
	public void initValue() {
		boardId = getIntent().getStringExtra("boardId");
		getData();
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			LogUtil.e(reqBase.getBody().toString());
			try {
				JsonObject body = JsonUtil.String2Object(reqBase.getBody()
						.toString());
				List<BoardUserDTO> postList = (List<BoardUserDTO>) JsonUtil
						.jsonToList(body.get("boardUserList").toString(),
								new TypeToken<List<BoardUserDTO>>() {
								}.getType());
				LogUtil.e(postList.size() + "");
				if (!Tool.isEmpty(postList)) {
					showList.addAll(postList);
					showAdapter.setmDatas(showList);
					showAdapter.notifyDataSetChanged();
				} else {
					ToastUtil.showShortToast("无更多成员");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mPullToRefreshListView.onRefreshComplete();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

}
