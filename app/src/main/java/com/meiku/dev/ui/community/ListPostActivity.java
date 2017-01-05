package com.meiku.dev.ui.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.BoardBean;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.TopicData;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.utils.DropDownAnim;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.TextLongClickListener;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.fab.ButtonFloat;
import com.umeng.analytics.MobclickAgent;

/**
 * 版块详情，帖子列表
 */
public class ListPostActivity extends BaseActivity implements OnClickListener {
	public final int REQCODE_TITLE = 9000;
	private String title = "";
	private PullToRefreshListView mPullRefreshListView;
	private TextView rightTxt, txt_boardtitle, txt_postnum, txt_replynum,
			txt_pay, tv_remark;
	private ImageView img_pay;
	private CommonAdapter<PostsEntity> showAdapter;
	private List<PostsEntity> showList = new ArrayList<PostsEntity>();
	private ButtonFloat buttonFloat;
	// 头部整体的view
	private View topADViewGroup;
	private int curPage = 1;
	private String boardId;
	private BoardBean board;
	private int TAG_ADDNEWPOST = 1;
	private int TAG_REFRESHPOST = 2;
	private String topicId = "-1";
	private String cityCode = "-1";
	private String provinceCode = "-1";
	private LinearLayout layoutMember;
	private final int REQCODE_GETTOPIC = 1;
	private final int REQCODE_ACTIVITYPOSTDETAIL = 2;
	private List<TopicData> tList = new ArrayList<TopicData>();// 所有话题数据
	/** 投票状态 0:投票尚未开始 1:投票进行中 2:投票已结束 */
	private String FLAG_VOTE_FINISH = "2";
	private String FLAG_VOTE_ING = "1";
	private String FLAG_VOTE_NOTSTART = "0";
	private String[] TYPES_POST = new String[] { "全部", "最新", "最热", "精华" };
	private String[] TYPES_POST_VALUE = new String[] { "-1", "1", "2", "3" };
	private LinearLayout layoutHot;
	private String orderByType = "-1";
	private boolean hasDoGuanzhu = false;// 是否有操作
	private HorizontalScrollView scrollViewTopic;
	private LinearLayout headLayout;
	private boolean isExplaned = true;
	private DropDownAnim animationDown, animationUp;// 展开收回动画
	private LinearLayout layoutExplan;
	private LinearLayout topicLayout;
	// private ImageView center_iv;
	private TextView center_tv;
	private String clientHeadImgUrl;
	protected String topicString;
	private int oldItem = 0;
	private MySimpleDraweeView headImg;

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

	private void initHeadView() {

		LinearLayout layout_addImage = (LinearLayout) topADViewGroup
				.findViewById(R.id.layout_addImage);
		headImg = new MySimpleDraweeView(ListPostActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(headImg, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout_addImage.setOnClickListener(this);

		layoutMember = (LinearLayout) topADViewGroup
				.findViewById(R.id.layoutMember);// 成员
		layoutHot = (LinearLayout) topADViewGroup.findViewById(R.id.layoutHot);// 热门
		txt_boardtitle = (TextView) topADViewGroup
				.findViewById(R.id.txt_boardtitle);
		txt_postnum = (TextView) topADViewGroup.findViewById(R.id.txt_postnum);
		txt_replynum = (TextView) topADViewGroup
				.findViewById(R.id.txt_replynum);
		txt_pay = (TextView) topADViewGroup.findViewById(R.id.txt_pay);
		img_pay = (ImageView) topADViewGroup.findViewById(R.id.img_pay);
		img_pay.setOnClickListener(this);

		layoutExplan = (LinearLayout) topADViewGroup
				.findViewById(R.id.layoutExplan);
		layoutExplan.setOnClickListener(this);
		headLayout = (LinearLayout) topADViewGroup
				.findViewById(R.id.headLayout);
		scrollViewTopic = (HorizontalScrollView) topADViewGroup
				.findViewById(R.id.scrollTopic);
		topicLayout = (LinearLayout) topADViewGroup
				.findViewById(R.id.topicLayout);
		topicLayout.removeAllViews();
		tv_remark = (TextView) topADViewGroup.findViewById(R.id.tv_remark);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_listpost;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void initView() {
		rightTxt = (TextView) findViewById(R.id.right_txt_title);
		rightTxt.setBackgroundResource(R.drawable.fatiebackground);
		rightTxt.setTextColor(getResources().getColor(R.color.mrrck_bg));
		rightTxt.setVisibility(View.GONE);
		center_tv = (TextView) findViewById(R.id.center_txt_title);
		// center_iv = (ImageView) findViewById(R.id.center_img);
		// center_iv.setVisibility(View.VISIBLE);
		// center_iv.setBackgroundResource(R.drawable.arowdown);
		center_tv.setOnClickListener(this);
		// center_iv.setOnClickListener(this);
		topADViewGroup = LayoutInflater.from(ListPostActivity.this).inflate(
				R.layout.view_boardshow, null, false);

		buttonFloat = (ButtonFloat) findViewById(R.id.buttonFloat);
		initPullListView();
		initHeadView();
	}

	@Override
	public void initValue() {
		boardId = getIntent().getStringExtra(ConstantKey.KEY_BOARDID);
		String titleStr = getIntent()
				.getStringExtra(ConstantKey.KEY_BOARDTITLE);
		getTopic(boardId);
	}

	/**
	 * 获取所有话题
	 * 
	 * @param boardId
	 */
	private void getTopic(String boardId) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		reqBase.setHeader(new ReqHead(AppConfig.COMM_GETTOPIC));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(REQCODE_GETTOPIC, AppConfig.PUBLICK_BOARD, reqBase, true);
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.getRefreshableView().addHeaderView(topADViewGroup,
				null, false);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						title = "";
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						upRefreshData();
					}
				});
		mPullRefreshListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > oldItem) {
					buttonFloat.setVisibility(View.INVISIBLE);
				} else if (firstVisibleItem < oldItem) {
					buttonFloat.setVisibility(View.VISIBLE);
				} else {

				}
				oldItem = firstVisibleItem;
			}
		});

		// 适配器
		showAdapter = new CommonAdapter<PostsEntity>(ListPostActivity.this,
				R.layout.item_new_superteacher, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final PostsEntity t) {
				if (TextUtils.isEmpty(t.getTitle())) {
					viewHolder.setText(R.id.postTitle, EmotionHelper
							.getLocalEmotion(ListPostActivity.this,
									t.getContent()));
				} else {
					viewHolder.setText(R.id.postTitle, EmotionHelper
							.getLocalEmotion(ListPostActivity.this,
									t.getTitle()));
				}
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				lin_head.removeAllViews();
				MyRoundDraweeView img_head = new MyRoundDraweeView(
						ListPostActivity.this);
				lin_head.addView(img_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.postAuthor, t.getNickName());
				viewHolder.setText(R.id.tv_time, t.getClientCreateDate());
				viewHolder.setText(R.id.tv_view, "" + t.getViewNum());
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
							ListPostActivity.this);
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
							ListPostActivity.this);
					lin_img1.addView(img1, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img1.setUrlOfImage(t.getPostsAttachmentList().get(0)
							.getClientPicUrl());
					LinearLayout lin_img2 = viewHolder.getView(R.id.lin_img2);
					lin_img2.removeAllViews();
					MySimpleDraweeView img2 = new MySimpleDraweeView(
							ListPostActivity.this);
					lin_img2.addView(img2, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img2.setUrlOfImage(t.getPostsAttachmentList().get(1)
							.getClientPicUrl());
					LinearLayout lin_img3 = viewHolder.getView(R.id.lin_img3);
					lin_img3.removeAllViews();
					MySimpleDraweeView img3 = new MySimpleDraweeView(
							ListPostActivity.this);
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
			if (activeFlag == 0) {
				Intent intent = new Intent(ListPostActivity.this,
						PostDetailNewActivity.class);
				intent.putExtra(ConstantKey.KEY_POSTID, postsId + "");
				intent.putExtra(ConstantKey.KEY_BOARDID, boardId);
				intent.putExtra("isflag", false);
				startActivityForResult(intent, TAG_REFRESHPOST);
			} else if (activeFlag == 1) {
				// 获取详细,得知VoteFlag,跳转不同状态下的页面
				// getActivityPostData(postsId);

				// 新的活动页面
				Intent intent = new Intent(ListPostActivity.this,
						ShowMainActivity.class);
				intent.putExtra("postsId", postsId);
				startActivity(intent);
			}
		}

	}

	/**
	 * 活动帖详情
	 */
	private void getActivityPostData(String postsId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("postsId", postsId);
		map.put("page", 1);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_VOTE_POSTDETAIL));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(REQCODE_ACTIVITYPOSTDETAIL, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	public void bindListener() {
		rightTxt.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		layoutMember.setOnClickListener(this);

		buttonFloat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(ListPostActivity.this,
						SearchPostActivity.class), REQCODE_TITLE);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		mPullRefreshListView.onRefreshComplete();
		LogUtil.d("wangke", resp.getBody() + "");
		switch (requestCode) {
		case reqCodeOne:
			try {
				if (!Tool.isEmpty(resp.getBody().get("board"))
						&& resp.getBody().get("board").toString().length() > 2) {

					board = (BoardBean) JsonUtil.jsonToObj(BoardBean.class,
							resp.getBody().get("board").toString());
					rightTxt.setVisibility(board.getUserId() == AppContext
							.getInstance().getUserInfo().getId() ? View.VISIBLE
							: View.GONE);
					headImg.setUrlOfImage(board.getClientImgUrl()
							+ ConstantKey.THUMB);
					clientHeadImgUrl = board.getClientImgUrl();
					// String code = ConstantKey.QR_CODE_BOARD + boardId + "&"
					// + board.getMenuId();
					tv_remark.setText(board.getRemark());
					tv_remark.setOnLongClickListener(new TextLongClickListener(
							ListPostActivity.this, tv_remark));
					txt_boardtitle.setText(board.getName());
					txt_postnum.setText(board.getPostsNum() + "");
					txt_replynum.setText(board.getCommentNum() + "");
					setGuanzhuStatus();
					layoutHot
							.setVisibility(board.getHotFlag() == 1 ? View.VISIBLE
									: View.GONE); // 热门: 0普通 1热门
					showList.clear();
					if (!Tool.isEmpty(board)
							&& !Tool.isEmpty(board.getPostsList())) {
						showList.addAll(board.getPostsList());
					} else {
						if (title.equals("")) {
							ToastUtil.showShortToast("没有更多帖子了");
						} else {
							ToastUtil.showShortToast("没有找到关于" + title + "的内容");
						}
					}
					showAdapter.setmDatas(showList);
					showAdapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case reqCodeTwo:
			List<PostsEntity> postsList = (List<PostsEntity>) JsonUtil
					.jsonToList(resp.getBody().get("postsList").toString(),
							new TypeToken<List<PostsEntity>>() {
							}.getType());
			if (!Tool.isEmpty(postsList)) {
				showList.addAll(postsList);
				showAdapter.setmDatas(showList);
				showAdapter.notifyDataSetChanged();
			} else {
				ToastUtil.showShortToast("没有更多帖子了");
			}
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("关注成功");
			board.setAddFlag(1);
			setGuanzhuStatus();
			hasDoGuanzhu = true;
			break;
		case reqCodeFour:
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ToastUtil.showShortToast("取消关注");
				board.setAddFlag(0);
				setGuanzhuStatus();
				hasDoGuanzhu = true;
			} else {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
			}
			break;
		case REQCODE_GETTOPIC:
			LogUtil.e(arg0.toString());
			String temp = resp.getBody().toString();
			if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil
					.getJSONType(temp)) {
				tList.clear();
				tList = (List<TopicData>) JsonUtil.jsonToList(
						(JsonUtil.String2Object(temp).get("topic")).toString(),
						new TypeToken<List<TopicData>>() {
						}.getType());
				tList.add(0, new TopicData("全部", -1));
				for (int i = 0, size = tList.size(); i < size; i++) {
					addTopicView(i, tList.get(i));
				}
			}
			downRefreshData();
			break;

		case REQCODE_ACTIVITYPOSTDETAIL:
			// PostsEntity pe = (PostsEntity) JsonUtil.jsonToObj(
			// PostsEntity.class, resp.getBody().get("postEntity")
			// .toString());
			// if (!Tool.isEmpty(pe)) {
			// String voteFlag = pe.getVoteFlag();
			// LogUtil.d("hl", "当前帖子VoteFlag=" + voteFlag);
			// if (FLAG_VOTE_FINISH.equals(voteFlag)) {// 投票结束
			// Intent intent = new Intent(context,
			// VoteFinishMainFragment.class);
			// intent.putExtra(ConstantKey.KEY_POSTID,
			// String.valueOf(pe.getPostsId()));
			// startActivity(intent);
			// } else if (FLAG_VOTE_ING.equals(voteFlag)) {
			// Intent intent = new Intent(context,
			// VotingMainFragment.class);
			// intent.putExtra(ConstantKey.KEY_POSTID,
			// String.valueOf(pe.getPostsId()));
			// startActivity(intent);
			// } else if (FLAG_VOTE_NOTSTART.equals(voteFlag)) {
			// Intent intent = new Intent(context, VoteMainFragment.class);
			// intent.putExtra(ConstantKey.KEY_POSTID,
			// String.valueOf(pe.getPostsId()));
			// intent.putExtra(ConstantKey.KEY_SIGNUPFLAG,
			// String.valueOf(pe.getSignupFlag()));
			// startActivity(intent);
			// }
			// } else {
			// ToastUtil.showShortToast("获取活动贴详情出错!");
			// }

			break;
		default:
			break;
		}
	}

	/***
	 * 添加话题view
	 * 
	 * @param i
	 * @param topicData
	 */
	private void addTopicView(int i, TopicData topicData) {
		int distense = ScreenUtil.dip2px(ListPostActivity.this, 0);
		final TextView tv = new TextView(this);
		tv.setText("   " + topicData.getName() + "   ");
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(16);
		tv.setBackgroundResource(i == 0 ? R.drawable.biaoqianroungnew
				: R.drawable.whiteround);
		// 默认选第一个话题
		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, ScreenUtil.dip2px(
						ListPostActivity.this, 40));
		params.setMargins(distense, 0, distense, 0);
		tv.setTextColor(i == 0 ? getResources().getColor(R.color.white) : Color
				.parseColor("#999999"));
		tv.setOnClickListener(new OnTopicClickLisener(i));
		topicLayout.addView(tv, params);
	}

	class OnTopicClickLisener implements OnClickListener {

		private int position;

		public OnTopicClickLisener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			for (int j = 0, size = topicLayout.getChildCount(); j < size; j++) {
				TextView tv = (TextView) topicLayout.getChildAt(j);
				tv.setBackgroundResource(position == j ? R.drawable.biaoqianroungnew
						: R.drawable.whiteround);
				// 默认选第一个话题
				tv.setTextColor(position == j ? getResources().getColor(
						R.color.white) : Color.parseColor("#999999"));
				// if (position==0&&j==0) {
				// tv.setBackgroundResource(R.drawable.biaoqianroungnew);
				// }
			}
			if (position > 0) {
				topicId = tList.get(position).getId() + "";
				topicString = tList.get(position).getName();
			} else {
				topicId = "-1";
				topicString = "";
			}
			downRefreshData();
		}

	}

	/**
	 * 处理是否关注
	 */
	private void setGuanzhuStatus() {
		if (board.getAddFlag() == 1) {
			txt_pay.setText("已关注");
			img_pay.setImageResource(R.drawable.heart_selected);
		} else if (board.getAddFlag() == 0) {
			txt_pay.setText("未关注");
			img_pay.setImageResource(R.drawable.heart_unselect);
		}
	}

	// 上拉加载更多数据
	private void upRefreshData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("page", ++curPage);
		map.put("provinceCode", provinceCode);
		map.put("cityCode", cityCode);
		map.put("topicId", topicId);
		map.put("orderByType", orderByType);
		map.put("title", title);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BANKUAIMEITUEZI));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		showList.clear();
		curPage = 1;
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("pageNum", ConstantKey.PageNum);
		map.put("page", curPage);
		map.put("provinceCode", provinceCode);
		map.put("cityCode", cityCode);
		map.put("topicId", topicId);
		map.put("orderByType", orderByType);
		map.put("title", title);
		LogUtil.d("hl", "请求版块信息_" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BANKUAIANDTIEZI));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			finish();
			break;
		default:
			ToastUtil.showShortToast("获取数据失败！");
			finish();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_addImage:
			if (!"".equals(clientHeadImgUrl)) {
				Intent intentImage = new Intent();
				intentImage.setClass(this, ImagePagerActivity.class);
				intentImage.putExtra("showOnePic", clientHeadImgUrl);
				startActivity(intentImage);
			}
			break;
		case R.id.goback:
			finishWithResult();
			break;
		case R.id.right_txt_title:
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				Intent intent = new Intent(ListPostActivity.this,
						ReleaseTopicActivity.class);
				intent.putExtra(ReleaseTopicActivity.KEY_BOARDID, boardId);
				intent.putExtra(ReleaseTopicActivity.KEY_TOPICID, topicId);
				intent.putExtra(ReleaseTopicActivity.KEY_TOPICSTRING,
						topicString);
				intent.putExtra("menuId", board.getMenuId() + "");
				startActivityForResult(intent, TAG_ADDNEWPOST);
			} else {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
			}
			break;
		case R.id.img_pay:
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("menuId", board.getMenuId());
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("boardId", boardId);
				req.setBody(JsonUtil.Map2JsonObj(map));
				if (board.getAddFlag() == 0) { // 未关注
					req.setHeader(new ReqHead(
							AppConfig.BUSINESS_COMM_BOARDBYADD));
					httpPost(reqCodeThree, AppConfig.PUBLICK_BOARD, req);
				} else {
					req.setHeader(new ReqHead(
							AppConfig.BUSINESS_COMM_BOARDBYDEL));
					httpPost(reqCodeFour, AppConfig.PUBLICK_BOARD, req);
				}
			} else {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
			}
			break;
		case R.id.center_txt_title:
		case R.id.center_img:
			// ArrayList<String> allStr = new ArrayList<String>();
			// allStr.addAll(Arrays.asList(TYPES_POST));
			// new ChooseTextDialog(ListPostActivity.this, "选择类型", allStr,
			// new ChooseOneStrListener() {
			//
			// @Override
			// public void doChoose(int position, String s) {
			// orderByType = TYPES_POST_VALUE[position];
			// center_tv.setText(TYPES_POST[position]);
			// downRefreshData();
			// }
			// }).show();
			break;
		case R.id.layoutMember:
			Intent intent2 = new Intent(ListPostActivity.this,
					MemberListActivity.class);
			intent2.putExtra("boardId", boardId);
			startActivity(intent2);
			break;
		case R.id.layoutExplan:
			if (!isExplaned) {
				if (animationDown == null) {
					animationDown = new DropDownAnim(scrollViewTopic,
							ScreenUtil.dip2px(ListPostActivity.this, 40), true);
					animationDown.setDuration(200);
				}
				headLayout.startAnimation(animationDown);
				isExplaned = true;
			} else {
				if (animationUp == null) {
					animationUp = new DropDownAnim(scrollViewTopic,
							ScreenUtil.dip2px(ListPostActivity.this, 40), false);
					animationUp.setDuration(200);
				}
				headLayout.startAnimation(animationUp);
				isExplaned = false;
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			// 添加新帖子,查看帖子详细回来之后要刷新页面的帖子数和回复数
			if (requestCode == TAG_ADDNEWPOST || requestCode == TAG_REFRESHPOST) {
				downRefreshData();
			} else if (requestCode == REQCODE_TITLE) {
				title = data.getStringExtra("title");
				downRefreshData();
			}
		}
	}

	/**
	 * 关闭页面返回是否有操作关注,用来刷新主页面
	 */
	private void finishWithResult() {
		if (hasDoGuanzhu) {
			setResult(RESULT_OK, new Intent());
			sendBroadcast(new Intent(
					BroadCastAction.ACTION_BOARD_REFRESH_GUANZHU));
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWithResult();
		}
		return false;
	}
}
