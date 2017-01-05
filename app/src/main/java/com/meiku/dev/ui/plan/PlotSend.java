package com.meiku.dev.ui.plan;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UploadImg;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.decoration.MyNeedDecPubActivity;
import com.meiku.dev.ui.decoration.NewPublicPostActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.utils.EmotionHelper;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.EmotionEditText;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

//找策划评论

public class PlotSend extends BaseActivity implements OnClickListener {
	private MyGridView gridview_uploadpic;
	private List<Object> picPathList = new ArrayList<Object>();// 上传店面图片
	private final int SELECT_COMPANYPICS = 1003;
	public int addpicCount;// 添加图片数量
	private TextView tv_hint;
	private Button btn_ok;
	private EmotionEditText et_content;
	private int needUploadPicSize;
	private String caseId;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_plotsend;
	}

	@Override
	public void initView() {
		et_content = (EmotionEditText) findViewById(R.id.et_content);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		gridview_uploadpic = (MyGridView) findViewById(R.id.gridview_uploadpic);
		picPathList.add("+");
		setSelectPic(true);
	}

	@Override
	public void initValue() {
		caseId = getIntent().getStringExtra("caseId");
	}

	@Override
	public void bindListener() {
		btn_ok.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("wangke", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody())
					&& !Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				JsonArray picJsonArray = resp.getBody().get("data")
						.getAsJsonArray();

				needUploadPicSize = picPathList.size() - 1;

				int webResultPicSize = picJsonArray.size();

				addpicCount = 0;
				if (!Tool.isEmpty(picJsonArray)
						&& needUploadPicSize == webResultPicSize) {
					for (int i = 0; i < webResultPicSize; i++) {
						ReqBase req = new ReqBase();
						Map<String, Object> map = new HashMap<String, Object>();
						List<String> oneInfo = new ArrayList<String>();
						oneInfo.add(picJsonArray.get(i).toString());
						map.put("fileUrlJSONArray",
								JsonUtil.listToJsonArray(oneInfo));
						req.setHeader(new ReqHead(
								AppConfig.BUSINESS_FILE_IMG10002));
						req.setBody(JsonUtil.Map2JsonObj(map));
						Log.e("hl", "upload pic request " + (i + 1) + "/"
								+ picJsonArray.size() + "==>" + map);
						Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
						List<FormFileBean> details_FileBeans = new ArrayList<FormFileBean>();
						FormFileBean mainFfb = new FormFileBean();
						mainFfb.setFileName("photo_" + i + ".png");

						mainFfb.setFile(new File((String) picPathList.get(i)));

						details_FileBeans.add(mainFfb);
						mapFileBean.put("file", details_FileBeans);
						uploadResFiles(2001 + i, AppConfig.PUBLICK_UPLOAD,
								mapFileBean, req, true);
					}
				}
			} else {
				ToastUtil.showShortToast(resp.getHeader().getRetMessage());
				setResult(RESULT_OK);
				finish();
			}
			break;
		default:
			if (requestCode > 2000) {
				addpicCount++;
				if (addpicCount == needUploadPicSize) {// 所有图片都上传则发布成功
					ToastUtil.showShortToast(resp.getHeader().getRetMessage());
					setResult(RESULT_OK);
					finish();
				}
			}
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				ToastUtil.showShortToast(resp.getHeader().getRetMessage()
						.toString());
			}
			break;
		default:
			break;
		}

	}

	private void setSelectPic(boolean isAdd) {
		gridview_uploadpic.setAdapter(new CommonAdapter<Object>(this,
				R.layout.add_talent_pic_gridview_item, picPathList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final Object imgItem) {
				// 如果大于6个删除第一个图片也就是增加图片按钮
				if (picPathList.size() < 7) {
					if (imgItem == getItem(picPathList.size() - 1)) {
						viewHolder.setImage(R.id.img_item,
								R.drawable.new_add_picture);
						viewHolder.getView(R.id.delete_btn).setVisibility(
								View.INVISIBLE);
						viewHolder.getView(R.id.img_item).setOnClickListener(
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												PlotSend.this,
												SelectPictureActivity.class);
										intent.putExtra(
												"SELECT_MODE",
												SelectPictureActivity.MODE_SINGLE);// 选择模式
										int picNum = 5 - picPathList.size();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_COMPANYPICS) { // 选择图片
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

			case SELECT_COMPANYPICS:
				addpicCount++;
				picPathList.add(picPathList.size() - 1, result);
				setSelectPic(true);
				if (addpicCount == size) {
					dismissProgressDialog();
				}
				tv_hint.setVisibility(View.GONE);
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
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_ok:
			if (Tool.isEmpty(et_content.getText().toString())) {
				ToastUtil.showShortToast("请输入评论内容");
				return;
			}
			Data();
			break;

		default:
			break;
		}
	}

	public void Data() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("planCaseId", caseId);
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("comment", EmotionHelper.getSendEmotion(PlotSend.this,
				et_content.getText().toString()));
		List<UploadImg> imgList = new ArrayList<UploadImg>();
		for (int i = 0, size = picPathList.size() - 1; i < size; i++) {
			UploadImg ui = new UploadImg();
			ui.setFileType("0");
			ui.setFileUrl("");
			ui.setFileThumb(((String) picPathList.get(i)).substring(
					((String) picPathList.get(i)).lastIndexOf(".") + 1,
					((String) picPathList.get(i)).length()));
			imgList.add(ui);
		}
		map.put("fileUrlJSONArray", JsonUtil.listToJsonArray(imgList));
		req.setHeader(new ReqHead(AppConfig.PLAN_500004));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.e("2323", "————" + map);
		httpPost(reqCodeOne, AppConfig.PLAN_REQUEST_MAPPING, req);
	}
}
