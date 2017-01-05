package com.meiku.dev.ui.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.CompanyCustomContentEntity;
import com.meiku.dev.bean.CompanyDesignersEntity;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.bean.UserAttachmentEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.ui.recruit.AddOneCompDetailActivity;
import com.meiku.dev.ui.recruit.AddOneDesignerInfoActivity;
import com.meiku.dev.ui.service.GetLoacationActivity;
import com.meiku.dev.ui.vote.SelectVedioActivity;
import com.meiku.dev.utils.FileHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ChooseTextDialog;
import com.meiku.dev.views.ChooseTextDialog.ChooseOneStrListener;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;

//编辑企业信息
public class EditCompInfoActivity extends BaseActivity implements
		OnClickListener {
	private final int reqCodeFive = 500;
	private final int reqCodeSix = 600;
	private final int reqCodeSeven = 700;
	private LinearLayout layout_logo, layout_name;
	private LinearLayout layout_license;
	private EditText et_complanyName;
	private LinearLayout layout_city;
	private EditText et_address;
	private TextView tv_city;
	private String cityCode;
	// private String provinceCode;
	private LinearLayout layout_complanyPeople;
	private TextView tv_complanyPeople;
	private EditText et_contact, et_website, et_email;
	private EditText et_phoneNum;
	private EditText et_introduce;
	private FrameLayout frame_uploadvideo;
	private ImageView iv_play;
	private MyGridView gridview_uploadpic, gridview;
	private List<Object> picPathList = new ArrayList<Object>();// 上传公司图片--选择的图片路径
	private List<UserAttachmentEntity> zzpicPathList = new ArrayList<UserAttachmentEntity>();
	private final int TAKE_PHOTO_LOGO = 1001;// LOGO选择
	private final int TAKE_PHOTO_LICENSE = 1002;// 执照选择
	private final int SELECT_COMPANYPICS = 1003;//
	private final int TAKE_VIDEO = 1004;//
	private final int GET_ADDRESS = 1005;//
	private final int SELECT_ZZPIC = 1006;// 资质图片选择
	private final int Add_MORECOMPINFO = 1007;// 添加自定义项
	private final int Add_DESIGNER = 1008;// 添加设计师
	private final int REQ_ADDZZPIC = 1009;// 添加设计师
	private CommonAdapter<DataconfigEntity> typeAdapter;
	private ArrayList<String> allScaleStr = new ArrayList<String>();// 规模待选
	private String bitMap_video;
	private String videoPath_video;
	private String logoPicPath, licensePicPath;// logo,license图片路径
	private String companyScaleCodeId = "-1";// 公司规模ID
	private LinearLayout lay_upload;
	private List<DataconfigEntity> companyScaleData = new ArrayList<DataconfigEntity>();// 公司规模数据
	private List<DataconfigEntity> bossTypesData = new ArrayList<DataconfigEntity>();// 老板类型数据
	private float longitude;
	private float latitude;
	private String bossType = "";// 多个以逗号分隔
	private String bossTypeName = "";// 多个以逗号分隔
	private int fileSeconds;
	private LinearLayout videoInfoLayout;
	private TextView videoInfo;
	private Button videoReSet;
	private String videoSeconds;
	private CompanyEntity companyEntity;
	private List<UserAttachmentEntity> attachmentList;
	private ImageView iv_delete;
	private CommonDialog commonDialog;
	private final int reqCodeEight = 800;
	private Button btn_edit;
	private LinearLayout lin_logo;
	private LinearLayout lin_zhizhao;
	private MySimpleDraweeView iv_logo;
	private MySimpleDraweeView iv_zhizhao;
	private MyGridView gridview_uploadzzpic;
	private ArrayList<CompanyCustomContentEntity> detailsInfoList = new ArrayList<CompanyCustomContentEntity>();
	private ArrayList<CompanyDesignersEntity> designerInfoList = new ArrayList<CompanyDesignersEntity>();
	private LinearLayout layout_moreGroup;
	private LinearLayout layout_teamGroup;
	private final int REFRESH_ALL = 0;
	private final int REFRESH_ZIZHI = 1;
	private final int REFRESH_DESIGNER = 2;
	private final int REFRESH_ZIDINGYI = 3;
	private int REFRESH_BODYFLAG = 0;

	private int refreshType;
	private EditText et_zzry;
	private final int REQ_DELETE_DESIGNER = 501;
	private final int REQ_DELETE_CUSTOM = 502;
	private final int REQ_DELETE_ZZPIC = 503;
	private final int REQ_ADD_ZZPIC = 504;
	private final int REQ_CHANGE_LOGO = 505;
	private final int REQ_CHANGE_VIDEO = 506;
	private final int REQ_CHANGE_VIDEOPIC = 507;
	private final int REQ_CHANGE_COMPPIC = 508;
	public String onezzPicPath;
	private String currentAddCompPicPath;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_edit_comp_info;
	}

	@Override
	public void initView() {
		initTipDialog();
		// logo
		layout_logo = (LinearLayout) findViewById(R.id.layout_logo);
		lin_logo = (LinearLayout) findViewById(R.id.lin_logo);
		// 企业营业执照
		layout_license = (LinearLayout) findViewById(R.id.layout_license);
		lin_zhizhao = (LinearLayout) findViewById(R.id.lin_zhizhao);
		// 公司名称
		et_complanyName = (EditText) findViewById(R.id.et_complanyName);
		// 城市
		layout_city = (LinearLayout) findViewById(R.id.layout_city);
		tv_city = (TextView) findViewById(R.id.tv_city);
		// 公司地址
		et_address = (EditText) findViewById(R.id.et_address);
		// 公司网址
		et_website = (EditText) findViewById(R.id.et_website);
		// 公司邮箱
		et_email = (EditText) findViewById(R.id.et_email);
		// 企业规模
		setCompanyScale();
		// 联系人
		et_contact = (EditText) findViewById(R.id.et_contact);
		// 联系电话
		et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
		// 企业简介
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		// 上传视频
		initUploadVideo();
		// 资质文字
		et_zzry = (EditText) findViewById(R.id.et_zzry);
		// 照片上传
		gridview_uploadpic = (MyGridView) findViewById(R.id.gridview_uploadpic);
		picPathList.add("+");
		setSelectPic(true);
		btn_edit = (Button) findViewById(R.id.btn_edit);
		layout_name = (LinearLayout) findViewById(R.id.layout_name);
		// 资质图片
		gridview_uploadzzpic = (MyGridView) findViewById(R.id.gridview_uploadzzpic);
		zzpicPathList.add(new UserAttachmentEntity());
		setZZSelectPic(true);
		layout_moreGroup = (LinearLayout) findViewById(R.id.layout_moreGroup);
		layout_teamGroup = (LinearLayout) findViewById(R.id.layout_teamGroup);
	}

	private void initTipDialog() {
		commonDialog = new CommonDialog(EditCompInfoActivity.this, "提示",
				"是否放弃本次编辑?", "确定", "取消");
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
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

	/**
	 * 上传视频初始化
	 */
	private void initUploadVideo() {
		videoInfoLayout = (LinearLayout) findViewById(R.id.videoInfoLayout);
		videoInfo = (TextView) findViewById(R.id.videoInfo);
		videoReSet = (Button) findViewById(R.id.videoReSet);
		videoReSet.setOnClickListener(this);
		frame_uploadvideo = (FrameLayout) findViewById(R.id.frame_top);
		lay_upload = (LinearLayout) findViewById(R.id.lay_upload);
		iv_play = (ImageView) findViewById(R.id.iv_play);
		iv_play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("mrrck_videoPath", videoPath_video);
				intent.setClass(EditCompInfoActivity.this,
						TestVideoActivity.class);
				startActivity(intent);
			}
		});
		iv_delete = (ImageView) findViewById(R.id.iv_del);
		iv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteviedio();
			}
		});
	}

	/**
	 * 检测是否具有提交的条件
	 * 
	 * @return
	 */
	private boolean checkIsReady() {
		// if (Tool.isEmpty(logoPicPath)) {
		// ToastUtil.showShortToast("请上传企业LOGO");
		// return false;
		// }
		if (Tool.isEmpty(et_complanyName.getText().toString().trim())) {
			ToastUtil.showShortToast("企业名称未填写");
			return false;
		}
		if (Tool.isEmpty(tv_city.getText().toString().trim())) {
			ToastUtil.showShortToast("所在城市未选择");
			return false;
		}
		if (Tool.isEmpty(et_address.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写公司地址");
			return false;
		}
		// if (Tool.isEmpty(tv_complanyPeople.getText().toString().trim())
		// || Tool.isEmpty(companyScaleCodeId)) {
		// ToastUtil.showShortToast("公司规模未选择");
		// return false;
		// }
		if (Tool.isEmpty(et_contact.getText().toString().trim())) {
			ToastUtil.showShortToast("联系人未填写");
			return false;
		}
		if (Tool.isEmpty(et_phoneNum.getText().toString().trim())) {
			ToastUtil.showShortToast("联系电话未填写");
			return false;
		}
		// if (Tool.isEmpty(et_introduce.getText().toString().trim())) {
		// ToastUtil.showShortToast("企业简介未填写");
		// return false;
		// }
		// if (Tool.isEmpty(bossType) || Tool.isEmpty(bossTypeName)) {
		// ToastUtil.showShortToast("请选择老板性格标签");
		// return false;
		// }
		return true;
	}

	/**
	 * 公司规模
	 */
	private void setCompanyScale() {
		layout_complanyPeople = (LinearLayout) findViewById(R.id.layout_complanyPeople);
		tv_complanyPeople = (TextView) findViewById(R.id.tv_complanyPeople);
		companyScaleData = MKDataBase.getInstance().getCompanyScale();
		for (int i = 0, size = companyScaleData.size(); i < size; i++) {
			allScaleStr.add(companyScaleData.get(i).getValue());
		}

	}

	// 老板类型
	private void initBossTypeSelected() {
		bossTypesData = MKDataBase.getInstance().getBossTypes();
		if (!Tool.isEmpty(companyEntity.getBossType())) {
			String[] bossTypesIds = companyEntity.getBossType().split(",");
			for (int k = 0, length = bossTypesIds.length; k < length; k++) {
				for (int j = 0, size = bossTypesData.size(); j < size; j++) {
					if (bossTypesIds[k]
							.equals(bossTypesData.get(j).getCodeId())) {
						bossTypesData.get(j).setDelStatus("1");
					}
				}
			}
		}
		gridview = (MyGridView) findViewById(R.id.gridview);
		typeAdapter = new CommonAdapter<DataconfigEntity>(
				EditCompInfoActivity.this, R.layout.item_xingqu, bossTypesData) {

			@Override
			public void convert(ViewHolder viewHolder, DataconfigEntity t) {
				TextView text = viewHolder.getView(R.id.text);
				text.setText(t.getValue());
				if ("0".equals(t.getDelStatus())
						|| Tool.isEmpty(t.getDelStatus())) {// 1选中,0未选
					text.setTextColor(Color.parseColor("#333333"));
					text.setBackgroundResource(R.drawable.shape_btn_normal);
				} else {
					text.setTextColor(Color.WHITE);
					text.setBackgroundResource(R.drawable.shape_btn_press);
				}
			}

		};
		gridview.setAdapter(typeAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DataconfigEntity confEntity = bossTypesData.get(arg2);
				List<DataconfigEntity> bossTypeSelected = new ArrayList<DataconfigEntity>();// 老板类型数据
				// 获取初始选择的项
				for (int i = 0, size = bossTypesData.size(); i < size; i++) {
					if ("1".equals(bossTypesData.get(i).getDelStatus())) {// 暂使用DelStatus作为选中字段使用
						bossTypeSelected.add(bossTypesData.get(i));
					}
				}
				if ("0".equals(confEntity.getDelStatus())
						|| Tool.isEmpty(confEntity.getDelStatus())) {
					if (bossTypeSelected.size() == 2) {
						ToastUtil.showShortToast("至多选择两项");
					} else {
						confEntity.setDelStatus("1");
						typeAdapter.notifyDataSetChanged();
					}
				} else {
					confEntity.setDelStatus("0");
					typeAdapter.notifyDataSetChanged();
				}

				bossTypeSelected.clear();
				// 获取最终选择的项
				for (int i = 0, size = bossTypesData.size(); i < size; i++) {
					if ("1".equals(bossTypesData.get(i).getDelStatus())) {// 暂使用DelStatus作为选中字段使用
						bossTypeSelected.add(bossTypesData.get(i));
					}
				}
				bossType = "";// 多个以逗号分隔
				bossTypeName = "";// 多个以逗号分隔
				// 选择的老板类型
				for (int i = 0, size = bossTypeSelected.size(); i < size; i++) {
					bossType += bossTypeSelected.get(i).getCodeId();
					bossTypeName += bossTypeSelected.get(i).getValue();
					if (i != size - 1) {// 最后一个数据最后不添加逗号","
						bossType += ",";
						bossTypeName += ",";
					}
				}
			}
		});
	}

	private void setSelectPic(boolean isAdd) {
		gridview_uploadpic.setAdapter(new CommonAdapter<Object>(this,
				R.layout.add_talent_pic_gridview_item, picPathList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final Object imgItem) {
				// 如果大于8个删除第一个图片也就是增加图片按钮
				if (picPathList.size() < 9) {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.setImage(R.id.img_item,
								R.drawable.clickaddpic);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												EditCompInfoActivity.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 10 - picPathList.size();
										intent.putExtra("MAX_NUM",
												(picNum > 0) ? picNum : 0);// 选择的张数
										startActivityForResult(intent,
												SELECT_COMPANYPICS);
									}
								});

					} else {
						String imagePath = "";
						if (imgItem.getClass().getName()
								.equals(AttachmentListDTO.class.getName())) {
							AttachmentListDTO attachmentListDTO = (AttachmentListDTO) imgItem;
							imagePath = attachmentListDTO.getClientFileUrl();
						} else {
							imagePath = (String) imgItem;
						}
						viewHolder.setImage(R.id.img_item, imagePath, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										// 删除公司图片
										ReqBase reqBase = new ReqBase();
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("companyId",
												companyEntity.getId());
										map.put("userId", AppContext
												.getInstance().getUserInfo()
												.getId());
										map.put("attachmentIds", attachmentList
												.get(viewHolder.getPosition())
												.getAttachmentId());
										reqBase.setHeader(new ReqHead(
												AppConfig.BUSNIESS_DELETE_COMPHOTO));
										reqBase.setBody(JsonUtil
												.String2Object(JsonUtil
														.hashMapToJson(map)));
										httpPost(
												reqCodeFive,
												AppConfig.EMPLOY_REQUEST_MAPPING,
												reqBase);
										picPathList.remove(imgItem);
										setSelectPic(false);

									}
								});

					}
				} else {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.getView(R.id.add_pic_framelayout)
								.setVisibility(View.GONE);

					} else {
						String imagePath = "";
						if (imgItem.getClass().getName()
								.equals(AttachmentListDTO.class.getName())) {
							AttachmentListDTO attachmentListDTO = (AttachmentListDTO) imgItem;
							imagePath = attachmentListDTO.getClientFileUrl();
						} else {
							imagePath = (String) imgItem;
						}
						viewHolder.setImage(R.id.img_item, imagePath, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										// 删除公司图片
										ReqBase reqBase = new ReqBase();
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("companyId",
												companyEntity.getId());
										map.put("userId", AppContext
												.getInstance().getUserInfo()
												.getId());
										map.put("attachmentIds", attachmentList
												.get(viewHolder.getPosition())
												.getAttachmentId());
										reqBase.setHeader(new ReqHead(
												AppConfig.BUSNIESS_DELETE_COMPHOTO));
										reqBase.setBody(JsonUtil
												.String2Object(JsonUtil
														.hashMapToJson(map)));
										httpPost(
												reqCodeFive,
												AppConfig.EMPLOY_REQUEST_MAPPING,
												reqBase);
										picPathList.remove(imgItem);
										setSelectPic(false);

									}
								});

					}
				}
			}

		});
	}

	@Override
	public void initValue() {
		// successUrl = getIntent().getStringExtra(ConstantKey.KEY_COMPANY_URL);
		layout_logo.setOnClickListener(this);
		layout_license.setOnClickListener(this);
		et_complanyName.setOnClickListener(this);
		layout_city.setOnClickListener(this);
		layout_complanyPeople.setOnClickListener(this);
		frame_uploadvideo.setOnClickListener(this);
		findViewById(R.id.right_txt_title).setOnClickListener(this);
		findViewById(R.id.iv_location).setOnClickListener(this);
		getcompanyinformation(REFRESH_ALL);
	}

	public void getcompanyinformation(int type) {
		refreshType = type;
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_COMPLANYINFO_RT_90068));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, req, true);
	}

	/**
	 * 选择图片
	 * 
	 * @param resultCode
	 */
	private void selectPicture(int resultCode) {
		Intent intent = new Intent(this, SelectPictureActivity.class);
		intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
		intent.putExtra("MAX_NUM", 1);// 选择的张数
		startActivityForResult(intent, resultCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == TAKE_PHOTO_LOGO) {
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				showProgressDialog("图片压缩中...");
				if (pics != null) {// 多选图片返回
					new MyTask(TAKE_PHOTO_LOGO).execute(pics.get(0));
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					new MyTask(TAKE_PHOTO_LOGO).execute(photoPath);
				}
			}
			if (requestCode == TAKE_PHOTO_LICENSE) {
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				showProgressDialog("图片压缩中...");
				if (pics != null) {// 多选图片返回
					new MyTask(TAKE_PHOTO_LICENSE).execute(pics.get(0));
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					new MyTask(TAKE_PHOTO_LICENSE).execute(photoPath);
				}
			}
			if (requestCode == SELECT_COMPANYPICS) { // 选择图片
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPics = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				showProgressDialog("图片压缩中...");
				if (pics != null) {// 多选图片返回
					for (int i = 0, size = pics.size(); i < size; i++) {
						new MyTask(SELECT_COMPANYPICS, size).execute(pics
								.get(i));
					}
				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					if (!Tool.isEmpty(photoPath)) {
						new MyTask(SELECT_COMPANYPICS, 1).execute(photoPath);
					}
				}
			}
			if (requestCode == TAKE_VIDEO) {
				bitMap_video = data.getStringExtra("bitMapPath");
				videoPath_video = data.getStringExtra("videoPath");
				videoSeconds = data.getStringExtra("videoSeconds");
				LogUtil.e("hl", "报名_作品路径=" + videoPath_video);
				LogUtil.e("hl", "报名_作品缩略图=" + bitMap_video);
				dealWithPath();
			}
			if (requestCode == GET_ADDRESS) {
				et_address.setText(data.getStringExtra("address"));
				longitude = data.getFloatExtra("lng",
						(float) MrrckApplication.getInstance().longitude);
				latitude = data.getFloatExtra("lat",
						(float) MrrckApplication.getInstance().laitude);
			} else if (requestCode == SELECT_ZZPIC) {
				if (!Tool.isEmpty(data)) {
					List<String> pics = data
							.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
					showProgressDialog("图片压缩中...");
					if (pics != null) {// 多选图片返回
						for (int i = 0, size = pics.size(); i < size; i++) {
							new MyTask(SELECT_ZZPIC, size).execute(pics.get(i));
						}
					} else {// 拍照返回
						String photoPath = data
								.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
						new MyTask(SELECT_ZZPIC, 1).execute(photoPath);
					}
				}
			} else if (requestCode == Add_MORECOMPINFO) {
				getcompanyinformation(REFRESH_ZIDINGYI);
			} else if (requestCode == Add_DESIGNER) {
				getcompanyinformation(REFRESH_DESIGNER);
			}
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param photoPath
	 */
	private class MyTask extends AsyncTask<String, Integer, String> {

		private int id;
		private int size;

		public MyTask(int id) {
			this.id = id;
		}

		public MyTask(int id, int size) {
			this.id = id;
			this.size = size;
		}

		@Override
		protected void onPostExecute(String result) {
			switch (id) {
			case TAKE_PHOTO_LOGO:
				logoPicPath = result;
				BitmapUtils bitmapUtils = new BitmapUtils(
						EditCompInfoActivity.this);
				bitmapUtils.display(iv_logo, logoPicPath);
				dismissProgressDialog();
				logogetdata();
				break;
			case TAKE_PHOTO_LICENSE:
				licensePicPath = result;
				BitmapUtils bitmapUtils1 = new BitmapUtils(
						EditCompInfoActivity.this);
				bitmapUtils1.display(iv_zhizhao, licensePicPath);
				dismissProgressDialog();
				editLisense();
				break;
			case SELECT_COMPANYPICS:
				picPathList.add(picPathList.size() - 1, result);
				addphotogetdata(result);
				dismissProgressDialog();
				break;
			case SELECT_ZZPIC:
				onezzPicPath = result;
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("type", 1);
				map.put("companyId", AppContext.getInstance().getUserInfo()
						.getCompanyEntity().getId());
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				List<UploadImg> imgList = new ArrayList<UploadImg>();
				UploadImg ui = new UploadImg();
				ui.setFileType("0");
				ui.setFileUrl("");
				String picPath = onezzPicPath;
				if (!Tool.isEmpty(picPath)) {
					ui.setFileThumb(picPath.substring(
							picPath.lastIndexOf(".") + 1, picPath.length()));
				} else {
					ui.setFileThumb("");
				}
				imgList.add(ui);
				map.put("qualityHonorPhoto", JsonUtil.listToJsonArray(imgList));
				reqBase.setHeader(new ReqHead(
						AppConfig.BUSINESS_COMPLANYINFO_RT_90069));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				httpPost(REQ_ADDZZPIC, AppConfig.EMPLOY_REQUEST_MAPPING,
						reqBase);
				dismissProgressDialog();
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

	private void dealWithPath() {
		if (Tool.isEmpty(videoSeconds)) {
			MediaPlayer mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setDataSource(videoPath_video);
				mediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			int duration = mediaPlayer.getDuration();
			fileSeconds = duration / 1000;
		} else {
			fileSeconds = Integer.parseInt(videoSeconds);
		}

		int minutes = fileSeconds / 60;
		int seconds = (fileSeconds % 60000);
		String timeInMinutes = minutes > 0 ? minutes + "分" : "" + seconds + "秒";

		File f = new File(videoPath_video);
		String fSize = FileHelper.formatFileSize(f.length());
		LogUtil.e("hl", "报名_作品fileSeconds=" + fileSeconds);
		LogUtil.e("hl", "报名_作品fSize=" + fSize);
		videoInfo.setText("时长:" + timeInMinutes + "\n大小:" + fSize);

		if (f.length() / 1048576 > 10) {// 超出60s或者并且大于10M
			ToastUtil.showShortToast("视频文件太大,限制10M之内");
			videoPath_video = "";
			bitMap_video = "";
			videoSeconds = "";
		} else {
			setVideoViewShow(true);
			BitmapUtils bitmapUtils = new BitmapUtils(EditCompInfoActivity.this);
			BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
			bitmapDisplayConfig.setShowOriginal(true);
			bitmapUtils.display(frame_uploadvideo, bitMap_video,
					bitmapDisplayConfig);
			// videoInfoLayout.setVisibility(View.VISIBLE);

			updateviedio();
		}
	}

	/**
	 * 根据是否有视频显示不同view
	 * 
	 * @param b
	 */
	private void setVideoViewShow(boolean hasVideo) {
		lay_upload.setVisibility(hasVideo ? View.GONE : View.VISIBLE);
		iv_play.setVisibility(hasVideo ? View.VISIBLE : View.GONE);
		iv_delete.setVisibility(hasVideo ? View.VISIBLE : View.GONE);
		if (!hasVideo) {
			frame_uploadvideo.setBackgroundResource(R.drawable.addvideo);
		}
	}

	@Override
	public void bindListener() {
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.iv_addTeamPeople).setOnClickListener(this);
		findViewById(R.id.addOnemoreLayout).setOnClickListener(this);
	}

	@SuppressWarnings("null")
	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hl", requestCode + "认证__" + reqBase.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(reqBase.getBody().get("company"))) {

				companyEntity = (CompanyEntity) JsonUtil.jsonToObj(
						CompanyEntity.class, reqBase.getBody().get("company")
								.toString());
				if (!Tool.isEmpty(companyEntity)) {
					attachmentList = companyEntity.getAttachmentList();
					if (companyEntity.getAuthPass().equals("1")) {
						layout_license
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										ToastUtil.showShortToast("营业执照不可修改");
									}
								});
						layout_city
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
										ToastUtil.showShortToast("所在城市不可修改");
									}
								});
						et_complanyName.setEnabled(false);
						layout_name
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View arg0) {
										ToastUtil.showShortToast("企业名称不可修改");
									}
								});
						if (refreshType == REFRESH_ALL) {
							setCompanyInfo();
							initBossTypeSelected();
							zzpicPathList.clear();
							if (!Tool.isEmpty(reqBase.getBody().get(
									"qualitHonorPhotoList"))) {
								List<UserAttachmentEntity> qualitHonorPhotoList = (List<UserAttachmentEntity>) JsonUtil
										.jsonToList(
												reqBase.getBody()
														.get("qualitHonorPhotoList")
														.toString(),
												new TypeToken<List<UserAttachmentEntity>>() {
												}.getType());
								if (!Tool.isEmpty(qualitHonorPhotoList)) {
									zzpicPathList.addAll(qualitHonorPhotoList);
								}
							}
							zzpicPathList.add(new UserAttachmentEntity());
							setZZSelectPic(true);
						}
					} else if (companyEntity.getAuthPass().equals("0")) {
						btn_edit.setVisibility(View.VISIBLE);
						btn_edit.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										EditCompInfoActivity.this, "拨打电话",
										"企业信息审核中，暂时不可修改，加急审核请联系客服400-688-6800",
										"拨打", "取消");
								commonDialog.show();
								commonDialog
										.setClicklistener(new CommonDialog.ClickListenerInterface() {
											@Override
											public void doConfirm() {
												Intent intent = new Intent(
														Intent.ACTION_CALL,
														Uri.parse("tel:4006886800"));
												startActivity(intent);
												commonDialog.dismiss();
											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});
							}
						});
						findViewById(R.id.right_txt_title).setVisibility(
								View.GONE);
						((TextView) findViewById(R.id.center_txt_title))
								.setText("企业信息(审核中)");
					}
				}
			}
			switch (refreshType) {
			case REFRESH_ALL:
				if (!Tool.isEmpty(reqBase.getBody().get("designersList"))) {
					designerInfoList = (ArrayList<CompanyDesignersEntity>) JsonUtil
							.jsonToList(
									reqBase.getBody().get("designersList")
											.toString(),
									new TypeToken<List<CompanyDesignersEntity>>() {
									}.getType());
					layout_teamGroup.removeAllViews();
					for (int i = 0; i < designerInfoList.size(); i++) {
						addOneDesignerItem(i, designerInfoList.get(i));
					}
				}
				if (!Tool.isEmpty(reqBase.getBody().get("customList"))) {
					detailsInfoList = (ArrayList<CompanyCustomContentEntity>) JsonUtil
							.jsonToList(
									reqBase.getBody().get("customList")
											.toString(),
									new TypeToken<List<CompanyCustomContentEntity>>() {
									}.getType());
					layout_moreGroup.removeAllViews();
					for (int i = 0; i < detailsInfoList.size(); i++) {
						if (!Tool.isEmpty(detailsInfoList.get(i).getFileList())) {
							ArrayList<String> pics = new ArrayList<String>();
							ArrayList<String> picIds = new ArrayList<String>();
							for (int j = 0, picSize = detailsInfoList.get(i)
									.getFileList().size(); j < picSize; j++) {
								pics.add(detailsInfoList.get(i).getFileList()
										.get(j).getClientFileUrl());
								picIds.add(detailsInfoList.get(i).getFileList()
										.get(j).getId()
										+ "");
							}
							detailsInfoList.get(i).setPics(pics);
							detailsInfoList.get(i).setIds(picIds);
						}
					}

					for (int i = 0; i < detailsInfoList.size(); i++) {
						addOneCompanyDetailItem(i, detailsInfoList.get(i));
					}
				}
				break;
			case REFRESH_DESIGNER:
				if (!Tool.isEmpty(reqBase.getBody().get("designersList"))) {
					designerInfoList = (ArrayList<CompanyDesignersEntity>) JsonUtil
							.jsonToList(
									reqBase.getBody().get("designersList")
											.toString(),
									new TypeToken<List<CompanyDesignersEntity>>() {
									}.getType());
					layout_teamGroup.removeAllViews();
					for (int i = 0; i < designerInfoList.size(); i++) {
						addOneDesignerItem(i, designerInfoList.get(i));
					}
				}

				break;
			case REFRESH_ZIDINGYI:
				if (!Tool.isEmpty(reqBase.getBody().get("customList"))) {
					detailsInfoList = (ArrayList<CompanyCustomContentEntity>) JsonUtil
							.jsonToList(
									reqBase.getBody().get("customList")
											.toString(),
									new TypeToken<List<CompanyCustomContentEntity>>() {
									}.getType());
					layout_moreGroup.removeAllViews();
					for (int i = 0; i < detailsInfoList.size(); i++) {
						if (!Tool.isEmpty(detailsInfoList.get(i).getFileList())) {
							ArrayList<String> pics = new ArrayList<String>();
							ArrayList<String> picIds = new ArrayList<String>();
							for (int j = 0, picSize = detailsInfoList.get(i)
									.getFileList().size(); j < picSize; j++) {
								pics.add(detailsInfoList.get(i).getFileList()
										.get(j).getClientFileUrl());
								picIds.add(detailsInfoList.get(i).getFileList()
										.get(j).getId()
										+ "");
							}
							detailsInfoList.get(i).setPics(pics);
							detailsInfoList.get(i).setIds(picIds);
						}
					}

					for (int i = 0; i < detailsInfoList.size(); i++) {
						addOneCompanyDetailItem(i, detailsInfoList.get(i));
					}
				}
				break;
			case REFRESH_ZIZHI:
				zzpicPathList.clear();
				if (!Tool
						.isEmpty(reqBase.getBody().get("qualitHonorPhotoList"))) {
					List<UserAttachmentEntity> qualitHonorPhotoList = (List<UserAttachmentEntity>) JsonUtil
							.jsonToList(
									reqBase.getBody()
											.get("qualitHonorPhotoList")
											.toString(),
									new TypeToken<List<UserAttachmentEntity>>() {
									}.getType());
					if (!Tool.isEmpty(qualitHonorPhotoList)) {
						zzpicPathList.addAll(qualitHonorPhotoList);
					}
				}
				zzpicPathList.add(new UserAttachmentEntity());
				setZZSelectPic(true);
				break;
			default:
				break;
			}
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("修改成功");
			setResult(RESULT_OK);
			finish();
			break;
		case reqCodeThree:// 修改logo
			if (!Tool.isEmpty(reqBase.getBody().get("logo"))) {
				JsonArray picJsonArray = reqBase.getBody().get("logo")
						.getAsJsonArray();
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", picJsonArray);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("photo_logo.png");
				mainFfb.setFile(new File(logoPicPath));
				LogUtil.d("hl", "上传修改logo图片__" + logoPicPath);
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(REQ_CHANGE_LOGO, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, true);
			}
			break;
		case reqCodeFour:
			if (!Tool.isEmpty(reqBase.getBody().get("photo"))) {
				JsonArray picJsonArray = reqBase.getBody().get("photo")
						.getAsJsonArray();
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", picJsonArray);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("photo_company.png");
				mainFfb.setFile(new File(currentAddCompPicPath));
				LogUtil.d("hl", "上传公司图片__" + currentAddCompPicPath);
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(REQ_CHANGE_COMPPIC, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, true);
			}
			break;
		case reqCodeFive:
			ToastUtil.showShortToast("删除成功");
			if ((reqBase.getBody().get("company") + "").length() > 2) {
				companyEntity = (CompanyEntity) JsonUtil.jsonToObj(
						CompanyEntity.class, reqBase.getBody().get("company")
								.toString());
				attachmentList = companyEntity.getAttachmentList();
				List<UserAttachmentEntity> oldPic = companyEntity
						.getAttachmentList();
				if (!Tool.isEmpty(oldPic)) {
					picPathList.clear();
					for (int i = 0, size = oldPic.size(); i < size; i++) {
						picPathList.add(oldPic.get(i).getClientFileUrl());
					}
					picPathList.add("+");
					setSelectPic(false);
				}
			}
			// getcompanyinformation();
			break;
		case reqCodeSix:
			ToastUtil.showShortToast("删除成功");
			setVideoViewShow(false);
			break;
		case reqCodeSeven:
			if (!Tool.isEmpty(reqBase.getBody().get("companyVideoPhoto"))) {
				JsonArray picJsonArray = reqBase.getBody()
						.get("companyVideoPhoto").getAsJsonArray();
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", picJsonArray);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("photo_video.png");
				mainFfb.setFile(new File(bitMap_video));
				LogUtil.d("hl", "上传修改视频缩略图__" + bitMap_video);
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(REQ_CHANGE_VIDEOPIC, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, true);
			}

			if (!Tool.isEmpty(reqBase.getBody().get("companyVideo"))) {
				JsonArray picJsonArray = reqBase.getBody().get("companyVideo")
						.getAsJsonArray();
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", picJsonArray);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("video.mp4");
				mainFfb.setFile(new File(videoPath_video));
				LogUtil.d("hl", "上传修改视频__" + videoPath_video);
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(REQ_CHANGE_VIDEO, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, true);
			}
			break;
		case reqCodeEight:
			setResult(RESULT_OK);
			ToastUtil.showShortToast("上传成功");
			break;
		case REQ_DELETE_DESIGNER:
			setResult(RESULT_OK);
			getcompanyinformation(REFRESH_DESIGNER);
			break;
		case REQ_DELETE_CUSTOM:
			setResult(RESULT_OK);
			getcompanyinformation(REFRESH_ZIDINGYI);
			break;
		case REQ_ADDZZPIC:
			if (!Tool.isEmpty(reqBase.getBody().get("qualityHonorPhoto"))) {
				JsonArray picJsonArray = reqBase.getBody()
						.get("qualityHonorPhoto").getAsJsonArray();
				ReqBase req = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileUrlJSONArray", picJsonArray);
				req.setHeader(new ReqHead(AppConfig.BUSINESS_FILE_IMG10002));
				req.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
				FormFileBean mainFfb = new FormFileBean();
				mainFfb.setFileName("photo_zizhi.png");
				mainFfb.setFile(new File(onezzPicPath));
				LogUtil.d("hl", "上传资质图片__" + onezzPicPath);
				details_FileBeans.add(mainFfb);
				mapFileBean.put("file", details_FileBeans);
				uploadResFiles(REQ_ADD_ZZPIC, AppConfig.PUBLICK_UPLOAD,
						mapFileBean, req, true);
			}
			break;
		case REQ_ADD_ZZPIC:
		case REQ_DELETE_ZZPIC:
			getcompanyinformation(REFRESH_ZIZHI);
			break;
		case REQ_CHANGE_LOGO:
			setResult(RESULT_OK);
			ToastUtil.showShortToast("企业LOGO上传成功");
			getcompanyinformation(REFRESH_ALL);
			break;
		// case REQ_CHANGE_VIDEOPIC:
		case REQ_CHANGE_VIDEO:
			setResult(RESULT_OK);
			REFRESH_BODYFLAG = 1;
			ToastUtil.showShortToast("企业视频上传成功");
			getcompanyinformation(REFRESH_ALL);
			break;
		case REQ_CHANGE_COMPPIC:
			ToastUtil.showShortToast("上传成功");
			REFRESH_BODYFLAG = 2;
			setResult(RESULT_OK);
			getcompanyinformation(REFRESH_ALL);
			break;
		default:
			break;
		}
	}

	// 绘制企业信息
	private void setCompanyInfo() {
		if (REFRESH_BODYFLAG == 0) {
			iv_logo = new MySimpleDraweeView(EditCompInfoActivity.this);
			lin_logo.removeAllViews();
			lin_logo.addView(iv_logo, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			iv_logo.setUrlOfImage(companyEntity.getClientCompanyLogo());
			iv_zhizhao = new MySimpleDraweeView(EditCompInfoActivity.this);
			lin_zhizhao.removeAllViews();
			lin_zhizhao.addView(iv_zhizhao, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			iv_zhizhao.setUrlOfImage(companyEntity.getClientLicense());
			et_complanyName.setText(companyEntity.getName());
			tv_city.setText(companyEntity.getCityName());
			cityCode = companyEntity.getCityCode() + "";
			et_address.setText(companyEntity.getAddress());
			tv_complanyPeople.setText(companyEntity.getScaleName());
			companyScaleCodeId = companyEntity.getScale() + "";
			et_contact.setText(companyEntity.getContactName());
			et_phoneNum.setText(companyEntity.getPhone());
			et_website.setText(companyEntity.getWebsite());
			et_email.setText(companyEntity.getEmail());
			et_introduce.setText(companyEntity.getIntroduce());
			bossType = companyEntity.getBossType();
			bossTypeName = companyEntity.getBossTypeName();
			et_zzry.setText(companyEntity.getQualityHonor());
			// 企业logo
			logoPicPath = companyEntity.getClientCompanyLogo();
			// 企业视频
			if (Tool.isEmpty(companyEntity.getClientVideo())) {
				setVideoViewShow(false);
			} else {
				setVideoViewShow(true);
				videoPath_video = companyEntity.getClientVideo();
				BitmapUtils bitmapUtils = new BitmapUtils(
						EditCompInfoActivity.this);
				BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
				bitmapDisplayConfig.setShowOriginal(true);
				bitmapUtils.display(frame_uploadvideo,
						companyEntity.getClientVideoPhoto(),
						bitmapDisplayConfig);
			}

			// 企业照片
			List<UserAttachmentEntity> oldPic = companyEntity
					.getAttachmentList();
			picPathList.clear();
			if (!Tool.isEmpty(oldPic)) {
				for (int i = 0, size = oldPic.size(); i < size; i++) {
					picPathList.add(oldPic.get(i).getClientFileUrl());
				}
			}
			picPathList.add("+");
			setSelectPic(false);
		}
		if (REFRESH_BODYFLAG == 1) {
			// 企业logo
			logoPicPath = companyEntity.getClientCompanyLogo();
			// 企业视频
			if (Tool.isEmpty(companyEntity.getClientVideo())) {
				setVideoViewShow(false);
			} else {
				setVideoViewShow(true);
				videoPath_video = companyEntity.getClientVideo();
				BitmapUtils bitmapUtils = new BitmapUtils(
						EditCompInfoActivity.this);
				BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
				bitmapDisplayConfig.setShowOriginal(true);
				bitmapUtils.display(frame_uploadvideo,
						companyEntity.getClientVideoPhoto(),
						bitmapDisplayConfig);
			}
		}
		if (REFRESH_BODYFLAG == 2) {
			// 企业照片
			List<UserAttachmentEntity> oldPic = companyEntity
					.getAttachmentList();
			picPathList.clear();
			if (!Tool.isEmpty(oldPic)) {
				for (int i = 0, size = oldPic.size(); i < size; i++) {
					picPathList.add(oldPic.get(i).getClientFileUrl());
				}
			}
			picPathList.add("+");
			setSelectPic(false);
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("获取企业信息失败");
			finish();
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("修改失败");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("修改失败");
			break;
		case reqCodeFour:
			ToastUtil.showShortToast("上传失败");
			break;
		default:
			break;
		}
		/**
		 * if (!Tool.isEmpty(videoPath_video)) {
		 * ToastUtil.showShortToast("认证失败!建议上传的视频不要太大!"); } else {
		 * ToastUtil.showShortToast("失败"); }
		 */
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_logo:
			selectPicture(TAKE_PHOTO_LOGO);
			break;
		case R.id.layout_license:
			selectPicture(TAKE_PHOTO_LICENSE);
			break;
		case R.id.layout_city:
			new WheelSelectCityDialog(EditCompInfoActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_city.setText(cityName);
							EditCompInfoActivity.this.cityCode = cityCode + "";
						}

					}).show();
			break;
		case R.id.layout_complanyPeople:
			new ChooseTextDialog(EditCompInfoActivity.this, "选择类型",
					allScaleStr, new ChooseOneStrListener() {

						@Override
						public void doChoose(int position, String s) {
							tv_complanyPeople.setText(s);
							companyScaleCodeId = companyScaleData.get(position)
									.getCodeId();
						}
					}).show();
			break;
		case R.id.frame_top:
			startActivityForResult(new Intent(EditCompInfoActivity.this,
					SelectVedioActivity.class), TAKE_VIDEO);
			break;
		case R.id.right_txt_title:
			if (checkIsReady()) {
				doUploadData();
			}
			break;
		case R.id.iv_location:
			startActivityForResult(
					new Intent(this, GetLoacationActivity.class), GET_ADDRESS);
		case R.id.videoReSet:
			lay_upload.setVisibility(View.VISIBLE);
			iv_play.setVisibility(View.GONE);
			frame_uploadvideo.setBackgroundResource(R.drawable.addvideo);
			videoPath_video = "";
			bitMap_video = "";
			videoSeconds = "";
			fileSeconds = 0;
			videoInfoLayout.setVisibility(View.GONE);
			break;
		case R.id.goback:
			finishWhenTip();
			break;
		case R.id.iv_addTeamPeople:
			startActivityForResult(new Intent(EditCompInfoActivity.this,
					AddOneDesignerInfoActivity.class).putExtra("useType", 1),
					Add_DESIGNER);
			break;
		case R.id.addOnemoreLayout:
			startActivityForResult(new Intent(EditCompInfoActivity.this,
					AddOneCompDetailActivity.class).putExtra("useType", 1),
					Add_MORECOMPINFO);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishWhenTip();
		}
		return false;
	}

	private void finishWhenTip() {
		if (commonDialog != null && !commonDialog.isShowing()) {
			commonDialog.show();
		} else {
			finish();
		}
	}

	/**
	 * 上传企业数据
	 */
	private void doUploadData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", companyEntity.getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("name", et_complanyName.getText().toString().trim());
		String sortString = PinyinUtil.getTerm(et_complanyName.getText()
				.toString().trim());
		map.put("nameFirstChar", sortString.matches("[A-Z]") ? sortString : "#");
		map.put("cityCode", cityCode);
		map.put("address", et_address.getText().toString().trim());
		map.put("scale", companyScaleCodeId);
		map.put("website", et_website.getText().toString().trim());
		map.put("email", et_email.getText().toString().trim());
		map.put("contactName", et_contact.getText().toString().trim());
		map.put("phone", et_phoneNum.getText().toString().trim());
		map.put("introduce", et_introduce.getText().toString().trim());
		map.put("bossType", bossType);
		map.put("bossTypeName", bossTypeName);
		map.put("qualityHonor", et_zzry.getText().toString());
		map.put("longitude", MrrckApplication.getInstance().getLongitudeStr());//
		map.put("latitude", MrrckApplication.getInstance().getLaitudeStr()); //
		map.put("authPass", companyEntity.getAuthPass());
		LogUtil.d("hl", "公司认证请求__" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_COMPLANYINFO_UPDATE));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
	}

	// 修改企业logo
	public void logogetdata() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", companyEntity.getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		List<UploadImg> imgList = new ArrayList<UploadImg>();
		UploadImg ui = new UploadImg();
		ui.setFileType("0");
		ui.setFileUrl("");
		String picPath = logoPicPath;
		if (!Tool.isEmpty(picPath)) {
			ui.setFileThumb(picPath.substring(picPath.lastIndexOf(".") + 1,
					picPath.length()));
		} else {
			ui.setFileThumb("");
		}
		imgList.add(ui);
		map.put("logo", JsonUtil.listToJsonArray(imgList));
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_RT_90072));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
	}

	// 修改营业执照
	public void editLisense() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", companyEntity.getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_MODIFY_COMPANY_LICENSE));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (!Tool.isEmpty(licensePicPath)) {
			List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
			logo_FileBeans.add(new FormFileBean(new File(licensePicPath),
					"license.png"));
			mapFileBean.put("photo", logo_FileBeans);
		}
		uploadFiles(reqCodeEight, AppConfig.EMPLOY_REQUEST_MAPPING,
				mapFileBean, reqBase, true);
	}

	// 增加公司图片
	public void addphotogetdata(String result) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", companyEntity.getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		List<UploadImg> imgList = new ArrayList<UploadImg>();
		UploadImg ui = new UploadImg();
		ui.setFileType("0");
		ui.setFileUrl("");
		currentAddCompPicPath = result;
		if (!Tool.isEmpty(currentAddCompPicPath)) {
			ui.setFileThumb(currentAddCompPicPath.substring(
					currentAddCompPicPath.lastIndexOf(".") + 1,
					currentAddCompPicPath.length()));
		} else {
			ui.setFileThumb("");
		}
		imgList.add(ui);
		map.put("photo", JsonUtil.listToJsonArray(imgList));
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_RT_90071));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeFour, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
	}

	// 删除企业视频
	public void deleteviedio() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", companyEntity.getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSNIESS_DELETE_COMPANYVIDEO));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeSix, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
	}

	// 修改企业视频接口
	public void updateviedio() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", companyEntity.getId());
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		if (!Tool.isEmpty(videoPath_video)) {
			map.put("videoSeconds", fileSeconds);
		}
		List<UploadImg> videoList = new ArrayList<UploadImg>();
		UploadImg uii = new UploadImg();
		uii.setFileType("0");
		uii.setFileUrl("");
		String videoPath = videoPath_video;
		if (!Tool.isEmpty(videoPath)) {
			uii.setFileThumb(videoPath.substring(
					videoPath.lastIndexOf(".") + 1, videoPath.length()));
		} else {
			uii.setFileThumb("");
		}
		videoList.add(uii);
		map.put("companyVideo", JsonUtil.listToJsonArray(videoList));

		List<UploadImg> imgList = new ArrayList<UploadImg>();
		UploadImg ui = new UploadImg();
		ui.setFileType("0");
		ui.setFileUrl("");
		String picPath = bitMap_video;
		if (!Tool.isEmpty(picPath)) {
			ui.setFileThumb(picPath.substring(picPath.lastIndexOf(".") + 1,
					picPath.length()));
		} else {
			ui.setFileThumb("");
		}
		imgList.add(ui);
		map.put("companyVideoPhoto", JsonUtil.listToJsonArray(imgList));
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_RT_90070));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeSeven, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
	}

	/**
	 * 添加一条详情
	 * 
	 * @param position
	 * @param isEdit
	 * @param title
	 * @param detail
	 * @param pics
	 */
	private void addOneCompanyDetailItem(final int position,
			final CompanyCustomContentEntity detailInfo) {
		View itemView = LayoutInflater.from(EditCompInfoActivity.this).inflate(
				R.layout.include_addproductinfo, null, false);
		EditText editTitle = (EditText) itemView.findViewById(R.id.editTitle);
		editTitle.setText(detailInfo.getTitle());
		editTitle.setEnabled(false);
		EditText editDetail = (EditText) itemView.findViewById(R.id.editDetail);
		Button editBtn = (Button) itemView.findViewById(R.id.edit);
		editDetail.setText(detailInfo.getContent());
		editDetail.setEnabled(false);
		MyGridView gridview_uploadpic = (MyGridView) itemView
				.findViewById(R.id.gridview_uploadpic);
		editBtn.setVisibility(View.VISIBLE);
		editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(EditCompInfoActivity.this,
						AddOneCompDetailActivity.class).putExtra("useType", 2)
						.putExtra("oldData", JsonUtil.objToJson(detailInfo)),
						Add_MORECOMPINFO);
			}
		});
		setGridPic2(gridview_uploadpic, detailInfo.getPics(), position);
		Button deleteIcon = (Button) itemView.findViewById(R.id.delete);
		deleteIcon.setVisibility(View.VISIBLE);
		// 删除按钮监听
		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CommonDialog deleteTipDialog = new CommonDialog(
						EditCompInfoActivity.this, "提示", "确定删除本条详情?", "确定",
						"取消");
				deleteTipDialog
						.setClicklistener(new CommonDialog.ClickListenerInterface() {

							@Override
							public void doConfirm() {
								// detailsInfoList.remove(position);
								// layout_moreGroup.removeAllViews();
								// for (int i = 0; i < detailsInfoList.size();
								// i++) {
								// addOneCompanyDetailItem(i,
								// detailsInfoList.get(i));
								// }
								deleteTipDialog.dismiss();
								ReqBase reqBase = new ReqBase();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("type", 3);
								map.put("companyId", AppContext.getInstance()
										.getUserInfo().getCompanyEntity()
										.getId());
								map.put("companyCustomContent", JsonUtil
										.Entity2JsonObj(detailsInfoList
												.get(position)));
								reqBase.setHeader(new ReqHead(
										AppConfig.BUSINESS_COMPLANYINFO_RT_90066));
								reqBase.setBody(JsonUtil.String2Object(JsonUtil
										.hashMapToJson(map)));
								httpPost(REQ_DELETE_CUSTOM,
										AppConfig.EMPLOY_REQUEST_MAPPING,
										reqBase);
								deleteTipDialog.dismiss();
							}

							@Override
							public void doCancel() {
								deleteTipDialog.dismiss();
							}
						});
				deleteTipDialog.show();
			}
		});
		editTitle.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				detailsInfoList.get(position).setTitle(editable.toString());
			}
		});
		// 产品详细输入框监听
		editDetail.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				detailsInfoList.get(position).setContent(editable.toString());
			}
		});
		layout_moreGroup.addView(itemView);
	}

	private void setGridPic2(final MyGridView gridview,
			final ArrayList<String> pisList, final int position) {
		gridview.setAdapter(new CommonAdapter<String>(this,
				R.layout.item_pic_product, pisList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final String imgItem) {
				viewHolder.setImage(R.id.img_item, imgItem, 0);
				viewHolder.getView(R.id.delete_btn).setVisibility(View.GONE);
			}
		});
	}

	private void addOneDesignerItem(final int position,
			final CompanyDesignersEntity cde) {
		View itemView = LayoutInflater.from(EditCompInfoActivity.this).inflate(
				R.layout.item_adddesigner, null, false);
		EditText editName = (EditText) itemView.findViewById(R.id.name);
		editName.setText(cde.getName());
		editName.setEnabled(false);
		EditText editDetail = (EditText) itemView.findViewById(R.id.detail);
		ImageView headPic = (ImageView) itemView.findViewById(R.id.headPic);
		headPic.setBackgroundResource(R.drawable.gg_icon);
		LinearLayout editLayout = (LinearLayout) itemView
				.findViewById(R.id.edit);
		editDetail.setText(cde.getPersonalNote());
		editDetail.setEnabled(false);
		editLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(
						new Intent(EditCompInfoActivity.this,
								AddOneDesignerInfoActivity.class).putExtra(
								"useType", 2).putExtra("oldData",
								JsonUtil.objToJson(cde)), Add_DESIGNER);
			}
		});
		if (!Tool.isEmpty(cde.getClientHeadUrl())) {
			BitmapUtils bitmapUtils = new BitmapUtils(EditCompInfoActivity.this);
			bitmapUtils.display(headPic, cde.getClientHeadUrl());
		}
		LinearLayout deleteIcon = (LinearLayout) itemView
				.findViewById(R.id.delete);
		deleteIcon.setVisibility(View.VISIBLE);
		// 删除按钮监听
		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CommonDialog deleteTipDialog = new CommonDialog(
						EditCompInfoActivity.this, "提示", "确定删除本条详情?", "确定",
						"取消");
				deleteTipDialog
						.setClicklistener(new CommonDialog.ClickListenerInterface() {

							@Override
							public void doConfirm() {
								// designerInfoList.remove(position);
								// layout_teamGroup.removeAllViews();
								// for (int i = 0; i < designerInfoList.size();
								// i++) {
								// addOneDesignerItem(i,
								// designerInfoList.get(i));
								// }
								ReqBase reqBase = new ReqBase();
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("type", 3);
								map.put("companyId", AppContext.getInstance()
										.getUserInfo().getCompanyEntity()
										.getId());
								map.put("designers", JsonUtil
										.Entity2JsonObj(designerInfoList
												.get(position)));
								reqBase.setHeader(new ReqHead(
										AppConfig.BUSINESS_COMPLANYINFO_RT_90067));
								reqBase.setBody(JsonUtil.String2Object(JsonUtil
										.hashMapToJson(map)));
								httpPost(REQ_DELETE_DESIGNER,
										AppConfig.EMPLOY_REQUEST_MAPPING,
										reqBase);
								deleteTipDialog.dismiss();
							}

							@Override
							public void doCancel() {
								deleteTipDialog.dismiss();
							}
						});
				deleteTipDialog.show();
			}
		});
		editName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				designerInfoList.get(position).setName(editable.toString());
			}
		});
		// 产品详细输入框监听
		editDetail.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i2, int i3) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i2,
					int i3) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				designerInfoList.get(position).setPersonalNote(
						editable.toString());
			}
		});
		layout_teamGroup.addView(itemView);
	}

	private void setZZSelectPic(boolean isAdd) {
		gridview_uploadzzpic
				.setAdapter(new CommonAdapter<UserAttachmentEntity>(this,
						R.layout.add_talent_pic_gridview_item, zzpicPathList) {
					@Override
					public void convert(ViewHolder viewHolder,
							final UserAttachmentEntity imgItem) {
						// 如果大于8个删除第一个图片也就是增加图片按钮
						if (zzpicPathList.size() < 9) {
							if (imgItem == getItem(zzpicPathList.size() - 1)) {
								viewHolder.setImage(R.id.img_item,
										R.drawable.clickaddpic);
								viewHolder.getView(R.id.delete_btn)
										.setVisibility(View.INVISIBLE);
								viewHolder.getView(R.id.img_item)
										.setOnClickListener(
												new View.OnClickListener() {
													@Override
													public void onClick(View v) {
														Intent intent = new Intent(
																EditCompInfoActivity.this,
																SelectPictureActivity.class);
														intent.putExtra(
																"SELECT_MODE",
																SelectPictureActivity.MODE_SINGLE);// 选择模式
														int picNum = 9 - zzpicPathList
																.size();
														intent.putExtra(
																"MAX_NUM",
																(picNum > 0) ? picNum
																		: 0);// 选择的张数
														startActivityForResult(
																intent,
																SELECT_ZZPIC);
													}
												});

							} else {
								String imagePath = "";
								if (imgItem
										.getClass()
										.getName()
										.equals(UserAttachmentEntity.class
												.getName())) {
									UserAttachmentEntity attachmentListDTO = (UserAttachmentEntity) imgItem;
									imagePath = attachmentListDTO
											.getClientFileUrl();
								}
								viewHolder
										.setImage(R.id.img_item, imagePath, 0);
								viewHolder.getView(R.id.delete_btn)
										.setOnClickListener(
												new View.OnClickListener() {
													@Override
													public void onClick(View v) {
														ReqBase reqBase = new ReqBase();
														Map<String, Object> map = new HashMap<String, Object>();
														map.put("type", 2);
														map.put("companyId",
																AppContext
																		.getInstance()
																		.getUserInfo()
																		.getCompanyEntity()
																		.getId());
														map.put("userId",
																AppContext
																		.getInstance()
																		.getUserInfo()
																		.getId());
														map.put("delAttachmentIds",
																imgItem.getId());
														reqBase.setHeader(new ReqHead(
																AppConfig.BUSINESS_COMPLANYINFO_RT_90069));
														reqBase.setBody(JsonUtil
																.String2Object(JsonUtil
																		.hashMapToJson(map)));
														httpPost(
																REQ_DELETE_ZZPIC,
																AppConfig.EMPLOY_REQUEST_MAPPING,
																reqBase);
													}
												});

							}
						} else {
							if (imgItem == getItem(zzpicPathList.size() - 1)) {
								viewHolder.getView(R.id.add_pic_framelayout)
										.setVisibility(View.GONE);
							} else {
								String imagePath = "";
								if (imgItem
										.getClass()
										.getName()
										.equals(UserAttachmentEntity.class
												.getName())) {
									UserAttachmentEntity attachmentListDTO = (UserAttachmentEntity) imgItem;
									imagePath = attachmentListDTO
											.getClientFileUrl();
								}
								viewHolder
										.setImage(R.id.img_item, imagePath, 0);
								viewHolder.getView(R.id.delete_btn)
										.setOnClickListener(
												new View.OnClickListener() {
													@Override
													public void onClick(View v) {
														ReqBase reqBase = new ReqBase();
														Map<String, Object> map = new HashMap<String, Object>();
														map.put("type", 2);
														map.put("companyId",
																AppContext
																		.getInstance()
																		.getUserInfo()
																		.getCompanyEntity()
																		.getId());
														map.put("userId",
																AppContext
																		.getInstance()
																		.getUserInfo()
																		.getId());
														map.put("delAttachmentIds",
																imgItem.getId());
														reqBase.setHeader(new ReqHead(
																AppConfig.BUSINESS_COMPLANYINFO_RT_90069));
														reqBase.setBody(JsonUtil
																.String2Object(JsonUtil
																		.hashMapToJson(map)));
														httpPost(
																REQ_DELETE_ZZPIC,
																AppConfig.EMPLOY_REQUEST_MAPPING,
																reqBase);
													}
												});

							}
						}
					}

				});
	}
}
