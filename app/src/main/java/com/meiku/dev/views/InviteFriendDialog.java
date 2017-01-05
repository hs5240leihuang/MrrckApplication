package com.meiku.dev.views;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.ShareMessage;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.chat.ChooseGroupActivity;
import com.meiku.dev.ui.im.AddressListSearchActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.UMQQSsoHandler;
import com.umeng.socialize.handler.UMWXHandler;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.WeiXinShareContent;
import com.umeng.socialize.utils.Log;

//import com.umeng.socialize.sso.SinaSsoHandler;

/**
 * 邀请朋友
 * 
 */
public class InviteFriendDialog extends Dialog {

	private GridView gridview;
	private Context context;
	private List<ItemCell> showList = new ArrayList<ItemCell>();
	private CommonAdapter<ItemCell> showAdapter;
	private String filePath;
	private String shareContent;
	private String shareImage;
	private String shareTitle;
	private String key;
	protected int shareType;

	/**
	 * 
	 * @param context
	 * @param filePath
	 *            分享Url
	 * @param shareTitle
	 *            分享标题
	 * @param shareContent
	 *            分享内容
	 * @param shareImage
	 *            分享图片
	 * @param key
	 *            参数
	 * @param shareType
	 *            分享类型
	 */
	public InviteFriendDialog(Context context, String filePath,
			String shareTitle, String shareContent, String shareImage,
			String key, int shareType) {
		super(context, R.style.DialogStyleBottom);
		this.context = context;
		this.filePath = filePath;
		this.shareTitle = shareTitle;
		this.shareContent = shareContent;
		this.shareImage = shareImage;
		this.key = key;
		this.shareType = shareType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_invitefriend);
		// initSocialSDK();
		Window window = this.getWindow();
		// 此处可以设置dialog显示的位置
		window.setGravity(Gravity.BOTTOM);
		// 占满屏幕
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		initview();
	}

	private void initview() {
		findViewById(R.id.cancle).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dismiss();
					}
				});
		setData();
		initGrid();
	}

	private void initGrid() {
		gridview = (GridView) findViewById(R.id.gridview);
		showAdapter = new CommonAdapter<ItemCell>(context,
				R.layout.item_share_menu, showList) {

			@Override
			public void convert(ViewHolder viewHolder, ItemCell t) {
				viewHolder.setImage(R.id.img_bg, R.drawable.whiteround);
				viewHolder.setText(R.id.id_txt, t.getStr());
				viewHolder.setImage(R.id.id_img, t.getRes());

			}

		};
		gridview.setAdapter(showAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (showList.get(arg2).getIndex()) {
				case 0:
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShareMessage shareGroup = new ShareMessage(shareTitle,
								shareContent, shareImage, filePath, key,
								ConstantKey.SEARCHPAGE_OPENTYPE_LOCAL,
								shareType);
						context.startActivity(new Intent(context,
								ChooseGroupActivity.class).putExtra(
								ConstantKey.KEY_SHARE_KEY,
								JsonUtil.objToJson(shareGroup)));
					} else {
						ShowLoginDialogUtil.showTipToLoginDialog(context);
					}

					break;
				case 1:
					if (AppContext.getInstance().isLoginedAndInfoPerfect()) {
						ShareMessage sm = new ShareMessage(shareTitle,
								shareContent, shareImage, filePath, key,
								ConstantKey.SEARCHPAGE_OPENTYPE_LOCAL,
								shareType);
						context.startActivity(new Intent(context,
								AddressListSearchActivity.class)
								.putExtra("useType",
										ConstantKey.SEARCHPAGE_UESTYPE_SHARE)
								.putExtra(ConstantKey.KEY_SHARE_KEY,
										JsonUtil.objToJson(sm)));
					} else {
						ShowLoginDialogUtil.showTipToLoginDialog(context);
					}
					break;
				case 2:
					ClipboardManager cm = (ClipboardManager) context
							.getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本数据复制到剪贴板
					cm.setText(filePath);
					ToastUtil.showShortToast("已复制到剪贴板");
					break;
				case 3:
					new ShareAction((Activity) context)
							.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
							.setCallback(umShareListener)
							.withText("this is description")
							.withText(shareContent).withTitle(shareTitle)
							.withMedia(new UMImage(context, shareImage))
							.withTargetUrl(filePath).share();
					break;
				case 4:
					new ShareAction((Activity) context)
							.setPlatform(SHARE_MEDIA.WEIXIN)
							.setCallback(umShareListener)
							.withText(shareContent).withTitle(shareTitle)
							.withMedia(new UMImage(context, shareImage))
							.withTargetUrl(filePath).share();
					break;
				case 5:
					new ShareAction((Activity) context)
							.setPlatform(SHARE_MEDIA.QQ)
							.setCallback(umShareListener)
							.withText(shareContent).withTitle(shareTitle)
							.withMedia(new UMImage(context, shareImage))
							.withTargetUrl(filePath).share();
					break;
				default:
					break;
				}
				dismiss();
			}
		});
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.d("plat", "platform" + platform);
			if (platform.name().equals("WEIXIN_FAVORITE")) {
				Toast.makeText(context, "收藏成功啦", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "分享成功啦", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(context, "分享失败啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(context, "分享取消了", Toast.LENGTH_SHORT).show();
		}
	};

	private void setData() {
		showList.add(new ItemCell(0, "分享到美库群", R.drawable.contact_group_icon));
		showList.add(new ItemCell(1, "分享到美库好友", R.drawable.shequselect));
		showList.add(new ItemCell(2, "复制链接", R.drawable.lianjie));
		showList.add(new ItemCell(3, "分享到微信朋友圈", R.drawable.pengyouquan));
		showList.add(new ItemCell(4, "分享到微信好友", R.drawable.wechat_share));
		showList.add(new ItemCell(5, "分享到QQ好友", R.drawable.qqtouxiang));
	}

	public class ItemCell {
		private String str;
		private int res;
		private int index;

		public ItemCell(int index, String str, int res) {
			this.str = str;
			this.res = res;
			this.index = index;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

		public int getRes() {
			return res;
		}

		public void setRes(int res) {
			this.res = res;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

	}

}
