package com.meiku.dev.ui.product;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.R.color;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MkProductCategory;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.views.ViewHolder;

/**
 * <li >传FLAG为SELECT_MONTH 选择发布时长 <li >传FLAG为SELECT_TYPE 选择产品类型
 */
public class PublishDurationActivity extends BaseActivity {

	private GridView gridview;
	private CommonAdapter<String> showAdapter;
	private List<String> showlist = new ArrayList<String>();
	private String flag;
	private List<MkProductCategory> showMenuList = new ArrayList<MkProductCategory>();

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.acticity_publishduration;
	}

	@Override
	public void initView() {
		TextView title = (TextView) findViewById(R.id.center_txt_title);
		flag = getIntent().getStringExtra("FLAG");
		if (flag.equals("SELECT_MONTH")) {
			title.setText("发布时长");
			for (int i = 1; i <= 12; i++) {
				showlist.add(i + "个月");
			}
		} else if (flag.equals("SELECT_TYPE")) {
			title.setText("产品类型");
			showMenuList.addAll(MKDataBase.getInstance().getProdectCategory());
			for (int i = 0, size = showMenuList.size(); i < size; i++) {
				showlist.add(showMenuList.get(i).getName());
			}
		}

		gridview = (GridView) findViewById(R.id.gridview);
		showAdapter = new CommonAdapter<String>(PublishDurationActivity.this,
				R.layout.item_txt, showlist) {

			@Override
			public void convert(ViewHolder viewHolder, final String t) {
				viewHolder.setText(R.id.txt, t);
				final int position = viewHolder.getPosition();
				viewHolder.getView(R.id.txt).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent();
								intent.putExtra("index", position);
								intent.putExtra("value", t);
								if (flag.equals("SELECT_TYPE")) {
									intent.putExtra("id",
											showMenuList.get(position).getId());
								}
								setResult(RESULT_OK, intent);
								finish();
							}
						});
			}
		};
		gridview.setAdapter(showAdapter);
	}

	@Override
	public void initValue() {
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
