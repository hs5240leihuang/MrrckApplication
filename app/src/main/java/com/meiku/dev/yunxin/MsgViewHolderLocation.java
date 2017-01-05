package com.meiku.dev.yunxin;

import android.content.Intent;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.service.GetLoacationActivity;
import com.meiku.dev.utils.ScreenUtil;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderLocation extends MsgViewHolderBase {

	public MsgThumbImageView mapView;
	public TextView addressText;

	@Override
	protected int getContentResId() {
		return R.layout.nim_message_item_location;
	}

	@Override
	protected void inflateContentView() {
		mapView = (MsgThumbImageView) view
				.findViewById(R.id.message_item_location_image);
		addressText = (TextView) view
				.findViewById(R.id.message_item_location_address);
	}

	@Override
	protected void bindContentView() {
		final LocationAttachment location = (LocationAttachment) message
				.getAttachment();
		addressText.setText(location.getAddress());

		int[] bound = ImageUtil.getBoundWithLength(getLocationDefEdge(),
				R.drawable.nim_location_bk, true);
		int width = bound[0];
		int height = bound[1];

		setLayoutParams(width, height, mapView);
		setLayoutParams(width, (int) (0.38 * height), addressText);

		mapView.loadAsResource(R.drawable.nim_location_bk,
				R.drawable.nim_message_item_round_bg);
	}

	@Override
	protected void onItemClick() {
		Intent intentLocation = new Intent(context, GetLoacationActivity.class);
		LocationAttachment location = (LocationAttachment) message
				.getAttachment();
		intentLocation
				.putExtra(ConstantKey.KEY_LAITUDE, location.getLatitude()+"");
		intentLocation.putExtra(ConstantKey.KEY_LONGITUDE,
				location.getLongitude()+"");
		context.startActivity(intentLocation);
	}

	public static int getLocationDefEdge() {
		return (int) (0.5 * ScreenUtil.SCREEN_WIDTH);
	}
}
