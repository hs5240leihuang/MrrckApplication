package com.meiku.dev.ui.myshow;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class WorkCategoryActivity extends BaseActivity{

	private List<MkPostsActiveCategory> titletabList;
	private ListView category_list;
	private CommonAdapter<MkPostsActiveCategory> showAdapter;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_workcategory;
	}

	@Override
	public void initView() {
		category_list = (ListView) findViewById(R.id.category_list);
	}

	@Override
	public void initValue() {
		if(!Tool.isEmpty(getIntent().getStringExtra("categoryIds"))){
			String categoryIds = getIntent().getStringExtra("categoryIds");
			titletabList = MKDataBase.getInstance().getShowCategoryTabsByCategories(categoryIds);
		}else{
			titletabList = MKDataBase.getInstance().getShowCategoryTabs();
		}
		// 适配器
		showAdapter = new CommonAdapter<MkPostsActiveCategory>(WorkCategoryActivity.this,
				R.layout.item_workcategory, titletabList) {

 			@Override
			public void convert(ViewHolder viewHolder, final MkPostsActiveCategory t) {
 				viewHolder.setText(R.id.tv_category, t.getName());
 				viewHolder.getView(R.id.layout_category).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.putExtra("categoryName", t.getName()+"");
						intent.putExtra("categoryId", t.getId()+"");
						intent.putExtra("fileType", t.getFileType()+"");
						setResult(RESULT_OK, intent);
						finish();
					}
				});
			}
		};
		category_list.setAdapter(showAdapter);
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
