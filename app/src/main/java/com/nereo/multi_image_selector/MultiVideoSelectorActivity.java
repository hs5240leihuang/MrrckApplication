package com.nereo.multi_image_selector;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.meiku.dev.R;

/**
 * 多视频选择 Created by Nereo on 2015/4/7.
 */
public class MultiVideoSelectorActivity extends FragmentActivity implements
		MultiVideoSelectorFragment.Callback {

	/** 最大图片选择次数，int类型，默认9 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/** 图片选择模式，默认多选 */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** 是否显示相机，默认显示 */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
	public static final String EXTRA_RESULT = "select_result";
	/** 默认选择集 */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

	/** 单选 */
	public static final int MODE_SINGLE = 0;
	/** 多选 */
	public static final int MODE_MULTI = 1;

	private ArrayList<String> resultList = new ArrayList<String>();
	private TextView mSubmitButton;
	private int mDefaultCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_imageselector);
		((TextView) findViewById(R.id.center_txt_title)).setText("选择视频");
		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
		int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
		boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
		if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
			resultList = intent
					.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
		}

		Bundle bundle = new Bundle();
		bundle.putInt(MultiVideoSelectorFragment.EXTRA_SELECT_COUNT,
				mDefaultCount);
		bundle.putInt(MultiVideoSelectorFragment.EXTRA_SELECT_MODE, mode);
		bundle.putBoolean(MultiVideoSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
		bundle.putStringArrayList(
				MultiVideoSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST,
				resultList);

		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.image_grid,
						Fragment.instantiate(this,
								MultiVideoSelectorFragment.class.getName(),
								bundle)).commit();

		// 返回按钮
		findViewById(R.id.left_res_title).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});

		// 完成按钮
		mSubmitButton = (TextView) findViewById(R.id.right_txt_title);
		if (mode == MODE_MULTI) {
			if (resultList == null || resultList.size() <= 0) {
				mSubmitButton.setText(" 请选择 ");
				mSubmitButton.setEnabled(false);
			} else {
				mSubmitButton.setText("发送(" + resultList.size() + "/"
						+ mDefaultCount + ")");
				mSubmitButton.setEnabled(true);
			}
			mSubmitButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (resultList != null && resultList.size() > 0) {
						// 返回已选择的图片数据
						Intent data = new Intent();
						data.putStringArrayListExtra(EXTRA_RESULT, resultList);
						setResult(RESULT_OK, data);
						finish();
					}
				}
			});
		} else {
			mSubmitButton.setVisibility(View.GONE);
		}

	}

	@Override
	public void onSingleImageSelected(String path) {
		Intent data = new Intent();
		resultList.add(path);
		data.putStringArrayListExtra(EXTRA_RESULT, resultList);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// 有图片之后，改变按钮状态
		if (resultList.size() > 0) {
			mSubmitButton.setText("发送(" + resultList.size() + "/"
					+ mDefaultCount + ")");
			if (!mSubmitButton.isEnabled()) {
				mSubmitButton.setEnabled(true);
			}
		}
	}

	@Override
	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
			mSubmitButton.setText("发送(" + resultList.size() + "/"
					+ mDefaultCount + ")");
		} else {
			mSubmitButton.setText("发送(" + resultList.size() + "/"
					+ mDefaultCount + ")");
		}
		// 当为选择图片时候的状态
		if (resultList.size() == 0) {
			mSubmitButton.setText("请选择");
			mSubmitButton.setEnabled(false);
		}
	}

	@Override
	public void onCameraShot(String path) {
		if (path != null) {
			Intent data = new Intent();
			resultList.add(path);
			data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			setResult(RESULT_OK, data);
			finish();
		}
	}
}
