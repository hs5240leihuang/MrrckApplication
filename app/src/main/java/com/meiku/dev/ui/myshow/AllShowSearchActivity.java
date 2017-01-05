package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PostsSignupSearchRecordEntity;
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
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MyListView;
import com.meiku.dev.views.ViewHolder;

/**
 * 秀场搜索页面
 * 
 */
public class AllShowSearchActivity extends BaseActivity {

	private ClearEditText etSearch;
	private TextView cancel;
	private PullToRefreshScrollView pull_refreshSV;
	private LinearLayout layout_hot;
	private List<PostsSignupSearchRecordEntity> hotSearchGridData = new ArrayList<PostsSignupSearchRecordEntity>();
	private List<PostsSignupSearchRecordEntity> historyListData = new ArrayList<PostsSignupSearchRecordEntity>();
	private List<String> searchMatchListData = new ArrayList<String>();
	private CommonAdapter<PostsSignupSearchRecordEntity> hotSearchAdapter;
	private CommonAdapter<PostsSignupSearchRecordEntity> historyListAdapter;
	private CommonAdapter<String> searchMatchListAdapter;
	private MyListView historyListView;
	private MyListView matchListView;
	private boolean isHotPage;
	private int matchPage = 1;
	protected boolean isUpRefresh;
	private TextView clearView;
	private int matchId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_allshowsearch;
	}

	@Override
	public void initView() {
		matchId = getIntent().getIntExtra("matchId", -1);// matchId为-1则为秀场作品搜索，否则是赛事作品搜素
		initSearch();
		initPullView();
		initHotSearchGrid();
		// initHistorySearchList();
		initSearchList();
		showHotAndHistorySearch(false);
	}

	private void initSearch() {
		etSearch = (ClearEditText) findViewById(R.id.et_msg_search);
		etSearch.setHint("作品名、姓名、作品编号");
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				String searchContent = etSearch.getText().toString();
				// if (!Tool.isEmpty(searchContent)) {
				showHotAndHistorySearch(false);
				matchPage = 1;
				searchMatchListData.clear();
				if (!Tool.isEmpty(searchContent)) {
					doSearchMatch(searchContent);
				} else {
					searchMatchListAdapter.notifyDataSetChanged();
				}
				// clearView.setVisibility(View.GONE);
				// }
				// else {
				// showHotAndHistorySearch(true);
				// matchPage = 1;
				// searchMatchListData.clear();
				// searchMatchListAdapter.notifyDataSetChanged();
				// downRefreshData();
				// }
			}
		});
		cancel = (TextView) findViewById(R.id.cancel_text);
		cancel.setVisibility(View.VISIBLE);
		cancel.setTextSize(17);
		cancel.setBackgroundColor(getResources().getColor(R.color.transparent));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputTools.HideKeyboard(etSearch);
				finish();
			}
		});
	}

	/**
	 * 匹配后的搜索列表
	 */
	private void initSearchList() {
		matchListView = (MyListView) findViewById(R.id.searchListView);
		searchMatchListAdapter = new CommonAdapter<String>(
				AllShowSearchActivity.this, R.layout.item_searchmatch,
				searchMatchListData) {

			@Override
			public void convert(ViewHolder viewHolder, String t) {
				viewHolder.setText(R.id.txt, t);
			}

		};
		matchListView.setAdapter(searchMatchListAdapter);
		matchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AllShowSearchActivity.this,
						AllShowSearchResultActivity.class);
				intent.putExtra("searchName", searchMatchListData.get(arg2));
				if (matchId != -1) {// 赛事搜素所具有
					intent.putExtra("matchId", matchId);
				}
				startActivity(intent);
			}
		});
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
						isUpRefresh = true;
						upRefreshData();
					}
				});
	}

	/**
	 * 历史搜索列表
	 */
	// private void initHistorySearchList() {
	// historyListView = (MyListView) findViewById(R.id.myListView);
	// historyListAdapter = new CommonAdapter<PostsSignupSearchRecordEntity>(
	// AllShowSearchActivity.this, R.layout.item_show_searchhistory,
	// historyListData) {
	//
	// @Override
	// public void convert(ViewHolder viewHolder,
	// PostsSignupSearchRecordEntity t) {
	// final String searchName = t.getSearchName();
	// viewHolder.setText(R.id.id_txt, searchName);
	// viewHolder.getView(R.id.layout).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// etSearch.setText(searchName);
	// etSearch.setSelection(searchName.length());
	// }
	// });
	// viewHolder.getView(R.id.id_delete).setOnClickListener(
	// new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// deleteOneSearchHistory(searchName);
	// }
	// });
	// }
	//
	// };
	// historyListView.setAdapter(historyListAdapter);
	// //clearView = (TextView) findViewById(R.id.clearView);
	// // clearView.setOnClickListener(new OnClickListener() {
	// //
	// // @Override
	// // public void onClick(View arg0) {
	// // if (Tool.isEmpty(historyListData)) {
	// // ToastUtil.showShortToast("暂无搜索记录");
	// // } else {
	// // deleteAllSearchHistory();
	// // }
	// // }
	// // });
	// }

	/**
	 * 热门搜索列表
	 */
	private void initHotSearchGrid() {
		layout_hot = (LinearLayout) findViewById(R.id.layout_hot);
		MyGridView myGridView = (MyGridView) findViewById(R.id.gridview);
		hotSearchAdapter = new CommonAdapter<PostsSignupSearchRecordEntity>(
				AllShowSearchActivity.this, R.layout.item_show_hotsearchtxt,
				hotSearchGridData) {

			@Override
			public void convert(ViewHolder viewHolder,
					PostsSignupSearchRecordEntity t) {
				viewHolder.setText(R.id.txt, t.getSearchName());
			}

		};
		myGridView.setAdapter(hotSearchAdapter);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String txt = hotSearchGridData.get(arg2).getSearchName();
				etSearch.setText(txt);
				etSearch.setSelection(txt.length());
			}
		});
	}

	protected void showHotAndHistorySearch(boolean show) {
		layout_hot.setVisibility(show ? View.VISIBLE : View.GONE);
		// historyListView.setVisibility(show ? View.VISIBLE : View.GONE);
		matchListView.setVisibility(show ? View.GONE : View.VISIBLE);
		isHotPage = show;
	}

	protected void downRefreshData() {
		if (isHotPage) {
			historyListData.clear();
			page = 1;
			// getMySearchRecord();
		} else {
			matchPage = 1;
			searchMatchListData.clear();
			doSearchMatch(etSearch.getText().toString());
		}

	}

	protected void upRefreshData() {
		if (isHotPage) {
			page++;
			// getMySearchRecord();
		} else {
			matchPage++;
			doSearchMatch(etSearch.getText().toString());
		}
	}

	@Override
	public void initValue() {
		// getMySearchRecord();
	}

	/**
	 * 获取我的搜索记录
	 */
	// private void getMySearchRecord() {
	// ReqBase req = new ReqBase();
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("page", page);
	// map.put("userId", AppContext.getInstance().getUserInfo().getId());
	// map.put("pageNum", ConstantKey.PageNum);
	// req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_GET_CITIAO));
	// req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
	// httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	// }

	/**
	 * 删除一条搜索记录
	 */
	// private void deleteOneSearchHistory(String searchName) {
	// ReqBase req = new ReqBase();
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("searchName", searchName);
	// map.put("userId", AppContext.getInstance().getUserInfo().getId());
	// req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_DELETE_CITIAO));
	// req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
	// httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
	// }

	/**
	 * 匹配搜索
	 */
	protected void doSearchMatch(String searchName) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", matchPage);
		map.put("searchName", searchName);
		map.put("pageNum", ConstantKey.PageNum);
		if (matchId != -1) {// 赛事搜素所具有
			map.put("matchId", matchId);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_MATCH_SEARCH_CITIAO));
		} else {
			req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_MATCH));
		}
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.PUBLICK_BOARD, req, false);
	}

	/**
	 * 删除一条搜索记录
	 */
	// private void deleteAllSearchHistory() {
	// ReqBase req = new ReqBase();
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("userId", AppContext.getInstance().getUserInfo().getId());
	// req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_CLEAR));
	// req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
	// httpPost(reqCodeFour, AppConfig.PUBLICK_BOARD, req, true);
	// }

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "_" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			// 热门搜索
			// String hotSearchStr = resp.getBody().get(
			// "postsSignupHotSearchRecord")
			// + "";
			// if (hotSearchStr.length() > 2) {
			// layout_hot.setVisibility(View.VISIBLE);
			// List<PostsSignupSearchRecordEntity> hotData =
			// (List<PostsSignupSearchRecordEntity>) JsonUtil
			// .jsonToList(
			// hotSearchStr,
			// new TypeToken<List<PostsSignupSearchRecordEntity>>() {
			// }.getType());
			// if (!Tool.isEmpty(hotData)) {
			// hotSearchGridData.clear();
			// hotSearchGridData.addAll(hotData);
			// }
			// hotSearchAdapter.notifyDataSetChanged();
			// }
			// // 我的搜索记录
			// String SearchRecord =
			// resp.getBody().get("postsSignupSearchRecord")
			// + "";
			// if (SearchRecord.length() > 2) {
			// List<PostsSignupSearchRecordEntity> hisData =
			// (List<PostsSignupSearchRecordEntity>) JsonUtil
			// .jsonToList(
			// SearchRecord,
			// new TypeToken<List<PostsSignupSearchRecordEntity>>() {
			// }.getType());
			// if (!Tool.isEmpty(hisData)) {
			// historyListData.addAll(hisData);
			// }
			// } else if (isUpRefresh) {
			// ToastUtil.showShortToast("无更多搜索记录");
			// }
			// // historyListAdapter.notifyDataSetChanged();
			// // clearView.setVisibility(Tool.isEmpty(historyListData) ?
			// View.GONE
			// // : View.VISIBLE);
			// break;
		case reqCodeTwo:
			historyListData.clear();
			page = 1;
			// getMySearchRecord();
			break;
		case reqCodeThree:
			if ((resp.getBody().get("dataList") + "").length() > 2) {
				List<String> matchData = (List<String>) JsonUtil.jsonToList(
						resp.getBody().get("dataList").toString(),
						new TypeToken<List<String>>() {
						}.getType());
				if (!Tool.isEmpty(matchData)) {
					searchMatchListData.addAll(matchData);
				} else if (isUpRefresh) {
					ToastUtil.showShortToast("无更多搜索数据");
				}
			}

			searchMatchListAdapter.notifyDataSetChanged();
			break;
		case reqCodeFour:
			historyListData.clear();
			// historyListAdapter.notifyDataSetChanged();
			ToastUtil.showShortToast("清空搜索记录成功!");
			// clearView.setVisibility(View.GONE);
		}
		pull_refreshSV.onRefreshComplete();
		isUpRefresh = false;
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeTwo:
			ToastUtil.showShortToast("删除失败!");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("清空搜索记录失败!");
			break;
		}
		if (null!=pull_refreshSV) {
			pull_refreshSV.onRefreshComplete();
		}
		isUpRefresh = false;
	}

}
