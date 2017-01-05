package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.PersonalTagEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.FlowLayout;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

/**
 * 个性标签
 * 
 */
public class HornorActivity extends BaseActivity {
	private TextView right_txt_title;
	private MyGridView gv_myTag;
	private LinearLayout layout_TagGroups;
	private List<PersonalTagEntity> selectedTags = new ArrayList<PersonalTagEntity>();
	private CommonAdapter<PersonalTagEntity> myTagAdapter;
	private TextView tagTip;
	private List<PersonalTagEntity> allTagGroupsData;
	private String personalTag;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_hornor;
	}

	@Override
	public void initView() {
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackground(null);
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String personalTag = "";
				for (int i = 0, size = selectedTags.size(); i < size; i++) {
					personalTag += selectedTags.get(i).getId() + ",";
				}
				Intent intent = new Intent();
				if (!Tool.isEmpty(personalTag)) {
					intent.putExtra("personalTag",
							personalTag.substring(0, personalTag.length() - 1));
				} else {
					intent.putExtra("personalTag", "");
				}
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		tagTip = (TextView) findViewById(R.id.tagTip);

		layout_TagGroups = (LinearLayout) findViewById(R.id.layout_TagGroups);
		gv_myTag = (MyGridView) findViewById(R.id.gv_myTag);
		myTagAdapter = new CommonAdapter<PersonalTagEntity>(this,
				R.layout.item_selectedtags, selectedTags) {

			@Override
			public void convert(ViewHolder viewHolder, PersonalTagEntity t) {
				viewHolder.setText(R.id.tv_tag, t.getTagName());
				if (!Tool.isEmpty(t.getColor())) {
					LinearLayout layout_tag = (LinearLayout) viewHolder
							.getView(R.id.layout_tag);
					layout_tag.setBackgroundResource(R.drawable.whiteround);
					Drawable drawable = layout_tag.getBackground();
					int color = Color.parseColor(t.getColor());
					drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				}

			}

		};
		gv_myTag.setAdapter(myTagAdapter);
		gv_myTag.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectedTags.remove(arg2);
				myTagAdapter.notifyDataSetChanged();
				setTagsTip(selectedTags.size());
			}
		});
	}

	@Override
	public void initValue() {
		personalTag = getIntent().getStringExtra("personalTag");
		LogUtil.d("hl", "old TagIds==>" + personalTag);
		request();
	}

	@Override
	public void bindListener() {

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (resp.getBody().get("personalTag").toString().length() > 2) {
				allTagGroupsData = (List<PersonalTagEntity>) JsonUtil
						.jsonToList(resp.getBody().get("personalTag")
								.toString(),
								new TypeToken<List<PersonalTagEntity>>() {
								}.getType());
				if (!Tool.isEmpty(allTagGroupsData)) {
					PreferHelper.setSharedParam("PERSION_TAGS", resp.getBody()
							.get("personalTag").toString());
					setTagsTip(0);
					layout_TagGroups.removeAllViews();
					// 设置底部选择标签
					for (int i = 0, size = allTagGroupsData.size(); i < size; i++) {
						creatOneTypeTagsGroups(allTagGroupsData.get(i));
					}
					// 设置默认已经选择的标签
					if (!Tool.isEmpty(personalTag)) {
						String[] tagIds = personalTag.split(",");
						for (int i = 0, tagNum = tagIds.length; i < tagNum; i++) {
							for (int j = 0, size = allTagGroupsData.size(); j < size; j++) {
								for (int k = 0, num = allTagGroupsData.get(j)
										.getPersonalList().size(); k < num; k++) {
									if (tagIds[i].equals(allTagGroupsData
											.get(j).getPersonalList().get(k)
											.getId()
											+ "")) {
										selectedTags.add(allTagGroupsData
												.get(j).getPersonalList()
												.get(k));
										myTagAdapter.notifyDataSetChanged();
										setTagsTip(selectedTags.size());
									}
								}
							}
						}
					}

				}
			} else {
				ToastUtil.showShortToast("个性标签加载不成功");
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 添加一行(一种)类型标签
	 * 
	 * @param oneTypeData
	 */
	private void creatOneTypeTagsGroups(PersonalTagEntity oneTypeData) {
		View tagGroupItem = LayoutInflater.from(this).inflate(
				R.layout.view_taggroups, null, false);
		TextView tagGroupName = (TextView) tagGroupItem
				.findViewById(R.id.tagGroupName);
		tagGroupName.setText(oneTypeData.getTagName());
		FlowLayout layout_tags = (FlowLayout) tagGroupItem
				.findViewById(R.id.layout_tags);
		layout_tags.removeAllViews();
		for (int i = 0, size = oneTypeData.getPersonalList().size(); i < size; i++) {
			PersonalTagEntity tagData = oneTypeData.getPersonalList().get(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int margin = ScreenUtil.dip2px(this, 10);
			params.setMargins(margin, margin, margin, margin);
			int padding = ScreenUtil.dip2px(this, 10);
			TextView mTextView = new TextView(this);
			mTextView.setPadding(padding, padding / 3, padding, padding / 3);
			mTextView.setEllipsize(TruncateAt.END);
			mTextView.setSingleLine();
			mTextView.setGravity(Gravity.NO_GRAVITY);
			mTextView.setLayoutParams(params);
			if (!Tool.isEmpty(tagData.getColor())) {
				mTextView.setBackgroundResource(R.drawable.whiteround);
			}
			Drawable drawable = mTextView.getBackground();
			int color = Color.parseColor(tagData.getColor());
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
			mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			mTextView.setTextColor(getResources().getColor(R.color.white));
			mTextView.setText(tagData.getTagName());
			mTextView.setOnClickListener(new TagClickListener(i, tagData));
			layout_tags.addView(mTextView);
		}
		layout_TagGroups.addView(tagGroupItem);
	}

	private class TagClickListener implements OnClickListener {

		private PersonalTagEntity tagData;

		public TagClickListener(int i, PersonalTagEntity tagData) {
			this.tagData = tagData;
		}

		@Override
		public void onClick(View arg0) {

			Integer currentTagParentId = tagData.getParentId();
			boolean hasThisTypeTag = false;
			int position = 0;
			for (int i = 0, size = selectedTags.size(); i < size; i++) {
				if (selectedTags.get(i).getParentId() == currentTagParentId) {
					hasThisTypeTag = true;
					position = i;
				}
			}
			// 已经有此类型则替换，没有则添加
			if (hasThisTypeTag) {
				selectedTags.set(position, tagData);
			} else {
				selectedTags.add(tagData);
			}
			myTagAdapter.notifyDataSetChanged();
			setTagsTip(selectedTags.size());
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeOne:
			ToastUtil.showShortToast("个性标签加载不成功");
			finish();
			break;

		default:
			break;
		}
	}

	public void setTagsTip(int size) {
		tagTip.setText("我的标签，我与众不同（" + size + "/" + allTagGroupsData.size()
				+ "）");
	}

	// 请求个性签名
	public void request() {
		ReqBase req = new ReqBase();
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PERSION_TAGS));
		req.setBody(new JsonObject());
		httpPost(reqCodeOne, AppConfig.PERSONAL_REQUEST_MAPPING, req, true);
	}

}
