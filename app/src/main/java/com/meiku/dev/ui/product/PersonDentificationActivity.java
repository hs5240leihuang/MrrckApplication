package com.meiku.dev.ui.product;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.bean.FormFileBean;
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

//个人认证
public class PersonDentificationActivity extends BaseActivity implements
		OnClickListener {
	private EditText ed_product_name, ed_product_idnumber, ed_product_address,
			ed_product_phone;
	private Button btn_productcommit;
	private ImageView img_product_neiceng1, img_product_waiceng1,
			img_product_neiceng2, img_product_waiceng2, img_product_neiceng3,
			img_product_waiceng3;
	private String imgresault1, imgresault2, imgresault3;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_persondentification;
	}

	@Override
	public void initView() {
		img_product_neiceng1 = (ImageView) findViewById(R.id.img_product_neiceng1);
		img_product_waiceng1 = (ImageView) findViewById(R.id.img_product_waiceng1);
		img_product_neiceng2 = (ImageView) findViewById(R.id.img_product_neiceng2);
		img_product_waiceng2 = (ImageView) findViewById(R.id.img_product_waiceng2);
		img_product_neiceng3 = (ImageView) findViewById(R.id.img_product_neiceng3);
		img_product_waiceng3 = (ImageView) findViewById(R.id.img_product_waiceng3);
		ed_product_name = (EditText) findViewById(R.id.ed_product_name);
		ed_product_idnumber = (EditText) findViewById(R.id.ed_product_idnumber);
		ed_product_address = (EditText) findViewById(R.id.ed_product_address);
		ed_product_phone = (EditText) findViewById(R.id.ed_product_phone);
		btn_productcommit = (Button) findViewById(R.id.btn_productcommit);

	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
		img_product_waiceng1.setOnClickListener(this);
		img_product_waiceng2.setOnClickListener(this);
		img_product_waiceng3.setOnClickListener(this);
		btn_productcommit.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeFour:

			final CommonDialog commonDialog1 = new CommonDialog(
					PersonDentificationActivity.this, "提示",
					"恭喜您认证通过，可以进行产品发布了。", "确定");

			commonDialog1.setClicklistener(new ClickListenerInterface() {

				@Override
				public void doConfirm() {
					commonDialog1.dismiss();
					setResult(RESULT_OK);
					Intent intent = new Intent(
							PersonDentificationActivity.this,
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
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeFour:
			ToastUtil.showShortToast("失败");
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_product_waiceng1:
			if (img_product_waiceng1.getDrawable() != null) {
				dialog(img_product_waiceng1, img_product_neiceng1,
						R.drawable.new_add_picture);
				imgresault1="";
			} else {
				Intent intent1 = new Intent(PersonDentificationActivity.this,
						SelectPhotoStyleActivity.class);
				intent1.putExtra("flag", 1);
				startActivityForResult(intent1, reqCodeOne);
			}

			break;
		case R.id.img_product_waiceng2:
			if (img_product_waiceng2.getDrawable() != null) {
				dialog(img_product_waiceng2, img_product_neiceng2,
						R.drawable.new_add_picture);
				imgresault2="";
			} else {
				Intent intent2 = new Intent(PersonDentificationActivity.this,
						SelectPhotoStyleActivity.class);
				intent2.putExtra("flag", 2);
				startActivityForResult(intent2, reqCodeTwo);
			}

			break;
		case R.id.img_product_waiceng3:
			if (img_product_waiceng3.getDrawable() != null) {
				dialog(img_product_waiceng3, img_product_neiceng3,
						R.drawable.new_add_picture);
				imgresault3="";
			} else {
				Intent intent3 = new Intent(PersonDentificationActivity.this,
						SelectPhotoStyleActivity.class);
				intent3.putExtra("flag", 3);
				startActivityForResult(intent3, reqCodeThree);
			}

			break;

		case R.id.btn_productcommit:
			if (checkIsReady()) {
				getData();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				backPhoto(reqCodeOne, data);
				break;
			case reqCodeTwo:
				backPhoto(reqCodeTwo, data);
				break;
			case reqCodeThree:
				backPhoto(reqCodeThree, data);
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
			case reqCodeOne:
				BitmapUtils bitmapUtils1 = new BitmapUtils(
						PersonDentificationActivity.this);
				bitmapUtils1.display(img_product_neiceng1, result);
				dismissProgressDialog();
				img_product_waiceng1.setImageDrawable(ContextCompat
						.getDrawable(PersonDentificationActivity.this,
								R.drawable.yinshi_jianhao));
				imgresault1 = result;

				break;
			case reqCodeTwo:
				BitmapUtils bitmapUtils2 = new BitmapUtils(
						PersonDentificationActivity.this);
				bitmapUtils2.display(img_product_neiceng2, result);
				dismissProgressDialog();
				img_product_waiceng2.setImageDrawable(getResources()
						.getDrawable(R.drawable.yinshi_jianhao));
				imgresault2 = result;

				break;
			case reqCodeThree:
				BitmapUtils bitmapUtils3 = new BitmapUtils(
						PersonDentificationActivity.this);
				bitmapUtils3.display(img_product_neiceng3, result);
				dismissProgressDialog();
				img_product_waiceng3.setImageDrawable(getResources()
						.getDrawable(R.drawable.yinshi_jianhao));
				imgresault3 = result;

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

	// 图片返回
	public void backPhoto(int requestCode, Intent data1) {
		List<String> pictrue = data1
				.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
		if (!Tool.isEmpty(pictrue)) {// 多选图片返回
			for (int i = 0; i < pictrue.size(); i++) {
				CompressPic(requestCode, pictrue.get(i));
			}

		} else {// 拍照返回
			String photoPath = data1.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
			CompressPic(requestCode, photoPath);
		}
	}

	// 提交请求
	public void getData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("userName", ed_product_name.getText().toString());
		map.put("userIdNumber", ed_product_idnumber.getText().toString());
		map.put("address", ed_product_address.getText().toString());
		map.put("contactPhone", ed_product_phone.getText().toString());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PERSON_INFORMATION));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
		List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
		FormFileBean formFile = new FormFileBean(new File(imgresault1),
				"photo.png");
		formFileBeans.add(formFile);
		mapFileBean.put("userCardPhoto", formFileBeans);
		List<FormFileBean> formFileBeans1 = new ArrayList<FormFileBean>();
		FormFileBean formFile1 = new FormFileBean(new File(imgresault2),
				"photo.png");
		formFileBeans1.add(formFile1);
		mapFileBean.put("personalPhoto", formFileBeans1);
		List<FormFileBean> formFileBeans2 = new ArrayList<FormFileBean>();
		FormFileBean formFile2 = new FormFileBean(new File(imgresault3),
				"photo.png");
		formFileBeans2.add(formFile2);
		mapFileBean.put("nationalPhoto", formFileBeans2);
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		uploadFiles(reqCodeFour, AppConfig.PUBLIC_REQUEST_MAPPING_USER,
				mapFileBean, reqBase, true);
	}

	public void dialog(final ImageView drawaImageView,
			final ImageView drawaImageView1, final int img) {
		final CommonDialog commonDialog = new CommonDialog(
				PersonDentificationActivity.this, "提示", "是否删除该照片", "确认", "取消");
		commonDialog.show();
		commonDialog.setClicklistener(new ClickListenerInterface() {

			@Override
			public void doConfirm() {
				drawaImageView.setImageDrawable(null);
				drawaImageView1.setImageDrawable(ContextCompat.getDrawable(
						PersonDentificationActivity.this, img));
				commonDialog.dismiss();
			}

			@Override
			public void doCancel() {
				commonDialog.dismiss();
			}
		});
	}

	/**
	 * 检测是否具有提交的条件
	 * 
	 * @return
	 */
	private boolean checkIsReady() {
		if (Tool.isEmpty(ed_product_name.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写姓名");
			return false;
		}
		if (Tool.isEmpty(ed_product_idnumber.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写身份证号");
			return false;
		}
		if (Tool.isEmpty(ed_product_address.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写地址");
			return false;
		}
		if (Tool.isEmpty(ed_product_phone.getText().toString().trim())) {
			ToastUtil.showShortToast("请填写电话");
			return false;
		}
		if (Tool.isEmpty(img_product_waiceng1.getDrawable())) {
			ToastUtil.showShortToast("请上传手持身份证照片");
			return false;
		}
		if (Tool.isEmpty(img_product_waiceng2.getDrawable())) {
			ToastUtil.showShortToast("请上传个人信息所在面身份证照片");
			return false;
		}
		if (Tool.isEmpty(img_product_waiceng3.getDrawable())) {
			ToastUtil.showShortToast("请上传国徽图案面身份证照片");
			return false;
		}

		return true;
	}
}
