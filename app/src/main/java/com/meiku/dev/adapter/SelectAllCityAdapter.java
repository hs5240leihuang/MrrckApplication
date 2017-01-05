package com.meiku.dev.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

public class SelectAllCityAdapter extends BaseAdapter {

	private Context mContext;
	private List<AreaEntity> sortList = new ArrayList<AreaEntity>();

	public SelectAllCityAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if (Tool.isEmpty(sortList)) {
			return 0;
		} else {
			return sortList.size();
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
				parent, R.layout.item_selectallcity, position);
		viewHolder.setText(R.id.tv_name, sortList.get(position)
				.getCityName());
		LogUtil.d("hl", "getView=" + sortList.get(position).getCityName());
		TextView alpha = viewHolder.getView(R.id.alpha);

		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			alpha.setVisibility(View.VISIBLE);
			alpha.setText(PinyinUtil.getTerm(sortList.get(position).getCityName()));
		} else {
			alpha.setVisibility(View.GONE);
		}
		
		viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("cityCode", sortList.get(position).getCityCode() + "");
				intent.putExtra("cityName", sortList.get(position).getCityName());
				intent.putExtra("provinceCode", sortList.get(position).getParentCode()+"");
				((Activity) mContext).setResult(-1, intent);
				((Activity) mContext).finish();
			}
		});
		return viewHolder.getConvertView();
	}

	public int getSectionForPosition(int position) {
		if (position > sortList.size())
			return 0;
		return PinyinUtil.getTerm(sortList.get(position).getCityName()).charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = PinyinUtil.getTerm(sortList.get(i).getCityName());
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	public void setData(List<AreaEntity> sortList) {
		this.sortList = sortList;
		LogUtil.d("hl", "sortList=" + sortList.size());
		notifyDataSetChanged();
	}

}
