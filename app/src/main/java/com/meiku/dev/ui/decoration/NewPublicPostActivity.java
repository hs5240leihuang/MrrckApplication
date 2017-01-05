package com.meiku.dev.ui.decoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.PopupData;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.TopicData;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.EmotionView;
import com.meiku.dev.views.EmotionView.ChooseOneEmotionListener;
import com.meiku.dev.views.NewPopupwindows;
import com.meiku.dev.views.NewPopupwindows.popwindowListener;

public class NewPublicPostActivity extends BaseActivity implements
		OnClickListener {
	private TextView tv_huati;
	private TextView right_txt_title;
	private TextView tv_biaoti;
	private LinearLayout lin_huati;
	private EmotionEditText et_biaoti;
	private EmotionEditText et_content;
	private int topicId;
	private ImageView btnPicture;
	private ImageView btnExpress;
	private NewPopupwindows newPopupwindows;
	private List<PopupData> list = new ArrayList<PopupData>();
	private List<TopicData> showlist = new ArrayList<TopicData>();
	public static final int TAKE_PHOTO_IMG = 100;// 图片选择
	protected String picPath;
	private int attacheNum = 0;// 附件数量
	protected boolean showEmotion;
	protected LinearLayout emotionLayout;
	private String topicname;
	private String boardId;
	private int menuId;
	private ImageView img_delect;
	//private Button btn_ok;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_newpublicpost;
	}

	@Override
	public void initView() {
	//	btn_ok = (Button) findViewById(R.id.btn_ok);
		img_delect = (ImageView) findViewById(R.id.img_delect);
		emotionLayout = (LinearLayout) findViewById(R.id.emotionLayout);
		btnExpress = (ImageView) findViewById(R.id.btn_express);
		btnPicture = (ImageView) findViewById(R.id.btn_picture);
		et_content = (EmotionEditText) findViewById(R.id.et_content);
		et_biaoti = (EmotionEditText) findViewById(R.id.et_biaoti);
		tv_biaoti = (TextView) findViewById(R.id.tv_biaoti);
		lin_huati = (LinearLayout) findViewById(R.id.lin_huati);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackground(null);
		right_txt_title.setTextColor(Color.parseColor("#FF3499"));
		tv_huati = (TextView) findViewById(R.id.tv_huati);
		Drawable drawable = ContextCompat.getDrawable(
				NewPublicPostActivity.this, R.drawable.decoration_down);
		drawable.setBounds(0, 0,
				ScreenUtil.dip2px(NewPublicPostActivity.this, 6),
				ScreenUtil.dip2px(NewPublicPostActivity.this, 6));
		tv_huati.setCompoundDrawables(null, null, drawable, null);
		et_biaoti.setCursorVisible(false);
		et_biaoti.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (!arg1) {
					et_biaoti.setCursorVisible(true);
				}
				showEmotion = false;
				emotionLayout.setVisibility(View.GONE);
			}
		});
		et_content.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				showEmotion = false;
				emotionLayout.setVisibility(View.GONE);
			}
		});
		emotionLayout.addView(new EmotionView(NewPublicPostActivity.this,
				new ChooseOneEmotionListener() {

					@Override
					public void doChooseOneEmotion(String emotionText) {
						if (et_biaoti.hasFocus()) {
							showEmotionTxt(et_biaoti, emotionText);
						} else if (et_content.hasFocus()) {
							showEmotionTxt(et_content, emotionText);
						}

					}
				}).getView());
	}

	@Override
	public void initValue() {
		menuId = getIntent().getIntExtra("menuId", -1);
		boardId = getIntent().getStringExtra("boardId");
		GetData();
	}

	@Override
	public void bindListener() {
		et_biaoti.setOnClickListener(this);
		et_content.setOnClickListener(this);
		tv_huati.setOnClickListener(this);
		right_txt_title.setOnClickListener(this);
		btnPicture.setOnClickListener(this);
		btnExpress.setOnClickListener(this);
		img_delect.setOnClickListener(this);
	//	btn_ok.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("data").toString().length() > 2) {
				List<TopicData> listData = (List<TopicData>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<TopicData>>() {
								}.getType());
				showlist.clear();
				showlist.addAll(listData);
			} else {
				ToastUtil.showShortToast("获取参数失败");
			}
			break;
		case reqCodeTwo:
			if (!Tool.isEmpty(picPath) && !Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))) {
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", resp.getBody().get("data")
						.getAsJsonArray());
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
				FormFileBean formFile = new FormFileBean(new File(picPath),
						"photo.png");
				formFileBeans.add(formFile);
				mapFileBean.put("file", formFileBeans);
				Log.e("wangke", map + "");
				uploadResFiles(reqCodeThree, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, true);
			} else {
				ToastUtil.showShortToast("发布成功");
				setResult(RESULT_OK);
				finish();
			}
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("发布成功");
			setResult(RESULT_OK);
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
			ToastUtil.showShortToast("获取话题失败！");
			break;
		case reqCodeTwo:
		case reqCodeThree:
			ToastUtil.showShortToast("发布失败！ ");
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_huati:
			if (!Tool.isEmpty(showlist)) {
				list.clear();
				for (int i = 0, size = showlist.size(); i < size; i++) {
					list.add(new PopupData(showlist.get(i).getName(), 0));
				}
				newPopupwindows = new NewPopupwindows(
						NewPublicPostActivity.this, list,
						new popwindowListener() {

							@Override
							public void doChoose(int position) {
								tv_huati.setText(list.get(position).getName());
								topicId = showlist.get(position).getId();
								topicname = list.get(position).getName();
							}
						}, 0);
				newPopupwindows.show(findViewById(R.id.lin_huati));
			} else {
				GetData();
			}
			break;
		case R.id.right_txt_title:
			InputTools.HideKeyboard(arg0);
			if (validate()) {
				if (!TextUtils.isEmpty(picPath)) {
					attacheNum = 1;
				} else {
					attacheNum = 0;
				}
				Publish();
			}
			break;
		case R.id.btn_picture:
			InputTools.HideKeyboard(arg0);
			Intent intent = new Intent(this, SelectPictureActivity.class);
			intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
			intent.putExtra("MAX_NUM", 1);// 选择的张数
			startActivityForResult(intent, TAKE_PHOTO_IMG);
			break;
		case R.id.btn_express:
			showEmotion = !showEmotion;
			showEmotionView(showEmotion);
			break;
		case R.id.et_biaoti:
			et_biaoti.setCursorVisible(true);
			et_biaoti.requestFocus();
			showEmotion = false;
			emotionLayout.setVisibility(View.GONE);
			break;
		case R.id.et_content:
			et_content.requestFocus();
			showEmotion = false;
			emotionLayout.setVisibility(View.GONE);
			break;
		case R.id.img_delect:
			btnPicture.setImageDrawable(getResources().getDrawable(
					R.drawable.add_photo));
			img_delect.setVisibility(View.GONE);
			picPath = "";
			break;
//		case R.id.btn_ok:
//			break;
		default:
			break;
		}
	}

	// 数据请求
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardId", boardId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_POSTTOPIC));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 发布
	public void Publish() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("topicId", topicId);
		map.put("menuId", menuId);
		map.put("boardId", boardId);
		map.put("title", EmotionHelper.getSendEmotion(
				NewPublicPostActivity.this, et_biaoti.getText().toString()));
		map.put("content", EmotionHelper.getSendEmotion(
				NewPublicPostActivity.this, et_content.getText().toString()));
		if (Tool.isEmpty(MrrckApplication.provinceCode)) {
			map.put("provinceCode", -1);
		} else {
			map.put("provinceCode", MrrckApplication.provinceCode);
		}
		if (Tool.isEmpty(MrrckApplication.cityCode)) {
			map.put("cityCode", -1);
		} else {
			map.put("cityCode", MrrckApplication.cityCode);
		}
		List<UploadImg> imgList = new ArrayList<UploadImg>();
		UploadImg ui = new UploadImg();
		ui.setFileType("0");
		ui.setFileUrl("");
		if (!Tool.isEmpty(picPath)) {
			map.put("attachmentNum", 1);
			ui.setFileThumb(picPath.substring(picPath.lastIndexOf(".") + 1,
					picPath.length()));
		} else {
			map.put("attachmentNum", 0);
			ui.setFileThumb("");
		}
		imgList.add(ui);
		if (!Tool.isEmpty(picPath)) {
			map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(imgList));
		} else {
			map.put("fileUrlJSONArray",
					JsonUtil.listToJsonArray(new ArrayList<UploadImg>()));
		}
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PUBLISHPOST));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
		Log.e("wangke", map + "");
	}

	private boolean validate() {
		if (Tool.isEmpty(topicname)) {
			ToastUtil.showShortToast("请选择话题");
			return false;
		}
		if (Tool.isEmpty(et_biaoti.getText().toString())) {
			ToastUtil.showShortToast("请输入标题");
			return false;
		}
		if (Tool.isEmpty(et_content.getText().toString())) {
			ToastUtil.showShortToast("请输入内容");
			return false;
		}
		return true;
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
				picPath = result;
				BitmapUtils bitmapUtils = new BitmapUtils(
						NewPublicPostActivity.this);
				bitmapUtils.display(btnPicture, picPath);
				img_delect.setVisibility(View.VISIBLE);
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

	private void showEmotionView(boolean show) {
		View v;
		if (et_biaoti.hasFocus()) {
			v = et_biaoti;
		} else {
			v = et_content;
		}
		if (show) {
			InputTools.HideKeyboard(v);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					emotionLayout.setVisibility(View.VISIBLE);
				}
			}, 200);
		} else {
			emotionLayout.setVisibility(View.GONE);
			InputTools.ShowKeyboard(v);
		}
	}

	protected void showEmotionTxt(EmotionEditText etContent, String emotionText) {
		int start = etContent.getSelectionStart();
		StringBuffer sb = new StringBuffer(etContent.getText());
		if (emotionText.equals(":l7:")) {// 删除图标
			if (start >= 4
					&& EmotionHelper.getLocalEmoMap(NewPublicPostActivity.this)
							.containsKey(sb.substring(start - 4, start))) {
				sb.replace(start - 4, start, "");
				etContent.setText(sb.toString());
				CharSequence info = etContent.getText();
				if (info instanceof Spannable) {
					Spannable spannable = (Spannable) info;
					Selection.setSelection(spannable, start - 4);
				}
				return;
			}
			return;
		}
		sb.replace(etContent.getSelectionStart(), etContent.getSelectionEnd(),
				emotionText);
		etContent.setText(sb.toString());

		CharSequence info = etContent.getText();
		if (info instanceof Spannable) {
			Spannable spannable = (Spannable) info;
			Selection.setSelection(spannable, start + emotionText.length());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TAKE_PHOTO_IMG:
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				if (pics != null) {// 多选图片返回
					CompressPic(pics.get(0));
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(photoPath);
				}
				break;
			default:
				break;
			}
		}
	}
}
