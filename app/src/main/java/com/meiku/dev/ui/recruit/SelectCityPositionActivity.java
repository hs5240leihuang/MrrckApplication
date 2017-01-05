package com.meiku.dev.ui.recruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AreaContainEntity;
import com.meiku.dev.bean.AreaSetEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ListViewUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonExpandableListView;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

/** 发布职位选择职位界面 */
public class SelectCityPositionActivity extends BaseActivity implements
		android.view.View.OnClickListener {
	private int cityCode = -1, provinceCode = -1;// 城市代码
	private String cityName, provinceName;// 城市名称
	private EditText selectedCityED;// 选择的城市
	private CommonExpandableListView areaListView;// 省市列表
	private int flag;
	private String multiSelectedIds;
	private String multiSelectedValues;
	private String multiSelectedProvinces;
	private LinearLayout layout_hotcity;
	private List<AreaSetEntity> areasetlist = new ArrayList<AreaSetEntity>();
	private LinearLayout lin_all;
	private String wholeCode;

	// public static boolean isWhole = false;//是否是全国,默认否

	private static boolean ischeck = false;// 是否选择全国

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		lin_all = (LinearLayout) findViewById(R.id.lin_all);
		layout_hotcity = (LinearLayout) findViewById(R.id.layout_hotcity);
		layout_hotcity.setVisibility(View.GONE);
		findViewById(R.id.right_txt_title).setOnClickListener(this);
		selectedCityED = (EditText) findViewById(R.id.selected_city_ed);
	}

	// 配置省市列表
	private void initAreaList(List<AreaSetEntity> areasetlist,
			final Map<String, List<AreaContainEntity>> district) {
		areaListView = (CommonExpandableListView) findViewById(R.id.area_list);
		areaListView.setGroupIndicator(null);
		ReExpandableListAdapter adapter = new ReExpandableListAdapter(this,
				areasetlist, district);
		areaListView.setAdapter(adapter);
		ListViewUtil.setListViewHeightBasedOnChildren(areaListView);
	}

	// 获取省市列表
	private void getArea() {
		Map<String, List<AreaContainEntity>> map = new HashMap<String, List<AreaContainEntity>>();
		for (int i = 0; i < areasetlist.size(); i++) {
			List<AreaContainEntity> district = areasetlist.get(i)
					.getContainAreaList();
			if (MKDataBase.getInstance().getMunCityCode()
					.containsKey(areasetlist.get(i).getCityCode())) {
				AreaContainEntity allSelect = new AreaContainEntity();
				allSelect.setCityCode(areasetlist.get(i).getCityCode());
				allSelect.setCityName("全部");
				allSelect.setDelStatus1(2);
				district.add(0, allSelect);
				AreaContainEntity tempbean = new AreaContainEntity();
				tempbean.setCityCode(areasetlist.get(i).getCityCode());
				tempbean.setCityName(areasetlist.get(i).getCityName());
				tempbean.setParentCode(areasetlist.get(i).getCityCode());
				district.add(tempbean);
				map.put(areasetlist.get(i).getCityName(), district);
			} else if (district.size() > 0) {
				AreaContainEntity allSelect = new AreaContainEntity();
				allSelect.setCityCode(-1);
				allSelect.setCityName("全部");
				allSelect.setDelStatus1(2);
				district.add(0, allSelect);
				map.put(areasetlist.get(i).getCityName(), district);
			}
			// else {
			// AreaSetEntity self = areasetlist.get(i);
			// List<AreaContainEntity> temp = new
			// ArrayList<AreaContainEntity>();
			// temp.addAll((Collection<? extends AreaContainEntity>) self);
			// map.put(areasetlist.get(i).getCityName(), temp);
			// }
		}
		initAreaList(areasetlist, map);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit_btn:
		case R.id.right_txt_title:
			backWithDate();
			break;
		case R.id.lin_all:
			multiSelectedProvinces = "";
			multiSelectedValues = "";
			multiSelectedIds = "";
			if (ischeck) {
				selectedCityED.setText("");
				ischeck = false;
			} else {
				selectedCityED.setText("全国");
				ischeck = true;
			}
			break;
		default:
			break;
		}
	}

	// 带数据返回
	private void backWithDate() {
		Intent intent = new Intent();
		// 多选返回
		intent.putExtra(
				"cityCode",
				!Tool.isEmpty(multiSelectedIds) ? multiSelectedIds.substring(0,
						multiSelectedIds.length() - 1) : "");
		intent.putExtra(
				"cityName",
				!Tool.isEmpty(multiSelectedValues) ? multiSelectedValues
						.substring(0, multiSelectedValues.length() - 1) : "");
		intent.putExtra(
				"provinceCode",
				!Tool.isEmpty(multiSelectedProvinces) ? multiSelectedProvinces
						.substring(0, multiSelectedProvinces.length() - 1) : "");
		intent.putExtra("isWhole", ischeck);

		setResult(RESULT_OK, intent);
		if (flag == 1) {
			sendBroadcast(intent);
		}
		finish();
	}

	// 二级ListView数据适配器
	class ReExpandableListAdapter extends BaseExpandableListAdapter {

		private List<AreaSetEntity> mParent;
		private Map<String, List<AreaContainEntity>> mChild;
		private Context mContext;

		public ReExpandableListAdapter(Context context,
				List<AreaSetEntity> parent,
				Map<String, List<AreaContainEntity>> child) {
			mContext = context;
			mParent = parent;
			mChild = child;
		}

		@Override
		public int getGroupCount() {
			return mParent.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mParent.get(groupPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.expandablelistview_item_parent, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.text);
			textView.setText(mParent.get(groupPosition).getCityName());
			return textView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			String key = mParent.get(groupPosition).getCityName();
			return mChild.get(key).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String key = mParent.get(groupPosition).getCityName();
			final List<AreaContainEntity> info = mChild.get(key);

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.expandablelistview_item_child, null);
			}
			MyGridView gridView = (MyGridView) convertView
					.findViewById(R.id.district_grid);
			gridView.setAdapter(new CommonAdapter<AreaContainEntity>(mContext,
					R.layout.gridview_item_text_whitebg, info) {
				@Override
				public void convert(ViewHolder viewHolder,
						final AreaContainEntity areaDTO) {

					viewHolder.setText(R.id.text, areaDTO.getCityName());
					viewHolder.getConvertView().setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									ischeck = false;
									if (2 == areaDTO.getDelStatus1()) {// 2全选所有市
										for (int i = 0, size = info.size(); i < size; i++) {
											info.get(i).setDelStatus1(1);
										}
										areaDTO.setDelStatus1(3);
									} else if (3 == areaDTO.getDelStatus1()) {// 3取消全选所有市
										for (int i = 0, size = info.size(); i < size; i++) {
											info.get(i).setDelStatus1(0);
										}
										areaDTO.setDelStatus1(2);
									} else if (1 == areaDTO.getDelStatus1()) {
										areaDTO.setDelStatus1(0);
									} else if (0 == areaDTO.getDelStatus1()) {
										areaDTO.setDelStatus1(1);
									}

									notifyDataSetChanged();
									multiSelectedIds = "";
									multiSelectedValues = "";
									multiSelectedProvinces = "";
									for (int i = 0, size = mParent.size(); i < size; i++) {
										String key = mParent.get(i)
												.getCityName();
										List<AreaContainEntity> childInfo = mChild
												.get(key);
										boolean hasSelectedThisProvince = false;
										if (childInfo != null) {
											for (int j = 0, childSize = childInfo
													.size(); j < childSize; j++) {
												if (childInfo.get(j)
														.getDelStatus1() == 1) {
													multiSelectedIds += childInfo
															.get(j)
															.getCityCode()
															+ ",";
													multiSelectedValues += childInfo
															.get(j)
															.getCityName()
															+ ",";
													hasSelectedThisProvince = true;
												}
											}
										}

										if (hasSelectedThisProvince) {
											multiSelectedProvinces += mParent
													.get(i).getCityCode() + ",";
										}
									}
									selectedCityED.setText(!Tool
											.isEmpty(multiSelectedValues) ? multiSelectedValues
											.substring(0, multiSelectedValues
													.length() - 1) : "");

								}
							});
					ImageView selectView = viewHolder.getView(R.id.select);

					selectView.setVisibility(View.VISIBLE);
					if (1 == areaDTO.getDelStatus1()) {// 使用DelStatus做选择标记使用
						selectView.setVisibility(View.VISIBLE);
						viewHolder.setImage(R.id.select,
								R.drawable.icon_duigoux);
					} else if (0 == areaDTO.getDelStatus1()) {
						selectView.setVisibility(View.VISIBLE);
						viewHolder.setImage(R.id.select,
								R.drawable.icon_weixuanzhong);
					} else if (2 == areaDTO.getDelStatus1()
							|| 3 == areaDTO.getDelStatus1()) {
						selectView.setVisibility(View.GONE);
					}

				}
			});
			return gridView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_select_city;
	}

	@Override
	public void initValue() {
		getData();
		flag = getIntent().getIntExtra("flag", 0);
	}

	@Override
	public void bindListener() {
		lin_all.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("303030", "发布城市" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("wholeCode") + "").length() > 2) {
				wholeCode = resp.getBody().get("wholeCode").getAsString();
				if ("1".equals(wholeCode)) {
					lin_all.setVisibility(View.VISIBLE);
				} else {
					lin_all.setVisibility(View.GONE);
				}
			}
			if ((resp.getBody().get("data") + "").length() > 2) {
				areasetlist = (List<AreaSetEntity>) JsonUtil.jsonToList(resp
						.getBody().get("data").toString(),
						new TypeToken<List<AreaSetEntity>>() {
						}.getType());
				getArea();
			} else {
				ToastUtil.showShortToast("没有开放城市");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

	}

	public void getData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PUBLIC_POSITION_CITY));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase, true);
	}
}
