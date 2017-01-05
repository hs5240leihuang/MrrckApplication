package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DecorateCompanyEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 免费设计
 * 
 */
public class FreedesignActivity extends BaseActivity {
	private CommonAdapter<DecorateCompanyEntity> showAdapter;
	private List<DecorateCompanyEntity> showList = new ArrayList<DecorateCompanyEntity>();
	private PullToRefreshListView pull_refreshListView;
	protected int selectCityCode;
	protected int selectProvinceCode;
	
	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_freedesign;
	}

	@Override
	public void initView() {
		initPullListView();
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
	
	/**
	 * 下拉刷新控件
	 */
	private void initPullListView() {
		pull_refreshListView = (PullToRefreshListView) findViewById(R.id.mylistview);
		pull_refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
		LinearLayout layout = new LinearLayout(FreedesignActivity.this);
		ImageView hearImg = new ImageView(FreedesignActivity.this);
		hearImg.setBackgroundResource(R.drawable.freedesignpicture);
		layout.addView(hearImg,
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						ScreenUtil.dip2px(FreedesignActivity.this, 200)));
		pull_refreshListView.getRefreshableView().addHeaderView(layout);
		pull_refreshListView
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
		showAdapter = new CommonAdapter<DecorateCompanyEntity>(
				FreedesignActivity.this, R.layout.item_freedesign, showList) {

			@Override
			public void convert(ViewHolder viewHolder,
					final DecorateCompanyEntity t) {
				LinearLayout lin_showimg = viewHolder.getView(R.id.layout_img);
				MySimpleDraweeView showImgview = new MySimpleDraweeView(
						FreedesignActivity.this);
				lin_showimg.removeAllViews();
				lin_showimg.addView(showImgview, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				showImgview.setUrlOfImage(t.getClientCompanyLogo());
				viewHolder.setText(R.id.companyName, t.getName());
				viewHolder.setText(R.id.hotCommNum,
						"获得店主好评：" + t.getHighCommentNum() + "家");
				viewHolder.setText(R.id.designerNum,
						"优秀设计师：" + t.getDesignFineNum() + "人");
				viewHolder.setText(R.id.address, t.getAddress());
				// 0 已经认证 1 未认证
				viewHolder.getView(R.id.iv_bao).setVisibility(
						t.getProtecteFlag() == 1 ? View.GONE : View.VISIBLE);
				//是否已被美库认证 0:未认证 1:已认证
				viewHolder.getView(R.id.iv_yan).setVisibility(
						t.getMrrckAuthFlag() == 0 ? View.GONE : View.VISIBLE);
				//置顶标记  0  置顶  1 未置顶
				viewHolder.getView(R.id.iv_top).setVisibility(
						t.getTopFlag()== 1 ? View.GONE : View.VISIBLE);
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(
										FreedesignActivity.this,
										DecCompanyDetailActivity.class)
										.putExtra("shareTitle",
												t.getShareTitle())
										.putExtra("shareContent",
												t.getShareContent())
										.putExtra("shareImg", t.getShareImg())
										.putExtra("shareUrl", t.getShareUrl())
										.putExtra("userId", t.getUserId())
										.putExtra("sourceId", t.getId())
										.putExtra("loadUrl", t.getLoadUrl()));
							}
						});
			}

		};
		pull_refreshListView.setAdapter(showAdapter);

	}

	// 上拉加载更多数据
	private void upRefreshData() {
		page++;
		getData();
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		page = 1;
		showList.clear();
		getData();
	}

	private void getData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("provinceCode", selectProvinceCode);
		map.put("cityCode", selectCityCode);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300028));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "————" + map);
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	@Override
	public void initValue() {
		selectCityCode = getIntent().getIntExtra("selectCityCode", -1);
		selectProvinceCode = getIntent().getIntExtra("selectProvinceCode", -1);
		downRefreshData();
	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<DecorateCompanyEntity> listData = (List<DecorateCompanyEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<DecorateCompanyEntity>>() {
								}.getType());
				showList.addAll(listData);
			} else {
				ToastUtil.showShortToast("没有更多数据");
			}
			showAdapter.notifyDataSetChanged();
			break;

		default:
			break;

		}
		pull_refreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

		if (null != pull_refreshListView) {
			pull_refreshListView.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						FreedesignActivity.this, "提示", resp.getHeader()
								.getRetMessage(), "确定");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			}
			break;
		}
	}

}
