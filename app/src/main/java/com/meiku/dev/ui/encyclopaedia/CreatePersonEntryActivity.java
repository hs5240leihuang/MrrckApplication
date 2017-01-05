package com.meiku.dev.ui.encyclopaedia;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.BaikeMkEntity;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkMrrckBaikeAttachment;
import com.meiku.dev.bean.MkMrrckBaikeBasic;
import com.meiku.dev.bean.MkMrrckBaikeContent;
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
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.Util;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;
import com.meiku.dev.views.WheelSelectCityDialog;
import com.meiku.dev.views.WheelSelectCityDialog.SelectCityListener;
import com.umeng.socialize.utils.Log;

public class CreatePersonEntryActivity extends BaseActivity implements
		android.view.View.OnClickListener {
	private EditText et_citiao, et_gaishu, et_jianjie, et_chinesename,
			et_foreignname, et_othername, et_country, et_nation, et_profession,
			et_faith, et_school, et_works, et_aihao, et_shuomoing,
			et_achievement;
	private LinearLayout lin_chushengtime, lin_chushengcity;
	private TextView tv_chushengtime, tv_chushengcity;
	private Button btn_next, btn_addmore, btn_commit;
	private ScrollView sc_view, sc_lview;
	private List<MkMrrckBaikeAttachment> photolist = new ArrayList<MkMrrckBaikeAttachment>();
	private CommonAdapter<MkMrrckBaikeAttachment> roomAdapter;
	private MyGridView gv_show;
	private final int reqadd = 1;
	private boolean flag = true;// 页面判断flag
	private ImageView img_neiceng, img_waiceng;
	private final int reqgaiyao = 2;
	private String imgresault;// 概述图片
	private final int reqjust = 3;
	private final int reqbase = 4;
	private String title;// 添加的话题
	private String addmore;// 更多介绍
	/** 内容自定义项 */
	private List<MkMrrckBaikeContent> listBkContent = new ArrayList<MkMrrckBaikeContent>();
	private LinearLayout layout_addMoreBaseInfo;// 添加自定义项
	/** 自定义添加详父布局 */
	private LinearLayout layout_addmoreInfo;
	private LinearLayout layout_baseInfoMore;
	/** 基本信息自定义项 */
	private List<MkMrrckBaikeBasic> listBkBasic = new ArrayList<MkMrrckBaikeBasic>();
	/** 操作类型 0基本信息，1自定义项，2个人荣誉 */
	private final int TYPE_EDIT_BKBASIC = 0;
	private final int TYPE_EDIT_BKCONTENT = 1;
	private final int TYPE_EDIT_ACHIEVE = 2;
	private final int reqDeleteBaseInfo = 5;
	private final int reqDeleteDetailInfo = 6;
	private final int reqEditOneBase = 7;
	private final int reqEditOneDetail = 8;
	private LinearLayout layout_addAchievement;
	/** 基本信息自定义项 */
	private List<String> listBkAchievement = new ArrayList<String>();
	private int pageType;
	private int edit_baikeId;
	/** 页面模块刷新类型 0刷新所有，1刷新自定义基本信息，2刷新自定义详细信息 ，3刷新相册图片 */
	private int updateType;
	private final int reqCodeFive = 500;
	private final int reqCodeEditGaishuPic = 600;
	private int sortNoMax;
	private TextView center_txt_title;
	private String newphotoPath;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_createperson_citiao;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		layout_addmoreInfo = (LinearLayout) findViewById(R.id.layout_addmoreInfo);
		layout_addmoreInfo.removeAllViews();
		layout_addMoreBaseInfo = (LinearLayout) findViewById(R.id.layout_addMoreBaseInfo);
		layout_baseInfoMore = (LinearLayout) findViewById(R.id.layout_baseInfoMore);
		layout_baseInfoMore.removeAllViews();
		et_shuomoing = (EditText) findViewById(R.id.et_shuomoing);
		et_aihao = (EditText) findViewById(R.id.et_aihao);
		et_works = (EditText) findViewById(R.id.et_works);
		et_school = (EditText) findViewById(R.id.et_school);
		et_faith = (EditText) findViewById(R.id.et_faith);
		et_profession = (EditText) findViewById(R.id.et_profession);
		et_nation = (EditText) findViewById(R.id.et_nation);
		et_country = (EditText) findViewById(R.id.et_country);
		et_othername = (EditText) findViewById(R.id.et_othername);
		et_foreignname = (EditText) findViewById(R.id.et_foreignname);
		et_chinesename = (EditText) findViewById(R.id.et_chinesename);
		img_neiceng = (ImageView) findViewById(R.id.img_neiceng);
		img_waiceng = (ImageView) findViewById(R.id.img_waiceng);
		gv_show = (MyGridView) findViewById(R.id.gv_show);
		sc_lview = (ScrollView) findViewById(R.id.sc_lview);
		sc_view = (ScrollView) findViewById(R.id.sc_view);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_addmore = (Button) findViewById(R.id.btn_addmore);
		tv_chushengcity = (TextView) findViewById(R.id.tv_chushengcity);
		lin_chushengcity = (LinearLayout) findViewById(R.id.lin_chushengcity);
		tv_chushengtime = (TextView) findViewById(R.id.tv_chushengtime);
		lin_chushengtime = (LinearLayout) findViewById(R.id.lin_chushengtime);
		layout_addAchievement = (LinearLayout) findViewById(R.id.layout_addAchievement);
		layout_addAchievement.removeAllViews();
		et_jianjie = (EditText) findViewById(R.id.et_jianjie);
		et_citiao = (EditText) findViewById(R.id.et_citiao);
		et_gaishu = (EditText) findViewById(R.id.et_gaishu);
		et_achievement = (EditText) findViewById(R.id.et_achievement);
		Util.setHintTextSize(et_citiao, "请输入你要创建的个人词条", 12);
		Util.setHintTextSize(et_gaishu, "请简单描述词条的主要内容和关键信息，点击后输入", 12);
		Util.setHintTextSize(et_jianjie, "点击添加,个人特色为主,限140字。", 12);
	}

	@Override
	public void initValue() {
		initphotoGrid();
		pageType = getIntent().getIntExtra("pageType", 0);
		edit_baikeId = getIntent().getIntExtra("edit_baikeId", 0);
		if (pageType == 1) {
			center_txt_title.setText("编辑达人词条");
			getDetailData(0);
		}
	}

	@Override
	public void bindListener() {
		btn_commit.setOnClickListener(this);
		img_neiceng.setOnClickListener(this);
		img_waiceng.setOnClickListener(this);
		lin_chushengtime.setOnClickListener(this);
		lin_chushengcity.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_addmore.setOnClickListener(this);
		layout_addMoreBaseInfo.setOnClickListener(this);
		findViewById(R.id.iv_addAchievement).setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	/**
	 * 获取词条详情数据 updateType 页面模块刷新类型 0刷新所有，1刷新自定义基本信息，2刷新自定义详细信息 ，3刷新相册图片
	 */
	private void getDetailData(int updateType) {
		this.updateType = updateType;
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("baikeId", edit_baikeId);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BAIKEINFO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_BAIKE, req, true);
	}

	/**
	 * 编辑时获取详细数据，配置到页面
	 * 
	 * @param bk
	 */
	private void setPageData(BaikeMkEntity bk) {
		et_citiao.setText(bk.getName());// 词条名
		et_gaishu.setText(bk.getSummary());// 概述
		et_chinesename.setText(bk.getChnName());// 中文名
		et_foreignname.setText(bk.getEnName());// 外文名
		et_othername.setText(bk.getAliasName());// 别名
		et_country.setText(bk.getCountry());// 国籍
		et_nation.setText(bk.getNational());// 民族
		tv_chushengcity.setText(bk.getBirthPlace());// 出身地
		tv_chushengtime.setText(bk.getBirthDate());// 出身日期
		et_profession.setText(bk.getPositionName());// 职业
		et_school.setText(bk.getGraduateSchool());// 毕业院校
		et_faith.setText(bk.getBelief());// 信仰
		et_works.setText(bk.getMajorWorks());// 代表作品
		String majorAchievement = bk.getAchievement();
		if (Tool.isEmpty(majorAchievement) || !majorAchievement.contains("%%")) {
			et_achievement.setText(majorAchievement);
		} else {
			String[] worksStrs = majorAchievement.split("%%");
			if (worksStrs.length > 1) {
				et_achievement.setText(worksStrs[0]);
				listBkAchievement.clear();
				listBkAchievement.addAll(Arrays.asList(worksStrs));
				listBkAchievement.remove(0);// 第一个数据不是自定义
				layout_addAchievement.removeAllViews();
				for (int i = 0, size = listBkAchievement.size(); i < size; i++) {
					AddOneAchievementInfo(i, worksStrs[i]);
				}
			}
		}

		// 第二页
		et_jianjie.setText(bk.getRemark());// 个人简介
		et_aihao.setText(bk.getHobby());// 兴趣爱好
		et_shuomoing.setText(bk.getDetailRemark());// 兴趣爱好

		if (!Tool.isEmpty(bk.getMainPhotoThumb())) {
			BitmapUtils bitmapUtils = new BitmapUtils(
					CreatePersonEntryActivity.this);
			bitmapUtils.display(img_neiceng, bk.getMainPhotoThumb());
			img_waiceng.setImageDrawable(ContextCompat.getDrawable(
					CreatePersonEntryActivity.this, R.drawable.yinshi_jianhao));
		}

		if (!Tool.isEmpty(bk.getListBkBasic())) {
			listBkBasic.clear();
			listBkBasic.addAll(bk.getListBkBasic());
			layout_baseInfoMore.removeAllViews();
			for (int i = 0, size = listBkBasic.size(); i < size; i++) {
				AddOneBaseInfo(i, listBkBasic.get(i));
			}
		}

		if (!Tool.isEmpty(bk.getListBkContent())) {
			listBkContent.clear();
			listBkContent.addAll(bk.getListBkContent());
			layout_addmoreInfo.removeAllViews();
			for (int i = 0, size = listBkContent.size(); i < size; i++) {
				AddOneDetailInfo(i, listBkContent.get(i));
			}
		}

		if (!Tool.isEmpty(bk.getListBkAttachment())) {
			photolist.clear();
			MkMrrckBaikeAttachment att = new MkMrrckBaikeAttachment();
			att.setClientFileUrl((R.drawable.addphoto + ""));
			photolist.add(att);
			photolist.addAll(bk.getListBkAttachment());
			roomAdapter.notifyDataSetChanged();
			for (int i = 0, size = bk.getListBkAttachment().size(); i < size; i++) {
				if (sortNoMax < bk.getListBkAttachment().get(i).getSortNo()) {
					sortNoMax = bk.getListBkAttachment().get(i).getSortNo();
				}
			}
		}
	}

	/**
	 * 删除相册图片请求
	 */
	private void deleteOneXCPic(int photoId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("baikeId", edit_baikeId);
		map.put("photoId", photoId);
		map.put("categoryId", 1);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BK_XC));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeThree, AppConfig.PUBLICK_BAIKE, req, true);
	}

	/**
	 * 新增相册图片请求
	 */
	private void AddOneXCPic(String picPath) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("baikeId", edit_baikeId);
		map.put("sortNo", sortNoMax);
		map.put("categoryId", 1);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_BK_ADDXCPIC));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (!Tool.isEmpty(picPath)) {
			// 发帖图片
			List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
			logo_FileBeans.add(new FormFileBean(new File(picPath), System
					.currentTimeMillis() + "_bk.png"));
			mapFileBean.put("photo", logo_FileBeans);
		}
		uploadFiles(reqCodeFour, AppConfig.PUBLICK_BAIKE, mapFileBean, reqBase,
				true);
	}

	/**
	 * 概述图请求
	 */
	private void AddOrEditGaishuPic(String picPath) {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("baikeId", edit_baikeId);
		map.put("categoryId", 1);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_BK_GAISHUPIC));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (!Tool.isEmpty(picPath)) {
			// 发帖图片
			List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
			logo_FileBeans.add(new FormFileBean(new File(picPath), System
					.currentTimeMillis() + "_bk.png"));
			mapFileBean.put("mainPhoto", logo_FileBeans);
		}
		uploadFiles(reqCodeEditGaishuPic, AppConfig.PUBLICK_BAIKE, mapFileBean,
				reqBase, true);
	}

	/**
	 * 删除一条基本信息
	 */
	private void deleteOneBaseInfo(int bkBasicId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("baikeId", edit_baikeId);
		map.put("bkBasicId", bkBasicId);
		map.put("categoryId", 1);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BK_DELETEONEBASE));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqDeleteBaseInfo, AppConfig.PUBLICK_BAIKE, req, true);
	}

	/**
	 * 删除一条详细信息
	 */
	private void deleteOneDetailInfo(int bkContentId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("baikeId", edit_baikeId);
		map.put("bkContentId", bkContentId);
		map.put("categoryId", 1);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BK_DELETEONEDETAIL));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqDeleteDetailInfo, AppConfig.PUBLICK_BAIKE, req, true);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("wangke", "result__" + reqBase.getBody());
		switch (requestCode) {
		case reqCodeOne:// 添加返回
			final CommonDialog commonDialog = new CommonDialog(
					CreatePersonEntryActivity.this, "提示",
					"感谢您为别人提供美容领域的专业知识，您的词条将在48小时内完成审核并发布", "确定");
			commonDialog.show();
			commonDialog.setClicklistener(new ClickListenerInterface() {

				@Override
				public void doConfirm() {
					setResult(RESULT_OK);
					finish();
				}

				@Override
				public void doCancel() {

				}
			});

			break;
		case reqCodeTwo:
			if (reqBase.getBody().get("baiKeDetail").toString().length() > 2) {
				BaikeMkEntity baikeMkEntity = (BaikeMkEntity) JsonUtil
						.jsonToObj(BaikeMkEntity.class,
								reqBase.getBody().get("baiKeDetail").toString());
				if (null != baikeMkEntity) {
					// 0刷新所有，1刷新自定义基本信息，2刷新自定义详细信息 ，3刷新相册图片
					switch (updateType) {
					case 0:
						setPageData(baikeMkEntity);
						break;
					case 1:
						if (!Tool.isEmpty(baikeMkEntity.getListBkBasic())) {
							listBkBasic.clear();
							listBkBasic.addAll(baikeMkEntity.getListBkBasic());
							layout_baseInfoMore.removeAllViews();
							for (int i = 0, size = listBkBasic.size(); i < size; i++) {
								AddOneBaseInfo(i, listBkBasic.get(i));
							}
						}
						break;
					case 2:
						if (!Tool.isEmpty(baikeMkEntity)) {
							listBkContent.clear();
							listBkContent.addAll(baikeMkEntity
									.getListBkContent());
							layout_addmoreInfo.removeAllViews();
							for (int i = 0, size = listBkContent.size(); i < size; i++) {
								AddOneDetailInfo(i, listBkContent.get(i));
							}
						}
						break;
					case 3:
						Log.d("222000", baikeMkEntity.getListBkAttachment()
								.size() + "");
						// if (baikeMkEntity.getListBkAttachment().size()>-1) {
						photolist.clear();
						MkMrrckBaikeAttachment att = new MkMrrckBaikeAttachment();
						att.setClientFileUrl((R.drawable.addphoto + ""));
						photolist.add(att);
						photolist.addAll(baikeMkEntity.getListBkAttachment());
						roomAdapter.notifyDataSetChanged();
						for (int i = 0, size = baikeMkEntity
								.getListBkAttachment().size(); i < size; i++) {
							if (sortNoMax < baikeMkEntity.getListBkAttachment()
									.get(i).getSortNo()) {
								sortNoMax = baikeMkEntity.getListBkAttachment()
										.get(i).getSortNo();
							}
						}
						// }
						break;
					default:
						break;
					}
				}
			}
			break;
		case reqCodeThree:// 删除相册图片
			getDetailData(3);
			break;
		case reqCodeFour:// 新增相册图片
			getDetailData(3);
			break;
		case reqDeleteBaseInfo:// 删除自定义基本信息
			getDetailData(1);
			break;
		case reqDeleteDetailInfo:// 删除自定义详细信息
			getDetailData(2);
			break;
		case reqCodeFive:
			ToastUtil.showShortToast("修改达人词条成功");
			setResult(RESULT_OK);
			finish();
			break;
		case reqCodeEditGaishuPic:
			ToastUtil.showShortToast("概述图添加成功");
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("创建个人词条失败");
			break;
		case reqCodeFive:
			ToastUtil.showShortToast("编辑个人词条失败");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_chushengtime:
			new TimeSelectDialog(CreatePersonEntryActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							tv_chushengtime.setText(time);
						}
					}).show();
			break;
		case R.id.lin_chushengcity:
			new WheelSelectCityDialog(CreatePersonEntryActivity.this, false,
					new SelectCityListener() {

						@Override
						public void ChooseOneCity(int provinceCode,
								String provinceName, int cityCode,
								String cityName) {
							tv_chushengcity.setText(cityName);
						}

					}).show();
			break;
		case R.id.btn_commit:
			if (pageType == 0) {
				commit();
			} else {
				gotoEdit();
			}

			break;
		case R.id.btn_next:
			if (Tool.isEmpty(et_citiao.getText().toString())) {
				ToastUtil.showShortToast("词条名不能为空");
				return;
			}
			if (Tool.isEmpty(et_gaishu.getText().toString())) {
				ToastUtil.showShortToast("概述不能为空");
				return;
			}
			if (Tool.isEmpty(img_waiceng.getDrawable())) {
				ToastUtil.showShortToast("概述图不能为空");
				return;
			}
			sc_lview.setVisibility(View.GONE);
			sc_view.setVisibility(View.VISIBLE);
			flag = false;
			break;
		case R.id.goback:
			if (flag) {
				finish();
			} else {
				sc_lview.setVisibility(View.VISIBLE);
				sc_view.setVisibility(View.GONE);
				flag = true;
			}
			break;
		case R.id.img_waiceng:
			if (img_waiceng.getDrawable() == null) {

				Intent intent1 = new Intent(CreatePersonEntryActivity.this,
						SelectPictureActivity.class);
				intent1.putExtra("SELECT_MODE",
						SelectPictureActivity.MODE_SINGLE);// 选择模式
				intent1.putExtra("MAX_NUM", 1);// 选择的张数
				startActivityForResult(intent1, reqgaiyao);
			} else {
				final CommonDialog commonDialog = new CommonDialog(
						CreatePersonEntryActivity.this, "提示", "是否删除该照片", "确认",
						"取消");
				commonDialog.show();
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						img_waiceng.setImageDrawable(null);
						img_neiceng.setImageDrawable(ContextCompat.getDrawable(
								CreatePersonEntryActivity.this,
								R.drawable.entry_gaishu));
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
			}
			break;
		case R.id.btn_addmore:
			Intent addiIntent = new Intent(CreatePersonEntryActivity.this,
					AddEntryActivity.class);
			if (pageType == 1) {// 编辑时添加详细信息
				addiIntent.putExtra("moreflag", 7);
				addiIntent.putExtra("baikeId", edit_baikeId);
				addiIntent.putExtra("categoryId", 1);
				startActivityForResult(addiIntent, reqEditOneDetail);
			} else {// 创建时添加详细信息
				addiIntent.putExtra("moreflag", 1);
				startActivityForResult(addiIntent, reqjust);
			}
			break;
		case R.id.layout_addMoreBaseInfo:
			Intent addBaseIntent = new Intent(CreatePersonEntryActivity.this,
					AddEntryActivity.class);
			if (pageType == 1) {// 编辑时添加基本信息
				addBaseIntent.putExtra("moreflag", 8);
				addBaseIntent.putExtra("baikeId", edit_baikeId);
				addBaseIntent.putExtra("categoryId", 1);
				startActivityForResult(addBaseIntent, reqEditOneBase);
			} else {// 创建时添加基本信息
				addBaseIntent.putExtra("moreflag", 4);
				startActivityForResult(addBaseIntent, reqbase);
			}
			break;
		case R.id.iv_addAchievement:
			listBkAchievement.add("");
			AddOneAchievementInfo(listBkAchievement.size() - 1, "");
			break;
		default:
			break;
		}
	}

	/**
	 * 添加一个个人荣誉
	 * 
	 * @param string
	 * 
	 * @param title
	 * @param detail
	 */
	private void AddOneAchievementInfo(int position, String content) {
		View view = LayoutInflater.from(CreatePersonEntryActivity.this)
				.inflate(R.layout.item_baike_addachievement, null, false);
		EditText et_achievement = (EditText) view
				.findViewById(R.id.et_achievement);
		et_achievement.setText(content);
		layout_addAchievement.addView(view, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		et_achievement.addTextChangedListener(new AddInfoETTextWatcherListener(
				position, TYPE_EDIT_ACHIEVE));
		view.findViewById(R.id.delete).setOnClickListener(
				new DeleteClick(position, TYPE_EDIT_ACHIEVE));
	}

	// 相册/参赛作品
	private void initphotoGrid() {
		photolist.clear();
		MkMrrckBaikeAttachment groupManageImageBeanAdd = new MkMrrckBaikeAttachment();
		groupManageImageBeanAdd.setClientFileUrl(R.drawable.addphoto + "");
		photolist.add(groupManageImageBeanAdd);
		roomAdapter = new CommonAdapter<MkMrrckBaikeAttachment>(this,
				R.layout.item_newphotogrid, photolist) {

			@Override
			public void convert(final ViewHolder viewHolder,
					final MkMrrckBaikeAttachment t) {
				if (viewHolder.getPosition() == 0) {
					viewHolder.setImage(R.id.image_out,
							Integer.parseInt(t.getClientFileUrl()));
					viewHolder.getConvertView().setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (photolist.size() >= 9) {
										ToastUtil.showShortToast("最多只能上传8张照片！");
										return;
									}
									Intent intent = new Intent(
											CreatePersonEntryActivity.this,
											SelectPictureActivity.class);
									intent.putExtra("SELECT_MODE",
											SelectPictureActivity.MODE_SINGLE);// 选择模式
									intent.putExtra("MAX_NUM", 1);// 选择的张数
									startActivityForResult(intent, reqadd);

								}
							});
				} else {
					viewHolder.setImage(R.id.image_out,
							R.drawable.yinshi_jianhao);
					viewHolder.setImage(R.id.image, t.getClientFileUrl(), 1);
					viewHolder.getConvertView().setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									final CommonDialog commonDialog = new CommonDialog(
											CreatePersonEntryActivity.this,
											"提示", "是否删除该照片", "确认", "取消");
									commonDialog.show();
									commonDialog
											.setClicklistener(new ClickListenerInterface() {

												@Override
												public void doConfirm() {
													if (pageType == 1
															&& !Tool.isEmpty(t
																	.getId())) {
														deleteOneXCPic(t
																.getId());

													} else {
														photolist
																.remove(viewHolder
																		.getPosition());
														roomAdapter
																.notifyDataSetChanged();
													}
													commonDialog.dismiss();
												}

												@Override
												public void doCancel() {
													commonDialog.dismiss();
												}
											});
								}
							});
				}

			}

		};
		gv_show.setAdapter(roomAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqadd) {
				List<String> pictrue = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPictrue = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(reqadd, pictrue.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqadd, photoPath);
				}
			} else if (requestCode == reqgaiyao) {
				List<String> pictrue = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(reqgaiyao, pictrue.get(i));
					}

				} else {// 拍照返回
					newphotoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);

					if (!Tool.isEmpty(newphotoPath)) {
						if (ConstantKey.USE_PIC_CUT) {
							Uri uri = Uri.fromFile(new File(newphotoPath));
							cropImageUri(uri, 200, 200, reqCodeFour);
						} else {
							CompressPic(reqgaiyao, newphotoPath);
						}
					} else {
						ToastUtil.showShortToast("获取图片失败");
					}
				}
			} else if (requestCode == reqjust) {
				MkMrrckBaikeContent mkAddInfo = new MkMrrckBaikeContent();
				mkAddInfo.setTitle(data.getStringExtra("title"));
				mkAddInfo.setContent(data.getStringExtra("detail"));
				mkAddInfo.setUserId(AppContext.getInstance().getUserInfo()
						.getId());
				listBkContent.add(mkAddInfo);
				AddOneDetailInfo(listBkContent.size() - 1, mkAddInfo);
			} else if (requestCode == reqbase) {
				MkMrrckBaikeBasic mkBaseInfo = new MkMrrckBaikeBasic();
				mkBaseInfo.setTitle(data.getStringExtra("title"));
				mkBaseInfo.setContent(data.getStringExtra("detail"));
				mkBaseInfo.setUserId(AppContext.getInstance().getUserInfo()
						.getId());
				listBkBasic.add(mkBaseInfo);
				AddOneBaseInfo(listBkBasic.size() - 1, mkBaseInfo);
			} else if (requestCode == reqEditOneBase) {
				getDetailData(1);
			} else if (requestCode == reqEditOneDetail) {
				getDetailData(2);
			} else if (requestCode == reqCodeFour) {
				CompressPic(reqgaiyao, newphotoPath);
			}
		}
	}

	/**
	 * 添加一个基本信息
	 * 
	 * @param mkBaseInfo
	 * @param mkBaseInfo
	 */
	private void AddOneBaseInfo(int position, final MkMrrckBaikeBasic mkBaseInfo) {
		View view = LayoutInflater.from(CreatePersonEntryActivity.this)
				.inflate(R.layout.item_baike_addbasic, null, false);
		TextView title_add = (TextView) view.findViewById(R.id.tv_baseTitle);
		title_add.setText(mkBaseInfo.getTitle());

		EditText et_add = (EditText) view.findViewById(R.id.et_baseContent);
		et_add.setText(mkBaseInfo.getContent());
		layout_baseInfoMore.addView(view, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		et_add.addTextChangedListener(new AddInfoETTextWatcherListener(
				position, TYPE_EDIT_BKBASIC));
		view.findViewById(R.id.delete).setOnClickListener(
				new DeleteClick(position, TYPE_EDIT_BKBASIC));
		if (pageType == 1) {
			et_add.setEnabled(false);
			ImageView edit = (ImageView) view.findViewById(R.id.edit);
			edit.setVisibility(View.VISIBLE);
			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent addiIntent = new Intent(
							CreatePersonEntryActivity.this,
							AddEntryActivity.class);
					addiIntent.putExtra("moreflag", 6);
					addiIntent.putExtra("baikeId", edit_baikeId);
					Bundle bundle = new Bundle();
					bundle.putSerializable("baseInfo", mkBaseInfo);
					addiIntent.putExtras(bundle);
					startActivityForResult(addiIntent, reqEditOneBase);
				}
			});
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
			case reqadd:
				dismissProgressDialog();
				if (pageType == 1) {
					AddOneXCPic(result);
				} else {
					MkMrrckBaikeAttachment groupManageImageBean1 = new MkMrrckBaikeAttachment();
					groupManageImageBean1.setClientFileUrl(result);
					photolist.add(groupManageImageBean1);
					roomAdapter.notifyDataSetChanged();
				}
				break;
			case reqgaiyao:
				dismissProgressDialog();
				BitmapUtils bitmapUtils = new BitmapUtils(
						CreatePersonEntryActivity.this);
				bitmapUtils.display(img_neiceng, result);
				img_waiceng.setImageDrawable(ContextCompat.getDrawable(
						CreatePersonEntryActivity.this,
						R.drawable.yinshi_jianhao));
				if (pageType == 1) {
					AddOrEditGaishuPic(result);
				} else {
					imgresault = result;
				}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (flag) {
				finish();
			} else {
				sc_lview.setVisibility(View.VISIBLE);
				sc_view.setVisibility(View.GONE);
				flag = true;
			}
		}
		return false;
		// return super.onKeyDown(keyCode, event);
	}

	// 数据提交
	public void commit() {
		if (Tool.isEmpty(et_citiao.getText().toString())) {
			ToastUtil.showShortToast("词条名不能为空");
			return;
		}
		if (Tool.isEmpty(et_gaishu.getText().toString())) {
			ToastUtil.showShortToast("概述不能为空");
			return;
		}
		if (Tool.isEmpty(img_waiceng.getDrawable())) {
			ToastUtil.showShortToast("概述图不能为空");
			return;
		}
		ReqBase req = new ReqBase();
		BaikeMkEntity bk = new BaikeMkEntity();
		bk.setUserId(AppContext.getInstance().getUserInfo().getUserId());// 用户id
		bk.setCategoryId(1);
		bk.setName(et_citiao.getText().toString());// 词条名
		bk.setSummary(et_gaishu.getText().toString());// 概述
		bk.setChnName(et_chinesename.getText().toString());// 中文名
		bk.setEnName(et_foreignname.getText().toString());// 外文名
		bk.setAliasName(et_othername.getText().toString());// 别名
		bk.setCountry(et_country.getText().toString());// 国籍
		bk.setNational(et_nation.getText().toString());// 民族
		bk.setBirthPlace(tv_chushengcity.getText().toString());// 出身地
		bk.setBirthDate(tv_chushengtime.getText().toString());// 出身日期
		bk.setPositionName(et_profession.getText().toString());// 职业
		bk.setGraduateSchool(et_school.getText().toString());// 毕业院校
		bk.setBelief(et_faith.getText().toString());// 信仰
		String achievementStr = "";
		if (!Tool.isEmpty(et_achievement.getText().toString())) {
			achievementStr = et_achievement.getText().toString()
					+ ((listBkAchievement.size() == 0) ? "" : "%%");
		}
		for (int i = 0, size = listBkAchievement.size(); i < size; i++) {
			if (Tool.isEmpty(listBkAchievement.get(i))) {
				continue;
			}
			achievementStr += listBkAchievement.get(i)
					+ (i == size - 1 ? "" : "%%");
		}
		bk.setAchievement(achievementStr);// 主要成就
		LogUtil.d("hl", "achievementStr=" + achievementStr);
		bk.setMajorWorks(et_works.getText().toString());// 代表作品
		for (int i = 0, size = listBkBasic.size(); i < size; i++) {
			listBkBasic.get(i).setSortNo(i + 1);
		}
		bk.setListBkBasic(listBkBasic);// 自定义项
		bk.setRemark(et_jianjie.getText().toString());// 个人简介
		bk.setHobby(et_aihao.getText().toString());// 兴趣爱好
		bk.setDetailRemark(et_shuomoing.getText().toString());// 个人说明
		for (int i = 0, size = listBkContent.size(); i < size; i++) {
			listBkContent.get(i).setSortNo(i + 1);
		}
		bk.setListBkContent(listBkContent);// 更多介绍
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (!Tool.isEmpty(imgresault)) {
			List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
			FormFileBean gaishuImgFile = new FormFileBean(new File(imgresault),
					"gaishuImg.png");
			formFileBeans.add(gaishuImgFile);
			mapFileBean.put("mainPhoto", formFileBeans);// 概述图片
		}
		List<FormFileBean> xcFileBeans = new ArrayList<FormFileBean>();
		for (int i = 1, size = photolist.size(); i < size; i++) {
			FormFileBean photo = new FormFileBean(new File(photolist.get(i)
					.getClientFileUrl()), "photo_" + i + ".png");
			xcFileBeans.add(photo);
		}
		if (xcFileBeans.size() > 1) {
			mapFileBean.put("photo", xcFileBeans);// 相册图片
		}
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PERSONBAIKE));
		req.setBody(JsonUtil.Entity2JsonObj(bk));
		uploadFiles(reqCodeOne, AppConfig.PUBLICK_BAIKE, mapFileBean, req, true);

	}

	public void gotoEdit() {
		if (Tool.isEmpty(et_citiao.getText().toString())) {
			ToastUtil.showShortToast("词条名不能为空");
			return;
		}
		if (Tool.isEmpty(et_gaishu.getText().toString())) {
			ToastUtil.showShortToast("概述不能为空");
			return;
		}
		if (Tool.isEmpty(img_waiceng.getDrawable())) {
			ToastUtil.showShortToast("概述图不能为空");
			return;
		}
		ReqBase req = new ReqBase();
		BaikeMkEntity bk = new BaikeMkEntity();
		bk.setUserId(AppContext.getInstance().getUserInfo().getUserId());// 用户id
		bk.setCategoryId(1);
		bk.setId(edit_baikeId);
		bk.setName(et_citiao.getText().toString());// 词条名
		bk.setSummary(et_gaishu.getText().toString());// 概述
		bk.setChnName(et_chinesename.getText().toString());// 中文名
		bk.setEnName(et_foreignname.getText().toString());// 外文名
		bk.setAliasName(et_othername.getText().toString());// 别名
		bk.setCountry(et_country.getText().toString());// 国籍
		bk.setNational(et_nation.getText().toString());// 民族
		bk.setBirthPlace(tv_chushengcity.getText().toString());// 出身地
		bk.setBirthDate(tv_chushengtime.getText().toString());// 出身日期
		bk.setPositionName(et_profession.getText().toString());// 职业
		bk.setGraduateSchool(et_school.getText().toString());// 毕业院校
		bk.setBelief(et_faith.getText().toString());// 信仰
		String achievementStr = "";
		if (!Tool.isEmpty(et_achievement.getText().toString())) {
			achievementStr = et_achievement.getText().toString()
					+ ((listBkAchievement.size() == 0) ? "" : "%%");
		}
		for (int i = 0, size = listBkAchievement.size(); i < size; i++) {
			if (Tool.isEmpty(listBkAchievement.get(i))) {
				continue;
			}
			achievementStr += listBkAchievement.get(i)
					+ (i == size - 1 ? "" : "%%");
		}
		bk.setAchievement(achievementStr);// 主要成就
		bk.setMajorWorks(et_works.getText().toString());// 代表作品
		bk.setRemark(et_jianjie.getText().toString());// 个人简介
		bk.setHobby(et_aihao.getText().toString());// 兴趣爱好
		bk.setDetailRemark(et_shuomoing.getText().toString());// 个人说明
		req.setHeader(new ReqHead(AppConfig.BUSINESS_BK_EDITFIRSTPAGE));
		req.setBody(JsonUtil.Entity2JsonObj(bk));
		httpPost(reqCodeFive, AppConfig.PUBLICK_BAIKE, req);
	}

	/**
	 * 添加一个详情
	 * 
	 * @param title
	 * @param detail
	 */
	private void AddOneDetailInfo(int position,
			final MkMrrckBaikeContent mkAddInfo) {
		View view = LayoutInflater.from(CreatePersonEntryActivity.this)
				.inflate(R.layout.item_baike_addinfo, null, false);
		TextView title_add = (TextView) view.findViewById(R.id.title_add);
		title_add.setText(mkAddInfo.getTitle());
		EditText et_add = (EditText) view.findViewById(R.id.et_add);
		et_add.setText(mkAddInfo.getContent());
		layout_addmoreInfo.addView(view, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		et_add.addTextChangedListener(new AddInfoETTextWatcherListener(
				position, TYPE_EDIT_BKCONTENT));
		view.findViewById(R.id.delete).setOnClickListener(
				new DeleteClick(position, TYPE_EDIT_BKCONTENT));
		if (pageType == 1) {
			et_add.setEnabled(false);
			ImageView edit = (ImageView) view.findViewById(R.id.edit);
			edit.setVisibility(View.VISIBLE);
			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent addiIntent = new Intent(
							CreatePersonEntryActivity.this,
							AddEntryActivity.class);
					addiIntent.putExtra("moreflag", 5);
					addiIntent.putExtra("baikeId", edit_baikeId);
					Bundle bundle = new Bundle();
					bundle.putSerializable("baikeContentInfo", mkAddInfo);
					addiIntent.putExtras(bundle);
					startActivityForResult(addiIntent, reqEditOneDetail);
				}
			});
		}
	}

	/***
	 * 自定义添加的详情，输入框监听
	 */
	class AddInfoETTextWatcherListener implements TextWatcher {

		/** 0基本信息，1自定义项更多 */
		private int type;
		private int position;

		public AddInfoETTextWatcherListener(int position, int type) {
			this.position = position;
			this.type = type;
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			if (type == TYPE_EDIT_BKBASIC) {
				listBkBasic.get(position).setContent(arg0.toString());
			} else if (type == TYPE_EDIT_BKCONTENT) {
				listBkContent.get(position).setContent(arg0.toString());
			} else if (type == TYPE_EDIT_ACHIEVE) {
				listBkAchievement.set(position, arg0.toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

	}

	/***
	 * 自定义添加的详情删除按钮监听
	 */
	class DeleteClick implements OnClickListener {

		/** 0基本信息，1自定义项更多 */
		private int type;
		private int position;

		public DeleteClick(int position, int type) {
			this.position = position;
			this.type = type;
		}

		@Override
		public void onClick(View arg0) {
			if (type == TYPE_EDIT_BKBASIC) {
				if (pageType == 1
						&& !Tool.isEmpty(listBkBasic.get(position).getId())) {
					deleteOneBaseInfo(listBkBasic.get(position).getId());
				}
				layout_baseInfoMore.removeAllViews();
				listBkBasic.remove(position);
				LogUtil.d("hl", "自定义添加基本数据=" + listBkBasic.size());
				for (int i = 0, size = listBkBasic.size(); i < size; i++) {
					AddOneBaseInfo(i, listBkBasic.get(i));
				}
			} else if (type == TYPE_EDIT_BKCONTENT) {
				if (pageType == 1
						&& !Tool.isEmpty(listBkContent.get(position).getId())) {
					deleteOneDetailInfo(listBkContent.get(position).getId());
				}
				layout_addmoreInfo.removeAllViews();
				listBkContent.remove(position);
				LogUtil.d("hl", "自定义添加数据=" + listBkContent.size());
				for (int i = 0, size = listBkContent.size(); i < size; i++) {
					AddOneDetailInfo(i, listBkContent.get(i));
				}
			} else if (type == TYPE_EDIT_ACHIEVE) {
				layout_addAchievement.removeAllViews();
				listBkAchievement.remove(position);
				LogUtil.d("hl", "自定义添加荣誉数据=" + listBkAchievement.size());
				for (int i = 0, size = listBkAchievement.size(); i < size; i++) {
					AddOneAchievementInfo(i, listBkAchievement.get(i));
				}
			}

		}

	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}
}
