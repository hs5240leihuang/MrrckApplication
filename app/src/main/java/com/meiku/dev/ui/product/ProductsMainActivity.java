package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.google.gson.reflect.TypeToken;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.AdViewPagerAdapter;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AdvertiseBannerEntity;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.bean.MkProductCategory;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.community.ListPostActivity;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.IndicatorView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.TileButton;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectDialog;
import com.meiku.dev.views.WheelSelectDialog.SelectStrListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 找产品主页面
 * 
 * @author Administrator
 * 
 */
public class ProductsMainActivity extends BaseActivity implements
		OnClickListener {

	private ViewPager adVpager;
	private List<ImageView> guides = new ArrayList<ImageView>();
	private AdViewPagerAdapter adVpagerAdapter;
	protected boolean isRunning = true;
	private IndicatorView indicatorGroup;
	protected int ADCHANGE_TIME = 6000;
	private GridView gridView;
	private List<MkProductCategory> showMenuList = new ArrayList<MkProductCategory>();
	private CommonAdapter<MkProductCategory> menuAdapter;
	private RelativeLayout ADlayout;
	private ClearEditText et_msg_search;
	private TextView tv_productcatagory;
	private List<AreaEntity> provincesData = new ArrayList<AreaEntity>();
	private String[] provincesStr;
	/** 认证类型 0未认证，1企业认证，2个人认证 */
	private String authenType = "0";
	public static int ProvincesCode = -1;
	private Button btn_myproduct;
	/**
	 * 广告的数据
	 */
	private List<AdvertiseBannerEntity> adData = new ArrayList<AdvertiseBannerEntity>();
	private LinearLayout layoutTypes;
	protected int itemHeight;
	private String currentProvinceName;
	private int defaultProvincesIndex = 0;

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkIsAuthentication();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_findproducts;
	}

	@Override
	public void initView() {
		initTitle();
		initAdViews();
		initGrid();
		initBottomButton();
	}

	private void initTitle() {
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		tv_productcatagory = (TextView) findViewById(R.id.tv_productcatagory);
		et_msg_search.setHint("请输入产品、公司名称搜索");
		et_msg_search.setKeyListener(null);
		et_msg_search.setOnClickListener(this);
		Drawable drawable = ContextCompat.getDrawable(
				ProductsMainActivity.this, R.drawable.triangle);
		drawable.setBounds(0, 0,
				ScreenUtil.dip2px(ProductsMainActivity.this, 8),
				ScreenUtil.dip2px(ProductsMainActivity.this, 8));
		tv_productcatagory.setCompoundDrawables(null, null, drawable, null);
		if (!Tool.isEmpty(MrrckApplication.getInstance().provinceCode)) {
			ProvincesCode = Integer
					.parseInt(MrrckApplication.getInstance().provinceCode);
			currentProvinceName = MKDataBase.getInstance()
					.getCityNameByCityCode(ProvincesCode);
		}
		tv_productcatagory
				.setText(!Tool.isEmpty(currentProvinceName) ? currentProvinceName
						: "请选择");
		tv_productcatagory.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	private void initBottomButton() {
		btn_myproduct = (Button) findViewById(R.id.btn_myproduct);
		btn_myproduct.setOnClickListener(this);
		findViewById(R.id.btn_publish).setOnClickListener(this);
	}

	private void initGrid() {
		layoutTypes = (LinearLayout) findViewById(R.id.layoutTypes);
		layoutTypes.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						layoutTypes.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						int gridHeight = layoutTypes.getMeasuredHeight();
						itemHeight = (gridHeight - ScreenUtil.dip2px(
								ProductsMainActivity.this, 15) * 3) / 4;
					}
				});
		showMenuList.addAll(MKDataBase.getInstance().getProdectCategory());
		gridView = (GridView) findViewById(R.id.gridview);
		menuAdapter = new CommonAdapter<MkProductCategory>(
				ProductsMainActivity.this, R.layout.item_menu_products,
				showMenuList) {

			@Override
			public void convert(ViewHolder viewHolder, final MkProductCategory t) {
				TileButton tv = viewHolder.getView(R.id.txt);
				tv.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, itemHeight));
				Drawable drawable = tv.getBackground();
				int color = Color.parseColor(t.getColorCode());
				drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				tv.setText(t.getName());
				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(ProductsMainActivity.this,
								ProductListSearchActivity.class);
						i.putExtra("name", t.getName());
						i.putExtra("id", t.getId());
						startActivity(i);
					}
				});
			}

		};
		gridView.setAdapter(menuAdapter);
	}

	/**
	 * 是否经过认证
	 */
	private void checkIsAuthentication() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_YESORNO_DENTIFICATION));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING_USER, req, false);
	}

	/**
	 * 是否显示广告栏
	 */
	private void isShowingADBar() {
		itemHeight = guides.size() > 0 ? itemHeight : itemHeight
				+ ScreenUtil.dip2px(ProductsMainActivity.this, 160) / 4;
		menuAdapter.notifyDataSetChanged();
		ADlayout.setVisibility(guides.size() > 0 ? View.VISIBLE : View.GONE);
	}

	private void initAdViews() {
		ADlayout = (RelativeLayout) findViewById(R.id.ADlayout);
		// 点阵
		indicatorGroup = (IndicatorView) findViewById(R.id.indicatorGroup);

		adVpager = (ViewPager) findViewById(R.id.adPager);
		adVpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				currentItem = position;
				// 实现无限制循环播放
				if (!Tool.isEmpty(guides)) {
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
					handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
					break;
				default:
					break;
				}
			}

		});
	}

	/**
	 * 添加一个广告图片
	 */
	private void addOneAD(String url, final String webUrl, int isClientApp,
			final int typeString, final int idString) {
		MySimpleDraweeView iv = new MySimpleDraweeView(this);
		GenericDraweeHierarchy hierarchy = iv.getHierarchy();
		hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		iv.setLayoutParams(params);
		iv.setUrlOfImage(url);
		if (isClientApp == 1) {
			if (!TextUtils.isEmpty(webUrl)) {
				iv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(ProductsMainActivity.this,
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
							Intent intent1 = new Intent(
									ProductsMainActivity.this,
									PostDetailNewActivity.class);
							intent1.putExtra(ConstantKey.KEY_POSTID,
									String.valueOf(idString));
							startActivity(intent1);
							break;
						case 2:// 赛事帖子
							Intent intent2 = new Intent(
									ProductsMainActivity.this,
									ShowMainActivity.class);
							intent2.putExtra("postsId", idString);
							startActivity(intent2);
							break;
						case 3:// 社区版块
							Intent intent3 = new Intent(
									ProductsMainActivity.this,
									ListPostActivity.class);
							intent3.putExtra(ConstantKey.KEY_BOARDID,
									String.valueOf(idString));
							startActivity(intent3);
							break;
						case 4:// 产品页面
							Intent intent4 = new Intent(
									ProductsMainActivity.this,
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
		guides.add(iv);
	}

	@Override
	public void initValue() {
		getADData();
	}

	@Override
	public void bindListener() {

	}

	/**
	 * 获取广告数据
	 */
	private void getADData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryId", ConstantKey.REQ_AD_PRODUCT);
		map.put("provinceCode", MrrckApplication.getInstance().provinceCode);
		map.put("cityCode", MrrckApplication.getInstance().cityCode);
		LogUtil.d("hl", "#getADData#" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_HOME_ADS));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_COMMON, req, true);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody() + "").length() > 2) {
				authenType = resp.getBody().get("data").getAsString();
				LogUtil.d("hl", "authenType=" + authenType);
				if (ConstantKey.TYPE_AUTH_NO.equals(authenType)) {
					btn_myproduct.setText("我的收藏");
				} else if (ConstantKey.TYPE_AUTH_COM.equals(authenType)
						|| ConstantKey.TYPE_AUTH_PERSION.equals(authenType)) {
					btn_myproduct.setText("我的产品");
				}
			} else {
				ToastUtil.showShortToast("获取认证数据失败");
			}
			break;
		case reqCodeTwo:
			String jsonstr = resp.getBody().get("advertiseList").toString();
			List<AdvertiseBannerEntity> data = new ArrayList<AdvertiseBannerEntity>();
			try {
				data = (List<AdvertiseBannerEntity>) JsonUtil.jsonToList(
						jsonstr, new TypeToken<List<AdvertiseBannerEntity>>() {
						}.getType());
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}

			adData.clear();
			adData.addAll(data);
			int ADCount = adData.size();
			LogUtil.d("hl", "===PRODUCT_ADCount===" + ADCount);
			indicatorGroup.setPointCount(ProductsMainActivity.this, ADCount);
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
						addOneAD(adData.get(i).getClientImgUrl(), adData.get(i)
								.getUrl(), isClientApp, typeString, idString);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (!Tool.isEmpty(guides)) {
				adVpagerAdapter = new AdViewPagerAdapter(guides);
				adVpager.setAdapter(adVpagerAdapter);
				if (guides.size() >= 2) {
					handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
				}
			}
			isShowingADBar();
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
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
		case R.id.btn_myproduct:
			if (!NetworkTools.isNetworkAvailable(ProductsMainActivity.this)) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.netNoUse));
				return;
			}
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				Intent i = new Intent();
				if (ConstantKey.TYPE_AUTH_COM.equals(authenType)
						|| ConstantKey.TYPE_AUTH_PERSION.equals(authenType)) {// 我的产品
					i.setClass(ProductsMainActivity.this,
							MyProductActivity.class);
				} else {// 我的收藏
					i.setClass(ProductsMainActivity.this,
							MyCollectActivity.class);
				}
				startActivity(i);
			} else {
				ShowLoginDialogUtil
						.showTipToLoginDialog(ProductsMainActivity.this);
			}
			break;
		case R.id.btn_publish:// 发布产品
			if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
				if (ConstantKey.TYPE_AUTH_COM.equals(authenType)
						|| ConstantKey.TYPE_AUTH_PERSION.equals(authenType)) {
					startActivity(new Intent(ProductsMainActivity.this,
							PublishProductActivity.class));
				} else {
					startActivity(new Intent(ProductsMainActivity.this,
							SelectDentificationActivity.class));
				}
			} else {
				ShowLoginDialogUtil
						.showTipToLoginDialog(ProductsMainActivity.this);
			}
			break;
		case R.id.tv_productcatagory:
			provincesData.clear();
			AreaEntity allCountry = new AreaEntity();
			allCountry.setCityCode(-1);
			allCountry.setCityName("全国");
			provincesData.add(allCountry);
			provincesData.addAll(MKDataBase.getInstance().getCity());
			int provincesSize = provincesData.size();
			provincesStr = new String[provincesSize];
			for (int i = 0; i < provincesSize; i++) {
				provincesStr[i] = provincesData.get(i).getCityName();
				if (provincesData.get(i).getCityName()
						.equals(currentProvinceName)) {
					defaultProvincesIndex = i;
				}
			}
			new WheelSelectDialog(ProductsMainActivity.this,
					defaultProvincesIndex, new SelectStrListener() {

						@Override
						public void ChooseOneString(int itemIndex, String str) {
							defaultProvincesIndex = itemIndex;
							currentProvinceName = str;
							tv_productcatagory.setText(currentProvinceName);
							ProvincesCode = provincesData.get(itemIndex)
									.getCityCode();
						}
					}, provincesStr).show();
			break;
		case R.id.goback:
			finish();
			break;
		case R.id.et_msg_search:// 点击搜素栏
			Intent intent = new Intent(ProductsMainActivity.this,
					ProductListSearchActivity.class);
			intent.putExtra("flag", 1);
			intent.putExtra("onlysearch", true);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	protected static final int MSG_UPDATE_IMAGE = 1;
	protected static final int MSG_KEEP_SILENT = 2;
	protected static final int MSG_BREAK_SILENT = 3;
	protected static final long MSG_DELAY = 6000;
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

}
