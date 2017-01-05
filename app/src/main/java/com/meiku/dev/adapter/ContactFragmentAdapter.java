package com.meiku.dev.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.FriendEntity;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.im.ChatActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.ShowLoginDialogUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

public class ContactFragmentAdapter extends BaseAdapter {

	private Context mContext;
	private List<FriendEntity> sortUserList = new ArrayList<FriendEntity>();

	public ContactFragmentAdapter(Context mContext) {
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
				parent, R.layout.item_addresslist, position);
		final FriendEntity fe = sortUserList.get(position);
		viewHolder.setText(R.id.tv_name, fe.getAliasName());
		viewHolder.setText(R.id.tv_major, fe.getPositionName());
		LinearLayout layout_addImage = viewHolder.getView(R.id.layout_addImage);
		layout_addImage.removeAllViews();
		MyRectDraweeView iv_head = new MyRectDraweeView(mContext);
		layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv_head.setUrlOfImage(fe.getClientThumbHeadPicUrl());
		// viewHolder.setImage(R.id.majorImg, fe.getClientPositionImgUrl());
		TextView alpha = viewHolder.getView(R.id.alpha);
		String intro = fe.getIntroduce();
		if (Tool.isEmpty(intro)) {
			intro = "我期望通过手艺交同行朋友";
		}
		viewHolder.setText(R.id.tv_intro, intro);
		LinearLayout layout_sex = viewHolder.getView(R.id.layout_sex);
		ImageView iv_sex = viewHolder.getView(R.id.iv_sex);
		if ("1".equals(fe.getGender())) {
			iv_sex.setBackgroundResource(R.drawable.nan_white);
			layout_sex.setBackgroundResource(R.drawable.sex_bg_man);
		} else {
			iv_sex.setBackgroundResource(R.drawable.nv_white);
			layout_sex.setBackgroundResource(R.drawable.sex_bg_woman);
		}
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			alpha.setVisibility(View.VISIBLE);
			String sortString = PinyinUtil.getTerm(fe.getAliasName());
			alpha.setText(sortString.matches("[A-Z]") ? sortString : "#");
		} else {
			alpha.setVisibility(View.GONE);
		}
		layout_addImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if
				// (!AppContext.getInstance().isLoginedAndInfoPerfect())
				// {
				// ShowLoginDialogUtil.showTipToLoginDialog(mContext);
				// return;
				// }
				Intent intent = new Intent(mContext, PersonShowActivity.class);
				intent.putExtra(PersonShowActivity.TO_USERID_KEY,
						fe.getFriendId() + "");
				intent.putExtra("nickName", fe.getAliasName());
				mContext.startActivity(intent);
			}
		});
		viewHolder.getConvertView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!AppContext.getInstance().isLoginedAndInfoPerfect()) {
					ShowLoginDialogUtil.showTipToLoginDialog(mContext);
					return;
				}
				Intent i = new Intent(mContext, ChatActivity.class);
				i.putExtra(ConstantKey.KEY_IM_TALKTO, fe.getFriendId());
				i.putExtra(ConstantKey.KEY_IM_TALKTOACCOUNT, fe.getLeanCloudUserName());
				i.putExtra(ConstantKey.KEY_IM_TALKTO_NAME, fe.getAliasName());
				i.putExtra(ConstantKey.KEY_IM_TALKTO_HEADIMAGEPATH,
						fe.getClientHeadPicUrl());
				mContext.startActivity(i);
			}
		});
		return viewHolder.getConvertView();
	}

	public int getSectionForPosition(int position) {
		if (position > sortUserList.size())
			return 0;
		String sortString = PinyinUtil.getTerm(sortUserList.get(position)
				.getAliasName());
		return sortString.matches("[A-Z]") ? sortString.charAt(0) : "#"
				.charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortString = PinyinUtil.getTerm(sortUserList.get(i)
					.getAliasName());
			char firstChar = sortString.matches("[A-Z]") ? sortString.charAt(0)
					: "#".charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	public void setData(List<FriendEntity> sortUserList) {
		this.sortUserList = sortUserList;
		AppContext.setAddressList(sortUserList);
		notifyDataSetChanged();
	}

	public List<FriendEntity> getSortUserList() {
		return sortUserList;
	}

}
