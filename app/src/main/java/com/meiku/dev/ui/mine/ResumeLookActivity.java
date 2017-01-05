package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ResumeCenterEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.MrrckResumeActivity;
import com.meiku.dev.ui.morefun.TestAudioActivity;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.TopTitleBar;
import com.umeng.analytics.MobclickAgent;

public class ResumeLookActivity extends BaseActivity implements OnClickListener {

	public static final int REQCODE_GETRESUME = 10001; // 获取个人简历
	public static final int REQCODE_MKRESUMEVOICE = 4001;// 音频简历
	public static final int REQCODE_MKRESUMEVEDIO = 4002;// 视频简历

	public static final int GENDER = 0;// 性别
	public static final int BIRTHDAY = 1;// 出生日期
	public static final int JOB = 2;// 现在岗位
	public static final int EDUCATION = 4;// 最高学历
	public static final int WORKTIME = 5;// 美业职龄
	public static final int MARRY = 6;// 婚姻状况
	public static final int JOB_POSITION = 7; // 现在岗位
	public static final int TAKE_VIDEO = 8; // 录制视频
	public static final int EDITVIDEO = 17;// 修改视频
	public static final int EDITVOICE = 18;// 修改音频
	public static final int BOSS_TYPE = 19;
	public static final int requestupdate = 30;

	View layout_info, layout_birthday, layout_education, layout_intent,
			layout_city, layout_salary, layout_reward, layout_workyear,
			layout_proskill, layout_glory;
	ImageView usergender, iv_major;

	TextView major, tv_education, tv_reward, tv_workyear, tv_experience;
	TextView tv_glory;

	HashSet<String> multiChoiceID = new HashSet<String>();
	HashSet<String> multiChoiceValue = new HashSet<String>();
	private TextView et_phone;
	private TextView submit;// 保存按钮

	private TextView realNameEdit;// 真实姓名

	private LinearLayout genderView;// 性别

	private LinearLayout birthdayView;// 出生日期

	private TextView birthdayText;// 出生日期

	private LinearLayout jobView;// 现在岗位
	private TextView jobText;// 现在岗位

	private LinearLayout homeView;// 现居地
	private TextView homeText;// 现居地
	private TextView tv_jobtime;// 工作性质
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
	private TextView tujianxinTV;
	private TextView liveLocateET;// 居住地
	private TextView marryStateTExt;// 婚姻状况
	private TextView currentJobTX;// 目前岗位
	private TextView intentPosET;// 意向职位;
	private TextView marryStateET;// 意向城市
	private TextView usermarrytv;// 是否结婚
	private TextView bossTypeET;// 意向老板类型
	private String bossTypeId = "";
	private String bossTypeValue = "";

	private TextView requireStayET;// 是否要求住宿
	private TextView shuaxin_text;// 刷新时间

	private TextView userage;// 用户年龄

	private TextView ExpectSalay;// 期望月薪
	private TextView usersexTV;// 性别
	private TextView tv_gongzuostatue;// 工作状态
	private TextView work_year;// 工作年限
	private TextView userNameTXT;// 用户名字
	private LinearLayout lin_userHeadIMG;// 用户头像
	private ToggleButton isPublic;// 是否公开
	/**
	 * 添加视频
	 */
	private FrameLayout addVideoFL;
	private LinearLayout lin_videoView;
	private String videoPath = null;

	/**
	 * 添加录音
	 */
	private RelativeLayout addAudioRL;
	private String soundPath = null;// 录音路径
	private String recordingTime;// 录音时间
	private String videoSeconds;
	// 目前状况
	String changeStr = "1";

	private String strId;
	private String strValue;

	private String educationCode;// 学历
	private String SalaycodeId;// 期望薪水
	private String intentcodeId;// 求职意向的ID
	private String cityCode;// 城市code
	private String wantCityCode;// 意向城市
	private String positionId;
	private int accommodation;// 是否住宿
	private String work_code;
	private String wordResumeId;// 修改的時候提交的id
	private String resumeId;// 修改的時候提交的id
	private String Knowledge;// 知识
	private TopTitleBar toptotile_apply_for_job;
	private String isPublicStr = "1"; // 0不公开 1公开
	private String isPublish;
	String thumbnailPath = null;

	String resumeMrrckId;

	private int mUserId;
	private TextView infoAudioTXT;
	private List<Object> picPathList = new ArrayList<Object>();// 选择的图片路径
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

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_resumelook;
	}

	private void initData() {
		String getresumeId = AppContext.getInstance().getUserInfo()
				.getResumeId()
				+ "";

		mUserId = AppContext.getInstance().getUserInfo().getId();

		if ("-1".equals(getresumeId)) {
			et_phone.setText(AppContext.getInstance().getUserInfo().getPhone());
			layout_info.setVisibility(View.INVISIBLE);
			MyRectDraweeView userHeadIMG = new MyRectDraweeView(ResumeLookActivity.this);
			lin_userHeadIMG.removeAllViews();
			lin_userHeadIMG.addView(userHeadIMG, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
		tv_workyear.setText(r.getJobAgeName());
		tv_glory.setText(r.getHonor());
		et_phone.setText(r.getPhone());// 获取手机号码
		realNameEdit.setText(r.getRealName());// 真实姓名
		intentPosET.setText(r.getLikeJobs());// 意向职位;
		intentcodeId = r.getLikeJobsId();// 意向职位
		wantCityCode = r.getLikeCitysCode();// 意向城市
		marryStateET.setText(r.getLikeCitys());// 意向城市
		ExpectSalay.setText(r.getExpectSalaryName());// 期望月薪
		SalaycodeId = r.getExpectSalary() + "";// 意向薪资
		fringeBenefits = r.getFringeBenefits();
		fringeBenefitsId = r.getFringeBenefitsId();
		tv_reward.setText(fringeBenefits);

		if ("1".equals(r.getGender())) {
			Drawable drawable = ContextCompat.getDrawable(
					ResumeLookActivity.this, R.drawable.nan_white);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			userage.setCompoundDrawables(drawable, null, null, null);
			userage.setBackgroundResource(R.drawable.nanxing);
			usersexTV.setText("男");
		} else {
			Drawable drawable = ContextCompat.getDrawable(
					ResumeLookActivity.this, R.drawable.nv_white);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			userage.setCompoundDrawables(drawable, null, null, null);
			userage.setBackgroundResource(R.drawable.nvxing);
			usersexTV.setText("女");
		}
		if ("2".equals(r.getIsMarry())) {
			usermarrytv.setText("已婚");
		} else {
			usermarrytv.setText("未婚");
		}
		birthdayText.setText(r.getBirthDate());
		tv_education.setText(r.getEducationName());
		educationCode = r.getEducation() + "";
		if (1 == r.getJobType()) {
			tv_jobtime.setText("全职");
		} else if (2 == r.getJobType()) {
			tv_jobtime.setText("兼职");
		} else {
			tv_jobtime.setText("实习");
		}
		if (r.getIsRecommend() == 1) {
			tujianxinTV.setText("有");
		} else {
			tujianxinTV.setText("无");
		}
		tv_experience.setText(r.getPersonalResume());

		// resumeId = resume1.getId();

		if ("0".equals(r.getResumeStatus())) {
			tv_gongzuostatue.setText("暂不考虑新工作");
		} else if ("1".equals(r.getResumeStatus())) {
			tv_gongzuostatue.setText("正在找工作");
		} else {
			tv_gongzuostatue.setText("观望好的机会");
		}
		BitmapUtils bitmapUtils = new BitmapUtils(ResumeLookActivity.this);
		bitmapUtils.display(iv_major, r.getClientPositionIcon());
		Knowledge = r.getKnowledge();
		proSkillTXT.setText(Knowledge);
		proSkillcode = r.getKnowledgeId();// 专业知识

		changeStr = r.getResumeStatus();// 简历状态0:暂不考虑新工作 1:正在找工作 2:观望好的机会
		if (!TextUtils.isEmpty(r.getVoiceResume())) {
			soundPath = r.getVoiceResume();
			recordingTime = r.getVoiceSeconds() + "";
		}
		if (!TextUtils.isEmpty(r.getVideoResume())) {
			videoPath = r.getVideoResume();
			videoSeconds = r.getVideoSeconds() + "";
		}
		MyRectDraweeView userHeadIMG = new MyRectDraweeView(ResumeLookActivity.this);
		lin_userHeadIMG.removeAllViews();
		lin_userHeadIMG.addView(userHeadIMG, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		if (!TextUtils.isEmpty(r.getResumePhoto())) {
			userHeadIMG.setUrlOfImage(r.getResumePhoto());
		} else {

			userHeadIMG.setUrlOfImage(AppContext.getInstance().getUserInfo()
					.getClientHeadPicUrl()
					+ ConstantKey.THUMB);

		}
		MySimpleDraweeView videoView = new MySimpleDraweeView(ResumeLookActivity.this);
		lin_videoView.removeAllViews();
		lin_videoView.addView(videoView, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		videoView.setUrlOfImage(r.getVideoPhoto());

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.layout_reward:

			break;
		case R.id.layout_workyear:

			break;
		case R.id.goback:
			finish();
			break;

		case R.id.right_txt_title: // 编辑
			if (!NetworkTools.isNetworkAvailable(ResumeLookActivity.this)) {
				ToastUtil.showShortToast(getResources().getString(
						R.string.netNoUse));
				return;
			}
			Intent updateIntent = new Intent(ResumeLookActivity.this,
					MrrckResumeActivity.class);
			startActivityForResult(updateIntent, requestupdate);
			break;
		case R.id.layout_proskill: // 专业技法

			break;

		case R.id.layout_birthday:
			break;
		case R.id.layout_education:

			break;
		case R.id.addVideoFL: // 录制视频
			if (null != videoPath && !"".equals(videoPath)) {
				intent = new Intent();
				intent.putExtra("mrrck_videoPath", videoPath);
				intent.setClass(this, TestVideoActivity.class);
				startActivity(intent);
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
			}

			break;

		case R.id.layout_city:// 意向城市

			break;
		case R.id.layout_intent:// 意向职位

			break;
		case R.id.layout_salary:// 期望薪水

			break;
		case R.id.lin_userHeadIMG:// 上传 个人简历头像

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case requestupdate:
				initData();
				break;
			default:
				break;
			}
		}
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
	public void initView() {
		tv_gongzuostatue = (TextView) findViewById(R.id.tv_gongzuostatue);
		tujianxinTV = (TextView) findViewById(R.id.tujianxinTV);
		tv_jobtime = (TextView) findViewById(R.id.tv_jobtime);
		usermarrytv = (TextView) findViewById(R.id.usermarrytv);
		usersexTV = (TextView) findViewById(R.id.usersexTV);
		submit = (TextView) findViewById(R.id.right_txt_title);
		submit.setTextColor(Color.parseColor("#000000"));
		submit.setBackgroundDrawable(null);
		submit.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
		toptotile_apply_for_job = (TopTitleBar) findViewById(R.id.toptotile_apply_for_job);// 标题
		lin_userHeadIMG = (LinearLayout) findViewById(R.id.lin_userHeadIMG);
		lin_userHeadIMG.setOnClickListener(this);

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
		lin_videoView = (LinearLayout) findViewById(R.id.lin_videoView);
		addAudioRL = (RelativeLayout) findViewById(R.id.addAudioRL);
		/*
		 * editAudioBTN = (Button) findViewById(R.id.editAudioBTN);
		 * editAudioBTN.setOnClickListener(this);
		 */
		addAudioRL.setOnClickListener(this);

		// 姓名
		realNameEdit = (TextView) findViewById(R.id.usernameET);

		// 出生日期
		birthdayText = (TextView) findViewById(R.id.birthdayTXT);
		layout_birthday = findViewById(R.id.layout_birthday);
		layout_birthday.setOnClickListener(this);
		// 学历
		layout_education = findViewById(R.id.layout_education);
		layout_education.setOnClickListener(this);
		tv_education = (TextView) findViewById(R.id.tv_education);

		et_phone = (TextView) findViewById(R.id.et_phone);

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

		layout_glory = findViewById(R.id.layout_glory);
		layout_glory.setOnClickListener(this);
		tv_glory = (TextView) findViewById(R.id.tv_glory);

		tv_experience = (TextView) findViewById(R.id.tv_experience);

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
			MySimpleDraweeView videoView = new MySimpleDraweeView(ResumeLookActivity.this);
			lin_videoView.removeAllViews();
			lin_videoView.addView(videoView, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			videoView.setUrlOfImage("file://" + videoPath);
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case ConstantKey.REQCODE_NONET:// 网络断开
			finish();
			break;
		default:
			ToastUtil.showShortToast("获取数据失败！");
			finish();
			break;
		}
	}
}
