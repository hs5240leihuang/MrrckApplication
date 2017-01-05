package com.meiku.dev.ui.im;

import java.util.List;

import android.R.string;
import android.content.Intent;
import android.graphics.RadialGradient;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.bean.ReportType;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.myshow.WorkCategoryActivity;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class JuBaoActivity extends BaseActivity {
	private TextView next;
	private ListView category_list;
	private List<ReportType> titletabList;
	private CommonAdapter<ReportType> showAdapter;
	private int reportType;
	private String friendid;
	private String sourceType;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_jubao_reason;
	}

	@Override
	public void initView() {
		next = (TextView) findViewById(R.id.right_txt_title);
		category_list = (ListView) findViewById(R.id.category_list);
	}

	@Override
	public void initValue() {
		sourceType=getIntent().getStringExtra("sourceType");
		friendid=getIntent().getStringExtra("friendid");
		titletabList = MKDataBase.getInstance().getReportType();

		// 适配器
		showAdapter = new CommonAdapter<ReportType>(JuBaoActivity.this,
				R.layout.item_reporttype, titletabList) {

			@Override
			public void convert(ViewHolder viewHolder, final ReportType t) {
				viewHolder.setText(R.id.tv_category, t.getName());
				if (showAdapter.getSelectedPosition() == viewHolder
						.getPosition()) {
					viewHolder.getView(R.id.img_ok).setVisibility(View.VISIBLE);
					viewHolder.setImage(R.id.img_ok, R.drawable.reporttype);
				} 
				else{
					viewHolder.getView(R.id.img_ok).setVisibility(View.INVISIBLE);
				}
			}
		};
		category_list.setAdapter(showAdapter);
		reportType = titletabList.get(0).getId();
		category_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showAdapter.setSelectedPosition(arg2);
				reportType = titletabList.get(arg2).getId();
				showAdapter.notifyDataSetChanged();
			}
		});
		next.setText("下一步");
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(JuBaoActivity.this,
						CommitActivity.class);
				intent.putExtra("friendid", friendid);
				intent.putExtra("reportType", reportType);
				intent.putExtra("sourceType", sourceType);
				startActivity(intent);
			}
		});
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

	}

}
