package com.meiku.dev.ui.myshow;

import org.apache.http.util.TextUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.ShootVideoActivity;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;

public class EnrollVedioActivity extends BaseActivity implements
		OnClickListener {
	private final int reqCodeFive = 500;
	private MyGridView gv_show;
	private EditText et_name, et_phone, et_workyear, et_showname,
			et_declaration, et_company;
	private TextView tv_name, tv_birthday, tv_phone, tv_city;
	private RadioGroup group_sex;
	private LinearLayout layout_birthday, layout_city;
	private Button btn_submit;

	private String birthday;
	private String cityCode;
	private String gender = "2";

	private FrameLayout layout_vedio;
	private String videoPath;
	private String videoSeconds;
	private ImageView iv_vedio;
	private String thumbnailPath;
	private TextView tv_addVedio;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_enroll_vedio;
	}

	@Override
	public void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		tv_name = (TextView) findViewById(R.id.tv_name);
		layout_birthday = (LinearLayout) findViewById(R.id.layout_birthday);
		tv_birthday = (TextView) findViewById(R.id.tv_birthday);
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		et_workyear = (EditText) findViewById(R.id.et_workyear);
		layout_city = (LinearLayout) findViewById(R.id.layout_city);
		tv_city = (TextView) findViewById(R.id.tv_city);
		et_showname = (EditText) findViewById(R.id.et_showname);
		et_declaration = (EditText) findViewById(R.id.et_declaration);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		group_sex = (RadioGroup) findViewById(R.id.group_sex);
		et_company = (EditText) findViewById(R.id.et_company);

		layout_vedio = (FrameLayout) findViewById(R.id.layout_vedio);
		iv_vedio = (ImageView) findViewById(R.id.iv_vedio);
		tv_addVedio = (TextView) findViewById(R.id.tv_addVedio);
	}

	@Override
	public void initValue() {

	}

	@Override
	public void bindListener() {
		layout_birthday.setOnClickListener(this);
		layout_city.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		group_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.btn_gender_male:
					gender = "1";
					break;
				case R.id.btn_gender_female:
					gender = "2";
					break;
				default:
					break;
				}
			}
		});
		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (et_phone
						.getText()
						.toString()
						.equals(AppContext.getInstance().getUserInfo()
								.getPhone())) {
					tv_phone.setVisibility(View.VISIBLE);
				} else {
					tv_phone.setVisibility(View.GONE);
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
		et_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (et_phone
						.getText()
						.toString()
						.equals(AppContext.getInstance().getUserInfo()
								.getRealName())) {
					tv_name.setVisibility(View.VISIBLE);
				} else {
					tv_name.setVisibility(View.GONE);
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
		layout_vedio.setOnClickListener(this);
		tv_addVedio.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_birthday:
			new TimeSelectDialog(EnrollVedioActivity.this,
					new TimeSelectDialog.CallBackListener() {

						@Override
						public void CallBackOfTimeString(String time) {
							birthday = time;
							tv_birthday.setText(time);
						}
					}).show();
			break;
		case R.id.layout_city:
			new WheelSelectCityDialog(EnrollVedioActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_city.setText(cityName);
							EnrollVedioActivity.this.cityCode = cityCode + "";
						}

					}).show();
			break;
		case R.id.btn_submit:
			if (validate()) {
				upload();
			}
			break;
		case R.id.layout_vedio:
			if (null != videoPath && !"".equals(videoPath)) {
				Intent intent = new Intent();
				intent.putExtra("mrrck_videoPath", videoPath);
				intent.setClass(this, TestVideoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, ShootVideoActivity.class);
				startActivityForResult(intent, reqCodeTwo);
			}
			break;
		case R.id.tv_addVedio:
			Intent intent = new Intent(this, ShootVideoActivity.class);
			startActivityForResult(intent, reqCodeTwo);
			break;
		default:
			break;
		}
	}

	private void upload() {
		// ReqBase reqBase = new ReqBase();
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("userId", AppContext.getInstance().getUserInfo()
		// .getId());
		// map.put("name",et_name.getText().toString());
		// map.put("categoryId", topicId);
		// map.put("provinceCode",
		// MrrckApplication.getInstance().provinceCode);
		// map.put("cityCode", MrrckApplication.getInstance().cityCode);
		// map.put("attachmentNum", 1);
		// map.put("activeType", activeType);
		// if (!Tool.isEmpty(videoPath) && activeType.equals("1")) {// 视频作品贴
		// MediaPlayer mediaPlayer = new MediaPlayer();
		// try {
		// mediaPlayer.setDataSource(videoPath);
		// mediaPlayer.prepare();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// int duration = mediaPlayer.getDuration();
		// int fileSeconds = duration / 1000;
		// map.put("fileSeconds", fileSeconds);
		// }
		// reqBase.setHeader(new ReqHead(
		// AppConfig.BUSINESS_VOTE_UPLOAD));
		// reqBase.setBody(JsonUtil.String2Object(JsonUtil
		// .hashMapToJson(map)));
		// LogUtil.d("hl", "报名_请求=" + map);
		//
		// Map<String, List<FormFileBean>> mapFileBean = new HashMap<String,
		// List<FormFileBean>>();
		// if (activeType.equals("0")) {// 图片作品贴
		// List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
		// FormFileBean formFile = new FormFileBean(new File(
		// workPicPath), "workphoto.png");
		// formFileBeans.add(formFile);
		// mapFileBean.put("photo", formFileBeans);
		// } else {// 视频
		// List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
		// FormFileBean formFile = new FormFileBean(new File(
		// bitMapPath), "photo.png");
		// formFileBeans.add(formFile);
		// mapFileBean.put("photo", formFileBeans);
		//
		// List<FormFileBean> formFileBeans2 = new ArrayList<FormFileBean>();
		// FormFileBean formFile2 = new FormFileBean(new File(
		// videoPath), "video.mp4");
		// formFileBeans2.add(formFile2);
		// mapFileBean.put("video", formFileBeans2);
		// }
		//
		// uploadFiles(reqCodeThree, AppConfig.PUBLICK_BOARD,
		// mapFileBean, reqBase, true);
	}

	private boolean validate() {
		if (TextUtils.isEmpty(et_name.getText().toString())) {
			ToastUtil.showShortToast("请填写姓名");
			return false;
		} else if (TextUtils.isEmpty(tv_birthday.getText().toString())) {
			ToastUtil.showShortToast("请选择生日");
			return false;
		} else if (TextUtils.isEmpty(et_phone.getText().toString())) {
			ToastUtil.showShortToast("请填写手机号");
			return false;
		} else if (TextUtils.isEmpty(et_workyear.getText().toString())) {
			ToastUtil.showShortToast("请填写从业年限");
			return false;
		} else if (TextUtils.isEmpty(et_company.getText().toString())) {
			ToastUtil.showShortToast("请填写工作单位");
			return false;
		} else if (TextUtils.isEmpty(tv_city.getText().toString())) {
			ToastUtil.showShortToast("请选择参赛城市");
			return false;
		} else if (TextUtils.isEmpty(et_showname.getText().toString())) {
			ToastUtil.showShortToast("请填写作品名称");
			return false;
		} else if (TextUtils.isEmpty(et_declaration.getText().toString())) {
			ToastUtil.showShortToast("请填写参赛宣言");
			return false;
		} else {
			return true;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeTwo:
				videoPath = data.getStringExtra("videoPath");
				videoSeconds = data.getStringExtra("videoSeconds");
				iv_vedio.setImageBitmap(getVideoImg(videoPath));
				String bitMapPath = data.getStringExtra("bitMapPath");
				thumbnailPath = bitMapPath;
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
}
