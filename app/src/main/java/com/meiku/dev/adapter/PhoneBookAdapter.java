package com.meiku.dev.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.PhoneNums;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class PhoneBookAdapter extends BaseAdapter {

	private Context mContext;
	private List<PhoneNums> sortUserList = new ArrayList<PhoneNums>();

	public PhoneBookAdapter(Context mContext) {
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
				parent, R.layout.item_phonebook, position);
		viewHolder.setText(R.id.tv_name, sortUserList.get(position)
				.getUser_name());
		// viewHolder.setImage(R.id.iv_headimage, t.getClientHeadPicUrl());
		LogUtil.d("hl", "getView=" + sortUserList.get(position).getNickName());
		TextView alpha = viewHolder.getView(R.id.alpha);

		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			alpha.setVisibility(View.VISIBLE);
			alpha.setText(sortUserList.get(position).getInitial().substring(0,1));
		} else {
			alpha.setVisibility(View.GONE);
		}
		Button btn_add = viewHolder.getView(R.id.btn_add);
		//0陌生人 1好友 2粉丝
		if(sortUserList.get(position).getIsFriend().equals("0")){
			btn_add.setTextColor(Color.BLACK);
			btn_add.setClickable(false);
		}else{
			btn_add.setTextColor(mContext.getResources().getColor(R.color.light_gray));
			btn_add.setClickable(false);
		}
		
		viewHolder.setImage(R.id.iv_headimage,sortUserList.get(position).getHeadPicUrl());

		return viewHolder.getConvertView();
	}

	public int getSectionForPosition(int position) {
		if (position > sortUserList.size())
			return 0;
		return sortUserList.get(position).getInitial().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = sortUserList.get(i).getInitial();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	public void setData(List<PhoneNums> sortUserList) {
		this.sortUserList = sortUserList;
		LogUtil.d("hl", "sortUserList=" + sortUserList.size());
		notifyDataSetChanged();
	}
	
	public List<PhoneNums> getSortUserList() {
		return sortUserList;
	}
}
