package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.PostsSignupCommentEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShowPostsSignupEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.DoEditObs;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyListView;
import com.meiku.dev.views.MyPopupwindow;
import com.meiku.dev.views.MyPopupwindow.popListener;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.VideoPlayerViewGroup;
import com.meiku.dev.views.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 未参赛作品详情
 */
public class WorkDetailNewActivity extends BaseActivity implements
		OnClickListener {
	private PullToRefreshScrollView pull_refreshSV;// 下拉刷新
	private ImageView smallIcon, img_aixin, more;
	private LinearLayout lin_head;
	private TextView tv_name, tv_position, tv_time, tv_looknum, tv_kouhao,
			tv_xuanyan, tv_allpinglun, send, tv_dianzanshu;
	private FrameLayout frame_top;
	private String videoPath_video;// 视频路径
	private VideoPlayerViewGroup viewPlayer;// 播放组件
	private MyListView pinglun_list;
	private EmotionEditText et_pinglun;
	private CommonAdapter<PostsSignupCommentEntity> commonAdapter;// 适配器
	private List<PostsSignupCommentEntity> showlist = new ArrayList<PostsSignupCommentEntity>();// 集合
	private List<AttachmentListDTO> imageDates = new ArrayList<AttachmentListDTO>();
	private LinearLayout emotionLayout;
	private Integer signupId;
	private ShowPostsSignupEntity data;
	private boolean showEmotion = true;
	private String likestatu;
	private Integer likeNum;// 0未读；1以读
	private final int reqCodeFive = 500;
	private final int reqCodeSix = 600;
	private final int reqCodeSeven = 700;
	private int CommentNum;
	private MyPopupwindow myPopupwindow;
	private int flag = 1;

	private List<ImageView> guides = new ArrayList<ImageView>();
	protected boolean isRunning = true;
	protected int ADCHANGE_TIME = 6000;
	private List<PopupData> list = new ArrayList<PopupData>();
	private ImageView img_top;

	protected int getCurrentLayoutID() {
		return R.layout.activity_work_detail_new;
	}

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

	@Override
	public void initView() {
		img_top = (ImageView) findViewById(R.id.img_top);
		more = (ImageView) findViewById(R.id.right_res_title);
		img_aixin = (ImageView) findViewById(R.id.img_aixin);
		tv_dianzanshu = (TextView) findViewById(R.id.tv_dianzanshu);
		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		lin_head = (LinearLayout) findViewById(R.id.lin_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_position = (TextView) findViewById(R.id.tv_position);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_looknum = (TextView) findViewById(R.id.tv_looknum);
		frame_top = (FrameLayout) findViewById(R.id.frame_top);
		tv_kouhao = (TextView) findViewById(R.id.tv_kouhao);
		tv_xuanyan = (TextView) findViewById(R.id.tv_xuanyan);
		tv_allpinglun = (TextView) findViewById(R.id.tv_allpinglun);
		pinglun_list = (MyListView) findViewById(R.id.pinglun_list);
		smallIcon = (ImageView) findViewById(R.id.smallIcon);
		et_pinglun = (EmotionEditText) findViewById(R.id.et_pinglun);
		et_pinglun.clearFocus();
		et_pinglun.setCursorVisible(false);
		et_pinglun.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (!arg1) {
					et_pinglun.setCursorVisible(true);
				}
			}
		});
		send = (TextView) findViewById(R.id.send);
		initEmotion();
		commonAdapter = new CommonAdapter<PostsSignupCommentEntity>(
				WorkDetailNewActivity.this, R.layout.item_work_detail, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final PostsSignupCommentEntity t) {
				LinearLayout lin_rhead = viewHolder.getView(R.id.lin_rhead);
				MyRoundDraweeView img_head = new MyRoundDraweeView(
						WorkDetailNewActivity.this);
				lin_rhead.removeAllViews();
				lin_rhead.addView(img_head, new LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.tv_time, t.getCreateDate());
				viewHolder.setText(R.id.tv_item_pinglun, EmotionHelper
						.getLocalEmotion(WorkDetailNewActivity.this,
								t.getContent()));
				img_head.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(WorkDetailNewActivity.this,
								PersonShowActivity.class);
						intent.putExtra(PersonShowActivity.TO_USERID_KEY,
								t.getUserId() + "");
						intent.putExtra("nickName", t.getNickName());
						startActivity(intent);
					}
				});
			}
		};
		pinglun_list.setAdapter(commonAdapter);
	}

	@Override
	public void initValue() {
		signupId = getIntent().getIntExtra("SignupId", 0);
		initPullView();
		downRefreshData();
	}

	@Override
	public void bindListener() {
		smallIcon.setOnClickListener(this);
		send.setOnClickListener(this);
		img_aixin.setOnClickListener(this);
		more.setOnClickListener(this);
		img_top.setOnClickListener(this);
		et_pinglun.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			list.clear();
			if ((resp.getBody().get("postsSignup") + "").length() > 2) {
				String jsString = resp.getBody().get("postsSignup").toString();
				data = (ShowPostsSignupEntity) JsonUtil.jsonToObj(
						ShowPostsSignupEntity.class, jsString);
			} else {
				ToastUtil.showShortToast("该作品已删除");
				finish();
				return;
			}
			if (data == null) {
				finish();
				return;
			}
			// (data.getUserId()+"").equals(AppContext.getInstance().getUserInfo().getUserId()+"");
			if ((data.getUserId() + "").equals(AppContext.getInstance()
					.getUserInfo().getUserId()
					+ "")) {
				findViewById(R.id.layout_enroll).setVisibility(View.VISIBLE);
				findViewById(R.id.btn_enroll).setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent i = new Intent(
										WorkDetailNewActivity.this,
										EnrollActivity.class);
								i.putExtra("flag", flag);
								i.putExtra("imgresault",
										data.getClientPhotoFileUrl());
								i.putExtra("name", data.getName());
								i.putExtra("xuanyan", data.getRemark());
								i.putExtra("fileUrl", data.getFileUrl());
								startActivity(i);
							}
						});
				list.add(new PopupData("分享", R.drawable.show_fengxiang));
				list.add(new PopupData("编辑", R.drawable.show_bianji));
				list.add(new PopupData("删除", R.drawable.show_shanchu));
				myPopupwindow = new MyPopupwindow(this, list,
						new popListener() {

							@Override
							public void doChoose(int position) {
								switch (position) {
								case 0:
									new InviteFriendDialog(
											WorkDetailNewActivity.this, data
													.getPostsSignupShareUrl(),
											"[美库秀场]" + data.getName(),
											"美业有你更精彩~", data
													.getClientPhotoFileUrl(),
											data.getSignupId() + "",
											ConstantKey.ShareStatus_NOSHOWWORK)
											.show();
									myPopupwindow.dismiss();
									break;
								case 1:
									Intent intent = new Intent(
											WorkDetailNewActivity.this,
											EditWorkActivity.class);
									intent.putExtra("name", data.getName());
									intent.putExtra("signupId",
											data.getSignupId() + "");
									intent.putExtra("categoryName",
											data.getCategoryName());
									intent.putExtra("categoryId",
											data.getCategoryId() + "");
									intent.putExtra("clientPicUrl",
											data.getClientFileUrl());
									intent.putExtra("content", data.getRemark());
									startActivityForResult(intent, reqCodeOne);
									myPopupwindow.dismiss();
									break;
								case 2:
									ReqBase req = new ReqBase();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("userId", AppContext.getInstance()
											.getUserInfo().getUserId());
									map.put("signupId", signupId);
									req.setHeader(new ReqHead(
											AppConfig.BUSINESS_DELETE_WORK));
									req.setBody(JsonUtil.Map2JsonObj(map));
									httpPost(reqCodeSix,
											AppConfig.PUBLICK_BOARD, req);
									break;
								default:
									break;
								}
							}
						});
			} else {
				list.add(new PopupData("分享", R.drawable.show_fengxiang));
				list.add(new PopupData("收藏", R.drawable.show_shoucang));
				myPopupwindow = new MyPopupwindow(this, list,
						new popListener() {

							@Override
							public void doChoose(int position) {
								switch (position) {
								case 0:
									new InviteFriendDialog(
											WorkDetailNewActivity.this, data
													.getPostsSignupShareUrl(),
											"[美库秀场]" + data.getName(),
											"美业有你更精彩~", data
													.getClientPhotoFileUrl(),
											data.getSignupId() + "",
											ConstantKey.ShareStatus_NOSHOWWORK)
											.show();
									myPopupwindow.dismiss();
									break;
								case 1:
									if (AppContext.getInstance()
											.isLoginedAndInfoPerfect()) {
										ReqBase req = new ReqBase();
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("userId", AppContext
												.getInstance().getUserInfo()
												.getUserId());
										map.put("signupId", signupId);
										LogUtil.e(map + "");
										req.setHeader(new ReqHead(
												AppConfig.BUSINESS_COLLECT_WORK));
										req.setBody(JsonUtil.Map2JsonObj(map));
										httpPost(reqCodeSeven,
												AppConfig.PUBLICK_BOARD, req);
									} else {
										ShowLoginDialogUtil
												.showTipToLoginDialog(WorkDetailNewActivity.this);
									}
									break;
								default:
									break;
								}
							}
						});
			}
			videoPath_video = data.getClientFileUrl();
			showlist.clear();
			showlist.addAll(data.getPostsSignupCommentList());
			commonAdapter.setmDatas(showlist);
			commonAdapter.notifyDataSetChanged();
			likestatu = data.getLikeStatus();
			likeNum = data.getLikeNum();
			CommentNum = data.getCommentNum();
			if (likestatu.equals("0")) {
				img_aixin.setBackgroundResource(R.drawable.quxiaodianzan);
			} else {
				img_aixin.setBackgroundResource(R.drawable.dianzan);
			}
			for (int i = 0; i < data.getAttachmentList().size(); i++) {
				AttachmentListDTO attachmentListDTO = new AttachmentListDTO();
				attachmentListDTO.setClientFileUrl(data.getAttachmentList()
						.get(i).getClientFileUrl());
				imageDates.add(attachmentListDTO);
			}
			MyRoundDraweeView img_head = new MyRoundDraweeView(
					WorkDetailNewActivity.this);
			lin_head.removeAllViews();
			lin_head.addView(img_head, new LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
			img_head.setUrlOfImage(data.getClientThumbHeadPicUrl());
			lin_head.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(WorkDetailNewActivity.this,
							PersonShowActivity.class);
					intent.putExtra(PersonShowActivity.TO_USERID_KEY,
							data.getUserId() + "");
					intent.putExtra("nickName", data.getNickName());
					startActivity(intent);
				}
			});
			tv_name.setText(data.getNickName());
			tv_position.setText(data.getPositionName());
			tv_time.setText(data.getCreateDate());
			tv_looknum.setText(data.getViewNum() + "");
			tv_kouhao.setText(data.getName());
			tv_xuanyan.setText(data.getRemark());
			tv_dianzanshu.setText(likeNum.toString());
			tv_allpinglun.setText("评论 " + data.getCommentNum());

			if (data.getFileType() == 1) {
				frame_top.setVisibility(View.VISIBLE);
				img_top.setVisibility(View.GONE);
				videoViewGroup();
			} else {
				frame_top.setVisibility(View.GONE);
				img_top.setVisibility(View.VISIBLE);
				// 获取赛事图片
				String url = data.getClientFileUrl();
				ImageLoader.getInstance().loadImage(url,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								img_top.setBackgroundResource(R.drawable.gg_icon);
							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								if (null != arg2) {
									DisplayMetrics dm = new DisplayMetrics();
									WorkDetailNewActivity.this
											.getWindowManager()
											.getDefaultDisplay().getMetrics(dm);
									int showGeight = arg2.getHeight()
											* dm.widthPixels / arg2.getWidth();
									img_top.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.FILL_PARENT,
											showGeight));
									img_top.setImageBitmap(arg2);
								}

							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
							}
						});
			}
			pull_refreshSV.onRefreshComplete();
			break;
		case reqCodeTwo:
			CommentNum = CommentNum + 1;
			tv_allpinglun.setText("评论 " + CommentNum);
			et_pinglun.setText("");
			showlist.clear();
			chaPinLun(1);
			break;
		case reqCodeThree:
			tv_dianzanshu.setText((likeNum + 1) + "");
			likeNum = likeNum + 1;
			likestatu = "1";
			img_aixin.setBackgroundResource(R.drawable.dianzan);
			break;
		case reqCodeFour:
			tv_dianzanshu.setText((likeNum - 1) + "");
			likeNum = likeNum - 1;
			likestatu = "0";
			img_aixin.setBackgroundResource(R.drawable.quxiaodianzan);
			break;
		case reqCodeFive:
			try {
				String json = resp.getBody().get("postsSignupComment")
						.toString();
				if (resp.getBody().get("postsSignupComment").toString()
						.length() > 2) {
					List<PostsSignupCommentEntity> commentData = (List<PostsSignupCommentEntity>) JsonUtil
							.jsonToList(
									json,
									new TypeToken<List<PostsSignupCommentEntity>>() {
									}.getType());
					if (!Tool.isEmpty(commentData)) {
						showlist.addAll(commentData);
					}
				}
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(showlist)) {
				ToastUtil.showShortToast("没有更多数据");
			} else {
				commonAdapter.setmDatas(showlist);
			}
			commonAdapter.notifyDataSetChanged();
			pull_refreshSV.onRefreshComplete();
			break;
		case reqCodeSix:
			ToastUtil.showShortToast("删除成功");
			RefreshObs.getInstance().notifyAllLisWithTag("normal");
			DoEditObs.getInstance().notifyDoRefresh();// 如果从我的秀场进来，则刷新
			finish();
			break;
		case reqCodeSeven:
			ToastUtil.showShortToast("收藏成功");
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				downRefreshData();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeSeven:
			ToastUtil.showShortToast("您已收藏该作品");
			break;
		case reqCodeOne:
			ToastUtil.showShortToast("获取数据失败！");
			break;
		default:
			break;
		}
		if (pull_refreshSV != null) {
			pull_refreshSV.onRefreshComplete();
		}
		finish();
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullView() {
		pull_refreshSV = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
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
		pull_refreshSV.getRefreshableView().setOnTouchListener(
				new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// 触摸隐藏键盘
						hideBottomLayout();
						hideSoftInputView();
						return false;
					}
				});
	}

	protected void upRefreshData() {
		page++;
		chaPinLun(page);
	}

	protected void downRefreshData() {
		page = 1;
		showlist.clear();
		getWorkDetailData(page);
	}

	// 添加播放用的组件
	public void videoViewGroup() {
		viewPlayer = new VideoPlayerViewGroup(WorkDetailNewActivity.this,
				videoPath_video, data.getAttachmentList().get(0)
						.getFileSeconds());
		if (!Tool.isEmpty(data.getClientPhotoFileUrl())) {
			viewPlayer.getWorkImg().setImageURI(
					Uri.parse(data.getClientPhotoFileUrl()));
		}
		frame_top.addView(viewPlayer.getView());
	}

	@Override
	protected void onStop() {
		if (viewPlayer != null) {
			viewPlayer.onStop();
		}
		super.onStop();
	}

	// 请求数据
	private void getWorkDetailData(int page) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("signupId", signupId);
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_COMMON_WORK));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.smallIcon:
			showEmotionView(showEmotion);
			showEmotion = !showEmotion;
			break;
		case R.id.send:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
				return;
			}
			if (Tool.isEmpty(et_pinglun.getText().toString().trim())) {
				ToastUtil.showShortToast("评论内容为空");
				return;
			}
			addpinglun();
			InputTools.HideKeyboard(arg0);
			emotionLayout.setVisibility(View.GONE);

			break;
		case R.id.img_aixin:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
				return;
			}
			// 0未赞; 1以赞
			if (likestatu.equals("0")) {
				dianZan();
			} else {
				cancel();
			}
			break;
		case R.id.right_res_title:
			if (null != myPopupwindow) {
				myPopupwindow.showAsDropDown(more,
						ScreenUtil.dip2px(WorkDetailNewActivity.this, -80),
						ScreenUtil.dip2px(WorkDetailNewActivity.this, 20));
			}
			break;
		case R.id.img_top:
			Intent intentImageview = new Intent();
			intentImageview.setClass(WorkDetailNewActivity.this,
					ImagePagerActivity.class);
			// intentImageview.putParcelableArrayListExtra("imageDates",
			// (ArrayList<? extends Parcelable>) imageDates);
			intentImageview.putExtra("showOnePic", data.getClientFileUrl());
			startActivity(intentImageview);
			break;
		case R.id.et_pinglun:
			hideBottomLayout();
			et_pinglun.setCursorVisible(true);
			break;
		default:
			break;
		}
	}

	// 添加评论
	public void addpinglun() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("signupId", signupId);
		map.put("toUserId", data.getUserId());
		map.put("toCommentId", 0);
		map.put("content", EmotionHelper.getSendEmotion(
				WorkDetailNewActivity.this, et_pinglun.getText().toString()));
		map.put("fileType", 0);
		map.put("floorNum", 0);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ADD_COMMENT));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
	}

	// 查评论
	public void chaPinLun(int page) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("signupId", signupId);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_WORK_PINLUN_STRING));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeFive, AppConfig.PUBLICK_BOARD, req, true);
	}

	// 点赞
	public void dianZan() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("signupId", signupId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_DIANZAN));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.PUBLICK_BOARD, req, true);
	}

	// 取消点赞
	public void cancel() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("signupId", signupId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_CANCEL_DIANZAN));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeFour, AppConfig.PUBLICK_BOARD, req, true);
	}

	/**
	 * 是否显示标签栏
	 * 
	 * @param show
	 */
	private void showEmotionView(final boolean show) {
		if (show) {
			InputTools.HideKeyboard(et_pinglun);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					emotionLayout.setVisibility(View.VISIBLE);
				}
			}, 200);
		} else {
			emotionLayout.setVisibility(View.GONE);
			InputTools.ShowKeyboard(et_pinglun);
		}

	}

	/**
	 * 初始化标签view
	 */
	private void initEmotion() {
		emotionLayout.addView(new EmotionView(WorkDetailNewActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						int start = et_pinglun.getSelectionStart();
						StringBuffer sb = new StringBuffer(et_pinglun.getText());
						if (emotionText.equals(":l7:")) {// 删除图标
							if (start >= 4
									&& EmotionHelper.getLocalEmoMap(
											WorkDetailNewActivity.this)
											.containsKey(
													sb.substring(start - 4,
															start))) {
								sb.replace(start - 4, start, "");
								et_pinglun.setText(sb.toString());
								CharSequence info = et_pinglun.getText();
								if (info instanceof Spannable) {
									Spannable spannable = (Spannable) info;
									Selection
											.setSelection(spannable, start - 4);
								}
								return;
							}
							return;
						}
						sb.replace(et_pinglun.getSelectionStart(),
								et_pinglun.getSelectionEnd(), emotionText);
						et_pinglun.setText(sb.toString());

						CharSequence info = et_pinglun.getText();
						if (info instanceof Spannable) {
							Spannable spannable = (Spannable) info;
							Selection.setSelection(spannable, start
									+ emotionText.length());
						}
					}
				}).getView());
	}

	// /**
	// * 添加一个广告图片
	// */
	// private void addOneAD(final String url, final int i) {
	// ImageView iv = new ImageView(WorkDetailNewActivity.this);
	// ImageLoader.getInstance().displayImage(url + "_thumb.png", iv,
	// PictureUtil.normalImageOptions);
	// ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
	// ViewGroup.LayoutParams.FILL_PARENT,
	// ViewGroup.LayoutParams.WRAP_CONTENT);
	// iv.setLayoutParams(params);
	// iv.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// Intent intentImage = new Intent();
	// intentImage.setClass(WorkDetailNewActivity.this,
	// ImagePagerActivity.class);
	// intentImage.putParcelableArrayListExtra("imageDates",
	// (ArrayList<? extends Parcelable>) imageDates);
	// intentImage.putExtra("index", i);
	// startActivity(intentImage);
	// }
	// });
	// iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
	// guides.add(iv);
	// }

	/**
	 * 隐藏底部栏
	 */
	protected void hideBottomLayout() {
		showEmotionView(false);
	}

	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInputView() {
		InputTools.HideKeyboard(et_pinglun);
	}

	@Override
	protected void onDestroy() {
		Tool.releaseImageViewResouce(img_top);
		super.onDestroy();
	}

}
