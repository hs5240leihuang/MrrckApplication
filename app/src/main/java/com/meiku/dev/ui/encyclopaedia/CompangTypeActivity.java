package com.meiku.dev.ui.encyclopaedia;

import java.util.List;

import android.R.string;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.ReportType;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.views.ViewHolder;

public class CompangTypeActivity extends BaseActivity {
	private CommonAdapter<DataconfigEntity> showAdapter;
	private ListView listView;
	private List<DataconfigEntity> titletabList;
	private String type;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_companytype;
	}

	@Override
	public void initView() {
		listView = (ListView) findViewById(R.id.list_type);
		titletabList = MKDataBase.getInstance().getCompanyType();
	}

	@Override
	public void initValue() {
		showAdapter = new CommonAdapter<DataconfigEntity>(
				CompangTypeActivity.this, R.layout.item_companytype,
				titletabList) {

			@Override
			public void convert(final ViewHolder viewHolder, DataconfigEntity t) {
				viewHolder.setText(R.id.tv_companytype, t.getValue());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								type = titletabList.get(
										viewHolder.getPosition()).getValue();
								Intent intent=new Intent();
								intent.putExtra("type", type);
								setResult(RESULT_OK, intent);
								finish();
							}
						});
			}
		};
		listView.setAdapter(showAdapter);
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
