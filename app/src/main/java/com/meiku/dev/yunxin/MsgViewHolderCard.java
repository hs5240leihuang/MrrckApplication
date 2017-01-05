package com.meiku.dev.yunxin;

import android.content.Intent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.views.MyRoundDraweeView;

public class MsgViewHolderCard extends MsgViewHolderBase {

	private CardAttachment attch;

	@Override
	protected int getContentResId() {
		return R.layout.chat_item_card;
	}

	@Override
	protected void inflateContentView() {

	}

	@Override
	protected void bindContentView() {
		// 名片
		TextView card_name = (TextView) view.findViewById(R.id.card_name);
		TextView card_Id = (TextView) view.findViewById(R.id.card_Id);
		LinearLayout show = (LinearLayout) view.findViewById(R.id.card_headImg);
		attch = (CardAttachment) message.getAttachment();
		if (attch != null) {
			card_name.setText(attch.getCard_name());
			card_Id.setText("ID：" + attch.getCard_id());
			show.removeAllViews();
			MyRoundDraweeView img_head = new MyRoundDraweeView(context);
			show.addView(img_head, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			img_head.setUrlOfImage(attch.getCard_headImg());
		}
	}

	@Override
	protected void onItemClick() {
		if (attch != null) {
			Intent intent = new Intent(context, PersonShowActivity.class);
			intent.putExtra(PersonShowActivity.TO_USERID_KEY,
					attch.getCard_id());
			intent.putExtra("nickName", attch.getCard_name());
			context.startActivity(intent);
		}
		super.onItemClick();
	}

}
