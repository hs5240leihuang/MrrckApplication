package com.meiku.dev.ui.chat;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiku.dev.R;
import com.meiku.dev.adapter.PhoneBookAdapter;
import com.meiku.dev.bean.FriendInformation;
import com.meiku.dev.bean.PhoneNums;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ClearEditText;
import com.meiku.dev.views.EnLetterView;
import com.meiku.dev.views.TopTitleBar;

/**
 * 添加通讯录朋友 Created by huanglei on 2015/9/28.
 */
public class AddFriendsFromPhoneBookActivity extends BaseActivity {
	/** 标题栏 */
	private TopTitleBar topTitle;
	/** 搜索框 */
	private ClearEditText msgSearch;
	/** 字母提示 */
	TextView dialogTextView;
	/** 右侧字母导航栏 */
	EnLetterView rightLetter;
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
	/** 头像ID */
	private static final int PHONES_PHOTO_ID_INDEX = 2;
	/** 联系人的ID */
	private static final int PHONES_CONTACT_ID_INDEX = 3;

	static final int GB_SP_DIFF = 160;
	// 存放国标一级汉字不同读音的起始区位码
	static final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302,
			2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
			4086, 4390, 4558, 4684, 4925, 5249, 5600 };
	// 存放国标一级汉字不同读音的起始区位码对应读音
	static final char[] firstLetter = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'W', 'X',
			'Y', 'Z' };
	/** 联系人号码 */
	private ArrayList<String> mContactsNumber;
	/** 适配器 */
	private PhoneBookAdapter friendsListAdapter;
	private ListView friendsList;
	/** 拼音排序比较器 */
	private static PinyinComparator pinyinComparator;
	/** 所有手机通讯录列表数据 */
	private ArrayList<PhoneNums> list;
	/** 默认头像 */
	private Bitmap defaultBmp;
	/** 匹配后的列表数据 */
	public static ArrayList<PhoneNums> newlist = new ArrayList<PhoneNums>();
	// public static ArrayList<PhoneNums> newlist = new ArrayList<PhoneNums>();
	/** 无数据提示 */
	private View emptyView;

	@Override
	protected void onResume() {
		super.onResume();
		getAllContacts();
	}

	/**
	 * 获取所有联系人
	 */
	private void getAllContacts() {
		list = new ArrayList<PhoneNums>();
		mContactsNumber = new ArrayList<String>();
		getPhoneContacts();
		getPhoneContactsFromSIMCard();
	}

	private void initListView() {
		friendsList = (ListView) findViewById(R.id.list_friends);
		friendsListAdapter = new PhoneBookAdapter(this);
		friendsList.setAdapter(friendsListAdapter);
	}

	/**
	 * 请求添加好友
	 * 
	 * @param index
	 */
	private void gotoAddFriend(int index) {
		// Intent intent = new Intent(AddFriendsFromPhoneBookActivity.this,
		// com.meiku.dev.ui.activitys.personal.AddFriendsActivity.class);
		// intent.putExtra("headUrl", newlist.get(index).getHeadPicUrl());
		// intent.putExtra("name", newlist.get(index).getNickName());
		// intent.putExtra("sign", "");
		// intent.putExtra("leanCloudId", newlist.get(index).getLeanCloudId());
		// startActivity(intent);
	}

	private void initRightLetterViewAndSearchEdit() {
		rightLetter.setTextView(dialogTextView);
		rightLetter
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}

	private class LetterListViewListener implements
			EnLetterView.OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(String s) {
			int position = friendsListAdapter
					.getPositionForSection(s.charAt(0));
			if (position != -1) {
				friendsList.setSelection(position);
			}
		}
	}

	/**
	 * 得到手机通讯录联系人信息
	 **/
	private void getPhoneContacts() {
		try {
			ContentResolver resolver = AddFriendsFromPhoneBookActivity.this
					.getContentResolver();
			// 获取手机联系人
			Cursor phoneCursor = resolver.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					PHONES_PROJECTION, null, null, null);
			if (phoneCursor != null) {
				while (phoneCursor.moveToNext()) {
					PhoneNums phonenum = new PhoneNums();// 实例化
					// 得到手机号码
					String phoneNumber = phoneCursor
							.getString(PHONES_NUMBER_INDEX);
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
					if (TextUtils.isEmpty(phoneNumber)
							|| !"1".equals(isPhoneNum)
							|| phoneNumber.length() != 11)
						continue;

					// 得到联系人名称
					String contactName = phoneCursor
							.getString(PHONES_DISPLAY_NAME_INDEX);

					if (TextUtils.isEmpty(contactName))
						continue;

					// 得到联系人ID
					Long contactid = phoneCursor
							.getLong(PHONES_CONTACT_ID_INDEX);

					// 得到联系人头像ID
					Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

					// 得到联系人头像Bitamp
					Bitmap contactPhoto = null;
					// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
					if (photoid > 0) {
						Uri uri = ContentUris.withAppendedId(
								ContactsContract.Contacts.CONTENT_URI,
								contactid);
						InputStream input = ContactsContract.Contacts
								.openContactPhotoInputStream(resolver, uri);
						contactPhoto = BitmapFactory.decodeStream(input);
					} else {
						contactPhoto = defaultBmp;
					}
					// 循环添加------------------------------------------

					String characters = contactName;
					String spells = PinyinUtil.getTerm(characters);
					String initial = "";
					if (spells.length() > 0) {
						initial = spells.substring(0, 1);
					}
					String phone_number = phoneNumber;
					phonenum.setUser_name(characters);
					phonenum.setIcon(contactPhoto);
					phonenum.setPhone_number(phone_number);
					phonenum.setInitial(initial);
					list.add(phonenum);
					Log.v("hl", contactName + phone_number + "__" + initial);
					mContactsNumber.add(phoneNumber);
					if (Build.VERSION.SDK_INT < 14) {
						phoneCursor.close();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到SIM卡联系人信息
	 **/
	private void getPhoneContactsFromSIMCard() {
		try {

			ContentResolver resolver = this.getContentResolver();
			// 获取Sims卡联系人
			Uri uri = Uri.parse("content://icc/adn");
			Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null,
					null, null);

			if (phoneCursor != null) {
				while (phoneCursor.moveToNext()) {
					PhoneNums phonenum = new PhoneNums();// 实例化
					// 得到手机号码
					String phoneNumber = phoneCursor
							.getString(PHONES_NUMBER_INDEX);
					// 当手机号码为空的或者为空字段 跳过当前循环
					if (TextUtils.isEmpty(phoneNumber))
						continue;
					// 得到联系人名称
					String contactName = phoneCursor
							.getString(PHONES_DISPLAY_NAME_INDEX);

					// Sim卡中没有联系人头像

					mContactsNumber.add(phoneNumber);

					String characters = contactName;
					String spells = PinyinUtil.getTerm(characters);
					String initial = "";
					if (spells.length() > 0) {
						initial = spells.substring(0, 1);
					}
					phonenum.setUser_name(characters);
					phonenum.setIcon(defaultBmp);
					phonenum.setPhone_number(phoneNumber);
					phonenum.setInitial(initial);
					Log.v("hl", "SIM卡——" + contactName + phoneNumber + "__"
							+ initial);
					list.add(phonenum);
				}
				if (Build.VERSION.SDK_INT < 14) {
					phoneCursor.close();
				}
			}
			getFriendDataFromPhoneBook();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Log.v("hl", phones);
		// GroupDataLogic.getInstance().getPhonesData(phones,
		// AddFriendsFromPhoneBookActivity.this);
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getUserId());
		map.put("phone", AppContext.getInstance().getUserInfo().getPhone());
		map.put("phones", phones);
		reqBase.setHeader(new ReqHead(AppConfig.BUSSINESS_CHECK_PHONEBOOK));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		httpPost(reqCodeOne, AppConfig.PUBLICK_NEARBY_GROUP, reqBase, true);
	}

	static char convert(byte[] bytes) {
		char result = '-';
		int secPosValue = 0;
		int i;
		for (i = 0; i < bytes.length; i++) {
			bytes[i] -= GB_SP_DIFF;
		}
		secPosValue = bytes[0] * 100 + bytes[1];
		for (i = 0; i < 23; i++) {
			if (secPosValue >= secPosValueList[i]
					&& secPosValue < secPosValueList[i + 1]) {
				result = firstLetter[i];
				break;
			}
		}
		return result;
	}

	/**
	 * 排序
	 */
	public class PinyinComparator implements Comparator<PhoneNums> {
		public int compare(PhoneNums o1, PhoneNums o2) {
			if (o1.getInitial().equals("@") || o2.getInitial().equals("#")) {
				return -1;
			} else if (o1.getInitial().equals("#")
					|| o2.getInitial().equals("@")) {
				return 1;
			} else {
				return o1.getInitial().compareTo(o2.getInitial());
			}
		}
	}

	/**
	 * 通过 联系人电话 得到联系人姓名
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

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_add_friendsfromphonebook;
	}

	@Override
	public void initView() {
		pinyinComparator = new PinyinComparator();
		defaultBmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.user_icon);
		// pDialog = showSpinnerDialog();
		topTitle = (TopTitleBar) findViewById(R.id.title_bar);
		msgSearch = (ClearEditText) findViewById(R.id.et_msg_search);
		emptyView = (View) findViewById(R.id.emptyView);
		msgSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// startActivity(new
				// Intent(AddFriendsFromPhoneBookActivity.this,
				// InputSearchPhoneBookActivity.class));
			}
		});

		dialogTextView = (TextView) findViewById(R.id.dialog);
		rightLetter = (EnLetterView) findViewById(R.id.right_letter);
		initListView();
		initRightLetterViewAndSearchEdit();
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindListener() {
		friendsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(
						AddFriendsFromPhoneBookActivity.this,
						PersonShowActivity.class);
				intent.putExtra(PersonShowActivity.TO_USERID_KEY,
						newlist.get(arg2).getUser_id() + "");
				intent.putExtra("nickName", newlist.get(arg2).getNickName());
				startActivity(intent);
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		switch (requestCode) {
		case reqCodeOne:
			LogUtil.e(reqBase.getBody().toString());
			newlist.clear();
			try {
				JsonObject jsonObject = reqBase.getBody();
				String str = jsonObject.get("userPhone").toString();
				Type type = new TypeToken<List<FriendInformation>>() {
				}.getType();
				if (!Tool.isEmpty(str) && str.length() > 2) {
					List<FriendInformation> pplist = (List<FriendInformation>) JsonUtil
							.jsonToList(str, type);
					for (int i = 0, size = pplist.size(); i < size; i++) {
						for (int j = 0, allsize = list.size(); j < allsize; j++) {

							if (list.get(j).getPhone_number()
									.equals(pplist.get(i).getPhone())) {
								// 填充部分字段
								FriendInformation finfo = pplist.get(i);
								PhoneNums pn = list.get(j);
								pn.setUser_id(finfo.getUserId() + "");
								pn.setNickName(finfo.getNickName());
								pn.setIsFriend(finfo.getIsFriend());
								pn.setHeadPicUrl(finfo.getClientHeadPicUrl());
								newlist.add(pn);
							}
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Collections.sort(newlist, pinyinComparator);
			friendsListAdapter.setData(newlist);
			emptyView.setVisibility(newlist.size() == 0 ? View.VISIBLE
					: View.GONE);
			break;
		default:
			break;
		}

	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (defaultBmp != null && !defaultBmp.isRecycled()) {
			defaultBmp.recycle();
			defaultBmp = null;
		}
	}

}