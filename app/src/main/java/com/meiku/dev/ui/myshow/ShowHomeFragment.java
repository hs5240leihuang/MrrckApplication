package com.meiku.dev.ui.myshow;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.RefreshObs.NeedRefreshListener;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 才艺秀-首页
 * 
 */
public class ShowHomeFragment extends BaseFragment {

	private View layout_view;
	private Integer postsId;
	private PostsEntity data;
	private ImageView img_gamestate;
	private TextView tv_title, tv_gametime, tv_gamecity, tv_yibaonum,
			tv_kebaonum, tv_surplusnum;
	private Button btn_baoming;
	private int flag = 0;

	/**
	 * 用来与外部activity交互的
	 */
	private FragmentInteraction listterner;
	private ImageView img_background;

	/**
	 * 当FRagmen被加载到activity的时候会被回调
	 * 
	 * @param activity
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof FragmentInteraction) {
			listterner = (FragmentInteraction) activity;
		} else {
			throw new IllegalArgumentException(
					"activity must implements FragmentInteraction");
		}

	}

	@Override
	public void onDetach() {
		super.onDetach();
		listterner = null;
	}

	/**
	 * 定义了所有activity必须实现的接口
	 */
	public interface FragmentInteraction {
		/**
		 * Fragment 向Activity传递指令，这个方法可以根据需求来定义
		 * 
		 * @param data
		 */
		void process(PostsEntity data);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_homepage, null);
		regisRefresh();
		return layout_view;
	}

	private void regisRefresh() {
		RefreshObs.getInstance().registerListener("ShowHomeFragment_normal",
				new NeedRefreshListener() {

					@Override
					public void onPageRefresh() {
						homePage(false);
					}

					@Override
					public void getFirstPageData(int key) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void initValue() {
		img_background = (ImageView) layout_view
				.findViewById(R.id.img_background);
		img_gamestate = (ImageView) layout_view
				.findViewById(R.id.img_gamestate);
		tv_title = (TextView) layout_view.findViewById(R.id.tv_title);
		tv_gametime = (TextView) layout_view.findViewById(R.id.tv_gametime);
		tv_gamecity = (TextView) layout_view.findViewById(R.id.tv_gamecity);
		tv_yibaonum = (TextView) layout_view.findViewById(R.id.tv_yibaonum);
		tv_kebaonum = (TextView) layout_view.findViewById(R.id.tv_kebaonum);
		btn_baoming = (Button) layout_view.findViewById(R.id.btn_baoming);
		tv_surplusnum = (TextView) layout_view.findViewById(R.id.tv_surplusnum);
		Bundle bundle = getArguments();
		postsId = bundle.getInt("postsId");
		homePage(true);
		btn_baoming.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
					return;
				}
				if (!NetworkTools.isNetworkAvailable(getActivity())) {
					ToastUtil.showShortToast(getResources().getString(
							R.string.netNoUse));
					return;
				}
				if (data.getSignupFlag().equals("1")) {
					if ((data.getApplyMaxNum() - data.getApplyNum()) > 0) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("userId", AppContext.getInstance()
								.getUserInfo().getUserId());
						map.put("postsId", data.getPostsId());
						req.setHeader(new ReqHead(AppConfig.BUSINESS_IS_ENROLL));
						req.setBody(JsonUtil.String2Object(JsonUtil
								.hashMapToJson(map)));
						httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
					} else {
						ToastUtil.showShortToast("报名人数已满，请选择其他比赛");
					}
				} else if (data.getSignupFlag().equals("0")) {
					ToastUtil.showShortToast("报名尚未开始");
				} else {
					ToastUtil.showShortToast("比赛已结束");
				}

			}
		});

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hhh", requestCode + "100##" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("posts") + "").length() > 2) {
				String jsonstr = resp.getBody().get("posts").toString();
				data = (PostsEntity) JsonUtil.jsonToObj(PostsEntity.class,
						jsonstr);
				listterner.process(data);
				xuanran();
			}

			break;
		case reqCodeTwo:
			if ((resp.getBody().get("signupFlag") + "").length() > 2) {
				String signupFlag = resp.getBody().get("signupFlag")
						.getAsString();
				if (signupFlag.equals("1")) {
					ToastUtil.showShortToast("您已报名");
				} else {
					Intent intent = new Intent(getActivity(),
							EnrollActivity.class);
					intent.putExtra("flag", flag);
					intent.putExtra("activeCityName", data.getActiveCityName());
					intent.putExtra("postsId", data.getPostsId());
					intent.putExtra("matchYear", data.getMatchYear());
					intent.putExtra("matchCityId", data.getMatchCityId());
					intent.putExtra("matchMonth", data.getMatchMonth());
					intent.putExtra("categoryId", data.getCategoryId());
					startActivity(intent);
				}
			}

			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	/** 查询活动贴信息(首页)接口 */
	public void homePage(boolean showDialog) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("postsId", postsId);
		reqBase.setHeader(new ReqHead(
				AppConfig.BUSINESS_SEARCH_INFORMATION_HOMEPAGE));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.e("查询" + map.toString());
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, reqBase, showDialog);
	}

	@SuppressLint("NewApi")
	public void xuanran() {
		if (!Tool.isEmpty(data.getClientImgUrl())) {
			ImageLoader.getInstance().loadImage(data.getClientImgUrl(),
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							img_background
									.setBackgroundResource(R.drawable.gg_icon);
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							if (null != arg2) {
								DisplayMetrics dm = new DisplayMetrics();
								getActivity().getWindowManager()
										.getDefaultDisplay().getMetrics(dm);
								int showGeight = arg2.getHeight()
										* dm.widthPixels / arg2.getWidth();
								img_background
										.setLayoutParams(new FrameLayout.LayoutParams(
												LayoutParams.FILL_PARENT,
												showGeight));
								img_background.setImageBitmap(arg2);
							}

						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
		}
		if (data.getSignupFlag().equals("0")) {
			img_gamestate.setBackground(ContextCompat.getDrawable(
					getActivity(), R.drawable.weikaishi));
		} else {
			if (data.getSignupFlag().equals("1")) {
				img_gamestate.setBackground(ContextCompat.getDrawable(
						getActivity(), R.drawable.jinxingzhong));
			} else {
				img_gamestate.setBackground(ContextCompat.getDrawable(
						getActivity(), R.drawable.yijieshu));
			}
		}
		tv_title.setText(data.getContent());
		tv_gametime.setText("比赛时间:" + data.getApplyStartDate() + "-"
				+ data.getApplyEndDate());
		tv_gamecity.setText("比赛范围:" + data.getActiveCityName());
		tv_yibaonum.setText(data.getApplyNum().toString());
		tv_kebaonum.setText(data.getApplyMaxNum().toString());
		tv_surplusnum
				.setText((data.getApplyMaxNum() - data.getApplyNum()) + "");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RefreshObs.getInstance().UnRegisterListener("ShowHomeFragment_normal");
		Tool.releaseImageViewResouce(img_background);
	}
}
