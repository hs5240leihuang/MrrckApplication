package com.meiku.dev.ui.morefun;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.CompanyResumeData;
import com.meiku.dev.bean.CompanyResumeData.CompanyResumeReq;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.RecruitmentBean;
import com.meiku.dev.bean.RecruitmentInfo;
import com.meiku.dev.bean.RecruitmentInfo.RecruitmentReq;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.TimeSelectDialog.CallBackListener;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;

/**
 * Created by Administrator on 2015/8/24. 发布职位
 */
public class RecruitmentTreasureActivity extends BaseActivity implements
		View.OnClickListener, CompoundButton.OnCheckedChangeListener {
	public static final int JOBNAME = 30;// 职位名称
	public static final int APPLICATIONTYPE = 31;// 应聘类型
	public static final int NUMBERNEEDS = 32;// 需要人数
	public static final int SALARY = 33;// 薪资待遇
	public static final int ACCOMMODATION = 34;// 住宿
	public static final int ISMARRY = 35;// 是否结婚
	public static final int MARRYSTARE = 12;// 上班地址
	public static final int GENDER = 36;// 性别
	public static final int AGE = 37;// 年龄
	public static final int EDUCATION = 38;// 学历
	public static final int WORKINGEXPERIENCE = 39;// 学历
	public static final int TIMEARRIVAL = 40;// 到岗时间
	public static final int PROFESSION_KNOWLEDGE = 41;// 专业知识
	public static final int JOBDESCRIPTION = 43;// 职位描述
	public static final int LIFESKILL = 44;// 生活特长
	public static final int TAKE_SOUND = 9; // 录制音频
	private String soundPath = null;// 录音路径
	private String recordingTime = "0";// 录音时间
	private String delStatus = "0";// 是否发布 默认为0发布 2是不发布

	public static final int REQCODE_EDITJOBINFO = 10001;// 编辑职位信息
	public static final int REQCODE_PUBLISHJOBINFO = 10002;// 发布职位信息
	public static final int REQCODE_UPLOADMKMEDIA = 1003;
	/**
	 * 职位信息
	 */
	private LinearLayout position_information;
	private String positionId;
	private String jobTypeName;

	/**
	 * 职位要求
	 */
	private LinearLayout job_requirements;
	/**
	 * 招聘宝最下面的三个布局
	 */
	private LinearLayout job_other;
	/**
	 * 职位名称
	 */
	private EditText jobNameET;
	private String jobNamegroupId;
	private String jobNameId;
	private String jobType;

	/**
	 * 联系人
	 */
	private EditText contactName;

	/**
	 * 联系人电话
	 */
	private EditText contactPhone;
	/**
	 * 应聘类型
	 */
	private TextView application_type;
	/**
	 * 职位类别
	 */
	private TextView positionName;
	/**
	 * 工作城市
	 */
	private TextView work_city;
	private String cityCod;
	/**
	 * 需要人数
	 */
	private TextView number_of_needs;
	String numberneeds;// 需要人数
	String numberNeedscode;
	/**
	 * 薪资待遇
	 */
	private TextView salary_text;
	String salary;
	String salarycode;
	String maximum;
	String minimum;
	/**
	 * 住宿
	 */
	private TextView get_accommodation;
	int accommodation;
	/**
	 * 上班地址
	 */
	private TextView work_address;
	String workaddressCode;
	/**
	 * 婚姻状况
	 */
	private TextView marital_status;
	private Integer isMarryPostion;
	/**
	 * 性别
	 */
	private TextView gender_text;
	private Integer genderpostion;
	/**
	 * 年龄
	 */
	private TextView age_text;
	private Integer ageCode;
	/**
	 * 学历
	 */
	private TextView education_text;
	private Integer educationCode;
	/**
	 * 美业职龄
	 */
	private TextView working_experience;
	private Integer experienceCode;
	/**
	 * 到岗时间
	 */
	private TextView time_of_arrival;
	private Integer arrivalCode;
	/**
	 * 专业知识
	 */
	private LinearLayout professional_knowledge;
	private TextView knowledge_text;
	int knowledgeCode;
	private TextView time_text;

	/**
	 * 确定
	 */
	private TextView sure_commit;
	private String jobId = null;

	private RecruitmentInfo.RecruitmentReq bean;
	/**
	 * 职位描述
	 */
	private TextView job_description;
	/**
	 * 生活特长
	 */
	// private TextView life_skills;
	// private String lifeSkillId;
	private TextView commit;
	private TextView companyJobTextView;
	private LinearLayout voice_description;// 语音描述
	private ToggleButton isPublic;

	@Override
	public void initView() {
		commit = (TextView) findViewById(R.id.right_txt_title);
		voice_description = (LinearLayout) findViewById(R.id.voice_description);
		position_information = (LinearLayout) findViewById(R.id.position_information_id);
		job_requirements = (LinearLayout) findViewById(R.id.job_requirements_id);
		jobNameET = (EditText) findViewById(R.id.jobNameET);
		positionName = (TextView) findViewById(R.id.positionName);
		work_city = (TextView) findViewById(R.id.work_city);
		job_description = (TextView) findViewById(R.id.job_description);
		application_type = (TextView) findViewById(R.id.application_type);
		number_of_needs = (TextView) findViewById(R.id.number_of_needs);
		salary_text = (TextView) findViewById(R.id.salary_text);
		get_accommodation = (TextView) findViewById(R.id.get_accommodation);
		work_address = (TextView) findViewById(R.id.work_address);
		marital_status = (TextView) findViewById(R.id.marital_status);
		gender_text = (TextView) findViewById(R.id.gender_text);
		age_text = (TextView) findViewById(R.id.age_text);
		education_text = (TextView) findViewById(R.id.education_text);
		working_experience = (TextView) findViewById(R.id.working_experience);
		time_of_arrival = (TextView) findViewById(R.id.time_of_arrival);
		professional_knowledge = (LinearLayout) findViewById(R.id.professional_knowledge);
		knowledge_text = (TextView) findViewById(R.id.knowledge_text);
		time_text = (TextView) findViewById(R.id.time_text);
		sure_commit = (TextView) findViewById(R.id.sure_commit);// 确定
		// life_skills=(TextView)findViewById(R.id.life_skills);
		companyJobTextView = (TextView) findViewById(R.id.company_job);
		isPublic = (ToggleButton) findViewById(R.id.JP_setting_toggleButton);
		contactName = (EditText) findViewById(R.id.contactName);
		contactPhone = (EditText) findViewById(R.id.contactPhone);
	}

	private void initListener() {
		// jobNameET.setOnClickListener(this);
		voice_description.setOnClickListener(this);
		commit.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		application_type.setOnClickListener(this);
		positionName.setOnClickListener(this);
		work_city.setOnClickListener(this);
		job_description.setOnClickListener(this);
		number_of_needs.setOnClickListener(this);
		salary_text.setOnClickListener(this);
		get_accommodation.setOnClickListener(this);
		work_address.setOnClickListener(this);
		marital_status.setOnClickListener(this);
		// life_skills.setOnClickListener(this);
		gender_text.setOnClickListener(this);
		age_text.setOnClickListener(this);
		education_text.setOnClickListener(this);
		working_experience.setOnClickListener(this);
		time_of_arrival.setOnClickListener(this);
		professional_knowledge.setOnClickListener(this);
		time_text.setOnClickListener(this);
		sure_commit.setOnClickListener(this);
		isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton,
					boolean b) {
				if (b) {
					delStatus = "0";
				} else if (!b) {
					delStatus = "2";
				}
			}
		});
	}

	private void initData() {
		// 获取接口信息
		if (null != jobId) {
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", AppContext.getInstance().getUserInfo().getId());
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
			map.put("jobId", jobId);
			req.setHeader(new ReqHead(AppConfig.BUSINESS_JOB_DETAIL));
			req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
			httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req);
		}

		// 获取公司信息
		ReqBase req2 = new ReqBase();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("userId", AppContext.getInstance().getUserInfo().getId());
		req2.setHeader(new ReqHead(AppConfig.BUSINESS_EMPLOY_GET_COMPANY));
		req2.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map2)));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, req2);
	}

	private void getData(RecruitmentInfo.RecruitmentReq recruitment) {
		RecruitmentInfo recruitmentInfo = recruitment.job;
		jobNameET.setText(recruitmentInfo.getJobName());
		positionName.setText(recruitmentInfo.getPositionName());
		application_type.setText(recruitmentInfo.getJobTypeName());
		number_of_needs.setText(recruitmentInfo.getNeedNum());
		numberNeedscode = recruitmentInfo.getNeedNum();
		work_city.setText(recruitmentInfo.getCityName());

		salary_text.setText(recruitmentInfo.getSalaryValue());
		salary = recruitmentInfo.getSalaryValue();
		salarycode = recruitmentInfo.getSalaryId();
		get_accommodation.setText(recruitmentInfo.getHouseSupportName());
		accommodation = Integer.parseInt(recruitmentInfo.getHouseSupport());
		work_address.setText(recruitmentInfo.getWorkAddress());
		marital_status.setText(recruitmentInfo.getIsMarryName());
		isMarryPostion = Integer.parseInt(recruitmentInfo.getIsMarry());
		gender_text.setText(recruitmentInfo.getGenderName());
		genderpostion = Integer.parseInt(recruitmentInfo.getGender());
		age_text.setText(recruitmentInfo.getAgeValue());
		ageCode = Integer.parseInt(recruitmentInfo.getAge());

		education_text.setText(recruitmentInfo.getEducationValue());
		working_experience.setText(recruitmentInfo.getJobAgeValue());
		experienceCode = Integer.parseInt(recruitmentInfo.getJobAge());
		time_of_arrival.setText(recruitmentInfo.getArriveTimeName());
		arrivalCode = Integer.parseInt(recruitmentInfo.getArriveTime());
		knowledge_text.setText(recruitmentInfo.getKnowledge());
		// life_skills.setText(recruitmentInfo.getGoodAt());
		time_text.setText(recruitmentInfo.getCreateDate());
		job_description.setText(recruitmentInfo.getJobIntroduce());
		positionId = recruitmentInfo.getPositionId();
		jobType = recruitmentInfo.getJobType();
		workaddressCode = recruitmentInfo.getCityCode();
		knowledgeCode = Integer.parseInt(recruitmentInfo.getKnowledgeId());
		educationCode = Integer.parseInt(recruitmentInfo.getEducation());
		cityCod = recruitmentInfo.getCityCode();

		// lifeSkillId=recruitmentInfo.getGoodAtId();
		soundPath = recruitmentInfo.getVoice();
		recordingTime = recruitmentInfo.getVoiceSeconds();
		contactName.setText(recruitmentInfo.getContactName().toString());
		contactPhone.setText(recruitmentInfo.getContactPhone().toString());

		maximum = recruitmentInfo.getSalaryTo();
		minimum = recruitmentInfo.getSalaryFrom();

		if (!TextUtils.isEmpty(recruitmentInfo.getDelStatus())) {
			delStatus = recruitmentInfo.getDelStatus();
			if (delStatus.equals("0")) {
				isPublic.setChecked(true);
			} else {
				isPublic.setChecked(false);
			}
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		/**
		 * 职位名称
		 */
		case R.id.positionName:
			startActivityForResult(new Intent(this,
					SelectPositionActivity.class).putExtra("hasJobFlag", true),
					JOBNAME);
			break;
		/**
		 * 应聘类型
		 */
		case R.id.application_type:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "application");
			intent.putExtra("code", "applicationCode");

			intent.putExtra("requestCode", APPLICATIONTYPE);
			startActivityForResult(intent, APPLICATIONTYPE);
			break;
		/**
		 * 工作城市
		 */
		case R.id.work_city:
			new WheelSelectCityDialog(RecruitmentTreasureActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							work_city.setText(cityName);
							cityCod = cityCode + "";
						}

					}).show();
			break;
		/**
		 * 需要人数
		 */
		case R.id.number_of_needs:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "numberneeds");
			intent.putExtra("code", "numberneedsCode");

			intent.putExtra("requestCode", NUMBERNEEDS);
			startActivityForResult(intent, NUMBERNEEDS);
			break;
		/**
		 * 薪资待遇
		 */
		case R.id.salary_text:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "salary");
			intent.putExtra("code", "salaryCode");
			intent.putExtra("requestCode", SALARY);
			startActivityForResult(intent, SALARY);
			break;
		/**
		 * 住宿
		 */
		case R.id.get_accommodation:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "requireStay");
			intent.putExtra("postion", "accommodation");
			intent.putExtra("requestCode", ACCOMMODATION);
			startActivityForResult(intent, ACCOMMODATION);
			break;
		/**
		 * 工作地址
		 */
		case R.id.work_address:
			// startActivityForResult(new Intent(this,
			// SelectCityActivity.class), MARRYSTARE);
			intent = new Intent(this, CommonDescriptionActivity.class);
			intent.putExtra("tag", "workaddress");
			intent.putExtra("requestCode", MARRYSTARE);
			startActivityForResult(intent, MARRYSTARE);
			break;
		/**
		 * 是否结婚
		 */
		case R.id.marital_status:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "isMarry");
			intent.putExtra("postion", "isMarryPostion");
			intent.putExtra("requestCode", ISMARRY);
			startActivityForResult(intent, ISMARRY);
			break;
		/**
		 * 性别
		 */
		case R.id.gender_text:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "gender");
			intent.putExtra("postion", "genderPostion");
			intent.putExtra("requestCode", GENDER);
			startActivityForResult(intent, GENDER);
			break;
		/**
		 * 年龄
		 */
		case R.id.age_text:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "age");
			intent.putExtra("code", "ageCode");
			intent.putExtra("requestCode", AGE);
			startActivityForResult(intent, AGE);
			break;
		/**
		 * 学历
		 */
		case R.id.education_text:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "education");
			intent.putExtra("code", "educationCode");
			intent.putExtra("requestCode", EDUCATION);
			startActivityForResult(intent, EDUCATION);
			break;
		/**
		 * 美业职龄
		 */
		case R.id.working_experience:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "experience");
			intent.putExtra("code", "experienceCode");
			intent.putExtra("requestCode", WORKINGEXPERIENCE);
			startActivityForResult(intent, WORKINGEXPERIENCE);
			break;
		/**
		 * 到岗时间
		 */
		case R.id.time_of_arrival:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "arrival");
			intent.putExtra("code", "arrivalCode");
			intent.putExtra("requestCode", TIMEARRIVAL);
			startActivityForResult(intent, TIMEARRIVAL);
			break;
		/**
		 * 专业知识
		 */
		case R.id.professional_knowledge:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "knowledge");
			intent.putExtra("code", "knowledgeCode");
			intent.putExtra("requestCode", PROFESSION_KNOWLEDGE);
			startActivityForResult(intent, PROFESSION_KNOWLEDGE);
			break;
		/**
		 * 刷新时间
		 */
		case R.id.time_text:
			new TimeSelectDialog(this, new CallBackListener() {

				@Override
				public void CallBackOfTimeString(String time) {
					time_text.setText(time);
				}
			}).show();
			break;
		/**
		 * 职位描述
		 */
		case R.id.job_description:
			intent = new Intent(this, CommonDescriptionActivity.class);
			intent.putExtra("tag", "jobdescription");
			intent.putExtra("requestCode", JOBDESCRIPTION);
			startActivityForResult(intent, JOBDESCRIPTION);
			break;
		/**
		 * 生活特长 9.24 bug1402 去掉
		 */
		// case R.id.life_skills:
		// intent = new Intent(this, CommonListSelectActivity.class);
		// intent.putExtra("tag", "lifeskills");
		// intent.putExtra("code", "lifeskillsCode");
		// intent.putExtra("requestCode", LIFESKILL);
		// startActivityForResult(intent, LIFESKILL);
		// break;
		case R.id.goback:
			finish();
			break;
		case R.id.voice_description:
			// 如果播放路径不为空直接播放 否则录音上传在播放
			if (null != soundPath) {
				intent = new Intent(this, SelectVoiceActivity.class);
				intent.putExtra("filePath", soundPath);
				intent.putExtra("recordingTime", recordingTime + "");
				intent.putExtra(RecordActivity.BUNDLE_IS_PUBLISH, "1");
				startActivityForResult(intent, TAKE_SOUND);
			} else {
				intent = new Intent(this, RecordActivity.class);
				intent.putExtra(RecordActivity.BUNDLE_IS_PUBLISH, "1");
				startActivityForResult(intent, TAKE_SOUND);
			}

			break;
		/**
		 * 确定提交
		 */
		case R.id.right_txt_title:
			if (isDateLegal()) {
				final RecruitmentBean recruitmentBean = new RecruitmentBean();
				String voicePath;
				if (null != soundPath) {
					voicePath = soundPath;
				} else {
					voicePath = "";
				}
				recruitmentBean.setUserId(AppContext.getInstance()
						.getUserInfo().getId()
						+ "");
				recruitmentBean.setCompanyId(AppContext.getInstance()
						.getUserInfo().getCompanyEntity().getId()
						+ "");
				recruitmentBean.setPositionId(positionId);
				recruitmentBean.setJobName(jobNameET.getText().toString());
				recruitmentBean.setJobType(jobType);
				recruitmentBean.setIsMarry(isMarryPostion + "");
				recruitmentBean.setCityCode(cityCod);
				recruitmentBean.setSalaryId(salarycode);
				recruitmentBean
						.setSalaryValue(salary_text.getText().toString());

				recruitmentBean.setSalaryFrom(minimum);
				recruitmentBean.setSalaryTo(maximum);

				recruitmentBean.setHouseSupport(accommodation + "");
				recruitmentBean.setJobIntroduce(job_description.getText()
						.toString());
				recruitmentBean.setGender(genderpostion + "");
				recruitmentBean.setAge(ageCode + "");
				recruitmentBean.setEducation(educationCode + "");
				recruitmentBean.setJobAge(experienceCode + "");// codeId
				recruitmentBean
						.setNeedNum(number_of_needs.getText().toString());
				recruitmentBean.setKnowledgeId(knowledgeCode + "");
				recruitmentBean.setKnowledge(knowledge_text.getText()
						.toString());

				// recruitmentBean.setGoodAtId(lifeSkillId);
				// recruitmentBean.setGoodAt(life_skills.getText().toString());
				recruitmentBean.setGoodAtId("");
				recruitmentBean.setGoodAt("");

				recruitmentBean.setWorkAddress(work_address.getText()
						.toString());
				recruitmentBean.setArriveTime(arrivalCode + "");
				recruitmentBean.setLongitude(MrrckApplication.getInstance()
						.getLongitudeStr());
				recruitmentBean.setLatitude(MrrckApplication.getInstance()
						.getLaitudeStr());
				recruitmentBean.setVoiceSeconds(recordingTime);

				recruitmentBean
						.setContactName(contactName.getText().toString());
				recruitmentBean.setContactPhone(contactPhone.getText()
						.toString());
				recruitmentBean.setResumeEmail("");
				recruitmentBean.setEndDate("");
				recruitmentBean.setDelStatus(delStatus);

				if (null != jobId) {
					recruitmentBean.setJobId(jobId);
					ReqBase reqBase = new ReqBase();
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSINESS_MODIFY_JOBINFO));
					Map<String, Object> body = new HashMap<String, Object>();
					body.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					body.put("jobId", recruitmentBean.getJobId());
					body.put("companyId", recruitmentBean.getCompanyId());
					body.put("positionId", recruitmentBean.getPositionId());
					body.put("jobName", recruitmentBean.getJobName());
					body.put("jobType", recruitmentBean.getJobType());
					body.put("isMarry", recruitmentBean.getIsMarry());
					body.put("cityCode", recruitmentBean.getCityCode());
					body.put("salaryId", recruitmentBean.getSalaryId());
					body.put("salaryValue", recruitmentBean.getSalaryValue());
					body.put("salaryFrom", recruitmentBean.getSalaryFrom());
					body.put("salaryTo", recruitmentBean.getSalaryTo());
					body.put("houseSupport", recruitmentBean.getHouseSupport());
					body.put("jobIntroduce", recruitmentBean.getJobIntroduce());
					body.put("gender", recruitmentBean.getGender());
					body.put("age", recruitmentBean.getAge());
					body.put("education", recruitmentBean.getEducation());
					body.put("jobAge", recruitmentBean.getJobAge());
					body.put("needNum", recruitmentBean.getNeedNum());
					body.put("knowledgeId", recruitmentBean.getKnowledgeId());
					body.put("knowledge", recruitmentBean.getKnowledge());
					body.put("goodAtId", recruitmentBean.getGoodAtId());
					body.put("goodAt", recruitmentBean.getGoodAt());
					body.put("workAddress", recruitmentBean.getWorkAddress());
					body.put("arriveTime", recruitmentBean.getArriveTime());
					body.put("longitude", recruitmentBean.getLongitude());
					body.put("latitude", recruitmentBean.getLatitude());
					body.put("voiceSeconds", recruitmentBean.getVoiceSeconds());
					body.put("contactName", recruitmentBean.getContactName());
					body.put("contactPhone", recruitmentBean.getContactPhone());
					body.put("resumeEmail", recruitmentBean.getResumeEmail());
					body.put("endDate", recruitmentBean.getEndDate());
					body.put("delStatus", recruitmentBean.getDelStatus());

					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(body)));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
					FormFileBean formFile = new FormFileBean(
							new File(voicePath), "audio.aac");
					formFileBeans.add(formFile);
					mapFileBean.put("audio", formFileBeans);
					uploadFiles(REQCODE_EDITJOBINFO,
							AppConfig.EMPLOY_REQUEST_MAPPING, mapFileBean,
							reqBase, true);

					// EmployDataLogic.getInstance().editJobInfoAction(
					// recruitmentBean, voicePath, new HttpCallback() {
					// @Override
					// public void onSuccess(Object body) {
					// dialog1.dismiss();
					// finish();
					// }
					//
					// @Override
					// public void onFailed(String error) {
					// dialog1.dismiss();
					// ToastUtil.showShortToast("" + error);
					// }
					// });

				} else {
					// 新增publishJobAction
					ReqBase reqBase = new ReqBase();
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSINESS_PUBLISH_JOBINFO));
					Map<String, Object> body = new HashMap<String, Object>();
					body.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					body.put("companyId", recruitmentBean.getCompanyId());
					body.put("positionId", recruitmentBean.getPositionId());
					body.put("jobName", recruitmentBean.getJobName());
					body.put("jobType", recruitmentBean.getJobType());
					body.put("isMarry", recruitmentBean.getIsMarry());
					body.put("cityCode", recruitmentBean.getCityCode());
					body.put("salaryId", recruitmentBean.getSalaryId());
					body.put("salaryValue", recruitmentBean.getSalaryValue());
					body.put("salaryFrom", recruitmentBean.getSalaryFrom());
					body.put("salaryTo", recruitmentBean.getSalaryTo());
					body.put("houseSupport", recruitmentBean.getHouseSupport());
					body.put("jobIntroduce", recruitmentBean.getJobIntroduce());
					body.put("gender", recruitmentBean.getGender());
					body.put("age", recruitmentBean.getAge());
					body.put("education", recruitmentBean.getEducation());
					body.put("jobAge", recruitmentBean.getJobAge());
					body.put("needNum", recruitmentBean.getNeedNum());
					body.put("knowledgeId", recruitmentBean.getKnowledgeId());
					body.put("knowledge", recruitmentBean.getKnowledge());
					body.put("goodAtId", recruitmentBean.getGoodAtId());
					body.put("goodAt", recruitmentBean.getGoodAt());
					body.put("workAddress", recruitmentBean.getWorkAddress());
					body.put("arriveTime", recruitmentBean.getArriveTime());
					body.put("longitude", recruitmentBean.getLongitude());
					body.put("latitude", recruitmentBean.getLatitude());
					body.put("voiceSeconds", recruitmentBean.getVoiceSeconds());
					body.put("contactName", recruitmentBean.getContactName());
					body.put("contactPhone", recruitmentBean.getContactPhone());
					body.put("resumeEmail", recruitmentBean.getResumeEmail());
					body.put("endDate", recruitmentBean.getEndDate());
					body.put("delStatus", recruitmentBean.getDelStatus());

					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(body)));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
					FormFileBean formFile = new FormFileBean(
							new File(voicePath), "audio.aac");
					formFileBeans.add(formFile);
					mapFileBean.put("audio", formFileBeans);
					uploadFiles(REQCODE_PUBLISHJOBINFO,
							AppConfig.EMPLOY_REQUEST_MAPPING, mapFileBean,
							reqBase, true);

					// EmployDataLogic.getInstance().publishJobAction(
					// recruitmentBean, voicePath, new HttpCallback() {
					// @Override
					// public void onSuccess(Object body) {
					// dialog1.dismiss();
					// finish();
					// ToastUtil.showShortToast("发布职位成功！");
					// }
					//
					// @Override
					// public void onFailed(String error) {
					// dialog1.dismiss();
					// ToastUtil.showShortToast("" + error);
					// }
					// });
				}
			}
			break;
		}
	}

	// 检验数据合法性
	private boolean isDateLegal() {
		String jobName = jobNameET.getText().toString();
		String application = application_type.getText().toString();
		String position = positionName.getText().toString();
		String numberneeds = number_of_needs.getText().toString();
		String salary = salary_text.getText().toString();
		String accommodation = get_accommodation.getText().toString();
		String workaddress = work_address.getText().toString();
		String maritalstatus = marital_status.getText().toString();
		String gendertext = gender_text.getText().toString();
		String agetext = age_text.getText().toString();
		String educationtext = education_text.getText().toString();
		String workingexperience = working_experience.getText().toString();
		String timearrival = time_of_arrival.getText().toString();
		String knowledgetext = knowledge_text.getText().toString();
		// String lifeskill=life_skills.getText().toString();
		String workcity = work_city.getText().toString();
		String jobdescription = job_description.getText().toString();
		String conName = contactName.getText().toString();
		String conPhone = contactPhone.getText().toString();
		if ("".equals(jobName)) {
			Toast.makeText(this, "请输入职位名称！", Toast.LENGTH_SHORT).show();
			return false;
		} else if ("请选择".equals(position)) {
			ToastUtil.showShortToast("请选择职位类别");
			return false;
		} else if ("请选择".equals(application)) {
			ToastUtil.showShortToast("工作性质请选择");
			return false;
		} else if ("请选择".equals(workcity)) {
			ToastUtil.showShortToast("工作城市请选择");
			return false;
		} else if ("请选择".equals(salary)) {
			ToastUtil.showShortToast("请选择薪资待遇");
			return false;

		} else if ("请选择".equals(accommodation)) {
			ToastUtil.showShortToast("请选择是否住宿");
			return false;

		} else if ("请选择".equals(numberneeds)) {
			ToastUtil.showShortToast("请选择需要人数");
			return false;

		} else if ("请填写".equals(jobdescription)) {
			ToastUtil.showShortToast("请填写职位描述");
			return false;

		} else if ("请选择".equals(workaddress)) {
			ToastUtil.showShortToast("请选择上班地址");
			return false;
		} else if ("请选择".equals(maritalstatus)) {
			ToastUtil.showShortToast("请选择婚姻状况");
			return false;
		} else if ("请选择".equals(gendertext)) {
			ToastUtil.showShortToast("请选择性别");
			return false;
		} else if ("请选择".equals(agetext)) {
			ToastUtil.showShortToast("请选择年龄");
			return false;
		} else if ("请选择".equals(educationtext)) {
			ToastUtil.showShortToast("请选择学历");
			return false;
		} else if ("请选择".equals(workingexperience)) {
			ToastUtil.showShortToast("请选择美业职龄");
			return false;
		} else if ("请选择".equals(timearrival)) {
			ToastUtil.showShortToast("请选择到岗时间");
			return false;
		} else if ("请选择".equals(knowledgetext)) {
			ToastUtil.showShortToast("请选择专业技能");
			return false;
		} else if ("请填写".equals(conName)) {
			ToastUtil.showShortToast("请填写联系人");
			return false;
		} else if ("请填写".equals(conPhone)) {
			ToastUtil.showShortToast("请填写联系电话");
			return false;
		}
		// else if("请选择".equals(lifeskill)){
		// ToastUtil.showShortToast("请选择生活特长");
		// return false;
		// }
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			/**
			 * 职位名称
			 */
			case JOBNAME:
				positionName.setText(data.getStringExtra("name"));
				positionId = data.getIntExtra("id", 0) + "";
				// positiongroupId = data.getIntExtra("groupId", 0) + "";
				String intentPostype = data.getStringExtra("type");
				break;
			/**
			 * 需要人数
			 */
			case NUMBERNEEDS:
				numberneeds = data.getStringExtra("numberneeds");
				numberNeedscode = data.getStringExtra("numberneedsCode");
				number_of_needs.setText(numberneeds);
				break;
			/**
			 * 工资
			 */
			case SALARY:

				salary = data.getStringExtra("salary");
				salarycode = data.getStringExtra("salaryCode");
				maximum = data.getStringExtra("maximum");
				minimum = data.getStringExtra("minimum");
				salary_text.setText(salary);
				break;
			/**
			 * 住宿
			 */
			case ACCOMMODATION:
				get_accommodation.setText(data.getStringExtra("requireStay"));
				accommodation = Integer.parseInt(data
						.getStringExtra("accommodation"));
				break;
			/**
			 * 上班地址
			 */
			case MARRYSTARE:
				work_address.setText(data.getStringExtra("workaddress"));
				// workaddressCode = data.getStringExtra("cityCode");
				break;
			/**
			 * 是否结婚
			 */
			case ISMARRY:

				marital_status.setText(data.getStringExtra("isMarry"));
				isMarryPostion = Integer.parseInt(data
						.getStringExtra("isMarryPostion"));
				break;
			/**
			 * 性别
			 */
			case GENDER:
				gender_text.setText(data.getStringExtra("gender"));
				genderpostion = Integer.parseInt(data
						.getStringExtra("genderPostion"));
				break;
			/**
			 * 年龄
			 */
			case AGE:
				age_text.setText(data.getStringExtra("age"));
				ageCode = Integer.parseInt(data.getStringExtra("ageCode"));
				break;
			/**
			 * 学历
			 */
			case EDUCATION:
				education_text.setText(data.getStringExtra("education"));
				educationCode = Integer.parseInt(data
						.getStringExtra("educationCode"));
				break;
			/**
			 * 学历
			 */
			case WORKINGEXPERIENCE:
				working_experience.setText(data.getStringExtra("experience"));
				experienceCode = Integer.parseInt(data
						.getStringExtra("experienceCode"));
				break;
			/**
			 * 到岗时间
			 */
			case TIMEARRIVAL:
				time_of_arrival.setText(data.getStringExtra("arrival"));
				arrivalCode = Integer.parseInt(data
						.getStringExtra("arrivalCode"));
				break;
			/**
			 * 专业知识
			 */
			case PROFESSION_KNOWLEDGE:
				knowledge_text.setText(data.getStringExtra("knowledge"));
				knowledgeCode = Integer.parseInt(data
						.getStringExtra("knowledgeCode"));
				break;
			/**
			 * 职位描述
			 */
			case JOBDESCRIPTION:
				job_description.setText(data.getStringExtra("jobdescription"));
				break;
			/**
			 * 生活特长
			 */
			// case LIFESKILL:
			// life_skills.setText(data.getStringExtra("lifeskills"));
			// lifeSkillId = data.getStringExtra("lifeskillsCode");
			// break;
			/**
			 * 工作性质
			 */
			case APPLICATIONTYPE:
				application_type.setText(data.getStringExtra("application"));
				jobType = data.getStringExtra("applicationCode");
				break;

			case TAKE_SOUND:
				// 录制音频
				if (data != null) {
					String soundFile = data.getStringExtra("file");
					recordingTime = data.getStringExtra("recordingTime");// 播放时间
					soundPath = soundFile;

					ReqBase reqBase = new ReqBase();
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSINESS_UPLOAD_MK_RESUME_MEDIA));
					Map<String, Object> body = new HashMap<String, Object>();
					body.put("userId", AppContext.getInstance().getUserInfo()
							.getId());
					body.put("fileSeconds", recordingTime);
					body.put("title", "");
					body.put("remark", "");
					body.put("isPublic", 1);
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(body)));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
					FormFileBean formFile = new FormFileBean(
							new File(soundPath), "audio.mp3");
					formFileBeans.add(formFile);
					mapFileBean.put("voice", formFileBeans);
					uploadFiles(REQCODE_UPLOADMKMEDIA,
							AppConfig.RESUME_REQUEST_MAPPING, mapFileBean,
							reqBase, true);

					// ResumeDataLogic.getInstance()
					// .uploadMKResumeMediaAttachWithUserId(
					// AppData.getInstance().getLoginUser()
					// .getUserId(), 0, recordingTime, "",
					// "", soundPath, "", 1, new HttpCallback() {
					// @Override
					// public void onSuccess(Object body) {
					// }
					//
					// @Override
					// public void onFailed(String error) {
					// }
					// });
				}
				break;
			}

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			delStatus = "1";
		} else {
			delStatus = "0";
		}
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.recruitment_trea_activity;
	}

	@Override
	public void initValue() {
		Intent intent = getIntent();
		jobId = intent.getStringExtra("jobId");
		initData();
	}

	@Override
	public void bindListener() {
		initListener();
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		String jsondata = resp.getBody().toString();
		LogUtil.d("hl", jsondata);
		switch (requestCode) {
		case reqCodeOne:
			bean = (RecruitmentReq) JsonUtil.jsonToObj(
					RecruitmentInfo.RecruitmentReq.class, jsondata);
			if (null != bean.job) {
				getData(bean);
			}
			break;

		case reqCodeTwo:
			CompanyResumeData.CompanyResumeReq req = (CompanyResumeReq) JsonUtil
					.jsonToObj(CompanyResumeData.CompanyResumeReq.class,
							jsondata);
			CompanyResumeData data = req.company;
			if (null != data) {
				// 公司名称
				String name = data.name;
				// 公司地址
				String address = data.address;
				String phone = data.phone;
				contactPhone.setText(phone);
				String textCompany = "";
				if (!"".equals(name)) {
					textCompany += name + "\n";
				}
				if (!"".equals(address)) {
					textCompany += address;
				}
				companyJobTextView.setText(textCompany);
			}
			break;
		case REQCODE_EDITJOBINFO:
			finish();
			break;
		case REQCODE_PUBLISHJOBINFO:
			ToastUtil.showShortToast("发布职位成功！");
			finish();
			break;
		case REQCODE_UPLOADMKMEDIA:
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}
}
