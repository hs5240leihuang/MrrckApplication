package com.meiku.dev.ui.encyclopaedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.BaikeMkEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

/**
 * 词条搜索页面
 */
public class EncyclopaediaSearchActivity extends BaseActivity {

	private ClearEditText et_msg_search;
	private PullToRefreshListView mPullRefreshListView;
	private List<BaikeMkEntity> showlist = new ArrayList<BaikeMkEntity>();
	private CommonAdapter<BaikeMkEntity> showAdapter;
	private TextView cancel;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_baike_search;
	}

	@Override
	public void initView() {
		et_msg_search = (ClearEditText) findViewById(R.id.et_msg_search);
		et_msg_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_SEARCH) {

				}
				return false;
			}
		});

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
				downRefreshData();
			}
		});
		cancel = (TextView) findViewById(R.id.cancel_text);
		cancel.setVisibility(View.VISIBLE);
		cancel.setTextSize(17);
		cancel.setBackgroundColor(getResources().getColor(R.color.transparent));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputTools.HideKeyboard(et_msg_search);
				finish();
			}
		});
		initPullListView();
		showAdapter = new CommonAdapter<BaikeMkEntity>(
				EncyclopaediaSearchActivity.this, R.layout.item_mycitiao,
				showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final BaikeMkEntity t) {
				LinearLayout lin_photo = viewHolder.getView(R.id.lin_photo);
				lin_photo.removeAllViews();
				MyRectDraweeView img_photo = new MyRectDraweeView(
						EncyclopaediaSearchActivity.this);
				lin_photo.addView(img_photo, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img_photo.setUrlOfImage(t.getMainPhotoThumb());
				viewHolder.setText(R.id.tv_name, t.getName());
				viewHolder.setText(R.id.tv_summary, t.getSummary());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {

							}
						});
			}

		};
		mPullRefreshListView.setAdapter(showAdapter);
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
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						upRefreshData();
					}
				});

	}

	protected void upRefreshData() {
		page++;
		getSearchData();
	}

	protected void downRefreshData() {
		page = 1;
		showlist.clear();
		getSearchData();
	}

	/**
	 * 获取搜索数据
	 */
	private void getSearchData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("searchName", et_msg_search.getText().toString());
		map.put("categoryId", -1);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCHBAIKE));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BAIKE, req, true);
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "result__" + resp.getBody());
		if (resp.getBody().get("baiKe").toString().length() > 2) {
			List<BaikeMkEntity> listData = (List<BaikeMkEntity>) JsonUtil
					.jsonToList(resp.getBody().get("baiKe").toString(),
							new TypeToken<List<BaikeMkEntity>>() {
							}.getType());
			showlist.addAll(listData);
		} else {
			ToastUtil.showShortToast("没有更多数据");
		}
		showAdapter.notifyDataSetChanged();
		mPullRefreshListView.onRefreshComplete();
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("搜索词条失败");
			break;

		default:
			break;
		}
	}

}
