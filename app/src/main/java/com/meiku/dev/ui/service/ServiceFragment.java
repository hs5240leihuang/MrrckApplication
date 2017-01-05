package com.meiku.dev.ui.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView.ScrollYChangeListener;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.AdViewPagerAdapter;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AdvertiseBannerEntity;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.PeopleAdvisorEntity;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ProductInfoEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ServiceDataBean;
import com.meiku.dev.bean.ServiceFreeWorkBean;
import com.meiku.dev.bean.ServiceMenuBean;
import com.meiku.dev.bean.ServiceSpecialBenefitBean;
import com.meiku.dev.bean.ServiceZhaoPingBean;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.ListPostActivity;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.community.PublishPostActivity;
import com.meiku.dev.ui.decoration.DecorationMianActivity;
import com.meiku.dev.ui.findjob.CompanyInfoActivity;
import com.meiku.dev.ui.findjob.FindJobMainActivity;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.ui.mine.EditCompInfoActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.ui.plan.PlanMainActivity;
import com.meiku.dev.ui.product.ProductDetailActivity;
import com.meiku.dev.ui.product.ProductsMainActivity;
import com.meiku.dev.ui.recruit.RecruitMainActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.SystemBarTintManager;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.IndicatorView;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MyListView;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 服务页面
 * 
 * @author Administrator
 * 
 */
public class ServiceFragment extends BaseFragment {

	private View layout_view;
	private List<ServiceMenuBean> showMenuList = new ArrayList<ServiceMenuBean>();
	private List<ServiceDataBean> listData = new ArrayList<ServiceDataBean>();// 全部列表数据
	private ArrayList<ServiceZhaoPingBean> midGridData_rdzp;// 热点招聘数据
	private ArrayList<ProductInfoEntity> midListData_cpfb;// 项目产品发布数据
	private ArrayList<ServiceSpecialBenefitBean> midGridData_tbh;// 特别惠数据
	private ArrayList<ServiceFreeWorkBean> midGridData_mkygt;// 美库义工团数据
	private ArrayList<PeopleAdvisorEntity> midGridData_drdst;// 达人导师团数据

	private final int AREA_RDZP = 0;// 热点招聘
	private final int AREA_CPFB = 1;// 项目产品发布
	private final int AREA_TBH = 2;// 特别惠
	private final int AREA_MKYGT = 3;// 美库义工团
	private final int AREA_DRDST = 4;// 达人导师团

	private final String[] modelNames = new String[] { "热点招聘", "项目产品发布", "特别惠",
			"美库义工团", "名家问答" };

	private final String[] modelTips = new String[] { "匠心时代，没你不行", "新品尝鲜看",
			"莫让记忆粘灰，交换精彩不同", "感受多一点的温暖", "和行业大咖面对面" };

	private boolean STATE_RDZP = false;// 热点招聘--是否展开
	private boolean STATE_CPFB = false;// 项目产品发布--是否展开
	private boolean STATE_TBH = false;// 特别惠--是否展开
	private boolean STATE_MKYGT = false;// 美库义工团--是否展开
	private boolean STATE_DRDST = false;// 达人导师团--是否展开

	protected CommonAdapter<ServiceZhaoPingBean> midGridAdapter1;
	protected CommonAdapter<ProductInfoEntity> midlistAdapter_cpfb;
	protected CommonAdapter<ServiceSpecialBenefitBean> midGridAdapter_tbh;
	protected CommonAdapter<ServiceFreeWorkBean> midGridAdapter_mkygt;
	protected CommonAdapter<PeopleAdvisorEntity> midGridAdapter_drdst;

	private String provinceCode = "-1";
	private String cityCode = "-1";
	private PullToRefreshScrollView pull_refreshSV;
	/** 模块数 */
	private int modelPart = 5;
	private LinearLayout[] partslayout = new LinearLayout[modelPart];
	private RelativeLayout[] bottomArrowlayout = new RelativeLayout[modelPart];
	private ImageView[] arrawViews = new ImageView[modelPart];
	private TextView[] moreTViews = new TextView[modelPart];
	private int[] partslayoutIds = new int[] { R.id.layoutPart1,
			R.id.layoutPart2, R.id.layoutPart3, R.id.layoutPart4,
			R.id.layoutPart5 };
	protected String zhaopingUrl;
	private IndicatorView indicatorGroup;
	private ViewPager adVpager;
	private List<ImageView> guides = new ArrayList<ImageView>();
	private List<PostsEntity> showPostList = new ArrayList<PostsEntity>();
	private LinearLayout layout_addmenu;
	private CommonAdapter<PostsEntity> postAdapter;
	/** 广告的数据 */
	private List<AdvertiseBannerEntity> adData = new ArrayList<AdvertiseBannerEntity>();
	private HorizontalScrollView menuScrollView;
	/** 一批话题的数量 */
	private int ONEGROUPNUM = 5;
	/** 菜单图标的可见数量（一屏幕几个） */
	private int MENUNUM = 4;
	protected SystemBarTintManager tintManager;
	protected int currentStatusbarColor;
	protected int currentScroolY;

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_services, null);
		initPullView();
		initView();
		return layout_view;
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullView() {
		pull_refreshSV = (PullToRefreshScrollView) layout_view
				.findViewById(R.id.pull_refresh);
		pull_refreshSV.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		pull_refreshSV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						GetServicePageData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
					}
				});
		pull_refreshSV.addScrollYChangeListener(new ScrollYChangeListener() {

			@Override
			public void onScrollYChanged(int y) {
				currentScroolY = y;
				if (y > ScreenUtil.dip2px(getActivity(), 150)) {
					currentStatusbarColor = Color.parseColor("#66000000");
				} else {
					currentStatusbarColor = Color.TRANSPARENT;
				}
				if (getTintManager() != null) {
					getTintManager().setTintColor(currentStatusbarColor);
				}
			}
		});
	}

	/**
	 * 初始化广告位
	 */
	private void initAD() {
		// 点阵
		indicatorGroup = (IndicatorView) layout_view
				.findViewById(R.id.indicatorGroup);
		adVpager = (ViewPager) layout_view.findViewById(R.id.adPager);
		adVpager.setCurrentItem(Integer.MAX_VALUE / 2);// 默认在中间，使用户看不到边界
		adVpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				currentItem = position;
				if (!Tool.isEmpty(guides)) {
					// 实现无限制循环播放
					position %= guides.size();
					indicatorGroup.setSelectedPosition(position);
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
				switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler.sendEmptyMessage(MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler.sendEmptyMessageDelayed(MSG_BREAK_SILENT, MSG_DELAY);
					break;
				default:
					break;
				}
			}

		});

	}

	protected final int MSG_UPDATE_IMAGE = 11;
	protected final int MSG_KEEP_SILENT = 22;
	protected final int MSG_BREAK_SILENT = 33;
	protected final long MSG_DELAY = 6000;
	private boolean showOneAd = true;
	private int currentItem = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (hasMessages(MSG_UPDATE_IMAGE)) {
				removeMessages(MSG_UPDATE_IMAGE);
			}
			switch (msg.what) {
			case MSG_UPDATE_IMAGE:
				if (guides.size() == 2) {
					showOneAd = !showOneAd;
					adVpager.setCurrentItem(showOneAd ? 0 : 1);
				} else {
					currentItem++;
					adVpager.setCurrentItem(currentItem);
				}
				// 准备下次播放
				sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			case MSG_KEEP_SILENT:
				// 只要不发送消息就暂停了
				break;
			case MSG_BREAK_SILENT:
				sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private LinearLayout layoutPost;
	private int TopicGroupSize;
	private List<PostsEntity> AllTopicData;
	private int currentTopicGroupIndex;
	private ImageView iv_circle;
	private Animation circle_anim;
	/**
	 * 点击换一批
	 */
	private OnClickListener changGroupClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (circle_anim != null) {
				iv_circle.startAnimation(circle_anim); // 开始动画
			}
			currentTopicGroupIndex++;
			if (currentTopicGroupIndex > TopicGroupSize) {
				currentTopicGroupIndex = 1;
			}
			showGroupTopicDataByIndex(currentTopicGroupIndex);
		}
	};
	private MyGridView menuGridview;
	private CommonAdapter<ServiceMenuBean> menuGridAdapter;

	/**
	 * 添加一个广告图片
	 */
	private void addOneAD(String url, final String webUrl, int isClientApp,
			final int typeString, final int idString) {
		MySimpleDraweeView iv = new MySimpleDraweeView(getActivity());
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		iv.setLayoutParams(params);
		GenericDraweeHierarchy hierarchy = iv.getHierarchy();
		hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
		iv.setImageURI(Uri.parse(url));
		if (isClientApp == 1) {
			if (!TextUtils.isEmpty(webUrl)) {
				iv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(getActivity(),
								WebViewActivity.class);
						intent.putExtra("webUrl", webUrl);
						startActivity(intent);
					}
				});
			}
		} else {
			if (Tool.isEmpty(typeString) || Tool.isEmpty(idString)) {
				return;
			} else {
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						switch (typeString) {
						case 1:// 一般帖子
							Intent intent1 = new Intent(getActivity(),
									PostDetailNewActivity.class);
							intent1.putExtra(ConstantKey.KEY_POSTID,
									String.valueOf(idString));
							startActivity(intent1);
							break;
						case 2:// 赛事帖子
							Intent intent2 = new Intent(getActivity(),
									ShowMainActivity.class);
							intent2.putExtra("postsId", idString);
							startActivity(intent2);
							break;
						case 3:// 社区版块
							Intent intent3 = new Intent(getActivity(),
									ListPostActivity.class);
							intent3.putExtra(ConstantKey.KEY_BOARDID,
									String.valueOf(idString));
							startActivity(intent3);
							break;
						case 4:// 产品页面
							Intent intent4 = new Intent(getActivity(),
									ProductDetailActivity.class);
							intent4.putExtra("productId",
									String.valueOf(idString));
							startActivity(intent4);
							break;

						default:
							break;
						}
					}

				});
			}
		}
		iv.setScaleType(ImageView.ScaleType.FIT_XY);
		guides.add(iv);
	}

	private void GetOldLocalData() {
		// 菜单缓存数据配置
		String menuJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_MENU, "");
		if (!Tool.isEmpty(menuJson)) {
			setMenuData(menuJson);
		}
		// 广告缓存数据配置
		String ADJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_AD, "");
		if (!Tool.isEmpty(ADJson)) {
			setADData(ADJson);
		} else {
			layout_view.findViewById(R.id.ADlayout).setVisibility(View.GONE);
		}
		// 话题缓存数据配置
		String topicJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_TOPIC, "");
		if (!Tool.isEmpty(topicJson)) {
			layoutPost.setVisibility(View.VISIBLE);
			setTopicData(topicJson);
		} else {
			layoutPost.setVisibility(View.GONE);
		}
		// 热点招聘缓存数据配置
		String zhaopJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_RDZP, "");
		if (!Tool.isEmpty(zhaopJson)) {
			setItemBottomLayoutVisibility(AREA_RDZP, true);
			setZhaoPinData(zhaopJson);
		} else {
			setItemBottomLayoutVisibility(AREA_RDZP, false);
		}
		// 产品发布缓存数据配置
		String productJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_PRODUCTS, "");
		if (!Tool.isEmpty(productJson)) {
			setItemBottomLayoutVisibility(AREA_CPFB, true);
			setProducvtsData(productJson);
		} else {
			setItemBottomLayoutVisibility(AREA_CPFB, false);
		}
		// 特别惠缓存数据配置
		String tbhJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_TBH, "");
		if (!Tool.isEmpty(tbhJson)) {
			setItemBottomLayoutVisibility(AREA_TBH, true);
			setTbhData(tbhJson);
		} else {
			setItemBottomLayoutVisibility(AREA_TBH, false);
		}
		// 义工团缓存数据配置
		String ygtJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_YGT, "");
		if (!Tool.isEmpty(ygtJson)) {
			setItemBottomLayoutVisibility(AREA_MKYGT, true);
			setYgtData(ygtJson);
		} else {
			setItemBottomLayoutVisibility(AREA_MKYGT, false);
		}
		// 导师团缓存数据配置
		String dstJson = (String) PreferHelper.getSharedParam(
				ConstantKey.SERVICE_DST, "");
		if (!Tool.isEmpty(dstJson)) {
			setItemBottomLayoutVisibility(AREA_DRDST, true);
			setDstData(dstJson);
		} else {
			setItemBottomLayoutVisibility(AREA_DRDST, false);
		}
		// 更多
		listData.get(AREA_RDZP).setMoreUrl(
				(String) PreferHelper.getSharedParam(
						ConstantKey.SERVICE_MORE_URL_RDZP, ""));
		listData.get(AREA_CPFB).setMoreUrl(
				(String) PreferHelper.getSharedParam(
						ConstantKey.SERVICE_MORE_URL_PRODUCTS, ""));
		listData.get(AREA_TBH).setMoreUrl(
				(String) PreferHelper.getSharedParam(
						ConstantKey.SERVICE_MORE_URL_TBH, ""));
		listData.get(AREA_MKYGT).setMoreUrl(
				(String) PreferHelper.getSharedParam(
						ConstantKey.SERVICE_MORE_URL_YGT, ""));
		setMoreTxtIsShow();
	}

	/**
	 * 获取服务页数据
	 */
	private void GetServicePageData() {
		midGridData_rdzp.clear();
		midListData_cpfb.clear();
		midGridData_tbh.clear();
		midGridData_mkygt.clear();
		midGridData_drdst.clear();
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", ConstantKey.REQ_ADDATA);
		map.put("cityCode",
				cityCode == null ? MrrckApplication.getInstance().cityCode
						: cityCode);
		map.put("provinceCode",
				provinceCode == null ? MrrckApplication.getInstance().provinceCode
						: provinceCode);
		LogUtil.d("hl", "请求服务页面数据-->" + map);
		req.setBody(JsonUtil.Map2JsonObj(map));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SERVICEFUNCTION_NEW));
		httpPost(reqCodeOne, AppConfig.PUBLICK_SERVICEFUNCTION, req, false);
	}

	/**
	 * 检测公司是否认证
	 */
	private void CheckCompanyIsCertificate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setBody(JsonUtil.Map2JsonObj(map));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_VERIFY));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, req, true);
	}

	private void initView() {
		initAD();
		menuGridview = (MyGridView) layout_view.findViewById(R.id.menuGridview);
		layout_addmenu = (LinearLayout) layout_view
				.findViewById(R.id.layout_addmenu);
		menuScrollView = (HorizontalScrollView) layout_view
				.findViewById(R.id.menuScrollView);
		initPostData();
		midGridData_rdzp = new ArrayList<ServiceZhaoPingBean>();// 热点招聘数据
		midListData_cpfb = new ArrayList<ProductInfoEntity>();// 项目产品发布数据
		midGridData_tbh = new ArrayList<ServiceSpecialBenefitBean>();// 特别惠数据
		midGridData_mkygt = new ArrayList<ServiceFreeWorkBean>();// 美库义工团数据
		midGridData_drdst = new ArrayList<PeopleAdvisorEntity>();// 大人导师团数据
		// 配置显示数据
		for (int i = 0, size = partslayout.length; i < size; i++) {
			listData.add(new ServiceDataBean(i, modelNames[i], modelTips[i]));
			partslayout[i] = (LinearLayout) layout_view
					.findViewById(partslayoutIds[i]);
			((TextView) partslayout[i].findViewById(R.id.tv_modelName))
					.setText(listData.get(i).getName());
			((TextView) partslayout[i].findViewById(R.id.tv_tip))
					.setText(listData.get(i).getRemark());
			bottomArrowlayout[i] = (RelativeLayout) partslayout[i]
					.findViewById(R.id.morelayout);
			partslayout[i].findViewById(R.id.arrowLayout).setOnClickListener(
					new ArrowClick(i));
			arrawViews[i] = (ImageView) partslayout[i].findViewById(R.id.arrow);
			moreTViews[i] = (TextView) partslayout[i]
					.findViewById(R.id.tv_more);
			moreTViews[i].setOnClickListener(new MoreTxtClick(i));
		}

		// 招聘
		MyGridView midGridViewPart1 = (MyGridView) partslayout[0]
				.findViewById(R.id.midGridview1);
		midGridAdapter1 = new CommonAdapter<ServiceZhaoPingBean>(getActivity(),
				R.layout.item_service_rdzp, midGridData_rdzp) {

			@Override
			public void convert(ViewHolder viewHolder, ServiceZhaoPingBean t) {
				if (1 == t.getAdvertFlag()) {
					viewHolder.setImage(R.id.id_fullImg,
							t.getClientAdvertImgUrl());
				} else {
					viewHolder.getView(R.id.id_fullImg)
							.setVisibility(View.GONE);
					viewHolder.setImageWithNewSize(R.id.id_img,
							t.getClientAdvertLogo(), 150, 150);
					// SimpleDraweeView id_img =
					// viewHolder.getView(R.id.id_img);
					// if (!Tool.isEmpty(t.getClientAdvertLogo())) {
					// id_img.setImageURI(Uri.parse(t.getClientAdvertLogo()));
					// }
					viewHolder.setText(R.id.id_name, t.getShortName());

					String[] infos = new String[2];
					if (t.getShortRemark().split("%%").length == 2) {
						infos = t.getShortRemark().split("%%");
						viewHolder.setText(R.id.id_people, infos[0]);
						viewHolder.setText(R.id.id_money, infos[1]);
					} else {
						viewHolder.setText(R.id.id_people, t.getShortRemark());
					}
				}
			}
		};
		midGridViewPart1.setAdapter(midGridAdapter1);
		midGridViewPart1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// gotoActivity(-1,
				// midGridData_rdzp.get(arg2).getClientHFiveUrl(), 1,
				// midGridData_rdzp.get(arg2).getShortName());
				String h5Url = midGridData_rdzp.get(arg2).getClientHFiveUrl();
				if (!Tool.isEmpty(h5Url)) {
					startActivity(new Intent(getActivity(),
							CompanyInfoActivity.class)
							.putExtra(
									"comeUrl",
									h5Url.substring(0,
											h5Url.lastIndexOf("=") + 1))
							.putExtra("key", midGridData_rdzp.get(arg2).getId())
							.putExtra("type", "fromCompany"));
				}
			}
		});

		// 产品发布
		MyListView midListView2 = (MyListView) partslayout[1]
				.findViewById(R.id.midListview2);
		midlistAdapter_cpfb = new CommonAdapter<ProductInfoEntity>(
				getActivity(), R.layout.item_service_cpfb, midListData_cpfb) {

			@Override
			public void convert(ViewHolder viewHolder, final ProductInfoEntity t) {
				viewHolder.getView(R.id.id_fullImg).setVisibility(View.GONE);
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MySimpleDraweeView id_img = new MySimpleDraweeView(
						getActivity());
				layout_addImage.addView(id_img, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				id_img.setUrlOfImage(t.getClientPosterThum());
				viewHolder.setText(R.id.id_name, t.getProductName());
				viewHolder
						.setText(R.id.id_place, "项目类型：" + t.getCategoryName());
				viewHolder.setText(R.id.id_tv3, "招商省份：" + t.getProvinceNames());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(getActivity(),
										ProductDetailActivity.class).putExtra(
										"productId", t.getId() + ""));
							}
						});
			}
		};
		midListView2.setAdapter(midlistAdapter_cpfb);

		// 特别惠
		MyGridView midGridView_tbh = (MyGridView) partslayout[2]
				.findViewById(R.id.midGridview3);
		midGridAdapter_tbh = new CommonAdapter<ServiceSpecialBenefitBean>(
				getActivity(), R.layout.item_service_tbh, midGridData_tbh) {

			@Override
			public void convert(ViewHolder viewHolder,
					ServiceSpecialBenefitBean t) {
				// ** 广告推广显示方式 0:logo加文字形式,1:整图广告 默认:0
				if (1 == t.getAdvertFlag()) {
					viewHolder.setImage(R.id.id_fullImg,
							t.getClientAdvertImgUrl());
				} else {
					viewHolder.getView(R.id.id_fullImg)
							.setVisibility(View.GONE);
					LinearLayout layout_addImage = viewHolder
							.getView(R.id.layout_addImage);
					layout_addImage.removeAllViews();
					MySimpleDraweeView id_img = new MySimpleDraweeView(
							getActivity());
					layout_addImage.addView(id_img,
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
					id_img.setUrlOfImage(t.getClientAdvertLogo());
					viewHolder.setText(R.id.id_name, t.getShortName());
					viewHolder.setText(R.id.id_people, t.getShortRemark());
				}
			}
		};
		midGridView_tbh.setAdapter(midGridAdapter_tbh);
		midGridView_tbh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				gotoActivity(-1, midGridData_tbh.get(arg2).getClientHFiveUrl(),
						1, midGridData_tbh.get(arg2).getShortName());
			}
		});

		// 美库义工团
		MyGridView midGridView_mkygt = (MyGridView) partslayout[3]
				.findViewById(R.id.midGridview4);
		midGridAdapter_mkygt = new CommonAdapter<ServiceFreeWorkBean>(
				getActivity(), R.layout.item_service_mkygt, midGridData_mkygt) {

			@Override
			public void convert(ViewHolder viewHolder, ServiceFreeWorkBean t) {
				// ** 广告推广显示方式 0:logo加文字形式,1:整图广告 默认:0
				if (1 == t.getAdvertFlag()) {
					viewHolder.setImage(R.id.id_fullImg,
							t.getClientAdvertImgUrl());
				} else {
					viewHolder.getView(R.id.id_fullImg)
							.setVisibility(View.GONE);
					LinearLayout layout_addImage = viewHolder
							.getView(R.id.layout_addImage);
					layout_addImage.removeAllViews();
					MySimpleDraweeView id_img = new MySimpleDraweeView(
							getActivity());
					layout_addImage.addView(id_img,
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
					id_img.setUrlOfImage(t.getClientAdvertLogo());
					viewHolder.setText(R.id.id_txt, t.getShortName());
					viewHolder.setText(R.id.id_txt2, t.getShortRemark());
				}
			}
		};
		midGridView_mkygt.setAdapter(midGridAdapter_mkygt);
		midGridView_mkygt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				gotoActivity(-1, midGridData_mkygt.get(arg2)
						.getClientHFiveUrl(), 1, midGridData_mkygt.get(arg2)
						.getShortName());
			}
		});

		// 达人导师团
		MyGridView midGridView_drdst = (MyGridView) partslayout[4]
				.findViewById(R.id.midGridview5);
		midGridAdapter_drdst = new CommonAdapter<PeopleAdvisorEntity>(
				getActivity(), R.layout.item_service_mjtj, midGridData_drdst) {

			@Override
			public void convert(ViewHolder viewHolder,
					final PeopleAdvisorEntity t) {
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MySimpleDraweeView id_img = new MySimpleDraweeView(
						getActivity());
				int cellSize = (ScreenUtil.SCREEN_WIDTH - ScreenUtil.dip2px(
						getActivity(), 30)) / 3;
				layout_addImage.addView(id_img, new LinearLayout.LayoutParams(
						cellSize, cellSize));
				id_img.setUrlOfImage(t.getClientAdvertImgUrl());
				viewHolder.setText(R.id.id_name, t.getTitle());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								String action = t.getAdvertUrl();
								LogUtil.d("hl", "导师action_" + action);
								if (!Tool.isEmpty(action)
										&& action.contains("APP_MODEL_BOARD")) {
									String boardId = action.substring(action
											.lastIndexOf("=") + 1);
									LogUtil.d("hl", "导师boardId_" + boardId);
									if (!Tool.isEmpty(boardId)) {
										Intent intent = new Intent(
												getActivity(),
												ListPostActivity.class);
										intent.putExtra(
												ConstantKey.KEY_BOARDID,
												boardId);
										startActivity(intent);
									} else {
										ToastUtil.showShortToast("模板ID有误");
									}
								} else {
									ToastUtil.showShortToast("路径有误");
								}
							}
						});
			}

		};
		midGridView_drdst.setAdapter(midGridAdapter_drdst);
	}

	/**
	 * 推荐帖子（话题）
	 */
	private void initPostData() {
		layoutPost = (LinearLayout) layout_view.findViewById(R.id.layoutPost);
		layout_view.findViewById(R.id.publish_post).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
							Intent intent = new Intent(getActivity(),
									PublishPostActivity.class);
							startActivity(intent);
						} else {
							ShowLoginDialogUtil
									.showTipToLoginDialog(getActivity());
						}

					}
				});
		iv_circle = (ImageView) layout_view.findViewById(R.id.iv_circle);
		circle_anim = AnimationUtils
				.loadAnimation(getActivity(), R.anim.circle);
		LinearInterpolator interpolator = new LinearInterpolator();
		circle_anim.setInterpolator(interpolator);
		iv_circle.setOnClickListener(changGroupClick);
		layout_view.findViewById(R.id.tv_changeNext).setOnClickListener(
				changGroupClick);
		MyListView postListView = (MyListView) layout_view
				.findViewById(R.id.postListView);
		postAdapter = new CommonAdapter<PostsEntity>(getActivity(),
				R.layout.item_service_post, showPostList) {

			@Override
			public void convert(ViewHolder viewHolder, final PostsEntity t) {
				LinearLayout layout_addHead = viewHolder
						.getView(R.id.layout_addHead);
				layout_addHead.removeAllViews();
				MyRoundDraweeView headView = new MyRoundDraweeView(
						getActivity());
				layout_addHead.addView(headView, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				headView.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.id_typeName, t.getMenuName());
				viewHolder.setText(R.id.id_name, t.getNickName());
				viewHolder.setText(
						R.id.id_title,
						EmotionHelper.getLocalEmotion(getActivity(),
								t.getTitle()));

				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				if (!Tool.isEmpty(t.getPostsAttachmentList())) {
					layout_addImage.setVisibility(View.VISIBLE);
					layout_addImage.removeAllViews();
					MySimpleDraweeView postImg = new MySimpleDraweeView(
							getActivity());
					layout_addImage.addView(postImg,
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
					postImg.setUrlOfImage(t.getPostsAttachmentList().get(0)
							.getClientPicUrl());
				} else {
					layout_addImage.setVisibility(View.GONE);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(getActivity(),
										PostDetailNewActivity.class)
										.putExtra(ConstantKey.KEY_POSTID,
												t.getId() + "")
										.putExtra(ConstantKey.KEY_BOARDID,
												t.getId() + "")
										.putExtra("isflag", false));
							}
						});
			}
		};
		postListView.setAdapter(postAdapter);
	}

	public class MoreTxtClick implements OnClickListener {

		private int i;

		public MoreTxtClick(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View arg0) {
			if (modelNames[0].equals(listData.get(i).getName())) {
				startActivity(new Intent(getActivity(),
						FindJobMainActivity.class));
			} else if (modelNames[1].equals(listData.get(i).getName())) {
				startActivity(new Intent(getActivity(),
						ProductsMainActivity.class));
			} else {
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				LogUtil.d("hl", "服务页面-更多->" + listData.get(i).getMoreUrl());
				intent.putExtra("webUrl", listData.get(i).getMoreUrl());
				startActivity(intent);
			}
		}
	}

	public class ArrowClick implements OnClickListener {

		private int i;

		public ArrowClick(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View arg0) {
			switch (i) {
			case AREA_RDZP:
				ChangeArrowAndShowMoreTxt(STATE_RDZP, arrawViews[0]);
				STATE_RDZP = !STATE_RDZP;
				showAllData_RDZP(STATE_RDZP);
				break;
			case AREA_CPFB:
				ChangeArrowAndShowMoreTxt(STATE_CPFB, arrawViews[1]);
				STATE_CPFB = !STATE_CPFB;
				showAllData_CPFB(STATE_CPFB);
				break;
			case AREA_TBH:
				ChangeArrowAndShowMoreTxt(STATE_TBH, arrawViews[2]);
				STATE_TBH = !STATE_TBH;
				showAllData_TBH(STATE_TBH);
				break;
			case AREA_MKYGT:
				ChangeArrowAndShowMoreTxt(STATE_MKYGT, arrawViews[3]);
				STATE_MKYGT = !STATE_MKYGT;
				showAllData_MKYGT(STATE_MKYGT);
				break;
			case AREA_DRDST:
				ChangeArrowAndShowMoreTxt(STATE_DRDST, arrawViews[4]);
				STATE_DRDST = !STATE_DRDST;
				showAllData_DRDST(STATE_DRDST);
				break;
			}
		}
	}

	/**
	 * 是否转换箭头
	 * 
	 * @param STATE
	 * @param arrow
	 */
	private void ChangeArrowAndShowMoreTxt(boolean state, ImageView arrow) {
		arrow.setBackgroundResource(state ? R.drawable.xiala
				: R.drawable.shangla);
	}

	/**
	 * 达人导师团团是否显示全部
	 * 
	 * @param showall
	 */
	public void showAllData_DRDST(boolean showall) {
		if (Tool.isEmpty(midGridData_drdst)) {// 空数据则不显示底部箭头的一栏
			bottomArrowlayout[4].setVisibility(View.GONE);
		} else {
			bottomArrowlayout[4].setVisibility(View.VISIBLE);
			if (!showall) {
				midGridAdapter_drdst.setmDatas(midGridData_drdst.subList(
						0,
						midGridData_drdst.size() > 6 ? 6 : midGridData_drdst
								.size()));
			} else {
				midGridAdapter_drdst.setmDatas(midGridData_drdst);
			}
			midGridAdapter_drdst.notifyDataSetChanged();
		}

	}

	/**
	 * 美库义工团是否显示全部
	 * 
	 * @param showall
	 */
	public void showAllData_MKYGT(boolean showall) {
		if (Tool.isEmpty(midGridData_mkygt)) {// 空数据则不显示底部箭头的一栏
			bottomArrowlayout[3].setVisibility(View.GONE);
		} else {
			bottomArrowlayout[3].setVisibility(View.VISIBLE);
			if (!showall) {
				midGridAdapter_mkygt.setmDatas(midGridData_mkygt.subList(
						0,
						midGridData_mkygt.size() > 3 ? 3 : midGridData_mkygt
								.size()));
			} else {
				midGridAdapter_mkygt.setmDatas(midGridData_mkygt);
			}
			midGridAdapter_mkygt.notifyDataSetChanged();
		}

	}

	/**
	 * 特别惠是否显示全部
	 * 
	 * @param showall
	 */
	public void showAllData_TBH(boolean showall) {
		if (Tool.isEmpty(midGridData_tbh)) {// 空数据则不显示底部箭头的一栏
			bottomArrowlayout[2].setVisibility(View.GONE);
		} else {
			bottomArrowlayout[2].setVisibility(View.VISIBLE);
			if (!showall) {
				midGridAdapter_tbh.setmDatas(midGridData_tbh
						.subList(0, midGridData_tbh.size() > 4 ? 4
								: midGridData_tbh.size()));
			} else {
				midGridAdapter_tbh.setmDatas(midGridData_tbh);
			}
			midGridAdapter_tbh.notifyDataSetChanged();
		}

	}

	/**
	 * 产品发布是否显示全部
	 * 
	 * @param showall
	 */
	public void showAllData_CPFB(boolean showall) {
		if (Tool.isEmpty(midListData_cpfb)) {// 空数据则不显示底部箭头的一栏
			bottomArrowlayout[1].setVisibility(View.GONE);
		} else {
			bottomArrowlayout[1].setVisibility(View.VISIBLE);
			if (!showall) {
				midlistAdapter_cpfb.setmDatas(midListData_cpfb.subList(
						0,
						midListData_cpfb.size() > 2 ? 2 : midListData_cpfb
								.size()));
			} else {
				midlistAdapter_cpfb.setmDatas(midListData_cpfb);
			}
			midlistAdapter_cpfb.notifyDataSetChanged();
		}
	}

	/**
	 * 热点招聘是否显示全部
	 * 
	 * @param showall
	 */
	public void showAllData_RDZP(boolean showall) {
		if (Tool.isEmpty(midGridData_rdzp)) {// 空数据则不显示底部箭头的一栏
			bottomArrowlayout[0].setVisibility(View.GONE);
		} else {
			bottomArrowlayout[0].setVisibility(View.VISIBLE);
			if (!showall) {
				midGridAdapter1.setmDatas(midGridData_rdzp.subList(
						0,
						midGridData_rdzp.size() > 4 ? 4 : midGridData_rdzp
								.size()));
			} else {
				midGridAdapter1.setmDatas(midGridData_rdzp);
			}
			midGridAdapter1.notifyDataSetChanged();
		}
	}

	/**
	 * 跳转页面
	 * 
	 * @param id
	 * 
	 * @param functionUrl
	 *            跳转H5页面
	 * @param isClientApp
	 *            是否使用h5还是本地页面// ** 是否客户端应用 0:客户端应用打开 1:H5网页打开 默认:0
	 * @param string
	 *            页面名称(用来跳不同页面)
	 */
	protected void gotoActivity(Integer id, String functionUrl,
			int isClientApp, String string) {
		if (1 == isClientApp) {
			if (!Tool.isEmpty(functionUrl)) {
				Intent intent = new Intent(getActivity(), WebViewActivity.class);
				LogUtil.d("hl", "服务页面-->" + functionUrl);
				intent.putExtra("webUrl", functionUrl);
				startActivity(intent);
			}
		} else {
			if (2 == id) {
				zhaopingUrl = functionUrl;
				CheckCompanyIsCertificate();
			} else if (11 == id) {
				startActivity(new Intent(getActivity(),
						FindJobMainActivity.class));
			} else if ("APP_MODEL_DECORATE".equals(functionUrl)) {
				startActivity(new Intent(getActivity(),
						DecorationMianActivity.class));
			} else if ("APP_MODEL_PRODUCT".equals(functionUrl)) {// 找产品
				startActivity(new Intent(getActivity(),
						ProductsMainActivity.class));
			} else if ("APP_MODEL_PLAN".equals(functionUrl)) {// 找策划
				startActivity(new Intent(getActivity(),
						PlanMainActivity.class));
			} else {
				ToastUtil.showShortToast("敬请期待");
			}
		}
	}

	@Override
	public void initValue() {
		cityCode = MrrckApplication.getInstance().cityCode;
		provinceCode = MrrckApplication.getInstance().provinceCode;
		if (!NetworkTools.isNetworkAvailable(getActivity())) {
			GetOldLocalData();// 第一次进页面，没网取缓存
		} else {
			GetServicePageData();// 有网，页面第一次初始化加载最新数据
		}
		if (pull_refreshSV != null) {
			pull_refreshSV.post(new Runnable() {
				@Override
				public void run() {
					pull_refreshSV.getRefreshableView().scrollTo(0, 0);
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "#服务#" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			try {
				// 广告
				if (!Tool.isEmpty(resp.getBody().get(
						"serviceAdvertiseBannerList"))
						&& (resp.getBody().get("serviceAdvertiseBannerList") + "")
								.length() > 2) {
					PreferHelper.setSharedParam(ConstantKey.SERVICE_AD, resp
							.getBody().get("serviceAdvertiseBannerList")
							.toString());
					setADData(resp.getBody().get("serviceAdvertiseBannerList")
							+ "");
				} else {
					layout_view.findViewById(R.id.ADlayout).setVisibility(
							View.GONE);
				}

				// 头部菜单
				if (!Tool.isEmpty(resp.getBody().get("newServiceFunction"))
						&& (resp.getBody().get("newServiceFunction") + "")
								.length() > 2) {
					setMenuData(resp.getBody().get("newServiceFunction")
							.toString());
					PreferHelper.setSharedParam(ConstantKey.SERVICE_MENU, resp
							.getBody().get("newServiceFunction").toString());
				} else {
					menuScrollView.setVisibility(View.GONE);
					PreferHelper.setSharedParam(ConstantKey.SERVICE_MENU, "");
				}

				// 话题
				if (!Tool.isEmpty(resp.getBody().get("serviceTopicList"))
						&& (resp.getBody().get("serviceTopicList") + "")
								.length() > 2) {
					setTopicData(resp.getBody().get("serviceTopicList")
							.toString());
					PreferHelper.setSharedParam(ConstantKey.SERVICE_TOPIC, resp
							.getBody().get("serviceTopicList").toString());
				} else {
					layoutPost.setVisibility(View.GONE);
					PreferHelper.setSharedParam(ConstantKey.SERVICE_TOPIC, "");
				}

				// 招聘
				if (!Tool.isEmpty(resp.getBody().get("company"))
						&& (resp.getBody().get("company") + "").length() > 2) {
					setItemBottomLayoutVisibility(AREA_RDZP, true);
					setZhaoPinData(resp.getBody().get("company").toString());
					PreferHelper.setSharedParam(ConstantKey.SERVICE_RDZP, resp
							.getBody().get("company").toString());
				} else {
					setItemBottomLayoutVisibility(AREA_RDZP, false);
					PreferHelper.setSharedParam(ConstantKey.SERVICE_RDZP, "");
				}

				// 产品
				if (!Tool.isEmpty(resp.getBody().get("productInfo"))
						&& (resp.getBody().get("productInfo") + "").length() > 2) {
					setItemBottomLayoutVisibility(AREA_CPFB, true);
					setProducvtsData(resp.getBody().get("productInfo")
							.toString());
					PreferHelper.setSharedParam(ConstantKey.SERVICE_PRODUCTS,
							resp.getBody().get("productInfo").toString());
				} else {
					setItemBottomLayoutVisibility(AREA_CPFB, false);
					PreferHelper.setSharedParam(ConstantKey.SERVICE_PRODUCTS,
							"");
				}

				// 特别惠
				if (!Tool.isEmpty(resp.getBody().get("specialBenefit"))
						&& (resp.getBody().get("specialBenefit") + "").length() > 2) {
					setItemBottomLayoutVisibility(AREA_TBH, true);
					setTbhData(resp.getBody().get("specialBenefit").toString());
					PreferHelper.setSharedParam(ConstantKey.SERVICE_TBH, resp
							.getBody().get("specialBenefit").toString());
				} else {
					setItemBottomLayoutVisibility(AREA_TBH, false);
					PreferHelper.setSharedParam(ConstantKey.SERVICE_TBH, "");
				}

				// 美库义工团
				if (!Tool.isEmpty(resp.getBody().get("freeWork"))
						&& (resp.getBody().get("freeWork") + "").length() > 2) {
					setItemBottomLayoutVisibility(AREA_MKYGT, true);
					setYgtData(resp.getBody().get("freeWork").toString());
					PreferHelper.setSharedParam(ConstantKey.SERVICE_YGT, resp
							.getBody().get("freeWork").toString());
				} else {
					setItemBottomLayoutVisibility(AREA_MKYGT, false);
					PreferHelper.setSharedParam(ConstantKey.SERVICE_YGT, "");
				}

				// 达人导师团
				if (!Tool.isEmpty(resp.getBody().get("peopleAdvisor"))
						&& (resp.getBody().get("peopleAdvisor") + "").length() > 2) {
					setItemBottomLayoutVisibility(AREA_DRDST, true);
					setDstData(resp.getBody().get("peopleAdvisor").toString());
					PreferHelper.setSharedParam(ConstantKey.SERVICE_DST, resp
							.getBody().get("peopleAdvisor").toString());
				} else {
					setItemBottomLayoutVisibility(AREA_DRDST, false);
					PreferHelper.setSharedParam(ConstantKey.SERVICE_DST, "");
				}

				String comp_more_url = resp.getBody()
						.get(ConstantKey.ADVERT_COMPANY_MORE_URL).getAsString();
				listData.get(AREA_RDZP).setMoreUrl(comp_more_url);
				PreferHelper.setSharedParam(ConstantKey.SERVICE_MORE_URL_RDZP,
						comp_more_url);

				String project_more_url = resp.getBody()
						.get(ConstantKey.ADVERT_PROJECT_MORE_URL).getAsString();
				listData.get(AREA_CPFB).setMoreUrl(project_more_url);
				PreferHelper
						.setSharedParam(ConstantKey.SERVICE_MORE_URL_PRODUCTS,
								project_more_url);

				String benefit_more_url = resp.getBody()
						.get(ConstantKey.ADVERT_SPECIAL_BENEFIT_MORE_URL)
						.getAsString();
				listData.get(AREA_TBH).setMoreUrl(benefit_more_url);
				PreferHelper.setSharedParam(ConstantKey.SERVICE_MORE_URL_TBH,
						benefit_more_url);

				String freework_more_url = resp.getBody()
						.get(ConstantKey.ADVERT_FREE_WORK_MORE_URL)
						.getAsString();
				listData.get(AREA_MKYGT).setMoreUrl(freework_more_url);
				PreferHelper.setSharedParam(ConstantKey.SERVICE_MORE_URL_YGT,
						freework_more_url);

				listData.get(AREA_DRDST).setMoreUrl("");
				// 若更多的url为空则不显示
				setMoreTxtIsShow();
			} catch (Exception e) {
				e.printStackTrace();
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
			String companyEntityStr = resp.getBody().get("company").toString();
			LogUtil.d("hl", "检测认证company__" + companyEntityStr);
			if (Tool.isEmpty(companyEntityStr)
					|| companyEntityStr.length() == 2) {
				final CommonDialog commonDialog = new CommonDialog(
						getActivity(), "提示", "您还不是企业用户，是否去认证", "是", "否");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						startActivity(new Intent(getActivity(),
								CompanyCertificationActivity.class).putExtra(
								ConstantKey.KEY_COMPANY_URL, zhaopingUrl)
								.putExtra("flag", "1"));
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
			} else {
				CompanyEntity companyEntity = (CompanyEntity) JsonUtil
						.jsonToObj(CompanyEntity.class, companyEntityStr);
				if (companyEntity.getAuthPass().equals("1")) {// 0：审核中 1：已完成
																// 2:不通过
					// AppContext.getInstance().getUserInfo()
					// .setCompanyEntity(companyEntity);
					// Intent intent = new Intent(getActivity(),
					// WebViewActivity.class);
					// intent.putExtra("webUrl", zhaopingUrl);
					// startActivity(intent);

					Intent intent = new Intent(getActivity(),
							RecruitMainActivity.class);
					startActivity(intent);
				} else if (companyEntity.getAuthPass().equals("0")) {
					Intent intent = new Intent(getActivity(),
							EditCompInfoActivity.class);
					startActivity(intent);
				} else {
					final CommonDialog commonDialog = new CommonDialog(
							getActivity(), "提示", "您的企业信息未通过审核！\n 原因："
									+ companyEntity.getAuthResult(), "修改", "取消");
					commonDialog.show();
					commonDialog
							.setClicklistener(new CommonDialog.ClickListenerInterface() {
								@Override
								public void doConfirm() {
									commonDialog.dismiss();
									Intent intent = new Intent(getActivity(),
											EditCompInfoActivity.class);
									startActivity(intent);
								}

								@Override
								public void doCancel() {
									commonDialog.dismiss();
								}
							});
				}
			}
			break;
		}
	}

	/**
	 * 设置item底部更多是否显示
	 * 
	 * @param i
	 */
	private void setItemBottomLayoutVisibility(int i, boolean isShow) {
		partslayout[i].setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	/**
	 * 配置达人导师团数据
	 * 
	 * @param string
	 */
	private void setDstData(String data) {
		List<PeopleAdvisorEntity> daoshiData = (List<PeopleAdvisorEntity>) JsonUtil
				.jsonToList(data, new TypeToken<List<PeopleAdvisorEntity>>() {
				}.getType());
		if (!Tool.isEmpty(daoshiData)) {
			midGridData_drdst.clear();
			midGridData_drdst.addAll(daoshiData);
			showAllData_DRDST(STATE_DRDST);
		}
	}

	/**
	 * 配置义工团数据
	 * 
	 * @param string
	 */
	private void setYgtData(String data) {
		List<ServiceFreeWorkBean> freeWorkData = (List<ServiceFreeWorkBean>) JsonUtil
				.jsonToList(data, new TypeToken<List<ServiceFreeWorkBean>>() {
				}.getType());
		if (!Tool.isEmpty(freeWorkData)) {
			midGridData_mkygt.clear();
			midGridData_mkygt.addAll(freeWorkData);
			showAllData_MKYGT(STATE_MKYGT);
		}
	}

	/**
	 * 配置特别惠数据
	 * 
	 * @param string
	 */
	private void setTbhData(String data) {
		List<ServiceSpecialBenefitBean> benefitData = (List<ServiceSpecialBenefitBean>) JsonUtil
				.jsonToList(data,
						new TypeToken<List<ServiceSpecialBenefitBean>>() {
						}.getType());
		if (!Tool.isEmpty(benefitData)) {
			midGridData_tbh.clear();
			midGridData_tbh.addAll(benefitData);
			showAllData_TBH(STATE_TBH);
		}
	}

	/**
	 * 配置产品数据
	 * 
	 * @param string
	 */
	private void setProducvtsData(String data) {
		List<ProductInfoEntity> bussinessData = (List<ProductInfoEntity>) JsonUtil
				.jsonToList(data, new TypeToken<List<ProductInfoEntity>>() {
				}.getType());
		if (!Tool.isEmpty(bussinessData)) {
			midListData_cpfb.clear();
			midListData_cpfb.addAll(bussinessData);
			showAllData_CPFB(STATE_CPFB);
		}
	}

	/**
	 * 配置招聘数据
	 * 
	 * @param string
	 */
	private void setZhaoPinData(String data) {
		List<ServiceZhaoPingBean> compData = (List<ServiceZhaoPingBean>) JsonUtil
				.jsonToList(data, new TypeToken<List<ServiceZhaoPingBean>>() {
				}.getType());
		if (!Tool.isEmpty(compData)) {
			midGridData_rdzp.clear();
			midGridData_rdzp.addAll(compData);
			showAllData_RDZP(STATE_RDZP);
		}
	}

	/**
	 * 配置话题数据
	 * 
	 * @param string
	 */
	private void setTopicData(String data) {
		AllTopicData = (List<PostsEntity>) JsonUtil.jsonToList(data,
				new TypeToken<List<PostsEntity>>() {
				}.getType());
		if (!Tool.isEmpty(AllTopicData)) {
			TopicGroupSize = AllTopicData.size() / ONEGROUPNUM
					+ (AllTopicData.size() % ONEGROUPNUM == 0 ? 0 : 1);// 总批数(每批5个)
			currentTopicGroupIndex = 1;
			showGroupTopicDataByIndex(currentTopicGroupIndex);
		} else {
			layoutPost.setVisibility(View.GONE);
		}
	}

	/**
	 * 配置广告数据
	 * 
	 * @param string
	 */
	private void setADData(String adStr) {
		handler.sendEmptyMessage(MSG_KEEP_SILENT);
		List<AdvertiseBannerEntity> data = new ArrayList<AdvertiseBannerEntity>();
		try {
			data = (List<AdvertiseBannerEntity>) JsonUtil.jsonToList(adStr,
					new TypeToken<List<AdvertiseBannerEntity>>() {
					}.getType());
		} catch (Exception e) {
			LogUtil.d("error:", e.getMessage());
		}
		adData.clear();
		guides.clear();
		adData.addAll(data);
		int ADCount = adData.size();
		LogUtil.d("hl", "===ADCount===" + ADCount);
		if (adData == null || ADCount == 0) {
			// 无广告数据则隐藏头部
			layout_view.findViewById(R.id.ADlayout).setVisibility(View.GONE);
		} else {
			layout_view.findViewById(R.id.ADlayout).setVisibility(View.VISIBLE);
			indicatorGroup.setPointCount(getActivity(), ADCount);
			indicatorGroup.setSelectedPosition(0);
			int typeString = 0;
			int idString = 0;
			for (int i = 0; i < ADCount; i++) {
				int isClientApp = adData.get(i).getIsClientApp();
				try {
					if (isClientApp == 0) {
						typeString = Integer.parseInt(adData.get(i).getUrl()
								.substring(5, 6));
						idString = Integer.parseInt(adData.get(i).getUrl()
								.substring(7));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				addOneAD(adData.get(i).getClientImgUrl(), adData.get(i)
						.getUrl(), isClientApp, typeString, idString);
			}
			AdViewPagerAdapter adVpagerAdapter = new AdViewPagerAdapter(guides);
			adVpager.setAdapter(adVpagerAdapter);
			if (guides.size() >= 2) {
				handler.sendEmptyMessageDelayed(MSG_BREAK_SILENT, MSG_DELAY);
			}
		}
	}

	/**
	 * 显示第几批的话题数据
	 * 
	 * @param currentIndex
	 */
	private void showGroupTopicDataByIndex(int currentIndex) {
		if (Tool.isEmpty(AllTopicData)) {
			return;
		}
		layoutPost.setVisibility(View.VISIBLE);
		int endListPosition;
		if (currentIndex < TopicGroupSize) {
			endListPosition = currentIndex * ONEGROUPNUM;
		} else {// 最后一组有可能不是正好ONEGROUPNUM个数据，在此判断
			endListPosition = AllTopicData.size();
		}
		showPostList.clear();
		showPostList.addAll(AllTopicData.subList((currentIndex - 1)
				* ONEGROUPNUM, endListPosition));
		postAdapter.setmDatas(showPostList);
		postAdapter.notifyDataSetChanged();
		LogUtil.d("hl", AllTopicData.size() + "_" + TopicGroupSize + "_"
				+ currentIndex);
	}

	/**
	 * 配置服务菜单数据
	 * 
	 * @param string
	 */
	private void setMenuData(String data) {
		try {
			List<ServiceMenuBean> menuData = (List<ServiceMenuBean>) JsonUtil
					.jsonToList(data, new TypeToken<List<ServiceMenuBean>>() {
					}.getType());
			if (!Tool.isEmpty(menuData)) {
				// menuScrollView.setVisibility(View.VISIBLE);
				showMenuList.clear();
				showMenuList.addAll(menuData);
				if (showMenuList.size() <= 4 || showMenuList.size() > 5) {
					menuGridview.setNumColumns(4);
				} else if (showMenuList.size() == 5) {
					menuGridview.setNumColumns(5);
				}
				layout_addmenu.removeAllViews();
				// setMenuViews();
				menuGridview.setVisibility(View.VISIBLE);
				menuGridAdapter = new CommonAdapter<ServiceMenuBean>(
						getActivity(), R.layout.item_service_menu, showMenuList) {

					@Override
					public void convert(ViewHolder viewHolder, ServiceMenuBean t) {
						ImageView img_bg = (ImageView) viewHolder
								.getView(R.id.img_bg);
						LinearLayout layout_addImage = (LinearLayout) viewHolder
								.getView(R.id.layout_img);
						layout_addImage.removeAllViews();
						MySimpleDraweeView iv_head = new MySimpleDraweeView(
								getActivity());
						layout_addImage.addView(iv_head,
								new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.MATCH_PARENT));
						GenericDraweeHierarchy hierarchy = iv_head
								.getHierarchy();
						hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
						iv_head.setUrlOfImage(t.getClientFunctionPhoto());
						TextView id_txt = (TextView) viewHolder
								.getView(R.id.id_txt);
						id_txt.setText(t.getFunctionName());
						Drawable drawable = img_bg.getBackground();
						int color = Color.parseColor(t.getBgColor());
						drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
						viewHolder.getConvertView()
								.setOnClickListener(
										new MenuClickListener(viewHolder
												.getPosition()));
					}
				};
				menuGridview.setAdapter(menuGridAdapter);
			} else {
				// menuScrollView.setVisibility(View.GONE);
				menuGridview.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// menuScrollView.setVisibility(View.GONE);
			menuGridview.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置菜单menu
	 */
	private void setMenuViews() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int SCREEN_WIDTH = dm.widthPixels;
		for (int i = 0, size = showMenuList.size(); i < size; i++) {
			ServiceMenuBean t = showMenuList.get(i);
			View menuView = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_service_menu, null, false);
			ImageView img_bg = (ImageView) menuView.findViewById(R.id.img_bg);
			LinearLayout layout_addImage = (LinearLayout) menuView
					.findViewById(R.id.layout_img);
			layout_addImage.removeAllViews();
			MySimpleDraweeView iv_head = new MySimpleDraweeView(getActivity());
			layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			GenericDraweeHierarchy hierarchy = iv_head.getHierarchy();
			hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
			iv_head.setUrlOfImage(t.getClientFunctionPhoto());
			TextView id_txt = (TextView) menuView.findViewById(R.id.id_txt);
			id_txt.setText(t.getFunctionName());
			Drawable drawable = img_bg.getBackground();
			int color = Color.parseColor(t.getBgColor());
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
			menuView.setOnClickListener(new MenuClickListener(i));
			LogUtil.d("hl", "add menu " + t.getFunctionName() + "/width="
					+ SCREEN_WIDTH / MENUNUM);
			layout_addmenu.addView(menuView, new LinearLayout.LayoutParams(
					SCREEN_WIDTH / MENUNUM, LayoutParams.MATCH_PARENT));
		}
	}

	/**
	 * 菜单项点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class MenuClickListener implements OnClickListener {
		private int position;

		public MenuClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			// 0:需要登录 ,1:无需登录
			if (showMenuList.get(position).getIsLogin() == 0
					&& !AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(getActivity());
				return;
			} else {
				if (showMenuList.get(position).getIsOpen() == 1) {
					/** 是否开启 , 0:开启, 1:不开启 默认:0 */
					ToastUtil.showShortToast("该功能尚未开通");
				} else {
					gotoActivity(showMenuList.get(position).getId(),
							showMenuList.get(position).getFunctionUrl(),
							showMenuList.get(position).getIsClientApp(),
							showMenuList.get(position).getFunctionName());
				}
			}
		}
	}

	/**
	 * 若更多的url为空则不显示"更多"
	 */
	private void setMoreTxtIsShow() {
		for (int i = 0, size = listData.size(); i < size; i++) {
			if (Tool.isEmpty(listData.get(i).getMoreUrl())) {
				moreTViews[i].setVisibility(View.GONE);
			} else {
				moreTViews[i].setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != pull_refreshSV) {
			pull_refreshSV.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
			GetOldLocalData();// 获取数据失败则取缓存显示
			break;

		default:
			break;
		}
		// ToastUtil.showShortToast("获取数据失败!");
	}

	public SystemBarTintManager getTintManager() {
		return tintManager;
	}

	public void setTintManager(SystemBarTintManager tintManager) {
		this.tintManager = tintManager;
	}

	public int getCurrentStatusbarColor() {
		return currentStatusbarColor;
	}

	public void setCurrentStatusbarColor(int currentStatusbarColor) {
		this.currentStatusbarColor = currentStatusbarColor;
	}

	public void scrollToTop() {
		if (pull_refreshSV != null) {
			pull_refreshSV.getRefreshableView().scrollTo(0, currentScroolY);
		}
	}
}
