package com.meiku.dev.ui.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.PersonalPhotoEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.activitys.ImagePagerActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class PersonPhotoActivity extends BaseActivity implements
		OnClickListener {
	private PullToRefreshGridView pull_refreshGV;
	private LinearLayout editlayout;
	private TextView btnShare, btnSelectAll, btnDelete;
	private int page;
	protected boolean isEditModel;
	protected BaseAdapter showAdapter;
	protected boolean isUpRefresh;
	private boolean isSelectAll;
	private String userId;
	private TextView right_txt_title, center_txt_title;
	private Button btn_uploadingphoto;
	private final int reqphoto = 100;
	private String personalPhotoIds;
	private boolean hasedit = false;
	private List<PersonalPhotoEntity> showlist = new ArrayList<PersonalPhotoEntity>();
	private List<AttachmentListDTO> photolist = new ArrayList<AttachmentListDTO>();
	private List<HashMap<String, Object>> Indexlist = new ArrayList<HashMap<String, Object>>();

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_im_myphoto;
	}

	@Override
	public void initView() {
		btn_uploadingphoto = (Button) findViewById(R.id.btn_uploadingphoto);
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackgroundDrawable(null);
		userId = getIntent().getStringExtra("userId");
		if ((AppContext.getInstance().getUserInfo().getId() + "")
				.equals(userId)) {
			right_txt_title.setVisibility(View.VISIBLE);
			center_txt_title.setText("我的相册");
			btn_uploadingphoto.setVisibility(View.VISIBLE);
		}

		else {
			right_txt_title.setVisibility(View.GONE);
			center_txt_title.setText("TA的相册");
			btn_uploadingphoto.setVisibility(View.GONE);
		}
		initPullView();
		initBottomEditBar();
		downRefreshData();
	}

	@Override
	public void initValue() {

	}

	@Override
	public void bindListener() {
		btnShare.setOnClickListener(this);
		btnSelectAll.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		right_txt_title.setOnClickListener(this);
		btn_uploadingphoto.setOnClickListener(this);
		findViewById(R.id.goback).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("020202", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("personalPhoto") + "").length() > 2) {
				List<PersonalPhotoEntity> data = (List<PersonalPhotoEntity>) JsonUtil
						.jsonToList(resp.getBody().get("personalPhoto")
								.toString(),
								new TypeToken<List<PersonalPhotoEntity>>() {
								}.getType());
				if (!Tool.isEmpty(data)) {
					showlist.addAll(data);
					photolist.clear();
					for (int i = 0, size = showlist.size(); i < size; i++) {
						AttachmentListDTO attachmentListDTO = new AttachmentListDTO();
						attachmentListDTO.setClientFileUrl(showlist.get(i)
								.getClientFileUrl());
						photolist.add(attachmentListDTO);

						if (i == 0) {
							String currentTime = showlist.get(i)
									.getClientViewDate();
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("TIME", currentTime);
							map.put("INDEX", 0);
							Indexlist.add(map);
						} else {
							if (!showlist
									.get(i)
									.getClientViewDate()
									.equals(showlist.get(i - 1)
											.getClientViewDate())) {
								HashMap<String, Object> map2 = new HashMap<String, Object>();
								map2.put("TIME", showlist.get(i)
										.getClientViewDate());
								map2.put("INDEX", i);
								Indexlist.add(map2);
							}
						}
					}
					int k = 0;
					for (int j = 0, indexSize = Indexlist.size(); j < indexSize; j++) {
						if (j == 0) {

						} else {
							int currentIndex = (Integer) Indexlist.get(j).get(
									"INDEX");
							int distense = currentIndex
									- ((Integer) Indexlist.get(j - 1).get(
											"INDEX"));
							if (distense % 2 == 1) {
								LogUtil.d("hl", "empty——currentIndex="
										+ currentIndex);
								showlist.add(currentIndex + k,
										new PersonalPhotoEntity());
								k++;
							}
						}
					}
				}
			} else {
				if (isUpRefresh) {
					ToastUtil.showShortToast("无更多数据");
				} else {
					ToastUtil.showShortToast("暂无数据");
				}
			}
			showAdapter.notifyDataSetChanged();
			pull_refreshGV.onRefreshComplete();
			isUpRefresh = false;
			if (Tool.isEmpty(showlist)) {
				isSelectAll = false;
				btnSelectAll.setText(!isSelectAll ? "全选" : "取消全选");
				Drawable drawable = ContextCompat.getDrawable(
						PersonPhotoActivity.this,
						!isSelectAll ? R.drawable.photo_quanxuan
								: R.drawable.photo_cancel);
				drawable.setBounds(0, 0,
						ScreenUtil.dip2px(PersonPhotoActivity.this, 30),
						ScreenUtil.dip2px(PersonPhotoActivity.this, 30));
				btnSelectAll.setCompoundDrawables(drawable, null, null, null);
			}
			break;

		case reqCodeTwo:
			hasedit = true;
			downRefreshData();
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("新增照片成功");
			hasedit = true;
			downRefreshData();
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (null != pull_refreshGV) {
			pull_refreshGV.onRefreshComplete();
		}
		switch (requestCode) {
		case reqCodeOne:

			ToastUtil.showShortToast("获取相册数据失败！");
			break;
		case reqCodeTwo:
			isUpRefresh = false;
			downRefreshData();
			ToastUtil.showShortToast("删除失败！");
			break;
		case reqCodeThree:
			ToastUtil.showShortToast("新增相册数据失败！");
			break;
		}

	}

	private void initBottomEditBar() {
		editlayout = (LinearLayout) findViewById(R.id.editlayout);
		btnShare = (TextView) findViewById(R.id.btnShare);
		btnSelectAll = (TextView) findViewById(R.id.btnSelectAll);
		btnDelete = (TextView) findViewById(R.id.btnDelete);
	}

	private void initPullView() {
		pull_refreshGV = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		pull_refreshGV.setMode(PullToRefreshGridView.Mode.BOTH);
		pull_refreshGV
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						isUpRefresh = true;
						upRefreshData();
					}
				});
		showAdapter = new CommonAdapter<PersonalPhotoEntity>(
				PersonPhotoActivity.this, R.layout.item_personphoto, showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final PersonalPhotoEntity t) {
				final int position = viewHolder.getPosition();
				LinearLayout outside = viewHolder.getView(R.id.outside);
				if (Tool.isEmpty(t.getClientViewDate())) {
					outside.setVisibility(View.INVISIBLE);
				} else {
					outside.setVisibility(View.VISIBLE);
					RelativeLayout linelou = viewHolder.getView(R.id.linelou);
					final ImageView timeIcon = viewHolder
							.getView(R.id.timeIcon);
					final TextView time = viewHolder.getView(R.id.time);
					time.setText(t.getClientViewDate());
					if (!Tool.isEmpty(t.getClientViewDate())
							&& position > 0
							&& !Tool.isEmpty(showlist.get(position - 1)
									.getClientViewDate())
							&& showlist.get(position - 1).getClientViewDate()
									.equals(t.getClientViewDate())) {
						if (position % 2 == 0) {
							linelou.setVisibility(View.VISIBLE);
						} else {
							linelou.setVisibility(View.INVISIBLE);
						}
						time.setVisibility(View.INVISIBLE);
						timeIcon.setVisibility(View.GONE);
					} else {
						time.setVisibility(View.VISIBLE);
						linelou.setVisibility(View.VISIBLE);
						timeIcon.setVisibility(View.VISIBLE);
					}
					viewHolder
							.setImage(R.id.workImg, t.getClientThumbFileUrl());
					ImageView iv_select = viewHolder.getView(R.id.iv_select);
					if (isEditModel) {
						if (t.isTimeSelected()) {
							timeIcon.setBackgroundResource(R.drawable.icon_xuanzhong_circle);
						} else {
							timeIcon.setBackgroundResource(R.drawable.photo_xiaoyuan);
						}
						iv_select.setVisibility(View.VISIBLE);
					} else {
						iv_select.setVisibility(View.GONE);
						timeIcon.setBackgroundResource(R.drawable.time);
					}
					timeIcon.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (isEditModel) {
								t.setTimeSelected(!t.isTimeSelected());
								for (int i = 0, size = showlist.size(); i < size; i++) {
									String currentDate = t.getClientViewDate();
									String date = showlist.get(i)
											.getClientViewDate();
									if (!Tool.isEmpty(currentDate)
											&& !Tool.isEmpty(date)
											&& currentDate.equals(date)) {
										showlist.get(i).setSelected(
												t.isTimeSelected());
									}
								}
								notifyDataSetChanged();
							}
						}
					});
					if (t.isSelected()) {
						iv_select
								.setBackgroundResource(R.drawable.icon_xuanzhong_circle);
					} else {
						iv_select
								.setBackgroundResource(R.drawable.photo_xiaoyuan);
					}
					viewHolder.getView(R.id.workImg).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (isEditModel) {
										showlist.get(position).setSelected(
												!t.isSelected());
										showAdapter.notifyDataSetChanged();
										return;
									} else {
										startActivity(new Intent(
												PersonPhotoActivity.this,
												ImagePagerActivity.class)
												.putParcelableArrayListExtra(
														"imageDates",
														(ArrayList<? extends Parcelable>) photolist)
												.putExtra("index", position));
									}
								}
							});
				}
			}
		};
		pull_refreshGV.setAdapter(showAdapter);
	}

	/**
	 * 是否显示底部的编辑条
	 */
	private void isShowBottomEditBar(boolean show) {
		editlayout.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	protected void upRefreshData() {
		page++;
		Indexlist.clear();
		getMyShowData();
	}

	protected void downRefreshData() {
		page = 1;
		showlist.clear();
		Indexlist.clear();
		getMyShowData();
	}

	/**
	 * 获取我的相册数据
	 */
	private void getMyShowData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("page", page);
		map.put("pageNum", ConstantKey.PageNum);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_PHOTO));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING_USER, req, true);
	}

	/**
	 * 删除选择的相册
	 */
	private void deleteSelectedMyWorks() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("personalPhotoIds", personalPhotoIds);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_DELETE_PHOTO_MINE));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeTwo, AppConfig.PUBLIC_REQUEST_MAPPING_USER, req, true);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.goback:
			if (hasedit) {
				setResult(RESULT_OK);
				finish();
			} else {
				finish();
			}
			break;
		case R.id.btn_uploadingphoto:
			Intent intent = new Intent(PersonPhotoActivity.this,
					SelectPictureActivity.class);
			intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
			intent.putExtra("MAX_NUM", 1);// 选择的张数
			startActivityForResult(intent, reqphoto);
			break;
		case R.id.right_txt_title:
			isEditModel = !isEditModel;
			isShowBottomEditBar(isEditModel);
			showAdapter.notifyDataSetChanged();
			if (!isEditModel) {
				right_txt_title.setText("选择");
				for (int i = 0, size = showlist.size(); i < size; i++) {
					showlist.get(i).setSelected(false);
					showlist.get(i).setTimeSelected(false);
				}
			} else {
				right_txt_title.setText("取消");
			}
			break;
		case R.id.btnShare:

			break;
		case R.id.btnSelectAll:
			isSelectAll = !isSelectAll;
			btnSelectAll.setText(!isSelectAll ? "全选" : "取消全选");
			Drawable drawable1 = ContextCompat.getDrawable(
					PersonPhotoActivity.this,
					!isSelectAll ? R.drawable.photo_quanxuan
							: R.drawable.photo_cancel);
			drawable1.setBounds(0, 0,
					ScreenUtil.dip2px(PersonPhotoActivity.this, 30),
					ScreenUtil.dip2px(PersonPhotoActivity.this, 30));
			btnSelectAll.setCompoundDrawables(drawable1, null, null, null);
			for (int i = 0, size = showlist.size(); i < size; i++) {
				showlist.get(i).setSelected(isSelectAll);
				showlist.get(i).setTimeSelected(isSelectAll);
			}
			showAdapter.notifyDataSetChanged();
			break;
		case R.id.btnDelete:
			personalPhotoIds = "";
			for (int i = 0, size = showlist.size(); i < size; i++) {
				if (showlist.get(i).isSelected()
						&& !Tool.isEmpty(showlist.get(i).getCreateDate())) {
					personalPhotoIds += ","
							+ showlist.get(i).getPersonalPhotoId();
				}
			}
			if (personalPhotoIds.length() > 1) {
				personalPhotoIds = personalPhotoIds.substring(1,
						personalPhotoIds.length());
				deleteSelectedMyWorks();
			} else {
				ToastUtil.showShortToast("请勾选需要删除的照片");
			}
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (hasedit) {
				setResult(RESULT_OK);
				finish();
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqphoto) {
				String photoPath = data
						.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
				CompressPic(reqphoto, photoPath);

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
			case reqphoto:
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", userId);
				reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_ADD_PHOTO));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));

				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();

				// 照片
				List<FormFileBean> logo_FileBeans = new ArrayList<FormFileBean>();
				logo_FileBeans.add(new FormFileBean(new File(result),
						"logo.png"));
				mapFileBean.put("photo", logo_FileBeans);
				uploadFiles(reqCodeThree,
						AppConfig.PUBLIC_REQUEST_MAPPING_USER, mapFileBean,
						reqBase, true);
				LogUtil.d("hl", "压缩后处理__" + result);
				dismissProgressDialog();
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
}
