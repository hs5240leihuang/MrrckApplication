package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
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
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 装修攻略--在线问答
 * 
 */
public class StrategyAskFragment extends BaseFragment {

	private View layout_view;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<PostsEntity> showAdapter;
	private List<PostsEntity> showList = new ArrayList<PostsEntity>();
	private int TAG_REFRESHPOST = 2;
	private int boardId = -1;
	private int menuId = -1;
	private int topicId = -1;
	private int page = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.onlypulllist, null);
		initView();
		return layout_view;
	}

	private void initView() {
		initPullListView();
	}

	@Override
	public void initValue() {

	}

	protected void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("menuId", menuId);
		map.put("topicId", topicId);
		map.put("loadFlag", 1);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_STRATEGYList));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "getData————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
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

		// 适配器
		showAdapter = new CommonAdapter<PostsEntity>(getActivity(),
				R.layout.item_new_superteacher, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final PostsEntity t) {
				if (TextUtils.isEmpty(t.getTitle())) {
					viewHolder.setText(
							R.id.postTitle,
							EmotionHelper.getLocalEmotion(getActivity(),
									t.getContent()));
				} else {
					viewHolder.setText(
							R.id.postTitle,
							EmotionHelper.getLocalEmotion(getActivity(),
									t.getTitle()));
				}
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				lin_head.removeAllViews();
				MyRoundDraweeView img_head = new MyRoundDraweeView(
						getActivity());
				lin_head.addView(img_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.postAuthor, t.getNickName());
				viewHolder.setText(R.id.tv_time, t.getClientCreateDate());
				viewHolder.setText(R.id.tv_view, t.getViewNum() + "");
				if (t.getPostsAttachmentList().size() > 0
						&& t.getPostsAttachmentList().size() < 3) {
					viewHolder.getView(R.id.lin_allimg)
							.setVisibility(View.GONE);
					viewHolder.getView(R.id.layout_postImg).setVisibility(
							View.VISIBLE);
					LinearLayout layout_postImg = viewHolder
							.getView(R.id.layout_postImg);
					layout_postImg.removeAllViews();
					MySimpleDraweeView postImg = new MySimpleDraweeView(
							getActivity());
					layout_postImg.addView(postImg,
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
					postImg.setUrlOfImage(t.getPostsAttachmentList().get(0)
							.getClientPicUrl());
				} else if (t.getPostsAttachmentList().size() == 0) {
					viewHolder.getView(R.id.lin_allimg)
							.setVisibility(View.GONE);
					viewHolder.getView(R.id.layout_postImg).setVisibility(
							View.GONE);

				} else if (t.getPostsAttachmentList().size() >= 3) {
					viewHolder.getView(R.id.layout_postImg).setVisibility(
							View.GONE);
					viewHolder.getView(R.id.lin_allimg).setVisibility(
							View.VISIBLE);
					LinearLayout lin_img1 = viewHolder.getView(R.id.lin_img1);
					lin_img1.removeAllViews();
					MySimpleDraweeView img1 = new MySimpleDraweeView(
							getActivity());
					lin_img1.addView(img1, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img1.setUrlOfImage(t.getPostsAttachmentList().get(0)
							.getClientPicUrl());
					LinearLayout lin_img2 = viewHolder.getView(R.id.lin_img2);
					lin_img2.removeAllViews();
					MySimpleDraweeView img2 = new MySimpleDraweeView(
							getActivity());
					lin_img2.addView(img2, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img2.setUrlOfImage(t.getPostsAttachmentList().get(1)
							.getClientPicUrl());
					LinearLayout lin_img3 = viewHolder.getView(R.id.lin_img3);
					lin_img3.removeAllViews();
					MySimpleDraweeView img3 = new MySimpleDraweeView(
							getActivity());
					lin_img3.addView(img3, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img3.setUrlOfImage(t.getPostsAttachmentList().get(2)
							.getClientPicUrl());
				}

				// viewHolder.setImageWithNewSize(R.id.postImg,
				// t.getClientThumbHeadPicUrl(), 150, 150);

				// if (t.getHotFlag() == 1) {
				// /** 热门: 0普通 1热门 */
				// viewHolder.setImage(R.id.img_re, R.drawable.re);
				// viewHolder.getView(R.id.img_re).setVisibility(View.VISIBLE);
				// } else {
				// viewHolder.getView(R.id.img_re).setVisibility(View.GONE);
				// }
				// if (t.getGoodFlag() == 1) {
				// /** 推荐标识: 0普通 1精华 */
				// viewHolder.setImage(R.id.img_jing, R.drawable.jing);
				// viewHolder.getView(R.id.img_jing).setVisibility(
				// View.VISIBLE);
				// } else {
				// viewHolder.getView(R.id.img_jing).setVisibility(View.GONE);
				// }
				// if (t.getOfficeFlag() == 1) {
				// /** 数据来源: 0:普通(用户自创建) 1:官方 默认:0 */
				// viewHolder.setImage(R.id.img_guan, R.drawable.guan);
				// viewHolder.getView(R.id.img_guan).setVisibility(
				// View.VISIBLE);
				// } else {
				// viewHolder.getView(R.id.img_guan).setVisibility(View.GONE);
				// }
				// if (t.getTopFlag() == 1) {
				// /** 置顶 : 0不置顶 1置顶 */
				// viewHolder.setImage(R.id.img_ding, R.drawable.top);
				// viewHolder.getView(R.id.img_ding).setVisibility(
				// View.VISIBLE);
				// } else {
				// viewHolder.getView(R.id.img_ding).setVisibility(View.GONE);
				// }
				// String voteFlag = t.getVoteFlag();
				// if (t.getActiveFlag() == 1) {// 是活动帖
				// viewHolder.setImage(R.id.img_huodong,
				// R.drawable.icon_toupiaotiezi);
				// if (FLAG_VOTE_FINISH.equals(voteFlag)) {// 投票结束
				// viewHolder.setText(R.id.voteStatus, "(投票已结束)");
				// } else if (FLAG_VOTE_ING.equals(voteFlag)) {
				// viewHolder.setText(R.id.voteStatus, "(投票中)");
				// } else if (FLAG_VOTE_NOTSTART.equals(voteFlag)) {
				// viewHolder.setText(R.id.voteStatus, "(投票未开始)");
				// }
				// } else {
				// viewHolder.getView(R.id.voteStatus)
				// .setVisibility(View.GONE);
				// viewHolder.getView(R.id.img_huodong).setVisibility(
				// View.GONE);
				// }
				//
				viewHolder.getView(R.id.postTitle).setOnClickListener(
						new PostClick(t.getPostsId(), String.valueOf(t
								.getBoardId()), t.getActiveFlag()));
				viewHolder.getView(R.id.postLayout).setOnClickListener(
						new PostClick(t.getPostsId(), String.valueOf(t
								.getBoardId()), t.getActiveFlag()));
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);

	}

	/**
	 * 帖子点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class PostClick implements OnClickListener {

		private int postsId;
		private String boardId;
		private int activeFlag;

		public PostClick(int postsId, String boardId, int activeFlag) {
			this.postsId = postsId;
			this.boardId = boardId;
			this.activeFlag = activeFlag;
		}

		@Override
		public void onClick(View arg0) {
			LogUtil.d("hl", "当前帖子:ActiveFlag=" + activeFlag + ",postsId="
					+ postsId);
			if (activeFlag == 2) {
				Intent intent = new Intent(getActivity(),
						PostDetailNewActivity.class);
				intent.putExtra(ConstantKey.KEY_POSTID, postsId + "");
				intent.putExtra(ConstantKey.KEY_BOARDID, boardId);
				intent.putExtra("isflag", false);
				startActivityForResult(intent, TAG_REFRESHPOST);
			}
		}

	}

	// 上拉加载更多数据
	private void upRefreshData() {
		page++;
		getData();
	}

	// 下拉重新刷新页面
	public void downRefreshData() {
		showList.clear();
		page = 1;
		getData();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "" + resp.getBody());
		if (!Tool.isEmpty(resp.getBody().get("dataList"))
				&& resp.getBody().get("dataList").toString().length() > 2) {
			List<PostsEntity> postList = (List<PostsEntity>) JsonUtil
					.jsonToList(resp.getBody().get("dataList").toString(),
							new TypeToken<List<PostsEntity>>() {
							}.getType());
			if (!Tool.isEmpty(postList)) {
				showList.addAll(postList);
			}
		}
		showAdapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}

	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

}
