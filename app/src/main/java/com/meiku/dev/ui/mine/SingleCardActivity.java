package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.BackgroundConfigEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

/**
 * 好友（个人）主页个性背景
 * 
 */
public class SingleCardActivity extends BaseActivity {
	private TextView right_txt_title;
	private PullToRefreshGridView pull_refreshGV;
	private CommonAdapter<BackgroundConfigEntity> commonAdapter;
	private List<BackgroundConfigEntity> list = new ArrayList<BackgroundConfigEntity>();
	private String clientBackgroundId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.single_card_activity;
	}

	@Override
	public void initView() {
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackground(null);
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Tool.isEmpty(list)
						|| commonAdapter.getSelectedPosition() < 0) {
					ToastUtil.showLongToast("未选择任何图片");
					return;
				}
				// Intent intent = new Intent();
				// intent.putExtra("ClientPicUrl",
				// list.get(commonAdapter.getSelectedPosition())
				// .getClientPicUrl());
				// intent.putExtra("PicUrl",
				// list.get(commonAdapter.getSelectedPosition())
				// .getPicUrl());
				// setResult(RESULT_OK, intent);
				updateCardBg();
			}
		});
		initPullView();
	}

	// 更新主页背景图片url
	public void updateCardBg() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("backGroundId", list.get(commonAdapter.getSelectedPosition())
				.getPicUrl());
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CHANGE_CARDBG));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		mapFileBean.put("photo", new ArrayList<FormFileBean>());
		uploadFiles(reqCodeTwo, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
				mapFileBean, reqBase, true);
	}

	@Override
	public void initValue() {
		clientBackgroundId = getIntent().getStringExtra("clientBackgroundId");
		commonAdapter.setSelectedPosition(-1);
		downRefreshData();
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hhh", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			List<BackgroundConfigEntity> data = (List<BackgroundConfigEntity>) JsonUtil
					.jsonToList(resp.getBody().get("backgroundConfig")
							.toString(),
							new TypeToken<List<BackgroundConfigEntity>>() {
							}.getType());
			if (!Tool.isEmpty(data)) {
				list.addAll(data);
				for (int i = 0, size = list.size(); i < size; i++) {
					if (!Tool.isEmpty(list.get(i).getClientPicUrl())
							&& list.get(i).getClientPicUrl()
									.equals(clientBackgroundId)) {
						commonAdapter.setSelectedPosition(i);
						break;
					}
				}
				pull_refreshGV.setAdapter(commonAdapter);
			}
			break;
		case reqCodeTwo:
			setResult(RESULT_OK, new Intent().putExtra("changeSuccess", true));
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("个性图片加载不成功");
			finish();
			break;

		case reqCodeTwo:
			ToastUtil.showShortToast("更换个性背景图片失败");
			break;
		default:
			break;
		}
	}

	// 请求个性化图片
	public void request() {
		ReqBase req = new ReqBase();
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_BACKGROUND));
		req.setBody(new JsonObject());
		httpPost(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING, req, true);
	}

	private void initPullView() {
		pull_refreshGV = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		pull_refreshGV.setMode(PullToRefreshGridView.Mode.DISABLED);
		pull_refreshGV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// upRefreshData();
					}
				});
		commonAdapter = new CommonAdapter<BackgroundConfigEntity>(
				SingleCardActivity.this, R.layout.single_card_item, list) {

			@Override
			public void convert(final ViewHolder viewHolder,
					BackgroundConfigEntity t) {
				viewHolder.setImage(R.id.img_sinle, t.getClientThumbPicUrl());
				viewHolder.setText(R.id.tv_introduce, t.getRemark());
				final int position = viewHolder.getPosition();
				if (position == getSelectedPosition()) {
					viewHolder.getView(R.id.img_xuanzhong).setVisibility(
							View.VISIBLE);
				} else {
					viewHolder.getView(R.id.img_xuanzhong).setVisibility(
							View.GONE);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								commonAdapter.setSelectedPosition(position);
								commonAdapter.notifyDataSetChanged();
							}
						});
			}
		};
		pull_refreshGV.setAdapter(commonAdapter);
	}

	protected void upRefreshData() {
		page++;
		request();
	}

	protected void downRefreshData() {
		// page = 1;
		list.clear();
		request();
	}

}
