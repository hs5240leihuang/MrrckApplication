package com.meiku.dev.ui.morefun;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.views.ViewHolder;

/**
 * Created by zjh on 2015/8/26.
 */
public class DataConfigSelectActivity extends BaseActivity {

	public static final String BUNDLE_CODE = "code";

	private TextView topTitle;// 标题
	private ListView listView;// 列表控件

	private List<DataconfigEntity> mList = new ArrayList<DataconfigEntity>();
	private CommonAdapter<DataconfigEntity> mAdapter;

	private String mCode;

	@Override
	public void initView() {
		topTitle = (TextView) findViewById(R.id.center_txt_title);
		topTitle.setText("请选择");
		listView = (ListView) findViewById(R.id.list_view);

		listView.setAdapter(mAdapter = new CommonAdapter<DataconfigEntity>(
				getApplication(), R.layout.select_item_group, mList) {
			@Override
			public void convert(ViewHolder viewHolder, DataconfigEntity entity) {
				viewHolder.setText(R.id.text_ku, entity.getValue());
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DataconfigEntity entity = mList.get(position);

				Intent data = new Intent();
				data.putExtra("value", entity.getValue());
				data.putExtra("codeId", entity.getCodeId());

				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	private void initData() {
		if (!TextUtils.isEmpty(mCode)) {
			mList.addAll(MKDataBase.getInstance().getDataConfigList(mCode));
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_dataconfig_select;
	}

	@Override
	public void initValue() {
		mCode = getIntent().getStringExtra(BUNDLE_CODE);
		initData();
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
