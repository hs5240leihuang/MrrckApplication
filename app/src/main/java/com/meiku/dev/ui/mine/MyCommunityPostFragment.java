package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class MyCommunityPostFragment extends BaseFragment {
	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<PostsEntity> showAdapter;
	private List<PostsEntity> showlist = new ArrayList<PostsEntity>();
	private int page = 1;
	private int userId;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater
				.inflate(R.layout.fragment_threemycommunity, null);
		initPullListView();
		return layout_view;

	}

	@Override
	public void initValue() {
		userId = getArguments().getInt("userId");
		downRefreshData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("00000", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("postsList").toString().length() > 2) {
				List<PostsEntity> listData = (List<PostsEntity>) JsonUtil
						.jsonToList(resp.getBody().get("postsList").toString(),
								new TypeToken<List<PostsEntity>>() {
								}.getType());
				showlist.addAll(listData);
			} else {
				ToastUtil.showShortToast("没有更多数据");

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
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			getActivity().finish();
			break;
		case reqCodeOne:
			ToastUtil.showShortToast("请求数据失败");
			if (null != mPullRefreshListView) {
				mPullRefreshListView.onRefreshComplete();
			}
			break;

		default:
			break;
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

		showAdapter = new CommonAdapter<PostsEntity>(getActivity(),
				R.layout.item_mycommunity_mine, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final PostsEntity t) {
				viewHolder.setText(
						R.id.tv_posttitle,
						EmotionHelper.getLocalEmotion(getActivity(),
								t.getTitle()));
				viewHolder.setText(R.id.tv_laiyuan, t.getMenuName());
				viewHolder.setText(R.id.tv_time, t.getClientViewDate());
				viewHolder.setText(R.id.tv_numberp, t.getCommentNum() + "/");
				viewHolder.setText(R.id.tv_numberlook, t.getViewNum() + "");
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(getActivity(),
										PostDetailNewActivity.class);
								intent.putExtra(ConstantKey.KEY_POSTID,
										t.getPostsId() + "");
								intent.putExtra(ConstantKey.KEY_BOARDID,
										t.getBoardId() + "");
								intent.putExtra("FLAG", "FROMMINECOM");
								startActivityForResult(intent, 100);
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

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MYCOMMUNITYPOST));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req);
	}
}
