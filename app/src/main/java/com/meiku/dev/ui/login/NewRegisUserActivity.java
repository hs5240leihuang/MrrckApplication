package com.meiku.dev.ui.login;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.HomeActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.ui.morefun.SelectPositionActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.NewDialog;

public class NewRegisUserActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout lin_head;
	private LinearLayout top_picture;
	protected String headImgPath;
	private TextView tv_shenfen;
	private Button btn_yanzhengma;
	private EditText et_phone;
	private EditText et_yanzhengma;
	private String validNumber;
	private EditText et_psd;
	private Button btn_ok;
	private EditText et_nicheng;
	private String position;
	private String type;
	private TextView tv_xiewyi;
	private boolean ispassword = false;
	private ImageView img_setpasstatus;
	private String currentSendPhoneNum;
	private boolean iszhuce=true;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_new_regis;
	}

	@Override
	public void initView() {
		img_setpasstatus = (ImageView) findViewById(R.id.img_setpasstatus);
		tv_xiewyi = (TextView) findViewById(R.id.tv_xiewyi);
		et_nicheng = (EditText) findViewById(R.id.et_nicheng);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		lin_head = (LinearLayout) findViewById(R.id.lin_head);
		top_picture = (LinearLayout) findViewById(R.id.top_picture);
		tv_shenfen = (TextView) findViewById(R.id.tv_shenfen);
		btn_yanzhengma = (Button) findViewById(R.id.btn_yanzhengma);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);
		et_psd = (EditText) findViewById(R.id.et_psd);
		et_phone.setText(getIntent().getStringExtra("phoneNum"));
		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (et_phone.getText().toString().length() == 11) {
					ReqBase req = new ReqBase();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("phone", et_phone.getText().toString());
					// map.put("queryType", ConstantKey.PHONE_TYPE_LOGIN);
					req.setHeader(new ReqHead(AppConfig.BUSINESS_US_20075));
					req.setBody(JsonUtil.Map2JsonObj(map));
					httpPost(reqCodeTwo, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
							req, false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@Override
	public void initValue() {
		top_picture.removeAllViews();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		MyRoundDraweeView img_top=new MyRoundDraweeView(NewRegisUserActivity.this);
		top_picture.addView(img_top,layoutParams);
		img_top.setUrlOfImage("");
	}

	@Override
	public void bindListener() {
		tv_xiewyi.setOnClickListener(this);
		lin_head.setOnClickListener(this);
		top_picture.setOnClickListener(this);
		tv_shenfen.setOnClickListener(this);
		btn_yanzhengma.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		top_picture.setOnClickListener(this);
		findViewById(R.id.layout_setpasstatus).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase req = (ReqBase) arg0;
		Log.e("wangke", req.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			validNumber = req.getBody().get("validNumber").getAsString();
			currentSendPhoneNum = req.getBody().get("phone").getAsString();
			break;
		case reqCodeTwo:
			try {

				if (!Tool.isEmpty(req.getBody())
						&& req.getBody().get("data").toString().length() > 2) {// 号码已存在
					ToastUtil.showShortToast("手机号已注册");
					iszhuce=false;
				}
				else {
					iszhuce=true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case reqCodeThree:
			try {
				// String loginCode =
				// req.getBody().get("loginCode").getAsString();
				
				//ToastUtil.showShortToast("注册成功");
				AppContext.getInstance().setJsessionId(
						req.getHeader().getJsessionId());
				MkUser userBean = (MkUser) JsonUtil.jsonToObj(MkUser.class, req
						.getBody().get("user").toString());
				AppContext.getInstance().setLocalUser(userBean);
				MrrckApplication.getInstance().doYunXinLogin(userBean.getLeanCloudUserName(), userBean.getLeanCloudId());
				sendBroadcast(new Intent(BroadCastAction.ACTION_LOGIN_SUCCESS));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!Tool.isEmpty(headImgPath) && !Tool.isEmpty(req.getBody())
					&& !Tool.isEmpty(req.getBody().get("data"))) {
				ReqBase req2 = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", req.getBody().get("data")
						.getAsJsonArray());
				req2.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req2.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
				FormFileBean formFile = new FormFileBean(new File(headImgPath),
						"photo.png");
				formFileBeans.add(formFile);
				mapFileBean.put("file", formFileBeans);
				Log.e("wangke", map + "");
				uploadResFiles(reqCodeFour, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req2, true);
			} else {

				startActivity(new Intent(NewRegisUserActivity.this,
						HomeActivity.class));
				finish();
			}
			break;
		case reqCodeFour:
			 startActivity(new Intent(NewRegisUserActivity.this,
					HomeActivity.class));
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
		case reqCodeThree:
		case reqCodeFour:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final NewDialog newDialog = new NewDialog(
						NewRegisUserActivity.this, "提示", resp.getHeader()
								.getRetMessage(), "确定");
				newDialog
						.setClicklistener(new NewDialog.ClickListenerInterface() {

							@Override
							public void doConfirm() {
								newDialog.dismiss();
							}

							@Override
							public void doCancel() {
								newDialog.dismiss();
							}
						});
				newDialog.show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.top_picture:
		case R.id.lin_head:
			Intent intent = new Intent(this, SelectPictureActivity.class);
			intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
			intent.putExtra("MAX_NUM", 1);// 选择的张数
			startActivityForResult(intent, reqCodeOne);
			break;
		case R.id.tv_shenfen:
			startActivityForResult(new Intent(NewRegisUserActivity.this,
					SelectPositionActivity.class), reqCodeTwo);
			break;
		case R.id.btn_yanzhengma:
			if (iszhuce) {
				getSecurityCode();
			}
			else {
				ToastUtil.showShortToast("手机号已注册");
			}
			break;
		case R.id.layout_setpasstatus:
			if (ispassword) {
				et_psd.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				img_setpasstatus.setBackgroundResource(R.drawable.guanbi_login);
				ispassword = false;
			} else {
				et_psd
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				img_setpasstatus
						.setBackgroundResource(R.drawable.zhengkai_login);
				ispassword = true;
			}
			et_psd.setSelection(et_psd.getText().toString()
					.length());
			break;
		case R.id.btn_ok:
			if (TextUtils.isEmpty(headImgPath)) {
				ToastUtil.showShortToast("请上传头像");
				return;
			}
			if (TextUtils.isEmpty(et_nicheng.getText().toString())) {
				ToastUtil.showShortToast("昵称不能为空");
				return;
			}
			if (TextUtils.isEmpty(tv_shenfen.getText().toString())) {
				ToastUtil.showShortToast("身份不能为空");
				return;
			}
			if (et_phone.getText().toString().length() < 11) {
				ToastUtil.showShortToast("手机号码不正确！");
				return;
			}
			if (et_psd.getText().toString().length()<6) {
				ToastUtil.showShortToast("密码至少6位");
				return;
			}
			if (Tool.isEmpty(et_yanzhengma.getText().toString())) {
				ToastUtil.showShortToast("请填写验证码");
				return;
			}
			if (!et_yanzhengma.getText().toString().equals(validNumber)) {
				ToastUtil.showShortToast("验证码不匹配");
				return;
			}
			if (!et_phone.getText().toString().equals(currentSendPhoneNum)) {
				ToastUtil.showShortToast("验证码不匹配");
				return;
			}
			ReqBase req = new ReqBase();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phone", et_phone.getText().toString());
			map.put("pwd", et_psd.getText().toString());
			map.put("nickName", et_nicheng.getText().toString());
			map.put("position", position);
			// map.put("type", type);
			map.put("longitude", MrrckApplication.getInstance()
					.getLongitudeStr());
			map.put("latitude", MrrckApplication.getInstance().getLaitudeStr());
			List<UploadImg> imgList = new ArrayList<UploadImg>();
			UploadImg ui = new UploadImg();
			ui.setFileType("0");
			ui.setFileUrl("");
			if (!Tool.isEmpty(headImgPath)) {
				ui.setFileThumb(headImgPath.substring(
						headImgPath.lastIndexOf(".") + 1, headImgPath.length()));
			} else {
				ui.setFileThumb("");
			}
			imgList.add(ui);
			if (!Tool.isEmpty(headImgPath)) {
				map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(imgList));
			} else {
				map.put("fileUrlJSONArray",
						JsonUtil.listToJsonArray(new ArrayList<UploadImg>()));
			}
			req.setHeader(new ReqHead(AppConfig.BUSINESS_US_20074));
			req.setBody(JsonUtil.Map2JsonObj(map));
			httpPost(reqCodeThree, AppConfig.PUBLIC_REQUEST_MAPPING_USER, req);
			Log.e("wangke", map + "");
			break;
		case R.id.tv_xiewyi:
			startActivity(new Intent(NewRegisUserActivity.this,
					AgreeMentActivity.class).putExtra("type", 0));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				String photoPath = data
						.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
				CompressPic(photoPath);
				break;
			case reqCodeTwo:
				tv_shenfen.setText(data.getStringExtra("name"));
				position = data.getIntExtra("id", 0) + "";
				type = data.getStringExtra("type");
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
				headImgPath = result;
				top_picture.removeAllViews();
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				MyRoundDraweeView img_top=new MyRoundDraweeView(NewRegisUserActivity.this);
				top_picture.addView(img_top,layoutParams);
				img_top.setUrlOfImage("file://"+headImgPath);
				LogUtil.d("hl", "压缩后处理__" + result);
				dismissProgressDialog();
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

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (wait <= 0) {
				btn_yanzhengma.setEnabled(true);
				btn_yanzhengma.setText("获取验证码");
				wait = ConstantKey.VERIFY_COUNT_DOWN_TIME;
				timer.cancel();
				timer = null;
				et_yanzhengma.clearFocus();
			} else {
				wait -= 1;
				btn_yanzhengma.setEnabled(false);
				btn_yanzhengma.setText("(" + wait + "s)重新发送");
			}
		}
	};
	// /////////==定义验证码定时器
	private Timer timer = null;// 定时器
	private int wait = ConstantKey.VERIFY_COUNT_DOWN_TIME;// 倒计时

	/**
	 * 开启定时器
	 */

	private void setTimer() {
		if (timer == null) {
			timer = new Timer();
		}
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 0, 1000);
	}

	/**
	 * 关闭定时器
	 */
	private void removeTimer() {
		wait = ConstantKey.VERIFY_COUNT_DOWN_TIME;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	// 获取验证码
	private void getSecurityCode() {
		if (TextUtils.isEmpty(et_phone.getText().toString())) {
			ToastUtil.showShortToast("手机号不能为空");
			return;
		}
		if (et_phone.getText().toString().length() < 11) {
			ToastUtil.showShortToast("手机号码不正确！");
			return;
		}
		setTimer();
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", et_phone.getText().toString());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_USER_VETIFICATION));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.USER_REQUEST_MAPPING, req, false);
	}

}
