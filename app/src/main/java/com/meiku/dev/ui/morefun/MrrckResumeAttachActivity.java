package com.meiku.dev.ui.morefun;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.MrrckResumeAttachAdapter;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.ResumeAttachData;
import com.meiku.dev.bean.ResumeAttachData.ResumeAttachReq;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.views.TopTitle;

/**
 * 个人简历视频列表
 * <p/>
 * Created by zjh on 2015/8/27.
 */
public class MrrckResumeAttachActivity extends BaseActivity implements
		View.OnClickListener {

	/**
	 * 附件类型
	 */
	public static final String BUNDLE_ATTACH_TYPE = "attach_type";
	public static final int TAKE_VIDEO = 8; // 录制视频
	public static final int TAKE_SOUND = 9; // 录制音频
	public static final int TO_SELECT_VIDEO = 16;// 视频选择

	private static final int REQUESTCODE_GETRESUMEMEDIA = 10002;
	private static final int REQUESTCODE_DELRESUMEMEDIA = 10003;
	public static final int REQCODE_MKRESUMEVOICE = 4001;// 音频简历
	public static final int REQCODE_MKRESUMEVEDIO = 4002;// 视频简历

	private ListView videoLV;

	private MrrckResumeAttachAdapter mAdapter;

	private List<ResumeAttachData> mList = new ArrayList<ResumeAttachData>();
	private boolean flag = false;

	/**
	 * 简历id
	 */
	private int mResumeId;

	/**
	 * 附件类型 1 video 2 voice
	 */
	private int attachType;
	/**
	 * 显示删除和全选的按钮
	 */
	private LinearLayout delet_layout;
	/**
	 * 全选
	 */
	private TextView all_text;
	/**
	 * 删除信息
	 */
	private TextView delet_text;
	/**
	 * 显示添加录音或者录视频的按钮
	 */
	private TextView add_text;

	private String videoPath;
	private String soundPath;// 录音路径
	private String recordingTime;// 录音时间
	private TextView edit_text;
	private String resumeMrrckId;

	public void initData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("fileType", attachType);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_GET_MK_RESUME_MEDIA));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(REQUESTCODE_GETRESUMEMEDIA, AppConfig.RESUME_REQUEST_MAPPING,
				reqBase, true);
	}

	private void delResume(final List<HashMap<String, Object>> List) {

		StringBuilder builder = new StringBuilder();
		if (null != List && !List.isEmpty()) {
			for (int i = 0; i < List.size(); i++) {
				builder.append(List.get(i).get("attachmentId") + ",");
			}
		}

		String attactmentIds = builder.toString();
		attactmentIds = attactmentIds.substring(0, attactmentIds.length() - 1);

		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("attactmentIds", attactmentIds);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_DELETE_MK_RESUME_MEDIA));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(REQUESTCODE_DELRESUMEMEDIA, AppConfig.RESUME_REQUEST_MAPPING,
				reqBase, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goback: // 返回
			setResult(Activity.RESULT_OK);
			finish();
			break;
		case R.id.right_res_title: // 编辑

			if (flag) {
				flag = false;
				mAdapter.setShowCheckBox(false);
				delet_layout.setVisibility(View.GONE);
				add_text.setVisibility(View.VISIBLE);
				edit_text.setText("编辑");
			} else {
				flag = true;
				mAdapter.setShowCheckBox(true);
				delet_layout.setVisibility(View.VISIBLE);
				add_text.setVisibility(View.GONE);
				edit_text.setText("完成");
			}

			// List<ResumeAttachData> list = new ArrayList<ResumeAttachData>();
			// list.add(mList.get(0));
			// delResume(list);
			break;
		case R.id.add_text:
			Intent intent;
			if (1 == attachType) {
				intent = new Intent(this, ShootVideoActivity.class);
				startActivityForResult(intent, TO_SELECT_VIDEO);
			} else if (2 == attachType) {
				intent = new Intent(this, RecordActivity.class);
				intent.putExtra(RecordActivity.BUNDLE_IS_PUBLISH, "1");
				startActivityForResult(intent, TAKE_SOUND);
			}
			break;
		case R.id.all_text:
			// 遍历list的长度，将MyAdapter中的map值全部设为true
			for (int i = 0; i < mList.size(); i++) {
				mAdapter.getIsSelected().put(i, true);
			}
			// 数量设为list的长度
			// checkNum = mList.size();
			// 刷新listview和TextView的显示
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.delet_text:
			List<HashMap<String, Object>> List = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> hm;
			for (int i = 0; i < mList.size(); i++) {
				if (mAdapter.getIsSelected().get(i)) {
					hm = new HashMap<String, Object>();
					hm.put("attachmentId", mList.get(i).attachmentId);
					hm.put("position", i + "");
					List.add(hm);
				}

			}
			if (List.size() > 0) {
				delResume(List);
			}

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TO_SELECT_VIDEO: // 录制视频
				videoPath = data.getStringExtra("videoPath");
				String videoSeconds = data.getStringExtra("videoSeconds");
				String bitMapPath = data.getStringExtra("bitMapPath");

				ReqBase reqBase1 = new ReqBase();
				reqBase1.setHeader(new ReqHead(
						AppConfig.BUSINESS_UPLOAD_MK_RESUME_MEDIA));
				Map<String, Object> body1 = new HashMap<String, Object>();
				body1.put("userId",  AppContext.getInstance().getUserInfo()
						.getId());
				body1.put("fileSeconds", videoSeconds);
				body1.put("title", "");
				body1.put("remark", "");
				body1.put("isPublic", 1);
				reqBase1.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(body1)));
				Map<String, List<FormFileBean>> mapFileBean1 = new HashMap<String, List<FormFileBean>>();
				List<FormFileBean> formFileBeans1 = new ArrayList<FormFileBean>();
				FormFileBean formFile1 = new FormFileBean(new File(bitMapPath),
						"photo.png");
				formFileBeans1.add(formFile1);
				mapFileBean1.put("photo", formFileBeans1);

				List<FormFileBean> formFileBeans2 = new ArrayList<FormFileBean>();
				FormFileBean formFile2 = new FormFileBean(new File(videoPath),
						"video.mp4");
				formFileBeans2.add(formFile2);
				mapFileBean1.put("video", formFileBeans2);
				uploadFiles(REQCODE_MKRESUMEVEDIO,
						AppConfig.RESUME_REQUEST_MAPPING, mapFileBean1,
						reqBase1, true);

				// ResumeDataLogic.getInstance().uploadMKResumeMediaAttachWithUserId(AppData.getInstance().getLoginUser().getUserId(),
				// 1, videoSeconds, "", "", videoPath, bitMapPath, 1, new
				// HttpCallback() {
				// @Override
				// public void onSuccess(Object body) {
				//
				// dialog.dismiss();
				// initData();
				// mAdapter.notifyDataSetChanged();
				// }
				//
				// @Override
				// public void onFailed(String error) {
				// dialog.dismiss();
				// ToastUtil.showShortToast(error);
				// }
				// });

				// updateCompanyVideo(videoSeconds, videoPath, bitMapPath);
				break;
			case TAKE_SOUND: // 录制音频
				if (data != null) {
					String soundFile = data.getStringExtra("file");
					recordingTime = data.getStringExtra("recordingTime");// 播放时间
					soundPath = soundFile;

					ReqBase reqBase = new ReqBase();
					reqBase.setHeader(new ReqHead(
							AppConfig.BUSINESS_UPLOAD_MK_RESUME_MEDIA));
					Map<String, Object> body = new HashMap<String, Object>();
					body.put("userId",  AppContext.getInstance().getUserInfo()
							.getId());
					body.put("fileSeconds", recordingTime);
					body.put("title", "");
					body.put("remark", "");
					body.put("isPublic", 1);
					reqBase.setBody(JsonUtil.String2Object(JsonUtil
							.hashMapToJson(body)));
					Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
					List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
					FormFileBean formFile = new FormFileBean(
							new File(soundPath), "audio.mp3");
					formFileBeans.add(formFile);
					mapFileBean.put("voice", formFileBeans);
					uploadFiles(REQCODE_MKRESUMEVOICE,
							AppConfig.RESUME_REQUEST_MAPPING, mapFileBean,
							reqBase, true);
					// ResumeDataLogic.getInstance().uploadMKResumeMediaAttachWithUserId(AppData.getInstance().getLoginUser().getUserId(),
					// 0, recordingTime, "", "", soundPath, "", 1, new
					// HttpCallback() {
					// @Override
					// public void onSuccess(Object body) {
					// dialog2.dismiss();
					// initData();
					// mAdapter.notifyDataSetChanged();
					//
					// }
					//
					// @Override
					// public void onFailed(String error) {
					// dialog2.dismiss();
					// ToastUtil.showShortToast(error);
					// }
					// });
				}

				break;
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(Activity.RESULT_OK);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_resume_video;
	}

	@Override
	public void initView() {
		TopTitle topTitle = (TopTitle) findViewById(R.id.topTitle);
		TextView titleTXT = (TextView) findViewById(R.id.center_txt_title);
		if (1 == attachType) {
			titleTXT.setText("我的视频");
		} else if (2 == attachType) {
			titleTXT.setText("我的音频");
		}
		findViewById(R.id.goback).setOnClickListener(this);
		ImageView more = (ImageView) findViewById(R.id.right_res_title);
		more.setOnClickListener(this);
		add_text = (TextView) findViewById(R.id.add_text);
		if (1 == attachType) {
			add_text.setText("添加视频");
		} else if (2 == attachType) {
			add_text.setText("添加音频");
		}
		add_text.setOnClickListener(this);

		delet_layout = (LinearLayout) findViewById(R.id.delet_layout);
		all_text = (TextView) findViewById(R.id.all_text);
		all_text.setOnClickListener(this);

		delet_text = (TextView) findViewById(R.id.delet_text);
		delet_text.setOnClickListener(this);

		videoLV = (ListView) findViewById(R.id.videoLV);
		mAdapter = new MrrckResumeAttachAdapter(this);
		videoLV.setAdapter(mAdapter);

		edit_text = (TextView) findViewById(R.id.right_txt_title);

		// videoLV.setAdapter(mAdapter = new
		// CommonAdapter<ResumeAttachData>(this, R.layout.item_resume_video,
		// mList) {
		// @Override
		// public void convert(ViewHolder viewHolder, final ResumeAttachData
		// data) {
		// String title = data.title;
		// viewHolder.setText(R.id.nameTXT, TextUtils.isEmpty(title) ? "未命名" :
		// title);
		//
		// TextView resumeTXT = viewHolder.getView(R.id.resumeTXT);
		// resumeTXT.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// updateResume(data);
		// }
		// });
		// }
		// });
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub
		mResumeId = AppContext.getInstance().getUserInfo().getResumeId();
		attachType = getIntent().getIntExtra(BUNDLE_ATTACH_TYPE, 1);
		resumeMrrckId = getIntent().getStringExtra("resumeMrrckId");
		initData();
	}

	@Override
	public void bindListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case MrrckResumeAttachAdapter.REQUESTCODE_UPDATEMKRESUME:
			LogUtil.e(arg0.toString());
			initData();
			mAdapter.notifyDataSetChanged();
			break;
		case REQUESTCODE_GETRESUMEMEDIA:
			ReqBase resp = (ReqBase) arg0;
			String temp = resp.getBody().toString();
			if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil
					.getJSONType(temp)) {
				ResumeAttachData.ResumeAttachReq resumeAttachReq = (ResumeAttachReq) JsonUtil
						.jsonToObj(ResumeAttachData.ResumeAttachReq.class, temp);
				List<ResumeAttachData> datas = resumeAttachReq.attachmentList;
				if (datas != null && !datas.isEmpty()) {
					if (null != mList) {
						mList.clear();
					}
					mList.addAll(datas);

					mAdapter.setDatas(mList, attachType);
					mAdapter.notifyDataSetChanged();
				} else {
					if (null != mList) {
						mList.clear();
						mAdapter.notifyDataSetChanged();
					}
				}
			}

			// GsonParser<> parser = new
			// GsonParser<ResumeAttachData.ResumeAttachReq>(ResumeAttachData.ResumeAttachReq.class);
			// ResumeAttachData.ResumeAttachReq req =
			// parser.parse(body.toString());

			break;
		case REQUESTCODE_DELRESUMEMEDIA:
			initData();
			mAdapter.notifyDataSetChanged();
			break;
		case REQCODE_MKRESUMEVOICE:
			LogUtil.e(arg0.toString());
			initData();
			mAdapter.notifyDataSetChanged();
			break;
		case REQCODE_MKRESUMEVEDIO:
			LogUtil.e( arg0.toString());
			 initData();
			 mAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
