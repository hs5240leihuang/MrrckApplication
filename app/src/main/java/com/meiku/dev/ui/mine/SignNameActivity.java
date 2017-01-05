package com.meiku.dev.ui.mine;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.ui.activitys.BaseActivity;
import com.meiku.dev.utils.InputTools;
import com.meiku.dev.utils.ToastUtil;
import com.meiku.dev.utils.Tool;
import com.meiku.dev.views.ViewHolder;

/**
 * 个性签名
 * 
 */
public class SignNameActivity extends BaseActivity {
	private TextView ok;
	private List<String> list = new ArrayList<String>();
	private CommonAdapter<String> commonAdapter;
	private String signname;
	private EditText nameEditText;
	private ListView listView;

	@Override
	protected int getCurrentLayoutID() {
		return R.layout.activity_signname;
	}

	@Override
	public void initView() {
		ok = (TextView) findViewById(R.id.cancel_text);
		nameEditText = (EditText) findViewById(R.id.et_msg_search);
		listView = (ListView) findViewById(R.id.listsignname);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void initValue() {
		signname = getIntent().getStringExtra("signname");
		ok.setVisibility(View.VISIBLE);
		ok.setText("确定");
		ok.setTextColor(getResources().getColor(R.color.white));
		ok.setBackgroundResource(R.drawable.nvxing);
		nameEditText.setCompoundDrawables(null, null, null, null);
		nameEditText.setHint("");
		nameEditText.setText(signname);
		nameEditText.setSelection(signname.length());
		list.add("我期望通过手艺交同行朋友");
		list.add("我期望找到特牛逼的美容师切磋手艺");
		list.add("我期望找到女王型的老板带我成长");
		list.add("我期望找到有灵性的徒弟接班");
		list.add("我期望找到有耐心的师傅教我手艺");
		list.add("我期望找到吃苦耐劳的员工共创明天");
		list.add("我期望找到成人之美、其乐无穷的环境");
		init();

	}

	@Override
	public void bindListener() {
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputTools.HideKeyboard(nameEditText);
				if (Tool.isEmpty(nameEditText.getText().toString().trim())) {
					ToastUtil.showShortToast("请填写个性签名");
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("signname", nameEditText.getText().toString()
						.trim());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public <T> void onSuccess(int requestCode, T arg0) {
	}

	@Override
	public <T> void onFailed(int requestCode, T arg0) {
	}

	public void init() {
		commonAdapter = new CommonAdapter<String>(this,
				R.layout.item_listsignname, list) {

			@Override
			public void convert(final ViewHolder viewHolder, String t) {
				viewHolder.setText(R.id.textsignname,
						list.get(viewHolder.getPosition()));
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								signname = list.get(viewHolder.getPosition());
								nameEditText.setText(signname);
								nameEditText.setSelection(nameEditText
										.getText().length());
							}
						});
			}
		};
		listView.setAdapter(commonAdapter);
	}
}
