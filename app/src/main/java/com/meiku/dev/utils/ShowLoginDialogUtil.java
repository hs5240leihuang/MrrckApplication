package com.meiku.dev.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.meiku.dev.ui.login.MkLoginActivity;
import com.meiku.dev.views.CommonDialog;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck
 * 
 * 类描述： 提示框util 类名称：com.meiku.dev.utils.ShowLoginDialogUtil 创建人：admin
 * 创建时间：2015-10-30 上午10:46:23
 * 
 * @version V3.0
 */
public class ShowLoginDialogUtil {
	// 提示登录对话框
	public static void showTipToLoginDialog(final Context mContext) {
		// final CommonDialog commonDialog = new CommonDialog(mContext, "提示",
		// "请完善资料，马上互动呦~", "完善资料", "取消");
		// commonDialog.show();
		// commonDialog
		// .setClicklistener(new CommonDialog.ClickListenerInterface() {
		// @Override
		// public void doConfirm() {
		// // Intent intent = new Intent(mContext,
		// // MkLoginActivity.class);
		// mContext.startActivity(new Intent(mContext,
		// PerfectMyInfoActivity.class));
		// commonDialog.dismiss();
		// }
		//
		// @Override
		// public void doCancel() {
		// commonDialog.dismiss();
		// }
		// });
		mContext.startActivity(new Intent(mContext, MkLoginActivity.class));
	}

	// 提示邀请好友对话框
	public static void showTipToInviteDialog(final Context mContext,
			final String number) {
		final CommonDialog commonDialog = new CommonDialog(mContext, "提示",
				"该用户尚未注册美库", "立即邀请", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						Uri smsToUri = Uri.parse("smsto://" + number);
						Intent mIntent = new Intent(
								android.content.Intent.ACTION_SENDTO, smsToUri);
						mContext.startActivity(mIntent);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
	}

	// 提示余额不足对话框
	public static void showMoneyDialog(final Context mContext) {
		final CommonDialog commonDialog = new CommonDialog(mContext, "提示",
				"您的美库账户余额不足，请联系客服电话进行充值，联系电话4006886800", "打电话", "取消");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						Uri smsToUri = Uri.parse("tel:" + "4006886800");
						Intent mIntent = new Intent(
								android.content.Intent.ACTION_DIAL, smsToUri);
						((Activity) mContext).startActivity(mIntent);
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
	}

	// 提示自定义文字
	public static void showTipDialog(final Context mContext,
			final String content) {
		final CommonDialog commonDialog = new CommonDialog(mContext, "提示",
				content, "确定");
		commonDialog.show();
		commonDialog
				.setClicklistener(new CommonDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
					}
				});
	}
}
