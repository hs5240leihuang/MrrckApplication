package com.meiku.dev.views;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.meiku.dev.R;

public class MySimpleDraweeView extends SimpleDraweeView {

	public MySimpleDraweeView(Context context) {
		super(context);
		GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(
				getResources());
		builder.setPlaceholderImage(
				ContextCompat.getDrawable(context, R.drawable.gg_icon),
				ScalingUtils.ScaleType.CENTER_CROP);
		builder.setFailureImage(
				ContextCompat.getDrawable(context, R.drawable.gg_icon),
				ScalingUtils.ScaleType.CENTER_CROP);
		setHierarchy(builder.build());
	}

	public void setUrlOfImage(String uriString) {
		Uri uri = (uriString != null) ? Uri.parse(uriString) : null;
		setImageURI(uri);
	}
}
