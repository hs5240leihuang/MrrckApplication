package com.meiku.dev.ui.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.bean.AttachmentListDTO;
import com.meiku.dev.bean.FormFileBean;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.bean.PersonalPhotoEntity;
import com.meiku.dev.bean.PersonalTagEntity;
import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.bean.ReqHead;
import com.meiku.dev.bean.UserHomeEntity;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.BroadCastAction;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.ui.login.BindPhoneActivity;
import com.meiku.dev.ui.morefun.SelectPictureActivity;
import com.meiku.dev.ui.morefun.SelectPositionActivity;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.PictureUtil;
import com.meiku.dev.utils.PinyinUtil;
import com.meiku.dev.utils.PreferHelper;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.CommonDialog;
import com.meiku.dev.views.CommonDialog.ClickListenerInterface;
import com.meiku.dev.views.CommonEditDialog;
import com.meiku.dev.views.CommonEditDialog.EditClickOkListener;
import com.meiku.dev.views.FlowLayout;
import com.meiku.dev.views.MyGridView;
import com.meiku.dev.views.MyRectDraweeView;
import com.meiku.dev.views.MySimpleDraweeView;
import com.meiku.dev.views.TimeSelectDialog;
import com.meiku.dev.views.ViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * 编辑个人资料
 * 
 */
public class EditPersonalInfoActivity extends BaseActivity {
	private final int reqintroduce = 1;
	private final int reqlabel = 2;
	private final int reqBindPhone = 3;

	private final int reqCodeFive = 500;
	private final int reqCodeSix = 600;
	private LinearLayout lin_head;
	private LinearLayout birthdayLayout, linintroduce;
	private TextView birthdayTextView, tv_hornor;
	private LinearLayout layout_hornor;
	private TextView positionTextView, tv_phone, introduceTextView;
	private ImageView confirmButton;
	private RadioGroup sexGroup, marryGroup;
	private MkUser mkUser = AppContext.getInstance().getUserInfo();
	private String position = "";
	private String gender = ""; // 1男 2女
	private String marryFlag = "";// 0未婚 1已婚
	private int year, month, day;
	private List<String> picPathList = new ArrayList<String>();// 选择的图片路径
	private UserHomeEntity userHomeEntity;
	protected final int reqCode_add = 93;
	private List<FormFileBean> formFileBeans;
	private FormFileBean formFile;
	private TextView tv_nickName;// 昵称
	private TextView tv_realName;// 真实姓名
	private TextView tv_loves;// 爱好
	private TextView tv_citys;// 出没地
	private TextView tv_remark;// 个人说明
	protected String personalTag;
	private FlowLayout layout_tags;
	private boolean hasSetHeadImage = false;
	private MyGridView gridview_myPhotos;
	private CommonAdapter<PersonalPhotoEntity> adapter_myPhotos;
	private List<PersonalPhotoEntity> data_myPhotos = new ArrayList<PersonalPhotoEntity>();
	private LinearLayout layout_myPhotos;
	private String photoPath;
	private MyRectDraweeView headImageView;
	private ScrollView scrollView;
	private LinearLayout layout_bindphone;
	private TextView tv_bindphone;

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_edit_personal_info;
	}

	@Override
	public void initView() {
		TextView right_txt_title = (TextView) findViewById(R.id.right_txt_title);
		right_txt_title.setBackgroundDrawable(null);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		layout_tags = (FlowLayout) findViewById(R.id.layout_tags);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_hornor = (TextView) findViewById(R.id.tv_hornor);
		linintroduce = (LinearLayout) findViewById(R.id.linintroduce);
		lin_head = (LinearLayout) findViewById(R.id.lin_head);
		tv_nickName = (TextView) findViewById(R.id.tv_nickName);
		tv_realName = (TextView) findViewById(R.id.tv_realName);
		birthdayLayout = (LinearLayout) findViewById(R.id.layout_birthday);
		birthdayTextView = (TextView) findViewById(R.id.tv_birthday);
		positionTextView = (TextView) findViewById(R.id.tv_position);
		introduceTextView = (TextView) findViewById(R.id.tv_introduce);
		sexGroup = (RadioGroup) findViewById(R.id.group_sex);
		marryGroup = (RadioGroup) findViewById(R.id.group_marry);
		confirmButton = (ImageView) findViewById(R.id.right_res_title);
		layout_hornor = (LinearLayout) findViewById(R.id.layout_hornor);
		tv_loves = (TextView) findViewById(R.id.tv_loves);
		tv_citys = (TextView) findViewById(R.id.tv_citys);
		tv_remark = (TextView) findViewById(R.id.tv_remark);

		layout_myPhotos = (LinearLayout) findViewById(R.id.layout_myPhotos);
		gridview_myPhotos = (MyGridView) findViewById(R.id.gridview_myPhotos);
		adapter_myPhotos = new CommonAdapter<PersonalPhotoEntity>(
				EditPersonalInfoActivity.this,
				R.layout.item_persionshow_myshow, data_myPhotos) {
			@Override
			public void convert(ViewHolder viewHolder, PersonalPhotoEntity t) {
				// viewHolder.setImageWithNewSize(R.id.img,
				// t.getClientThumbFileUrl(), 150, 150);
				LinearLayout lin_img = viewHolder.getView(R.id.lin_img);
				MySimpleDraweeView img = new MySimpleDraweeView(
						EditPersonalInfoActivity.this);
				lin_img.removeAllViews();
				lin_img.addView(img, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				img.setUrlOfImage(t.getClientThumbFileUrl());
				viewHolder.getView(R.id.tv).setVisibility(View.GONE);
			}
		};
		gridview_myPhotos.setAdapter(adapter_myPhotos);
		gridview_myPhotos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivityForResult(new Intent(
						EditPersonalInfoActivity.this,
						PersonPhotoActivity.class).putExtra("userId",
						AppContext.getInstance().getUserInfo().getId() + ""),
						reqCodeThree);
			}
		});
		// gridview_myPhotos
		// .setOnTouchInvalidPositionListener(new
		// OnTouchInvalidPositionListener() {
		//
		// @Override
		// public boolean onTouchInvalidPosition(int motionEvent) {
		// startActivityForResult(new Intent(
		// EditPersonalInfoActivity.this,
		// PersonPhotoActivity.class).putExtra("userId",
		// AppContext.getInstance().getUserInfo().getId()
		// + ""), reqCodeThree);
		// return false;
		// }
		// });
		layout_bindphone = (LinearLayout) findViewById(R.id.layout_bindphone);
		tv_bindphone = (TextView) findViewById(R.id.tv_bindphone);
		if (!Tool.isEmpty(AppContext.getInstance().getUserInfo().getPhone())) {
			layout_bindphone.setVisibility(View.GONE);
		}
	}

	@Override
	public void initValue() {
		getdate();
		headImageView = new MyRectDraweeView(EditPersonalInfoActivity.this);
		lin_head.removeAllViews();
		lin_head.addView(headImageView, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		headImageView.setUrlOfImage(mkUser.getClientThumbHeadPicUrl());
		if (!Tool.isEmpty(mkUser.getClientThumbHeadPicUrl())) {
			hasSetHeadImage = true;
		}
		tv_nickName.setText(mkUser.getNickName());
		tv_realName.setText(mkUser.getRealName());
		birthdayTextView.setText(mkUser.getBirthDate());
		if (!Tool.isEmpty(mkUser.getBirthDate())) {
			year = Integer.parseInt(mkUser.getBirthDate().substring(0, 4));
			month = Integer.parseInt(mkUser.getBirthDate().substring(5, 7));
			day = Integer.parseInt(mkUser.getBirthDate().substring(8, 10));
		}
		if (mkUser.getGender().equals("1")) {
			((RadioButton) findViewById(R.id.btn_gender_male)).setChecked(true);
			gender = "1";
		} else {
			((RadioButton) findViewById(R.id.btn_gender_female))
					.setChecked(true);
			gender = "2";
		}
		positionTextView.setText(mkUser.getPositionName());
		if (mkUser.getMarryFlag() == 1) {
			((RadioButton) findViewById(R.id.btn_marry)).setChecked(true);
			marryFlag = "1";
		} else {
			((RadioButton) findViewById(R.id.btn_unmarried)).setChecked(true);
			marryFlag = "0";
		}

		tv_citys.setText(mkUser.getAppearPlace());
		tv_loves.setText(mkUser.getHobby());

		if (Tool.isEmpty(mkUser.getIntroduce())) {
			introduceTextView.setText("我期望通过手艺交同行朋友");
		} else {
			introduceTextView.setText(mkUser.getIntroduce());
		}
		position = mkUser.getPosition() + "";
		tv_remark.setText(mkUser.getRemark());
		tv_phone.setText(mkUser.getPhone());
		findViewById(R.id.goback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				exitTip();
			}
		});
	}

	@Override
	public void bindListener() {
		findViewById(R.id.layout_selectImage).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(
								EditPersonalInfoActivity.this,
								SelectPictureActivity.class);
						intent.putExtra("SELECT_MODE",
								SelectPictureActivity.MODE_SINGLE);// 选择模式
						intent.putExtra("MAX_NUM", 1);// 选择的张数
						startActivityForResult(intent, reqCodeTwo);
					}
				});
		if (0 != year) {
			birthdayLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					new TimeSelectDialog(EditPersonalInfoActivity.this,
							new TimeSelectDialog.CallBackListener() {
								@Override
								public void CallBackOfTimeString(String time) {
									birthdayTextView.setText(time);
								}
							}, year, month, day).show();
				}
			});
		} else {
			birthdayLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					new TimeSelectDialog(EditPersonalInfoActivity.this,
							new TimeSelectDialog.CallBackListener() {
								@Override
								public void CallBackOfTimeString(String time) {
									birthdayTextView.setText(time);
								}
							}).show();
				}
			});
		}

		findViewById(R.id.layout_position).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivityForResult(new Intent(
								EditPersonalInfoActivity.this,
								SelectPositionActivity.class), reqCodeOne);
					}
				});

		sexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.btn_gender_male) {
					gender = "1";
				} else {
					gender = "2";
				}
			}
		});

		marryGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.btn_marry) {
					marryFlag = "1";
				} else {
					marryFlag = "0";
				}
			}
		});

		confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsReady()) {
					doConfirm();
				}
			}
		});
		layout_hornor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(EditPersonalInfoActivity.this,
						HornorActivity.class);
				it.putExtra("personalTag", personalTag);
				startActivityForResult(it, reqlabel);

			}
		});
		linintroduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(EditPersonalInfoActivity.this,
						SignNameActivity.class);
				intent.putExtra("signname", introduceTextView.getText()
						.toString());
				startActivityForResult(intent, reqintroduce);
			}
		});
		// 填写昵称
		findViewById(R.id.layout_nickname).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						new CommonEditDialog(EditPersonalInfoActivity.this,
								"昵称", "请填写昵称",
								tv_nickName.getText().toString(), 10, true,
								new EditClickOkListener() {

									@Override
									public void doConfirm(String inputString) {
										tv_nickName.setText(inputString);
									}
								}).show();
					}
				});
		// 填写真实姓名
		findViewById(R.id.layout_raleName).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						new CommonEditDialog(EditPersonalInfoActivity.this,
								"真实姓名", "请填写真实姓名", tv_realName.getText()
										.toString(), 10, true,
								new EditClickOkListener() {

									@Override
									public void doConfirm(String inputString) {
										tv_realName.setText(inputString);
									}
								}).show();
					}
				});
		// 填写出没地
		findViewById(R.id.layout_citys).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						new CommonEditDialog(EditPersonalInfoActivity.this,
								"常出没地", "请填写常出没地", tv_citys.getText()
										.toString(), 100, false,
								new EditClickOkListener() {

									@Override
									public void doConfirm(String inputString) {
										tv_citys.setText(inputString);
									}
								}).show();
					}
				});
		// 填写兴趣爱好
		findViewById(R.id.layout_loves).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						new CommonEditDialog(EditPersonalInfoActivity.this,
								"兴趣爱好", "请填写兴趣爱好", tv_loves.getText()
										.toString(), 500, false,
								new EditClickOkListener() {

									@Override
									public void doConfirm(String inputString) {
										tv_loves.setText(inputString);
									}
								}).show();
					}
				});
		// 填写个人说明
		findViewById(R.id.layout_remark).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						new CommonEditDialog(EditPersonalInfoActivity.this,
								"个人说明", "请填写个人说明", tv_remark.getText()
										.toString(), 500, false,
								new EditClickOkListener() {

									@Override
									public void doConfirm(String inputString) {
										tv_remark.setText(inputString);
									}
								}).show();
					}
				});
		layout_myPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(
						EditPersonalInfoActivity.this,
						PersonPhotoActivity.class).putExtra("userId",
						AppContext.getInstance().getUserInfo().getId() + ""),
						reqCodeThree);
			}
		});
		layout_bindphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Tool.isEmpty(AppContext.getInstance().getUserInfo()
						.getPhone())) {
					startActivityForResult(new Intent(
							EditPersonalInfoActivity.this,
							BindPhoneActivity.class), reqBindPhone);// 绑定手机
				}

			}
		});
	}

	protected boolean checkIsReady() {
		if (!hasSetHeadImage) {
			ToastUtil.showShortToast("请上传头像");
			return false;
		} else if (Tool.isEmpty(tv_nickName.getText().toString())) {
			ToastUtil.showShortToast("昵称不能为空");
			return false;
		}
		// else if (Tool.isEmpty(birthdayTextView.getText().toString())) {
		// ToastUtil.showShortToast("生日不能为空");
		// return false;
		// }
		else if (Tool.isEmpty(positionTextView.getText().toString())) {
			ToastUtil.showShortToast("岗位不能为空");
			return false;
		} else if (Tool.isEmpty(AppContext.getInstance().getUserInfo()
				.getPhone())) {
			ToastUtil.showShortToast("请完善注册号");
		}
		return true;
	}

	private void doConfirm() {
		ReqBase reqBase = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		String nickName = tv_nickName.getText().toString().trim();
		map.put("nickName", nickName);
		map.put("realName", tv_realName.getText().toString());
		map.put("gender", gender);
		map.put("birthDate", birthdayTextView.getText().toString());
		map.put("position", position);
		// map.put("marryFlag", marryFlag);
		map.put("introduce", introduceTextView.getText().toString());
		map.put("remark", tv_remark.getText().toString());
		map.put("appearPlace", tv_citys.getText().toString());
		map.put("hobby", tv_loves.getText().toString());
		map.put("personalTag", personalTag);
		String sortString = PinyinUtil.getTerm(nickName);
		map.put("nameFirstChar", sortString.matches("[A-Z]") ? sortString : "#");
		map.put("longitude", MrrckApplication.getInstance().getLongitudeStr());
		map.put("latitude", MrrckApplication.getInstance().getLaitudeStr());
		map.put("leanCloudUserName", AppContext.getInstance().getUserInfo()
				.getLeanCloudUserName());
		LogUtil.d("hl", "uploadInfo==>" + map);
		reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_USER_PERSONALINFO_NEW));
		reqBase.setBody(JsonUtil.String2Object(JsonUtil.hashMapToJson(map)));
		Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();

		if (null != picPathList && picPathList.size() > 0) {
			List<FormFileBean> formFileBeans = new ArrayList<FormFileBean>();
			FormFileBean formFile = new FormFileBean(new File(
					picPathList.get(0)), "photo.png");
			formFileBeans.add(formFile);
			mapFileBean.put("photo", formFileBeans);
		} else {
			mapFileBean.put("photo", new ArrayList<FormFileBean>());
		}

		// uploadFiles(reqCodeThree, AppConfig.USER_REQUEST_MAPPING,
		// mapFileBean, reqBase, true);
		if (tv_nickName.getText().toString().length() <= 0) {
			ToastUtil.showShortToast("昵称不能为空");
		} else {
			uploadFiles(reqCodeThree, AppConfig.USER_REQUEST_MAPPING,
					mapFileBean, reqBase, true);
		}

	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
		ReqBase reqBase = (ReqBase) arg0;
		LogUtil.d("hl", "==>" + reqBase.getBody().toString());
		switch (requestCode) {
		case reqCodeThree:
			LogUtil.e(reqBase.getBody().toString());
			if (JsonUtil.JSON_TYPE.JSON_TYPE_ERROR != JsonUtil
					.getJSONType(reqBase.getBody().get("user").toString())) {
				MkUser userbean = (MkUser) JsonUtil.jsonToObj(MkUser.class,
						reqBase.getBody().get("user").toString());
				AppContext.getInstance().setLocalUser(userbean);
				ToastUtil.showShortToast("修改成功");
				setResult(RESULT_OK);
				sendBroadcast(new Intent(BroadCastAction.ACTION_PERFECT_INFO));// 发广播取消首页底部提示
				sendBroadcast(new Intent(BroadCastAction.ACTION_CHANGE_AVATAR));// 头像又改变需要刷新其他位置的头像
				finish();
			}
			break;
		case reqCodeFour:
			if ((reqBase.getBody().get("user") + "").length() > 2) {
				userHomeEntity = (UserHomeEntity) JsonUtil.jsonToObj(
						UserHomeEntity.class, reqBase.getBody().get("user")
								.toString());
				if (userHomeEntity == null) {
					ToastUtil.showShortToast("未获取到详情数据");
					finish();
					return;
				}
				data_myPhotos.clear();
				data_myPhotos.addAll(userHomeEntity.getPersonalPhotoList());
				adapter_myPhotos.notifyDataSetChanged();
				personalTag = userHomeEntity.getPersonalTag();
				layout_tags.removeAllViews();
				for (int i = 0, size = userHomeEntity.getPersonalTagList()
						.size(); i < size; i++) {
					creatOneTags(userHomeEntity.getPersonalTagList().get(i));
				}
				showTagsOrSelectTxt(!Tool.isEmpty(personalTag));
				// initphotoGrid();
			}
			scrollView.scrollTo(0, 0);
			break;
		case reqCodeFive:
			ToastUtil.showShortToast("删除成功");
			getdate();
			break;
		case reqCodeSix:
			getdate();
			break;
		default:
			break;
		}
	}

	private void addPersionTags(String personalTagIds) {
		String[] tagIds = personalTagIds.split(",");
		String allTagGroupsStr = (String) PreferHelper.getSharedParam(
				"PERSION_TAGS", "");
		List<PersonalTagEntity> allTagGroupsData = (List<PersonalTagEntity>) JsonUtil
				.jsonToList(allTagGroupsStr,
						new TypeToken<List<PersonalTagEntity>>() {
						}.getType());
		if (!Tool.isEmpty(allTagGroupsData)) {
			layout_tags.removeAllViews();
			for (int i = 0, tagNum = tagIds.length; i < tagNum; i++) {
				for (int j = 0, size = allTagGroupsData.size(); j < size; j++) {
					for (int k = 0, num = allTagGroupsData.get(j)
							.getPersonalList().size(); k < num; k++) {
						if (tagIds[i].equals(allTagGroupsData.get(j)
								.getPersonalList().get(k).getId()
								+ "")) {
							creatOneTags(allTagGroupsData.get(j)
									.getPersonalList().get(k));
						}
					}
				}
			}
		}
	}

	/**
	 * 添加一个标签
	 * 
	 * @param oneTypeData
	 */
	private void creatOneTags(PersonalTagEntity tagData) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, ScreenUtil.dip2px(this, 3),
				ScreenUtil.dip2px(this, 12), ScreenUtil.dip2px(this, 3));
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
		layout_tags.addView(mTextView);
		LogUtil.d("hl", "add tag= " + tagData.getTagName());
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
		switch (requestCode) {
		case reqCodeThree:
			ToastUtil.showShortToast("修改失败");
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.d("hl", "onActivityResult--requestCode==" + requestCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case reqCodeOne:
				positionTextView.setText(data.getStringExtra("name"));
				position = data.getIntExtra("id", 0) + "";
				break;
			case reqCodeTwo:
				List<String> pics = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPics = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				if (pics != null) {// 多选图片返回
					for (int i = 0; i < pics.size(); i++) {
						CompressPic(reqCodeTwo, pics.get(i));
					}

				} else {// 拍照返回
					photoPath = data.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					if (!Tool.isEmpty(photoPath)) {
						if (ConstantKey.USE_PIC_CUT) {
							Uri uri = Uri.fromFile(new File(photoPath));
							cropImageUri(uri, 200, 200, reqCodeFour);
						} else {
							CompressPic(reqCodeTwo, photoPath);
						}
					} else {
						ToastUtil.showShortToast("获取图片失败");
					}
				}
				break;
			case reqintroduce:
				introduceTextView.setText(data.getStringExtra("signname"));
				break;
			case reqCode_add:
				List<String> pictrue = data
						.getStringArrayListExtra(SelectPictureActivity.EXTRA_RESULT);
				List<AttachmentListDTO> mrrckPictrue = data
						.getParcelableArrayListExtra("Mrrck_Album_Result");
				if (!Tool.isEmpty(pictrue)) {// 多选图片返回
					for (int i = 0; i < pictrue.size(); i++) {
						CompressPic(reqCode_add, pictrue.get(i));
					}

				} else {// 拍照返回
					String photoPath = data
							.getStringExtra(ConstantKey.KEY_PHOTO_PATH);
					CompressPic(reqCode_add, photoPath);
				}
				break;
			case reqlabel:
				personalTag = data.getStringExtra("personalTag");
				showTagsOrSelectTxt(!Tool.isEmpty(personalTag));
				if (!Tool.isEmpty(personalTag)) {
					addPersionTags(personalTag);
				}
				break;
			case reqCodeThree:
				getdate();
				setResult(RESULT_OK);
				break;
			case reqCodeFour:
				CompressPic(reqCodeTwo, photoPath);
				break;
			case reqBindPhone:
				tv_bindphone.setText(AppContext.getInstance().getUserInfo()
						.getPhone());
				break;
			default:
				break;
			}
		}
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 显示标签，还是“请选择”这几个字
	 * 
	 * @param showTags
	 */
	private void showTagsOrSelectTxt(boolean showTags) {
		if (showTags) {
			tv_hornor.setVisibility(View.GONE);
			layout_tags.setVisibility(View.VISIBLE);
		} else {
			layout_tags.setVisibility(View.GONE);
			tv_hornor.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 压缩图片转圈
	 * 
	 * @param reqcode
	 * 
	 * @param photoPath
	 */
	private void CompressPic(int reqcode, String photoPath) {
		showProgressDialog("图片压缩中...");
		new MyAsyncTask(reqcode).execute(photoPath);
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {

		private int reqcode;

		public MyAsyncTask(int reqcode) {
			this.reqcode = reqcode;
		}

		@Override
		protected void onPostExecute(String result) {
			switch (reqcode) {
			case reqCodeTwo:
				picPathList.clear();
				picPathList.add(result);
				headImageView.setUrlOfImage("file://"
						+ picPathList.get(0).toString());
				hasSetHeadImage = true;
				LogUtil.d("hl", "压缩后处理__" + result);
				dismissProgressDialog();
				break;
			case reqCode_add:
				ReqBase reqBase = new ReqBase();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("moduleType", 22);
				map.put("moduleId", AppContext.getInstance().getUserInfo()
						.getId());
				map.put("userId", AppContext.getInstance().getUserInfo()
						.getId());
				reqBase.setHeader(new ReqHead(
						AppConfig.BUSINESS_SHANGCHUAN_PHOTO));
				reqBase.setBody(JsonUtil.Map2JsonObj(map));
				Map<String, List<FormFileBean>> mapFileBean = new HashMap<String, List<FormFileBean>>();
				formFileBeans = new ArrayList<FormFileBean>();
				formFile = new FormFileBean(new File(result),
						System.currentTimeMillis() + ".png");
				formFileBeans.add(formFile);
				mapFileBean.put("photo", formFileBeans);
				uploadFiles(reqCodeSix, AppConfig.PUBLIC_REQUEST_MAPPING,
						mapFileBean, reqBase, true);
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(String... arg0) {
			String photoPath = PictureUtil.save(arg0[0]);// 压缩并另存图片
			LogUtil.d("hl", "返回拍照路径压缩__" + photoPath);
			return photoPath;
		}

	}

	public void getdate() {
		ReqBase req = new ReqBase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", AppContext.getInstance().getUserInfo().getId());
		req.setHeader(new ReqHead(AppConfig.BUSINESS_PHOTO_NEW));
		req.setBody(JsonUtil.Map2JsonObj(map));
		httpPost(reqCodeFour, AppConfig.PERSONAL_REQUEST_MAPPING, req);
	}

	// // 添加我的秀场请求
	// public void addgetdata() {
	// ReqBase reqBase = new ReqBase();
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("moduleType", 22);
	// map.put("moduleId", AppContext.getInstance().getUserInfo().getId());
	// map.put("userId", AppContext.getInstance().getUserInfo().getId());
	// reqBase.setHeader(new ReqHead(AppConfig.BUSINESS_SHANGCHUAN_PHOTO));
	// reqBase.setBody(JsonUtil.Map2JsonObj(map));
	// httpPost(reqCodeSix, AppConfig.PUBLIC_REQUEST_MAPPING, reqBase);
	// }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitTip();
		}
		return true;
	}

	private void exitTip() {
		final CommonDialog commonDialog = new CommonDialog(this, "提示",
				"是否放弃本次编辑", "确定", "取消");
		commonDialog.setClicklistener(new ClickListenerInterface() {

			@Override
			public void doConfirm() {
				commonDialog.dismiss();
				finish();
			}

			@Override
			public void doCancel() {
				// TODO Auto-generated method stub
				commonDialog.dismiss();
			}
		});
		commonDialog.show();
	}
}
