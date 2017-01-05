package com.meiku.dev.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.meiku.dev.R;
import com.meiku.dev.adapter.ChatEmotionPagerAdapter;
import com.meiku.dev.adapter.CommonAdapter;
import com.meiku.dev.utils.EmotionHelper;

/**
 * 表情选择view
 * @author Administrator
 *
 */
public class EmotionView {

	private Context context;
	private ChooseOneEmotionListener listener;
	private ViewPager emotionPager;
	private CommonAdapter<String> showAdapter;
	private View view;
	private IndicatorView indicatorGroup;

	public interface ChooseOneEmotionListener {
		public void doChooseOneEmotion(String s);
	}

	public View getView() {
		return view;
	}

	public EmotionView(Context context, ChooseOneEmotionListener listener) {
		this.context = context;
		this.listener = listener;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.dialog_emotion, null);
		emotionPager = (ViewPager) view.findViewById(R.id.emotionPager);
		indicatorGroup = (IndicatorView) view
				.findViewById(R.id.indicatorGroup);
		initEmotionPager();
	}

	private void initEmotionPager() {
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < EmotionHelper.emojiGroups.size(); i++) {
			views.add(getEmotionGridView(i));
		}
		ChatEmotionPagerAdapter pagerAdapter = new ChatEmotionPagerAdapter(
				views);
		emotionPager.setOffscreenPageLimit(3);
		emotionPager.setAdapter(pagerAdapter);
		emotionPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				indicatorGroup.setSelectedPosition(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		indicatorGroup.setPointCount(context, views.size());
	}

	private View getEmotionGridView(int pos) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View emotionView = inflater.inflate(R.layout.chat_emotion_gridview,
				null, false);
		GridView gridView = (GridView) emotionView.findViewById(R.id.gridview);

		List<String> pageEmotions = EmotionHelper.emojiGroups.get(pos);
		showAdapter = new CommonAdapter<String>(context,
				R.layout.chat_emotion_item, pageEmotions) {

			@Override
			public void convert(ViewHolder viewHolder, String emotion) {
				ImageView emotionImageView = viewHolder
						.getView(R.id.emotionImageView);
				emotion = emotion.substring(1, emotion.length() - 1);
				Bitmap bitmap = EmotionHelper
						.getEmojiDrawable(context, emotion);
				emotionImageView.setImageBitmap(bitmap);
			}

		};
		gridView.setAdapter(showAdapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String emotionText = (String) parent.getAdapter().getItem(
						position);
				listener.doChooseOneEmotion(emotionText);
				// int start = contentEdit.getSelectionStart();
				// StringBuffer sb = new StringBuffer(contentEdit.getText());
				// sb.replace(contentEdit.getSelectionStart(),
				// contentEdit.getSelectionEnd(), emotionText);
				// contentEdit.setText(sb.toString());
				//
				// CharSequence info = contentEdit.getText();
				// if (info instanceof Spannable) {
				// Spannable spannable = (Spannable) info;
				// Selection.setSelection(spannable,
				// start + emotionText.length());
				// }
			}
		});
		return emotionView;
	}
}
