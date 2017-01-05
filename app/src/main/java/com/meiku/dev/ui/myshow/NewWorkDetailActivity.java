package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.AdViewPagerAdapter;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.PostsSignupCommentEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ShowPostsSignupEntity;
import com.meiku.dev.bean.UserAttachmentEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.DoEditObs;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.IndicatorView;
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

/**
 * 参赛作品详情
 * */
public class NewWorkDetailActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout lin_head;
	private TextView tv_name, tv_position, tv_time, tv_looknum, tv_kouhao,
			tv_votenum, tv_votenum_tonghang, tv_votenum_renqi, tv_paiming,
			tv_xuanyan, tv_allpinglun, tv_bianhao;
	private FrameLayout frame_top;
	private Button btn_forvote, btn_fabiaopinglun;
	private MyListView pinglun_list;
	private String videoPath_video;// 视频路径
	private VideoPlayerViewGroup viewPlayer;// 播放组件
	private PullToRefreshScrollView pull_refreshSV;// 下拉刷新
	private CommonAdapter<PostsSignupCommentEntity> commonAdapter;// 适配器
	private List<PostsSignupCommentEntity> showlist = new ArrayList<PostsSignupCommentEntity>();// 集合
	private Integer signupId;
	private ShowPostsSignupEntity data;
	private EditText et_pinglun;
	private LinearLayout emotionLayout;
	private boolean showEmotion = true;
	private String voteStatus;
	private final int requestdata = 10;
	private ImageView more, img_top;
	private MyPopupwindow myPopupwindow;
	private List<PopupData> list = new ArrayList<PopupData>();
	private String msg;
	/**
	 * 头部整体的view
	 */
	private View topADViewGroup;
	/**
	 * 广告底部选中点阵
	 */
	private IndicatorView indicatorGroup;

	private LinearLayout titlebar_TopFloat;

	// private LinearLayout layout_top;
	/**
	 * 广告的数据
	 */
	private List<UserAttachmentEntity> adData = new ArrayList<UserAttachmentEntity>();
	private List<AttachmentListDTO> imageDates = new ArrayList<AttachmentListDTO>();
	private List<ImageView> guides = new ArrayList<ImageView>();
	private ViewPager adVpager;
	private AdViewPagerAdapter adVpagerAdapter;
	protected boolean isRunning = true;
	protected int ADCHANGE_TIME = 6000;
	private int CommentNum;
	private final int reqCodeFive = 500;
	private boolean isMyWork;
	private TextView tv_matchname;
	private TextView tv_SignupNo;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_work_detail;
	}

	@Override
	public void initView() {
		// initAD();
		img_top = (ImageView) findViewById(R.id.img_top);
		more = (ImageView) findViewById(R.id.right_res_title);
		tv_bianhao = (TextView) findViewById(R.id.tv_bianhao);
		// layout_top = (LinearLayout) findViewById(R.id.layout_top);
		btn_fabiaopinglun = (Button) findViewById(R.id.btn_fabiaopinglun);
		btn_fabiaopinglun = (Button) findViewById(R.id.btn_fabiaopinglun);
		lin_head = (LinearLayout) findViewById(R.id.lin_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_position = (TextView) findViewById(R.id.tv_position);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_looknum = (TextView) findViewById(R.id.tv_looknum);
		frame_top = (FrameLayout) findViewById(R.id.frame_top);
		tv_kouhao = (TextView) findViewById(R.id.tv_kouhao);
		tv_matchname = (TextView) findViewById(R.id.tv_matchname);
		tv_votenum = (TextView) findViewById(R.id.tv_votenum);
		tv_votenum_renqi = (TextView) findViewById(R.id.tv_votenum_renqi);
		tv_votenum_tonghang = (TextView) findViewById(R.id.tv_votenum_tonghang);

		tv_SignupNo = (TextView) findViewById(R.id.tv_SignupNo);
		tv_paiming = (TextView) findViewById(R.id.tv_paiming);
		tv_xuanyan = (TextView) findViewById(R.id.tv_xuanyan);
		tv_allpinglun = (TextView) findViewById(R.id.tv_allpinglun);
		pinglun_list = (MyListView) findViewById(R.id.pinglun_list);
		btn_forvote = (Button) findViewById(R.id.btn_forvote);
		et_pinglun = (EditText) findViewById(R.id.et_pinglun);
		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);

		commonAdapter = new CommonAdapter<PostsSignupCommentEntity>(this,
				R.layout.item_work_detail, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final PostsSignupCommentEntity t) {
				// viewHolder.setImageWithNewSize(R.id.rimg_head,
				// t.getClientThumbHeadPicUrl(), 100, 100);
				LinearLayout lin_rhead = viewHolder.getView(R.id.lin_rhead);
				MyRoundDraweeView img_head = new MyRoundDraweeView(
						NewWorkDetailActivity.this);
				lin_rhead.removeAllViews();
				lin_rhead.addView(img_head, new LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.tv_item_pinglun, EmotionHelper
						.getLocalEmotion(NewWorkDetailActivity.this,
								t.getContent()));
				viewHolder.setText(R.id.tv_time, t.getCreateDate());
				img_head.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(NewWorkDetailActivity.this,
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
		downRefreshData();
		initPullView();
	}

	@Override
	public void bindListener() {
		btn_forvote.setOnClickListener(this);
		btn_fabiaopinglun.setOnClickListener(this);
		more.setOnClickListener(this);
		img_top.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hhh", requestCode + "100##" + reqBase.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (null != reqBase.getBody()
					&& (reqBase.getBody().get("postsSignup") + "").length() > 2) {
				String jsString = reqBase.getBody().get("postsSignup")
						.toString();
				data = (ShowPostsSignupEntity) JsonUtil.jsonToObj(
						ShowPostsSignupEntity.class, jsString);
			} else {
				ToastUtil.showShortToast("该作品已删除");
				finish();
				return;
			}
			if (data == null) {
				ToastUtil.showShortToast("该作品已删除");
				finish();
				return;
			}
			list.clear();
			if (reqBase.getBody().has("msg")) {
				msg = reqBase.getBody().get("msg").getAsString();
			}
			if ((data.getUserId() + "").equals(AppContext.getInstance()
					.getUserInfo().getUserId()
					+ "")) {

				list.add(new PopupData("分享", R.drawable.show_fengxiang));
				if (!"2".equals(data.getVoteFlag())) {
					list.add(new PopupData("删除", R.drawable.show_shanchu));
				}

				myPopupwindow = new MyPopupwindow(this, list,
						new popListener() {

							@Override
							public void doChoose(int position) {
								switch (position) {
								case 0:
									new InviteFriendDialog(
											NewWorkDetailActivity.this, data
													.getPostsSignupShareUrl(),
											"我正在美库参加【"
													+ data.getMatchPostsName()
													+ "】大赛，请为我投票，投票也有奖哦！",
											"通过手艺交朋友！还有万元大奖等你来拿！", data
													.getClientPhotoFileUrl(),
											data.getSignupId() + "",
											ConstantKey.ShareStatus_SHOWWORK)
											.show();
									myPopupwindow.dismiss();
									break;
								case 1:
									ReqBase req = new ReqBase();
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("userId", AppContext.getInstance()
											.getUserInfo().getUserId());
									map.put("signupId", signupId);
									LogUtil.e(map + "");
									req.setHeader(new ReqHead(
											AppConfig.BUSINESS_DELETE_MATCHWORK));
									req.setBody(JsonUtil.Map2JsonObj(map));
									httpPost(reqCodeFive,
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
											NewWorkDetailActivity.this, data
													.getPostsSignupShareUrl(),
											"我正在美库参加【"
													+ data.getMatchPostsName()
													+ "】大赛，请为我投票，投票也有奖哦！",
											"通过手艺交朋友！还有万元大奖等你来拿！", data
													.getClientPhotoFileUrl(),
											data.getSignupId() + "",
											ConstantKey.ShareStatus_SHOWWORK)
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
										httpPost(reqCodeFour,
												AppConfig.PUBLICK_BOARD, req);
									} else {
										ShowLoginDialogUtil
												.showTipToLoginDialog(NewWorkDetailActivity.this);
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
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					commonAdapter.notifyDataSetChanged();
				}
			});
			voteStatus = data.getVoteStatus();
			CommentNum = data.getCommentNum();
			for (int i = 0; i < data.getAttachmentList().size(); i++) {
				AttachmentListDTO attachmentListDTO = new AttachmentListDTO();
				attachmentListDTO.setClientFileUrl(data.getAttachmentList()
						.get(i).getClientFileUrl());
				imageDates.add(attachmentListDTO);
			}
			MyRoundDraweeView img_head = new MyRoundDraweeView(
					NewWorkDetailActivity.this);
			lin_head.removeAllViews();
			lin_head.addView(img_head, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			img_head.setUrlOfImage(data.getClientThumbHeadPicUrl());
			lin_head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(NewWorkDetailActivity.this,
							PersonShowActivity.class);
					intent.putExtra(PersonShowActivity.TO_USERID_KEY,
							data.getUserId() + "");
					intent.putExtra("nickName", data.getNickName());
					startActivity(intent);
				}
			});
			tv_name.setText(data.getNickName());
			tv_position.setText(data.getPositionName());
			tv_bianhao.setText("编号:" + data.getSignupNo());
			tv_time.setText(data.getCreateDate());
			tv_looknum.setText(data.getViewNum() + "");
			tv_SignupNo.setText("作品编号：" + data.getSignupNo());
			if (data.getFileType() == 1) {
				frame_top.setVisibility(View.VISIBLE);
				img_top.setVisibility(View.GONE);
				// layout_top.setVisibility(View.GONE);
				videoViewGroup();
			} else {
				frame_top.setVisibility(View.GONE);
				img_top.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().loadImage(data.getClientFileUrl(),
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
									NewWorkDetailActivity.this
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
				// layout_top.setVisibility(View.VISIBLE);
				// adData.clear();
				// guides.clear();
				// adData.addAll(data.getAttachmentList());
				// int ADCount = data.getAttachmentList().size();
				// for (int i = 0; i < ADCount; i++) {
				// addOneAD(adData.get(i).getClientFileUrl(),i);
				// }
				// if (ADCount <= 1) {
				// indicatorGroup.setVisibility(View.GONE);
				// } else {
				// indicatorGroup.setPointCount(NewWorkDetailActivity.this,
				// ADCount);
				// }
				// adVpagerAdapter = new AdViewPagerAdapter(guides);
				// adVpager.setAdapter(adVpagerAdapter);
				// if (ADCount > 1) {
				// handler.removeMessages(0);
				// handler.sendEmptyMessageDelayed(0, ADCHANGE_TIME);
				// }
			}

			tv_matchname.setText(data.getMatchPostsName());
			tv_kouhao.setText(data.getName());
			String allVoteNumStr = "总  票  数：" + data.getTotalVoteNum();
			Util.setTVShowCloTxt(tv_votenum, allVoteNumStr, 8,
					allVoteNumStr.length(),
					getResources().getColor(R.color.mrrck_bg));

			String voteNum_renqi = "人气投票：" + data.getOtherVoteNum();
			Util.setTVShowCloTxt(tv_votenum_renqi, voteNum_renqi, 5,
					voteNum_renqi.length(),
					getResources().getColor(R.color.mrrck_bg));

			String voteNum_tonghang = "同行投票：" + data.getVoteNum();
			Util.setTVShowCloTxt(tv_votenum_tonghang, voteNum_tonghang, 5,
					voteNum_tonghang.length(),
					getResources().getColor(R.color.mrrck_bg));

			String paimingStr = "当前排名：" + data.getRankNum();
			Util.setTVShowCloTxt(tv_paiming, paimingStr, 5,
					paimingStr.length(),
					getResources().getColor(R.color.mrrck_bg));

			tv_xuanyan.setText(data.getRemark());
			tv_allpinglun.setText("全部评论（" + data.getCommentNum() + "）");

			isMyWork = data.getUserId() == AppContext.getInstance()
					.getUserInfo().getId();

			if ("0".equals(data.getVoteFlag())) {
				btn_forvote
						.setTextColor(getResources().getColor(R.color.white));
				btn_forvote.setBackgroundResource(R.drawable.noweitatoupiao);
				btn_forvote.setText("投票未开始");
				btn_forvote.setEnabled(false);
			} else if ("2".equals(data.getVoteFlag())) {
				btn_forvote
						.setTextColor(getResources().getColor(R.color.white));
				btn_forvote.setBackgroundResource(R.drawable.noweitatoupiao);
				btn_forvote.setText("投票已结束");
				btn_forvote.setEnabled(false);
			} else {
				if (isMyWork) {
					btn_forvote.setTextColor(getResources().getColor(
							R.color.mrrck_bg));
					btn_forvote
							.setBackgroundResource(R.drawable.votebackground);
					btn_forvote.setText("邀请朋友投票");
					btn_forvote.setEnabled(true);
				} else {
					if ("1".equals(voteStatus)) {
						btn_forvote.setTextColor(getResources().getColor(
								R.color.white));
						btn_forvote
								.setBackgroundResource(R.drawable.noweitatoupiao);
					} else {
						btn_forvote.setTextColor(getResources().getColor(
								R.color.mrrck_bg));
						btn_forvote
								.setBackgroundResource(R.drawable.votebackground);
					}
					btn_forvote.setText("为TA投票");
					btn_forvote.setEnabled(true);
				}
			}

			pull_refreshSV.post(new Runnable() {

				@Override
				public void run() {
					pull_refreshSV.onRefreshComplete();
					pull_refreshSV.getRefreshableView().scrollTo(0, 0);
				}
			});
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("投票成功");
			downRefreshData();
			// RefreshObs.getInstance().notifyAllLisWithTag("FragmentOneTypeShow");
			break;
		case reqCodeThree:
			try {
				String json = reqBase.getBody().get("postsSignupComment")
						.toString();
				List<PostsSignupCommentEntity> commentData = (List<PostsSignupCommentEntity>) JsonUtil
						.jsonToList(
								json,
								new TypeToken<List<PostsSignupCommentEntity>>() {
								}.getType());
				if (!Tool.isEmpty(commentData)) {
					showlist.addAll(commentData);
				}
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			if (Tool.isEmpty(showlist)) {
				ToastUtil.showShortToast("没有更多数据");
			} else {
				commonAdapter.setmDatas(showlist);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						commonAdapter.notifyDataSetChanged();
					}
				});
			}
			pull_refreshSV.onRefreshComplete();
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("收藏成功");
			break;
		case reqCodeFive:
			ToastUtil.showShortToast("删除成功");
			RefreshObs.getInstance().notifyAllLisWithTag("FragmentOneTypeShow");
			DoEditObs.getInstance().notifyDoRefresh();// 如果从我的秀场进来，则刷新
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
			ToastUtil.showShortToast("投票失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("您已收藏该作品");
			break;
		case reqCodeOne:
			ToastUtil.showShortToast("获取数据失败！");
			break;
		default:
			break;
		}
		if (null != pull_refreshSV) {
			pull_refreshSV.onRefreshComplete();
		}
		finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_forvote:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil
						.showTipToLoginDialog(NewWorkDetailActivity.this);
				return;
			}
			if (isMyWork) {
				new InviteFriendDialog(NewWorkDetailActivity.this,
						data.getPostsSignupShareUrl(), "我正在美库参加【"
								+ data.getMatchPostsName()
								+ "】大赛，请为我投票，投票也有奖哦！", "通过手艺交朋友！还有万元大奖等你来拿！",
						data.getClientPhotoFileUrl(), data.getSignupId() + "",
						ConstantKey.ShareStatus_SHOWWORK).show();
				return;
			}
			if (("0").equals(data.getVoteFlag())
					|| ("2").equals(data.getVoteFlag())) {
				ToastUtil.showShortToast(msg);
			} else {
				if ("1".equals(voteStatus)) {
					ToastUtil.showShortToast(msg);
				} else {
					vote();
				}
			}
			break;
		case R.id.btn_fabiaopinglun:
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(this);
				return;
			}
			Intent intent = new Intent(NewWorkDetailActivity.this,
					SendPinlunActivity.class);
			intent.putExtra("signupId", signupId);
			intent.putExtra("toUserId", data.getUserId());
			intent.putExtra("toUserName", data.getNickName());
			startActivityForResult(intent, requestdata);
			break;
		case R.id.right_res_title:
			// commonPopMenu.showWindow(more);// show popWindow
			myPopupwindow.showAsDropDown(more,
					ScreenUtil.dip2px(NewWorkDetailActivity.this, -80),
					ScreenUtil.dip2px(NewWorkDetailActivity.this, 20));
			break;
		case R.id.img_top:
			Intent intentImageview = new Intent();
			intentImageview.setClass(NewWorkDetailActivity.this,
					ImagePagerActivity.class);
			intentImageview.putExtra("showOnePic", data.getClientFileUrl());
			startActivity(intentImageview);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == requestdata) {
				CommentNum = CommentNum + 1;
				tv_allpinglun.setText("评论 " + CommentNum);
				showlist.clear();
				chaPinLun(1);
				page = 1;
			}
		}
	}

	// 为TA投票
	private void vote() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("signupId", signupId);
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("postsId", data.getPostsId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_FORVOTE));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req);
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
		viewPlayer = new VideoPlayerViewGroup(NewWorkDetailActivity.this,
				videoPath_video, data.getAttachmentList().get(0)
						.getFileSeconds());
		if (!Tool.isEmpty(data.getClientPhotoFileUrl())) {
			viewPlayer.getWorkImg().setImageURI(
					Uri.parse(data.getClientPhotoFileUrl()));
		}
		frame_top.addView(viewPlayer.getView());
	}

	// /**
	// * 初始化广告位
	// */
	// private void initAD() {
	// layout_top = (LinearLayout) findViewById(R.id.layout_top);
	// topADViewGroup = LayoutInflater.from(NewWorkDetailActivity.this)
	// .inflate(R.layout.view_newads, null, false);
	// layout_top.addView(topADViewGroup);
	// // 点阵
	// indicatorGroup = (IndicatorView) topADViewGroup
	// .findViewById(R.id.indicatorGroup);
	//
	// adVpager = (ViewPager) topADViewGroup.findViewById(R.id.adPager);
	// // LinearLayout.LayoutParams linearParams =(LayoutParams)
	// // adVpager.getLayoutParams();
	// // linearParams.height=ScreenUtil.dip2px(WorkDetailNewActivity.this,
	// // 180);
	// // adVpager.setLayoutParams(linearParams);
	// adVpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	// public void onPageSelected(int position) {
	// // 实现无限制循环播放
	// position %= guides.size();
	// indicatorGroup.setSelectedPosition(position);
	// }
	//
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// }
	//
	// public void onPageScrollStateChanged(int arg0) {
	// }
	//
	// });
	// }
	//
	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// // 执行滑动到下一个页面
	// adVpager.setCurrentItem(adVpager.getCurrentItem() + 1);
	// if (isRunning) {
	// // 在发一个handler延时
	// handler.sendEmptyMessageDelayed(0, ADCHANGE_TIME);
	// }
	// }
	// };
	//
	// /**
	// * 添加一个广告图片
	// */
	// private void addOneAD(final String url,final int i) {
	// ImageView iv = new ImageView(NewWorkDetailActivity.this);
	// ImageLoader.getInstance().displayImage(url+"_thumb.png", iv,
	// PictureUtil.normalImageOptions);
	// ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
	// ViewGroup.LayoutParams.FILL_PARENT,
	// ViewGroup.LayoutParams.FILL_PARENT);
	// iv.setLayoutParams(params);
	// iv.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// Intent intentImage = new Intent();
	// intentImage.setClass(NewWorkDetailActivity.this,
	// ImagePagerActivity.class);
	// intentImage.putParcelableArrayListExtra("imageDates",
	// (ArrayList<? extends Parcelable>) imageDates);
	// intentImage.putExtra("index",i);
	// startActivity(intentImage);
	// }
	// });
	// iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
	// guides.add(iv);
	// }

	// 请求数据
	private void getWorkDetailData(int page) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("signupId", signupId);
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_PARTY_WORK_STRING));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req);
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
		httpPost(reqCodeThree, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	protected void onStop() {
		if (viewPlayer != null) {
			viewPlayer.onStop();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Tool.releaseImageViewResouce(img_top);
		super.onDestroy();
	}

}