package com.meiku.dev.ui.myshow;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
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
import com.meiku.dev.bean.PostsEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MySimpleDraweeView;

public class EditWorkActivity extends BaseActivity implements OnClickListener {

	private Button btn_publish;
	private TextView tv_category;
	private EditText et_name, et_introduce;
	private LinearLayout layout_category;
	private LinearLayout lin_pic;

	private String categoryName, categoryId, name, clientPicUrl, content,
			signupId;
	private LinearLayout layout_top;
	private ImageView iv_top;
	private MySimpleDraweeView iv_pic;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_edit_work;
	}

	@Override
	public void initView() {
		btn_publish = (Button) findViewById(R.id.btn_publish);
		tv_category = (TextView) findViewById(R.id.tv_category);
		et_name = (EditText) findViewById(R.id.et_name);
		et_introduce = (EditText) findViewById(R.id.et_introduce);
		layout_category = (LinearLayout) findViewById(R.id.layout_category);
		lin_pic = (LinearLayout) findViewById(R.id.lin_pic);
		layout_top = (LinearLayout) findViewById(R.id.layout_top);
		iv_top = (ImageView) findViewById(R.id.iv_top);
	}

	@Override
	public void initValue() {
		categoryName = getIntent().getStringExtra("categoryName");
		categoryId = getIntent().getStringExtra("categoryId");
		name = getIntent().getStringExtra("name");
		clientPicUrl = getIntent().getStringExtra("clientPicUrl");
		content = getIntent().getStringExtra("content");
		signupId = getIntent().getStringExtra("signupId");
		iv_pic = new MySimpleDraweeView(EditWorkActivity.this);
		lin_pic.setOnClickListener(this);
		lin_pic.removeAllViews();
		lin_pic.addView(iv_pic, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv_pic.setUrlOfImage(clientPicUrl);
		et_name.setText(name);
		et_introduce.setText(content);
		tv_category.setText(categoryName);
		getTop();
	}

	private void getTop() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("cityCode",
				Tool.isEmpty(MrrckApplication.getInstance().cityCode) ? -1
						: MrrckApplication.getInstance().cityCode);
		req.setHeader(new ReqHead(
				AppConfig.BUSINESS_SEARCH_SOLICITATION_OF_PUBLICITY));
		req.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_BOARD, req, true);
	}

	@Override
	public void bindListener() {
		layout_category.setOnClickListener(this);
		btn_publish.setOnClickListener(this);

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase req = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			final PostsEntity postsGuide;
			if (req.getBody().get("postsGuide").toString().length() > 2) {
				postsGuide = (PostsEntity) JsonUtil.jsonToObj(
						PostsEntity.class, req.getBody().get("postsGuide")
								.toString());
				if (Tool.isEmpty(postsGuide)
						|| Tool.isEmpty(postsGuide.getClientRecommendImgUrl())) {
					layout_top.setVisibility(View.GONE);
				} else {
					iv_pic.setUrlOfImage(postsGuide.getClientRecommendImgUrl());
					iv_top.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							startActivity(new Intent(EditWorkActivity.this,
									ShowMainActivity.class).putExtra("postsId",
									postsGuide.getPostsId()));
						}
					});
				}
			} else {
				layout_top.setVisibility(View.GONE);
			}
			break;
		case reqCodeTwo:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		ToastUtil.showShortToast("失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_category:
			startActivityForResult(new Intent(EditWorkActivity.this,
					WorkCategoryActivity.class), reqCodeOne);
			break;
		case R.id.btn_publish:
			if (validate()) {
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("signupId", signupId);
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("name", et_name.getText().toString());
				map.put("categoryId", categoryId);
				map.put("remark", et_introduce.getText().toString());
				reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_EDIT_WORK));
				reqBase.setBody(JsonUtil.String2Object(JsonUtil
						.hashMapToJson(map)));
				LogUtil.d("hl", "报名_请求=" + map);
				httpPost(reqCodeTwo, AppConfig.PUBLICK_BOARD, reqBase);
			}
			break;
		case R.id.lin_pic:
			ToastUtil.showShortToast("作品不可更换");
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				categoryId = data.getStringExtra("categoryId");
				tv_category.setText(data.getStringExtra("categoryName"));
				// fileType = data.getStringExtra("fileType");
				break;
			default:
				break;
			}
		}
	}

	private boolean validate() {
		if (Tool.isEmpty(tv_category.getText().toString())) {
			ToastUtil.showShortToast("请选择作品类别");
			return false;
		} else if (Tool.isEmpty(et_name.getText().toString())) {
			ToastUtil.showShortToast("请输入作品名称");
			return false;
		} else if (Tool.isEmpty(et_introduce.getText().toString())) {
			ToastUtil.showShortToast("请输入作品介绍");
			return false;
		} else {
			return true;
		}
	}
}
