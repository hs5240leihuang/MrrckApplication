package com.meiku.dev.ui.myshow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CreateGroupChatAdapter;
import com.meiku.dev.adapter.SelectAllCityAdapter;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.bean.FriendEntity;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.PinyinComparator;
import com.meiku.dev.utils.PinyinCompare;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.EnLetterView;

public class SelectAllCityActivity extends BaseActivity {

	private EnLetterView rightLetter;
	private ListView list_friends;
	private TextView dialogTextView;
	private List<AreaEntity> result = new ArrayList<AreaEntity>();;
	private SelectAllCityAdapter adapter;
	
	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_select_all_city;
	}

	@Override
	public void initView() {
		list_friends = (ListView) findViewById(R.id.list_friends);
		initRightLetterViewAndSearchEdit();
		adapter = new SelectAllCityAdapter(this);
		list_friends.setAdapter(adapter);
	}

	private void initRightLetterViewAndSearchEdit() {
		rightLetter = (EnLetterView) findViewById(R.id.right_letter);
		dialogTextView = (TextView) findViewById(R.id.dialog);
		rightLetter.setTextView(dialogTextView);
		rightLetter
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	private class LetterListViewListener implements
			EnLetterView.OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(String s) {
			int position = adapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				list_friends.setSelection(position);
			}
		}
	}

	@Override
	public void initValue() {
		PostsEntity postsEntity = (PostsEntity) getIntent().getSerializableExtra("postEntity");
		if(!Tool.isEmpty(postsEntity)&&!Tool.isEmpty(postsEntity.getActiveProvinceCode())){
			result = MKDataBase.getInstance().getAllCityByProvinceCode(postsEntity.getActiveProvinceCode());
		}
		if(!Tool.isEmpty(postsEntity)&&!Tool.isEmpty(postsEntity.getActiveCityCode())){
			result.addAll(MKDataBase.getInstance().getAllCityByCityCode(postsEntity.getActiveCityCode()));
		}
		SortTask sortTask = new SortTask();
		sortTask.execute(result);
	}
	
	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}
	
	class SortTask extends AsyncTask<List<AreaEntity>, List<AreaEntity>, List<AreaEntity>>{  
          
        @Override  
        protected void onPreExecute() {  
            super.onPreExecute();  
        }  
          
        @Override  
        protected List<AreaEntity> doInBackground(List<AreaEntity>... params) {  
        	Collections
    		.sort(params[0], new PinyinCompare());
            return params[0];  
        }  
  
        @Override  
        protected void onProgressUpdate(List<AreaEntity>... progress) {  
            super.onProgressUpdate(progress);  
        }  
  
        @Override  
        protected void onPostExecute(List<AreaEntity> result) {  
        	adapter.setData(result);  
            super.onPostExecute(result);  
        }  
          
    } 
}
