package com.meiku.dev.ui.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView.ScrollYChangeListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.CompanyCustomContentEntity;
import com.meiku.dev.bean.CompanyDesignersEntity;
import com.meiku.dev.bean.CompanyEntity;
import com.meiku.dev.bean.CompanyYZResult;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.decoration.MyDecorationPublishActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.ui.morefun.TestVideoActivity;
import com.meiku.dev.ui.product.PublishProductActivity;
import com.meiku.dev.ui.recruit.AddOneCompDetailActivity;
import com.meiku.dev.ui.recruit.AddOneDesignerInfoActivity;
import com.meiku.dev.ui.recruit.RecruitMainActivity;
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
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;

/**
 * 公司认证
 * 
 * @author Administrator
 * 
 */
public class CompanyCertificationActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout layout_logo;
	private ImageView iv_logo;
	private LinearLayout layout_license;
	private EditText et_complanyName;
	private LinearLayout layout_city;
	private EditText et_address;
	private TextView tv_city;
	private String cityCode;
	// private String provinceCode;
	private LinearLayout layout_complanyPeople;
	private TextView tv_complanyPeople;
	private EditText et_contact, et_website, et_email, et_zzry;
	private EditText et_phoneNum;
	private EditText et_introduce;
	private FrameLayout frame_uploadvideo;
	private ImageView iv_play;
	private MyGridView gridview_uploadpic, gridview;
	private List<Object> picPathList = new ArrayList<Object>();// 上传公司图片--选择的图片路径
	private List<Object> zzpicPathList = new ArrayList<Object>();// 资质图片--选择的图片路径
	private final int TAKE_PHOTO_LOGO = 1001;// LOGO选择
	private final int TAKE_PHOTO_LICENSE = 1002;// 执照选择
	private final int SELECT_COMPANYPICS = 1003;// 执照选择
	private final int TAKE_VIDEO = 1004;// 视频
	private final int GET_ADDRESS = 1005;// 地址
	private final int SELECT_ZZPIC = 1006;// 资质图片选择
	private final int Add_MORECOMPINFO = 1007;// 添加自定义项
	private final int Add_DESIGNER = 1008;// 添加设计师
	private CommonAdapter<DataconfigEntity> typeAdapter;
	private ArrayList<String> allScaleStr = new ArrayList<String>();// 规模待选
	private String bitMap_video;
	private String videoPath_video;
	private ImageView iv_zhizhao;
	private String logoPicPath;// logo图片路径
	private String licensePicPath;// 执照图片路径
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
	public int addpicCount, addZZpicCount;
	private String successUrl;
	private String flag;
	private MyGridView gridview_uploadzzpic;
	private ArrayList<CompanyCustomContentEntity> detailsInfoList = new ArrayList<CompanyCustomContentEntity>();
	private ArrayList<CompanyDesignersEntity> designerInfoList = new ArrayList<CompanyDesignersEntity>();
	private LinearLayout layout_moreGroup;
	private LinearLayout layout_teamGroup;
	private boolean Upload_logo_result = true;
	private boolean Upload_license_result = true;
	private boolean Upload_compPic_result = true;
	private boolean Upload_compZZPic_result = true;
	private boolean Upload_designerPic_result = true;
	private boolean Upload_zdyPic_result = true;
	private boolean Upload_videoPic_result = true;
	private boolean Upload_video_result = true;
	private int Num_uploadedCompPic;
	private int Num_uploadedZZPic;
	private int Num_designerPic;
	private ArrayList<String> allZDYPic;
	private int Num_zdyPic;
	private PullToRefreshScrollView pull_refreshSV;
	protected int currentScroolY;
	private int allCustomCententPicSize;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_companycertification;
	}

	@Override
	public void initView() {
		pull_refreshSV = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshView);
		pull_refreshSV.setMode(PullToRefreshBase.Mode.DISABLED);
		pull_refreshSV.addScrollYChangeListener(new ScrollYChangeListener() {

			@Override
			public void onScrollYChanged(int y) {
				currentScroolY = y;
			}
		});
		// logo
		layout_logo = (LinearLayout) findViewById(R.id.layout_logo);
		iv_logo = (ImageView) findViewById(R.id.iv_logo);
		// 企业营业执照
		layout_license = (LinearLayout) findViewById(R.id.layout_license);
		iv_zhizhao = (ImageView) findViewById(R.id.iv_zhizhao);
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
		et_zzry = (EditText) findViewById(R.id.et_zzry);
		et_contact
				.setText(AppContext.getInstance().getUserInfo().getRealName());
		// 联系电话
		et_phoneNum = (EditText) findViewById(R.id.et_phoneNum);
		et_phoneNum.setText(AppContext.getInstance().getUserInfo().getPhone());
		// 企业简介
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		// 上传视频
		initUploadVideo();
		// 照片上传
		gridview_uploadpic = (MyGridView) findViewById(R.id.gridview_uploadpic);
		picPathList.add("+");
		setSelectPic(true);
		// 老板类型
		initBossTypeSelected();
		// 资质图片
		gridview_uploadzzpic = (MyGridView) findViewById(R.id.gridview_uploadzzpic);
		zzpicPathList.add("+");
		setZZSelectPic(true);
		layout_moreGroup = (LinearLayout) findViewById(R.id.layout_moreGroup);
		layout_teamGroup = (LinearLayout) findViewById(R.id.layout_teamGroup);
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
				intent.setClass(CompanyCertificationActivity.this,
						TestVideoActivity.class);
				startActivity(intent);
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
		if (Tool.isEmpty(licensePicPath)) {
			ToastUtil.showShortToast("请上传企业营业执照");
			return false;
		}
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
		 if (Tool.isEmpty(bossType) || Tool.isEmpty(bossTypeName)) {
		 ToastUtil.showShortToast("请选择老板性格标签");
		 return false;
		 }
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

	private void initBossTypeSelected() {
		bossTypesData = MKDataBase.getInstance().getBossTypes();
		gridview = (MyGridView) findViewById(R.id.gridview);
		typeAdapter = new CommonAdapter<DataconfigEntity>(
				CompanyCertificationActivity.this, R.layout.item_xingqu,
				bossTypesData) {

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
			public void convert(ViewHolder viewHolder, final Object imgItem) {
				// 如果大于9个删除第一个图片也就是增加图片按钮
				if (picPathList.size() < 9) {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.setImage(R.id.img_item,
								R.drawable.addcompanypic);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												CompanyCertificationActivity.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 9 - picPathList.size();
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
		flag = getIntent().getStringExtra("flag");
		successUrl = getIntent().getStringExtra(ConstantKey.KEY_COMPANY_URL);
		layout_logo.setOnClickListener(this);
		layout_license.setOnClickListener(this);
		layout_city.setOnClickListener(this);
		layout_complanyPeople.setOnClickListener(this);
		frame_uploadvideo.setOnClickListener(this);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.iv_location).setOnClickListener(this);
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
			} else if (requestCode == TAKE_PHOTO_LICENSE) {// 选择执照
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				showProgressDialog("图片压缩中...");
				if (pics != null) {
					new MyTask(TAKE_PHOTO_LICENSE).execute(pics.get(0));
				} else {
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					new MyTask(TAKE_PHOTO_LICENSE).execute(photoPath);
				}
			} else if (requestCode == SELECT_COMPANYPICS) { // 选择图片
				if (!Tool.isEmpty(data)) {
					List<String> pics = data
							.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
					List<AttachmentListDTO> mrrckPics = data
							.getParcelableArrayListExtra("Mrrck_Album_Result");
					showProgressDialog("图片压缩中...");
					addpicCount = 0;
					if (pics != null) {// 多选图片返回
						for (int i = 0, size = pics.size(); i < size; i++) {
							new MyTask(SELECT_COMPANYPICS, size).execute(pics
									.get(i));
						}
					} else {// 拍照返回
						String photoPath = data
								.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
						new MyTask(SELECT_COMPANYPICS, 1).execute(photoPath);
					}
				}

			} else if (requestCode == TAKE_VIDEO) {
				bitMap_video = data.getStringExtra("bitMapPath");
				videoPath_video = data.getStringExtra("videoPath");
				videoSeconds = data.getStringExtra("videoSeconds");
				LogUtil.d("hl", "报名_作品路径=" + videoPath_video);
				LogUtil.d("hl", "报名_作品缩略图=" + bitMap_video);
				dealWithPath();
			} else if (requestCode == GET_ADDRESS) {
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
					addZZpicCount = 0;
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
				CompanyCustomContentEntity oneDetail = new CompanyCustomContentEntity();
				oneDetail.setTitle(data.getStringExtra("title"));
				oneDetail.setContent(data.getStringExtra("detail"));
				oneDetail.setPics(data.getStringArrayListExtra("pics"));
				// 组织请求使用的FileUrlJSONArray
				List<UploadImg> imgList = new ArrayList<UploadImg>();
				for (int i = 0; i < oneDetail.getPics().size() - 1; i++) {
					UploadImg ui = new UploadImg();
					ui.setFileType("0");
					ui.setFileUrl("");
					String picPath = oneDetail.getPics().get(i);
					if (!Tool.isEmpty(picPath)) {
						ui.setFileThumb(picPath.substring(
								picPath.lastIndexOf(".") + 1, picPath.length()));
					} else {
						ui.setFileThumb("");
					}
					imgList.add(ui);
				}
				oneDetail
						.setFileUrlJSONArray(JsonUtil.listToJsonArray(imgList));
				detailsInfoList.add(oneDetail);
				addOneCompanyDetailItem(detailsInfoList.size() - 1, oneDetail);

			} else if (requestCode == Add_DESIGNER) {
				CompanyDesignersEntity cde = new CompanyDesignersEntity();
				cde.setName(data.getStringExtra("name"));
				cde.setJobName(data.getStringExtra("position"));
				cde.setWorkYears(data.getStringExtra("workyears"));
				cde.setPersonalNote(data.getStringExtra("detail"));
				cde.setHeadUrl(data.getStringExtra("headPicPath"));
				List<UploadImg> imgList = new ArrayList<UploadImg>();
				UploadImg ui = new UploadImg();
				ui.setFileType("0");
				ui.setFileUrl("");
				String picPath = cde.getHeadUrl();
				if (!Tool.isEmpty(picPath)) {
					ui.setFileThumb(picPath.substring(
							picPath.lastIndexOf(".") + 1, picPath.length()));
				} else {
					ui.setFileThumb("");
				}
				imgList.add(ui);
				cde.setFileUrlJSONArray(JsonUtil.listToJsonArray(imgList));
				designerInfoList.add(cde);
				addOneDesignerItem(designerInfoList.size() - 1, cde);
			} else if (requestCode >= 10000) {
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				showProgressDialog("图片压缩中...");
				if (pics != null) {
					new MyTask(requestCode).execute(pics.get(0));
				} else {
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					new MyTask(requestCode).execute(photoPath);
				}
			}
		}
	}

	private void addOneDesignerItem(final int position,
			CompanyDesignersEntity cde) {
		View itemView = LayoutInflater.from(CompanyCertificationActivity.this)
				.inflate(R.layout.item_adddesigner, null, false);
		EditText editName = (EditText) itemView.findViewById(R.id.name);
		editName.setText(cde.getName());
		EditText editDetail = (EditText) itemView.findViewById(R.id.detail);
		ImageView headPic = (ImageView) itemView.findViewById(R.id.headPic);
		LinearLayout editLayout = (LinearLayout) itemView
				.findViewById(R.id.edit);
		editDetail.setText(cde.getPersonalNote());
		editName.setHint("点击输入姓名");
		editDetail.setHint("点击添加详细说明");
		editLayout.setVisibility(View.GONE);

		if (!Tool.isEmpty(cde.getHeadUrl())) {
			BitmapUtils bitmapUtils = new BitmapUtils(
					CompanyCertificationActivity.this);
			bitmapUtils.display(headPic, cde.getHeadUrl());
		}
		LinearLayout deleteIcon = (LinearLayout) itemView
				.findViewById(R.id.delete);
		deleteIcon.setVisibility(View.VISIBLE);
		// 删除按钮监听
		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CommonDialog deleteTipDialog = new CommonDialog(
						CompanyCertificationActivity.this, "提示", "确定删除本条详情?",
						"确定", "取消");
				deleteTipDialog
						.setClicklistener(new CommonDialog.ClickListenerInterface() {
							@Override
							public void doConfirm() {
								designerInfoList.remove(position);
								layout_teamGroup.removeAllViews();
								for (int i = 0; i < designerInfoList.size(); i++) {
									addOneDesignerItem(i,
											designerInfoList.get(i));
								}
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
		View itemView = LayoutInflater.from(CompanyCertificationActivity.this)
				.inflate(R.layout.include_addproductinfo, null, false);
		EditText editTitle = (EditText) itemView.findViewById(R.id.editTitle);
		editTitle.setText(detailInfo.getTitle());
		EditText editDetail = (EditText) itemView.findViewById(R.id.editDetail);
		Button editBtn = (Button) itemView.findViewById(R.id.edit);
		editDetail.setText(detailInfo.getContent());
		MyGridView gridview_uploadpic = (MyGridView) itemView
				.findViewById(R.id.gridview_uploadpic);
		editTitle.setHint("点击输入详细介绍");
		editDetail.setHint("点击添加详细说明文字");
		editBtn.setVisibility(View.GONE);
		setGridPic(gridview_uploadpic, detailInfo.getPics(), position);
		Button deleteIcon = (Button) itemView.findViewById(R.id.delete);
		deleteIcon.setVisibility(View.VISIBLE);
		// 删除按钮监听
		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final CommonDialog deleteTipDialog = new CommonDialog(
						CompanyCertificationActivity.this, "提示", "确定删除本条详情?",
						"确定", "取消");
				deleteTipDialog
						.setClicklistener(new CommonDialog.ClickListenerInterface() {
							@Override
							public void doConfirm() {
								detailsInfoList.remove(position);
								layout_moreGroup.removeAllViews();
								for (int i = 0; i < detailsInfoList.size(); i++) {
									addOneCompanyDetailItem(i,
											detailsInfoList.get(i));
								}
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
						CompanyCertificationActivity.this);
				bitmapUtils.display(iv_logo, logoPicPath);
				dismissProgressDialog();
				break;

			case TAKE_PHOTO_LICENSE:
				licensePicPath = result;
				BitmapUtils bitmapUtils2 = new BitmapUtils(
						CompanyCertificationActivity.this);
				bitmapUtils2.display(iv_zhizhao, licensePicPath);
				dismissProgressDialog();
				break;

			case SELECT_COMPANYPICS:
				addpicCount++;
				picPathList.add(picPathList.size() - 1, result);
				setSelectPic(true);
				if (addpicCount == size) {
					dismissProgressDialog();
				}
				break;
			case SELECT_ZZPIC:
				addZZpicCount++;
				zzpicPathList.add(zzpicPathList.size() - 1, result);
				setZZSelectPic(true);
				if (addZZpicCount == size) {
					dismissProgressDialog();
				}
				break;
			default:
				if (id >= 10000) {
					int index = Math.abs(id) % 10000;
					detailsInfoList
							.get(index)
							.getPics()
							.add(detailsInfoList.get(index).getPics().size() - 1,
									result);
					layout_moreGroup.removeAllViews();
					for (int i = 0; i < detailsInfoList.size(); i++) {
						addOneCompanyDetailItem(i, detailsInfoList.get(i));
					}
					dismissProgressDialog();
				}
				break;
			}
			if (pull_refreshSV != null) {
				pull_refreshSV.getRefreshableView().scrollTo(0, currentScroolY);
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
		LogUtil.d("hl", "报名_作品fileSeconds=" + fileSeconds);
		LogUtil.d("hl", "报名_作品fSize=" + fSize);
		videoInfo.setText("时长:" + timeInMinutes + "\n大小:" + fSize);

		if (f.length() / 1048576 > 10) {// 超出60s或者并且大于10M
			ToastUtil.showShortToast("视频文件太大,限制10M之内");
			videoPath_video = "";
			bitMap_video = "";
			videoSeconds = "";
		} else {
			lay_upload.setVisibility(View.GONE);
			iv_play.setVisibility(View.VISIBLE);
			BitmapUtils bitmapUtils = new BitmapUtils(
					CompanyCertificationActivity.this);
			BitmapDisplayConfig bitmapDisplayConfig = new BitmapDisplayConfig();
			bitmapDisplayConfig.setShowOriginal(true);
			bitmapUtils.display(frame_uploadvideo, bitMap_video,
					bitmapDisplayConfig);
			// videoInfoLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void bindListener() {
		findViewById(R.id.goback).setOnClickListener(this);
		findViewById(R.id.iv_addTeamPeople).setOnClickListener(this);
		findViewById(R.id.addOnemoreLayout).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hl", "认证__" + reqBase.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((reqBase.getBody().get("company") + "").length() > 2) {
				CompanyEntity companyEntity = (CompanyEntity) JsonUtil
						.jsonToObj(CompanyEntity.class,
								reqBase.getBody().get("company").toString());
				AppContext.getInstance().getUserInfo()
						.setCompanyEntity(companyEntity);
				AppContext.getInstance().setLocalUser(
						AppContext.getInstance().getUserInfo());
				if ((reqBase.getBody().get("resultMap") + "").length() > 2) {
					CompanyYZResult cyz = (CompanyYZResult) JsonUtil.jsonToObj(
							CompanyYZResult.class,
							reqBase.getBody().get("resultMap").toString());
					// 上传LOGO
					if (!Tool.isEmpty(cyz.getLogo())
							&& !Tool.isEmpty(logoPicPath)) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("fileUrlJSONArray", cyz.getLogo());
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("logopic.png");
						mainFfb.setFile(new File(logoPicPath));
						LogUtil.d("hl", "上传企业logo__" + logoPicPath);
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(3001, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
						Upload_logo_result = false;
					}
					// 上传执照
					if (!Tool.isEmpty(cyz.getLicense())
							&& !Tool.isEmpty(licensePicPath)) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("fileUrlJSONArray", cyz.getLicense());
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("licensepic.png");
						mainFfb.setFile(new File(licensePicPath));
						LogUtil.d("hl", "上传企业执照__" + licensePicPath);
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(3002, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
						Upload_license_result = false;
					}
					// 上传视频缩略图
					if (!Tool.isEmpty(cyz.getCompanyVideoPhoto())
							&& !Tool.isEmpty(bitMap_video)) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("fileUrlJSONArray", cyz.getCompanyVideoPhoto());
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("CompanyVideoPhoto.png");
						mainFfb.setFile(new File(bitMap_video));
						LogUtil.d("hl", "上传企业视频缩略图__" + bitMap_video);
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(3003, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
						Upload_videoPic_result = false;
					}
					// 上传视频
					if (!Tool.isEmpty(cyz.getCompanyVideo())
							&& !Tool.isEmpty(videoPath_video)) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("fileUrlJSONArray", cyz.getCompanyVideo());
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("video.mp4");
						mainFfb.setFile(new File(videoPath_video));
						LogUtil.d("hl", "上传企业视频__" + videoPath_video);
						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(3004, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
						Upload_video_result = false;
					}
					// 上传公司图片
					if (!Tool.isEmpty(cyz.getPhoto()) && picPathList.size() > 1) {
						for (int i = 0; i < cyz.getPhoto().size(); i++) {
							ReqBase req = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							List<String> oneInfo = new ArrayList<String>();
							oneInfo.add(cyz.getPhoto().get(i).toString());
							map.put("fileUrlJSONArray",
									JsonUtil.listToJsonArray(oneInfo));
							req.setHeader(new ReqHead(
									AppConfig.BUSINESS_FILE_IMG10002));
							req.setBody(JsonUtil.Map2JsonObj(map));
							Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
							List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
							FormFileBean mainFfb = new FormFileBean();
							mainFfb.setFileName("photo_" + i + ".png");
							mainFfb.setFile(new File(picPathList.get(i)
									.toString()));
							LogUtil.d("hl", "上传企业照片__" + i
									+ picPathList.get(i).toString());
							details_FileBeans.add(mainFfb);
							mapFileBean.put("file", details_FileBeans);
							uploadResFiles(4001 + i, AppConfig.PUBLICK_UPLOAD,
									mapFileBean, req, true);
						}
						Upload_compPic_result = false;
					}
					// 上传资质图片
					if (!Tool.isEmpty(cyz.getQualityHonorPhoto())
							&& zzpicPathList.size() > 1) {
						for (int i = 0; i < cyz.getQualityHonorPhoto().size(); i++) {
							ReqBase req = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							List<String> oneInfo = new ArrayList<String>();
							oneInfo.add(cyz.getQualityHonorPhoto().get(i)
									.toString());
							map.put("fileUrlJSONArray",
									JsonUtil.listToJsonArray(oneInfo));
							req.setHeader(new ReqHead(
									AppConfig.BUSINESS_FILE_IMG10002));
							req.setBody(JsonUtil.Map2JsonObj(map));
							Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
							List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
							FormFileBean mainFfb = new FormFileBean();
							mainFfb.setFileName("photo_" + i + ".png");
							mainFfb.setFile(new File(zzpicPathList.get(i)
									.toString()));
							LogUtil.d("hl", "上传企业资质照片__" + i
									+ zzpicPathList.get(i).toString());
							details_FileBeans.add(mainFfb);
							mapFileBean.put("file", details_FileBeans);
							uploadResFiles(5001 + i, AppConfig.PUBLICK_UPLOAD,
									mapFileBean, req, true);
						}
						Upload_compZZPic_result = false;
					}
					// 上传设计师图
					if (!Tool.isEmpty(cyz.getDesignersList())
							&& !Tool.isEmpty(designerInfoList)) {
						for (int i = 0; i < cyz.getDesignersList().size(); i++) {
							ReqBase req = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							List<String> oneInfo = new ArrayList<String>();
							oneInfo.add(cyz.getDesignersList().get(i)
									.toString());
							map.put("fileUrlJSONArray",
									JsonUtil.listToJsonArray(oneInfo));
							req.setHeader(new ReqHead(
									AppConfig.BUSINESS_FILE_IMG10002));
							req.setBody(JsonUtil.Map2JsonObj(map));
							Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
							List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
							FormFileBean mainFfb = new FormFileBean();
							mainFfb.setFileName("photo_" + i + ".png");
							mainFfb.setFile(new File(designerInfoList.get(i)
									.getHeadUrl()));
							LogUtil.d("hl", "上传企业设计师照片__" + i
									+ designerInfoList.get(i).toString());
							details_FileBeans.add(mainFfb);
							mapFileBean.put("file", details_FileBeans);
							uploadResFiles(6001 + i, AppConfig.PUBLICK_UPLOAD,
									mapFileBean, req, true);
						}
						Upload_designerPic_result = false;
					}
					// 上传自定义项图片
					if (!Tool.isEmpty(cyz.getCustomCententList())
							&& !Tool.isEmpty(detailsInfoList)) {
						allZDYPic = new ArrayList<String>();
						for (int i = 0; i < detailsInfoList.size(); i++) {
							for (int j = 0; j < detailsInfoList.get(i)
									.getPics().size() - 1; j++) {
								allZDYPic.add(detailsInfoList.get(i).getPics()
										.get(j));
							}
						}
						allCustomCententPicSize = cyz.getCustomCententList().size();
						for (int i = 0; i < allCustomCententPicSize; i++) {
							ReqBase req = new ReqBase();
							Map<String, Object> map = new HashMap<String, Object>();
							List<String> oneInfo = new ArrayList<String>();
							oneInfo.add(cyz.getCustomCententList().get(i)
									.toString());
							map.put("fileUrlJSONArray",
									JsonUtil.listToJsonArray(oneInfo));
							req.setHeader(new ReqHead(
									AppConfig.BUSINESS_FILE_IMG10002));
							req.setBody(JsonUtil.Map2JsonObj(map));
							Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
							List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
							FormFileBean mainFfb = new FormFileBean();
							mainFfb.setFileName("photo_" + i + ".png");
							mainFfb.setFile(new File(allZDYPic.get(i)));
							LogUtil.d("hl",
									"上传自定义内容照片__" + i + allZDYPic.get(i));
							details_FileBeans.add(mainFfb);
							mapFileBean.put("file", details_FileBeans);
							uploadResFiles(7001 + i, AppConfig.PUBLICK_UPLOAD,
									mapFileBean, req, true);
						}
						Upload_zdyPic_result = false;
					}
				} else {
					sendBroadcast(new Intent(
							BroadCastAction.ACTION_IM_REFRESH_COMPANY));
				}
			}
			break;
		case reqCodeTwo:
			break;
		case 3001:
			Upload_logo_result = true;
			checkIsUploadFinished();
			break;
		case 3002:
			Upload_license_result = true;
			checkIsUploadFinished();
			break;
		case 3003:
			Upload_videoPic_result = true;
			checkIsUploadFinished();
			break;
		case 3004:
			Upload_video_result = true;
			checkIsUploadFinished();
			break;
		default:
			if (requestCode > 4000 && requestCode < 5000) {// 上传公司图片返回
				Num_uploadedCompPic++;
				if (Num_uploadedCompPic == picPathList.size() - 1) {
					Upload_compPic_result = true;
					checkIsUploadFinished();
				}
			}
			if (requestCode > 5000 && requestCode < 6000) {
				Num_uploadedZZPic++;
				if (Num_uploadedZZPic == zzpicPathList.size() - 1) {
					Upload_compZZPic_result = true;
					checkIsUploadFinished();
				}
			}
			if (requestCode > 6000 && requestCode < 7000) {
				Num_designerPic++;
				if (Num_designerPic == designerInfoList.size()) {
					Upload_designerPic_result = true;
					checkIsUploadFinished();
				}
			}
			if (requestCode > 7000 && requestCode < 8000) {
				Num_zdyPic++;
				if (Num_zdyPic == allCustomCententPicSize) {
					Upload_zdyPic_result = true;
					checkIsUploadFinished();
				}
			}
			break;
		}

	}

	private void checkIsUploadFinished() {
		showProgressDialog("附件上传中...");
		if (Upload_logo_result && Upload_license_result
				&& Upload_compPic_result && Upload_compZZPic_result
				&& Upload_designerPic_result && Upload_zdyPic_result
				&& Upload_videoPic_result && Upload_video_result) {
			dismissProgressDialog();
			ToastUtil.showShortToast("认证成功!");
			sendBroadcast(new Intent(BroadCastAction.ACTION_IM_REFRESH_COMPANY));
			if ("1".equals(flag)) {
				startActivity(new Intent(CompanyCertificationActivity.this,
						RecruitMainActivity.class));
				finish();
				return;
			}
			if ("2".equals(flag)) {
				startActivity(new Intent(CompanyCertificationActivity.this,
						MyDecorationPublishActivity.class));
				finish();
				return;
			}
			if ("3".equals(flag)) {
				setResult(RESULT_OK);
				finish();
				return;
			}
			final CommonDialog commonDialog1 = new CommonDialog(
					CompanyCertificationActivity.this, "提示",
					"恭喜您认证通过，可以进行产品发布了。", "确定");

			commonDialog1.setClicklistener(new ClickListenerInterface() {

				@Override
				public void doConfirm() {
					commonDialog1.dismiss();
					setResult(RESULT_OK);
					Intent intent = new Intent(
							CompanyCertificationActivity.this,
							PublishProductActivity.class);
					startActivity(intent);
					finish();
				}

				@Override
				public void doCancel() {
					// TODO Auto-generated method stub

				}
			});
			commonDialog1.show();
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		// if (!Tool.isEmpty(videoPath_video)) {
		// ToastUtil.showShortToast("认证失败!建议上传的视频不要太大!");
		// } else {
		ToastUtil.showShortToast("认证失败!");
		// }
		switch (requestCode) {
		case 3001:
			ToastUtil.showShortToast("企业LOGO上传失败");
			break;
		case 3002:
			ToastUtil.showShortToast("企业营业执照上传失败");
			break;
		case 3003:
			ToastUtil.showShortToast("视频缩略图上传失败");
			break;
		case 3004:
			ToastUtil.showShortToast("视频上传失败");
			break;
		default:
			break;
		}
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
			new WheelSelectCityDialog(CompanyCertificationActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_city.setText(cityName);
							CompanyCertificationActivity.this.cityCode = cityCode
									+ "";
						}

					}).show();
			break;
		case R.id.layout_complanyPeople:
			new ChooseTextDialog(CompanyCertificationActivity.this, "选择类型",
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
			startActivityForResult(new Intent(
					CompanyCertificationActivity.this,
					SelectVedioActivity.class), TAKE_VIDEO);
			break;
		case R.id.btn_ok:
			if (checkIsReady()) {
				doUploadData();
			}
			break;
		case R.id.iv_location:
			// startActivityForResult(
			// new Intent(this, GetLoacationActivity.class), GET_ADDRESS);
		case R.id.videoReSet:
			lay_upload.setVisibility(View.VISIBLE);
			iv_play.setVisibility(View.GONE);
			frame_uploadvideo.setBackgroundResource(R.drawable.clickaddvideo);
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
			startActivityForResult(new Intent(
					CompanyCertificationActivity.this,
					AddOneDesignerInfoActivity.class), Add_DESIGNER);
			break;
		case R.id.addOnemoreLayout:
			startActivityForResult(new Intent(
					CompanyCertificationActivity.this,
					AddOneCompDetailActivity.class), Add_MORECOMPINFO);
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
		final CommonDialog commonDialog = new CommonDialog(
				CompanyCertificationActivity.this, "提示", "是否放弃本次编辑?", "确定",
				"取消");
		commonDialog.show();
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
	 * 上传企业数据
	 */
	private void doUploadData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("name", et_complanyName.getText().toString().trim());
		String sortString = PinyinUtil.getTerm(et_complanyName.getText()
				.toString().trim());
		map.put("nameFirstChar", sortString.matches("[A-Z]") ? sortString : "#");
		map.put("cityCode", cityCode);
		map.put("address", et_address.getText().toString().trim());
		map.put("scale", companyScaleCodeId);
		map.put("contactName", et_contact.getText().toString().trim());
		map.put("phone", et_phoneNum.getText().toString().trim());
		map.put("introduce", et_introduce.getText().toString().trim());
		map.put("website", et_website.getText().toString().trim());
		map.put("email", et_email.getText().toString().trim());
		map.put("bossType", bossType);
		map.put("bossTypeName", bossTypeName);
		map.put("qualityHonor", et_zzry.getText().toString());
		map.put("longitude", MrrckApplication.getInstance().getLongitudeStr());//
		map.put("latitude", MrrckApplication.getInstance().getLaitudeStr()); //
		if (!Tool.isEmpty(videoPath_video)) {
			map.put("videoSeconds", fileSeconds);
		}
		setMapImageKeyValue(map, "logo", logoPicPath, "0");
		setMapImageKeyValue(map, "license", licensePicPath, "0");
		setMapImageKeyValue(map, "companyVideo", videoPath_video, "1");
		setMapImageKeyValue(map, "companyVideoPhoto", bitMap_video, "0");
		setMapImageListKeyValue(map, "qualityHonorPhoto", zzpicPathList);
		setMapImageListKeyValue(map, "photo", picPathList);

		map.put("designersList", JsonUtil.listToJsonArray(designerInfoList));
		map.put("customCententList", JsonUtil.listToJsonArray(detailsInfoList));
		LogUtil.d("hl", "公司认证请求__" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_COMPLANYINFO_RT_90065));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase);
		// Map<String, List<FormFileBean>> mapFileBean = new HashMap<String,
		// List<FormFileBean>>();
		//
		// if (!Tool.isEmpty(logoPicPath)) {
		// // logo图片
		// List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
		// logo_FileBeans.add(new FormFileBean(new File(logoPicPath),
		// "logo.png"));
		// mapFileBean.put("logo", logo_FileBeans);
		// }
		//
		// if (!Tool.isEmpty(licensePicPath)) {
		// // 执照图片
		// List<FormFileBean> license_FileBeans = new ArrayList<FormFileBean>();
		// license_FileBeans.add(new FormFileBean(new File(licensePicPath),
		// "license.png"));
		// mapFileBean.put("license", license_FileBeans);
		// }
		//
		// if (!Tool.isEmpty(videoPath_video)) {
		// // 视频缩略图
		// List<FormFileBean> videoPhoto_FileBeans = new
		// ArrayList<FormFileBean>();
		// videoPhoto_FileBeans.add(new FormFileBean(new File(bitMap_video),
		// "videoPhoto.png"));
		// mapFileBean.put("videoPhoto", videoPhoto_FileBeans);
		//
		// // 视频
		// List<FormFileBean> video_FileBeans = new ArrayList<FormFileBean>();
		// video_FileBeans.add(new FormFileBean(new File(videoPath_video),
		// "video.mp4"));
		// mapFileBean.put("video", video_FileBeans);
		// }
		//
		// if (!Tool.isEmpty(picPathList) && picPathList.size() > 1) {
		// // 公司图片
		// List<FormFileBean> company_FileBeans = new ArrayList<FormFileBean>();
		// for (int i = 0; i < picPathList.size() - 1; i++) {
		// Object imgPathString = picPathList.get(i);
		// if (imgPathString.getClass().getName()
		// .equals(String.class.getName())) {
		// FormFileBean ffb = new FormFileBean();
		// ffb.setFileName("company_image_" + i + ".png");
		// ffb.setFile(new File((String) imgPathString));
		// company_FileBeans.add(ffb);
		// }
		// }
		// mapFileBean.put("photo", company_FileBeans);
		// }
		// uploadFiles(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING,
		// mapFileBean,
		// reqBase, true);

	}

	/**
	 * 图片List上传添加key
	 * 
	 * @param map
	 * @param key
	 * @param picPath
	 */
	private void setMapImageListKeyValue(Map<String, Object> map, String key,
			List<Object> picPathList) {
		List<UploadImg> imgList = new ArrayList<UploadImg>();
		for (int j = 0, picNum = picPathList.size() - 1; j < picNum; j++) {
			UploadImg detailU = new UploadImg();
			String path = (String) picPathList.get(j);
			detailU.setFileThumb(path.substring(path.lastIndexOf(".") + 1,
					path.length()));
			detailU.setFileType("0");
			detailU.setFileUrl("");
			imgList.add(detailU);
		}
		map.put(key, JsonUtil.listToJsonArray(imgList));
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

	private void setZZSelectPic(boolean isAdd) {
		gridview_uploadzzpic.setAdapter(new CommonAdapter<Object>(this,
				R.layout.add_talent_pic_gridview_item, zzpicPathList) {
			@Override
			public void convert(ViewHolder viewHolder, final Object imgItem) {
				// 如果大于9个删除第一个图片也就是增加图片按钮
				if (zzpicPathList.size() < 9) {
					if (imgItem == getItem(zzpicPathList.size() - 1)) {
						viewHolder.setImage(R.id.img_item,
								R.drawable.clickaddpic);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												CompanyCertificationActivity.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 4 - zzpicPathList.size();
										intent.putExtra("MAX_NUM",
												(picNum > 0) ? picNum : 0);// 选择的张数
										startActivityForResult(intent,
												SELECT_ZZPIC);
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
										zzpicPathList.remove(imgItem);
										setZZSelectPic(false);
									}
								});

					}
				} else {
					if (imgItem == getItem(zzpicPathList.size() - 1)) {
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
										zzpicPathList.remove(imgItem);
										setZZSelectPic(false);
									}
								});

					}
				}
			}

		});
	}

	private void setGridPic(final MyGridView gridview,
			final ArrayList<String> pisList, final int position) {
		gridview.setAdapter(new CommonAdapter<String>(this,
				R.layout.item_pic_product, pisList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final String imgItem) {
				// 如果大于8个删除第一个图片也就是增加图片按钮
				if (pisList.size() < 9) {
					if (imgItem == getItem(pisList.size() - 1)) {
						viewHolder.setImage(R.id.img_item, R.drawable.addphoto);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												CompanyCertificationActivity.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 10 - pisList.size();
										intent.putExtra("MAX_NUM",
												(picNum > 0) ? picNum : 0);// 选择的张数
										startActivityForResult(intent,
												10000 + position);// 标记是哪一行Item的详情添加图片
									}
								});

					} else {
						viewHolder.setImage(R.id.img_item, imgItem, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										pisList.remove(imgItem);
										setGridPic(gridview, pisList, position);
									}
								});

					}
				} else {
					if (imgItem == getItem(pisList.size() - 1)) {
						viewHolder.getView(R.id.add_pic_framelayout)
								.setVisibility(View.GONE);
					} else {
						viewHolder.setImage(R.id.img_item, imgItem, 0);
						viewHolder.getView(R.id.delete_btn).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										pisList.remove(imgItem);
										setGridPic(gridview, pisList, position);
									}
								});

					}
				}
			}

		});
	}

}
