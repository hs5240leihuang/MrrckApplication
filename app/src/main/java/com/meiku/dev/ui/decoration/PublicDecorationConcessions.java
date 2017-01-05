package com.meiku.dev.ui.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.DecorateCompanyFavourContentEntity;
import com.meiku.dev.bean.DecorateCompanyFavourEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;

/**
 * 我的发布-->发布装修优惠
 * 
 */
public class PublicDecorationConcessions extends BaseActivity {
	private EditText et_youhuione, et_youhuitwo, et_youhuithree, et_youhuifour;
	private Button btn_publish;
	private ArrayList<FavourContent> favourContentList = new ArrayList<FavourContent>();
	private ArrayList<AddContent> addContentList = new ArrayList<AddContent>();
	private ArrayList<UpdateContent> updateContentList = new ArrayList<UpdateContent>();
	private int flag;
	private TextView center_txt_title;
	private List<DecorateCompanyFavourContentEntity> favourConten;
	private int id;
	private List<Integer> deleteIds = new ArrayList<Integer>();

	class FavourContent {
		String name;

		public FavourContent(String name) {
			this.name = name;
		}
	}

	class AddContent {
		String name;

		public AddContent(String name) {
			this.name = name;
		}
	}

	class UpdateContent {
		String name;
		int id;

		public UpdateContent(String name, int id) {
			this.name = name;
			this.id = id;
		}
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_publicdecorationconcessions;
	}

	@Override
	public void initView() {
		center_txt_title = (TextView) findViewById(R.id.center_txt_title);
		et_youhuione = (EditText) findViewById(R.id.et_youhuione);
		et_youhuitwo = (EditText) findViewById(R.id.et_youhuitwo);
		et_youhuithree = (EditText) findViewById(R.id.et_youhuithree);
		et_youhuifour = (EditText) findViewById(R.id.et_youhuifour);
		btn_publish = (Button) findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (flag == 1) {
					PanDuan();
					EditData();
				} else {
					if (!Tool.isEmpty(et_youhuione.getText().toString().trim())) {
						favourContentList.add(new FavourContent(et_youhuione
								.getText().toString().trim()));
					}
					if (!Tool.isEmpty(et_youhuitwo.getText().toString().trim())) {
						favourContentList.add(new FavourContent(et_youhuitwo
								.getText().toString().trim()));
					}
					if (!Tool.isEmpty(et_youhuithree.getText().toString()
							.trim())) {
						favourContentList.add(new FavourContent(et_youhuithree
								.getText().toString().trim()));
					}
					if (!Tool
							.isEmpty(et_youhuifour.getText().toString().trim())) {
						favourContentList.add(new FavourContent(et_youhuifour
								.getText().toString().trim()));
					}
					if (Tool.isEmpty(favourContentList)) {
						ToastUtil.showShortToast("请填写装修优惠");
						return;
					}
					GetData();
				}

			}
		});
	}

	@Override
	public void initValue() {
		id = getIntent().getIntExtra("id", -1);
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 1) {
			favourConten = (List<DecorateCompanyFavourContentEntity>) getIntent()
					.getExtras().getSerializable("youhui");
			center_txt_title.setText("编辑装修优惠");
			et_youhuione.setText(favourConten.get(0).getName());
			et_youhuitwo.setText(favourConten.get(1).getName());
			et_youhuithree.setText(favourConten.get(2).getName());
			et_youhuifour.setText(favourConten.get(3).getName());

		} else {
			center_txt_title.setText("发布装修优惠");
		}
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("000001", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			setResult(RESULT_OK);
			finish();
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
		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						PublicDecorationConcessions.this, "提示", resp
								.getHeader().getRetMessage(), "确定");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			}
			break;
		default:
			break;
		}
	}

	// 发布装修优惠
	public void GetData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("favourContents", JsonUtil.listToJsonArray(favourContentList));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300026));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_DECORATION, req);
	}

	// 编辑装修优惠
	public void EditData() {
		if (Tool.isEmpty(et_youhuione.getText().toString().trim())
				&& Tool.isEmpty(et_youhuitwo.getText().toString().trim())
				&& Tool.isEmpty(et_youhuithree.getText().toString().trim())
				&& Tool.isEmpty(et_youhuifour.getText().toString().trim())) {
			ToastUtil.showShortToast("编辑不能为空");
			return;
		}
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		String delDecorateIds = "";
		for (int i = 0, size = deleteIds.size(); i < size; i++) {
			delDecorateIds += deleteIds.get(i) + ((i == size - 1) ? "" : ",");
		}
		map.put("delDecorateIds", delDecorateIds);
		map.put("addDecorates", JsonUtil.listToJsonArray(addContentList));
		map.put("updateDecorates", JsonUtil.listToJsonArray(updateContentList));
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_300018));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_DECORATION, req);
	}

	public void PanDuan() {

		if (!TextUtils.equals(favourConten.get(0).getName(), et_youhuione
				.getText().toString().trim())) {
			if (Tool.isEmpty(et_youhuione.getText().toString().trim())) {
				deleteIds.add(favourConten.get(0).getId());
			} else {
				if (Tool.isEmpty(favourConten.get(0).getName())) {
					addContentList.add(new AddContent(et_youhuione.getText()
							.toString().trim()));
				} else {
					updateContentList.add(new UpdateContent(et_youhuione
							.getText().toString().trim(), favourConten.get(0)
							.getId()));
				}
			}
		}
		if (!TextUtils.equals(favourConten.get(1).getName(), et_youhuitwo
				.getText().toString().trim())) {
			if (Tool.isEmpty(et_youhuitwo.getText().toString().trim())) {
				deleteIds.add(favourConten.get(1).getId());
			} else {
				if (Tool.isEmpty(favourConten.get(1).getName())) {
					addContentList.add(new AddContent(et_youhuitwo.getText()
							.toString().trim()));

				} else {
					updateContentList.add(new UpdateContent(et_youhuitwo
							.getText().toString().trim(), favourConten.get(1)
							.getId()));
				}
			}

		}
		if (!TextUtils.equals(favourConten.get(2).getName(), et_youhuithree
				.getText().toString().trim())) {
			if (Tool.isEmpty(et_youhuithree.getText().toString().trim())) {
				deleteIds.add(favourConten.get(2).getId());
			} else {
				if (Tool.isEmpty(favourConten.get(2).getName())) {
					addContentList.add(new AddContent(et_youhuithree.getText()
							.toString().trim()));

				} else {
					updateContentList.add(new UpdateContent(et_youhuithree
							.getText().toString().trim(), favourConten.get(2)
							.getId()));
				}
			}

		}
		if (!TextUtils.equals(favourConten.get(3).getName(), et_youhuifour
				.getText().toString().trim())) {
			if (Tool.isEmpty(et_youhuifour.getText().toString().trim())) {
				deleteIds.add(favourConten.get(3).getId());
			} else {
				if (Tool.isEmpty(favourConten.get(3).getName())) {
					addContentList.add(new AddContent(et_youhuifour.getText()
							.toString().trim()));
				} else {
					updateContentList.add(new UpdateContent(et_youhuifour
							.getText().toString().trim(), favourConten.get(3)
							.getId()));
				}
			}

		}
	}
}
