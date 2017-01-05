package com.meiku.dev.ui.mine;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.WriterException;
import com.meiku.dev.R;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.QRcodeUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.InviteFriendDialog;
import com.meiku.dev.views.MyRectDraweeView;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck
 * 
 * 类描述：个人分享信息activity 类名称：com.meiku.dev.ui.mine.PersonShareInfoActivity 创建人：曙光
 * 创建时间：2015-11-3 下午3:28:30
 * 
 * @version V3.0
 */
public class PersonShareInfoActivity extends BaseActivity implements
		View.OnClickListener {

	/** 个人信息头像img */
	private LinearLayout lin_personImgId;
	/** 个人信息的名称(昵称) */
	private TextView personName;
	/** 分享的二维码图片 */
	private ImageView shareImg;

	/** 用户实体 */
	// private MkUser mkUser;
	/** 岗位 */
	private TextView personPost;
	/** 分享按钮 */
	private ImageView shareButton;

	private final String meiku_alter = "个人名片";
	/** 二维码bitmap */
	private Bitmap bitMap = null;
	private String nickname;
	private MkUser mkUser;
	private String shareUrl;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_person_qrcode_info;
	}

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        MobclickAgent.onPause(this);
    }
    
	@Override
	public void initView() {
		lin_personImgId = (LinearLayout) super
				.findViewById(R.id.lin_personImgId);
		personName = (TextView) super.findViewById(R.id.personNameId);
		shareImg = (ImageView) super.findViewById(R.id.shareImgId);
		personPost = (TextView) super.findViewById(R.id.personPostId);
		shareButton = (ImageView) super.findViewById(R.id.right_res_title);
		shareButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_res_title:// 分享
			if (Tool.isEmpty(shareUrl)) {
				ToastUtil.showShortToast("获取分享路径失败！");
				return;
			}
			new InviteFriendDialog(PersonShareInfoActivity.this, shareUrl,
					nickname, nickname, mkUser.getClientThumbHeadPicUrl(),
					AppContext.getInstance().getUserInfo().getUserId() + "",
					ConstantKey.ShareStatus_CARD_STATE).show();
			break;
		default:
			break;
		}

	}

	@Override
	public void initValue() {
		/** 获取数据 加载数据 */
		getShareInfo();
		// 获取分享路径
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PEESON_SHARE));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PERSONAL_REQUEST_MAPPING, req, true);
	}

	public void getShareInfo() {
		mkUser = AppContext.getInstance().getUserInfo();
		if (null != mkUser && !"".equals(mkUser)) {
			MyRectDraweeView personImg = new MyRectDraweeView(
					PersonShareInfoActivity.this);
			lin_personImgId.removeAllViews();
			lin_personImgId.addView(personImg, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			personImg.setUrlOfImage(mkUser.getClientThumbHeadPicUrl());
			nickname = mkUser.getNickName();
			personName.setText(nickname);
			personPost.setText(mkUser.getPositionName());
			// 生成二维码
			String qrcode = mkUser.getqRCode();
			if (null != qrcode && !"".equals(qrcode)) {
				try {
					// String code =
					// ConstantKey.QR_CODE_USER+String.valueOf(mkUser.getId());
					// 加载二维码
					bitMap = QRcodeUtil.createQRCode(mkUser.getqRCode(), 600);
					shareImg.setImageBitmap(bitMap);
				} catch (WriterException e) {
					ToastUtil.showShortToast("加载失败");
				}
			}
		} else {
			ToastUtil.showShortToast("加载失败");
		}

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "个人名片分享=" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("homePageShareUrl"))
					&& (resp.getBody().get("homePageShareUrl") + "").length() > 4) {
				shareUrl = resp.getBody().get("homePageShareUrl").getAsString();
			}
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

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	@Override
	public void bindListener() {
	}

}
