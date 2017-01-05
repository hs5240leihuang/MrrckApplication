package com.meiku.dev.views;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.utils.MessageObs;
import com.meiku.dev.utils.MessageObs.MessageSizeChageListener;
import com.meiku.dev.utils.ScreenUtil;
import com.meiku.dev.yunxin.recent.DropFake;
import com.meiku.dev.yunxin.recent.DropManager;

@SuppressLint("NewApi")
public class IMTitleTabsLayout {
	private TranslateAnimation animation;
	private List<String> titletabList = new ArrayList<String>();
	private ArrayList<Integer> titletabsIndexs = new ArrayList<Integer>();
	/**
	 * 每个Titletab的宽度
	 */
	private int titleTabWidth;
	private Context context;
	private LinearLayout selectBg;
	private LinearLayout TabGroups;
	public int currentIndex;
	private IMTabClickListener listener;
	private View view;
	// private TextView messageNumTV;
	private DropFake unreadTV;

	public IMTitleTabsLayout(Context context, List<String> titletabList,
			IMTabClickListener tabClickListener) {
		this.context = context;
		this.titletabList = titletabList;
		this.listener = tabClickListener;
		init();
	}

	public View getView() {
		return view;
	}

	public interface IMTabClickListener {
		void onOneTabClick(int index);
	}

	private void init() {
		registerUnreadMessageChageListener();
		view = LayoutInflater.from(context).inflate(R.layout.view_imtitletabs,
				null, false);
		TabGroups = (LinearLayout) view.findViewById(R.id.TabGroups);
		selectBg = (LinearLayout) view.findViewById(R.id.selectBg);
	}

	public void setChildView(int layoutWidth) {
		int tabSizes = titletabList.size();
		titleTabWidth = layoutWidth / tabSizes;
		selectBg.setLayoutParams(new RelativeLayout.LayoutParams(titleTabWidth,
				ScreenUtil.dip2px(context, 50)));
		for (int i = 0; i < tabSizes; i++) {
			addOneTitleTab(i, titletabList.get(i));
			titletabsIndexs.add(titleTabWidth * i);
		}
		SetAllTitleClo(0);
	}

	private void registerUnreadMessageChageListener() {
		MessageObs.getInstance().registerListener(
				new MessageSizeChageListener() {

					@Override
					public void onMsgSizeChange(int unreadMessageSize) {
						// if (messageNumTV != null) {
						// messageNumTV.setText(unreadMessageSize > 99 ? "99+"
						// : unreadMessageSize + "");
						// messageNumTV
						// .setVisibility(unreadMessageSize > 0 ? View.VISIBLE
						// : View.GONE);
						// messageNumTV.setTag(unreadMessageSize + "");
						// }
						if (unreadTV != null) {
							unreadTV.setText(unreadMessageSize > 99 ? "99+"
									: unreadMessageSize + "");
							unreadTV.setVisibility(unreadMessageSize > 0 ? View.VISIBLE
									: View.GONE);
						}
					}
				});
	}

	/**
	 * title tab的选中穿梭动画
	 * 
	 * @param toIndex
	 */
	private void startAnim(int toIndex) {
		animation = new TranslateAnimation(titletabsIndexs.get(currentIndex),
				titletabsIndexs.get(toIndex), 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(200);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				SetAllTitleClo(currentIndex);
			}
		});
		selectBg.startAnimation(animation);
	}

	/**
	 * 添加一个TitleTab
	 */
	private void addOneTitleTab(int position, String titleName) {
		View tabitem = LayoutInflater.from(context).inflate(
				R.layout.view_imtitleitem, null, false);
		TextView tv = (TextView) tabitem.findViewById(R.id.txt);
		TextView tv_messageNum = (TextView) tabitem
				.findViewById(R.id.message_dian);
		if (position == 1) {
			// messageNumTV = tv_messageNum;
			// messageNumTV.setOnTouchListener(new OnTouchListener() {
			//
			// @SuppressLint("NewApi") @Override
			// public boolean onTouch(View v, MotionEvent arg1) {
			// ClipData.Item item = new ClipData.Item((CharSequence) v
			// .getTag());
			// String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
			// ClipData dragData = new ClipData(v.getTag().toString(),
			// mimeTypes, item);
			// View.DragShadowBuilder myShadow = new DragShadowBuilder(
			// messageNumTV);
			// v.startDrag(dragData, // the data to be dragged
			// myShadow, // the drag shadow builder
			// null, // no need to use local data
			// 0 // flags (not currently used, set to 0)
			// );
			// messageNumTV.setVisibility(View.GONE);
			// return false;
			// }
			// });
			// tabitem.setOnDragListener(new OnDragListener() {
			// private boolean isDragOutside;
			//
			// @Override
			// public boolean onDrag(View v, DragEvent event) {
			// switch (event.getAction()) {
			// case DragEvent.ACTION_DRAG_ENTERED:
			// isDragOutside = false;
			// break;
			// case DragEvent.ACTION_DRAG_EXITED:
			// isDragOutside = true;
			// break;
			// case DragEvent.ACTION_DRAG_ENDED:
			// messageNumTV.setVisibility(isDragOutside ? View.GONE
			// : View.VISIBLE);
			// if (isDragOutside) {
			// MsgDataBase.getInstance().delNoReadNotice();
			// context.sendBroadcast(new Intent(
			// BroadCastAction.ACTION_IM_REFRESH_MESSAGE_PAGE));// 发广播刷新消息页面
			// }
			// break;
			// default:
			// break;
			// }
			// return true;
			// }
			// });

			unreadTV = ((DropFake) tabitem.findViewById(R.id.tab_new_msg_label));
//			unreadTV.setShowType(1);
			if (unreadTV != null) {
				unreadTV.setClickListener(new DropFake.ITouchListener() {
					@Override
					public void onDown() {
						DropManager.getInstance().setCurrentId("0");
						DropManager.getInstance().getDropCover()
								.down(unreadTV, unreadTV.getText());
					}

					@Override
					public void onMove(float curX, float curY) {
						DropManager.getInstance().getDropCover()
								.move(curX, curY);
					}

					@Override
					public void onUp() {
						DropManager.getInstance().getDropCover().up();
					}
				});
			}
		}
		LayoutParams params = new LinearLayout.LayoutParams(titleTabWidth,
				LayoutParams.FILL_PARENT);
		tabitem.setLayoutParams(params);
		tv.setText(titleName);
		tv.setTextColor(context.getResources().getColor(R.color.col_999999));
		tv.setOnClickListener(new TitleClick(position));
		TabGroups.addView(tabitem);
	}

	/**
	 * title tab 点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class TitleClick implements OnClickListener {

		private int position;

		public TitleClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			SetSelecetedIndex(position);
		}

	}

	public void SetSelecetedIndex(int index) {
		if (currentIndex != index) {
			setTitleTabTextClo(index, R.color.col_999999);
			listener.onOneTabClick(index);
			startAnim(index);
			currentIndex = index;
		}
	}

	/**
	 * 设置所有title tab文字颜色
	 * 
	 * @param selectedIndex
	 */
	public void SetAllTitleClo(int selectedIndex) {
		for (int i = 0, size = TabGroups.getChildCount(); i < size; i++) {
			if (i == selectedIndex) {
				setTitleTabTextClo(i, R.color.clo_ff3499);
			} else {
				setTitleTabTextClo(i, R.color.col_999999);
			}
		}
	}

	/**
	 * 设置某个childview的文字颜色
	 * 
	 * @param childIndex
	 * @param clo
	 */
	private void setTitleTabTextClo(int childIndex, int clo) {
		RelativeLayout layout = (RelativeLayout) TabGroups
				.getChildAt(childIndex);
		((TextView) layout.getChildAt(0)).setTextColor(context.getResources()
				.getColor(clo));
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * 直接选中
	 * 
	 * @param toIndex
	 */
	public void selectDirectlt(int toIndex) {
		SetAllTitleClo(toIndex);
		animation = new TranslateAnimation(titletabsIndexs.get(currentIndex),
				titletabsIndexs.get(toIndex), 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		selectBg.startAnimation(animation);
	}
}
