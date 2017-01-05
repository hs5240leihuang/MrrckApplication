package com.meiku.dev.config;

/**
 * 常量定义和固定KEY
 * 
 * @author 库
 * 
 */

public class ConstantKey {
	// 统一使用的编码方式
	public final static String encoding = "utf-8";
	// 数据是否加密 1=是,0=否；
	public final static String SECRET_FLAG = "1";
	// 选择照片/相册后 是否将个人头像裁剪
	public static boolean USE_PIC_CUT = true;
	// 缓存用户KEY
	public final static String USER = "com.meiku.dev.user";
	// 缓存jsessionId的KEY
	public final static String JSESSIONID = "jsessionId";

	// 缓存云信登陆account，token的KEY
	public static final String KEY_USER_ACCOUNT = "account";
	public static final String KEY_USER_TOKEN = "token";

	/**
	 * 好推二维码APPID
	 */
	public static final String HOT_APPID = "hotapp231914766751241920";

	/**
	 * 网络请求最大等待时间,单位毫秒
	 */
	public final static int ReqNetTimeOut = 30000;
	/**
	 * 全国citycode 100000
	 */
	public final static int CITYCODE_QUANGUO = 100000;
	/**
	 * 0否 1是
	 */
	public static final String SP_IS_GUIDE = "sp_is_guide";
	public static final String SP_IS_ADDMSGCOLUMN = "sp_is_addcum";
	/** IMDBVersion */
	public static final String SP_IM_DB_TAG = "tag_imdb";
	/**
	 * 0否 1是
	 */
	public static final String SP_IS_PERFECTINFO = "sp_is_perfectinfo";
	/**
	 * 案例详情左右提示，0否 1是
	 */
	public static final String SP_IS_CASEDETAILGUIDE = "sp_is_cesedetail_guide";
	/**
	 * 老板标签，0否 1是
	 */
	public static final String SP_IS_XIANSHI = "sp_is_biaoqian_guide";
	/**
	 * 赛事广告url
	 */
	public static final String SP_TIPAD_URL = "sp_ad_tip_url";
	/**
	 * 赛事广告信息（点击按钮信息，点击跳转等）
	 */
	public static final String SP_MATCHAD_INFO = "sp_DiagramMatchInfo";

	/**
	 * 是否听筒模式还是正常模式
	 */
	public static final String SP_VOICE_MODE = "sp_is_voicemode";
	/**
	 * 装修需求发布存sp的key
	 */
	public static final String SP_PUB_DECNEED = "sp_pub_decneed";

	// 返回结果成功
	public static final String REQ_SUCCESS = "0";

	// 标识当前设备类型 1:android 2:Pad 3:ios 4 iPad
	public static final String DEV_TYPE = "1";
	// 缓存数据库版本KEY
	public final static String DB_VERSION = "DB_VERSION";

	// 是否采用新版本数据库KEY
	public final static String MSG_DB_VERSION = "IS_MSG_DB";

	// 本地asset数据库是否导入
	public final static String DB_ASSET_IMPORTED = "isAssetDBImported";

	// 首页广告请求参数
	public static final String REQ_ADDATA = "1";
	public static final String REQ_AD_PRODUCT = "2";

	// 板列表刷新传递数据的key
	public static final String KEY_BOARD_POSITIONID = "positionId";
	public static final String KEY_BOARD_PROVINCECODE = "provinceCode";
	public static final String KEY_BOARD_CITYCODE = "cityCode";

	// 查询每页条数
	public static final int PageNum = 20;

	/** 附件类型 0:图片 1:视频 2音频 */
	public static final int FILE_TYPE_IMG = 0;
	public static final int FILE_TYPE_VIDEO = 1;
	public static final int FILE_TYPE_AUDIO = 2;

	/** 验证手机号是否存在查询类型0注册1登录 */
	public static final int PHONE_TYPE_REGIS = 0;
	public static final int PHONE_TYPE_LOGIN = 1;

	/** 手机号是否存在本系统 ，0=存在;1=不存在；2=手机号码已存在,需要更改密码 */
	public static final String PHONE_IS_NOEXIT = "1";
	public static final String PHONE_IS_RESET = "2";

	/** 登录返回CODE，0=成功；1=密码错误；2=手机号不存在 */
	public static final String PHONE_LOGIN_SUCCESS = "0";
	public static final String PHONE_LOGIN_FAIL = "1";
	public static final String PHONE_LOGIN_NOEXIT = "2";

	/** 二维码生成拼接 */
	public static final String QR_CODE_USER = "#USER_QRCODE#"; // 加好友
	public static final String QR_CODE_BOARD = "#BOARD_QRCODE#";// 关注社区版块
	public static final String QR_CODE_GROUP = "#GROUP_QRCODE#";// 加群

	/** 注册返回CODE，0=成功；1=该手机号已经注册；2=注册失败 */
	public static final String PHONE_REGIS_SUCCESS = "0";

	/** 获取验证码倒计时时间 秒 */
	public static final int VERIFY_COUNT_DOWN_TIME = 60;

	/** 分享标记------卡片（个人名片） */
	public static final int ShareStatus_CARD_STATE = 1;
	/** 分享标记------资讯、帖子 */
	public static final int ShareStatus_NEWS_STATE = 2;
	/** 分享标记------赛事活动 */
	public static final int ShareStatus_SAISHI = 3;
	/** 分享标记------参赛秀场作品 */
	public static final int ShareStatus_SHOWWORK = 4;
	/** 分享标记------未参赛秀场作品 */
	public static final int ShareStatus_NOSHOWWORK = 5;

	/** 推送标记------直接弹出窗口展示内容 */
	public static final int PUSHStatus_OPENDIALOG = 6;
	/** 推送标记------打开H5 */
	public static final int PUSHStatus_OPENH5 = 7;
	/** 分享标记------分享产品 */
	public static final int ShareStatus_PRODUCT = 8;
	/** 分享标记------分享职位 */
	public static final int ShareStatus_JOBPOSITION = 9;
	/** 分享标记------分享公司 */
	public static final int ShareStatus_COMPANY = 10;
	/** 重复登陆，需退出 */
	public static final int PUSHStatus_LOGOUT = 11;
	/** 分享标记------卡片（群名片） */
	public static final int ShareStatus_GROUP_CARD_STATE = 12;
	/** 分享标记------美库词条（ */
	public static final int ShareStatus_CITIAO = 13;
	/** 分享标记------分享美库（ */
	public static final int ShareStatus_MRRCK = 14;
	/** 分享标记------分享装修案例（ */
	public static final int ShareStatus_DECCASE = 15;
	/** 分享标记------分享公司详情（ */
	public static final int ShareStatus_COMPANYDETAIL = 16;
	/** 分享标记------分享简历详情（ */
	public static final int ShareStatus_CRESUMEDETAIL = 17;
	/**
	 * 分享标记------找策划案例详情/
	 * 
	 */
	public static final int ShareStatus_PLOTDETAIL = 18;
	/**
	 * 分享标记------找策划介绍人详情/
	 * 
	 */
	public static final int ShareStatus_PLOTPERSONDETAIL = 19;
	/** 消息标记------单聊消息 */
	public static final int TAG_MESSAGE_SINGLE = 101;
	/** 消息标记------单聊消息 */
	public static final int TAG_MESSAGE_GROUP = 102;

	/** 进入home主页面，跳转页面标记--201进赛事广告页 */
	public static final int INHOMEPAGE_SAISHIMAIN = 201;
	/** 好友搜索页面使用类型 0搜素，1发名片，2发分享 ，3转发消息 */
	public static final int SEARCHPAGE_UESTYPE_SEARCH = 0;// 搜索好友
	public static final int SEARCHPAGE_UESTYPE_CARD = 1;// 搜索好友-发名片
	public static final int SEARCHPAGE_UESTYPE_SHARE = 2;// 搜索好友-发分享
	public static final int SEARCHPAGE_UESTYPE_FORWARD = 3;// 转发消息

	/** 分享打开方式 0本地打开，1 网页打开 */
	public static final int SEARCHPAGE_OPENTYPE_LOCAL = 0;// 本地打开
	public static final int SEARCHPAGE_OPENTYPE_NET = 1;// 本地打开

	/** 注册用户性别 1=男；2=女 */
	public static final String USER_SEX_MALE = "1";
	public static final String USER_SEX_WEMALE = "2";

	/** 极光推送自定义消息，接受类型 */
	public static final int JGPUSH_MESSAGE_RES_ALL = 0;// 所有接收者
	public static final int JGPUSH_MESSAGE_RES_HASLOGIN = 1;// 登录者
	public static final int JGPUSH_MESSAGE_RES_NOLOGIN = 2;// 未登录者

	/***
	 * 缓存服务页面头部菜单数据的key
	 */
	public static final String SETTING_VIBRATE = "setting_vibrate";// 菜单

	/** 投票活动，进入作品详细 传递数据的key */
	public static final String VOTE_DETAIL_POSTID = "postid";
	public static final String VOTE_DETAIL_SIGNUPID = "signupId";
	public static final String VOTE_DETAIL_TYPE = "ismywork";

	/** 是活动贴,传递参数postid到子页面的Key */
	public final static String KEY_POSTID = "postsId";
	/** 活动贴,报名是否开始结束的状态的key */
	public final static String KEY_SIGNUPFLAG = "KEY_SIGNUPFLAG";

	/** 进入版块详细页传递的boardID */
	public final static String KEY_BOARDID = "boardId";
	/** 进入版块详细页传递的Title */
	public final static String KEY_BOARDTITLE = "title";

	/** 热点招聘(服务功能)更多信息返回 */
	public static final String ADVERT_COMPANY_MORE_URL = "companyMoreUrl";
	/** 项目产品发布(服务功能)更多信息返回 */
	public static final String ADVERT_PROJECT_MORE_URL = "projectMoreUrl";
	/** 特别惠(服务功能)更多信息返回 */
	public static final String ADVERT_SPECIAL_BENEFIT_MORE_URL = "specialBenefitMoreUrl";
	/** 美库义工团(服务功能)更多信息返回 */
	public static final String ADVERT_FREE_WORK_MORE_URL = "freeWorkMoreUrl";

	public static final int RESULT_REFRESH_GROUP = 111;// 群设置返回result code

	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	/***
	 * 使用相册中的图片-1张
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO_ONE = 2;
	/***
	 * 使用相册中的图片-多张
	 */
	public static final int SELECT_PIC_BY_PICK_PHOTO_MULIT = 3;

	/**
	 * 使用美库相册中的图片-1张
	 */
	public static final int SELECT_PIC_BY_PICK_MRRCK_ONE = 4;

	/**
	 * 使用美库相册中的图片-多张
	 */
	public static final int SELECT_PIC_BY_PICK_MRRCK_MULIT = 5;

	/**
	 * 是否显示美库相册
	 */
	public static final String BUNDLE_SHOW_MRRCK_ALBUM = "show_mrrck";
	/**
	 * 是否是选择视频模式
	 */
	public static final String BUNDLE_SELECT_VIDEO = "show_mrrck";
	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	/***
	 * 从Intent获取视频路径的KEY
	 */
	public static final String KEY_VIDEO_PATH = "video_path";
	public static final String KEY_VIDEO_SIZE = "video_size";
	public static final String KEY_VIDEO_WITH = "video_width";
	public static final String KEY_VIDEO_HEIGHT = "video_height";

	/***
	 * 消息监听key
	 */
	public static final String KEY_IM_MESSAGE = "message";
	/***
	 * 消息监听key-from
	 */
	public static final String KEY_IM_FROM = "messageFrom";
	/***
	 * 消息监听key-to
	 */
	public static final String KEY_IM_TO = "messageTo";
	/***
	 * 文件监听key
	 */
	public static final String KEY_IM_FILE = "file";
	/***
	 * 与谁聊天的key
	 */
	public static final String KEY_IM_TALKTO = "talkto";
	/***
	 * 与谁聊天的ID
	 */
	public static final String KEY_IM_TALKTOACCOUNT = "talkToAccount";
	/***
	 * 聊天类型
	 */
	public static final String KEY_IM_SESSIONTYPE = "sessionType";
	/***
	 * 与谁聊天的名字
	 */
	public static final String KEY_IM_TALKTO_NAME = "talktoName";
	/***
	 * 与谁聊天的头像路径
	 */
	public static final String KEY_IM_TALKTO_HEADIMAGEPATH = "talktoHaedImg";
	/***
	 * 聊天室名称
	 */
	public static final String KEY_IM_MULTI_CHATROOM = "chatRoomName";
	/***
	 * 聊天室id
	 */
	public static final String KEY_IM_MULTI_CHATROOMID = "chatRoomId";
	/***
	 * 聊天室介绍key
	 */
	public static final String KEY_IM_MULTI_GROUPINTRO = "groupintro";
	/***
	 * 聊天室人数
	 */
	public static final String KEY_IM_MULTI_MEMBERS = "members";
	/***
	 * 聊天室职业类型
	 */
	public static final String KEY_IM_MULTI_POSITION = "groupposition";
	/***
	 * 聊天室图片
	 */
	public static final String KEY_IM_MULTI_GROUPIMG = "groupimg";
	/***
	 * 聊天类型
	 */
	public static final String KEY_IM_TALKTYPE = "talktype";
	/***
	 * 企业认证,传递的URL的KEY
	 */
	public static final String KEY_COMPANY_URL = "companyurl";

	/***
	 * 传递经纬度的key
	 */
	public static final String KEY_LAITUDE = "laitude";
	public static final String KEY_LONGITUDE = "longitude";

	/***
	 * 消息服务器状态
	 */
	public static final String KEY_CONNECT_STATUS = "connectStatus";

	/***
	 * 当前网络状态key
	 */
	public static final String KEY_NETWORK_STATUS = "networkstatus";

	/***
	 * 分享参数的key
	 */
	public static final String KEY_SHARE_KEY = "sharekey";
	/***
	 * 通知绑定数据key
	 */
	public static final String NOTIFICATION_BUNDLE = "PushBundle";
	/***
	 * 通知绑定数据key
	 */
	public static final String NOTIFICATION_MESSAGE_BUNDLE = "MessageBundle";
	// 缩略图后缀
	public static final String THUMB = "_thumb.png";
	// 视频缩略图后缀
	public static final String THUMB_VIDEO = "_thumb.jpg";

	/***
	 * 举报类型sourceType 帖子是1 评论是2
	 */
	public static final String POST = "1";
	public static final String COMMENT = "2";

	/***
	 * 认证类型 0未认证，1企业认证，2个人认证
	 */
	public static final String TYPE_AUTH_NO = "0";
	public static final String TYPE_AUTH_COM = "1";
	public static final String TYPE_AUTH_PERSION = "2";

	/***
	 * 缓存服务页面头部菜单数据的key
	 */
	public static final String SERVICE_MENU = "service_menu";// 菜单
	/***
	 * 缓存服务页面列表数据的key
	 */
	public static final String SERVICE_AD = "service_ad";// 话题
	public static final String SERVICE_TOPIC = "service_topic";// 话题
	public static final String SERVICE_RDZP = "service_rdzp";// 热点招聘
	public static final String SERVICE_PRODUCTS = "service_products";// 产品
	public static final String SERVICE_TBH = "service_tbh";// 特别惠
	public static final String SERVICE_YGT = "service_ygt";// 美库义工团
	public static final String SERVICE_DST = "service_dst";// 达人导师团
	/***
	 * 缓存热点企业列表数据的key
	 */
	public static final String FINDJOB_HOTCOMPANY = "findjob_hotcompany";// 热点招聘
	public static final String FINDJOB_COMPANYURL = "companyDetailUrl";// 热点招聘url
	/***
	 * 缓存服务页面更多数据的key
	 */
	public static final String SERVICE_MORE_URL_RDZP = "more_rdzp";// 更多-热点招聘
	public static final String SERVICE_MORE_URL_PRODUCTS = "more_products";// 更多-产品
	public static final String SERVICE_MORE_URL_TBH = "more_tbh";// 更多-特别惠
	public static final String SERVICE_MORE_URL_YGT = "more_ygt";// 更多-美库义工团
	public static final String SERVICE_MORE_URL_DST = "more_dst";// 更多-达人导师团

	public static final int REQCODE_NONET = 10000;// 没网络返回code

	/**
	 * 系统消息跳转类型常量
	 */
	public static final int SYSTEMMSG_TYPE_POSTDETAIL = 101;// (帖子收到评论)(帖子加精/置顶)到帖子详情页
	public static final int SYSTEMMSG_TYPE_SHOWWORK = 102;// (作品收到评论)到秀场详情页
	public static final int SYSTEMMSG_TYPE_PRODUCT = 103;// (找产品意向申请)我的产品收到意向页面
	public static final int SYSTEMMSG_TYPE_POSITION = 104;// (招聘面试邀请)到招聘岗位详情页
	public static final int SYSTEMMSG_TYPE_MSYQ = 105;// (招聘面试邀请)到我的求职——面试邀请列表页
	public static final int SYSTEMMSG_TYPE_RESUMELIST = 106;// (求职意向申请)到我的招聘——收到简历列表页
	public static final int SYSTEMMSG_TYPE_GETPRIZE = 108;// (比赛获奖)到秀场作品详情页
	public static final int SYSTEMMSG_TYPE_GETZAN = 109;// (作品点赞量>1000设为HOT)到秀场作品详情页
	public static final int SYSTEMMSG_TYPE_GETVOTE = 110;// (有*人投票)到秀场作品详情页
	public static final int SYSTEMMSG_TYPE_UPDATEVEISION = 111;// (升级提示)到更新界面
	public static final int SYSTEMMSG_TYPE_YANZHNEG = 112; // 群验证 通知
	public static final int SYSTEMMSG_TYPE_H5 = 200;// 跳转H5

	/**
	 * 案例发布，购买地区等选择城市的类型
	 */
	public static int SELECT_AREA_ALL = 0;// 选择全国省市
	public static int SELECT_AREA_HASAERA = 1;// 选择提供的地区
	public static int SELECT_AREA_NOTHAS = 2;// 选择提供的省市之外的地区

	// 关于文件后缀的常量
	public static final class FileSuffix {
		public static final String JPG = ".jpg";

		public static final String PNG = ".png";

		public static final String M4A = ".m4a";

		public static final String THREE_3GPP = ".3gp";

		public static final String BMP = ".bmp";

		public static final String MP4 = ".mp4";

		public static final String AMR_NB = ".amr";

		public static final String APK = ".apk";

		public static final String AAC = ".aac";
	}

	// 关于mimetype的常量
	public static final class MimeType {
		public static final String MIME_JPEG = "image/jpeg";

		public static final String MIME_PNG = "image/png";

		public static final String MIME_BMP = "image/x-MS-bmp";

		public static final String MIME_GIF = "image/gif";

		public static final String MIME_AUDIO_3GPP = "audio/3gpp";

		public static final String MIME_AUDIO_MP4 = "audio/mp4";

		public static final String MIME_AUDIO_M4A = "audio/m4a";

		public static final String MIME_AUDIO_AMR_NB = "audio/amr";

		public static final String MIME_AUDIO_AAC = "audio/aac";

		public static final String MIME_TXT = "txt/txt";// 用 于PC长消息

		public static final String MIME_WAPPUSH_SMS = "message/sms";

		public static final String MIME_WAPPUSH_TEXT = "txt/wappush"; // 文字wappush

		public static final String MIME_MUSIC_LOVE = "music/love"; // 爱音乐

		public static final String MIME_MUSIC_XIA = "music/xia"; // 虾米音乐

		public static final String MIME_VIDEO_3GPP = "video/3gpp";

		public static final String MIME_VIDEO_ALL = "video/*";

		public static final String MIME_LOCATION_GOOGLE = "location/google";
	}
}
