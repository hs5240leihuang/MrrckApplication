package com.meiku.dev.ui.recruit;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

/**
 * 多选页面
 * 
 */
public class MultiSelectActivity extends BaseActivity {

	private ListView listView;
	private CommonAdapter<DataconfigEntity> showAdapter;
	private List<DataconfigEntity> showList = new ArrayList<DataconfigEntity>();
	private TextView center_txt_title;
	private int type;

	public final static int TYPE_BENIFIT = 1;// 福利待遇
	public final static int TYPE_KNOWLEDGE = 2;// 专业技能
	private String ids;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_multiselect;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		listView = (ListView) findViewById(R.id.pull_refresh_list);
		showAdapter = new CommonAdapter<DataconfigEntity>(
				MultiSelectActivity.this, R.layout.item_multiselect, showList) {

			@Override
			public void convert(ViewHolder viewHolder, final DataconfigEntity t) {
				viewHolder.setText(R.id.name, t.getValue());
				if ("0".equals(t.getDelStatus())) {
					viewHolder.setImage(R.id.select,
							R.drawable.icon_weixuanzhong);
				} else {
					viewHolder.setImage(R.id.select, R.drawable.icon_duigoux);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if ("0".equals(t.getDelStatus())) {
									t.setDelStatus("1");
								} else {
									t.setDelStatus("0");
								}
								notifyDataSetChanged();
							}
						});
			}

		};
		listView.setAdapter(showAdapter);
	}

	@Override
	public void initValue() {
		ids = getIntent().getStringExtra("ids");
		type = getIntent().getIntExtra("type", 0);
		switch (type) {
		case TYPE_BENIFIT:
			center_txt_title.setText("福利待遇");
			showList.addAll(MKDataBase.getInstance().getBenefits());
			if (showList != null && !Tool.isEmpty(ids)) {
				for (int i = 0, size = showList.size(); i < size; i++) {
					if (ids.contains(showList.get(i).getCodeId())) {
						showList.get(i).setDelStatus("1");
					}
				}
			}
			showAdapter.notifyDataSetChanged();
			break;
		case TYPE_KNOWLEDGE:
			center_txt_title.setText("专业技能");
			showList.addAll(MKDataBase.getInstance().getWorkSkill());
			if (showList != null && !Tool.isEmpty(ids)) {
				for (int i = 0, size = showList.size(); i < size; i++) {
					if (ids.contains(showList.get(i).getCodeId())) {
						showList.get(i).setDelStatus("1");
					}
				}
			}
			showAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	@Override
	public void bindListener() {
		findViewById(R.id.right_txt_title).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						switch (type) {
						case TYPE_BENIFIT:
						case TYPE_KNOWLEDGE:
							if (showList != null) {
								String selectIds = "";
								String selectValues = "";
								for (int i = 0, size = showList.size(); i < size; i++) {
									if ("1".equals(showList.get(i)
											.getDelStatus())) {
										selectIds += showList.get(i)
												.getCodeId() + ",";
										selectValues += showList.get(i)
												.getValue() + ",";
									}
								}
								if (!Tool.isEmpty(selectIds)
										&& !Tool.isEmpty(selectValues)) {
									Intent intent = new Intent();
									intent.putExtra(
											"selectIds",
											selectIds.substring(0,
													selectIds.length() - 1));// 去除最后一个逗号
									intent.putExtra("selectValues",
											selectValues.substring(0,
													selectValues.length() - 1));
									setResult(RESULT_OK, intent);
								} else {
									ToastUtil.showShortToast("请至少选择一项");
									return;
								}
							}
							finish();
							break;
						default:
							break;
						}
					}
				});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

}
