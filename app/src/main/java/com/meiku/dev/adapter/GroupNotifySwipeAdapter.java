package com.meiku.dev.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.meiku.dev.R;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

public class GroupNotifySwipeAdapter extends BaseAdapter {
	/**
	 * 上下文对象
	 */
	private Context mContext = null;
	private List<GroupUserEntity> data;

	private int mRightWidth = 0;

	/**
	 * @param mainActivity
	 */
	public GroupNotifySwipeAdapter(Context ctx, List<GroupUserEntity> data,
			int rightWidth) {
		mContext = ctx;
		this.data = data;
		mRightWidth = rightWidth;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView,
				parent, R.layout.item_group_notify, position);
		RelativeLayout item_left = (RelativeLayout) viewHolder
				.getView(R.id.item_left);
		RelativeLayout item_right = (RelativeLayout) viewHolder
				.getView(R.id.item_right);
		LinearLayout.LayoutParams lp1 = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		item_left.setLayoutParams(lp1);
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
				LayoutParams.MATCH_PARENT);
		item_right.setLayoutParams(lp2);
		item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightItemClick(v, position);
				}
			}
		});
		LinearLayout layout_addImage = (LinearLayout) viewHolder
				.getView(R.id.layout_icon);
		layout_addImage.removeAllViews();
		MyRectDraweeView iv_head = new MyRectDraweeView(mContext);
		layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv_head.setUrlOfImage(data.get(position).getClientThumbHeadPicUrl());

		viewHolder.setText(R.id.tv_title, data.get(position).getNickName());
		viewHolder.setText(R.id.tv_name, "申请加入"
				+ data.get(position).getGroupName());
		viewHolder.setText(R.id.tv_msg, data.get(position).getRemark());
		Button btn_agreee = viewHolder.getView(R.id.btn_agreee);
		if (data.get(position).getApproveStatus() == 1) {
			btn_agreee.setBackground(null);
			btn_agreee.setText("已同意");
			btn_agreee.setEnabled(false);
			btn_agreee.setTextColor(Color.parseColor("#AAAAAA"));
		} else if (data.get(position).getApproveStatus() == 2) {
			btn_agreee.setBackground(null);
			btn_agreee.setText("已拒绝");
			btn_agreee.setEnabled(false);
			btn_agreee.setTextColor(Color.parseColor("#AAAAAA"));
		} else if (data.get(position).getApproveStatus() == 0) {
			btn_agreee.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_btn_press));
			btn_agreee.setText("同意");
			btn_agreee.setEnabled(true);
			btn_agreee.setTextColor(Color.parseColor("#FFFFFF"));
		}
		btn_agreee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightBtnClick(v, position);
				}
			}
		});
		viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onLeftItemClick(v, position);
				}
			}
		});
		return viewHolder.getConvertView();
	}

	/**
	 * 单击事件监听器
	 */
	private onRightItemClickListener mListener = null;

	public void setOnRightItemClickListener(onRightItemClickListener listener) {
		mListener = listener;
	}

	public interface onRightItemClickListener {
		void onLeftItemClick(View v, int position);

		void onRightItemClick(View v, int position);

		void onRightBtnClick(View v, int position);
	}
}
