package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 群组搜索
 */
public class GroupSearchActivity extends BaseActivity {
	private PullToRefreshListView mPullToRefreshListView;
	private CommonAdapter<GroupEntity> showAdapter;
	private TextView cancel;
	private ClearEditText etSearch;
	private List<GroupEntity> groupList = new ArrayList<GroupEntity>();
	private String groupName = "";

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_kugroupsearch;
	}

	@Override
	public void initView() {
		etSearch = (ClearEditText) findViewById(R.id.et_msg_search);
		etSearch.setHint("请输入群组关键词搜索");
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				groupName = etSearch.getText().toString();
				if (!Tool.isEmpty(groupName)) {
					downRefreshData();
				}
			}
		});
		cancel = (TextView) findViewById(R.id.cancel_text);
		cancel.setVisibility(View.VISIBLE);
		cancel.setTextSize(17);
		cancel.setBackgroundColor(getResources().getColor(R.color.transparent));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputTools.HideKeyboard(etSearch);
				finish();
			}
		});
		initPullListView();
	}

	@Override
	public void initValue() {
	}

	private void initPullListView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		showAdapter = new CommonAdapter<GroupEntity>(this,
				R.layout.item_im_recommendgroupnew, groupList) {

			@Override
			public void convert(ViewHolder viewHolder, final GroupEntity t) {

				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(
						GroupSearchActivity.this);
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
						View tagView = LayoutInflater.from(
								GroupSearchActivity.this).inflate(
								R.layout.view_kugrouptag, null, false);
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
								// LogUtil.d("hl","点击的群组id----"+ t.getId());
								// Iterator<Entry<Integer, Integer>> iter =
								// AppContext.getGroupMap().entrySet().iterator();
								// while (iter.hasNext()) {
								// Map.Entry entry = (Map.Entry)iter.next();
								// LogUtil.d("hl","我的群组id==="+ entry.getKey());
								// }

								if (AppContext.getGroupMap().containsKey(
										t.getId())) {
									if (!AppContext.getInstance()
											.isLoginedAndInfoPerfect()) {
										ShowLoginDialogUtil
												.showTipToLoginDialog(GroupSearchActivity.this);
										return;
									}
									Intent intent = new Intent(
											GroupSearchActivity.this,
											ChatActivity.class);
									intent.putExtra(
											ConstantKey.KEY_IM_TALKTO_NAME,
											t.getGroupName());
									intent.putExtra(ConstantKey.KEY_IM_TALKTO,
											t.getId());
									intent.putExtra(
											ConstantKey.KEY_IM_TALKTOACCOUNT,
											t.getOtherId());
									intent.putExtra(
											ConstantKey.KEY_IM_SESSIONTYPE,
											XmppConstant.IM_CHAT_TALKTYPE_GROUPTALK);
									startActivity(intent);
								} else {
									Intent i = new Intent(
											GroupSearchActivity.this,
											GroupInfoActivity.class);
									i.putExtra(
											ConstantKey.KEY_IM_MULTI_CHATROOMID,
											t.getId() + "");
									startActivity(i);
								}
							}
						});
			}
		};
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
		mPullToRefreshListView.setAdapter(showAdapter);
		mPullToRefreshListView.getRefreshableView().setOnTouchListener(
				new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// 触摸隐藏键盘
						InputTools.HideKeyboard(etSearch);
						return false;
					}
				});
	}

	protected void upRefreshData() {
		page++;
		getGroupList();
	}

	protected void downRefreshData() {
		page = 1;
		groupList.clear();
		getGroupList();
	}

	/**
	 * 获取搜索群组
	 */
	private void getGroupList() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupName", groupName);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_IM_GROUP_SEARCH));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req, false);
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "==group search==>" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			try {
				List<GroupEntity> data = new ArrayList<GroupEntity>();
				if (!Tool.isEmpty(resp.getBody().get("group"))
						&& resp.getBody().get("group").toString().length() > 2) {
					data = (List<GroupEntity>) JsonUtil.jsonToList(resp
							.getBody().get("group").toString(),
							new TypeToken<List<GroupEntity>>() {
							}.getType());
					groupList.addAll(data);
					showAdapter.notifyDataSetChanged();
				} else {
					if (Tool.isEmpty(groupList)) {
						ToastUtil.showShortToast("无匹配群组");
						groupList.clear();
						showAdapter.notifyDataSetChanged();
					} else {
						ToastUtil.showShortToast("无更多群组");
					}
				}
				mPullToRefreshListView.onRefreshComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			if (mPullToRefreshListView != null) {
				mPullToRefreshListView.post(new Runnable() {

					@Override
					public void run() {
						mPullToRefreshListView.onRefreshComplete();
					}
				});
			}
			break;
		default:
			break;
		}
	}

}
