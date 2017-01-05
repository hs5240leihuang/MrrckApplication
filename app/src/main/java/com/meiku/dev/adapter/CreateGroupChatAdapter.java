package com.meiku.dev.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.CreateGroupChatUser;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class CreateGroupChatAdapter extends BaseAdapter {

	private Context mContext;
	private List<CreateGroupChatUser> sortUserList = new ArrayList<CreateGroupChatUser>();

	public CreateGroupChatAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if (Tool.isEmpty(sortUserList)) {
			return 0;
		} else {
			return sortUserList.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView,
				parent, R.layout.item_create_groupchat, position);
		viewHolder.setText(R.id.tv_name, sortUserList.get(position)
				.getMkNickname());
		// viewHolder.setImage(R.id.iv_headimage, t.getClientHeadPicUrl());
		LogUtil.d("hl", "getView=" + sortUserList.get(position).getMkNickname());
		TextView alpha = viewHolder.getView(R.id.alpha);

		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			alpha.setVisibility(View.VISIBLE);
			alpha.setText(sortUserList.get(position).getSortLetters());
		} else {
			alpha.setVisibility(View.GONE);
		}
		if(sortUserList.get(position).getIsAddGroup()){
			viewHolder.setImage(R.id.iv_choosestatus,R.drawable.weinengxuanzhong);
		}else {
			if(sortUserList.get(position).getIsCheck()){
				viewHolder.setImage(R.id.iv_choosestatus,R.drawable.icon_xuanzhong_circle);
			}else{
				viewHolder.setImage(R.id.iv_choosestatus,R.drawable.icon_weixuanzhong_circle);
			}
		}
		
		viewHolder.setImage(R.id.iv_headimage, sortUserList.get(position).getMkHeadurl());
		return viewHolder.getConvertView();
	}

	public int getSectionForPosition(int position) {
		if (position > sortUserList.size())
			return 0;
		return sortUserList.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = sortUserList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	public void setData(List<CreateGroupChatUser> sortUserList) {
		this.sortUserList = sortUserList;
		LogUtil.d("hl", "sortUserList=" + sortUserList.size());
		notifyDataSetChanged();
	}

}
