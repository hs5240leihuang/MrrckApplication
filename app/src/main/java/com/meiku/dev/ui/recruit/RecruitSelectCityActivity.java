package com.meiku.dev.ui.recruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AreaContainEntity;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.bean.AreaSetEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.decoration.OpenPermissionActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.ListViewUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonExpandableListView;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

/**
 * 招聘宝购买城市选择
 */
public class RecruitSelectCityActivity extends BaseActivity implements
		View.OnClickListener {

	private TextView selectedCityED;// 选择的城市
	private CommonExpandableListView areaListView;// 省市列表

	private String multiSelectedIds;
	private String multiSelectedValues;
	private String multiSelectedProvinces;
	private String multiSelectedProvincesNames;
	private ImageView img_quanguo;
	private boolean quangouSelected = false;
	private ReExpandableListAdapter adapter;
	private HashMap<String, List<AreaEntity>> mapALLCityData;
	private List<AreaEntity> allProvincesLiast;
	private int selectType;
	private ArrayList<String> canSelectAreaList = new ArrayList<String>();
	private LinearLayout layout_quanguo;
	private boolean isSingleSelect = false;
	private boolean fromOpenPermission = false;
	private String wholeCode;

	@Override
	public void initView() {
		selectedCityED = (TextView) findViewById(R.id.selected_city_ed);
		layout_quanguo = (LinearLayout) findViewById(R.id.layout_quanguo);
		img_quanguo = (ImageView) findViewById(R.id.img_quanguo);
		layout_quanguo.setOnClickListener(this);
		areaListView = (CommonExpandableListView) findViewById(R.id.area_list);
		areaListView.setGroupIndicator(null);
		ListViewUtil.setListViewHeightBasedOnChildren(areaListView);
		areaListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (quangouSelected) {
					ToastUtil.showShortToast("已选择全国");
					return true;
				}
				return false;
			}
		});
	}

	// 获取省市列表
	private void getArea() {
		mapALLCityData = new HashMap<String, List<AreaEntity>>();
		allProvincesLiast = MKDataBase.getInstance().getCity();
		for (int i = 0; i < allProvincesLiast.size(); i++) {
			List<AreaEntity> district = MKDataBase.getInstance().getDistrict(
					allProvincesLiast.get(i).getCityCode());
			if (district.size() > 0) {
				if (!isSingleSelect) {// 多选时添加选择全部
					AreaEntity allSelect = new AreaEntity();
					allSelect.setCityCode(-1);
					allSelect.setCityName("全部");
					allSelect.setDelStatus(2);
					district.add(0, allSelect);
				}
				if ("上海市".equals(allProvincesLiast.get(i).getCityName())
						|| "天津市".equals(allProvincesLiast.get(i).getCityName())
						|| "重庆市".equals(allProvincesLiast.get(i).getCityName())
						|| "北京市".equals(allProvincesLiast.get(i).getCityName())) {
					AreaEntity self = allProvincesLiast.get(i);
					List<AreaEntity> temp = new ArrayList<AreaEntity>();
					temp.add(self);
					mapALLCityData.put(allProvincesLiast.get(i).getCityName(),
							temp);
				} else {
					mapALLCityData.put(allProvincesLiast.get(i).getCityName(),
							district);
				}
			} else {
				AreaEntity self = allProvincesLiast.get(i);
				List<AreaEntity> temp = new ArrayList<AreaEntity>();
				temp.add(self);
				mapALLCityData
						.put(allProvincesLiast.get(i).getCityName(), temp);
			}
		}
		adapter = new ReExpandableListAdapter(this, allProvincesLiast,
				mapALLCityData);
		areaListView.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_txt_title:
			if (fromOpenPermission) {
				OpenPermissionActivity.setMapALLCityData(mapALLCityData);
			}
			Intent intent = new Intent();
			intent.putExtra("cityCode",
					Tool.checkEmptyAndDeleteEnd(multiSelectedIds));
			intent.putExtra("cityName",
					Tool.checkEmptyAndDeleteEnd(multiSelectedValues));
			intent.putExtra("provinceCode",
					Tool.checkEmptyAndDeleteEnd(multiSelectedProvinces));
			intent.putExtra("provinceName",
					Tool.checkEmptyAndDeleteEnd(multiSelectedProvincesNames));
			intent.putExtra("showSelect",
					Tool.checkEmptyAndDeleteEnd(selectedCityED.getText()
							.toString()));
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.layout_quanguo:
			if (quangouSelected) {
				img_quanguo
						.setBackgroundResource(R.drawable.zhaozhuangxiuxieyi);
				quangouSelected = false;
				selectedCityED.setText("");
			} else {
				img_quanguo
						.setBackgroundResource(R.drawable.zhaozhuangxiuxieyi1);
				quangouSelected = true;
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					if (areaListView != null) {
						areaListView.collapseGroup(i);// 关闭所有展开的组
					}
				}
				selectedCityED.setText("全国");
				// 重置数据
				for (int i = 0, size = allProvincesLiast.size(); i < size; i++) {
					String key = allProvincesLiast.get(i).getCityName();
					List<AreaEntity> childInfo = mapALLCityData.get(key);
					int childSize = childInfo.size();
					for (int j = 0; j < childSize; j++) {
						if (childInfo.get(j).getDelStatus() == 1) {
							childInfo.get(j).setDelStatus(0);
						}
					}
				}
				multiSelectedProvincesNames = "全国";
			}
			break;
		default:
			break;
		}
	}

	// 二级ListView数据适配器
	class ReExpandableListAdapter extends BaseExpandableListAdapter {

		private List<AreaEntity> mParent;
		private Map<String, List<AreaEntity>> mChild;
		private Context mContext;

		public ReExpandableListAdapter(Context context,
				List<AreaEntity> parent, Map<String, List<AreaEntity>> child) {
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
			final List<AreaEntity> info = mChild.get(key);

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.expandablelistview_item_child, null);
			}
			MyGridView gridView = (MyGridView) convertView
					.findViewById(R.id.district_grid);
			gridView.setAdapter(new CommonAdapter<AreaEntity>(mContext,
					R.layout.gridview_item_text_whitebg, info) {
				@Override
				public void convert(ViewHolder viewHolder,
						final AreaEntity areaDTO) {
					viewHolder.setText(R.id.text, areaDTO.getCityName());
					viewHolder.getConvertView().setOnClickListener(
							new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									if (!isSingleSelect) {

										if (2 == areaDTO.getDelStatus()) {// 2全选所有市
											for (int i = 0, size = info.size(); i < size; i++) {
												info.get(i).setDelStatus(1);
											}
											areaDTO.setDelStatus(3);
										} else if (3 == areaDTO.getDelStatus()) {// 3取消全选所有市
											for (int i = 0, size = info.size(); i < size; i++) {
												info.get(i).setDelStatus(0);
											}
											areaDTO.setDelStatus(2);
										} else if (1 == areaDTO.getDelStatus()) {
											areaDTO.setDelStatus(0);
										} else if (0 == areaDTO.getDelStatus()) {
											areaDTO.setDelStatus(1);
										}

										notifyDataSetChanged();
										multiSelectedIds = "";
										multiSelectedValues = "";
										multiSelectedProvinces = "";
										multiSelectedProvincesNames = "";

										String showArea = "";
										for (int i = 0, size = mParent.size(); i < size; i++) {
											String key = mParent.get(i)
													.getCityName();
											List<AreaEntity> childInfo = mChild
													.get(key);
											if (childInfo == null) {
												return;
											}
											int childSize = childInfo.size();
											boolean hasAllItme = false;// 是否有全部选项
											String oneProCityNames = "";// 一个省选择的所有城市名
											List<AreaEntity> OneProSelectedCitys = new ArrayList<AreaEntity>();
											for (int j = 0; j < childSize; j++) {
												if (childInfo.get(j)
														.getDelStatus() == 1) {
													oneProCityNames += childInfo
															.get(j)
															.getCityName()
															+ ",";
													OneProSelectedCitys
															.add(childInfo
																	.get(j));
												}
												if ("全部".equals(childInfo
														.get(j).getCityName())) {
													hasAllItme = true;
												}
											}
											if (OneProSelectedCitys.size() > 0
													&& OneProSelectedCitys
															.size() == childInfo
															.size()
															- (hasAllItme ? 1
																	: 0)) {
												showArea += key + ",";
												multiSelectedProvinces += mParent
														.get(i).getCityCode()
														+ ",";
												multiSelectedProvincesNames += mParent
														.get(i).getCityName()
														+ ",";
											} else {
												showArea += oneProCityNames;
												for (int j = 0; j < childSize; j++) {
													if (childInfo.get(j)
															.getDelStatus() == 1) {
														multiSelectedIds += childInfo
																.get(j)
																.getCityCode()
																+ ",";
														multiSelectedValues += childInfo
																.get(j)
																.getCityName()
																+ ",";
													}
												}
											}
										}

										if (selectType == ConstantKey.SELECT_AREA_HASAERA) {
											selectedCityED.setText(Tool
													.checkEmptyAndDeleteEnd(multiSelectedValues));
										} else {
											selectedCityED.setText(Tool
													.checkEmptyAndDeleteEnd(showArea));
										}
									} else {
										multiSelectedIds = areaDTO
												.getCityCode() + "";
										multiSelectedValues = areaDTO
												.getCityName() + "";
										multiSelectedProvinces = areaDTO
												.getParentCode() + "";
										selectedCityED.setText(Tool
												.checkEmptyAndDeleteEnd(multiSelectedValues));
									}
								}
							});
					ImageView selectView = viewHolder.getView(R.id.select);
					selectView.setVisibility(View.VISIBLE);
					if (isSingleSelect) {
						selectView.setVisibility(View.GONE);
					} else {
						if (1 == areaDTO.getDelStatus()) {// 使用DelStatus做选择标记使用
							selectView.setVisibility(View.VISIBLE);
							viewHolder.setImage(R.id.select,
									R.drawable.icon_duigoux);
						} else if (0 == areaDTO.getDelStatus()) {
							selectView.setVisibility(View.VISIBLE);
							viewHolder.setImage(R.id.select,
									R.drawable.icon_weixuanzhong);
						} else if (2 == areaDTO.getDelStatus()
								|| 3 == areaDTO.getDelStatus()) {
							selectView.setVisibility(View.GONE);
						}
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
		return R.layout.activity_multiselect_city;
	}

	@Override
	public void initValue() {
		selectType = getIntent().getIntExtra("selectType", 0);
		isSingleSelect = getIntent().getBooleanExtra("isSingleSelect", false);
		fromOpenPermission = getIntent().getBooleanExtra("fromOpenPermission",
				false);
		canSelectAreaList = getIntent().getStringArrayListExtra(
				"canSelectcitys");
		getArea();
		if (selectType == ConstantKey.SELECT_AREA_ALL) {// 默认可以选择全国

		} else if (selectType == ConstantKey.SELECT_AREA_HASAERA) {// 只可以选择提供的城市
			layout_quanguo.setVisibility(View.GONE);// 不显示全国
			List<AreaEntity> canselectProvinces = new ArrayList<AreaEntity>();
			HashMap<String, List<AreaEntity>> mapCanselectData = new HashMap<String, List<AreaEntity>>();
			for (int i = 0, prosize = allProvincesLiast.size(); i < prosize; i++) {
				String key = allProvincesLiast.get(i).getCityName();
				List<AreaEntity> childInfo = mapALLCityData.get(key);
				// 单个省份涵盖的可选城市list
				List<AreaEntity> canSelectChildInfo = new ArrayList<AreaEntity>();
				boolean hasThisCity = false;
				for (int j = 0, childSize = childInfo.size(); j < childSize; j++) {
					for (int j2 = 0, number = canSelectAreaList.size(); j2 < number; j2++) {
						if (canSelectAreaList.get(j2).equals(
								childInfo.get(j).getCityCode() + "")) {
							hasThisCity = true;
							canSelectChildInfo.add(childInfo.get(j));
							LogUtil.e("hl", "has this city ="
									+ canSelectAreaList.get(j2));
						}
					}
				}
				if (hasThisCity) {
					canselectProvinces.add(allProvincesLiast.get(i));
					mapCanselectData.put(
							allProvincesLiast.get(i).getCityName(),
							canSelectChildInfo);
					LogUtil.e("hl",
							"has this Provinces ="
									+ allProvincesLiast.get(i).getCityName()
									+ ",city——canselectSize="
									+ canSelectChildInfo.size());
				}
			}
			allProvincesLiast.clear();
			allProvincesLiast.addAll(canselectProvinces);
			mapALLCityData.clear();
			mapALLCityData = mapCanselectData;
			adapter = new ReExpandableListAdapter(this, allProvincesLiast,
					mapALLCityData);
			areaListView.setAdapter(adapter);
		} else if (selectType == ConstantKey.SELECT_AREA_NOTHAS) {// 只可以选择提供的城市之外的地区
			// 现有数据移除购买过的省
			LogUtil.e("hl", "1省 =" + allProvincesLiast.size() + "//"
					+ mapALLCityData.size());
			for (int m = allProvincesLiast.size() - 1; m >= 0; m--) {
				for (int n = 0, number = canSelectAreaList.size(); n < number; n++) {
					if (canSelectAreaList.get(n).equals(
							allProvincesLiast.get(m).getCityCode() + "")) {
						LogUtil.e("hl", "移除 已经购买的省 ="
								+ allProvincesLiast.get(m).getCityName()
								+ canSelectAreaList.get(n));
						mapALLCityData.remove(allProvincesLiast.get(m)
								.getCityName());
						allProvincesLiast.remove(m);
					}
				}
			}
			LogUtil.e("hl", "2省 =" + allProvincesLiast.size() + "//"
					+ mapALLCityData.size());
			// 现有数据移除购买过的城市
			for (int i = allProvincesLiast.size() - 1; i >= 0; i--) {
				String key = allProvincesLiast.get(i).getCityName();
				for (int j = mapALLCityData.get(key).size() - 1; j >= 0; j--) {
					for (int k = 0, canCitySize = canSelectAreaList.size(); k < canCitySize; k++) {
						if (j < mapALLCityData.get(key).size()
								&& canSelectAreaList.get(k).equals(
										mapALLCityData.get(key).get(j)
												.getCityCode()
												+ "")) {
							LogUtil.e("hl", "标记 已购买城市 city ="
									+ mapALLCityData.get(key).get(j)
											.getCityName());
							mapALLCityData.get(key).remove(j);
						}
					}
				}
			}
			LogUtil.e("hl", "3省 =" + allProvincesLiast.size() + "//"
					+ mapALLCityData.size());
			adapter = new ReExpandableListAdapter(this, allProvincesLiast,
					mapALLCityData);
			areaListView.setAdapter(adapter);
		}
		getHasCityData();
	}

	@Override
	public void bindListener() {
		findViewById(R.id.right_txt_title).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("hl", "装修宝已经购买城市" + resp.getBody());
		switch (requestCode) {
		case reqCodeOne:
			if ((resp.getBody().get("wholeCode") + "").length() > 2) {
				wholeCode = resp.getBody().get("wholeCode").getAsString();
				if ("1".equals(wholeCode)) {// 有全国
					ToastUtil.showShortToast("已经购买全国");
					finish();
					return;
				}
			}
			if ((resp.getBody().get("data") + "").length() > 2) {
				// 获取已购买地区
				List<AreaSetEntity> areasetlist = (List<AreaSetEntity>) JsonUtil
						.jsonToList(resp.getBody().get("data").toString(),
								new TypeToken<List<AreaSetEntity>>() {
								}.getType());
				for (int i = 0, hasSize = areasetlist.size(); i < hasSize; i++) {
					List<AreaEntity> thisProCityList = mapALLCityData
							.get(areasetlist.get(i).getCityName());
					if (areasetlist.get(i).getCityCode() == 110000
							|| areasetlist.get(i).getCityCode() == 120000
							|| areasetlist.get(i).getCityCode() == 310000
							|| areasetlist.get(i).getCityCode() == 500000
							|| areasetlist.get(i).getCityCode() == 710000
							|| areasetlist.get(i).getCityCode() == 810000
							|| areasetlist.get(i).getCityCode() == 820000) {
						mapALLCityData.remove(areasetlist.get(i).getCityName());// 移除直辖市
						for (int k = allProvincesLiast.size()-1; 0 <= k; k--) {
							if (allProvincesLiast.get(k).getCityCode() == areasetlist
									.get(i).getCityCode()) {
								allProvincesLiast.remove(k);// 移除直辖市
							}
						}
					}
					List<AreaContainEntity> containAreaList = areasetlist
							.get(i).getContainAreaList();
					for (int m = 0; m < containAreaList.size(); m++) {
						for (int j = thisProCityList.size() - 1; j >= 0; j--) {
							if (thisProCityList.get(j).getCityCode() == containAreaList
									.get(m).getCityCode()) {
								thisProCityList.remove(j);// 移除已有城市
							}
						}
					}

				}
				adapter = new ReExpandableListAdapter(this, allProvincesLiast,
						mapALLCityData);
				areaListView.setAdapter(adapter);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	public void getHasCityData() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_PUBLIC_POSITION_CITY));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.EMPLOY_REQUEST_MAPPING, reqBase, true);
	}

}
