package com.meiku.dev.ui.im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.FriendEntity;
import com.meiku.dev.bean.PhoneNums;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.ViewHolder;

public class NewFriendActivity extends BaseActivity {
	private PullToRefreshListView mPullToRefreshListView;
	private CommonAdapter<FriendEntity> showAdapter;
	private List<FriendEntity> showList = new ArrayList<FriendEntity>();
	/** 联系人号码 */
	private ArrayList<String> mContactsNumber;
	/** 所有手机通讯录列表数据 */
	private ArrayList<PhoneNums> list;
	/** 获取库Phone表字段 */
	private static final String[] PHONES_PROJECTION = new String[] {
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.NUMBER,
			ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
	/** 联系人显示名称 */
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	/** 电话号码 */
	private static final int PHONES_NUMBER_INDEX = 1;
	private TextView right_txt_title;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_new_friend;
	}

	@Override
	public void initView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(NewFriendActivity.this,
						AddFriendActivity.class);
				startActivity(intent);

			}
		});

		initPullListView();
	}

	@Override
	public void initValue() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		downRefreshData();
	}

	private void initPullListView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		mPullToRefreshListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						downRefreshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						upRefreshData();
					}
				});
		// 适配器
		showAdapter = new CommonAdapter<FriendEntity>(this,
				R.layout.activity_newfriend_item, showList) {
			@Override
			public void convert(final ViewHolder viewHolder,
					final FriendEntity t) {
				viewHolder.setText(R.id.name, t.getAliasName());
				LinearLayout layout_addImage = viewHolder
						.getView(R.id.layout_addImage);
				layout_addImage.removeAllViews();
				MyRectDraweeView iv_head = new MyRectDraweeView(
						NewFriendActivity.this);
				layout_addImage.addView(iv_head, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv_head.setUrlOfImage(t.getClientThumbHeadPicUrl());
				viewHolder.setText(R.id.phonelx, "");
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										NewFriendActivity.this,
										PersonShowActivity.class);
								intent.putExtra(
										PersonShowActivity.TO_USERID_KEY,
										showList.get(viewHolder.getPosition())
												.getUserId() + "");

								intent.putExtra("nickName",
										showList.get(viewHolder.getPosition())
												.getAliasName());
								startActivity(intent);
							}
						});
				if (t.getDelStatus() == 0) {
					viewHolder.setText(R.id.guanzhu, "关注了你");
				} else {
					viewHolder.setText(R.id.guanzhu, "未关注");
				}
				viewHolder.getConvertView().setOnLongClickListener(
						new OnLongClickListener() {

							@Override
							public boolean onLongClick(View arg0) {
								final CommonDialog commonDialog = new CommonDialog(
										NewFriendActivity.this, "提示",
										"是否删除该关注你的好友", "确定", "取消");
								commonDialog.show();
								commonDialog
										.setClicklistener(new ClickListenerInterface() {

											@Override
											public void doConfirm() {
												delectData(t.getUserId());
												commonDialog.dismiss();
											}

											@Override
											public void doCancel() {
												commonDialog.dismiss();
											}
										});
								return false;
							}
						});
			}
		};
		mPullToRefreshListView.setAdapter(showAdapter);
	}

	// 上拉加载更多数据
	private void upRefreshData() {
		getdate();
	}

	// 下拉重新刷新页面
	private void downRefreshData() {
		showList.clear();
		getdate();
	}

	@Override
	public void bindListener() {
	}

	public void getdate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_SEARCH_NEWFRIEND));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.e(reqBase.getBody().toString());
		switch (requestCode) {
		case reqCodeOne:
			if (!Tool.isEmpty(reqBase.getBody().get("friend"))
					&& reqBase.getBody().get("friend").toString().length() > 2) {
				try {
					List<FriendEntity> data = (List<FriendEntity>) JsonUtil
							.jsonToList(reqBase.getBody().get("friend")
									.toString(),
									new TypeToken<List<FriendEntity>>() {
									}.getType());
					if (Tool.isEmpty(data)) {
						ToastUtil.showShortToast("无更多好友请求");
					} else {
						showList.addAll(data);
					}
				} catch (Exception e) {
					LogUtil.d("error:", e.getMessage());
				}
			}
			showAdapter.notifyDataSetChanged();
			mPullToRefreshListView.onRefreshComplete();
			break;
		case reqCodeTwo:
			downRefreshData();
			break;
		default:
			break;
		}
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.onRefreshComplete();
		}
		switch (reqCodeOne) {
		case reqCodeOne:
			ToastUtil.showShortToast("获取失败");
			break;
		case reqCodeTwo:
			ToastUtil.showShortToast("新的朋友删除失败");
			break;
		default:
			break;
		}

		if (null != mPullToRefreshListView) {
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	/**
	 * 得到手机通讯录联系人信息
	 **/
	private void getPhoneContacts() {
		ContentResolver resolver = NewFriendActivity.this.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				PhoneNums phonenum = new PhoneNums();// 实例化
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				if (phoneNumber.contains("-")) {
					phoneNumber = phoneNumber.replace("-", "");
				}
				if (phoneNumber.contains("+86")) {
					phoneNumber = phoneNumber.replace("+86", "");
				}
				if (phoneNumber.contains("+")) {
					phoneNumber = phoneNumber.replace("+", "");
				}
				String isPhoneNum = phoneNumber.substring(0, 1);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber) || !"1".equals(isPhoneNum)
						|| phoneNumber.length() != 11)
					continue;

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				if (TextUtils.isEmpty(contactName))
					continue;
				// 循环添加------------------------------------------
				mContactsNumber.add(phoneNumber);
				String characters = contactName;
				String phone_number = phoneNumber;
				phonenum.setUser_name(characters);
				phonenum.setPhone_number(phone_number);
				list.add(phonenum);
				if (Build.VERSION.SDK_INT < 14) {
					phoneCursor.close();
				}
			}

		}
		getFriendDataFromPhoneBook();
	}

	/**
	 * 得到SIM卡联系人信息
	 **/
	private void getPhoneContactsFromSIMCard() {
		ContentResolver resolver = this.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
				null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				PhoneNums phonenum = new PhoneNums();// 实例化
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// Sim卡中没有联系人头像

				mContactsNumber.add(phoneNumber);
				String characters = contactName;
				phonenum.setUser_name(characters);
				phonenum.setPhone_number(phoneNumber);
				list.add(phonenum);
			}
			if (Build.VERSION.SDK_INT < 14) {
				phoneCursor.close();
			}
		}
		getFriendDataFromPhoneBook();
	}

	/**
	 * 通过接口匹配通讯录
	 */
	private void getFriendDataFromPhoneBook() {
		String phones = "";
		// (通讯录号码，多个逗号分隔)
		for (int i = 0, size = mContactsNumber.size(); i < size; i++) {
			phones += mContactsNumber.get(i);
			if (i != size - 1) {
				phones += ",";
			}
		}
	}

	/**
	 * 通过 联系人电话 得到联系人姓名s
	 **/

	public static String getContactName(Context context, String number) {
		Cursor c = null;
		try {
			c = context
					.getContentResolver()
					.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							new String[] {
									ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
									ContactsContract.CommonDataKinds.Phone.NUMBER },
							null, null, null);
			if (c != null && c.moveToFirst()) {
				while (!c.isAfterLast()) {
					if (PhoneNumberUtils.compare(number, c.getString(1))) {
						return c.getString(0);
					}
					c.moveToNext();
				}
			}
		} catch (Exception e) {

		} finally {
			if (c != null) {
				c.close();
			}
		}
		return null;
	}

	public void delectData(int friendId) {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		map.put("friendId", friendId);
		req.setHeader(new ReqHead(AppConfig.BUSSINESS_IM_DELETE_GUANZHU));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeTwo, AppConfig.PUBLICK_NEARBY_GROUP, req);
	}
}
