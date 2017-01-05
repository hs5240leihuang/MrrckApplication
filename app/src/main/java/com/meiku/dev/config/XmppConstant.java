package com.meiku.dev.config;


/**
 * 即时通讯的常量定义
 * 
 * @author 库
 * 
 */
public class XmppConstant {

	/** 消息对象传值的KEY */
	public static final String IMMESSAGE_KEY = "immessage.key";
	public static final String KEY_TIME = "immessage.time";
	public static final String KEY_PACKETID = "immessage.packetID";
	public static final String KEY_RESULT = "immessage.result";

	// 自定义的通讯数据表表名
	/**云信用户信息表 */
	public static final String IM_YX_USER_INFO = "mrrck_tb_msgyx_userinfo"; 
	
	/** 聊天记录表 */
	public static final String IM_DRAFT_TABLE_NAME = "mrrck_tb_draft";

	public final static int MSGTYPE_RCV = 0; // 消息接收
	public final static int MSGTYPE_SEND = 1; // 消息发送
	public final static int MSGTYPE_SYSTEM = 2; // 系统消息

	/** 传送的内容类型 0=文字；1=图片；2=语音 ；3位置；4视频；5名片；6分享 */
	public final static int MSG_CONTENT_TEXT = 0;
	public final static int MSG_CONTENT_IMG = 1;
	public final static int MSG_CONTENT_VOICE = 2;
	public final static int MSG_CONTENT_LOCATION = 3;
	public final static int MSG_CONTENT_VIDEO = 4;
	public final static int MSG_CONTENT_CARD = 5;
	public final static int MSG_CONTENT_SHARE = 6;

	/** 新版本支持URL形式的图片视频 ，0=不是新版本;1=图片；2=视频 */
	public final static int MSG_FILE_URL_NO = 0;
	public final static int MSG_FILE_URL_IMG = 1;
	public final static int MSG_FILE_URL_VIDEO = 2;

	/** 已读和未读 */
	public static final int NOTICE_READ = 0;
	public static final int NOTICE_UNREAD = 1;

	/** 已播和未播 */
	public static final int VOICE_PLAYED = 0;
	public static final int VOICE_NOTPLAY = 1;

	/** 接收到的通知类型 */
//	public static final int NOTICE_ADD_FRIEND = 1;// 好友请求
//	public static final int NOTICE_SYS_MSG = 2; // 系统消息
	public static final int NOTICE_CHAT_MSG = 3;// 聊天消息

	/** 接收到的系统通知类型 */
	public static final int SYSTEM_TYPE_DISMISS = 1;// 解散群
	public static final int SYSTEM_TYPE_ADD = 2; // 拉人进群
	public static final int SYSTEM_TYPE_KICK = 3;// 踢人出群

	/** 接收的文件名称前缀 */
	public static final String MSG_FILE_VOICE = "record";// 聊语音
	public static final String MSG_FILE_IMG = "image";// 图片
	public static final String MSG_FILE_VIDEO = "video";// 视频

	/** 上传消息文件的聊天类型 ，0=单聊;1=群聊；2=聊天室 */
	public final static int MSG_FILE_SINGLE = 0;
	public final static int MSG_FILE_GROUP = 1;
	public final static int MSG_FILE_MULROOM = 2;

	/** 语音文字位置字符串常量 */
	public static final String CHAT_MSG_VOICE = "[语音]";
	public static final String CHAT_MSG_IMAGE = "[图片]";
	public static final String CHAT_MSG_LOCATION = "[位置]";
	public static final String CHAT_MSG_VIDEO = "[视频]";
	public static final String CHAT_MSG_CARD = "[名片]";
	public static final String CHAT_MSG_SHARE = "[分享]";

	public static final String CONTENT_TIP_IMG = "[图片]消息，请下载新版本查看(http://t.cn/Rt9dHln)";
	public static final String CONTENT_TIP_VIDEO = "[视频]消息，请下载新版本查看(http://t.cn/Rt9dHln)";

	/** 消息服务器状态 */
	public final static int STATUS_CONTENT_SUCCESS = 100;
	public final static int STATUS_CONTENT_ERRORCOUNT = 401;// 错误的用户名或密码
	public final static int STATUS_CONTENT_ECXEPTION = 404;// 无法连接到消息服务器

	/** 群组最大人数 */
	public final static String IM_GROUP_MAXUSERS = "1000";
	public final static String IM_GROUP_INVITE_REASON = "加入我们群吧";

	/** 当前消息记录属于聊天群 */
	public final static int IM_CHAT_IS_ROOM = 1;// 属于聊天群的消息记录
	public final static int IM_CHAT_NO_ROOM = 0;// 属于单聊的消息记录

	/** 聊天室类型 */
	public final static int IM_CHAT_TALKTYPE_SINGLE = 0;// 好友单聊
	public final static int IM_CHAT_TALKTYPE_MULTITALK = 1;// 多人聊天室
	public final static int IM_CHAT_TALKTYPE_GROUPTALK = 2;// 群组聊天室

	/** 接收到的通知类型 */
	public static final int MESSAGE_RESULT_SENDING = 1;// 正在发送
	public static final int MESSAGE_RESULT_RSV = 2; // 服务器已接收（已送达）
	public static final int MESSAGE_RESULT_ERROR = 3;// 发送失败、未接收到
	public static final int MESSAGE_RESULT_OK = 4;// 对方已接受成功（ 已读）

	/** 接收等待回执消息时间 */
	public static final int MESSAGE_RECEIPT_TIME = 10;// 单位秒

	/** 默认存储消息排序号 */
	// public static final int MESSAGE_DEFAULT_SORTNO=999;

	/** 是否正在重连消息服务器 true=正在连接 */
	public static final String MESSAGE_XMPP_RECONNING = "XMPP_RECONNING";
}
