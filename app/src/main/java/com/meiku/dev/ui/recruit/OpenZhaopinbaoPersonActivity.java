package com.meiku.dev.ui.recruit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.CompanyInfoCountEntity;
import com.meiku.dev.bean.MkDataConfigReleaseMonths;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.db.MKDataBase;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.product.PayStyleActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyListView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;

public class OpenZhaopinbaoPersonActivity extends BaseActivity implements
		OnClickListener {
	private MyListView listview;
	private CommonAdapter<MkDataConfigReleaseMonths> showAdapter;
	private List<MkDataConfigReleaseMonths> showlist = new ArrayList<MkDataConfigReleaseMonths>();
	private ImageView img_data;
	private TextView tv_starttime;
	private Button btn_ok;
	private ImageView img_topvip;
	private TextView tv_toptype;
	private TextView tv_kaitongfeiyong;
	private TextView tv_allfeiyong;
	private int flag;//0续费  1开通
	private String month;
	private String inviteNum;
	private String money;
	private TextView center_txt_title;
	private CompanyInfoCountEntity companyinfocountentity;
	private LinearLayout lin_data;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_openzhaopinbaoperson;
	}

	@Override
	public void initView() {
		lin_data=(LinearLayout) findViewById(R.id.lin_data);
		center_txt_title=(TextView) findViewById(R.id.center_txt_title);
		tv_allfeiyong = (TextView) findViewById(R.id.tv_allfeiyong);
		tv_kaitongfeiyong = (TextView) findViewById(R.id.tv_kaitongfeiyong);
		tv_toptype = (TextView) findViewById(R.id.tv_toptype);
		img_topvip = (ImageView) findViewById(R.id.img_topvip);
		listview = (MyListView) findViewById(R.id.listview);
		img_data = (ImageView) findViewById(R.id.img_data);
		tv_starttime = (TextView) findViewById(R.id.tv_starttime);
		btn_ok = (Button) findViewById(R.id.btn_ok);
	}

	@Override
	public void initValue() {
		flag = getIntent().getIntExtra("flag", -1);
		if (flag == 0) {
			getinformation();
			center_txt_title.setText("续费美库招聘宝会员");
		} else {
			img_data.setEnabled(true);
			lin_data.setEnabled(true);
			center_txt_title.setText("开通美库招聘宝会员");
		}
		showlist = MKDataBase.getInstance().getReleaseMonthsList (2);
		if (Tool.isEmpty(showlist)) {
			return;
		}
		BitmapUtils bitmapUtils = new BitmapUtils(
				OpenZhaopinbaoPersonActivity.this);
		bitmapUtils.display(img_topvip, showlist.get(0).getMonthsName()
				.substring(0, showlist.get(0).getMonthsName().indexOf("|"))
				+ "_thumb.png");
		tv_toptype.setText(showlist
				.get(0)
				.getMonthsName()
				.substring(showlist.get(0).getMonthsName().indexOf("|") + 1,
						showlist.get(0).getMonthsName().indexOf("员") + 1));
		tv_kaitongfeiyong.setText(showlist
				.get(0)
				.getMonthsName()
				.substring(showlist.get(0).getMonthsName().indexOf("￥"),
						showlist.get(0).getMonthsName().indexOf("元") + 1));
		tv_allfeiyong.setText(showlist
				.get(0)
				.getMonthsName()
				.substring(showlist.get(0).getMonthsName().indexOf("￥"),
						showlist.get(0).getMonthsName().indexOf("元") + 1));
		month = showlist
				.get(0)
				.getMonthsName()
				.substring(showlist.get(0).getMonthsName().indexOf("会员|") + 3,
						showlist.get(0).getMonthsName().indexOf("个"));
		inviteNum = showlist
				.get(0)
				.getMonthsName()
				.substring(showlist.get(0).getMonthsName().indexOf("送") + 1,
						showlist.get(0).getMonthsName().indexOf("份"));
		money = showlist.get(0).getMoney();
		showAdapter = new CommonAdapter<MkDataConfigReleaseMonths>(
				OpenZhaopinbaoPersonActivity.this, R.layout.item_openvip,
				showlist) {

			@Override
			public void convert(ViewHolder viewHolder,
					final MkDataConfigReleaseMonths t) {
				final int position = viewHolder.getPosition();
				String data = t.getMonthsName();
				String vipType = data.substring(data.indexOf("|") + 1,
						data.indexOf("员") + 1);
				String url = data.substring(0, data.indexOf("|"));
				String numbermonth = data.substring(data.indexOf("会员|") + 3,
						data.indexOf("个"));
				String textmonth = data.substring(data.indexOf("个"),
						data.indexOf("个") + 2);
				String givejianli = data.substring(data.indexOf("月|") + 2,
						data.indexOf("|￥"));
				String numbermoney = data.substring(data.indexOf("￥"),
						data.indexOf("元"));
				String textmoney = data.substring(data.indexOf("元"),
						data.indexOf("元") + 1);
				LinearLayout vip = viewHolder.getView(R.id.lin_monthvip);
				TextView tv_numbermonth = viewHolder
						.getView(R.id.tv_numbermonth);
				TextView tv_textmonth = viewHolder.getView(R.id.tv_textmonth);
				TextView tv_givejianli = viewHolder.getView(R.id.tv_givejianli);
				TextView tv_numbermoney = viewHolder
						.getView(R.id.tv_numbermoney);
				TextView tv_textmoney = viewHolder.getView(R.id.tv_textmoney);
				viewHolder.setImage(R.id.img_vip, url);
				viewHolder.setText(R.id.tv_viptype, vipType);
				tv_textmonth.setText(textmonth);
				tv_numbermonth.setText(numbermonth);
				tv_givejianli.setText(givejianli);
				tv_numbermoney.setText(numbermoney);
				tv_textmoney.setText(textmoney);
				if (position == getSelectedPosition()) {
					vip.setBackgroundResource(R.drawable.shape_openvip);
					tv_numbermonth.setTextColor(Color.parseColor("#FF3499"));
					tv_textmonth.setTextColor(Color.parseColor("#FF3499"));
					tv_givejianli.setTextColor(Color.parseColor("#FF3499"));
					tv_numbermoney.setTextColor(Color.parseColor("#FF3499"));
					tv_textmoney.setTextColor(Color.parseColor("#FF3499"));
				} else {
					vip.setBackgroundResource(R.drawable.shape_start);
					tv_numbermonth.setTextColor(Color.parseColor("#000000"));
					tv_textmonth.setTextColor(Color.parseColor("#000000"));
					tv_givejianli.setTextColor(Color.parseColor("#000000"));
					tv_numbermoney.setTextColor(Color.parseColor("#000000"));
					tv_textmoney.setTextColor(Color.parseColor("#000000"));
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								showAdapter.setSelectedPosition(position);
								showAdapter.notifyDataSetChanged();
								BitmapUtils bitmapUtils = new BitmapUtils(
										OpenZhaopinbaoPersonActivity.this);
								bitmapUtils.display(
										img_topvip,
										t.getMonthsName().substring(0,
												t.getMonthsName().indexOf("|"))
												+ "_thumb.png");
								tv_toptype.setText(t.getMonthsName().substring(
										t.getMonthsName().indexOf("|") + 1,
										t.getMonthsName().indexOf("员") + 1));
								tv_kaitongfeiyong
										.setText(t
												.getMonthsName()
												.substring(
														t.getMonthsName()
																.indexOf("￥"),
														t.getMonthsName()
																.indexOf("元") + 1));
								tv_allfeiyong
										.setText(t
												.getMonthsName()
												.substring(
														t.getMonthsName()
																.indexOf("￥"),
														t.getMonthsName()
																.indexOf("元") + 1));
								month = t.getMonthsName().substring(
										t.getMonthsName().indexOf("会员|") + 3,
										t.getMonthsName().indexOf("个"));
								inviteNum = t.getMonthsName().substring(
										t.getMonthsName().indexOf("送") + 1,
										t.getMonthsName().indexOf("份"));
								money = t.getMoney();

							}
						});
			}
		};
		listview.setAdapter(showAdapter);
	}

	@Override
	public void bindListener() {
		img_data.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		lin_data.setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {

		ReqBase resp = (ReqBase) arg0;
		LogUtil.e("000000", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(resp.getBody().get("data"))
					&& resp.getBody().get("data").toString().length() > 2) {
				PayOrderGroupEntity payOrdergroup = (PayOrderGroupEntity) JsonUtil
						.jsonToObj(PayOrderGroupEntity.class, resp.getBody()
								.get("data").toString());
				Bundle b = new Bundle();
				b.putSerializable("MkPayOrders", (Serializable) payOrdergroup);
				startActivityForResult(new Intent(
						OpenZhaopinbaoPersonActivity.this,
						PayStyleActivity.class).putExtras(b), reqCodeTwo);
			} else {
				ToastUtil.showShortToast("下单失败！");
			}
			break;
		case reqCodeTwo:
			if (resp.getBody().get("company").toString().length() > 2) {
				String json = resp.getBody().get("company").toString();
				companyinfocountentity = (CompanyInfoCountEntity) JsonUtil
						.jsonToObj(CompanyInfoCountEntity.class, json);
			}
			tv_starttime.setText(companyinfocountentity.getEndVipDate());
			img_data.setEnabled(false);
			lin_data.setEnabled(false);
			break;
		default:
			break;

		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

		switch (requestCode) {
		case reqCodeOne:
		case reqCodeTwo:
			ReqBase resp = (ReqBase) arg0;
			if (resp != null && resp.getHeader() != null) {
				final CommonDialog commonDialog = new CommonDialog(
						OpenZhaopinbaoPersonActivity.this, "提示", resp
								.getHeader().getRetMessage(), "确定");
				commonDialog.setClicklistener(new ClickListenerInterface() {

					@Override
					public void doConfirm() {
						commonDialog.dismiss();
					}

					@Override
					public void doCancel() {
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_data:
		case R.id.img_data:
			new TimeSelectDialog(OpenZhaopinbaoPersonActivity.this,
					new TimeSelectDialog.CallBackListener() {
						@Override
						public void CallBackOfTimeString(String time) {
							tv_starttime.setText(time);
						}
					}).show();
			break;
		case R.id.btn_ok:
			tiJiao();
			break;
		default:
			break;
		}
	}

	// 提交订单
	public void tiJiao() {
		if (Tool.isEmpty(tv_starttime.getText().toString())) {
			ToastUtil.showShortToast("请选择开始日期");
			return;
		}
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
		map.put("sourceType", 3);
		map.put("companyId", AppContext.getInstance().getUserInfo()
				.getCompanyEntity().getId());
		map.put("startVipDate", tv_starttime.getText().toString());
		map.put("month", month);
		map.put("inviteNum", inviteNum);
		map.put("money", money);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PAY_ORDER));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_APIPAY, req);
		Log.d("wangke", map + "");
	}

	private void getinformation() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getCompanyEntity())) {
			map.put("companyId", AppContext.getInstance().getUserInfo()
					.getCompanyEntity().getId());
		} else {
			map.put("companyId", -1);
		}
		req.setHeader(new ReqHead(AppConfig.ADDPUBLISHCITY_INFORMATION_90064));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("wangke", "————" + map);
		httpPost(reqCodeTwo, AppConfig.EMPLOY_REQUEST_MAPPING, req);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == reqCodeTwo) {
				sendBroadcast(new Intent(
						BroadCastAction.ACTION_RECRUIT_ADD_CITY));
				setResult(RESULT_OK);
				finish();
			}
		}
	}

}
