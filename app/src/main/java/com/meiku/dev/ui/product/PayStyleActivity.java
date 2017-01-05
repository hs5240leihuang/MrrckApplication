package com.meiku.dev.ui.product;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.meiku.dev.R;
import com.meiku.dev.bean.PayOrderGroupEntity;
import com.meiku.dev.bean.PayResult;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantLogin;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.recruit.RecruitMainActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.HintDialogwk;
import com.meiku.dev.views.HintDialogwk.ClickListenerInterface;
import com.meiku.dev.views.HintDialogwk.DoOneClickListenerInterface;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 支付结算页面
 * 
 */
public class PayStyleActivity extends BaseActivity implements OnClickListener {
	private LinearLayout ALiPayLayout, WeiXinPayLayout, MrrckPayLayout;
	private ImageView img_zhifubao, img_weixin, img_mrrck;
	private Button btnOK;
	private TextView tv_jiesuan, tv_bianhao, tv_create, tv_name, tv_allmoney,
			tv_xiangqing;
	private int payType;// 0美库余额 ,1支付宝,2微信
	private IWXAPI wxAPI;
	private String name = "";
	private String content = "";
	private TextView tv_mrrckMoney;
	private String mrrckMoney;
	private Float orderAllAmount;
	private PayOrderGroupEntity payOrders;
	private int workType;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_paystyle;
	}

	@Override
	public void initView() {
		regisBroadcast();
		getMrrckMoney();
		wxAPI = WXAPIFactory.createWXAPI(this, ConstantLogin.WECHAT_APPId);
		btnOK = (Button) findViewById(R.id.btnOK);
		img_zhifubao = (ImageView) findViewById(R.id.img_zhifubao);
		img_weixin = (ImageView) findViewById(R.id.img_weixin);
		img_mrrck = (ImageView) findViewById(R.id.img_mrrck);
		ALiPayLayout = (LinearLayout) findViewById(R.id.ALiPayLayout);
		WeiXinPayLayout = (LinearLayout) findViewById(R.id.WeiXinPayLayout);
		MrrckPayLayout = (LinearLayout) findViewById(R.id.MrrckPayLayout);
		tv_jiesuan = (TextView) findViewById(R.id.tv_jiesuan);
		tv_bianhao = (TextView) findViewById(R.id.tv_bianhao);
		tv_create = (TextView) findViewById(R.id.tv_create);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_allmoney = (TextView) findViewById(R.id.tv_allmoney);
		tv_xiangqing = (TextView) findViewById(R.id.tv_xiangqing);
		tv_mrrckMoney = (TextView) findViewById(R.id.tv_mrrckMoney);
		payType = 2;
		img_zhifubao.setBackgroundResource(R.drawable.noselect);
		img_weixin.setBackgroundResource(R.drawable.redselect);
		img_mrrck.setBackgroundResource(R.drawable.noselect);
	}

	@Override
	public void initValue() {
		Bundle b = getIntent().getExtras();
		payOrders = (PayOrderGroupEntity) b.getSerializable("MkPayOrders");

		if (Tool.isEmpty(payOrders)) {
			ToastUtil.showShortToast("获取订单信息失败！");
			finish();
		}
		workType = payOrders.getWorkType();
		orderAllAmount = payOrders.getOrderAllAmount();
		tv_allmoney.setText("¥" + orderAllAmount + " 元");
		tv_jiesuan.setText("" + payOrders.getOrderAllAmount());
		tv_bianhao.setText("" + payOrders.getOrderGroupNumber());
		tv_create.setText("" + payOrders.getCreateDate());
		name = payOrders.getOrderName();
		tv_name.setText(name.replaceAll("%%", "\n"));
		content = payOrders.getOrderContent();
		tv_xiangqing.setText(content.replaceAll("%%", "\n"));
	}

	@Override
	public void bindListener() {
		btnOK.setOnClickListener(this);
		ALiPayLayout.setOnClickListener(this);
		WeiXinPayLayout.setOnClickListener(this);
		MrrckPayLayout.setOnClickListener(this);
		findViewById(R.id.phonNum).setOnClickListener(this);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		final ReqBase resp = (ReqBase) arg0;
		LogUtil.d("hl", "######" + resp.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (payType == 1) {// 1支付宝支付
				if (!Tool.isEmpty(resp.getBody().get("data"))
						&& resp.getBody().get("data").toString().length() > 2) {
					Runnable payRunnable = new Runnable() {

						@Override
						public void run() {
							// 构造PayTask 对象
							PayTask alipay = new PayTask(PayStyleActivity.this);
							// 调用支付接口，获取支付结果
							LogUtil.d("hl",
									"######"
											+ resp.getBody().get("data")
													.getAsString());
							String result = alipay.pay(
									resp.getBody().get("data").getAsString(),
									true);
							Message msg = new Message();
							msg.what = SDK_PAY_FLAG;
							msg.obj = result;
							mHandler.sendMessage(msg);
						}
					};
					// 必须异步调用
					Thread payThread = new Thread(payRunnable);
					payThread.start();
				}
			} else if (payType == 2) {// 2微信支付
				try {
					if (!Tool.isEmpty(resp.getBody().get("data"))
							&& resp.getBody().get("data").toString().length() > 2) {
						Map<String, String> map = JsonUtil.jsonToMap(resp
								.getBody().get("data").toString());
						if (map == null) {
							ToastUtil.showShortToast("获取微信支付参数失败！");
							return;
						}
						PayReq req = new PayReq();
						req.appId = map.get("appid");
						req.partnerId = map.get("partnerid");
						req.prepayId = map.get("prepayid");
						req.nonceStr = map.get("noncestr");
						req.timeStamp = map.get("timestamp");
						req.packageValue = map.get("package");
						req.sign = map.get("sign");
						// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
						wxAPI.registerApp(ConstantLogin.WECHAT_APPId);
						wxAPI.sendReq(req);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (payType == 0) {
				if (workType == 3) {
					if (!Tool.isEmpty(resp) && !Tool.isEmpty(resp.getHeader())) {
						final HintDialogwk commonDialog = new HintDialogwk(
								PayStyleActivity.this, "恭喜你支付成功，加足马力招聘吧！",
								"去招聘");
						commonDialog
								.setOneClicklistener(new DoOneClickListenerInterface() {

									@Override
									public void doOne() {
										commonDialog.dismiss();
										setResult(RESULT_OK);
										finish();
										Intent intent = new Intent(
												PayStyleActivity.this,
												RecruitMainActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent(
												BroadCastAction.ACTION_RECRUIT_CHANGETAB));
									}
								});

						commonDialog.show();
					}
				} else {
					if (!Tool.isEmpty(resp) && !Tool.isEmpty(resp.getHeader())) {
						final HintDialogwk commonDialog = new HintDialogwk(
								PayStyleActivity.this, resp.getHeader()
										.getRetMessage(), "确定");
						commonDialog
								.setOneClicklistener(new DoOneClickListenerInterface() {

									@Override
									public void doOne() {
										commonDialog.dismiss();
										setResult(RESULT_OK);
										finish();

									}
								});

						commonDialog.show();
					}
				}

			}

			// 支付成功 发广播
			sendBroadcast(new Intent(BroadCastAction.ACTION_PAYRESULT));
			break;
		case reqCodeTwo:
			if (resp != null && resp.getHeader() != null
					&& !Tool.isEmpty(resp.getHeader().getRetMessage())) {
				ToastUtil.showShortToast(resp.getHeader().getRetMessage());
			}
			setResult(RESULT_OK);
			finish();
			break;
		case reqCodeThree:
			if (!Tool.isEmpty(resp.getBody().get("amountData"))) {
				mrrckMoney = resp.getBody().get("amountData").toString();
				if (Tool.isEmpty(mrrckMoney)) {
					tv_mrrckMoney.setText("（余额：未知）");
				} else {
					tv_mrrckMoney.setText("（余额：" + mrrckMoney + "元）");
				}
			}
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
			if (!Tool.isEmpty(resp) && !Tool.isEmpty(resp.getHeader())) {
				final HintDialogwk commonDialog = new HintDialogwk(
						PayStyleActivity.this,  resp.getHeader()
								.getRetMessage(), "确定");
				commonDialog
						.setOneClicklistener(new DoOneClickListenerInterface() {

							@Override
							public void doOne() {
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
		case R.id.ALiPayLayout:
			payType = 1;
			img_zhifubao.setBackgroundResource(R.drawable.redselect);
			img_weixin.setBackgroundResource(R.drawable.noselect);
			img_mrrck.setBackgroundResource(R.drawable.noselect);
			break;
		case R.id.WeiXinPayLayout:
			payType = 2;
			img_zhifubao.setBackgroundResource(R.drawable.noselect);
			img_weixin.setBackgroundResource(R.drawable.redselect);
			img_mrrck.setBackgroundResource(R.drawable.noselect);
			break;
		case R.id.MrrckPayLayout:
			payType = 0;
			img_zhifubao.setBackgroundResource(R.drawable.noselect);
			img_weixin.setBackgroundResource(R.drawable.noselect);
			img_mrrck.setBackgroundResource(R.drawable.redselect);
			break;
		case R.id.btnOK:
			if (payType == 1) {// 1支付宝支付
				getThridPayData();
			} else if (payType == 2) {// 2微信支付
				if (wxAPI.isWXAppInstalled()) {
					getThridPayData();
				} else {
					ToastUtil.showShortToast("请安装微信客户端！");
				}
			}
			if (payType == 0) {

				if (Tool.isEmpty(mrrckMoney)
						|| Float.parseFloat(mrrckMoney) < orderAllAmount) {
					ToastUtil.showShortToast("美库余额不足！");
				} else {
					getThridPayData();
				}
			}
			break;
		case R.id.phonNum:
			final CommonDialog commonDialog = new CommonDialog(this, "拨打电话",
					"400-688-6800", "拨打", "取消");
			commonDialog
					.setClicklistener(new CommonDialog.ClickListenerInterface() {
						@Override
						public void doConfirm() {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:4006886800"));
							startActivity(intent);
							commonDialog.dismiss();
						}

						@Override
						public void doCancel() {
							commonDialog.dismiss();
						}
					});
			commonDialog.show();
			break;
		default:
			break;
		}
	}

	// 生成三方订单接口
	public void getThridPayData() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("orderGroupNumber", payOrders.getOrderGroupNumber());
		map.put("payType", payType);
		map.put("clientType", 0); // app端 0 H5端 1 pc端 2
		map.put("orderAmount", payOrders.getOrderAllAmount());
		LogUtil.d("hl", "getThridPayData————" + map);
		req.setHeader(new ReqHead(AppConfig.BUSINESS_THREEDINGDAN));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_APIPAY, req);
	}

	private static final int SDK_PAY_FLAG = 1;

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					ToastUtil.showShortToast("支付成功");
					doCheckPayResult();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.showShortToast("支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						ToastUtil.showShortToast("支付失败");
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * 注册广播
	 */
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadCastAction.ACTION_WX_PAYRESULT);
		registerReceiver(receiver, filter);
	}

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BroadCastAction.ACTION_WX_PAYRESULT.equals(intent.getAction())) {
				doCheckPayResult();
			}
		}
	};

	/**
	 * 支付完毕验证结果
	 */
	protected void doCheckPayResult() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderGroupNumber", payOrders.getOrderGroupNumber());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_CHECKPAYRESULT));
		req.setBody(JsonUtil.Map2JsonObj(map));
		LogUtil.d("hl", "doCheckPayResult————" + map);
		httpPost(reqCodeTwo, AppConfig.PUBLICK_APIPAY, req);
	}

	/**
	 * 查询美库余额
	 */
	protected void getMrrckMoney() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_ZX_22008));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeThree, AppConfig.PUBLICK_APIPAY, req);
	}

}
