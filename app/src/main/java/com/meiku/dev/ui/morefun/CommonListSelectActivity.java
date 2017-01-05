package com.meiku.dev.ui.morefun;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.JobClassEntity;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.views.ViewHolder;

public class CommonListSelectActivity extends BaseActivity {

	private TextView topTitle;// 标题
	private ListView listView;// 列表控件

	private static final String[] genderStr = { "男", "女" };// 性别
	private static final String[] gender = { "不限", "男", "女" };// 性别
	// 现在岗位
	// 现居地
	// private static final String[] educationStr = {"初中以下", "初中", "高中", "中专",
	// "大专", "本科", "本科以上"};//最高学历
	// private static final String[] workTimeStr = {"1年以下", "1年", "2年", "3年",
	// "4年", "5年", "6年", "7年", "8年", "9年", "10年", "10年以上"};//美业职龄
	// private static final String[] marryStr = {"未婚", "已婚", "保密"};//婚姻状况
	private static final String[] marryStr = { "不限", "已婚", "未婚" };// 发布职位婚姻状况
	private static final String[] marryStr1 = { "未婚", "已婚", "保密" };// 个人婚姻状况
	private static final String[] mrequireStayStr = { "不提供住宿", "提供住宿" };// 是否住宿
	// private static final String[] needNumbers = {"若干", "1人", "2人", "3人",
	// "4人", "5人"};//需要人数

	ArrayAdapter<String> adapter;
	private CommonAdapter<AreaEntity> coMadapter;// 城市

	private CommonAdapter<JobClassEntity> jobadapter;
	private CommonAdapter<DataconfigEntity> sarayAdapter;

	private void initDate(String[] dates) {
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dates);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				String selected = ((TextView) view).getText().toString();
				String postion = position + "";
				intent.putExtra(getIntent().getStringExtra("tag"), selected);
				intent.putExtra(getIntent().getStringExtra("postion"), postion);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void initDate(final List<AreaEntity> list) {

		coMadapter = new CommonAdapter<AreaEntity>(getApplication(),
				R.layout.select_item_group, list) {
			@Override
			public void convert(ViewHolder viewHolder,
					AreaEntity jobClassEntities) {
				viewHolder
						.setText(R.id.text_ku, jobClassEntities.getCityName());
			}
		};
		listView.setAdapter(coMadapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				String selected = list.get(position).getCityName();
				String citycode = list.get(position).getCityCode() + "";
				intent.putExtra(getIntent().getStringExtra("tag"), selected);
				intent.putExtra(getIntent().getStringExtra("code"), citycode);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void initPostion(final List<JobClassEntity> list) {
		jobadapter = new CommonAdapter<JobClassEntity>(getApplicationContext(),
				R.layout.select_item_group, list) {
			@Override
			public void convert(ViewHolder viewHolder,
					JobClassEntity jobClassEntities) {
				viewHolder.setText(R.id.text_ku, jobClassEntities.getName());

			}
		};
		listView.setAdapter(jobadapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				String selected = list.get(position).getName();
				String code = list.get(position).getId() + "";
				intent.putExtra(getIntent().getStringExtra("code"), code);
				intent.putExtra(getIntent().getStringExtra("tag"), selected);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private void intSarayData(final List<DataconfigEntity> list) {
		sarayAdapter = new CommonAdapter<DataconfigEntity>(
				getApplicationContext(), R.layout.select_item_group, list) {
			@Override
			public void convert(ViewHolder viewHolder,
					DataconfigEntity salaryEntity) {
				viewHolder.setText(R.id.text_ku, salaryEntity.getValue());
			}
		};
		listView.setAdapter(sarayAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				String selected = list.get(position).getValue();
				String codeid = list.get(position).getCodeId() + "";
				intent.putExtra(getIntent().getStringExtra("tag"), selected);
				intent.putExtra(getIntent().getStringExtra("code"), codeid);
				if (getIntent().getIntExtra("requestCode", 0) == (RecruitmentTreasureActivity.SALARY)) {
					intent.putExtra("minimum", list.get(position).getMinimum());
					intent.putExtra("maximum", list.get(position).getMaximum());
				}
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_common_select;
	}

	@Override
	public void initView() {
		topTitle = (TextView) findViewById(R.id.center_txt_title);
		listView = (ListView) findViewById(R.id.list_view);

		int requestCode;
		requestCode = getIntent().getIntExtra("requestCode",
				MrrckResumeActivity.GENDER);
		switch (requestCode) {
		case MrrckResumeActivity.GENDER:
			topTitle.setText("性别");
			initDate(genderStr);
			break;
		case MrrckResumeActivity.JOB:
			topTitle.setText("现在岗位");
			break;
		case MrrckResumeActivity.HOME:
			topTitle.setText("现居地");
			List<AreaEntity> nowcitylist = new ArrayList<AreaEntity>();
			nowcitylist = MKDataBase.getInstance().getCity();
			initDate(nowcitylist);
			break;
		case MrrckResumeActivity.EDUCATION:
			topTitle.setText("最高学历");
			List<DataconfigEntity> xueli = MKDataBase.getInstance()
					.getDataByCode("EDUCATION");
			intSarayData(xueli);
			break;
		case MrrckResumeActivity.WORKTIME:
			topTitle.setText("美业职龄");
			List<DataconfigEntity> jobAgeList = MKDataBase.getInstance()
					.getDataByCode("JOB_AGE_REQUIRE");
			intSarayData(jobAgeList);
			// initDate(workTimeStr);
			break;
		case MrrckResumeActivity.MARRY:
			topTitle.setText("婚姻状况");
			initDate(marryStr1);
			break;
		case MrrckResumeActivity.MREQUIRESTAY:
			topTitle.setText("是否住宿");
			initDate(mrequireStayStr);
			break;
		case MrrckResumeActivity.MARRYSTARE:
			topTitle.setText("意向城市");
			List<AreaEntity> citylist = new ArrayList<AreaEntity>();
			citylist = MKDataBase.getInstance().getHotCity();
			initDate(citylist);

			break;
		case MrrckResumeActivity.INTENTPOS:
			topTitle.setText("求职意向");
			List<JobClassEntity> list = new ArrayList<JobClassEntity>();
			list = MKDataBase.getInstance().getJobClass();
			initPostion(list);

			break;
		case MrrckResumeActivity.EXPECTSALAYNUM:
			topTitle.setText("期望薪水");
			//
			List<DataconfigEntity> Salarylist = new ArrayList<DataconfigEntity>();
			Salarylist = MKDataBase.getInstance().getSalary();
			intSarayData(Salarylist);
			break;
		case MrrckResumeActivity.GETWORKAGE:
			topTitle.setText("工作年限");
			List<DataconfigEntity> work_yearlist = new ArrayList<DataconfigEntity>();
			work_yearlist = MKDataBase.getInstance().getWorkAge();
			intSarayData(work_yearlist);

			break;
		case MrrckResumeActivity.PROFESSION_KNOWLEDGE:
			topTitle.setText("专业技法");
			List<DataconfigEntity> knowledgelist = new ArrayList<DataconfigEntity>();
			knowledgelist = MKDataBase.getInstance().getWorkSkill();
			intSarayData(knowledgelist);

			break;
		case RecruitmentTreasureActivity.NUMBERNEEDS:
			topTitle.setText("需要人数");
			List<DataconfigEntity> numberNeedlist = new ArrayList<DataconfigEntity>();
			numberNeedlist = MKDataBase.getInstance().getNumberNeeds();
			// initDate(needNumbers);
			intSarayData(numberNeedlist);
			break;
		case RecruitmentTreasureActivity.SALARY:
			topTitle.setText("薪资待遇");
			List<DataconfigEntity> salarylist = new ArrayList<DataconfigEntity>();
			salarylist = MKDataBase.getInstance().getSalary();
			intSarayData(salarylist);

			break;
		case RecruitmentTreasureActivity.ACCOMMODATION:
			topTitle.setText("住宿");
			// List<DataconfigEntity> accommodationlist = new
			// ArrayList<DataconfigEntity>();
			// accommodationlist = MKDataBase.getInstance().getSalary();
			// intSarayData(accommodationlist);
			initDate(mrequireStayStr);

			break;
		case RecruitmentTreasureActivity.ISMARRY:
			topTitle.setText("婚姻状况");
			initDate(marryStr);
			break;
		case RecruitmentTreasureActivity.GENDER:
			topTitle.setText("性别");
			initDate(gender);
			break;
		case RecruitmentTreasureActivity.AGE:
			topTitle.setText("年龄");
			List<DataconfigEntity> agelist = new ArrayList<DataconfigEntity>();
			agelist = MKDataBase.getInstance().getAge();
			intSarayData(agelist);
			break;
		case RecruitmentTreasureActivity.EDUCATION:
			topTitle.setText("学历");
			List<DataconfigEntity> educationlist = new ArrayList<DataconfigEntity>();
			educationlist = MKDataBase.getInstance().getEdution();
			intSarayData(educationlist);
			break;
		case RecruitmentTreasureActivity.WORKINGEXPERIENCE:
			topTitle.setText("美业职龄");
			List<DataconfigEntity> Worklist = MKDataBase.getInstance()
					.getDataByCode("JOB_AGE_REQUIRE");
			intSarayData(Worklist);
			break;
		case RecruitmentTreasureActivity.PROFESSION_KNOWLEDGE:
			topTitle.setText("专业知识");
			List<DataconfigEntity> pofessionlist = new ArrayList<DataconfigEntity>();
			pofessionlist = MKDataBase.getInstance().getWorkSkill();
			intSarayData(pofessionlist);
			break;
		case RecruitmentTreasureActivity.LIFESKILL:
			topTitle.setText("生活特长");
			List<DataconfigEntity> lifeSkilllist = new ArrayList<DataconfigEntity>();
			lifeSkilllist = MKDataBase.getInstance().getLifeSkill();
			intSarayData(lifeSkilllist);
			break;
		case RecruitmentTreasureActivity.APPLICATIONTYPE:
			topTitle.setText("工作性质");
			List<DataconfigEntity> applicationlist = new ArrayList<DataconfigEntity>();
			applicationlist = MKDataBase.getInstance().getWorkType();
			intSarayData(applicationlist);
			break;
		case RecruitmentTreasureActivity.TIMEARRIVAL:
			topTitle.setText("到岗时间");
			List<DataconfigEntity> arrivalList = new ArrayList<DataconfigEntity>();
			arrivalList = MKDataBase.getInstance().getArriveETme();
			intSarayData(arrivalList);
			break;
		}
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
