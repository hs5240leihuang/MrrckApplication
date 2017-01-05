package com.meiku.dev.ui.morefun;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.JobClassEntity;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.views.ViewHolder;

public class SelectPositionActivity extends BaseActivity {
	private List<JobClassEntity> resultList;
	private ListView listView;
	private List<JobClassEntity> subResultList;
	private int id;

	private TextView title;
	private int level = 1;
	private String logId = "";
	private int flag;
	private boolean hasUnlimited;

	@Override
	public void initView() {

		title = (TextView) findViewById(R.id.center_txt_title);

		findViewById(R.id.goback).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (1 == level) {
							finish();
						} else {
							initView();
							title.setText("选择职位");
							level = 1;
						}
					}
				});

		listView = (ListView) findViewById(R.id.postionList_lv);

		resultList = new ArrayList<JobClassEntity>();
		boolean hasJobFlag = getIntent().getBooleanExtra("hasJobFlag", false);
		if (hasJobFlag) {
			resultList = MKDataBase.getInstance().getFindJobPositionList();
		} else {
			resultList = MKDataBase.getInstance().getJobClassIntent();
		}

		Intent intent = getIntent();
		logId = intent.getStringExtra("id");
		flag = intent.getIntExtra("flag", 0);
		hasUnlimited = intent.getBooleanExtra("hasUnlimited", false);// 是否带有不限选择项
		if (hasUnlimited) {
			JobClassEntity jce = new JobClassEntity();
			jce.setName("不限");
			jce.setId(-1);
			jce.setGroupid(-1);
			jce.setType("-1");
			resultList.add(0, jce);
		}
		if ("".equals(logId) || null == logId) {// 如果从SelectLibraryGroupFragment选择了群库进入登陆页面直接选择职业

			listView.setAdapter(new CommonAdapter<JobClassEntity>(this,
					R.layout.listview_item_text, resultList) {
				@Override
				public void convert(ViewHolder viewHolder,
						JobClassEntity jobClassDTO) {
					viewHolder.setText(R.id.text, jobClassDTO.getName());
				}
			});
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long sid) {
					id = resultList.get(position).getId();
					if (hasUnlimited && id == -1) {
						Intent intent = new Intent(
								BroadCastAction.ACTION_PUBLIC_SEARCH_RESUME);
						intent.putExtra("id", resultList.get(position).getId());
						intent.putExtra("groupId", resultList.get(position)
								.getGroupid());
						intent.putExtra("type", resultList.get(position)
								.getType());
						intent.putExtra("name", resultList.get(position)
								.getName());
						if (flag == 1) {
							sendBroadcast(intent);
						}
						setResult(RESULT_OK, intent);
						finish();
						return;
					}
					level = 2;

					title.setText(resultList.get(position).getName());
					subResultList = MKDataBase.getInstance().getJobSubClass(id);
					listView.setAdapter(new CommonAdapter<JobClassEntity>(
							getApplicationContext(),
							R.layout.listview_item_text, subResultList) {
						@Override
						public void convert(ViewHolder viewHolder,
								final JobClassEntity jobClassDTO) {
							viewHolder.setText(R.id.text, jobClassDTO.getName());
						}
					});

					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(
									BroadCastAction.ACTION_PUBLIC_SEARCH_RESUME);
							intent.putExtra("id", subResultList.get(position)
									.getId());
							intent.putExtra("groupId",
									subResultList.get(position).getGroupid());
							intent.putExtra("type", subResultList.get(position)
									.getType());
							intent.putExtra("name", subResultList.get(position)
									.getName());
							if (flag == 1) {
								sendBroadcast(intent);
							}
							setResult(RESULT_OK, intent);
							finish();
						}
					});
				}
			});
		} else if (Integer.parseInt(logId) == -1) {
			subResultList = MKDataBase.getInstance().getJobClassIntent();
			listView.setAdapter(new CommonAdapter<JobClassEntity>(
					getApplicationContext(), R.layout.listview_item_text,
					subResultList) {
				@Override
				public void convert(ViewHolder viewHolder,
						final JobClassEntity jobClassDTO) {
					viewHolder.setText(R.id.text, jobClassDTO.getName());
				}
			});

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(
							BroadCastAction.ACTION_PUBLIC_SEARCH_RESUME);
					intent.putExtra("id", subResultList.get(position).getId());
					intent.putExtra("groupId", subResultList.get(position)
							.getGroupid());
					intent.putExtra("type", subResultList.get(position)
							.getType());
					intent.putExtra("name", subResultList.get(position)
							.getName());
					if (flag == 1) {
						sendBroadcast(intent);
					}
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		} else {
			id = Integer.parseInt(logId);
			subResultList = MKDataBase.getInstance().getJobSubClass(id);
			listView.setAdapter(new CommonAdapter<JobClassEntity>(
					getApplicationContext(), R.layout.listview_item_text,
					subResultList) {
				@Override
				public void convert(ViewHolder viewHolder,
						final JobClassEntity jobClassDTO) {
					viewHolder.setText(R.id.text, jobClassDTO.getName());
				}
			});

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(
							BroadCastAction.ACTION_PUBLIC_SEARCH_RESUME);
					intent.putExtra("id", subResultList.get(position).getId());
					intent.putExtra("groupId", subResultList.get(position)
							.getGroupid());
					intent.putExtra("type", subResultList.get(position)
							.getType());
					intent.putExtra("name", subResultList.get(position)
							.getName());
					if (flag == 1) {
						sendBroadcast(intent);
					}
					setResult(RESULT_OK, intent);
					finish();
				}
			});

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (1 == level) {
				finish();
			} else {
				initView();
				title.setText("选择职位");
				level = 1;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_select_position;
	}

	@Override
	public void initValue() {
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

}
