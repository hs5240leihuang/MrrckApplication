package com.meiku.dev.ui.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PlanCaseEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 案列搜索
 */
public class PlanSearchActivity extends BaseActivity {

	private LinearLayout layoutEmpty;
	private PullToRefreshListView mPullRefreshListView;
	private CommonAdapter<PlanCaseEntity> adapter;
	protected String searchName;
	private List<PlanCaseEntity> showList = new ArrayList<PlanCaseEntity>();
	private TextView tv_cancle;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_searchcase;
	}

	@Override
	public void initView() {
		tv_cancle = (TextView) findViewById(R.id.tv_cancle);
		tv_cancle.setVisibility(View.VISIBLE);
		findViewById(R.id.goback).setVisibility(View.GONE);
		layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
		((TextView) findViewById(R.id.tv_text)).setText("");
		ImageView imgEmpty = (ImageView) findViewById(R.id.img_picture);
		imgEmpty.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil
				.dip2px(this, 155), ScreenUtil.dip2px(this, 165)));
		imgEmpty.setBackgroundResource(R.drawable.searchempty);
		findViewById(R.id.tv_productcatagory).setLayoutParams(
				new LinearLayout.LayoutParams(ScreenUtil.dip2px(this, 16),
						LayoutParams.WRAP_CONTENT));
		EditText et_msg_search = (EditText) findViewById(R.id.et_msg_search);
		et_msg_search.setHint("请输入关键词搜索");
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
				searchName = arg0.toString();
				downRefreshData();
			}
		});
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
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
		adapter = new CommonAdapter<PlanCaseEntity>(this,
				R.layout.item_new_superteacher, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final PlanCaseEntity t) {
				viewHolder
						.setText(R.id.postTitle, EmotionHelper.getLocalEmotion(
								PlanSearchActivity.this, t.getTitle()));
				viewHolder.setText(R.id.tv_shop, t.getShopTypeName());
				LinearLayout lin_head = viewHolder.getView(R.id.lin_head);
				lin_head.removeAllViews();
				MyRoundDraweeView img_head = new MyRoundDraweeView(
						PlanSearchActivity.this);
				lin_head.addView(img_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_head.setUrlOfImage("");
				viewHolder.setText(R.id.postAuthor, t.getNickName());
				viewHolder.setText(R.id.tv_time, t.getClientUpdateDate());
				viewHolder.setText(R.id.tv_view, "" + t.getViewNum());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								startActivity(new Intent(
										PlanSearchActivity.this,
										PlotDetailActivity.class)
										.putExtra("shareTitle",
												t.getShareTitle())
										.putExtra("shareContent",
												t.getShareContent())
										.putExtra("shareImg", t.getShareImg())
										.putExtra("shareUrl", t.getShareUrl())
										.putExtra("loadUrl", t.getLoadUrl())
										.putExtra("userId", t.getUserId()));
							}
						});
				if (t.getPlanCaseAttachmentEntityList().size() > 0
						&& t.getPlanCaseAttachmentEntityList().size() < 3) {
					viewHolder.getView(R.id.lin_allimg)
							.setVisibility(View.GONE);
					viewHolder.getView(R.id.layout_postImg).setVisibility(
							View.VISIBLE);
					LinearLayout layout_postImg = viewHolder
							.getView(R.id.layout_postImg);
					layout_postImg.removeAllViews();
					MySimpleDraweeView postImg = new MySimpleDraweeView(
							PlanSearchActivity.this);
					layout_postImg.addView(postImg,
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
					postImg.setUrlOfImage(t.getPlanCaseAttachmentEntityList()
							.get(0).getClientThumbFileUrl());
				} else if (t.getPlanCaseAttachmentEntityList().size() == 0) {
					viewHolder.getView(R.id.lin_allimg)
							.setVisibility(View.GONE);
					viewHolder.getView(R.id.layout_postImg).setVisibility(
							View.GONE);

				} else if (t.getPlanCaseAttachmentEntityList().size() >= 3) {
					viewHolder.getView(R.id.layout_postImg).setVisibility(
							View.GONE);
					viewHolder.getView(R.id.lin_allimg).setVisibility(
							View.VISIBLE);
					LinearLayout lin_img1 = viewHolder.getView(R.id.lin_img1);
					lin_img1.removeAllViews();
					MySimpleDraweeView img1 = new MySimpleDraweeView(
							PlanSearchActivity.this);
					lin_img1.addView(img1, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img1.setUrlOfImage(t.getPlanCaseAttachmentEntityList()
							.get(0).getClientThumbFileUrl());
					LinearLayout lin_img2 = viewHolder.getView(R.id.lin_img2);
					lin_img2.removeAllViews();
					MySimpleDraweeView img2 = new MySimpleDraweeView(
							PlanSearchActivity.this);
					lin_img2.addView(img2, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img2.setUrlOfImage(t.getPlanCaseAttachmentEntityList()
							.get(1).getClientThumbFileUrl());
					LinearLayout lin_img3 = viewHolder.getView(R.id.lin_img3);
					lin_img3.removeAllViews();
					MySimpleDraweeView img3 = new MySimpleDraweeView(
							PlanSearchActivity.this);
					lin_img3.addView(img3, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img3.setUrlOfImage(t.getPlanCaseAttachmentEntityList()
							.get(2).getClientThumbFileUrl());
				}
			}

		};
		mPullRefreshListView.setAdapter(adapter);
	}

	protected void upRefreshData() {
		page++;
		GetSearchData();
	}

	protected void downRefreshData() {
		showList.clear();
		page = 1;
		GetSearchData();
	}

	private void GetSearchData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		map.put("searchName", searchName);
		req.setHeader(new ReqHead(AppConfig.PLAN_500001));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PLAN_REQUEST_MAPPING, req, false);
	}

	@Override
	public void initValue() {
	}

	@Override
	public void bindListener() {
		tv_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "##" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				try {
					List<PlanCaseEntity> caseList = (List<PlanCaseEntity>) JsonUtil
							.jsonToList(resp.getBody().get("data").toString(),
									new TypeToken<List<PlanCaseEntity>>() {
									}.getType());
					showList.addAll(caseList);
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
			}
			layoutEmpty.setVisibility(Tool.isEmpty(showList) ? View.VISIBLE
					: View.GONE);
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
	}

}
