package com.meiku.dev.ui.product;

//产品搜索
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ProductInfoEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.findjob.FindJobMainActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

public class ProductListSearchActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_productcatagory;
	private EditText et_msg_search;
	private PullToRefreshListView mPullRefreshListView;// 下拉刷新
	private List<ProductInfoEntity> showList = new ArrayList<ProductInfoEntity>();
	private CommonAdapter<ProductInfoEntity> commonAdapter;
	private List<ProductInfoEntity> showList_Search = new ArrayList<ProductInfoEntity>();
	private int flag;// 1搜索，0类型列表
	private ImageView goback;
	private String name;
	private String type = "SELECT_TYPE";
	private int catagory;
	private int searchPage = 1;
	private LinearLayout layoutSearch;
	private boolean onlysearch;
	protected boolean byDownOrUpRefresh;
	private TextView cancel_text;

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
	protected int getCurrentLayoutID() {
		return R.layout.activity_productlist_search;
	}

	@Override
	public void initView() {
		flag = getIntent().getIntExtra("flag", 0);
		name = getIntent().getStringExtra("name");
		onlysearch = getIntent().getBooleanExtra("onlysearch", false);
		layoutSearch = (LinearLayout) findViewById(R.id.layoutSearch);
		goback = (ImageView) findViewById(R.id.goback);
		cancel_text = (TextView) findViewById(R.id.cancel_text);
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		et_msg_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (flag == 1) {
					downRefreshData();
				}
			}
		});
		et_msg_search.setHint("请输入产品、公司名称搜索");
		tv_productcatagory = (TextView) findViewById(R.id.tv_productcatagory);
		if (flag == 0) {
			tv_productcatagory.setText(name);
			catagory = getIntent().getIntExtra("id", 0);
			Drawable drawable = ContextCompat.getDrawable(
					ProductListSearchActivity.this, R.drawable.triangle);
			drawable.setBounds(0, 0,
					ScreenUtil.dip2px(ProductListSearchActivity.this, 8),
					ScreenUtil.dip2px(ProductListSearchActivity.this, 8));
			tv_productcatagory.setCompoundDrawables(null, null, drawable, null);

		}
		setSearchBar(flag == 1);
		downRefreshData();
		initPullListView();
	}

	private void setSearchBar(boolean isSearch) {
		if (isSearch) {
			layoutSearch.setBackgroundColor(getResources().getColor(
					R.color.white));
			tv_productcatagory.setVisibility(View.GONE);
			et_msg_search.setCursorVisible(true);
			goback.setVisibility(View.GONE);
			cancel_text.setVisibility(View.VISIBLE);
		} else {
			layoutSearch.setBackgroundColor(getResources().getColor(
					R.color.white));
			tv_productcatagory.setVisibility(View.VISIBLE);
			et_msg_search.setText("");
			et_msg_search.setCursorVisible(false);
			goback.setVisibility(View.VISIBLE);
			cancel_text.setVisibility(View.GONE);
			InputTools.HideKeyboard(et_msg_search);
		}
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
		goback.setOnClickListener(this);
		cancel_text.setOnClickListener(this);
		tv_productcatagory.setOnClickListener(this);
		et_msg_search.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hhh", requestCode + "##" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			String jsonstr = resp.getBody().get("productInfo").toString();
			List<ProductInfoEntity> data = new ArrayList<ProductInfoEntity>();
			try {
				data.addAll((List<ProductInfoEntity>) JsonUtil.jsonToList(
						jsonstr, new TypeToken<List<ProductInfoEntity>>() {
						}.getType()));
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			showList.addAll(data);
			commonAdapter.setmDatas(showList);
			commonAdapter.notifyDataSetChanged();
			if (byDownOrUpRefresh) {
				mPullRefreshListView.onRefreshComplete();
			}
			byDownOrUpRefresh = false;
			break;
		case reqCodeTwo:
			String jsonstr1 = resp.getBody().get("productInfo").toString();
			List<ProductInfoEntity> searchData = new ArrayList<ProductInfoEntity>();
			try {
				searchData.addAll((List<ProductInfoEntity>) JsonUtil
						.jsonToList(jsonstr1,
								new TypeToken<List<ProductInfoEntity>>() {
								}.getType()));
			} catch (Exception e) {
				LogUtil.d("error:", e.getMessage());
			}
			showList_Search.addAll(searchData);
			commonAdapter.setmDatas(showList_Search);
			commonAdapter.notifyDataSetChanged();
			if (byDownOrUpRefresh) {
				mPullRefreshListView.onRefreshComplete();
			}
			byDownOrUpRefresh = false;
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("获取产品数据失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("查询产品数据失败");
			break;
		default:
			break;
		}
		if (null != mPullRefreshListView) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		mPullRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						byDownOrUpRefresh = true;
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						byDownOrUpRefresh = true;
						upRefreshData();
					}
				});
		commonAdapter = new CommonAdapter<ProductInfoEntity>(
				ProductListSearchActivity.this,
				R.layout.item_productlist_search, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final ProductInfoEntity t) {
				viewHolder.setImage(R.id.img_productphoto,
						t.getClientPosterThum());
				viewHolder.setText(R.id.tv_productname, t.getProductName());
				viewHolder.setText(R.id.tv_productcompany, t.getCompanyName());
				viewHolder.setText(R.id.tv_productprovince,
						t.getProvinceNames());
				if (t.getTopFlag() == 1) {
					viewHolder.getView(R.id.btn_producttop).setVisibility(
							View.VISIBLE);
				} else {
					viewHolder.getView(R.id.btn_producttop).setVisibility(
							View.GONE);
				}
				viewHolder.setText(R.id.tv_producttime,
						t.getClientRefreshDate());
				viewHolder.setText(R.id.tv_productnum, "浏览量：" + t.getViewNum());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										ProductListSearchActivity.this,
										ProductDetailActivity.class);
								intent.putExtra("productId", t.getProductId()
										+ "");
								startActivity(intent);
							}
						});
			}
		};
		mPullRefreshListView.setAdapter(commonAdapter);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ProductListSearchActivity.this,
						ShowMainActivity.class);
				// intent.putExtra("postsId", showList.get(arg2 -
				// 1).getPostsId());
				// intent.putExtra("title", showList.get(arg2 - 1).getTitle());
				startActivity(intent);
			}
		});

	}

	protected void upRefreshData() {
		if (flag == 1) {
			searchPage++;
			getSearchData(searchPage);
		} else {
			page++;
			getDataList(page);
		}
	}

	protected void downRefreshData() {
		if (flag == 1) {
			searchPage = 1;
			showList_Search.clear();
			getSearchData(searchPage);
		} else {
			page = 1;
			showList.clear();
			getDataList(page);
		}
	}

	// 找产品首页搜索接口19001
	public void getSearchData(int page) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("provinceCode", ProductsMainActivity.ProvincesCode);
		map.put("categoryId", -1);
		map.put("searchName", et_msg_search.getText().toString());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.d(map + "");
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_SEARCH));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PRODUCT_REQUEST_MAPPING, reqBase, false);
	}

	// 找产品产品类别列表接口19002
	public void getDataList(int page) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("provinceCode", ProductsMainActivity.ProvincesCode);
		map.put("categoryId", catagory);
		map.put("searchName", et_msg_search.getText().toString());
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		LogUtil.d(map + "");
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PRODUCT_CATAGORYLIST));
		reqBase.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PRODUCT_REQUEST_MAPPING, reqBase);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.goback:
		case R.id.cancel_text:
			if (onlysearch) {
				finish();
				return;
			}
			if (flag == 1) {
				showList_Search.clear();
				flag = 0;
				commonAdapter.setmDatas(showList);
				commonAdapter.notifyDataSetChanged();
				setSearchBar(false);
			} else {
				finish();
			}
			break;
		case R.id.tv_productcatagory:
			Intent intentcatagoryIntent = new Intent(
					ProductListSearchActivity.this,
					PublishDurationActivity.class);
			intentcatagoryIntent.putExtra("FLAG", type);
			startActivityForResult(intentcatagoryIntent, reqCodeThree);
			break;
		case R.id.et_msg_search:
			if (flag == 0) {
				showList_Search.clear();
				commonAdapter.setmDatas(showList_Search);
				commonAdapter.notifyDataSetChanged();
				flag = 1;
				setSearchBar(true);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == reqCodeThree) {
				tv_productcatagory.setText(data.getStringExtra("value"));
				catagory = data.getIntExtra("id", 0);
				showList.clear();
				page = 1;
				getDataList(page);
			}
		}
	}

}
