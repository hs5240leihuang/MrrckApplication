package com.meiku.dev.ui.im;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.GroupManageImageBean;
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
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.ViewHolder;

public class CommitActivity extends BaseActivity implements OnClickListener {
	private TextView commit, txtphoto;
	private LinearLayout linzhengju;
	private ImageView imgarrow;
	private final int reqimg = 108;
	private final int reqadd = 109;
	private List<GroupManageImageBean> photolist = new ArrayList<GroupManageImageBean>();
	private CommonAdapter<GroupManageImageBean> roomAdapter;
	private GridView gv_show;
	private int reportType;
	private String friendid;
	private List<FormFileBean> company_FileBeans = new ArrayList<FormFileBean>();
	private String sourceType;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_tijiao;
	}

	@Override
	public void initView() {
		commit = (TextView) findViewById(R.id.right_txt_title);
		linzhengju = (LinearLayout) findViewById(R.id.linzhengju);
		gv_show = (GridView) findViewById(R.id.gv_show);
		imgarrow = (ImageView) findViewById(R.id.imgarrow);
		txtphoto = (TextView) findViewById(R.id.txtphoto);
	}

	@Override
	public void initValue() {
		sourceType = getIntent().getStringExtra("sourceType");
		friendid = getIntent().getStringExtra("friendid");
		reportType = getIntent().getIntExtra("reportType", 0);
		commit.setText("提交");
	}

	@Override
	public void bindListener() {
		commit.setOnClickListener(this);
		linzhengju.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			startActivity(new Intent(this, ReportSuccessActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("举报失败");
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.right_txt_title:
			if (!Tool.isEmpty(photolist) && photolist.size() > 1) {
				// company_FileBeans = new ArrayList<FormFileBean>();
				for (int i = 1; i < photolist.size(); i++) {
					Object imgPathString = photolist.get(i).getClientPicUrl();
					if (imgPathString.getClass().getName()
							.equals(String.class.getName())) {
						FormFileBean ffb = new FormFileBean();
						ffb.setFileName("report_image_" + i + ".png");
						ffb.setFile(new File((String) imgPathString));
						company_FileBeans.add(ffb);

					}
				}
			}
			if (company_FileBeans.size() > 0) {
				report();
			} else {
				ToastUtil.showShortToast("请选择图片");
			}
			break;
		case R.id.linzhengju:
			Intent intent = new Intent(CommitActivity.this,
					SelectPictureActivity.class);
			intent.putExtra("SELECT_MODE", SelectPictureActivity.MODE_SINGLE);// 选择模式
			intent.putExtra("MAX_NUM", 8);// 选择的张数
			startActivityForResult(intent, reqimg);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqimg) {
				List<String> pictrue = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPictrue = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(reqimg, pictrue.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqimg, photoPath);
				}
			} else {
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
				}
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
			case reqimg:
				initphotoGrid();
				GroupManageImageBean groupManageImageBean = new GroupManageImageBean();
				groupManageImageBean.setClientPicUrl(result);
				photolist.add(groupManageImageBean);
				roomAdapter.notifyDataSetChanged();
				dismissProgressDialog();
				linzhengju.setEnabled(false);
				imgarrow.setVisibility(View.GONE);
				txtphoto.setText("1张图片");
				break;
			case reqadd:
				// initphotoGrid();
				GroupManageImageBean groupManageImageBean1 = new GroupManageImageBean();
				groupManageImageBean1.setClientPicUrl(result);
				photolist.add(groupManageImageBean1);
				roomAdapter.notifyDataSetChanged();
				dismissProgressDialog();
				txtphoto.setText(photolist.size() - 1 + "张图片");

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

	private void initphotoGrid() {
		photolist.clear();
		GroupManageImageBean groupManageImageBeanAdd = new GroupManageImageBean();
		groupManageImageBeanAdd.setClientPicUrl(R.drawable.tianjia + "");
		photolist.add(groupManageImageBeanAdd);
		roomAdapter = new CommonAdapter<GroupManageImageBean>(this,
				R.layout.item_photogrid, photolist) {

			@Override
			public void convert(final ViewHolder viewHolder,
					final GroupManageImageBean t) {
				if (viewHolder.getPosition() == 0) {
					viewHolder.setImage(R.id.image_out,
							Integer.parseInt(t.getClientPicUrl()));
					viewHolder.getConvertView().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									if (photolist.size() >= 9) {
										ToastUtil.showShortToast("最多只能上传8张照片！");
										return;
									}
									Intent intent = new Intent(
											CommitActivity.this,
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
					viewHolder.setImage(R.id.image, t.getClientPicUrl(), 1);
					viewHolder.getConvertView().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									final CommonDialog commonDialog = new CommonDialog(
											CommitActivity.this, "提示",
											"是否删除该照片", "确认", "取消");
									commonDialog.show();
									commonDialog
											.setClicklistener(new ClickListenerInterface() {

												@Override
												public void doConfirm() {
													photolist.remove(viewHolder
															.getPosition());
													roomAdapter
															.notifyDataSetChanged();
													if (photolist.size()>1) {
														txtphoto.setText(photolist
																.size()-1 + "张图片");
													}else{
														txtphoto.setText("");
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

	// 举报请求
	public void report() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sourceId", friendid);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("sourceType", sourceType);
		map.put("reportType", reportType);
		map.put("remark", "");
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PERSON_REPORT));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));

		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		if (!Tool.isEmpty(photolist) && photolist.size() > 1) {
			company_FileBeans = new ArrayList<FormFileBean>();
			for (int i = 1; i < photolist.size(); i++) {
				Object imgPathString = photolist.get(i).getClientPicUrl();
				if (imgPathString.getClass().getName()
						.equals(String.class.getName())) {
					FormFileBean ffb = new FormFileBean();
					ffb.setFileName("report_image_" + i + ".png");
					ffb.setFile(new File((String) imgPathString));
					company_FileBeans.add(ffb);

				}
			}
			// if (photolist.size() > 1) {
			// Object imgPathString = photolist.get(photolist.size() - 1);
			// if (imgPathString.getClass().getName()
			// .equals(String.class.getName())) {
			// FormFileBean ffb = new FormFileBean();
			// ffb.setFileName("company_image_" + (photolist.size() - 1)
			// + ".png");
			// ffb.setFile(new File((String) imgPathString));
			// company_FileBeans.add(ffb);
			// }
			// mapFileBean.put("photo", company_FileBeans);
			// }
		}
		mapFileBean.put("photo", company_FileBeans);
		uploadFiles(reqCodeOne, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
				mapFileBean, reqBase, true);

	}
}
