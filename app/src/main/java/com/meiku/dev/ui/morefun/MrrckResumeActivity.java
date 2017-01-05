package com.meiku.dev.ui.morefun;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.meiku.dev.R;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ResumBean;
import com.meiku.dev.bean.ResumeCenterEntity;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.recruit.MultiSelectActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.TopTitle;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人简历
 */
public class MrrckResumeActivity extends BaseActivity implements
		View.OnClickListener {

	public static final int REQCODE_GETRESUME = 10001; // 获取个人简历
	public static final int REQCODE_UPDATERESUME = 2001;// 创建个人简历
	public static final int REQCODE_MODIFYRESUME = 3001;// 更新个人简历
	public static final int REQCODE_MKRESUMEVOICE = 4001;// 音频简历
	public static final int REQCODE_MKRESUMEVEDIO = 4002;// 视频简历

	public static final int GENDER = 0;// 性别
	public static final int BIRTHDAY = 1;// 出生日期
	public static final int JOB = 2;// 现在岗位
	public static final int HOME = 3;// 现居地
	public static final int EDUCATION = 4;// 最高学历
	public static final int WORKTIME = 5;// 美业职龄
	public static final int MARRY = 6;// 婚姻状况
	public static final int JOB_POSITION = 7; // 现在岗位
	public static final int TAKE_VIDEO = 8; // 录制视频
	public static final int TAKE_SOUND = 9; // 录制音频
	public static final int MREQUIRESTAY = 10; // 是否要住宿
	public static final int INTENTPOS = 11; // 意向职位
	public static final int MARRYSTARE = 12;// 意向城市
	public static final int EXPECTSALAYNUM = 13;// 期望薪水
	public static final int GETWORKAGE = 14;// 工作年龄
	public static final int PROFESSION_KNOWLEDGE = 15;// 专业技法
	public static final int TO_SELECT_VIDEO = 16;// 视频选择
	public static final int TAKE_PHOTO_IMG = 100;// 头像选择
	public static final int EDITVIDEO = 17;// 修改视频
	public static final int EDITVOICE = 18;// 修改音频
	public static final int BOSS_TYPE = 19;
	public static final int BENEFITS = 20;// 福利待遇

	View layout_info, layout_birthday, layout_education, layout_intent,
			layout_city, layout_salary, layout_reward, layout_workyear,
			layout_proskill, layout_glory;
	ImageView usergender, iv_major;

	TextView major, tv_education, tv_verify, tv_reward, tv_workyear,
			tv_experience;
	EditText et_phone, tv_glory;
	RadioGroup group_sex, group_letter, group_marry, group_jobtime;

	private String isRecom = "1";

	HashSet<String> multiChoiceID = new HashSet<String>();
	HashSet<String> multiChoiceValue = new HashSet<String>();

	// private LinearLayout personalInfo;//个人信息

	private EditText realNameEdit;// 真实姓名

	private LinearLayout genderView;// 性别

	private LinearLayout birthdayView;// 出生日期

	private TextView birthdayText;// 出生日期

	private LinearLayout jobView;// 现在岗位
	private TextView jobText;// 现在岗位

	private LinearLayout homeView;// 现居地
	private TextView homeText;// 现居地

	private LinearLayout educationView;// 最高学历

	private LinearLayout workTimeView;// 美业职龄
	private TextView workTimeText;// 美业职龄

	private LinearLayout marryView;// 婚姻状况
	private TextView marryText;// 婚姻状况

	private static final String[] marryStr = new String[] { "未婚", "已婚" };// 性别
	private EditText phoneEdit;// 手机号码

	private EditText emailEdit;// 邮箱

	private EditText qqEdit;// QQ号码

	private TextView proSkillTXT;// 专业技法
	private String proSkillcode = "";
	// private EditText phoneNumET;\

	private TextView liveLocateET;// 居住地
	private TextView marryStateTExt;// 婚姻状况
	private TextView currentJobTX;// 目前岗位
	private TextView intentPosET;// 意向职位;
	private TextView marryStateET;// 意向城市

	private TextView bossTypeET;// 意向老板类型
	private String bossTypeId = "";
	private String bossTypeValue = "";

	private TextView requireStayET;// 是否要求住宿
	private TextView shuaxin_text;// 刷新时间
	private RadioGroup radioGroup;
	private RadioButton radioButtonomne;
	private RadioButton find_good_job;
	private RadioButton not_find_job;

	private TextView userage;// 用户年龄

	private TextView ExpectSalay;// 期望月薪

	private TextView work_year;// 工作年限
	private TextView userNameTXT;// 用户名字
	private MySimpleDraweeView userHeadIMG;// 用户头像
	private ToggleButton isPublic;// 是否公开
	/**
	 * 添加视频
	 */
	private FrameLayout addVideoFL;
	private MySimpleDraweeView videoView;
	private String videoPath = null;

	/**
	 * 添加录音
	 */
	private RelativeLayout addAudioRL;
	private String soundPath = null;// 录音路径
	private String recordingTime;// 录音时间
	private String videoSeconds;
	// 目前状况
	String changeStr = "2";

	private String strId;
	private String strValue;

	private String educationCode;// 学历
	private String jobType = "1";// 工作类型1:全职 2:兼职 3:实习
	private String SalaycodeId;// 期望薪水
	private String intentcodeId;// 求职意向的ID
	private String cityCode;// 城市code
	private String wantCityCode;// 意向城市
	private String positionId;
	private int postion;// 是否未婚
	private int accommodation;// 是否住宿
	private String work_code;
	private String wordResumeId;// 修改的時候提交的id
	private String resumeId;// 修改的時候提交的id
	private int genderpostion = 2;// 性別code、1男2女
	private String Knowledge;// 知识
	private String isPublicStr = "1"; // 0不公开 1公开
	private String isPublish;
	String thumbnailPath = null;

	String resumeMrrckId;

	/**
	 * 修改视频
	 */
	private TextView editVideoTXT;

	/**
	 * 修改音频
	 */
	private TextView editVoiceTXT;

	private int mUserId;
	private TextView infoAudioTXT;
	private String headPicPath;// 选择的图片路径
	private ImageView iv_recognize;
	private String fringeBenefitsId;
	private String fringeBenefits;

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	private void initData() {
		String getresumeId = AppContext.getInstance().getUserInfo()
				.getResumeId()
				+ "";

		mUserId = AppContext.getInstance().getUserInfo().getId();

		if ("-1".equals(getresumeId)) {
			et_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
			layout_info.setVisibility(View.INVISIBLE);
			userHeadIMG.setUrlOfImage(AppContext.getInstance().getUserInfo()
					.getClientHeadPicUrl()
					+ ConstantKey.THUMB);
		} else {
			ReqBase reqBase = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", mUserId);
			map.put("resumeId", getresumeId);
			reqBase.setHeader(new ReqHead(AppConfig.EMPLOY_REQUEST_SEARCHRESUME));
			reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
			httpPost(REQCODE_GETRESUME, AppConfig.RESUME_REQUEST_MAPPING,
					reqBase, true);
		}
	}

	// 网络获取数据
	private void getWebData(final ResumeCenterEntity r) {
		userage.setText(r.getAge() + "");
		major.setText(r.getLikeJobs());
		work_year.setText("工作年限:" + r.getJobAgeName());
		work_code = r.getJobAge() + "";
		tv_workyear.setText("工作年限:" + r.getJobAgeName());
		tv_glory.setText(r.getHonor());

		et_phone.setText(r.getPhone());// 获取手机号码
		// liveLocateET.setText(r.getCityName());// 居住地
		//
		// marryStateTExt.setText(r.getIsMarryName());
		//
		// currentJobTX.setText(r.getPositionName());// 当前工作

		realNameEdit.setText(r.getRealName());// 真实姓名
		// MkUser modUserData = AppContext.getInstance().getUserInfo();
		// if (null != r.getResumeId()
		// && !"".equals(r.getResumeId())) {
		// modUserData
		// .setResumeId(Integer.parseInt(r.getResumeId()));
		// } else {
		// modUserData.setResumeId(-1);
		// }

		intentPosET.setText(r.getLikeJobs());// 意向职位;
		intentcodeId = r.getLikeJobsId();// 意向职位
		wantCityCode = r.getLikeCitysCode();// 意向城市
		marryStateET.setText(r.getLikeCitys());// 意向城市
		// bossTypeET.setText(resumeWord.getBossType());
		ExpectSalay.setText(r.getExpectSalaryName());// 期望月薪
		SalaycodeId = r.getExpectSalary() + "";// 意向薪资

		fringeBenefits = r.getFringeBenefits();
		fringeBenefitsId = r.getFringeBenefitsId();
		tv_reward.setText(fringeBenefits);

		if ("1".equals(r.getGender())) {
			Drawable drawable = ContextCompat.getDrawable(
					MrrckResumeActivity.this, R.drawable.nan_white);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			userage.setCompoundDrawables(drawable, null, null, null);
			userage.setBackgroundResource(R.drawable.nanxing);
			group_sex.check(R.id.btn_gender_male);
			genderpostion = 1;
		} else {
			Drawable drawable = ContextCompat.getDrawable(
					MrrckResumeActivity.this, R.drawable.nv_white);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			userage.setCompoundDrawables(drawable, null, null, null);
			userage.setBackgroundResource(R.drawable.nvxing);
			group_sex.check(R.id.btn_gender_female);
			genderpostion = 2;
		}
		if ("2".equals(r.getIsMarry())) {
			group_marry.check(R.id.btn_marry);
			postion = 2;
		} else {
			group_marry.check(R.id.btn_unmarried);
			postion = 1;
		}
		birthdayText.setText(r.getBirthDate());
		tv_education.setText(r.getEducationName());
		educationCode = r.getEducation() + "";
		if (1 == r.getJobType()) {
			group_jobtime.check(R.id.btn_alltime);
			jobType = "1";
		} else if (2 == r.getJobType()) {
			group_jobtime.check(R.id.btn_parttime);
			jobType = "2";
		} else {
			group_jobtime.check(R.id.btn_practice);
			jobType = "3";
		}
		if (r.getIsRecommend() == 1) {
			group_letter.check(R.id.btn_have);
			isRecom = "1";
		} else {
			group_letter.check(R.id.btn_none);
			isRecom = "0";
		}
		tv_experience.setText(r.getPersonalResume());

		// resumeId = resume1.getId();

		if ("0".equals(r.getResumeStatus())) {
			not_find_job.setChecked(true);
		} else if ("1".equals(r.getResumeStatus())) {
			find_good_job.setChecked(true);
		} else {
			radioButtonomne.setChecked(true);
		}
		Knowledge = r.getKnowledge();
		proSkillTXT.setText(Knowledge);
		proSkillcode = r.getKnowledgeId();// 专业知识

		changeStr = r.getResumeStatus();// 简历状态0:暂不考虑新工作 1:正在找工作 2:观望好的机会
		// positionId = resume1.getPositionId();// 现在工作岗位
		// cityCode = resume1.getCityCode();// 现在居住城市

		// bossTypeValue = resumeWord.getBossType();
		// bossTypeId = resumeWord.getBossTypeId();

		// if (null != resumeWord.getHouseSupport()
		// && !"".equals(resumeWord.getHouseSupport())) {
		// accommodation = Integer.parseInt(resumeWord.getHouseSupport());//
		// 是否提供住宿
		// } else {
		// accommodation = -1;
		// }
		//
		// int nowstr = Integer.parseInt(Util.getTodayDate().substring(0, 4));
		// int birthStr = Integer.parseInt(resume1.getBirthDate().substring(0,
		// 4));
		// int oldStr = nowstr - birthStr;
		// userage.setText(oldStr + "岁");
		// recordingTime = resumeMrrck.getVoiceSeconds();
		// videoSeconds = resumeMrrck.getVideoSeconds();
		// if (!TextUtils.isEmpty(videoSeconds)) {
		//
		// } else {
		// videoSeconds = "0";
		// }
		// if (!TextUtils.isEmpty(recordingTime)) {
		// infoAudioTXT.setText("语音\n" + "(" + recordingTime + "秒）");
		// } else {
		// recordingTime = "0";
		// }
		if (!TextUtils.isEmpty(r.getVoiceResume())) {
			soundPath = r.getVoiceResume();
			recordingTime = r.getVoiceSeconds() + "";
		}
		if (!TextUtils.isEmpty(r.getVideoResume())) {
			videoPath = r.getVideoResume();
			videoSeconds = r.getVideoSeconds() + "";
		}
		// isPublicStr = resume1.getIsPublic();
		// if ("1".equals(isPublicStr)) {
		// isPublic.setChecked(true);
		// } else {
		// isPublic.setChecked(false);
		// }

		if (!TextUtils.isEmpty(r.getResumePhoto())) {
			userHeadIMG.setUrlOfImage(r.getResumePhoto());
		} else {
			userHeadIMG.setUrlOfImage(AppContext.getInstance().getUserInfo()
					.getClientHeadPicUrl()
					+ ConstantKey.THUMB);
		}
		if (!TextUtils.isEmpty(r.getVideoPhoto())) {
			videoView.setUrlOfImage(r.getVideoPhoto());
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		// case R.id.personal_info_view:
		// intent = new Intent(getActivity(), PersonalInfoActivity.class);
		// startActivity(intent);
		// break;
		case R.id.layout_reward:
			// List<DataconfigEntity> pofessionlist1 = MKDataBase.getInstance()
			// .getBenefits();
			// LogUtil.d("hl", "福利待遇=" + pofessionlist1.size());
			// alertDialog(pofessionlist1, "福利待遇", BENEFITS);
			// // fringeBenefitsId = strId;
			// // fringeBenefits = strValue;
			// // tv_reward.setText(fringeBenefits);

			intent = new Intent(this, MultiSelectActivity.class);
			intent.putExtra("type", MultiSelectActivity.TYPE_BENIFIT);
			intent.putExtra("ids", fringeBenefitsId);
			startActivityForResult(intent, BENEFITS);
			break;
		case R.id.layout_workyear:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "GETWORKAGE");
			intent.putExtra("code", "GETWORKAGECODE");
			intent.putExtra("requestCode", GETWORKAGE);
			startActivityForResult(intent, GETWORKAGE);
			break;
		case R.id.goback:
			finishWhenTip();
			break;
		case R.id.editVideoTXT: // 修改视频
			// Intent videoIntent = new Intent(this,
			// MrrckResumeAttachActivity.class);
			// videoIntent.putExtra(MrrckResumeAttachActivity.BUNDLE_ATTACH_TYPE,
			// 1);
			// // startActivity(videoIntent);
			// startActivityForResult(videoIntent, EDITVIDEO);

			intent = new Intent(this, ShootVideoActivity.class);
			startActivityForResult(intent, TO_SELECT_VIDEO);
			break;
		case R.id.editVoiceTXT: // 修改音频
			// Intent voiceIntent = new Intent(this,
			// MrrckResumeAttachActivity.class);
			// voiceIntent.putExtra(MrrckResumeAttachActivity.BUNDLE_ATTACH_TYPE,
			// 2);
			// if (!TextUtils.isEmpty(resumeMrrckId)) {
			// voiceIntent.putExtra("resumeMrrckId", resumeMrrckId);
			// // startActivity(voiceIntent);
			// }
			// startActivityForResult(voiceIntent, EDITVOICE);
			intent = new Intent(this, RecordActivity.class);
			intent.putExtra(RecordActivity.BUNDLE_IS_PUBLISH, "1");
			startActivityForResult(intent, TAKE_SOUND);
			break;
		case R.id.right_txt_title: // 提交
			if (isDateLegal()) {
				// final ProgressDialog dialog = showSpinnerDialog();
				ResumBean resumBean = new ResumBean();
				resumBean.setUserId(mUserId + "");// 用户id
				resumBean.setResumeStatus(changeStr); // 简历状态0:暂不考虑新工作 1:正在找工作
														// 2:观望好的机会
				// resumBean.setPositionId(positionId);// 现在工作岗位
				resumBean.setCityCode(cityCode);// 现居住城市code
				if (null != work_code && !"".equals(work_code)) {
					resumBean.setJobAge(work_code);// 美业职龄
				} else {
					resumBean.setJobAge("-1");// 美业职龄
				}

				// birthDate // 生日
				resumBean.setBirthDate(birthdayText.getText().toString());
				// gender // 性别
				resumBean.setGender(genderpostion + "");
				// realName // 真实姓名
				resumBean.setRealName(realNameEdit.getText().toString());

				resumBean.setVideoSeconds(videoSeconds);// 视频长度秒数（必填）
				// resumBean.setVideoTitle("");// 视频标题（可选）
				// resumBean.setVideoRemark("");// 视频备注（可选）
				resumBean.setVoiceSeconds(recordingTime);// 音频长度秒数（必填）
				// resumBean.setVoiceTitle("");// 音频标题（可选）
				// resumBean.setVoiceRemark("");// 音频备注（可选）

				// knowledgeId 专业知识id
				resumBean.setKnowledgeId(proSkillcode);
				// knowledge 专业知识
				resumBean.setKnowledge(Knowledge);

				resumBean.setIsMarry(postion + "");// 婚姻状况 1：未婚，2：已婚，3：保密
				resumBean.setPhone(et_phone.getText().toString());// 手机号码
				resumBean.setLikeJobsId(intentcodeId);// 意向职位id
				resumBean.setLikeJobs(intentPosET.getText().toString());// 意向职位
				resumBean.setLikeCitysCode(wantCityCode);// 意向城市code，多个，隔开
				resumBean.setLikeCitys(marryStateET.getText().toString());// 意向城市
				resumBean.setExpectSalary(SalaycodeId);// 意向工资
				// resumBean.setHouseSupport(accommodation + "");// 是否希望职位提供住宿

				resumBean.setJobType(jobType);
				resumBean.setEducation(educationCode);
				resumBean.setFringeBenefits(fringeBenefits);
				resumBean.setHonor(tv_glory.getText().toString());
				resumBean.setIsRecommend(isRecom);
				resumBean.setPersonalResume(tv_experience.getText().toString());
				resumBean.setFringeBenefitsId(fringeBenefitsId);
				// 意向老板类型
				// resumBean.setBossType(bossTypeValue);
				// resumBean.setBossTypeId(bossTypeId);

				// resumBean.setIsPublic(isPublicStr);// 是否公开

				if (-1 == AppContext.getInstance().getUserInfo().getResumeId()) {// 提交
					// 保存 soundPath音频路径videoPath视频路径

					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("birthDate", resumBean.getBirthDate());
					map.put("gender", resumBean.getGender());
					map.put("realName", resumBean.getRealName());
					map.put("knowledgeId", resumBean.getKnowledgeId());
					map.put("knowledge", resumBean.getKnowledge());
					// map.put("userId", resumBean.getUserId());
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId()
							+ "");
					map.put("resumeStatus", resumBean.getResumeStatus());
					// map.put("positionId", resumBean.getPositionId());
					if (null != AppContext.getInstance().getUserInfo()
							.getCityCode()
							&& !"".equals(AppContext.getInstance()
									.getUserInfo().getCityCode())) {
						map.put("cityCode", AppContext.getInstance()
								.getUserInfo().getCityCode());
					} else {

						map.put("cityCode", 0);
					}
					map.put("jobAge", resumBean.getJobAge());
					map.put("videoSeconds", resumBean.getVideoSeconds());
					map.put("voiceSeconds", resumBean.getVoiceSeconds());
					map.put("isMarry", resumBean.getIsMarry());
					map.put("phone", resumBean.getPhone());
					map.put("likeJobsId", resumBean.getLikeJobsId());
					map.put("likeJobs", resumBean.getLikeJobs());
					map.put("likeCitysCode", resumBean.getLikeCitysCode());
					map.put("likeCitys", resumBean.getLikeCitys());
					map.put("expectSalary", resumBean.getExpectSalary());
					// map.put("houseSupport", resumBean.getHouseSupport());
					// map.put("bossType", resumBean.getBossType());
					// map.put("bossTypeId", resumBean.getBossTypeId());
					// map.put("isPublic", resumBean.getIsPublic());
					map.put("education", resumBean.getEducation());
					map.put("fringeBenefits", resumBean.getFringeBenefits());
					map.put("fringeBenefitsId", resumBean.getFringeBenefitsId());
					map.put("jobType", resumBean.getJobType());
					map.put("honor", resumBean.getHonor());
					map.put("isRecommend", resumBean.getIsRecommend());
					map.put("personalResume", resumBean.getPersonalResume());
					if (!Tool.isEmpty(soundPath) && !soundPath.contains("http")) {
						setMapImageKeyValue(map, "voiceFileUrlJSONArray",
								soundPath, "2");
					}
					if (!TextUtils.isEmpty(videoPath)
							&& !TextUtils.isEmpty(thumbnailPath)
							&& !videoPath.contains("http")) {
						if (!Tool.isEmpty(thumbnailPath)) {
							setMapImageKeyValue(map,
									"videoPhotoFileUrlJSONArray",
									thumbnailPath, "0");
						}
						if (!Tool.isEmpty(videoPath)) {
							setMapImageKeyValue(map, "videoFileUrlJSONArray",
									videoPath, "1");
						}
					}
					if (!Tool.isEmpty(headPicPath)) {
						setMapImageKeyValue(map, "resumePhotoFileUrlJSONArray",
								headPicPath, "0");
					}
					LogUtil.d("hl", "创建我的简历" + map);
					reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_80060));
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));

					// final Map<String, List<FormFileBean>> mapFileBean = new
					// HashMap<String, List<FormFileBean>>();
					// s
					// if (!TextUtils.isEmpty(soundPath)
					// && !soundPath.contains("http")) {
					// // 音频
					// List<FormFileBean> listBeans = new
					// ArrayList<FormFileBean>();
					// listBeans.add(new FormFileBean(new File(soundPath),
					// "audio.mp3"));
					// mapFileBean.put("voice", listBeans);
					//
					// } else {
					// mapFileBean.put("voice", new ArrayList<FormFileBean>());
					// }
					// if (!TextUtils.isEmpty(videoPath)
					// && !videoPath.contains("http")
					// && !TextUtils.isEmpty(thumbnailPath)) {
					// // 视频
					// List<FormFileBean> listBeans = new
					// ArrayList<FormFileBean>();
					// listBeans.add(new FormFileBean(new File(thumbnailPath),
					// "photo.png"));
					// mapFileBean.put("videoPhoto", listBeans);
					//
					// List<FormFileBean> listBean = new
					// ArrayList<FormFileBean>();
					// listBean.add(new FormFileBean(new File(videoPath),
					// "video.mp4"));
					// mapFileBean.put("video", listBean);
					// } else {
					// mapFileBean.put("video", new ArrayList<FormFileBean>());
					// }
					// if (picPathList.size() > 0) {
					//
					// for (int i = 0; i < picPathList.size(); i++) {
					// Object imgPathString = picPathList.get(i);
					// if (imgPathString.getClass().getName()
					// .equals(String.class.getName())) {
					// List<FormFileBean> listBean = new
					// ArrayList<FormFileBean>();
					// listBean.add(new FormFileBean(new File(
					// (String) picPathList.get(i)), "image_"
					// + i + ".png"));
					// mapFileBean.put("photo", listBean);
					//
					// }
					// }
					// }

					httpPost(REQCODE_UPDATERESUME,
							AppConfig.RESUME_REQUEST_MAPPING, reqBase, true);

				} else {
					// 修改
					// 现在工作岗位
					ReqBase reqBase = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("resumeId", AppContext.getInstance().getUserInfo()
							.getResumeId());
					map.put("birthDate", resumBean.getBirthDate());
					map.put("gender", resumBean.getGender());
					map.put("realName", resumBean.getRealName());
					map.put("knowledgeId", resumBean.getKnowledgeId());
					map.put("knowledge", resumBean.getKnowledge());
					map.put("userId", AppContext.getInstance().getUserInfo()
							.getId()
							+ "");// resumBean.getUserId());
					map.put("resumeStatus", resumBean.getResumeStatus());
					// map.put("positionId", resumBean.getPositionId());
					if (null != AppContext.getInstance().getUserInfo()
							.getCityCode()
							&& !"".equals(AppContext.getInstance()
									.getUserInfo().getCityCode())) {
						map.put("cityCode", AppContext.getInstance()
								.getUserInfo().getCityCode());
					} else {
						map.put("cityCode", 0);
					}
					map.put("jobAge", resumBean.getJobAge());
					map.put("videoSeconds", resumBean.getVideoSeconds());
					map.put("voiceSeconds", resumBean.getVoiceSeconds());
					map.put("isMarry", resumBean.getIsMarry());
					map.put("phone", resumBean.getPhone());
					map.put("likeJobsId", resumBean.getLikeJobsId());
					map.put("likeJobs", resumBean.getLikeJobs());
					map.put("likeCitysCode", resumBean.getLikeCitysCode());
					map.put("likeCitys", resumBean.getLikeCitys());
					map.put("expectSalary", resumBean.getExpectSalary());
					// map.put("houseSupport", resumBean.getHouseSupport());
					// map.put("bossType", resumBean.getBossType());
					// map.put("bossTypeId", resumBean.getBossTypeId());
					// map.put("isPublic", resumBean.getIsPublic());
					map.put("education", resumBean.getEducation());
					map.put("fringeBenefits", resumBean.getFringeBenefits());
					map.put("fringeBenefitsId", resumBean.getFringeBenefitsId());
					map.put("jobType", resumBean.getJobType());
					map.put("honor", resumBean.getHonor());
					map.put("isRecommend", resumBean.getIsRecommend());
					map.put("personalResume", resumBean.getPersonalResume());
					if (!Tool.isEmpty(soundPath) && !soundPath.contains("http")) {
						setMapImageKeyValue(map, "voiceFileUrlJSONArray",
								soundPath, "2");
					}
					if (!TextUtils.isEmpty(videoPath)
							&& !TextUtils.isEmpty(thumbnailPath)
							&& !videoPath.contains("http")) {
						if (!Tool.isEmpty(thumbnailPath)) {
							setMapImageKeyValue(map,
									"videoPhotoFileUrlJSONArray",
									thumbnailPath, "0");
						}
						if (!Tool.isEmpty(videoPath)) {
							setMapImageKeyValue(map, "videoFileUrlJSONArray",
									videoPath, "1");
						}
					}
					if (!Tool.isEmpty(headPicPath)) {
						setMapImageKeyValue(map, "resumePhotoFileUrlJSONArray",
								headPicPath, "0");
					}
					LogUtil.d("hl", "修改我的简历" + map);
					reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_80061));
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(map)));

					// final Map<String, List<FormFileBean>> mapFileBean = new
					// HashMap<String, List<FormFileBean>>();

					// if (!TextUtils.isEmpty(soundPath)
					// && !soundPath.contains("http")) {
					// // 音频
					// List<FormFileBean> listBeans = new
					// ArrayList<FormFileBean>();
					// listBeans.add(new FormFileBean(new File(soundPath),
					// "audio.mp3"));
					// mapFileBean.put("voice", listBeans);
					//
					// } else {
					// mapFileBean.put("voice", new ArrayList<FormFileBean>());
					// }
					// if (!TextUtils.isEmpty(videoPath)
					// && !TextUtils.isEmpty(thumbnailPath)
					// && !videoPath.contains("http")) {
					// // 视频
					// List<FormFileBean> listBeans = new
					// ArrayList<FormFileBean>();
					// listBeans.add(new FormFileBean(new File(thumbnailPath),
					// "photo.png"));
					// mapFileBean.put("videoPhoto", listBeans);
					//
					// List<FormFileBean> listBean = new
					// ArrayList<FormFileBean>();
					// listBean.add(new FormFileBean(new File(videoPath),
					// "video.mp4"));
					// mapFileBean.put("video", listBean);
					// } else {
					// mapFileBean.put("video", new ArrayList<FormFileBean>());
					// }
					// if (picPathList.size() > 0) {
					//
					// for (int i = 0; i < picPathList.size(); i++) {
					// Object imgPathString = picPathList.get(i);
					// if (imgPathString.getClass().getName()
					// .equals(String.class.getName())) {
					// List<FormFileBean> listBean = new
					// ArrayList<FormFileBean>();
					// listBean.add(new FormFileBean(new File(
					// (String) picPathList.get(i)), "image_"
					// + i + ".png"));
					// mapFileBean.put("photo", listBean);
					//
					// }
					// }
					// }

					httpPost(REQCODE_MODIFYRESUME,
							AppConfig.RESUME_REQUEST_MAPPING, reqBase, true);
				}
			}
			break;
		case R.id.layout_proskill: // 专业技法
			// // intent = new Intent(this, CommonListSelectActivity.class);
			// // intent.putExtra("tag", "proSkill");
			// // intent.putExtra("code", "proSkillcode");
			// //
			// // intent.putExtra("requestCode", PROFESSION_KNOWLEDGE);
			// // startActivityForResult(intent, PROFESSION_KNOWLEDGE);
			//
			// List<DataconfigEntity> pofessionlist = MKDataBase.getInstance()
			// .getWorkSkill();
			// alertDialog(pofessionlist, "专业技法", PROFESSION_KNOWLEDGE);
			// proSkillcode = strId;
			// Knowledge = strValue;
			// proSkillTXT.setText(Knowledge);

			intent = new Intent(this, MultiSelectActivity.class);
			intent.putExtra("type", MultiSelectActivity.TYPE_KNOWLEDGE);
			intent.putExtra("ids", proSkillcode);
			startActivityForResult(intent, PROFESSION_KNOWLEDGE);
			break;
		// case R.id.bossType_tv:
		// alertDialog(
		// MKDataBase.getInstance().getDataConfigList("BOSS_TYPE"),
		// "意向老板类型", BOSS_TYPE);
		// bossTypeValue = strValue;
		// bossTypeET.setText(bossTypeValue);
		// bossTypeId = strId;
		// break;
		// case R.id.shuaxin_text:// 刷新時間
		// MyDateTimePicker shuaDateTimePicker = new MyDateTimePicker(this,
		// Util.getTodayDate());
		// shuaDateTimePicker.datePicKDialog(shuaxin_text);
		// break;

		case R.id.layout_birthday:
			// MyDateTimePicker myDateTimePicker = new
			// MyDateTimePicker(this,Util.getTodayDate());
			// myDateTimePicker.datePicKDialog(birthdayText);
			new TimeSelectDialog(MrrckResumeActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							birthdayText.setText(time);
						}
					}).show();
			break;
		case R.id.layout_education:
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "EDUCATION");
			intent.putExtra("code", "EDUCATIONCODE");
			intent.putExtra("requestCode", EDUCATION);
			startActivityForResult(intent, EDUCATION);
			break;
		// case R.id.currentJobTX: // 现在岗位
		// startActivityForResult(new Intent(this,
		// SelectPositionActivity.class), JOB_POSITION);
		// break;
		case R.id.addVideoFL: // 录制视频
			if (null != videoPath && !"".equals(videoPath)) {
				intent = new Intent();
				intent.putExtra("mrrck_videoPath", videoPath);
				intent.setClass(this, TestVideoActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(this, ShootVideoActivity.class);
				startActivityForResult(intent, TO_SELECT_VIDEO);
			}

			break;
		case R.id.addAudioRL: // 录制语音
			if (null != soundPath && !"".equals(soundPath)) {
				intent = new Intent(this, TestAudioActivity.class);
				String soundpath = soundPath.substring(0,
						soundPath.lastIndexOf("."));
				intent.putExtra("filePath", soundpath);
				intent.putExtra("recordingTime", recordingTime + "");
				startActivityForResult(intent, 1);
			} else {
				intent = new Intent(this, RecordActivity.class);
				intent.putExtra(RecordActivity.BUNDLE_IS_PUBLISH, "1");
				startActivityForResult(intent, TAKE_SOUND);
			}

			break;
		/*
		 * case R.id.editAudioBTN: // 编辑语音
		 * 
		 * break;
		 */

		// case R.id.marryStateTExt:// 是否已婚
		// intent = new Intent(this, CommonListSelectActivity.class);
		// intent.putExtra("tag", "marry");
		// intent.putExtra("postion", "position");
		// intent.putExtra("requestCode", MARRY);
		// startActivityForResult(intent, MARRY);
		// break;
		// case R.id.requireStayET:// 是否要住宿舍
		// intent = new Intent(this, CommonListSelectActivity.class);
		// intent.putExtra("tag", "requireStay");
		// intent.putExtra("postion", "accommodation");
		// intent.putExtra("requestCode", MREQUIRESTAY);
		// startActivityForResult(intent, MREQUIRESTAY);
		// break;
		case R.id.layout_city:// 意向城市
			new WheelSelectCityDialog(MrrckResumeActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							marryStateET.setText(cityName);
							wantCityCode = cityCode + "";
						}

					}).show();
			break;
		case R.id.layout_intent:// 意向职位
			startActivityForResult(new Intent(this,
					SelectPositionActivity.class).putExtra("hasJobFlag", true),
					INTENTPOS);
			break;
		case R.id.layout_salary:// 期望薪水
			intent = new Intent(this, CommonListSelectActivity.class);
			intent.putExtra("tag", "ExpectSalayET");
			intent.putExtra("code", "Salaycode");
			intent.putExtra("requestCode", EXPECTSALAYNUM);
			startActivityForResult(intent, EXPECTSALAYNUM);
			break;
		case R.id.layout_addImage:// 上传 个人简历头像
			intent = new Intent(MrrckResumeActivity.this,
					SelectPictureActivity.class);
			intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
			intent.putExtra("MAX_NUM", 1);// 选择的张数
			startActivityForResult(intent, TAKE_PHOTO_IMG);
			break;

		default:
			break;
		}
	}

	private void alertDialog(List<DataconfigEntity> pofessionlist,
			String title, final int type) {
		final String[] pItems = new String[pofessionlist.size()];
		final String[] pItemsId = new String[pofessionlist.size()];

		String[] temp = null;
		boolean[] booleans = new boolean[pofessionlist.size()];
		if (type == PROFESSION_KNOWLEDGE) {
			if (!TextUtils.isEmpty(proSkillcode)) {
				temp = proSkillcode.split(",");
			}
		} else if (type == BOSS_TYPE) {
			if (!TextUtils.isEmpty(bossTypeId)) {
				temp = bossTypeId.split(",");
			}

		} else if (type == BENEFITS) {
			if (!TextUtils.isEmpty(fringeBenefitsId)
					&& !Tool.isEmpty(tv_reward.getText().toString())) {
				temp = fringeBenefitsId.split(",");
			}
		}

		multiChoiceID.clear();
		multiChoiceValue.clear();
		strId = "";
		strValue = "";

		if (null != temp) {

			for (int i = 0, size = pofessionlist.size(); i < size; i++) {
				pItems[i] = pofessionlist.get(i).getValue();
				pItemsId[i] = pofessionlist.get(i).getCodeId();
				for (String s : temp) {
					if (s.equals(pofessionlist.get(i).getCodeId())) {
						multiChoiceID.add(pofessionlist.get(i).getCodeId());
						multiChoiceValue.add(pofessionlist.get(i).getValue());
						booleans[i] = true;
						break;
					}
				}

			}
		} else {
			for (int i = 0, size = pofessionlist.size(); i < size; i++) {
				pItems[i] = pofessionlist.get(i).getValue();
				pItemsId[i] = pofessionlist.get(i).getCodeId();
				booleans[i] = false;
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				MrrckResumeActivity.this);

		builder.setTitle(title);
		// 设置多选项
		builder.setMultiChoiceItems(pItems, booleans,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1,
							boolean arg2) {
						// TODO Auto-generated method stub
						if (arg2) {
							multiChoiceID.add(pItemsId[arg1]);
							multiChoiceValue.add(pItems[arg1]);
							// String tip = "你选择的ID为" +
							// multiChoiceID.toString()+ ",值为" +
							// multiChoiceValue.toString();
							// Toast toast =
							// Toast.makeText(getApplicationContext(), tip,
							// Toast.LENGTH_SHORT);
							// toast.show();
						} else {
							multiChoiceID.remove(pItemsId[arg1]);
							multiChoiceValue.remove(pItems[arg1]);
							// String tip = "你选择的ID为" + multiChoiceID.toString()
							// + ",值为" + multiChoiceValue.toString();
							// Toast toast =
							// Toast.makeText(getApplicationContext(), tip,
							// Toast.LENGTH_SHORT);
							// toast.show();
						}
					}
				});
		// 设置确定按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Iterator it = multiChoiceID.iterator();
				while (it.hasNext()) {
					strId += it.next().toString() + ",";
				}
				Iterator it1 = multiChoiceValue.iterator();
				while (it1.hasNext()) {
					strValue += it1.next().toString() + ",";
				}
				// Toast toast = Toast.makeText(getApplicationContext(), "你选择了"
				// + strId.substring(0,strId.length()-1), Toast.LENGTH_LONG);
				// toast.show();
				// Toast toast1 = Toast.makeText(getApplicationContext(), "你选择了"
				// + strValue.substring(0,strValue.length()-1),
				// Toast.LENGTH_LONG);
				// toast1.show();
				// Knowledge = strValue.substring(0, strValue.length() - 1);
				if (!strValue.equals("")) {
					strValue = strValue.substring(0, strValue.length() - 1);
					strId = strId.substring(0, strId.length() - 1);
					if (type == PROFESSION_KNOWLEDGE) {
						proSkillcode = strId;
						Knowledge = strValue;
						proSkillTXT.setText(Knowledge);
					} else if (type == BOSS_TYPE) {
						bossTypeValue = strValue;
						bossTypeET.setText(bossTypeValue);
						bossTypeId = strId;
					} else if (type == BENEFITS) {
						fringeBenefits = strValue;
						tv_reward.setText(fringeBenefits);
						fringeBenefitsId = strId;
					}
				}
			}
		});
		// 设置取消按钮
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		AlertDialog mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	// 检验数据合法性
	private boolean isDateLegal() {
		String realName = realNameEdit.getText().toString();
		String birthday = birthdayText.getText().toString();
		String education = tv_education.getText().toString();
		String phone = et_phone.getText().toString();
		String valijobIntent = intentPosET.getText().toString();
		String marryState = marryStateET.getText().toString();
		String ExpectSalary = ExpectSalay.getText().toString();
		String valiReward = tv_reward.getText().toString();
		String workyear = tv_workyear.getText().toString();
		String proSkill = proSkillTXT.getText().toString();
		// String valiHonor = tv_glory.getText().toString();
		// String valiExperience = tv_experience.getText().toString();

		if ("".equals(realName)) {
			ToastUtil.showShortToast("请填写姓名，\n让同频的老板第一时间找到你~~");
			return false;
		}
		// else if ("".equals(birthday)) {
		// Toast.makeText(this, "请选择生日！", Toast.LENGTH_SHORT).show();
		// return false;
		// }
		// else if ("".equals(education)) {
		// ToastUtil.showShortToast("请选择学历");
		// return false;
		// }
		else if ("".equals(valijobIntent)) {
			ToastUtil.showShortToast("请选择求职岗位，\n让同频的老板第一时间找到你~~");
			return false;
		} else if ("".equals(ExpectSalary)) {
			ToastUtil.showShortToast("请选择期望薪资，\n让同频的老板第一时间找到你~~");
			return false;
		} else if ("".equals(workyear)) {
			ToastUtil.showShortToast("请选择工作年限，\n让同频的老板第一时间找到你~~");
			return false;
		} else if ("".equals(marryState)) {
			ToastUtil.showShortToast("请选择意向城市，\n让同频的老板第一时间找到你~~");
			return false;
		} else if ("".equals(phone)) {
			ToastUtil.showShortToast("请填写手机号码，\n让同频的老板第一时间找到你~~");
			return false;
		}

		// else if ("".equals(valiReward)) {
		// ToastUtil.showShortToast("请选择福利待遇");
		// return false;
		// } else if ("".equals(proSkill)) {
		// ToastUtil.showShortToast("请选择专业技能");
		// return false;
		// }
		// else if("".equals(valiHonor)){
		// ToastUtil.showShortToast("请填写个人荣誉");
		// return false;
		// }else if("".equals(valiExperience)){
		// ToastUtil.showShortToast("请填写个人履历");
		// return false;
		// }
		// else if ("-1".equals(changeStr)) {
		// ToastUtil.showShortToast("请选择目前状态");
		// return false;
		// }
		else {
			return true;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {

			case EDUCATION:
				educationCode = data.getStringExtra("EDUCATIONCODE");
				tv_education.setText(data.getStringExtra("EDUCATION"));
				break;
			case WORKTIME:
				workTimeText.setText(data.getStringExtra("workTime"));
				break;
			// case MARRY:
			// marryStateTExt.setText(data.getStringExtra("marry"));
			// postion = Integer.parseInt(data.getStringExtra("position")) + 1;
			//
			// break;
			// 专业技法
			// case PROFESSION_KNOWLEDGE:
			// proSkillTXT.setText(data.getStringExtra("proSkill"));
			// Knowledge = data.getStringExtra("proSkill");
			// proSkillcode = data.getStringExtra("proSkillcode");
			// proSkillTXT.setText(data.getStringExtra("proSkill"));
			// Knowledge = data.getStringExtra("proSkill");
			// proSkillcode = data.getStringExtra("proSkillcode");
			// break;
			case JOB_POSITION: // 现在岗位
				String result_value = data.getStringExtra("name");
				currentJobTX.setText(result_value);
				positionId = data.getIntExtra("id", 0) + "";
				int positionId = data.getIntExtra("groupId", 0);
				String type = data.getStringExtra("type");
				break;
			case TO_SELECT_VIDEO: // 录制视频
				videoPath = data.getStringExtra("videoPath");
				videoSeconds = data.getStringExtra("videoSeconds");

				videoView.setImageBitmap(getVideoImg(videoPath));

				String bitMapPath = data.getStringExtra("bitMapPath");
				thumbnailPath = bitMapPath;
				//
				// ReqBase reqBase1 = new ReqBase();
				// reqBase1.setHeader(new ReqHead(
				// AppConfig.BUSINESS_UPLOAD_MK_RESUME_MEDIA));
				// Map<String, Object> body1 = new HashMap<String, Object>();
				// body1.put("userId", AppContext.getInstance().getUserInfo()
				// .getId());
				// body1.put("fileSeconds", videoSeconds);
				// body1.put("title", "");
				// body1.put("remark", "");
				// body1.put("isPublic", 1);
				// reqBase1.setBody(JsonUtil.String2Object(JsonUtil
				// .hashMapToJson(body1)));
				// Map<String, List<FormFileBean>> mapFileBean1 = new
				// HashMap<String, List<FormFileBean>>();
				// List<FormFileBean> formFileBeans1 = new
				// ArrayList<FormFileBean>();
				// FormFileBean formFile1 = new FormFileBean(new File(
				// thumbnailPath), "photo.png");
				// formFileBeans1.add(formFile1);
				// mapFileBean1.put("photo", formFileBeans1);
				//
				// List<FormFileBean> formFileBeans2 = new
				// ArrayList<FormFileBean>();
				// FormFileBean formFile2 = new FormFileBean(new
				// File(videoPath),
				// "video.mp4");
				// formFileBeans2.add(formFile2);
				// mapFileBean1.put("video", formFileBeans2);
				// uploadFiles(REQCODE_MKRESUMEVEDIO,
				// AppConfig.RESUME_REQUEST_MAPPING, mapFileBean1,
				// reqBase1, true);

				// ResumeDataLogic.getInstance().uploadMKResumeMediaAttachWithUserId(mUserId,
				// 1, videoSeconds, "", "", videoPath, bitMapPath, 1, new
				// HttpCallback() {
				// @Override
				// public void onSuccess(Object body) {
				//
				//
				// videoView.setImageBitmap(getVideoImg(videoPath));//得到保存后的视频缩略图
				//
				// dialog.dismiss();
				// //ToastUtil.showShortToast(videoPath);
				//
				//
				// }
				//
				// @Override
				// public void onFailed(String error) {
				// dialog.dismiss();
				// ToastUtil.showShortToast(error);
				// }
				// });
				break;
			case TAKE_SOUND: // 录制音频
				if (data != null) {
					String soundFile = data.getStringExtra("file");
					recordingTime = data.getStringExtra("recordingTime");// 播放时间
					soundPath = soundFile;

					// ReqBase reqBase = new ReqBase();
					// reqBase.setHeader(new ReqHead(
					// AppConfig.BUSINESS_UPLOAD_MK_RESUME_MEDIA));
					// Map<String, Object> body = new HashMap<String, Object>();
					// body.put("userId", AppContext.getInstance().getUserInfo()
					// .getId());
					// body.put("fileSeconds", recordingTime);
					// body.put("title", "");
					// body.put("remark", "");
					// body.put("isPublic", 1);
					// reqBase.setBody(JsonUtil.String2Object(JsonUtil
					// .hashMapToJson(body)));
					// Map<String, List<FormFileBean>> mapFileBean = new
					// HashMap<String, List<FormFileBean>>();
					// List<FormFileBean> formFileBeans = new
					// ArrayList<FormFileBean>();
					// FormFileBean formFile = new FormFileBean(
					// new File(soundPath), "audio.mp3");
					// formFileBeans.add(formFile);
					// mapFileBean.put("voice", formFileBeans);
					// uploadFiles(REQCODE_MKRESUMEVOICE,
					// AppConfig.RESUME_REQUEST_MAPPING, mapFileBean,
					// reqBase, true);
				}

				break;
			case MREQUIRESTAY:// 是否要住宿
				requireStayET.setText(data.getStringExtra("requireStay"));
				accommodation = Integer.parseInt(data
						.getStringExtra("accommodation")) + 1;
				break;
			case INTENTPOS:// 意向职位
				String intent_value = data.getStringExtra("name");
				intentPosET.setText(intent_value);
				intentcodeId = data.getIntExtra("id", 0) + "";
				// intentcodeId = data.getIntExtra("groupId", 0) + "";
				String intentPostype = data.getStringExtra("type");
				break;
			case EXPECTSALAYNUM:// 期望薪水
				ExpectSalay.setText(data.getStringExtra("ExpectSalayET"));
				SalaycodeId = data.getStringExtra("Salaycode");
				break;
			case HOME:// 现居住地
				liveLocateET.setText(data.getStringExtra("cityName"));
				cityCode = data.getStringExtra("cityCode");
				break;
			case GETWORKAGE:// 工作年限
				tv_workyear
						.setText("工作年限：" + data.getStringExtra("GETWORKAGE"));
				work_code = data.getStringExtra("GETWORKAGECODE");
				break;
			case TAKE_PHOTO_IMG:
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPics = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				if (pics != null) {// 多选图片返回
					for (int i = 0; i < pics.size(); i++) {
						CompressPic(pics.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(photoPath);
				}
				break;
			// 修改视频
			case EDITVIDEO:
				initData();
				break;
			// 修改音频
			case EDITVOICE:
				initData();
				break;
			case BENEFITS:
				fringeBenefits = data.getStringExtra("selectValues");
				tv_reward.setText(fringeBenefits);
				fringeBenefitsId = data.getStringExtra("selectIds");
				break;
			case PROFESSION_KNOWLEDGE:
				proSkillcode = data.getStringExtra("selectIds");
				Knowledge = data.getStringExtra("selectValues");
				proSkillTXT.setText(Knowledge);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param photoPath
	 */
	private void CompressPic(String photoPath) {
		showProgressDialog("图片压缩中...");
		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) {
				headPicPath = result;
				dismissProgressDialog();
				userHeadIMG.setUrlOfImage("file://" + result);
				super.onPostExecute(result);
			}

			@Override
			protected String doInBackground(String... arg0) {
				String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
				LogUtil.d("hl", "返回拍照路径压缩__" + photoPath);
				return photoPath;
			}

		}.execute(photoPath);
	}

	/**
	 * 获取视频缩略图*
	 */
	@SuppressLint("NewApi")
	public Bitmap getVideoImg(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				// retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_mrrck_resume;
	}

	@Override
	public void initView() {
		findViewById(R.id.right_txt_title).setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		radioGroup = (RadioGroup) findViewById(R.id.change_job);
		radioButtonomne = (RadioButton) findViewById(R.id.change_good_job);// 观望好的机会
		radioButtonomne
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeStr = 2 + "";
						}

					}
				});
		find_good_job = (RadioButton) findViewById(R.id.find_good_job);// 正在找工作
		find_good_job
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeStr = 1 + "";
						}

					}
				});
		not_find_job = (RadioButton) findViewById(R.id.not_find_job);// 暂不考虑新工作
		not_find_job
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							changeStr = 0 + "";
						}

					}
				});
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		userHeadIMG = new MySimpleDraweeView(MrrckResumeActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(userHeadIMG, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout_addImage.setOnClickListener(this);

		userNameTXT = (TextView) findViewById(R.id.userNameTXT);// 谁谁的简历
		userNameTXT.setText(AppContext.getInstance().getUserInfo()
				.getNickName());
		iv_recognize = (ImageView) findViewById(R.id.iv_recognize);// 认证

		layout_info = findViewById(R.id.layout_info);
		// usergender = (ImageView) findViewById(R.id.usergender);// 用户性别
		userage = (TextView) findViewById(R.id.userage);// 用户年龄
		iv_major = (ImageView) findViewById(R.id.iv_major);
		major = (TextView) findViewById(R.id.major);// 职业
		work_year = (TextView) findViewById(R.id.workyear);// 工作年限

		addVideoFL = (FrameLayout) findViewById(R.id.addVideoFL);
		addVideoFL.setOnClickListener(this);

		LinearLayout layout_videoView = (LinearLayout) findViewById(R.id.layout_videoView);
		videoView = new MySimpleDraweeView(MrrckResumeActivity.this);
		layout_videoView.addView(videoView, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addAudioRL = (RelativeLayout) findViewById(R.id.addAudioRL);
		/*
		 * editAudioBTN = (Button) findViewById(R.id.editAudioBTN);
		 * editAudioBTN.setOnClickListener(this);
		 */
		addAudioRL.setOnClickListener(this);

		// 姓名
		realNameEdit = (EditText) findViewById(R.id.usernameET);

		group_sex = (RadioGroup) findViewById(R.id.group_sex);
		group_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.btn_gender_male:
					genderpostion = 1;
					break;
				case R.id.btn_gender_female:
					genderpostion = 2;
					break;
				default:
					break;
				}
			}
		});

		group_marry = (RadioGroup) findViewById(R.id.group_marry);
		group_marry.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.btn_marry:
					postion = 2;
					break;
				case R.id.btn_unmarried:
					postion = 1;
					break;
				default:
					break;
				}
			}
		});

		// 出生日期
		birthdayText = (TextView) findViewById(R.id.birthdayTXT);
		layout_birthday = findViewById(R.id.layout_birthday);
		layout_birthday.setOnClickListener(this);
		// 学历
		layout_education = findViewById(R.id.layout_education);
		layout_education.setOnClickListener(this);
		tv_education = (TextView) findViewById(R.id.tv_education);

		et_phone = (EditText) findViewById(R.id.et_phone);

		tv_verify = (TextView) findViewById(R.id.tv_verify);
		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (et_phone
						.getText()
						.toString()
						.equals(AppContext.getInstance().getUserInfo()
								.getPhone())) {
					tv_verify.setVisibility(View.VISIBLE);
				} else {
					tv_verify.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		group_jobtime = (RadioGroup) findViewById(R.id.group_jobtime);
		group_jobtime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.btn_alltime:
					jobType = "1";
					break;
				case R.id.btn_parttime:
					jobType = "2";
					break;
				case R.id.btn_practice:
					jobType = "3";
					break;
				default:
					break;
				}
			}
		});
		// 意向职位
		intentPosET = (TextView) findViewById(R.id.intentPosET);
		layout_intent = findViewById(R.id.layout_intent);
		layout_intent.setOnClickListener(this);
		// 意向城市
		marryStateET = (TextView) findViewById(R.id.marryStateET);
		layout_city = findViewById(R.id.layout_city);
		layout_city.setOnClickListener(this);

		// 期望薪水
		ExpectSalay = (TextView) findViewById(R.id.ExpectSalayET);
		layout_salary = findViewById(R.id.layout_salary);
		layout_salary.setOnClickListener(this);

		layout_reward = findViewById(R.id.layout_reward);
		layout_reward.setOnClickListener(this);
		tv_reward = (TextView) findViewById(R.id.tv_reward);

		// 意向老板类型
		// bossTypeET = (TextView) findViewById(R.id.bossType_tv);
		// bossTypeET.setOnClickListener(this);

		layout_workyear = findViewById(R.id.layout_workyear);
		layout_workyear.setOnClickListener(this);
		tv_workyear = (TextView) findViewById(R.id.tv_workyear);

		// 专业技法
		proSkillTXT = (TextView) findViewById(R.id.proSkillTXT);
		// proSkillTXT.setOnClickListener(this);
		layout_proskill = findViewById(R.id.layout_proskill);
		layout_proskill.setOnClickListener(this);

		group_letter = (RadioGroup) findViewById(R.id.group_letter);
		group_letter.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.btn_have:
					isRecom = "1";
					break;
				case R.id.btn_none:
					isRecom = "0";
					break;
				default:
					break;
				}
			}
		});

		layout_glory = findViewById(R.id.layout_glory);
		layout_glory.setOnClickListener(this);
		tv_glory = (EditText) findViewById(R.id.tv_glory);

		tv_experience = (TextView) findViewById(R.id.tv_experience);

		editVideoTXT = (TextView) findViewById(R.id.editVideoTXT);
		editVideoTXT.setOnClickListener(this);
		editVoiceTXT = (TextView) findViewById(R.id.editVoiceTXT);
		editVoiceTXT.setOnClickListener(this);

		// infoAudioTXT = (TextView) findViewById(R.id.infoAudioTXT);

		// isPublic = (ToggleButton) findViewById(R.id.isPublic_toggleButton);
		// isPublic.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton compoundButton,
		// boolean b) {
		// if (b) {
		// isPublicStr = "1";
		// } else {
		// isPublicStr = "0";
		// }
		// }
		// });
	}

	@Override
	public void initValue() {
		// 获取数据
		initData();

	}

	@Override
	public void bindListener() {
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case REQCODE_GETRESUME:
			// JSONObject response = (JSONObject) body;

			ReqBase resp = (ReqBase) arg0;
			String temp = resp.getBody().toString();
			LogUtil.e("resume:" + temp);
			String tempJson = JsonUtil.String2Object(temp).get("resumeEntity")
					.toString();
			// if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil
			// .getJSONType(tempJson)) {
			ResumeCenterEntity resume = (ResumeCenterEntity) JsonUtil
					.jsonToObj(ResumeCenterEntity.class, tempJson);
			// 网络获取数
			if (!Tool.isEmpty(resume)) {
				getWebData(resume);
			}

			// }
			break;
		case REQCODE_MKRESUMEVOICE:
			// LogUtil.e((String) arg0);
			break;
		case REQCODE_MKRESUMEVEDIO:
			// LogUtil.e((String) arg0);
			videoView.setUrlOfImage("file://" + videoPath);
			break;
		case REQCODE_UPDATERESUME:// 提交
			try {
				ReqBase resp1 = (ReqBase) arg0;
				LogUtil.e("hl", "认证__" + resp1.getBody());
				String temp1 = resp1.getBody().toString();
				LogUtil.e(temp1);
				String tempResumeId = JsonUtil.String2Object(temp1)
						.get("resumeId").getAsInt()
						+ "";
				AppContext.getInstance().getUserInfo()
						.setResumeId(Integer.parseInt(tempResumeId));
				AppContext.getInstance().setLocalUser(
						AppContext.getInstance().getUserInfo());
				if (resp1.getBody().get("voiceUploadFileEntity") != null
						&& (resp1.getBody().get("voiceUploadFileEntity") + "")
								.length() > 2) {
					// 上传声音
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileUrlJSONArray",
							resp1.getBody().get("voiceUploadFileEntity")
									.getAsJsonArray());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
					req.setBody(JsonUtil.Map2JsonObj(map));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
					FormFileBean mainFfb = new FormFileBean();
					mainFfb.setFileName("audio.mp3");
					mainFfb.setFile(new File(soundPath));
					details_FileBeans.add(mainFfb);
					mapFileBean.put("file", details_FileBeans);
					uploadResFiles(3001, AppConfig.PUBLICK_UPLOAD, mapFileBean,
							req, true);
				}
				// 上传视频缩略图
				if (resp1.getBody().get("videoPhotoUploadFileEntity") != null
						&& (resp1.getBody().get("videoPhotoUploadFileEntity") + "")
								.length() > 2) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileUrlJSONArray",
							resp1.getBody().get("videoPhotoUploadFileEntity")
									.getAsJsonArray());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
					req.setBody(JsonUtil.Map2JsonObj(map));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
					FormFileBean mainFfb = new FormFileBean();
					mainFfb.setFileName("photo.png");
					mainFfb.setFile(new File(thumbnailPath));
					details_FileBeans.add(mainFfb);
					mapFileBean.put("file", details_FileBeans);
					uploadResFiles(3002, AppConfig.PUBLICK_UPLOAD, mapFileBean,
							req, true);
				}
				// 上传视频
				if (resp1.getBody().get("videoUploadFileEntity") != null
						&& (resp1.getBody().get("videoUploadFileEntity") + "")
								.length() > 2) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileUrlJSONArray",
							resp1.getBody().get("videoUploadFileEntity")
									.getAsJsonArray());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
					req.setBody(JsonUtil.Map2JsonObj(map));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
					FormFileBean mainFfb = new FormFileBean();
					mainFfb.setFileName("video.mp4");
					mainFfb.setFile(new File(videoPath));
					details_FileBeans.add(mainFfb);
					mapFileBean.put("file", details_FileBeans);
					uploadResFiles(3003, AppConfig.PUBLICK_UPLOAD, mapFileBean,
							req, true);
				}
				// 上传简历头像
				if (resp1.getBody().get("resumePhotoUploadFileEntity") != null
						&& (resp1.getBody().get("resumePhotoUploadFileEntity") + "")
								.length() > 2) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fileUrlJSONArray",
							resp1.getBody().get("resumePhotoUploadFileEntity")
									.getAsJsonArray());
					req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
					req.setBody(JsonUtil.Map2JsonObj(map));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
					FormFileBean mainFfb = new FormFileBean();
					mainFfb.setFileName("avatar.png");
					mainFfb.setFile(new File(headPicPath));
					details_FileBeans.add(mainFfb);
					mapFileBean.put("file", details_FileBeans);
					uploadResFiles(3004, AppConfig.PUBLICK_UPLOAD, mapFileBean,
							req, true);

				}

				ToastUtil.showShortToast(resp1.getHeader().getRetMessage());
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case REQCODE_MODIFYRESUME:// 修改
			// ReqBase req = (ReqBase) arg0;
			// String tempbody = req.getBody().toString();
			// if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil
			// .getJSONType(tempbody)) {
			// ResumeInformation resume1 = (ResumeInformation) JsonUtil
			// .jsonToObj(ResumeInformation.class, tempbody);
			// // 网络获取数
			// ResumeMrrck resumeMrrck = resume1.getResumeMrrck();
			// // UserData userData = AppData.getInstance().getLoginUser();
			// // resumeMrrckId = resumeMrrck.getId();
			// // UserDAO userDAO = new UserDAO(getApplicationContext());
			// //
			// userDAO.updateLoginUser(userDAO.convertDataToEntity(userData));
			// }
			ReqBase resp1 = (ReqBase) arg0;
			Log.e("wangke", resp1.getBody().toString());
			if (resp1.getBody().get("voiceUploadFileEntity") != null
					&& (resp1.getBody().get("voiceUploadFileEntity") + "")
							.length() > 2) {
				// 上传声音
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray",
						resp1.getBody().get("voiceUploadFileEntity")
								.getAsJsonArray());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("audio.mp3");
				mainFfb.setFile(new File(soundPath));
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(3001, AppConfig.PUBLICK_UPLOAD, mapFileBean,
						req, true);
			}
			// 上传视频缩略图
			if (resp1.getBody().get("videoPhotoUploadFileEntity") != null
					&& (resp1.getBody().get("videoPhotoUploadFileEntity") + "")
							.length() > 2) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray",
						resp1.getBody().get("videoPhotoUploadFileEntity")
								.getAsJsonArray());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("photo.png");
				mainFfb.setFile(new File(thumbnailPath));
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(3002, AppConfig.PUBLICK_UPLOAD, mapFileBean,
						req, true);
			}
			// 上传视频
			if (resp1.getBody().get("videoUploadFileEntity") != null
					&& (resp1.getBody().get("videoUploadFileEntity") + "")
							.length() > 2) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray",
						resp1.getBody().get("videoUploadFileEntity")
								.getAsJsonArray());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("video.mp4");
				mainFfb.setFile(new File(videoPath));
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(3003, AppConfig.PUBLICK_UPLOAD, mapFileBean,
						req, true);
			}
			// 上传简历头像
			if (resp1.getBody().get("resumePhotoUploadFileEntity") != null
					&& (resp1.getBody().get("resumePhotoUploadFileEntity") + "")
							.length() > 2) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray",
						resp1.getBody().get("resumePhotoUploadFileEntity")
								.getAsJsonArray());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("avatar.png");
				mainFfb.setFile(new File(headPicPath));
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(3004, AppConfig.PUBLICK_UPLOAD, mapFileBean,
						req, true);

			}
			ToastUtil.showShortToast(resp1.getHeader().getRetMessage());
			setResult(RESULT_OK);
			finish();
			break;
		default:
			if (requestCode > 3000) {

			}
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case REQCODE_GETRESUME:
			ToastUtil.showShortToast("获取简历信息失败！");
			break;
		case REQCODE_UPDATERESUME:
			ToastUtil.showShortToast("提交简历失败！");
			break;
		case REQCODE_MODIFYRESUME:
			ToastUtil.showShortToast("修改简历信息失败！");
			break;

		default:
			break;
		}
	}

	/**
	 * 图片上传添加key
	 * 
	 * @param map
	 * @param key
	 * @param picPath
	 */
	private void setMapImageKeyValue(Map<String, Object> map, String key,
			String picPath, String fileType) {
		List<UploadImg> imgList = new ArrayList<UploadImg>();
		if (!Tool.isEmpty(picPath)) {
			UploadImg ui = new UploadImg();
			ui.setFileThumb(picPath.substring(picPath.lastIndexOf(".") + 1,
					picPath.length()));
			ui.setFileType(fileType);
			ui.setFileUrl("");
			imgList.add(ui);
		}
		map.put(key, JsonUtil.listToJsonArray(imgList));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void finishWhenTip() {
		String tipMsg = "";
		String rightBtnTxt = "";
		if (-1 == AppContext.getInstance().getUserInfo().getResumeId()) {
			tipMsg = "是否放弃本次创建?";
			rightBtnTxt = "继续创建";
		} else {
			tipMsg = "是否放弃本次编辑?";
			rightBtnTxt = "继续编辑";
		}
		final HintDialogwk commonDialog = new HintDialogwk(
				MrrckResumeActivity.this,  tipMsg, "放弃", rightBtnTxt);
		commonDialog.show();
		commonDialog
				.setClicklistener(new HintDialogwk.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						finish();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
	}

}
