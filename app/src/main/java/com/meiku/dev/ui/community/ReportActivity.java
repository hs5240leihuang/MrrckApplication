package com.meiku.dev.ui.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ReportType;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

/**
 * 举报页面
 * 
 */
public class ReportActivity extends BaseActivity {

	private MyGridView gridview;
	private List<ReportType> reportTypeList = new ArrayList<ReportType>();
	private CommonAdapter<ReportType> showAdapter;
	private String toUserName, content, sourceType, sourceId;
	private TextView tv_username, tv_content;
	private int reportType;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_report;
	}

	@Override
	public void initView() {
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_content = (TextView) findViewById(R.id.tv_content);
		gridview = (MyGridView) findViewById(R.id.gridview);
		reportTypeList = MKDataBase.getInstance().getReportType();
		showAdapter = new CommonAdapter<ReportType>(ReportActivity.this,
				R.layout.item_xingqu, reportTypeList) {

			@Override
			public void convert(ViewHolder viewHolder, ReportType t) {
				viewHolder.getView(R.id.icon).setVisibility(View.VISIBLE);
				if (showAdapter.getSelectedPosition() == viewHolder
						.getPosition()) {
					viewHolder.setImage(R.id.icon, R.drawable.checkbos_bg_on);
				} else {
					viewHolder.setImage(R.id.icon, R.drawable.checkbos_bg_off);
				}
				viewHolder.setText(R.id.text, t.getName());
			}

		};
		gridview.setAdapter(showAdapter);
		reportType = reportTypeList.get(0).getId();
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showAdapter.setSelectedPosition(arg2);
				reportType = reportTypeList.get(arg2).getId();

				showAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void initValue() {
		toUserName = getIntent().getStringExtra("nickname");
		content = getIntent().getStringExtra("content");
		sourceType = getIntent().getStringExtra("sourceType");
		sourceId = getIntent().getStringExtra("sourceId");

		tv_username.setText(toUserName);
		if (Tool.isEmpty(content)) {
			tv_content.setText("[图片]");
		} else {
			tv_content.setText(content);
		}
	}

	@Override
	public void bindListener() {
		findViewById(R.id.right_txt_title).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("userId", AppContext.getInstance()
								.getUserInfo().getUserId());
						map.put("sourceId", sourceId);
						map.put("sourceType", sourceType);
						map.put("reportType", reportType);
						map.put("remark", "");
						LogUtil.d("nathan", "请求--举报" + map);
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_BOARD_REPORT));
						req.setBody(JsonUtil.Map2JsonObj(map));
						httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req,
								false);
					}
				});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("举报成功");
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("举报失败");
	}
}
