package com.meiku.dev.yunxin;

import java.util.Map;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meiku.dev.R;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.ui.mine.PersonShowActivity;
import com.meiku.dev.utils.DateTimeUtil;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.views.MyRoundDraweeView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 会话窗口消息列表项的ViewHolder基类，负责每个消息项的外层框架，包括头像，昵称，发送/接收进度条，重发按钮等。<br>
 * 具体的消息展示项可继承该基类，然后完成具体消息内容展示即可。
 */
public abstract class MsgViewHolderBase extends TViewHolder {

	protected IMMessage message;

	protected View alertButton;
	protected TextView timeTextView;
	protected ProgressBar progressBar;
	protected TextView nameTextView;
	protected FrameLayout contentContainer;
	protected LinearLayout nameContainer;
	protected TextView readReceiptTextView;

	private LinearLayout avatarLeft;
	private LinearLayout avatarRight;

	public ImageView nameIconView;

	// contentContainerView的默认长按事件。如果子类需要不同的处理，可覆盖onItemLongClick方法
	// 但如果某些子控件会拦截触摸消息，导致contentContainer收不到长按事件，子控件也可在inflate时重新设置
	protected View.OnLongClickListener longClickListener;

	// / -- 以下接口可由子类覆盖或实现
	// 返回具体消息类型内容展示区域的layout res id
	abstract protected int getContentResId();

	// 在该接口中根据layout对各控件成员变量赋值
	abstract protected void inflateContentView();

	// 将消息数据项与内容的view进行绑定
	abstract protected void bindContentView();

	// 内容区域点击事件响应处理。
	protected void onItemClick() {
	}

	// 内容区域长按事件响应处理。该接口的优先级比adapter中有长按事件的处理监听高，当该接口返回为true时，adapter的长按事件监听不会被调用到。
	protected boolean onItemLongClick() {
		return false;
	}

	// 当是接收到的消息时，内容区域背景的drawable id
	protected int leftBackground() {
		return R.drawable.nim_message_item_left_selector;
	}

	// 当是发送出去的消息时，内容区域背景的drawable id
	protected int rightBackground() {
		return R.drawable.nim_message_item_right_selector;
	}

	// 返回该消息是不是居中显示
	protected boolean isMiddleItem() {
		return false;
	}

	// 是否显示头像，默认为显示
	protected boolean isShowHeadImage() {
		return true;
	}

	// 是否显示气泡背景，默认为显示
	protected boolean isShowBubble() {
		return true;
	}

	// / -- 以下接口可由子类调用
	// 获取MsgAdapter对象
	protected final MsgAdapter getAdapter() {
		return (MsgAdapter) adapter;
	}

	/**
	 * 下载附件/缩略图
	 */
	protected void downloadAttachment() {
		if (message.getAttachment() != null
				&& message.getAttachment() instanceof FileAttachment)
			NIMClient.getService(MsgService.class).downloadAttachment(message,
					true);
	}

	// 设置FrameLayout子控件的gravity参数
	protected final void setGravity(View view, int gravity) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view
				.getLayoutParams();
		params.gravity = gravity;
	}

	// 设置控件的长宽
	protected void setLayoutParams(int width, int height, View... views) {
		for (View view : views) {
			ViewGroup.LayoutParams maskParams = view.getLayoutParams();
			maskParams.width = width;
			maskParams.height = height;
			view.setLayoutParams(maskParams);
		}
	}

	// 根据layout id查找对应的控件
	protected <T extends View> T findViewById(int id) {
		return (T) view.findViewById(id);
	}

	// 判断消息方向，是否是接收到的消息
	protected boolean isReceivedMessage() {
		return message.getDirect() == MsgDirectionEnum.In;
	}

	// 判断消息方向，是否是群消息
	protected boolean isTeamMessage() {
		return message.getSessionType() == SessionTypeEnum.Team;
	}

	// / -- 以下是基类实现代码
	@Override
	protected final int getResId() {
		return R.layout.nim_message_item;
	}

	@Override
	protected final void inflate() {
		timeTextView = findViewById(R.id.message_item_time);
		avatarLeft = findViewById(R.id.message_item_portrait_left);
		avatarRight = findViewById(R.id.message_item_portrait_right);
		alertButton = findViewById(R.id.message_item_alert);
		progressBar = findViewById(R.id.message_item_progress);
		nameTextView = findViewById(R.id.message_item_nickname);
		contentContainer = findViewById(R.id.message_item_content);
		nameIconView = findViewById(R.id.message_item_name_icon);
		nameContainer = findViewById(R.id.message_item_name_layout);
		readReceiptTextView = findView(R.id.textViewAlreadyRead);

		View.inflate(view.getContext(), getContentResId(), contentContainer);
		inflateContentView();
	}

	@Override
	protected final void refresh(Object item) {
		message = (IMMessage) item;
		setHeadAndNameView();
		setTimeTextView();
		setStatus();
		setOnClickListener();
		setLongClickListener();
		setContent();
		setReadReceipt();

		bindContentView();
	}

	public void refreshCurrentItem() {
		if (message != null) {
			refresh(message);
		}
	}

	/**
	 * 设置时间显示
	 */
	private void setTimeTextView() {
		if (getAdapter().needShowTime(message)) {
			timeTextView.setVisibility(View.VISIBLE);
		} else {
			timeTextView.setVisibility(View.GONE);
			return;
		}

		String text = DateTimeUtil.getTimeShowString(message.getTime(), false);
		timeTextView.setText(text);
	}

	/**
	 * 设置消息发送状态
	 */
	private void setStatus() {

		MsgStatusEnum status = message.getStatus();
		switch (status) {
		case fail:
			progressBar.setVisibility(View.GONE);
			alertButton.setVisibility(View.VISIBLE);
			break;
		case sending:
			progressBar.setVisibility(View.VISIBLE);
			alertButton.setVisibility(View.GONE);
			break;
		default:
			progressBar.setVisibility(View.GONE);
			alertButton.setVisibility(View.GONE);
			break;
		}
	}

	private void setHeadAndNameView() {
		LinearLayout show = isReceivedMessage() ? avatarLeft : avatarRight;
		LinearLayout hide = isReceivedMessage() ? avatarRight : avatarLeft;
		hide.setVisibility(View.GONE);
		if (!isShowHeadImage()) {
			show.removeAllViews();
			show.setVisibility(View.GONE);
			return;
		}
		// 先从成员DB取，
		IMYxUserInfo user = MsgDataBase.getInstance().getYXUserById(
				message.getFromAccount());
		Map<String, Object> ext = message.getRemoteExtension();
		if (isMiddleItem()) {
			show.setVisibility(View.GONE);
		} else {
			show.setVisibility(View.VISIBLE);
			MyRoundDraweeView img_head = new MyRoundDraweeView(context);
			show.removeAllViews();
			show.addView(img_head, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			if (isReceivedMessage()) {
				if (user != null) {
					img_head.setUrlOfImage(user.getUserHeadImg());
				} else if (ext != null && ext.containsKey("clientHeadPicUrl")) {
					img_head.setUrlOfImage(ext.get("clientHeadPicUrl")
							.toString());
				}
			} else {// 右边显示自己的头像
				img_head.setUrlOfImage(AppContext.getInstance().getUserInfo()
						.getClientThumbHeadPicUrl());
			}
		}
		// 设置昵称
		if (message.getSessionType() == SessionTypeEnum.Team
				&& isReceivedMessage() && !isMiddleItem()) {// 只显示群聊天昵称
			nameTextView.setVisibility(View.VISIBLE);
			if (ext != null && ext.containsKey("groupId")) {
				int groupId = Integer.parseInt(ext.get("groupId").toString());
				Map<String, String> nickNameMaps = AppContext
						.getGroupMemberNickNameMap().get(groupId);
				if (nickNameMaps != null&&nickNameMaps.containsKey(message.getFromAccount())) {
					nameTextView.setText(nickNameMaps.get(message.getFromAccount()));
					return;
				}
			}

			if (ext != null && ext.containsKey("nickName")) {
				String name = ext.get("nickName").toString();
				nameTextView.setText(name);
			} else {
				nameTextView.setText(user != null ? user.getNickName() : "");
			}
		} else {// 自己昵称不显示
			nameTextView.setVisibility(View.GONE);
		}
	}

	private void setOnClickListener() {
		// 重发/重收按钮响应事件
		if (getAdapter().getEventListener() != null) {
			alertButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					getAdapter().getEventListener().onFailedBtnClick(message);
				}
			});
		}

		// 内容区域点击事件响应， 相当于点击了整项
		contentContainer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemClick();
			}
		});

		// 头像点击事件响应
		avatarLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, Object> ext = message.getRemoteExtension();
				if (ext != null && ext.containsKey("userId")) {
					Intent intent = new Intent(context,
							PersonShowActivity.class);
					intent.putExtra(PersonShowActivity.TO_USERID_KEY,
							ext.get("userId") + "");
					context.startActivity(intent);
				}
			}
		});
		avatarRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, PersonShowActivity.class);
				intent.putExtra(PersonShowActivity.TO_USERID_KEY, AppContext
						.getInstance().getUserInfo().getId()
						+ "");
				context.startActivity(intent);
			}
		});
	}

	/**
	 * item长按事件监听
	 */
	private void setLongClickListener() {
		longClickListener = new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// 优先派发给自己处理，
				if (!onItemLongClick()) {
					if (getAdapter().getEventListener() != null) {
						getAdapter().getEventListener().onViewHolderLongClick(
								contentContainer, view, message);
						return true;
					}
				}
				return false;
			}
		};
		// 消息长按事件响应处理
		contentContainer.setOnLongClickListener(longClickListener);

		// 头像长按事件响应处理
		View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		};
		avatarLeft.setOnLongClickListener(longClickListener);
		avatarRight.setOnLongClickListener(longClickListener);
	}

	private void setContent() {
		if (!isShowBubble() && !isMiddleItem()) {
			return;
		}

		LinearLayout bodyContainer = (LinearLayout) view
				.findViewById(R.id.message_item_body);

		// 调整container的位置
		int index = isReceivedMessage() ? 0 : 3;
		if (bodyContainer.getChildAt(index) != contentContainer) {
			bodyContainer.removeView(contentContainer);
			bodyContainer.addView(contentContainer, index);
		}

		if (isMiddleItem()) {
			setGravity(bodyContainer, Gravity.CENTER);
		} else {
			if (isReceivedMessage()) {
				setGravity(bodyContainer, Gravity.LEFT);
				contentContainer.setBackgroundResource(leftBackground());
			} else {
				setGravity(bodyContainer, Gravity.RIGHT);
				contentContainer.setBackgroundResource(rightBackground());
			}
		}
	}

	private void setReadReceipt() {
		if (!TextUtils.isEmpty(getAdapter().getUuid())
				&& message.getUuid().equals(getAdapter().getUuid())) {
			readReceiptTextView.setVisibility(View.VISIBLE);
		} else {
			readReceiptTextView.setVisibility(View.GONE);
		}
	}
}
