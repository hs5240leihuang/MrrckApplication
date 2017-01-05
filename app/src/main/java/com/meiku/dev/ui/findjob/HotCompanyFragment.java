package com.meiku.dev.ui.findjob;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FindJobEntity;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.fragments.BaseFragment;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.ViewHolder;

public class HotCompanyFragment extends BaseFragment {

	private View layout_view;
	private List<FindJobEntity> jobGridData = new ArrayList<FindJobEntity>();// 招聘全部数据;
	private List<FindJobEntity> showData = new ArrayList<FindJobEntity>();// 招聘显示的数据;
	private CommonAdapter<FindJobEntity> jobGridAdapter;
	private String companyDetailUrl;
	private int pageIndex;
	private int dataSize;
	private GridView midGridView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_view = inflater.inflate(R.layout.fragment_hotcompany, null);
		init();
		return layout_view;
	}

	private void init() {
		String hotCompanyJson = (String) PreferHelper.getSharedParam(
				ConstantKey.FINDJOB_HOTCOMPANY, "");
		companyDetailUrl = (String) PreferHelper.getSharedParam(
				ConstantKey.FINDJOB_COMPANYURL, "");
		if (!Tool.isEmpty(hotCompanyJson)) {
			FindJobEntity jobEntity = (FindJobEntity) JsonUtil.jsonToObj(
					FindJobEntity.class, hotCompanyJson);
			if (jobEntity != null) {
				jobGridData.addAll(jobEntity.getCompanyList());
			}
		}
		midGridView = (MyGridView) layout_view.findViewById(R.id.midGridview1);
	}

	@Override
	public void initValue() {
		Bundle bundle = getArguments();
		pageIndex = bundle.getInt("pageIndex");
		dataSize = bundle.getInt("dataSize");
		int start = (pageIndex - 1) * 6;
		int end;
		if (pageIndex * 6 <= dataSize) {
			end = (pageIndex - 1) * 6 + 6;
		} else {
			end = dataSize;
		}
		LogUtil.d("hl", pageIndex + "//" + dataSize + "(" + start + "-" + end
				+ ")" + jobGridData.size());
		if (jobGridData != null || jobGridData.size() == dataSize) {
			showData = jobGridData.subList(start, end);
		}
		jobGridAdapter = new CommonAdapter<FindJobEntity>(getActivity(),
				R.layout.item_service_rdzp, showData) {

			@Override
			public void convert(ViewHolder viewHolder, final FindJobEntity t) {
				viewHolder.getView(R.id.id_fullImg).setVisibility(View.GONE);
				viewHolder.setImageWithNewSize(R.id.id_img, t.getCompanyLogo(),
						150, 150);
				viewHolder.setText(R.id.id_name, t.getCompanyName());
				viewHolder.setText(R.id.id_people,
						t.getJobName() + t.getNeedNum());
				viewHolder.setText(R.id.id_money, t.getSalaryValue());
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (!Tool.isEmpty(companyDetailUrl)
										&& !Tool.isEmpty(t.getCompanyId())) {
									startActivity(new Intent(getActivity(),
											CompanyInfoActivity.class)
											.putExtra("comeUrl",
													companyDetailUrl).putExtra(
													"key", t.getCompanyId()));
								} else {
									ToastUtil.showShortToast("参数有误，查看详细信息失败！");
								}
							}
						});
			}

		};
		midGridView.setAdapter(jobGridAdapter);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

}
