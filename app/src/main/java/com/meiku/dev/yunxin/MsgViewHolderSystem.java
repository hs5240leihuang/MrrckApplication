package com.meiku.dev.yunxin;

import android.content.Intent;
import android.text.TextUtils;

import com.meiku.dev.R;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.im.GroupNotifyActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.NewWorkDetailActivity;
import com.meiku.dev.ui.myshow.WorkDetailNewActivity;
import com.meiku.dev.ui.product.MyProductActivity;
import com.meiku.dev.ui.recruit.RecruitMainActivity;
import com.meiku.dev.utils.NetworkTools;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.utils.UpdateAppManager;

public class MsgViewHolderSystem extends MsgViewHolderText {

	@Override
	protected String getDisplayText() {
		SystemAttachment attachment = (SystemAttachment) message
				.getAttachment();
		return attachment.getMsg();
	}

	@Override
	protected void onItemClick() {
		SystemAttachment attachment = (SystemAttachment) message
				.getAttachment();
		if (attachment == null) {
			return;
		}
		switch (Integer.parseInt(attachment.getMsgType())) {

		case ConstantKey.SYSTEMMSG_TYPE_POSTDETAIL:// 101
													// (帖子收到评论)(帖子加精/置顶)到帖子详情页
			if (Tool.isEmpty(attachment.getSourceId())) {
				ToastUtil.showShortToast("资源ID有误");
				return;
			}
			context.startActivity(new Intent(context,
					PostDetailNewActivity.class).putExtra(
					ConstantKey.KEY_POSTID, attachment.getSourceId()));
			break;
		case ConstantKey.SYSTEMMSG_TYPE_SHOWWORK:// 102
													// (未参赛作品收到评论)到秀场详情页
		case ConstantKey.SYSTEMMSG_TYPE_GETZAN:// (作品点赞量>1000设为HOT)到秀场作品详情页
			if (Tool.isEmpty(attachment.getSourceId())) {
				ToastUtil.showShortToast("资源ID有误");
				return;
			}
			context.startActivity(new Intent(context,
					WorkDetailNewActivity.class).putExtra("SignupId",
					Integer.parseInt(attachment.getSourceId())));
			break;
		case ConstantKey.SYSTEMMSG_TYPE_PRODUCT:// (找产品意向申请)我的产品收到意向页面
			context.startActivity(new Intent(context, MyProductActivity.class)
					.putExtra("index", 1));
			break;
		// case ConstantKey.SYSTEMMSG_TYPE_POSITION://
		// (招聘面试邀请)到招聘岗位详情页
		case ConstantKey.SYSTEMMSG_TYPE_MSYQ:// (招聘面试邀请)到我的求职——面试邀请列表页
			if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
				ShowLoginDialogUtil.showTipToLoginDialog(context);
				return;
			}
			if (!TextUtils.isEmpty(AppContext.getInstance().getUserInfo()
					.getMyJobUrl())) {
				Intent i = new Intent(context, WebViewActivity.class);
				i.putExtra("webUrl", AppContext.getInstance().getUserInfo()
						.getMyJobUrl());
				context.startActivity(i);
			}
			break;
		case ConstantKey.SYSTEMMSG_TYPE_RESUMELIST:// (求职意向申请)到我的招聘——收到简历列表页
			context.startActivity(new Intent(context, RecruitMainActivity.class)
					.putExtra("index", 3));
			break;
		case ConstantKey.SYSTEMMSG_TYPE_GETPRIZE:// (比赛获奖)到秀场作品详情页
		case ConstantKey.SYSTEMMSG_TYPE_GETVOTE:// (有*人投票)到秀场作品详情页
			if (Tool.isEmpty(attachment.getSourceId())) {
				ToastUtil.showShortToast("资源ID有误");
				return;
			}
			context.startActivity(new Intent(context,
					NewWorkDetailActivity.class).putExtra("SignupId",
					Integer.parseInt(attachment.getSourceId())));
			break;
		case ConstantKey.SYSTEMMSG_TYPE_UPDATEVEISION:// (升级提示)到更新界面
			if (!NetworkTools.isNetworkAvailable(context)) {
				ToastUtil.showShortToast(context.getResources().getString(
						R.string.netNoUse));
				return;
			}
			UpdateAppManager manager = new UpdateAppManager(context);
			manager.setOnUpdateResultListener(new UpdateAppManager.OnUpdateResultListener() {
				@Override
				public void onNotUpdate() {
					ToastUtil.showShortToast("您已经是最新版本");
				}

				@Override
				public void onCancleClick() {
					// TODO Auto-generated method stub

				}
			});
			manager.checkUpdateInfo();
			break;
		case ConstantKey.SYSTEMMSG_TYPE_YANZHNEG://群验证 通知
			context.startActivity(new Intent(context, GroupNotifyActivity.class));
			break;
		case ConstantKey.SYSTEMMSG_TYPE_H5:// 跳转H5
			Intent intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("webUrl", attachment.getH5Url());
			context.startActivity(intent);
			break;
		}
	}

}
