package com.meiku.dev.ui.chat;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.meiku.dev.R;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.QRcodeUtil;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyRoundDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;

public class GroupQRCodeActivity extends BaseActivity implements
		OnClickListener {

	private ImageView iv_more, iv_qrcode;
	private TextView tv_groupname;
	private final String meiku_alter = "群名片";
	/** 二维码bitmap */
	private Bitmap bitMap = null;
	private String clientGroupPhoto;// 群头像
	private String groupName;// 群名称
	private String groupId;//
	private MySimpleDraweeView iv_grouphead;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_group_qrcode;
	}

	@Override
	public void initView() {
		iv_more = (ImageView) findViewById(R.id.right_res_title);
		LinearLayout layout_addImage = (LinearLayout) findViewById(R.id.layout_addImage);
		iv_grouphead = new MySimpleDraweeView(GroupQRCodeActivity.this);
		layout_addImage.removeAllViews();
		layout_addImage.addView(iv_grouphead, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
		tv_groupname = (TextView) findViewById(R.id.tv_groupname);
	}

	@Override
	public void initValue() {
		clientGroupPhoto = getIntent().getStringExtra("clientGroupPhoto");
		groupName = getIntent().getStringExtra("groupName");
		groupId = getIntent().getStringExtra("groupId");
		iv_grouphead.setUrlOfImage(clientGroupPhoto);
		tv_groupname.setText(groupName);
		try {
			bitMap = (QRcodeUtil.createQRCode(ConstantKey.QR_CODE_GROUP
					+ groupId, ScreenUtil.dpToPx(getResources(), 200)));
			iv_qrcode.setImageBitmap(bitMap);
		} catch (WriterException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void bindListener() {
		iv_more.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_res_title:
			new InviteFriendDialog(GroupQRCodeActivity.this,
					AppConfig.SHARE_DAPP_URL, meiku_alter, groupName,
					clientGroupPhoto, groupId + "",
					ConstantKey.ShareStatus_GROUP_CARD_STATE).show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitMap != null && !bitMap.isRecycled()) {
			bitMap.recycle();
			bitMap = null;
		}
		System.gc();
	}
}
