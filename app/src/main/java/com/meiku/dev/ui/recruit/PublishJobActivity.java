package com.meiku.dev.ui.recruit;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.JobEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.CommonListSelectActivity;
import com.meiku.dev.ui.morefun.RecruitmentTreasureActivity;
import com.meiku.dev.ui.morefun.SelectPositionActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.HintDialogwk.ClickListenerInterface;

/**
 * 发布职位（编辑职位）
 * 
 */
public class PublishJobActivity extends BaseActivity implements OnClickListener {

	private EditText et_jobName, et_address, et_introduce, et_publishDays,
			et_phoneperson, et_phoneconnect;
	private TextView tv_jobType, tv_needNum, tv_salary, tv_fuli, tv_age,
			tv_xueli, tv_experience, tv_comeTime, tv_skill, tv_publishCity;

	private final int INTENTPOS = 1;
	private final int PUBLISHCITY = 2;
	private final int BENEFIT = 3;
	private final int SKILLS = 4;

	private int jobType = 1, gender = 0, isMarry = 0;
	private RadioGroup group_jobType, group_gender, group_marriage;
	private String positionId;
	private String needNum;
	private String salaryId;
	private String salaryValue;
	private String salaryFrom;
	private String salaryTo;
	private String fringeBenefitsId;
	private String fringeBenefitsName;
	private String age = "0";
	private String jobAge;
	private String arriveTime;
	private String education = "-1";
	private String knowledgeId;
	private String knowledge;
	private String delStatus = "0";
	private String jobCityCode;
	private String provinceCode;
	private String jobCityName;

	private boolean isDoEdit;// 是否为编辑页面
	private int jobId;
	private TextView center_txt_title;
	private Button btnOK;
	private HintDialogwk commonDialog;
	private boolean isWhole = false;// 是否是全国

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_publishjob;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		btnOK = (Button) findViewById(R.id.btnOK);

		et_jobName = (EditText) findViewById(R.id.et_jobName);
		et_phoneperson = (EditText) findViewById(R.id.et_phoneperson);
		et_phoneperson.setText(AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getContactName());
		et_phoneconnect = (EditText) findViewById(R.id.et_phoneconnect);
		et_phoneconnect.setText(AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getPhone());
		et_address = (EditText) findViewById(R.id.et_address);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		et_publishDays = (EditText) findViewById(R.id.et_publishDays);

		tv_jobType = (TextView) findViewById(R.id.tv_jobType);
		tv_needNum = (TextView) findViewById(R.id.tv_needNum);
		tv_salary = (TextView) findViewById(R.id.tv_salary);
		tv_fuli = (TextView) findViewById(R.id.tv_fuli);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_xueli = (TextView) findViewById(R.id.tv_xueli);
		tv_experience = (TextView) findViewById(R.id.tv_experience);
		tv_comeTime = (TextView) findViewById(R.id.tv_comeTime);
		tv_skill = (TextView) findViewById(R.id.tv_skill);
		tv_publishCity = (TextView) findViewById(R.id.tv_publishCity);

		// 性别选择
		group_jobType = (RadioGroup) findViewById(R.id.group_jobType);
		group_jobType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.rbtn_quanzhi:// 全职
					jobType = 1;
					break;
				case R.id.rbtn_jianzhi:// 兼职
					jobType = 2;
					break;
				case R.id.rbtn_shixi:// 实习
					jobType = 3;
					break;
				default:
					break;
				}
			}
		});

		// 性别选择
		group_gender = (RadioGroup) findViewById(R.id.group_gender);
		group_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.rbtn_man:// 男
					gender = 1;
					break;
				case R.id.rbtn_woman:// 女
					gender = 2;
					break;
				case R.id.rbtn_allGender:// 不限
					gender = 0;
					break;
				default:
					break;
				}
			}
		});

		// 选择是否结婚
		group_marriage = (RadioGroup) findViewById(R.id.group_marriage);
		group_marriage
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						switch (arg1) {
						case R.id.rbtn_notmarried:// 未婚
							isMarry = 2;
							break;
						case R.id.rbtn_married:// 已婚
							isMarry = 1;
							break;
						case R.id.rbtn_allMarry:// 不限
							isMarry = 0;
							break;
						default:
							break;
						}
					}
				});
	}

	@Override
	public void initValue() {
		isDoEdit = getIntent().getBooleanExtra("isDoEdit", false);
		initExitTipDialog();
		jobId = getIntent().getIntExtra("jobId", -1);
		if (isDoEdit) {
			getJobDetail();
			center_txt_title.setText("编辑职位");
			btnOK.setText("确定");
		}
	}

	/**
	 * 获取职位详情
	 */
	private void getJobDetail() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("jobId", jobId);
		LogUtil.e("hl", "职位详情请求=" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_JOB_DETAIL));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase, true);
	}

	@Override
	public void bindListener() {
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.layout_jobType).setOnClickListener(this);
		findViewById(R.id.layout_needNum).setOnClickListener(this);
		findViewById(R.id.layout_salary).setOnClickListener(this);
		findViewById(R.id.layout_fuli).setOnClickListener(this);
		findViewById(R.id.layout_age).setOnClickListener(this);
		findViewById(R.id.layout_xueli).setOnClickListener(this);
		findViewById(R.id.layout_experience).setOnClickListener(this);
		findViewById(R.id.layout_comeTime).setOnClickListener(this);
		findViewById(R.id.layout_skill).setOnClickListener(this);
		findViewById(R.id.layout_publishCity).setOnClickListener(this);
		btnOK.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "发布职位" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			sendBroadcast(new Intent(BroadCastAction.ACTION_PUBLIC_POSITION));
			finish();
			break;
		case reqCodeTwo:
			if ((resp.getBody().get("job") + "").length() > 2) {
				JobEntity jobEntity = (JobEntity) JsonUtil.jsonToObj(
						JobEntity.class, resp.getBody().get("job").toString());
				if (null != jobEntity) {
					// 联系人
					et_phoneperson.setText(jobEntity.getContactName());
					// 联系电话
					et_phoneconnect.setText(jobEntity.getContactPhone());
					// 职位名称
					et_jobName.setText(jobEntity.getJobName());
					// 职位类别
					positionId = jobEntity.getPositionId() + "";
					tv_jobType.setText(MKDataBase.getInstance().getJobNameById(
							jobEntity.getPositionId()));

					// 工作性质
					String thisJobType = jobEntity.getJobType();// 1：全职，2：兼职，3：实习
					if ("3".equals(thisJobType)) {
						group_jobType.check(R.id.rbtn_shixi);
					} else if ("2".equals(thisJobType)) {
						group_jobType.check(R.id.rbtn_jianzhi);
					} else {
						group_jobType.check(R.id.rbtn_quanzhi);
					}
					// 需要人数
					needNum = jobEntity.getNeedNum();
					tv_needNum.setText(needNum);
					// 薪资待遇
					salaryValue = jobEntity.getSalaryValue();
					salaryFrom = jobEntity.getSalaryFrom() + "";
					salaryTo = jobEntity.getSalaryTo() + "";
					salaryId = jobEntity.getSalaryId() + "";
					tv_salary.setText(salaryValue);
					// 福利待遇
					fringeBenefitsId = jobEntity.getFringeBenefitsId();
					fringeBenefitsName = jobEntity.getFringeBenefitsName();
					tv_fuli.setText(fringeBenefitsName);
					// 上班地址
					et_address.setText(jobEntity.getWorkAddress());
					// 性别
					String gender = jobEntity.getGender();// 0：不限，1：限男性，2：限女性
					if ("2".equals(gender)) {
						group_gender.check(R.id.rbtn_woman);
					} else if ("1".equals(gender)) {
						group_gender.check(R.id.rbtn_man);
					} else {
						group_gender.check(R.id.rbtn_allGender);
					}
					// 婚姻状况
					String marriage = jobEntity.getIsMarry();// 0：不限，1：已婚，2：未婚
					if ("2".equals(marriage)) {
						group_marriage.check(R.id.rbtn_notmarried);
					} else if ("1".equals(marriage)) {
						group_marriage.check(R.id.rbtn_married);
					} else {
						group_marriage.check(R.id.rbtn_allMarry);
					}
					// 年龄
					tv_age.setText(jobEntity.getAgeValue());
					age = jobEntity.getAge() + "";
					// 学历
					education = jobEntity.getEducation() + "";
					tv_xueli.setText(MKDataBase.getInstance().getValueByCodeId(
							education));
					// 工作经验
					jobAge = jobEntity.getJobAge() + "";
					tv_experience.setText(MKDataBase.getInstance()
							.getValueByCodeId(jobAge));
					// 职位描述
					et_introduce.setText(jobEntity.getJobIntroduce());
					// 到岗时间
					arriveTime = jobEntity.getArriveTime() + "";
					tv_comeTime.setText(MKDataBase.getInstance()
							.getValueByCodeId(arriveTime));
					// 专业技能
					knowledge = jobEntity.getKnowledge();
					knowledgeId = jobEntity.getKnowledgeId();
					tv_skill.setText(knowledge);
					// 发布城市
					jobCityCode = jobEntity.getJobCityCode();
					jobCityName = jobEntity.getJobCityName();
					tv_publishCity.setText(jobCityName);
					if (tv_publishCity.getLineCount() > 1) {
						tv_publishCity.setGravity(Gravity.LEFT
								| Gravity.CENTER_VERTICAL);
					} else {
						tv_publishCity.setGravity(Gravity.RIGHT
								| Gravity.CENTER_VERTICAL);
					}
					// 发布有效时间
					et_publishDays.setText(jobEntity.getValidDay() + "");
				}
			}
			break;
		case reqCodeThree:
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			sendBroadcast(new Intent(BroadCastAction.ACTION_PUBLIC_POSITION));
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (arg0 != null) {
			ReqBase resp = (ReqBase) arg0;
			switch (requestCode) {
			case reqCodeOne:
				if (resp != null && resp.getHeader() != null
						&& !Tool.isEmpty(resp.getHeader().getRetMessage())) {
					ToastUtil.showShortToast(resp.getHeader().getRetMessage());
				} else {
					ToastUtil.showShortToast("发布失败！");
				}
				break;
			case reqCodeTwo:
				if (resp != null && resp.getHeader() != null
						&& !Tool.isEmpty(resp.getHeader().getRetMessage())) {
					ToastUtil.showShortToast(resp.getHeader().getRetMessage());
				} else {
					ToastUtil.showShortToast("获取职位详情失败！");
				}
				finish();
				break;
			case reqCodeThree:
				if (resp != null && resp.getHeader() != null
						&& !Tool.isEmpty(resp.getHeader().getRetMessage())) {
					ToastUtil.showShortToast(resp.getHeader().getRetMessage());
				} else {
					ToastUtil.showShortToast("职位编辑失败！");
				}
				finish();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.goback:// 返回
			showTipDialog(true);
			break;
		case R.id.layout_jobType:// 职位
			startActivityForResult(new Intent(this,
					SelectPositionActivity.class).putExtra("hasJobFlag", true),
					INTENTPOS);
			break;
		case R.id.layout_needNum:// 需要人数
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "numberneeds");
			intent.putExtra("code", "numberneedsCode");

			intent.putExtra("requestCode",
					RecruitmentTreasureActivity.NUMBERNEEDS);
			startActivityForResult(intent,
					RecruitmentTreasureActivity.NUMBERNEEDS);
			break;
		case R.id.layout_salary:// 薪水
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "ExpectSalayET");
			intent.putExtra("code", "Salaycode");
			intent.putExtra("requestCode", RecruitmentTreasureActivity.SALARY);
			startActivityForResult(intent, RecruitmentTreasureActivity.SALARY);
			break;
		case R.id.layout_fuli:// 福利待遇
			intent = new Intent(this, MultiSelectActivity.class);
			intent.putExtra("type", MultiSelectActivity.TYPE_BENIFIT);
			intent.putExtra("ids", fringeBenefitsId);
			startActivityForResult(intent, BENEFIT);
			break;
		case R.id.layout_age:// 要求年龄
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "age");
			intent.putExtra("code", "ageCode");
			intent.putExtra("requestCode", RecruitmentTreasureActivity.AGE);
			startActivityForResult(intent, RecruitmentTreasureActivity.AGE);
			break;
		case R.id.layout_xueli:// 学历
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "EDUCATION");
			intent.putExtra("code", "EDUCATIONCODE");
			intent.putExtra("requestCode",
					RecruitmentTreasureActivity.EDUCATION);
			startActivityForResult(intent,
					RecruitmentTreasureActivity.EDUCATION);
			break;
		case R.id.layout_experience:// 工作经验
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "experience");
			intent.putExtra("code", "experienceCode");
			intent.putExtra("requestCode",
					RecruitmentTreasureActivity.WORKINGEXPERIENCE);
			startActivityForResult(intent,
					RecruitmentTreasureActivity.WORKINGEXPERIENCE);
			break;
		case R.id.layout_comeTime:// 到岗时间
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "arrival");
			intent.putExtra("code", "arrivalCode");
			intent.putExtra("requestCode",
					RecruitmentTreasureActivity.TIMEARRIVAL);
			startActivityForResult(intent,
					RecruitmentTreasureActivity.TIMEARRIVAL);
			break;
		case R.id.layout_skill:// 专业技法
			intent = new Intent(this, MultiSelectActivity.class);
			intent.putExtra("type", MultiSelectActivity.TYPE_KNOWLEDGE);
			intent.putExtra("ids", knowledgeId);
			startActivityForResult(intent, SKILLS);
			break;
		case R.id.layout_publishCity:
			// startActivityForResult(
			// new Intent(this, SelectCityActivity.class).putExtra(
			// "isMultiSelect", true), PUBLISHCITY);
			startActivityForResult(new Intent(this,
					SelectCityPositionActivity.class), PUBLISHCITY);
			break;
		case R.id.btnOK:
			if (checkDataIsReady()) {

				if (isDoEdit) {
					doEditJobUpLoad();
				} else {
					doPublishJob();
				}
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 发布职位
	 */
	@SuppressWarnings("static-access")
	private void doPublishJob() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("jobName", et_jobName.getText().toString());
		map.put("contactName", et_phoneperson.getText().toString());
		map.put("contactPhone", et_phoneconnect.getText().toString());
		map.put("positionId", positionId);
		map.put("jobType", jobType);
		map.put("needNum", needNum);
		map.put("salaryId", salaryId);
		map.put("salaryValue", salaryValue);
		map.put("salaryFrom", salaryFrom);
		map.put("salaryTo", salaryTo);
		map.put("fringeBenefitsId", fringeBenefitsId);
		map.put("fringeBenefitsName", fringeBenefitsName);
		map.put("workAddress", et_address.getText().toString());
		map.put("gender", gender);
		map.put("isMarry", isMarry);
		map.put("age", age);
		map.put("cityCode", "-1");
		map.put("education", education);
		map.put("jobAge", jobAge);
		map.put("jobIntroduce", et_introduce.getText().toString());
		map.put("arriveTime", arriveTime);
		map.put("knowledgeId", knowledgeId);
		map.put("knowledge", knowledge);
		map.put("longitude", MrrckApplication.getInstance().longitude == 0 ? -1
				: MrrckApplication.getInstance().longitude);
		map.put("latitude", MrrckApplication.getInstance().laitude == 0 ? -1
				: MrrckApplication.getInstance().laitude);
		if (isWhole) {
			map.put("wholeCode", 100000);// 全国,固定值100000
		} else {
			map.put("wholeCode", "");// 不是全国
		}
		map.put("provinceCode", provinceCode);
		map.put("jobCityCode", jobCityCode);
		map.put("jobCityName", jobCityName);
		map.put("validDay", et_publishDays.getText().toString());
		map.put("delStatus", delStatus);
		LogUtil.d("hl", "发布职位请求=" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PUBLISH_NEWJOB));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase, true);
	}

	/**
	 * 编辑职位
	 */
	private void doEditJobUpLoad() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("jobId", jobId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("jobName", et_jobName.getText().toString());
		map.put("contactName", et_phoneperson.getText().toString());
		map.put("contactPhone", et_phoneconnect.getText().toString());
		map.put("positionId", positionId);
		map.put("jobType", jobType);
		map.put("needNum", needNum);
		map.put("salaryId", salaryId);
		map.put("salaryValue", salaryValue);
		map.put("salaryFrom", salaryFrom);
		map.put("salaryTo", salaryTo);
		map.put("fringeBenefitsId", fringeBenefitsId);
		map.put("fringeBenefitsName", fringeBenefitsName);
		map.put("workAddress", et_address.getText().toString());
		map.put("gender", gender);
		map.put("isMarry", isMarry);
		map.put("age", age);
		map.put("cityCode", "-1");
		map.put("education", education);
		map.put("jobAge", jobAge);
		map.put("jobIntroduce", et_introduce.getText().toString());
		map.put("arriveTime", arriveTime);
		map.put("knowledgeId", knowledgeId);
		map.put("knowledge", knowledge);
		map.put("longitude", MrrckApplication.getInstance().longitude == 0 ? -1
				: MrrckApplication.getInstance().longitude);
		map.put("latitude", MrrckApplication.getInstance().laitude == 0 ? -1
				: MrrckApplication.getInstance().laitude);
		if (isWhole) {
			map.put("wholeCode", 100000);// 全国,固定值100000
		} else {
			map.put("wholeCode", "");// 不是全国
		}
		map.put("provinceCode", provinceCode);
		map.put("jobCityCode", jobCityCode);
		map.put("jobCityName", jobCityName);
		map.put("validDay", et_publishDays.getText().toString());
		map.put("delStatus", delStatus);
		LogUtil.d("hl", "编辑职位请求=" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_EDITJOB));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase, true);

	}

	private boolean checkDataIsReady() {
		if (Tool.isEmpty(et_jobName.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写职位名称，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(tv_jobType.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择职位类，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(tv_needNum.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择需要人数，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(tv_salary.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择薪资待遇，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(tv_fuli.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择福利待遇，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(et_address.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写上班地址，让招聘更容易！");
			return false;
		}
		// if (Tool.isEmpty(tv_age.getText().toString().trim())) {
		// ToastUtil.showShortToast("年龄未选择！");
		// return false;
		// }
		// if (Tool.isEmpty(tv_xueli.getText().toString().trim())) {
		// ToastUtil.showShortToast("学历未选择！");
		// return false;
		// }
		if (Tool.isEmpty(tv_experience.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择工作经验，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(et_introduce.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写职位描，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(tv_comeTime.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择到岗时间，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(tv_skill.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择专业技能，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(tv_publishCity.getText().toString().trim())) {
			ToastUtil.showShortToast("请选择发布城市，让招聘更容易！");
			return false;
		}
		if (Tool.isEmpty(et_publishDays.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写发布有效时间，让招聘更容易！");
			return false;
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case RecruitmentTreasureActivity.EDUCATION:// 学历
				education = data.getStringExtra("EDUCATIONCODE");
				tv_xueli.setText(data.getStringExtra("EDUCATION"));
				break;
			case RecruitmentTreasureActivity.NUMBERNEEDS:// 需要人数
				needNum = data.getStringExtra("numberneeds");
				tv_needNum.setText(needNum);
				break;
			case INTENTPOS:// 职位
				String intent_value = data.getStringExtra("name");
				tv_jobType.setText(intent_value);
				positionId = data.getIntExtra("id", 0) + "";
				break;
			case RecruitmentTreasureActivity.SALARY:// 薪水
				salaryValue = data.getStringExtra("ExpectSalayET");
				tv_salary.setText(salaryValue);
				salaryId = data.getStringExtra("Salaycode");
				salaryFrom = data.getStringExtra("minimum");
				salaryTo = data.getStringExtra("maximum");
				break;
			case RecruitmentTreasureActivity.AGE:// 工作年限
				tv_age.setText(data.getStringExtra("age"));
				age = data.getStringExtra("ageCode");
				break;
			case RecruitmentTreasureActivity.WORKINGEXPERIENCE:// 工作经验
				tv_experience.setText(data.getStringExtra("experience"));
				jobAge = data.getStringExtra("experienceCode");
				break;
			case RecruitmentTreasureActivity.TIMEARRIVAL:// 到岗时间
				tv_comeTime.setText(data.getStringExtra("arrival"));
				arriveTime = data.getStringExtra("arrivalCode");
				break;
			case PUBLISHCITY:
				provinceCode = data.getStringExtra("provinceCode");
				jobCityName = data.getStringExtra("cityName");
				isWhole = data.getBooleanExtra("isWhole", false);
				// countryString = data.getStringExtra("country");
				if (isWhole) {
					tv_publishCity.setText("全国");
					jobCityName = "全国";
				} else {
					tv_publishCity.setText(jobCityName);
				}

				if (tv_publishCity.getLineCount() > 1) {
					tv_publishCity.setGravity(Gravity.LEFT
							| Gravity.CENTER_VERTICAL);
				} else {
					tv_publishCity.setGravity(Gravity.RIGHT
							| Gravity.CENTER_VERTICAL);
				}
				jobCityCode = data.getStringExtra("cityCode");
				break;
			case BENEFIT:
				fringeBenefitsName = data.getStringExtra("selectValues");
				fringeBenefitsId = data.getStringExtra("selectIds");
				tv_fuli.setText(fringeBenefitsName);
				break;
			case SKILLS:
				knowledge = data.getStringExtra("selectValues");
				knowledgeId = data.getStringExtra("selectIds");
				tv_skill.setText(knowledge);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showTipDialog(true);
		}
		return false;
	}

	private void showTipDialog(boolean show) {
		if (show) {
			if (commonDialog != null && !commonDialog.isShowing()) {
				commonDialog.show();
			}
		} else {
			if (commonDialog != null && commonDialog.isShowing()) {
				commonDialog.dismiss();
			}
		}
	}

	private void initExitTipDialog() {
		commonDialog = new HintDialogwk(PublishJobActivity.this, "已填写这么多，要忍心放弃"
				+ (isDoEdit ? "编辑" : "发布") + "吗?", "确定", "取消");
		commonDialog.setClicklistener(new ClickListenerInterface() {

			@Override
			public void doConfirm() {
				finish();
				showTipDialog(false);
			}

			@Override
			public void doCancel() {
				showTipDialog(false);
			}
		});
	}

}
