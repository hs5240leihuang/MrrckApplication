package com.meiku.dev.ui.myshow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.GroupManageImageBean;
import com.meiku.dev.bean.MatchCityEntity;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.RefreshObs;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.TimeSelectDialog;

//比赛报名包含视频和图片
public class EnrollActivity extends BaseActivity implements OnClickListener {
	private final int reqCodeFive = 500;
	// private MyGridView gv_show;
	private EditText et_name, et_phone, et_workyear, et_showname,
			et_declaration, et_company;
	private TextView tv_name, tv_birthday, tv_phone, tv_city;
	private RadioGroup group_sex;
	private LinearLayout layout_birthday, layout_city;
	private Button btn_submit;

	private MatchCityEntity matchCity;

	String matchId;

	private String birthday;
	private String cityCode;
	private String gender = "2";

	private List<GroupManageImageBean> photolist = new ArrayList<GroupManageImageBean>();
	private CommonAdapter<GroupManageImageBean> roomAdapter;
	private int postsId;
	private String fileType;
	private String categoryId;
	private TextView tv_category;
	// private LinearLayout layout_vedio_text;
	// private FrameLayout layout_vedio;
	private ImageView iv_vedio;
	private TextView tv_addVedio;
	private String videoPath;
	private String thumbnailPath;

	private String categoryIds;

	private LinearLayout layout_category;
	private PostsEntity postsEntity;
	private String videoSeconds;
	private ImageView img_neiceng, img_waiceng;
	private String imgresault;
	private int flag;
	private String name, xuanyan;
	private String fileUrl;
	private String activeCityName;
	private int matchCityId;
	private String matchYear, matchMonth;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_enroll;
	}

	@Override
	public void initView() {
		img_neiceng = (ImageView) findViewById(R.id.img_neiceng);
		img_waiceng = (ImageView) findViewById(R.id.img_waiceng);
		// gv_show = (MyGridView) findViewById(R.id.gv_show);
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
		tv_category = (TextView) findViewById(R.id.tv_category);
		// layout_vedio_text = (LinearLayout)
		// findViewById(R.id.layout_vedio_text);
		//
		// layout_vedio = (FrameLayout) findViewById(R.id.layout_vedio);
		iv_vedio = (ImageView) findViewById(R.id.iv_vedio);
		// tv_addVedio = (TextView) findViewById(R.id.tv_addVedio);

		layout_category = (LinearLayout) findViewById(R.id.layout_category);
	}

	@Override
	public void initValue() {
		categoryIds = getIntent().getStringExtra("categoryId");
		postsId = getIntent().getIntExtra("postsId", 0);
		matchCityId = getIntent().getIntExtra("matchCityId", 0);
		matchYear = getIntent().getStringExtra("matchYear");
		matchMonth = getIntent().getStringExtra("matchMonth");
		activeCityName = getIntent().getStringExtra("activeCityName");
		tv_city.setText(activeCityName);
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 1) {
			xuanyan = getIntent().getStringExtra("xuanyan");
			name = getIntent().getStringExtra("name");
			imgresault = getIntent().getStringExtra("imgresault");
			BitmapUtils bitmapUtils = new BitmapUtils(EnrollActivity.this);
			bitmapUtils.display(img_neiceng, imgresault);
			fileUrl = getIntent().getStringExtra("fileUrl");
		}
		// postsEntity = (PostsEntity)
		// getIntent().getSerializableExtra("postEntity");
		// postsId = postsEntity.getPostsId()+"";
		// categoryIds = postsEntity.getCategoryId();
		// postsId = getIntent().getStringExtra("postsId");
		// categoryIds = getIntent().getStringExtra("categoryId");

		// fileType = getIntent().getStringExtra("fileType");
		// categoryId = getIntent().getStringExtra("categoryId");
		// if (!Tool.isEmpty(categoryId)&&categoryId.equals("-1")) {
		//
		// } else {
		// tv_category.setText(getIntent().getStringExtra("categoryName"));
		// }
		// if (!Tool.isEmpty(categoryId)&&fileType.equals("1")) {
		// gv_show.setVisibility(View.GONE);
		// layout_vedio_text.setVisibility(View.VISIBLE);
		// } else {
		// gv_show.setVisibility(View.VISIBLE);
		// layout_vedio_text.setVisibility(View.GONE);
		// }
		initphotoGrid();
		// postsId = getIntent().getStringExtra("postsId");

		MkUser userInfo = AppContext.getInstance().getUserInfo();
		et_declaration.setText(xuanyan);
		et_showname.setText(name);
		et_name.setText(userInfo.getRealName());
		tv_birthday.setText(userInfo.getBirthDate());
		et_phone.setText(userInfo.getPhone());
		if (userInfo.getGender().equals("1")) {
			group_sex.check(R.id.btn_gender_male);
			gender = "1";
		} else {
			group_sex.check(R.id.btn_gender_female);
			gender = "2";
		}
		getMatchId();

	}

	/**
	 * 获取当前赛事的ID
	 */
	protected void getMatchId() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityCode", MrrckApplication.getInstance().cityCode);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_MATCH_ID));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	public void bindListener() {
		img_neiceng.setOnClickListener(this);
		img_waiceng.setOnClickListener(this);
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
		// layout_vedio.setOnClickListener(this);
		// tv_addVedio.setOnClickListener(this);
		layout_category.setOnClickListener(this);
		// et_phone.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence arg0, int arg1, int arg2,
		// int arg3) {
		// if (et_phone
		// .getText()
		// .toString()
		// .equals(AppContext.getInstance().getUserInfo()
		// .getPhone())) {
		// tv_phone.setVisibility(View.VISIBLE);
		// } else {
		// tv_phone.setVisibility(View.GONE);
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence arg0, int arg1,
		// int arg2, int arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// et_name.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence arg0, int arg1, int arg2,
		// int arg3) {
		// if (et_phone
		// .getText()
		// .toString()
		// .equals(AppContext.getInstance().getUserInfo()
		// .getRealName())) {
		// tv_name.setVisibility(View.VISIBLE);
		// } else {
		// tv_name.setVisibility(View.GONE);
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence arg0, int arg1,
		// int arg2, int arg3) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl---------------", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			RefreshObs.getInstance().notifyAllLisWithTag("FragmentOneTypeShow");
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			if (!Tool.isEmpty(resp.getBody().get("postsSignupId") + "")) {
				startActivity(new Intent(EnrollActivity.this,
						NewWorkDetailActivity.class).putExtra(
						"SignupId",
						Integer.parseInt(resp.getBody().get("postsSignupId")
								.getAsString())));
			}
			finish();
			break;
		case reqCodeTwo:
			if ((resp.getBody().get("match") + "").length() > 2) {
				Map<String, String> map = JsonUtil.jsonToMap(resp.getBody()
						.get("match") + "");
				matchId = map.get("id");
				// if (!Tool.isEmpty(matchId)) {
				// LogUtil.d("hl","matchId="+matchId);
				// startActivity(new Intent(getActivity(),
				// NewMatchActivity.class)
				// .putExtra("matchId", Integer.parseInt(matchId)));
				// }
			} else {
				ToastUtil.showShortToast("获取当前赛事失败！");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		// if (resp.getHeader().getRetStatus().equals("5")) {
		// ToastUtil.showShortToast("该比赛报名人数已达上限，请选择其他比赛报名");
		// } else if (resp.getHeader().getRetStatus().equals("6")) {
		// ToastUtil.showShortToast("该比赛一结束，请选择其他比赛");
		// } else {
		// ToastUtil.showShortToast("失败");
		// }
		ToastUtil.showShortToast(resp.getHeader().getRetMessage());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_birthday:
			new TimeSelectDialog(EnrollActivity.this,
					new TimeSelectDialog.CallBackListener() {

						@Override
						public void CallBackOfTimeString(String time) {
							birthday = time;
							tv_birthday.setText(time);
						}
					}).show();
			break;
		case R.id.layout_city:
			// if(!Tool.isEmpty(postsEntity.getWholeCode()) &&
			// postsEntity.getWholeCode()==100000){
			// startActivityForResult(new
			// Intent(EnrollActivity.this,SelectCityActivity.class),reqCodeOne);
			// }else{
			// Intent i = new Intent(this, SelectAllCityActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("postEntity", postsEntity);
			// i.putExtras(bundle);
			// startActivityForResult(i,reqCodeOne);
			// }
			Intent i = new Intent(EnrollActivity.this,
					MatchDistrictActivity.class);
			i.putExtra("matchId", matchId);
			startActivityForResult(i, reqCodeOne);
			break;
		case R.id.btn_submit:
			if (validate()) {
				upload();
			}
			break;
		// case R.id.layout_vedio:
		// if (null != videoPath && !"".equals(videoPath)) {
		// Intent intent = new Intent();
		// intent.putExtra("mrrck_videoPath", videoPath);
		// intent.setClass(this, TestVideoActivity.class);
		// startActivity(intent);
		// } else {
		// // Intent intent = new Intent(this, ShootVideoActivity.class);
		// // startActivityForResult(intent, reqCodeTwo);
		// Intent intent = new Intent(this, SelectVedioActivity.class);
		// startActivityForResult(intent, reqCodeTwo);
		// }
		// break;
		// case R.id.tv_addVedio:
		// // Intent intent = new Intent(this, ShootVideoActivity.class);
		// // startActivityForResult(intent, reqCodeTwo);
		// Intent intent = new Intent(this, SelectVedioActivity.class);
		// startActivityForResult(intent, reqCodeTwo);
		// break;
		case R.id.layout_category:
			if (tv_city.getText().toString().equals("")) {
				ToastUtil.showShortToast("请先选择参赛区域");
			} else {
				startActivityForResult(new Intent(EnrollActivity.this,
						WorkCategoryActivity.class).putExtra("categoryIds",
						categoryIds), reqCodeFour);
			}

			break;
		case R.id.img_neiceng:

			break;
		case R.id.img_waiceng:
			if (flag == 0) {
				if (img_waiceng.getDrawable() == null) {

					Intent intent1 = new Intent(EnrollActivity.this,
							SelectPictureActivity.class);
					intent1.putExtra("SELECT_MODE",
							SelectPictureActivity.MODE_SINGLE);// 选择模式
					intent1.putExtra("MAX_NUM", 1);// 选择的张数
					startActivityForResult(intent1, reqCodeThree);
				} else {
					final CommonDialog commonDialog = new CommonDialog(
							EnrollActivity.this, "提示", "是否删除该照片", "确认", "取消");
					commonDialog.show();
					commonDialog.setClicklistener(new ClickListenerInterface() {

						@Override
						public void doConfirm() {
							img_waiceng.setImageDrawable(null);
							img_neiceng.setImageDrawable(ContextCompat
									.getDrawable(EnrollActivity.this,R.drawable.addphoto));
							commonDialog.dismiss();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
				}
				return;
			}

			break;
		default:
			break;
		}
	}

	private void upload() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("postsId", postsId);
		map.put("name", et_showname.getText().toString());
		map.put("categoryId", categoryId);
		map.put("matchCityId", matchCityId);
		map.put("matchId", matchId);
		map.put("matchYear", matchYear);
		map.put("matchMonth", matchMonth);
		// map.put("provinceCode", MrrckApplication.getInstance().provinceCode);
		// map.put("cityCode", MrrckApplication.getInstance().cityCode);
		map.put("remark", et_declaration.getText().toString());
		// map.put("fileType", fileType);
		map.put("userName", et_name.getText().toString());
		map.put("gender", gender);
		map.put("birthDate", tv_birthday.getText().toString());
		map.put("phone", et_phone.getText().toString());
		map.put("workAge", et_workyear.getText().toString());
		map.put("workCompany", et_company.getText().toString());

		map.put("matchCityCode", cityCode);

		LogUtil.d("hl", "报名_请求=" + map);
		// if (fileType.equals("1")) {// 视频作品贴
		// MediaPlayer mediaPlayer = new MediaPlayer();
		// try {
		// mediaPlayer.setDataSource(videoPath);
		// mediaPlayer.prepare();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// int duration = mediaPlayer.getDuration();
		// int fileSeconds = duration / 1000;
		// if (fileSeconds > 60) {
		// ToastUtil.showShortToast("视频长度超过60s,请重新选择");
		// return;
		// }
		// map.put("fileSeconds", fileSeconds);
		// }
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_BOARD_PUBLISH));
		if (flag == 1) {
			map.put("oldUrl", fileUrl);
			reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
			httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, reqBase);
		} else {
			map.put("oldUrl", "");
			Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
			List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
			FormFileBean formFile = new FormFileBean(new File(imgresault),
					"photo.png");
			formFileBeans.add(formFile);
			mapFileBean.put("photo", formFileBeans);
			LogUtil.e(mapFileBean.toString());
			reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
			uploadFiles(reqCodeOne, AppConfig.PUBLICK_BOARD, mapFileBean,
					reqBase, true);
		}

		// else {// 视频
		// List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
		// FormFileBean formFile = new FormFileBean(new File(thumbnailPath),
		// "photo.png");
		// formFileBeans.add(formFile);
		// mapFileBean.put("photo", formFileBeans);
		//
		// List<FormFileBean> formFileBeans2 = new ArrayList<FormFileBean>();
		// FormFileBean formFile2 = new FormFileBean(new File(videoPath),
		// "video.mp4");
		// formFileBeans2.add(formFile2);
		// mapFileBean.put("video", formFileBeans2);
		// }

		// }
	}

	/**
	 * 我的秀场GridView初始化
	 */
	private void initphotoGrid() {/*
								 * photolist.clear(); GroupManageImageBean
								 * groupManageImageBeanAdd = new
								 * GroupManageImageBean();
								 * groupManageImageBeanAdd
								 * .setClientPicUrl(R.drawable.tianjia + "");
								 * photolist.add(groupManageImageBeanAdd); // if
								 * (!Tool.isEmpty(userAttachmentList)) { // //
								 * for (int i = 0; i <
								 * userAttachmentList.size(); i++) { //
								 * GroupManageImageBean groupManageImageBean =
								 * new GroupManageImageBean(); //
								 * groupManageImageBean
								 * .setClientPicUrl(userAttachmentList.get(i) //
								 * .getClientFileUrl()); //
								 * photolist.add(groupManageImageBean); // } //
								 * }
								 * 
								 * roomAdapter = new
								 * CommonAdapter<GroupManageImageBean>(this,
								 * R.layout.item_photogrid, photolist) {
								 * 
								 * @Override public void convert(final
								 * ViewHolder viewHolder, final
								 * GroupManageImageBean t) { if
								 * (viewHolder.getPosition() == 0) {
								 * viewHolder.setImage(R.id.image_out,
								 * Integer.parseInt(t.getClientPicUrl()));
								 * viewHolder
								 * .getConvertView().setOnClickListener( new
								 * OnClickListener() {
								 * 
								 * @Override public void onClick(View arg0) { if
								 * ( photolist.size()>=8) {
								 * ToastUtil.showShortToast("最多只能上传8张照片！");
								 * return; } Intent intent = new Intent(
								 * EnrollActivity.this,
								 * SelectPictureActivity.class);
								 * intent.putExtra( "SELECT_MODE",
								 * SelectPictureActivity.MODE_SINGLE);// 选择模式
								 * intent.putExtra("MAX_NUM", 1);// 选择的张数
								 * startActivityForResult(intent, reqCodeThree);
								 * 
								 * } }); } else { viewHolder
								 * .setImage(R.id.image_out,
								 * R.drawable.yinshi_jianhao);
								 * viewHolder.setImage(R.id.image,
								 * t.getClientPicUrl(),1);
								 * viewHolder.getConvertView
								 * ().setOnClickListener( new OnClickListener()
								 * {
								 * 
								 * @Override public void onClick(View arg0) {
								 * final CommonDialog commonDialog = new
								 * CommonDialog( EnrollActivity.this, "提示",
								 * "是否删除该照片", "确认", "取消"); commonDialog.show();
								 * commonDialog .setClicklistener(new
								 * ClickListenerInterface() {
								 * 
								 * @Override public void doConfirm() { // 删除照片请求
								 * // ReqBase req = new ReqBase(); //
								 * Map<String, Object> map = new HashMap<String,
								 * Object>(); // map.put("userId", // AppContext
								 * // .getInstance() // .getUserInfo() //
								 * .getId()); // map.put("attachmentId", //
								 * userAttachmentList // .get(viewHolder //
								 * .getPosition()-1) // .getAttachmentId()); //
								 * req.setHeader(new ReqHead( //
								 * AppConfig.BUSINESS_DELETE_PHOTO)); //
								 * req.setBody(JsonUtil // .Map2JsonObj(map));
								 * // httpPost( // reqCodeFive, //
								 * AppConfig.PUBLIC_REQUEST_MAPPING, // req);
								 * photolist.remove(viewHolder.getPosition());
								 * roomAdapter.notifyDataSetChanged();
								 * commonDialog.dismiss(); }
								 * 
								 * @Override public void doCancel() {
								 * commonDialog.dismiss(); } }); } }); }
								 * 
								 * }
								 * 
								 * }; // gv_show.setAdapter(roomAdapter);
								 */
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
		}
		// else if (TextUtils.isEmpty(et_company.getText().toString())) {
		// ToastUtil.showShortToast("请填写工作单位");
		// return false;
		// }
		else if (TextUtils.isEmpty(tv_city.getText().toString())) {
			ToastUtil.showShortToast("请选择参赛城市");
			return false;
		} else if (TextUtils.isEmpty(et_showname.getText().toString())) {
			ToastUtil.showShortToast("请填写作品名称");
			return false;
		}
		// else if (TextUtils.isEmpty(et_declaration.getText().toString())) {
		// ToastUtil.showShortToast("请填写参赛宣言");
		// return false;
		// }
		else if (Tool.isEmpty(tv_category.getText().toString())) {
			ToastUtil.showShortToast("请选择作品类别");
			return false;
		}

		else if (img_neiceng.getDrawable() == null && flag == 1) {
			ToastUtil.showShortToast("请上传图片");
			return false;
		} else if (img_waiceng.getDrawable() == null && flag == 0) {
			ToastUtil.showShortToast("请上传图片");
			return false;
		}

		// else if (fileType.equals("1") && Tool.isEmpty(videoPath)) {
		// ToastUtil.showShortToast("请选择视频");
		// return false;
		// }
		else {
			if (fileType.equals("1")) {
				File file = new File(videoPath);
				if (file.length() > 20971520) {
					ToastUtil.showShortToast("视频过大请重新上传");
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				matchCity = (MatchCityEntity) data
						.getSerializableExtra("matchCity");
				LogUtil.e(matchCity.getMatchCityName());
				tv_city.setText(matchCity.getMatchCityName());
				cityCode = matchCity.getMatchCityCode() + "";
				categoryIds = matchCity.getCategoryId();
				postsId = matchCity.getPostsId();
				matchCityId = matchCity.getMatchCityId();
				matchId = matchCity.getMatchId() + "";
				matchYear = matchCity.getMatchYear();
				matchMonth = matchCity.getMatchMonth();
				break;
			case reqCodeTwo:
				videoPath = data.getStringExtra("videoPath");
				videoSeconds = data.getStringExtra("videoSeconds");
				iv_vedio.setImageBitmap(getVideoImg(videoPath));
				String bitMapPath = data.getStringExtra("bitMapPath");
				thumbnailPath = bitMapPath;
				break;
			case reqCodeThree:
				List<String> pictrue = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(reqCodeThree, pictrue.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqCodeThree, photoPath);
				}
				break;
			case reqCodeFour:
				categoryId = data.getStringExtra("categoryId");
				tv_category.setText(data.getStringExtra("categoryName"));
				// fileType = data.getStringExtra("fileType");
				fileType = "0";
				// if (fileType.equals("0")) {
				// gv_show.setVisibility(View.VISIBLE);
				// layout_vedio_text.setVisibility(View.GONE);
				// } else {
				// gv_show.setVisibility(View.GONE);
				// layout_vedio_text.setVisibility(View.VISIBLE);
				// }
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param reqcode
	 * 
	 * @param photoPath
	 */
	private void CompressPic(int reqcode, String photoPath) {
		showProgressDialog("图片压缩中...");
		new MyAsyncTask(reqcode).execute(photoPath);
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {

		private int reqcode;

		public MyAsyncTask(int reqcode) {
			this.reqcode = reqcode;
		}

		@Override
		protected void onPostExecute(String result) {
			switch (reqcode) {
			case reqCodeThree:
				BitmapUtils bitmapUtils = new BitmapUtils(EnrollActivity.this);
				bitmapUtils.display(img_neiceng, result);
				dismissProgressDialog();
				img_waiceng.setImageDrawable(ContextCompat.getDrawable(EnrollActivity.this,
						R.drawable.yinshi_jianhao));
				imgresault = result;
				/*
				 * GroupManageImageBean groupManageImageBean = new
				 * GroupManageImageBean();
				 * groupManageImageBean.setClientPicUrl(result);
				 * photolist.add(groupManageImageBean);
				 * roomAdapter.notifyDataSetChanged(); dismissProgressDialog();
				 */
				// ReqBase reqBase = new ReqBase();
				// Map<String, Object> map = new HashMap<String, Object>();
				// map.put("moduleType", 22);
				// map.put("moduleId", AppContext.getInstance().getUserInfo()
				// .getId());
				// map.put("userId", AppContext.getInstance().getUserInfo()
				// .getId());
				// reqBase.setHeader(new ReqHead(
				// AppConfig.BUSINESS_SHANGCHUAN_PHOTO));
				// reqBase.setBody(JsonUtil.Map2JsonObj(map));
				// Map<String, List<FormFileBean>> mapFileBean = new
				// HashMap<String, List<FormFileBean>>();
				// formFileBeans = new ArrayList<FormFileBean>();
				// formFile = new FormFileBean(new File(result),
				// System.currentTimeMillis() + ".png");
				// formFileBeans.add(formFile);
				// mapFileBean.put("photo", formFileBeans);
				// uploadFiles(reqCodeSix, AppConfig.PUBLIC_REQUEST_MAPPING,
				// mapFileBean, reqBase, true);
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... arg0) {
			String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
			LogUtil.d("hl", "返回拍照路径压缩__" + photoPath);
			return photoPath;
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