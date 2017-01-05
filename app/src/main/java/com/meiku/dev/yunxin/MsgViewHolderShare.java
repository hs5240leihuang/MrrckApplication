package com.meiku.dev.yunxin;

import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.AboutMrrckActivity;
import com.meiku.dev.R;
import com.meiku.dev.bean.ShareMessage;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.community.PostDetailNewActivity;
import com.meiku.dev.ui.decoration.CaseDetailActivity;
import com.meiku.dev.ui.decoration.DecCompanyDetailActivity;
import com.meiku.dev.ui.encyclopaedia.EntriesDetailActivity;
import com.meiku.dev.ui.findjob.CompanyInfoActivity;
import com.meiku.dev.ui.im.GroupInfoActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.ui.morefun.WebViewActivity;
import com.meiku.dev.ui.myshow.NewWorkDetailActivity;
import com.meiku.dev.ui.myshow.ShowMainActivity;
import com.meiku.dev.ui.myshow.WorkDetailNewActivity;
import com.meiku.dev.ui.plan.PlotDetailActivity;
import com.meiku.dev.ui.plan.PlotterDetailActivity;
import com.meiku.dev.ui.product.ProductDetailActivity;
import com.meiku.dev.ui.recruit.ResumeNoHfive;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MySimpleDraweeView;

public class MsgViewHolderShare extends MsgViewHolderBase {

	private ShareMessage shareInfo;

	@Override
	protected int getContentResId() {
		return R.layout.chat_item_share;
	}

	@Override
	protected void inflateContentView() {
	}

	@Override
	protected void onItemClick() {
		if (!Tool.isEmpty(shareInfo)) {
			if (ConstantKey.SEARCHPAGE_OPENTYPE_LOCAL == shareInfo
					.getOpenType()) {// 本地打开
				switch (shareInfo.getShareType()) {
				case ConstantKey.ShareStatus_SAISHI:
					context.startActivity(new Intent(context,
							ShowMainActivity.class).putExtra("postsId",
							Integer.parseInt(shareInfo.getKey())));
					break;
				case ConstantKey.ShareStatus_SHOWWORK:
					context.startActivity(new Intent(context,
							NewWorkDetailActivity.class).putExtra("SignupId",
							Integer.parseInt(shareInfo.getKey())));
					break;
				case ConstantKey.ShareStatus_CRESUMEDETAIL:
					context.startActivity(new Intent(context,
							ResumeNoHfive.class).putExtra("resumeId",
							Integer.parseInt(shareInfo.getKey())).putExtra(
							"webUrl", shareInfo.getShareUrl()));
					break;
				case ConstantKey.ShareStatus_NOSHOWWORK:
					context.startActivity(new Intent(context,
							WorkDetailNewActivity.class).putExtra("SignupId",
							Integer.parseInt(shareInfo.getKey())));
					break;
				case ConstantKey.ShareStatus_PRODUCT:
					context.startActivity(new Intent(context,
							ProductDetailActivity.class).putExtra("productId",
							shareInfo.getKey()));
					break;
				case ConstantKey.ShareStatus_CARD_STATE:
					context.startActivity(new Intent(context,
							PersonShowActivity.class).putExtra(
							PersonShowActivity.TO_USERID_KEY,
							shareInfo.getKey()));
					break;
				case ConstantKey.ShareStatus_GROUP_CARD_STATE:
					context.startActivity(new Intent(context,
							GroupInfoActivity.class).putExtra(
							ConstantKey.KEY_IM_MULTI_CHATROOMID,
							shareInfo.getKey()));
					break;
				case ConstantKey.ShareStatus_JOBPOSITION:
					// context.startActivity(new Intent(context,
					// CompanyInfoActivity.class).putExtra(
					// "url", shareInfo.getKey()));
					context.startActivity(new Intent(context,
							CompanyInfoActivity.class).putExtra(
							"companyId_fromShare", shareInfo.getKey())
							.putExtra("sharUrl_fromShare",
									shareInfo.getShareUrl()));
					break;
				case ConstantKey.ShareStatus_COMPANY:
					// context.startActivity(new Intent(context,
					// CompanyInfoActivity.class).putExtra(
					// "url", shareInfo.getKey()));
					context.startActivity(new Intent(context,
							CompanyInfoActivity.class).putExtra(
							"companyId_fromShare", shareInfo.getKey())
							.putExtra("sharUrl_fromShare",
									shareInfo.getShareUrl()));
					break;
				case ConstantKey.ShareStatus_NEWS_STATE:
					context.startActivity(new Intent(context,
							PostDetailNewActivity.class).putExtra(
							ConstantKey.KEY_POSTID, shareInfo.getKey()));
					break;
				case ConstantKey.ShareStatus_CITIAO:
					context.startActivity(new Intent(context,
							EntriesDetailActivity.class).putExtra("jsonStr",
							shareInfo.getKey()).putExtra("flag", 1));
					break;
				case ConstantKey.ShareStatus_MRRCK:
					context.startActivity(new Intent(context,
							AboutMrrckActivity.class));
					break;
				case ConstantKey.ShareStatus_DECCASE:
					try {
						String jsonStr = shareInfo.getKey();
						Map<String, String> map = JsonUtil.jsonToMap(jsonStr);
						context.startActivity(new Intent(context,
								CaseDetailActivity.class)
								.putExtra("shareTitle", map.get("title"))
								.putExtra("shareContent", map.get("content"))
								.putExtra("shareImg", map.get("image"))
								.putExtra("shareUrl", map.get("shareUrl"))
								.putExtra("userId",
										Integer.parseInt(map.get("userId")))
								.putExtra("sourceId",
										Integer.parseInt(map.get("sourceId")))
								.putExtra("loadUrl", map.get("loadUrl")));
					} catch (Exception e) {
						ToastUtil.showShortToast("参数有误");
					}
					break;
				case ConstantKey.ShareStatus_COMPANYDETAIL:
					try {
						String jsonStr = shareInfo.getKey();
						Map<String, String> map = JsonUtil.jsonToMap(jsonStr);
						context.startActivity(new Intent(context,
								DecCompanyDetailActivity.class)
								.putExtra("shareTitle", map.get("title"))
								.putExtra("shareContent", map.get("content"))
								.putExtra("shareImg", map.get("image"))
								.putExtra("shareUrl", map.get("shareUrl"))
								.putExtra("userId",
										Integer.parseInt(map.get("userId")))
								.putExtra("sourceId",
										Integer.parseInt(map.get("sourceId")))
								.putExtra("loadUrl", map.get("loadUrl")));
					} catch (Exception e) {
						ToastUtil.showShortToast("参数有误");
					}
					break;
				case ConstantKey.ShareStatus_PLOTDETAIL:
					try {
						String jsonStr = shareInfo.getKey();
						Map<String, String> map = JsonUtil.jsonToMap(jsonStr);
						context.startActivity(new Intent(context,
								PlotDetailActivity.class)
								.putExtra("shareTitle", map.get("title"))
								.putExtra("shareContent", map.get("content"))
								.putExtra("shareImg", map.get("image"))
								.putExtra("shareUrl", map.get("shareUrl"))
								.putExtra("userId",
										Integer.parseInt(map.get("userId")))
								.putExtra("loadUrl", map.get("loadUrl")));
					} catch (Exception e) {
						ToastUtil.showShortToast("参数有误");
					}
					break;
				case ConstantKey.ShareStatus_PLOTPERSONDETAIL:
					try {
						String jsonStr = shareInfo.getKey();
						Map<String, String> map = JsonUtil.jsonToMap(jsonStr);
						context.startActivity(new Intent(context,
								PlotterDetailActivity.class)
								.putExtra("shareTitle", map.get("title"))
								.putExtra("shareContent", map.get("content"))
								.putExtra("shareImg", map.get("image"))
								.putExtra("shareUrl", map.get("shareUrl"))
								.putExtra("userId",
										Integer.parseInt(map.get("userId")))
								.putExtra("loadUrl", map.get("loadUrl")));
					} catch (Exception e) {
						ToastUtil.showShortToast("参数有误");
					}
					break;
				default:
					break;
				}
			} else {// 网页打开
				Intent intent = new Intent(context, WebViewActivity.class);
				intent.putExtra("webUrl", shareInfo.getShareUrl());
				context.startActivity(intent);
			}
		}

		super.onItemClick();
	}

	@Override
	protected void bindContentView() {
		// 分享
		TextView tv_shareTitle = (TextView) view.findViewById(R.id.tv_title);
		LinearLayout layout_add = (LinearLayout) view
				.findViewById(R.id.layout_add);
		MySimpleDraweeView iv_shareImg = new MySimpleDraweeView(context);
		if (layout_add != null) {
			layout_add.removeAllViews();
			layout_add.addView(iv_shareImg, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		TextView tv_shareContent = (TextView) view
				.findViewById(R.id.tv_content);
		ShareAttachment attch = (ShareAttachment) message.getAttachment();
		if (!Tool.isEmpty(attch.getShareJson())) {
			shareInfo = (ShareMessage) JsonUtil.jsonToObj(ShareMessage.class,
					attch.getShareJson());
			if (!Tool.isEmpty(shareInfo)) {
				tv_shareTitle.setText(shareInfo.getShareTitle());
				tv_shareContent.setText(shareInfo.getShareContent());
				iv_shareImg.setUrlOfImage(shareInfo.getShareImage());
			}
		}

	}

}
