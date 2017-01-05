package com.meiku.dev.config;

/**
 * 本应用的运行参数设置
 */
public class AppConfig {

	public static final boolean DEBUG = false;
	// 是否采用加密
	public static final boolean IS_SECRET = false;
	// 是否采用数据压缩
	public static final boolean IS_COMPRESS = true;
	// 压缩数据标记1压缩
	public static final String DATA_COMPRESS = "1";
	// 压缩数据标记0不压缩
	public static final String NOT_COMPRESS = "0";

	/** 数据接口IP */
//	public static final String REQUEST_URL = "http://192.168.1.128:8080";//
	 public static final String REQUEST_URL = "http://api.mrrck.cn:8080";
	public static final String DOMAIN = REQUEST_URL + "/mrrck-web/appAPI/";

	/** 文件上传路径IP */
//	public static final String REQUEST_URL_UPLOAD = "http://192.168.1.128:8080";//
	public static final String REQUEST_URL_UPLOAD = "http://upload.mrrck.cn";//
	public static final String DOMAIN_UPLOAD = REQUEST_URL_UPLOAD
			+ "/mrrck_upload/fileUpload/";

	/** XMPPConnection后台地址 */
	// public final static String XMPP_URL = "192.168.1.188";// 内网测试
	// public final static String XMPP_URL = "openfire.mrrck.cn";//
	// 外网正式数据121.41.46.68
	// /** XMPPConnection后台端口 */
	// public final static int XMPP_PORT = 5222;

	public final static String BAIDU_MAP_HTTP_API = "http://api.map.baidu.com/place/v2/suggestion?query=%1$s&region=%2$s&output=json&ak=%3$s";
	/** 分享下载app地址 */
	public final static String SHARE_DAPP_URL = "http://h5.mrrck.com/downloadApp/downloadApp.html";

	// ===============================请求接口
	// begin========================================
	public static final String ACTIVITY = "apiActivity.action";// 同城活动接口
	public static final String NEWS = "apiNews.action";// 美业资讯

	// ===============================请求接口
	// begin========================================

	// ==========================应用公共模块================================
	/**
	 * 公共模块的RequestMapping
	 */
	public final static String PUBLIC_REQUEST_MAPPING = "apiCommon.action";
	/** 单个附件(图片)实时上传 */
	public static final String BUSINESS_SHANGCHUAN_PHOTO = "10025";
	/** 单个附件(图片)实时删除 */
	public static final String BUSINESS_DELETE_PHOTO = "10026";
	/** 查询启动页配置是否显示配置图片跳转至具体赛事接口(20160429新开) */
	public static final String BUSINESS_AD_HASMATCH = "10028";
	/**
	 * 群聊天
	 */
	public final static String PUBLIC_REQUEST_MAPPING_GROUPCHAT = "apiJobChat.action";
	/**
	 * 用户
	 */
	public static final String PUBLIC_REQUEST_MAPPING_USER = "apiUser.action";
	/**
	 * 招聘宝RequestMapping
	 */
	public final static String DYNAMIC_REQUEST_MAPPING = "apiCircle.action";
	// ==========================用户模块===================================
	public final static String USER_REQUEST_MAPPING = "apiUser.action";

	//
	public final static String PUBLICK_COMMON = "apiCommon.action";

	// 社区用
	public final static String PUBLICK_BOARD = "apiBoard.action";

	/**
	 * 支付下单用
	 */
	public final static String PUBLICK_APIPAY = "apiPay.action";

	/**
	 * 美库百科用
	 */
	public final static String PUBLICK_BAIKE = "apiBaikeMk.action";
	/**
	 * 服务板块
	 */
	public final static String PUBLICK_SERVICEFUNCTION = "apiServiceFunction.action";
	/**
	 * 文件上传
	 */
	public final static String PUBLICK_UPLOAD = "apiUpload.action";
	/** 文件图片 */
	public static final String BUSINESS_FILE_IMG10000 = "10000";
	/** 文件视频 */
	public static final String BUSINESS_FILE_VIDEO10001 = "10001";
	/** 装修发帖处理文件 */
	public static final String BUSINESS_FILE_IMG10002 = "10002";
	/**
	 * 分享记录
	 */
	public final static String BUSINESS_PUBLIC_SHARE = "10001";
	/**
	 * 查询最新版本号
	 */
	public final static String BUSINESS_APP_VERSION = "10003";
	/**
	 * 平台举报
	 */
	public final static String BUSINESS_REPORT = "10002";
	/**
	 * 查询某个模块下的广告
	 */
	public final static String BUSINESS_GET_AD = "10004";
	/**
	 * 更新AppDB
	 */
	public final static String BUSINESS_UPDATE_DB = "10005";

	/**
	 * 首页配置
	 */
	public final static String BUSINESS_INDEX = "10006";

	/**
	 * 首页配置
	 */
	public final static String BUSINESS_UPLOAD_ERRORMESSAGE = "10010";
	/**
	 * 分享美库
	 */
	public final static String BUSINESS_SHARE_MEIKU = "16018";
	/**
	 * 查询背景墙图片接口
	 */
	public final static String BUSINESS_SEARCH_BACKGROUND = "10027";
	/**
	 * 查询APP版本检测和数据库版本检测接口
	 */
	public final static String BUSINESS_APP_DB_VERSION = "10029";

	/** 发帖加载选择菜单接口 */
	public final static String BUSINESS_GETBOARDDATA = "10031";
	/**
	 * 用户登录接口
	 */
	public final static String BUSINESS_USER_LOGIN = "20001";
	/**
	 * 用户个人资料接口
	 */
	public final static String BUSINESS_USER_INFO = "20002";
	/**
	 * 获取验证码
	 */
	public final static String BUSINESS_USER_VETIFICATION = "20003";
	/**
	 * 用户注册
	 */
	public final static String BUSINESS_USER_REGISTER = "20004";
	/**
	 * 用户完善信息
	 */
	public final static String BUSINESS_USER_COMPLETE = "20005";
	/**
	 * 用户绑定LeanCloudID
	 */
	public final static String BUSINESS_USER_UPLOAD_LEANCLOUDID = "20006";
	/**
	 * 获取全局的用户（美库小管家）
	 */
	public final static String BUSINESS_USER_GLODBLE_USER = "20007";
	/**
	 * 查询好友关系列表
	 */
	public final static String BUSINESS_USER_FRIEND_LIST = "20008";
	/**
	 * 请求加好友接口
	 */
	public final static String BUSINESS_USER_FRIEND_ADD = "20009";
	/**
	 * 审核加好友(同意or拒绝)请求
	 */
	public final static String BUSINESS_USER_FRIEND_APPROVE = "20010";
	/**
	 * 解除好友关系请求接口
	 */
	public final static String BUSINESS_USER_FRIEND_DELETE = "20011";
	/**
	 * 通过LeancloudID获取用户信息
	 */
	public final static String BUSINESS_USER_GETBY_LEANID = "20012";
	/**
	 * 验证手机号(已废弃)
	 */
	// public final static String BUSINESS_VALIDATE_USER_PHONE = "20013";
	/**
	 * 修改用户密码
	 */
	public final static String BUSINESS_SET_PASSWORD = "20014";
	/**
	 * 直接通过手机号登录-前提：客户端验证过验证码是否正确
	 */
	public final static String BUSINESS_PHONE_LOGIN = "20015";
	/**
	 * 本地通讯录📞 比对
	 */
	public final static String BUSINESS_ADDRESS_BOOK_MATCH = "20016";

	/**
	 * 通过phone查询用户信息
	 */
	public final static String BUSINESS_USER_INFO_BY_PHONE = "20017";

	/**
	 * 用户上传附件列表(秀才艺图片,活动图片,公司介绍附件图片)
	 */
	public final static String BUSINESS_USER_ATTACH = "20018";

	/**
	 * 解除删除请求记录接口
	 */
	public final static String BUSINESS_USER_HISTORICAL_DELETE = "20019";
	/**
	 * 快捷发布音频
	 */
	public final static String BUSINESS_QUICK_PUBLISH_AUDIO = "20020";
	/**
	 * 快捷发布视频
	 */
	public final static String BUSINESS_QUICK_PUBLISH_VIDEO = "20021";
	/**
	 * 快捷发布图片
	 */
	public final static String BUSINESS_QUICK_PUBLISH_PHOTO = "20022";
	/**
	 * 根据用户Id查询用户的总览信息（包括企业用户的公司信息）
	 */
	public final static String BUSINESS_PC_USER_ALL_INFO = "20023";
	/**
	 * 收藏一个用户（点赞）
	 */
	public final static String BUSINESS_PC_ZAN = "20024";
	/**
	 * 取消收藏一个用户（点赞）
	 */
	public final static String BUSINESS_PC_ZAN_CANCEL = "20025";
	/**
	 * 个人中心删除用户的附件（支持多条）
	 */
	public final static String BUSINESS_PC_DELETE_ATTACH = "20026";
	/**
	 * 修改用户附件相关信息
	 */
	public final static String BUSINESS_PC_UPDATE_ATTACH = "20027";
	/**
	 * 更新用户的个性签名
	 */
	public final static String BUSINESS_PC_UPDATE_SIGNATURE = "20028";
	/**
	 * 个人中心删除用户秀才艺
	 */
	public final static String BUSINESS_PC_DELETE_SHOW = "20029";
	/**
	 * 查询个人中心的附件详细列表
	 */
	public final static String BUSINESS_PC_ATTACH_DETAIL_LIST = "20030";
	/**
	 * 查询个人中心的才艺秀详细列表
	 */
	public final static String BUSINESS_PC_SHOW_LIST = "20031";
	/**
	 * 用户名片信息修改
	 */
	public final static String BUSINESS_USER_INFO_MODIFY = "20032";
	/**
	 * 根据LeanCloud用户信息查询
	 */
	public final static String BUSINESS_USER_INFO_SEARCH = "20033";

	/**
	 * 用户意见反馈
	 */
	public static final String BUSINESS_USER_FEED_BACK = "20034";

	/**
	 * 启动app时上传坐标
	 */
	public final static String UPLOAD_COORDINATES = "20036";
	/**
	 * 新版本登录BID
	 */
	public final static String BUSINESS_USER_MKLOGIN = "20037";
	/**
	 * 重置密码
	 */
	public final static String BUSINESS_USER_RESETPASS = "20038";
	/**
	 * 新版本注册BID
	 */
	public final static String BUSINESS_USER_MKREGIS = "20039";
	/**
	 * 完善用户信息
	 */
	public final static String BUSINESS_USER_PERSONALINFO = "20042";
	/** 我的主页-编辑完善个人资料 */
	public static final String BUSINESS_USER_PERSONALINFO_NEW = "20065";
	/** 用微信登陆或者QQ登陆校验 */
	public static final String BUSINESS_USER_OTHER_LOGIN = "20043";
	/**
	 * 根据手机号和微信或QQ验证
	 */
	public final static String BUSINESS_USER_OTHER_VETIFICATION = "20044";

	/**
	 * 验证手机号是否存在或是否有密码 2 手机号码已存在,需要更改密码 0 手机号码已存在 1 手机号码不存在
	 */
	public final static String BUSINESS_USER_PHONE_VETIFICATION = "20045";

	/**
	 * 退出登录掉服务器接口
	 */
	public static final String BUSINESS_LOGOUT = "20048";

	/** 个人信息认证接口 */
	public static final String BUSINESS_PERSON_INFORMATION = "20052";
	/** 查询用户是否通过验证(公司认证OR个人认证)接口 */
	public static final String BUSINESS_YESORNO_DENTIFICATION = "20051";
	/** 查询用户账户余额接口 */
	public static final String BUSSINESS_QUERY_BALANCE = "20053";
	/** 更换用户头像接口接口 */
	public static final String BUSSINESS_CHANGE_AVATAR = "20054";
	/** 更换用户个性背景 */
	public static final String BUSSINESS_CHANGE_CARDBG = "20055";
	/** 查询用户mongoDb最新唯一性ID接口 */
	public static final String BUSSINESS_MONGDB = "20056";
	/** 用户退出登录(账号在其他设备登录极光推送消息使用) */
	public static final String BUSINESS_OUTLOGIN = "20057";
	/** 查询用户荣誉配置数据接口 */
	public static final String BUSINESS_YONGHURONGYU = "20058";
	/** 验证手机号码是否存在(账户登录OR注册)接口 */
	public static final String BUSINESS_YANZHENG_EXIST = "20059";
	/** 通过手机号码和密码登录(账号登录)接口 */
	public static final String BUSINESS_NEW_LOGIN = "20060";
	/** 用微信登录或者QQ登录验证(账户快捷登录)接口 */
	public static final String BUSINESS_WEICHATANDQQ_LOGIN = "20061";
	/** 用户绑定微信或者QQ账号(快捷登录账号绑定)接口 */
	public static final String BUSINESS_FASTLOGIN_BANGDING = "20062";
	/** 手机号码及密码(带微信 or QQ openId)注册(注册账号)接口 */
	public static final String BUSINESS_WEICHATANDQQ_ZHUCE = "20063";
	/** 完善用户基本资料(填写基本资料)接口 */
	public static final String BUSINESS_WANSHANINFO = "20064";
	/** 查询用户资料By用户手机号码及密文密码 */
	public static final String BUSINESS_FINDMKUSER = "20066";
	/** 查询个人相册(我的相册)列表 */
	public static final String BUSINESS_SEARCH_PHOTO = "20068";
	/** 新增个人相册图片(我的相册) */
	public static final String BUSINESS_ADD_PHOTO = "20069";
	/** 删除个人相册(我的相册-支持批量删除) */
	public static final String BUSINESS_DELETE_PHOTO_MINE = "20070";
	/** 新版的三方登录接口 */
	public static final String BUSINESS_US_20072 = "20072";
	/** 添加推荐码接口 */
	public static final String BUSINESS_US_20073 = "20073";
	/** 新版本用户注册 */
	public static final String BUSINESS_US_20074 = "20074";
	/** 根据手机号查询用户是否存在并返回基本信息 */
	public static final String BUSINESS_US_20075 = "20075";
	/** 根据userId更改用户绑定手机号 */
	public static final String BUSINESS_US_20076 = "20076";
	/** 根据userId新增用户绑定手机号 */
	public static final String BUSINESS_US_20077 = "20077";
	/** 根据userId新增用户绑定手机号(手机登陆使用) */
	public static final String BUSINESS_US_20078 = "20078";
	// ==========================找产品模块===================================
	public final static String PRODUCT_REQUEST_MAPPING = "apiProduct.action";
	/** 找产品首页搜索接口 */
	public static final String BUSINESS_PRODUCT_SEARCH = "19001";

	/** 找产品产品类别列表接口 */
	public static final String BUSINESS_PRODUCT_CATAGORYLIST = "19002";

	/** 产品详情(产品信息、公司信息、发布的其他产品)接口 */
	public static final String BUSINESS_PRODUCT_DETAIL = "19003";

	/** 发布产品接口 */
	public static final String BUSINESS_PRODUCT_PUBLIC = "19004";

	/** 编辑产品接口 */
	public static final String BUSINESS_PRODUCT_EDIT = "19005";

	/** 删除产品接口 */
	public static final String BUSINESS_PRODUCT_DELECT = "19006";

	/** 收藏产品接口 */
	public static final String BUSINESS_PRODUCT_COLLECTION = "19007";

	/** 查询最近产品意向填写的用户信息接口 */
	public static final String BUSINESS_PRODUCT_INTENTION = "19008";

	/** 新增产品意向申请接口 */
	public static final String BUSINESS_PRODUCT_ADD_INTENTION = "19009";

	/** 我的产品(我的发布)接口 */
	public static final String BUSINESS_PRODUCT_MY_PUBLIC = "19010";

	/** 我的产品(收到意向)接口 */
	public static final String BUSINESS_PRODUCT_MY_INTENTION = "19011";

	/** 我的产品(产品收藏)接口 */
	public static final String BUSINESS_PRODUCT_MY_COLLECTION = "19012";

	/** 产品刷新接口 */
	public static final String BUSINESS_PRODUCT_REFRESH = "19013";

	/** 开启(上线)产品接口 */
	public static final String BUSINESS_PRODUCT_OPEN_ONLINE = "19014";

	/** 关闭(下线)产品接口 */
	public static final String BUSINESS_PRODUCT_CLOSE_OUTLINE = "19015";

	/** 置顶产品接口 */
	public static final String BUSINESS_PRODUCT_TOP = "19016";

	/** 续费产品接口 */
	public static final String BUSINESS_PRODUCT_RENEW = "19017";

	/** 删除收藏产品接口 */
	public static final String BUSINESS_PRODUCT_DELECT_COLLECTION = "19018";

	/** 删除产品意向申请记录接口 */
	public static final String BUSINESS_PRODUCT_DELECT_WILL = "19019";

	/** 查询找产品详情页URL接口 */
	public static final String BUSINESS_PRODUCT_DETAIL_URL = "19020";

	/** 查询找产品详情页 */
	public static final String BUSINESS_PRODUCT_INFO = "19021";

	/** 查询产品(编辑产品)对应的单个图文详情接口 */
	public static final String BUSINESS_ONEINFO = "19022";

	/** 添加产品(编辑产品)对应的图文详情接口 */
	public static final String BUSINESS_ADDINFO = "19023";

	/** 修改产品(编辑产品)对应的图文详情接口 */
	public static final String BUSINESS_EDITINFO = "19024";

	/** 删除产品(编辑产品)对应的图文详情接口 */
	public static final String BUSINESS_DELETEINFO = "19025";

	/** 新增招商省份接口 */
	public static final String BUSINESS_PRODUCT_ADDPROVINCES = "19026";

	/** 支付产品接口 */
	public static final String BUSINESS_PRODUCT_PAY = "19027";

	// ==========================秀才艺==================================
	/**
	 * 秀才艺模块的RequestMapping
	 */
	public final static String TALENT_REQUEST_MAPPING = "apiShowPost.action";

	/**
	 * 获取秀才艺列表
	 */
	public final static String BUSINESS_TALENT_LIST = "30001";
	/**
	 * 收藏某个秀才艺
	 */
	public final static String BUSINESS_TALENT_COLLECT = "30002";
	/**
	 * 取消收藏某个秀才艺
	 */
	public final static String BUSINESS_TALENT_CANCEL_COLLECT = "30003";
	/**
	 * 添加评论
	 */
	public final static String BUSINESS_TALENT_ADD_COMMENT = "30004";
	/**
	 * 获取评论列表
	 */
	public final static String BUSINESS_TALENT_COMMENT_LIST = "30005";
	/**
	 * 删除评论
	 */
	public final static String BUSINESS_TALENT_DELETE_COMMENT = "30006";
	/**
	 * 发帖子
	 */
	public final static String BUSINESS_TALENT_PUBLISH_POSTS = "30007";

	/**
	 * 话题类型
	 */
	public final static String BUSINESS_TALENT_CATA = "30008";
	/**
	 * 获取话题列表
	 */
	public final static String BUSINESS_TALENT_THEME_LIST = "30009";
	/**
	 * 快速检索话题
	 */
	public final static String BUSINESS_TALENT_SEARCH_THEME = "30010";
	/**
	 * 创建话题
	 */
	public final static String BUSINESS_TALENT_CREATE_THEME = "30011";

	/**
	 * 查询某个话题下的比赛
	 */
	public final static String BUSINESS_TALENT_MATCH = "30012";
	/**
	 * 查询某个话题下前20条帖子
	 */
	public final static String BUSINESS_TALENT_RECOMMEND_POSTS = "30013";
	/**
	 * 查询某个帖子详情（评论、获奖、赞）
	 */
	public final static String BUSINESS_TALENT_POST_DETAIL = "30014";
	/**
	 * 查询某个城市（包含全国）的历史榜单(分页)
	 */
	public final static String BUSINESS_TALENT_HISTORY_RANK = "30015";
	/**
	 * 查询某个话题榜单banner
	 */
	public final static String BUSINESS_TALENT_TOPIC_BANNER = "30016";

	// ==========================资讯部分接口4==================================
	/**
	 * 资讯模块的RequestMapping
	 */
	public final static String INFO_REQUEST_MAPPING = "apiNews.action";
	/**
	 * 查询用户定制资讯接口
	 */
	public final static String BUSINESS_INFO_GET_CUSTOM_CHANNEL = "40001";
	/**
	 * 查询所有资讯分类接口
	 */
	public final static String BUSINESS_INFO_GET_ALL_CHANNEL = "40002";
	/**
	 * *更新用户的定制资讯接口
	 */
	public final static String BUSINESS_UPDATE_CHANNEL = "40003";
	/**
	 * *上传用户所在城市资讯接口
	 */
	public final static String BUSINESS_UPLOAD_CITYCODE = "40004";
	/**
	 * *查询某个资讯频道下的资讯接口
	 */
	public final static String BUSINESS_CHANNEL_NEWS = "40005";
	/**
	 * 收藏一个资讯
	 */
	public final static String BUSINESS_INFO_ADD_COLLECTION = "40006";
	/**
	 * 查询收藏资讯列表
	 */
	public final static String BUSINESS_INFO_COLLECTION_LIST = "40007";
	/**
	 * 删除收藏资讯列表
	 */
	public final static String BUSINESS_INFO_DELETE_COLLECTION = "40008";
	/**
	 * 评论一个资讯
	 */
	public final static String BUSINESS_INFO_ADD_COMMENT = "40009";
	/**
	 * 查询一个资讯的所有评论
	 */
	public final static String BUSINESS_INFO_GET_COMMENT_LIST = "40010";
	/**
	 * 举报一个资讯
	 */
	public final static String BUSINESS_INFO_REPORT = "40011";
	/**
	 * 查询一个资讯详情
	 */
	public final static String BUSINESS_INFO_GET_DETAIL = "40012";
	/**
	 * 资讯详情取消一个收藏
	 */
	public final static String BUSINESS_INFO_DETAIL_DELETE_COL = "40013";

	/**
	 * ==========================活动部分接口5==================================
	 * /**秀才艺模块的RequestMapping
	 */
	public final static String ACTIVITY_REQUEST_MAPPING = "apiActivity.action";
	/**
	 * 创建一个活动
	 */
	public final static String BUSINESS_ACTIVITY_NEW = "50001";
	/**
	 * 获取活动列表
	 */
	public final static String QUERY_ACTIVITY_LIST = "50002";
	/**
	 * 参加一个活动
	 */
	public final static String ATTENDACTIVITY = "50003";
	/**
	 * 删除我的活动[已结束]
	 */
	public final static String DELETE_MY_ACTIVITIE_END = "50004";
	/**
	 * 查询我的活动
	 */
	public final static String QUERY_MY_ACTIVITIES = "50005";
	/**
	 * 举报一个活动
	 */
	public final static String REPORT_ACTIVITIE = "50006";
	/**
	 * 收藏一个活动
	 */
	public final static String COLLECTION_ACTIVITIE = "50007";
	/**
	 * 评论一个活动
	 */
	public final static String COMMMENT_ACTIVITIES = "50008";
	/**
	 * 查询活动评论;
	 */
	public final static String QUERYMY_ACTIVITIE_COMMMENT = "50009";
	/**
	 * 查询所有活动类型
	 */
	public final static String QUERYMY_ACTIVITIE_TYPE = "50010";
	/**
	 * 查询某个活动的详情
	 */
	public final static String BUSINESS_ACTIVITY_GET_DETAIL = "50011";
	/**
	 * 查询某个人举办的活动和累计参加人数
	 */
	public final static String BUSINESS_ACTIVITY_TA_INFO = "50012";
	/**
	 * 查询他的活动
	 */
	public final static String BUSINESS_ACTIVITY_TA_LIST = "50013";

	/**
	 * 修改某个活动的图片信息
	 */
	public final static String MODIFY_ACTIVITY_MESSAGE = "50014";
	/**
	 * 删除我的活动[未结束]
	 */
	public final static String DELETE_MY_ACTIVITIE_UNEND = "50015";

	// ==========================同岗私聊接口6==================================
	/**
	 * 同岗私聊模块的RequestMapping
	 */
	public final static String SICHAT_REQUEST_MAPPING = "apiJobChat.action";
	/**
	 * 库群列表
	 */
	public final static String BUSINESS_SICHAT_GROUP = "60001";
	/**
	 * 绑定库群
	 */
	public final static String BUSINESS_SICHAT_ADD_GROUP = "60002";
	/**
	 * 解绑库群
	 */
	public final static String BUSINESS_SICHAT_DELETE_GROUP = "60003";
	/**
	 * 创建聊天群组
	 */
	public final static String BUSINESS_SICHAT_CRE_CHAT_GROUP = "60004";
	/**
	 * 加入某个群组
	 */
	public final static String BUSINESS_SICHAT_JOIN_GROUP = "60005";
	/**
	 * 被踢出群
	 */
	public final static String BUSINESS_SICHAT_KICK_GROUP = "60006";
	/**
	 * 主动退出某个群组
	 */
	public final static String BUSINESS_SICHAT_QUIT_GROUP = "60007";

	/**
	 * 新增群公告
	 */
	public final static String BUSINESS_SICHAT_ADD_ANNOUNCEMENT = "60008";
	/**
	 * 删除群公告
	 */
	public final static String BUSINESS_SICHAT_DELETE_ANNOUNCEMENT = "60009";
	/**
	 * 获取群公告列表
	 */
	public final static String BUSINESS_SICHAT_ANNOUNCEMENT_LIST = "60010";
	/**
	 * 群主审核 某个用户
	 */
	public final static String BUSINESS_SICHAT_GROUP_APPROVE = "60011";
	/**
	 * 举报群组聊天信息
	 */
	public final static String BUSINESS_SICHAT_REPORT = "60012";
	/**
	 * 获取群组信息
	 */
	public final static String BUSINESS_SICHAT_GET_GROUP_INFO = "60013";

	/**
	 * 用户已经加入的群组列表
	 */
	public final static String BUSINESS_SICHAT_JOINED_GROUPS = "60014";
	/**
	 * 用户已经加入的群组列表;
	 */
	public final static String BUSINESS_SICHAT_SEARCH_GROUPS = "60015";

	/**
	 * 群组信息加精
	 */
	public final static String BUSINESS_SICHAT_ADD_COLLECTION = "60016";
	/**
	 * 群组精华列表
	 */
	public final static String BUSINESS_SICHAT_COLLECTION_LIST = "60017";
	/**
	 * 群组信息取消加精
	 */
	public final static String BUSINESS_SICHAT_DELETE_COLLECTION = "60018";
	/**
	 * 用户获取同岗私聊已经加入的库
	 */
	public final static String BUSINESS_SICHAT_ADDED_GROUP = "60019";
	/**
	 * 用户获取推荐的群组
	 */
	public final static String BUSINESS_SICHAT_RECOMMEND_GROUP = "60020";
	/**
	 * 用户完善群信息
	 */
	public final static String BUSINESS_SICHAT_COMPLETE_GROUP = "60021";
	/**
	 * 创始人解散群组
	 */
	public final static String BUSINESS_SICHAT_DISMISS_GROUP = "60022";
	/**
	 * 同岗私聊通过群的LID获取群组ID
	 */
	public final static String BUSINESS_SICHAT_LID_GETGROUP = "60023";

	/**
	 * 查询某个群的举报消息（分页）
	 */
	public final static String BUSINESS_SICHAT_REPORT_LIST = "60024";
	/**
	 * 处理某条举报消息
	 */
	public final static String BUSINESS_SICHAT_HANDLE_REPORT = "60025";
	/**
	 * 搜索群举报消息
	 */
	public final static String BUSINESS_SICHAT_SEARCH_REPORT = "60026";
	/**
	 * 搜索群加精消息
	 */
	public final static String BUSINESS_SICHAT_SEARCH_COLLECTION = "60027";
	/**
	 * 群主 批量加人
	 */
	public final static String BUSINESS_SICHAT_MGR_ADD_GROUP = "60028";

	/**
	 * 查询所有省份的群(每个省获取前两个)
	 */
	public final static String BUSINESS_SICHAT_QUERY_GROUP = "60029";
	/**
	 * 根据条件查询群
	 */
	public final static String BUSINESS_QUERY_GROUP_FILTER = "60032";
	/**
	 * 查询所有省份的群
	 */
	public final static String BUSINESS_SICHAT_QUERY_ALL_GROUP = "60030";
	/**
	 * 查询本人加入群
	 */
	public final static String BUSINESS_SICHAT_QUERY_MY_GROUPS = "60033";
	// ==========================鹊桥会7==================================
	// ==========================求职宝8==================================
	/**
	 * 求职宝RequestMapping
	 */
	public final static String RESUME_REQUEST_MAPPING = "apiResume.action";
	/**
	 * 创建简历总揽接口
	 */
	public final static String BUSINESS_CREATE_RESUME_BASEINFO = "80001";
	/**
	 * 更新简历总揽接口
	 */
	public final static String BUSINESS_UPDATE_RESUME_BASEINFO = "80002";
	/**
	 * 查询某个人的简历总揽接口
	 */
	public final static String BUSINESS_GET_RESUME_BASEINFO = "80003";
	/**
	 * 更新美库简历接口
	 */
	public final static String BUSINESS_UPDATE_MK_RESUME = "80004";
	/**
	 * 上传美库简历的多媒体附件信息
	 */
	public final static String BUSINESS_UPLOAD_MK_RESUME_MEDIA = "80005";
	/**
	 * 查询用户的美库简历附件列表接口
	 */
	public final static String BUSINESS_GET_MK_RESUME_MEDIA = "80006";
	/**
	 * 筛选职位接口
	 */
	public final static String BUSINESS_FILTER_POSITION = "80007";
	/**
	 * 创建培训经历接口
	 */
	public final static String BUSINESS_SEARCH_POSITION = "80008";

	/**
	 * 首页职位列表接口
	 */
	public final static String BUSINESS_RESUME_JOB_LIST = "80009";
	/**
	 * 查询职位详情
	 */
	public final static String BUSINESS_RESUME_JOB_DETAIL = "80010";
	/**
	 * 查询公司详情
	 */
	public final static String BUSINESS_RESUME_COMPANY_DETAIL = "80011";
	/**
	 * 查询某个公司下的职位
	 */
	public final static String BUSINESS_RESUME_COMPANY_JOB_LIST = "80012";
	/**
	 * 收藏一个职位
	 */
	public final static String BUSINESS_RESUME_JOB_COLLECT = "80013";
	/**
	 * 取消收藏一个职位
	 */
	public final static String BUSINESS_RESUME_JOB_COL_DEL = "80014";
	/**
	 * 举报一个企业或职位
	 */
	public final static String BUSINESS_RESUME_REPORT = "80015";
	/**
	 * 向一个职位投递简历
	 */
	public final static String BUSINESS_RESUME_SEND = "80016";

	/**
	 * 删除美库简历附件
	 */
	public final static String BUSINESS_DELETE_MK_RESUME_MEDIA = "80017";
	/**
	 * 更新一个文字简历
	 */
	public final static String BUSINESS_UPDATE_TEXT_RESUME = "80018";
	/**
	 * 创建工作经历
	 */
	public final static String BUSINESS_CREATE_WORKEXP = "80019";
	/**
	 * 查询工作经历List
	 */
	public final static String BUSINESS_GET_WORKEXP_LIST = "80020";
	/**
	 * 删除工作经历(支持多个)
	 */
	public final static String BUSINESS_DELETE_WORKEXP = "80021";
	/**
	 * 创建培训经历
	 */
	public final static String BUSINESS_CREATE_TRAINEXP = "80022";
	/**
	 * 查询培训经历List
	 */
	public final static String BUSINESS_GET_TRAINEXP_LIST = "80023";
	/**
	 * 删除培训经历（支持多个）
	 */
	public final static String BUSINESS_DELETE_TRAINEXP = "80024";
	/**
	 * 刷新简历
	 */
	public final static String BUSINESS_REFRESH_RESUME = "80025";

	/**
	 * 查询我的求职列表
	 */
	public final static String BUSINESS_RESUME_MY_APPLY_LIST = "80026";
	/**
	 * 删除我的求职
	 */
	public final static String BUSINESS_RESUME_DELETE_MY_APPLY = "80027";
	/**
	 * 搜索企业（屏蔽企业用）
	 */
	public final static String BUSINESS_RESUME_SEARCH_COMPANY = "80028";
	/**
	 * 查询我的屏蔽的企业
	 */
	public final static String BUSINESS_RESUME_MY_SHIELD_COMPANY = "80029";
	/**
	 * 新增N条屏蔽企业信息
	 */
	public final static String BUSINESS_RESUME_ADD_SHIELD_COMPANY = "80030";
	/**
	 * 删除屏蔽的企业
	 */
	public final static String BUSINESS_RESUME_DELETE_SHIELD_COMPANY = "80031";

	/**
	 * 更新美库简历（根据userId,resumeId）个人中心使用
	 */
	public final static String BUSINESS_SET_RESUME = "80032";

	/**
	 * 更新工作经验
	 */
	public final static String BUSINESS_UPDATE_WORK_EXP = "80033";
	/**
	 * 更新培训经验
	 */
	public final static String BUSINESS_UPDATE_TRAIN_EXP = "80034";
	/**
	 * 招聘宝美库简历
	 */
	public final static String EMPLOY_REQUEST_RESSUME = "80041";
	/**
	 * 新招聘宝美库简历
	 */
	public final static String EMPLOY_REQUEST_RESUME = "80042";
	/**
	 * 新招聘宝修改简历
	 */
	public final static String EMPLOY_REQUEST_EDITRESUME = "80043";
	/**
	 * 新招聘宝查询简历
	 */
	public final static String EMPLOY_REQUEST_SEARCHRESUME = "80044";
	/**
	 * 招聘宝修改美库简历
	 */
	public final static String MODIFY_REQUEST_RESSUME = "80040";

	// ==========================找工作==================================
	/** 找工作首页接口 */
	public static final String BUSINESS_FINDJOB_HOME = "80045";
	/** 找工作职位列表 */
	public static final String BUSINESS_FINDJOB_JOBLIST = "80046";
	/** 添加收藏企业(我的求职)接口 */
	public static final String BUSINESS_FINDJOB_COLLECT = "80050";

	/** 找工作查看职位详情公司信息接口 */
	public static final String BUSINESS_FINDJOB_GONGSI = "80051";
	/** 查询职位信息及企业信息(jobId)接口 */
	public static final String BUSINESS_FINDJOB_SELECTINFORMATION = "80056";
	/** 找工作搜索职位列表 */
	public static final String BUSINESS_FINDJOB_SEARCHJOB = "80055";
	/** 简历中心我的简历填写 (新版 2016年10月28日 18:05:39 jsg 添加) */
	public static final String BUSINESS_80060 = "80060";
	/** 简历中心我的简历编辑 (新版 2016年10月28日 18:05:39 jsg 添加) */
	public static final String BUSINESS_80061 = "80061";
	public static final String BUSINESS_80062 = "80062";
	// ==========================招聘宝9==================================

	/**
	 * 招聘宝RequestMapping
	 */
	public final static String EMPLOY_REQUEST_MAPPING = "apiEmploy.action";
	/**
	 * 认证企业信息
	 */
	public final static String BUSINESS_CERT_COMPANYINFO = "90001";
	/**
	 * 更新企业信息
	 */
	public final static String BUSINESS_UPDATE_COMPANYINFO = "90002";
	/**
	 * 更新/上传企业视频
	 */
	public final static String BUSNIESS_UPDATE_COMPANYVIDEO = "90003";
	/**
	 * 更新/企业描述图片
	 */
	public final static String BUSNIESS_UPDATE_COMPANYDESCPNG = "90004";
	/**
	 * 更新/上传企业LOGO
	 */
	// public final static String BUSNIESS_UPDATE_COMPANYLOGO = "90005";

	/**
	 * 删除企业图片接口
	 */
	public final static String BUSNIESS_DELETE_COMPHOTO = "90006";
	/**
	 * 发布职位
	 */
	public final static String BUSINESS_PUBLISH_JOBINFO = "90007";
	/**
	 * 修改发布职位
	 */
	public final static String BUSINESS_MODIFY_JOBINFO = "90008";
	/**
	 * 职位刷新时间接口
	 */
	public final static String BUSINESS_REFRESH_JOB = "90009";
	/**
	 * 删除招聘职位接口
	 */
	public final static String BUSINESS_DELETE_JOB = "90010";
	/**
	 * 查询发布职位列表接口
	 */
	public final static String BUSINESS_LIST_JOB = "90011";
	/**
	 * 查询职位详情接口
	 */
	public final static String BUSINESS_JOB_DETAIL = "90012";
	/**
	 * 查询公司详情接口
	 */
	public final static String BUSINESS_COMPANY_DETAIL = "90013";
	/**
	 * 人才库简历搜索(带分页列表)接口
	 */
	public final static String BUSINESS_RESUMELIST = "90014";
	/**
	 * 人才库简历高级搜索(带分页列表)接口
	 */
	public final static String BUSINESS_RESUME_SUPER_SEARCH = "90015";
	/**
	 * 邀请面试历史列表搜索(带分页列表)接口
	 */
	public final static String BUSINESS_INTERVIEWLIST = "90016";
	/**
	 * 邀请面试历史列表高级搜索(带分页列表)接口
	 */
	public final static String BUSINESS_INTERVIEW_SUPER_SEARCH = "90017";
	/**
	 * 删除邀请面试历史记录(多个同时删除)接口
	 */
	public final static String BUSINESS_INTERVIEW_DELETE = "90018";
	/**
	 * 查看某个人简历(总览及个人信息,美库简历,文字简历)接口
	 */
	public final static String BUSINESS_CHECK_RESUME = "90019";
	/**
	 * 公司邀请某人面试接口
	 */
	public final static String BUSINESS_INVITE_RESUME = "90020";
	/**
	 * 增加公司老板浏览简历记录接口
	 */
	public final static String BUSINESS_RESUME_ADD_RECORD = "90021";
	/**
	 * 查询公司老板浏览简历记录接口
	 */
	public final static String BUSINESS_RESUME_QUERY_RECORD = "90022";
	/**
	 * 删除企业老板浏览简历记录接口
	 */
	public final static String BUSINESS_RESUME_DELETE_CHECK_RECORD = "90023";
	/**
	 * 修改企业营业执照
	 */
	public final static String BUSINESS_MODIFY_COMPANY_LICENSE = "90024";
	/**
	 * 公司收藏某人简历接口
	 */
	public final static String BUSINESS_COMPANY_COLLECT_RESUME = "90025";
	/**
	 * 查询公司收藏简历列表接口
	 */
	public final static String BUSINESS_COMPANY_COLLECTION_LIST = "90026";
	/**
	 * 删除公司收藏简历接口
	 */
	public final static String BUSINESS_COMPANY_DELETE_COLLECTION = "90027";

	/**
	 * 查询简历库(求职者投递的简历)(带分页列表)接口
	 */
	public final static String BUSINESS_EMPLOY_RESUME_LIST = "90028";
	/**
	 * 删除投递简历信息(简历库)接口
	 */
	public final static String BUSINESS_EMPLOY_DELETE_RESUME = "90029";
	/**
	 * 一周内是否有邀请记录验证(邀请面试)接口
	 */
	public final static String BUSINESS_EMPLOY_CHECK_INVITABLE = "90030";

	/**
	 * 获取企业信息
	 */
	public static final String BUSINESS_EMPLOY_GET_COMPANY = "90033";
	/** 更新投递简历信息(简历投递,更新已查看记录)接口 */
	public static final String BUSINESS_UPDATE_RESUME_INFORMATION = "90036";

	/**
	 * 完善企业信息
	 */
	public static final String BUSINESS_COMPANY = "90038";
	/**
	 * 获取附近简历
	 */
	public static final String COORDINATE = "90037";

	/** 验证是否已认证企业信息接口 */
	public static final String BUSINESS_VERIFY = "90040";

	/** 提交企业认证信息接口 */
	// public static final String BUSINESS_COMPLANYINFO_UPLOAD = "90041";
	public static final String BUSINESS_COMPLANYINFO_RT_90065 = "90065";
	// 企业信息查询new 带自定义内容 、设计师
	public static final String BUSINESS_COMPLANYINFO_RT_90068 = "90068";
	// 企业设计师操作
	public static final String BUSINESS_COMPLANYINFO_RT_90067 = "90067";
	// 企业自定义内容操作
	public static final String BUSINESS_COMPLANYINFO_RT_90066 = "90066";
	// 企业资质操作
	public static final String BUSINESS_COMPLANYINFO_RT_90069 = "90069";

	/** 修改完善企业信息接口 */
	public static final String BUSINESS_COMPLANYINFO_UPDATE = "90042";

	/** H5查询附近简历接口 */
	public static final String BUSINESS_SEARCH_NEAR_RESUME = "90043";
	/** H5查询搜索简历接口 */
	public static final String BUSINESS_SEARCH_RESUME = "90044";
	/** H5查询发布职位列表接口 */
	public static final String BUSINESS_SELECT_PUBLIC_POSITION = "90045";
	/** 创建职位接口 */
	public static final String BUSINESS_PUBLISH_NEWJOB = "90046";
	/** 编辑职位接口 */
	public static final String BUSINESS_EDITJOB = "90048";
	/** H5职位置顶接口 */
	public static final String BUSINESS_POSITION_TOP = "90049";
	/** 汇总统计(我的招聘)接口 */
	public static final String BUSINESS_USAGESUMMARY = "90054";
	/** 消费明细(我的招聘)接口 */
	public static final String BUSINESS_CONSUMERDETAIL = "90055";
	/** H5找工作发布职位地区列表接口 */
	// public static final String BUSINESS_PUBLIC_POSITION_CITY = "90056";
	public static final String BUSINESS_PUBLIC_POSITION_CITY = "90063";
	/** 通过用户编号查询企业信息接口 */
	public static final String BUSINESS_COMPLANYINFO = "90057";

	/** 删除企业视频信息接口 */
	public static final String BUSNIESS_DELETE_COMPANYVIDEO = "90058";

	/** 新增企业介绍图片(单个文件实时上传)信息接口 */
	public static final String BUSINESS_COMPLANYINFO_ADD_PHOTO = "90059";

	/** 查询我的招聘(企业信息H5的URL)信息接口 */
	public static final String BUSINESS_MYRECRUIT = "90060";
	/** 新的汇总统计(我的招聘)接口 */
	public static final String NEWUSESUMMARY_90062 = "90062";
	/** 招聘宝开通会员增加城市计算城市费用 */
	public static final String BUSINESS_ZPB_BUYCITYMONEY = "90061";
	/** 获取公司招聘宝会员信息接口 */
	public static final String ADDPUBLISHCITY_INFORMATION_90064 = "90064";

	/** 修改企业视频接口 new cw */
	public static final String BUSINESS_RT_90070 = "90070";
	/** 新增企业图片介绍接口 new cw */
	public static final String BUSINESS_RT_90071 = "90071";
	/** 修改企业logo接口 new cw */
	public static final String BUSINESS_RT_90072 = "90072";
	// ==========================创业吧11==================================

	// ==========================美库百科12==================================

	// ==========================动态圈13==================================

	/**
	 * 发布动态圈
	 */
	public final static String BUSINESS_DYNAMIC_PUBLISH = "13001";
	/**
	 * 查询动态圈全部动态接口(带分页)
	 */
	public final static String BUSINESS_DYNAMIC_ALL = "13002";
	/**
	 * 查询动态圈我的动态接口(带分页)
	 */
	public final static String BUSINESS_DYNAMIC_MY = "13003";
	/**
	 * 删除动态圈我的动态接口
	 */
	public final static String BUSINESS_DYNAMIC_DELETE_MY = "13004";
	/**
	 * 查询单个动态圈接口
	 */
	public final static String BUSINESS_DYNAMIC_SINGLE_DETAIL = "13005";
	/**
	 * 查询动态圈评论接口(带分页)
	 */
	public final static String BUSINESS_DYNAMIC_COMMENTS = "13006";
	/**
	 * 新增动态圈评论接口
	 */
	public final static String BUSINESS_DYNAMIC_ADD_COMMENT = "13007";
	/**
	 * 取消新增动态圈评论接口
	 */
	public final static String BUSINESS_DYNAMIC_DELETE_COMMENT = "13008";
	/**
	 * 新增动态圈收藏(点赞)接口
	 */
	public final static String BUSINESS_DYNAMIC_ZAN = "13009";
	/**
	 * 取消动态圈收藏(点赞)接口
	 */
	public final static String BUSINESS_DYNAMIC_CANCEL_ZAN = "13010";

	// ==========================leancloud的AddRequest请求==================================
	public final static String LEAN_CLOUD_ADDREQUEST = "AddRequest";

	/**
	 * 首页广告
	 */
	public final static String BUSINESS_HOME_ADS = "10023";
	/**
	 * 首页板
	 */
	public final static String BUSINESS_HOME_BOARD = "15001";
	/**
	 * 其他板
	 */
	public final static String BUSINESS_HOME_OTHER_BOARD = "15002";
	/**
	 * 查询用户添加更多社区版列表数据接口
	 */
	public final static String BUSINESS_COMM_GETBOARDLIST = "15003";
	/**
	 * 用户添加更多社区版接口
	 */
	public final static String BUSINESS_COMM_BOARDBYADD = "15004";
	/**
	 * 用户删除更多社区版接口
	 */
	public final static String BUSINESS_COMM_BOARDBYDEL = "15005";

	/**
	 * 创建我的板
	 */
	public final static String BUSINESS_COMM_CREATBOARD = "15006";
	/**
	 * 查询某个版块以及版块下对应的帖子列表接口
	 */
	public final static String BUSINESS_BOARD_BPOSTS = "15009";
	/**
	 * 查询帖子详情列表
	 */
	public final static String BUSINESS_BOARD_POSTDETAIL = "15010";
	/**
	 * 查询帖子详情列表--新h5
	 */
	public final static String BUSINESS_BOARD_POSTDETAIL_NEW = "15088";
	/** 美容师社区帖子详情页收藏接口 */
	public static final String BUSINESS_BOARD_SHOUCANG = "15012";

	/** 美容师社区帖子详情页取消收藏接口 */
	public static final String BUSINESS_BOARD_NOTSHOUCANG = "15013";

	/**
	 * 查询更多帖子列表
	 */
	public final static String BUSINESS_BOARD_LISTPOST = "15017";
	/**
	 * 查询帖子评论列表
	 */
	public final static String BUSINESS_BOARD_LISTCMT = "15019";

	/**
	 * 获取话题
	 */
	public final static String COMM_GETTOPIC = "15008";
	/**
	 * 发布话题
	 */
	public final static String COMM_RELEASETOPIC = "15016";
	/**
	 * 附件上传公用接口
	 */
	public final static String ALL_ATTACHMENT = "10014";
	/**
	 * 回复话题
	 */
	public final static String COMM_REPLYTOPIC = "15011";
	/** 查询活动贴分类列表接口 */
	public final static String BUSINESS_VOTE_CATEGORY = "15018";
	/** 查询活动贴奖项设置接口 +查询活动贴活动规则接口 */
	public final static String BUSINESS_VOTE_RULE = "15032";
	/** 查询活动贴详细信息及参赛选手作品列表接口 */
	public final static String BUSINESS_VOTE_POSTDETAIL = "15021";
	/** 查询活动贴参赛选手作品列表接口 */
	public final static String BUSINESS_VOTE_WORKSLIST = "15022";
	/** 活动贴参赛选手上传作品接口 */
	public final static String BUSINESS_VOTE_UPLOAD = "15023";
	/** 查询活动贴参赛选手作品详细信息及评论接口 */
	public final static String BUSINESS_VOTE_DETAIL = "15024";
	/** 活动贴参赛选手作品投票接口 */
	public final static String BUSINESS_VOTE_VOTENOW = "15025";
	/** 活动贴参赛选手作品取消投票接口 */
	public final static String BUSINESS_VOTE_VOTECANCLE = "15026";
	/** 活动贴参赛选手作品取消投票接口 */
	public final static String BUSINESS_VOTE_ADDPINGLUN = "15027";
	/** 查询活动贴实时排名列表接口 */
	public final static String BUSINESS_VOTE_WORKSPAIMING = "15031";
	/** 查询验证用户是否已提交该活动贴报名作品接口 */
	public final static String BUSINESS_VOTE_ISSIGNUP = "15033";
	/** 查询版块下对应的成员列表接口 */
	public static final String BUSSINESS_MEMBER = "15034";
	/** 查询某个版块以及版块下对应的帖子列表接口 2016年7月7日 17:26:15 jsg 改造 */
	public static final String BUSINESS_BANKUAIANDTIEZI = "15099";
	/** 查询某个版块对应的帖子列表接口 2016年7月7日 17:28:25 jsg 改造 */
	public static final String BUSINESS_BANKUAIMEITUEZI = "15100";
	/** 服务板块发布接口 */
	public final static String BUSINESS_SERVER_CREATPOST = "15101";
	/** 基本信息获取接口(分享个人名片或个人中心基本信息) */
	public final static String BUSINESS_USER_SHARE_INFO = "20041";
	/** 查询首页动态热帖接口 */
	public static final String BUSINESS_HOME_DONGTAI = "15035";
	/** 修改帖子接口(2015-12-08新开接口) */
	public static final String BUSINESS_HOME_EDITPOST = "15036";
	/** 删除帖子接口(2015-12-08新开接口) */
	public static final String BUSINESS_DELETE_POST = "15037";
	/** 删除帖子评论接口(2015-12-08新开接口) */
	public static final String BUSINESS_DELETE_COMMENT = "15038";
	/** 帖子举报接口 */
	public static final String BUSINESS_BOARD_REPORT = "15066";
	/** 发布比赛作品 */
	public static final String BUSINESS_BOARD_PUBLISH = "15074";
	/** 社区首页其他帖子数据 */
	public static final String BUSINESS_HOME_POST = "15085";
	/** 个人举报接口 */
	public static final String BUSINESS_PERSON_REPORT = "20050";

	// -------------- 找同行 ------------------
	/**
	 * 附近同行
	 */
	public final static String PUBLICK_NEARBY_GROUP = "apiGroup.action";
	/** 附近人(找同行)接口 18001 */
	public static final String BUSINESS_NEARBY_PEOPLE = "18001";
	/** 查询好友列表(找同行)接口18002 */
	public static final String BUSINESS_NEARBY_FRIENDLIST = "18002";
	/** 创建群聊(找同行)接口18003 */
	public static final String BUSINESS_NEARBY_CREATGROUP = "18003";
	/** 修改群名称(找同行)接口 */
	public static final String BUSINESS_NEARBY_GROUPNAME = "18005";
	/** apiGroup申请加入群(找同行)接口 */
	public static final String BUSINESS_NEARBY_JOINGROUP = "18006";
	/** apiGroup 退出群(找同行)接口 */
	public static final String BUSINESS_NEARBY_QUITGROUP = "18008";
	/** apiGroup 删除群成员(找同行)接口 */
	public static final String BUSINESS_NEARBY_DELMEMBER = "18009";
	/** 查询新的朋友(找同行)接口18016 */
	public static final String BUSINESS_SEARCH_NEWFRIEND = "18016";
	/** 查询黑名单(找同行)接口18018 */
	public static final String BUSINESS_SEARCH_DEFRIEND = "18018";
	/** 添加关注好友(找同行)接口 */
	public static final String BUSSINESS_ADDFOCUS = "18011";
	/** 取消关注好友(找同行)接口 */
	public static final String BUSSINESS_CANCELFOCUS = "18012";
	/** 移除粉丝(找同行)接口 */
	public static final String BUSSINESS_REMOVEFANS = "18013";
	/** 好友设置(找同行)接口 */
	public static final String BUSSINESS_GOODFRIENDSET = "18014";
	/** 搜索联系人(昵称+手机号+美库号)接口 */
	public static final String BUSSINESS_SEARCH_FRIEND = "18015";
	/** 查询群聊(通讯录)接口 */
	public static final String BUSSINESS_SEARCH_GROUP = "18017";
	/** 添加黑名单用户(通讯录)接口 */
	public static final String BUSSINESS_ADD_BLACK = "18019";
	/** 取消黑名单用户(通讯录)接口 */
	public static final String BUSSINESS_CANCEL_BLACK = "18020";
	/** 查询群公告(找同行)接口18010 */
	public static final String BUSINESS_SEARCH_NOTIFICATION = "18010";
	/** 创建群公告(找同行)接口18004 */
	public static final String BUSINESS_CREATE_NOTIFICATION = "18004";
	/** 比对用户通讯录是否是好友返回数据接口 */
	public static final String BUSSINESS_CHECK_PHONEBOOK = "18021";
	/** 查询当前登录用户与查看用户关系接口 */
	public static final String BUSSINESS_CHECK_RELATIONSHIP = "18022";
	/** apiGroup 增加群成员(找同行)(支持批量邀请添加)接口 */
	public static final String BUSSINESS_NEARBY_ADDMEMBER = "18023";
	/** 增加群成员(找同行)(支持批量邀请添加)接口 新版 2016年11月8日 15:52:52 jsg */
	public static final String GP_18044 = "18044";
	/** apiGroup 查询群信息(找同行)接口 */
	public static final String BUSSINESS_NEARBY_GROUPINFORMATION = "18024";
	/** apiGroup 查询群成员列表数据(找同行)接口 */
	public static final String BUSSINESS_NEARBY_GROUPMEMBER = "18025";
	/** apiGroup 邀请好友加入群好友数据与群成员数据比对(找同行)接口 */
	public static final String BUSSINESS_NEARBY_GROUPMEMBERCHECK = "18026";
	/** 推荐兴趣群(找同行)接口 18027 */
	public static final String BUSSINESS_IM_GROUP_TUIJIAN = "18027";
	/** 搜索群(找同行)接口 18028 */
	public static final String BUSSINESS_IM_SEARCHGROUP = "18028";
	/** 群主解散群(找同行)接口 */
	public static final String BUSSINESS_IM_DISSOLUTION = "18029";
	/** 推荐兴趣群分页下拉(找同行)(不带聊天室)接口 */
	public static final String BUSSINESS_IM_GROUP_ONLYGROUP = "18030";
	/** 查询聊天室信息数据接口 */
	public static final String BUSSINESS_IM_CHARTROOMINFO = "18031";
	/** 通过岗位(一级)及城市code查询聊天室接口 */
	public static final String BUSSINESS_IM_CHATROOMID = "18032";
	/** 查询群信息-二维码扫描(找同行)接口 */
	public static final String BUSSINESS_IM_GROUPINFO = "18033";
	/** 新的朋友删除添加关注请求(找同行)接口 */
	public static final String BUSSINESS_IM_DELETE_GUANZHU = "18034";
	/** 用户修改在群中的昵称(找同行)接口 */
	public static final String BUSSINESS_IM_GROUP_CHANGENICKNAME = "18035";
	/** 用户设置群消息免打扰(找同行)接口 */
	public static final String BUSSINESS_IM_GROUP_DISTURD = "18036";
	/** 新的库群推荐 2016年8月12日 */
	public static final String BUSSINESS_IM_GROUP_TUIJIANNEW = "18037";
	/** 库群为推荐兴趣群搜索接口 2016年8月12日 16:15:35 jsg 添加 */
	public static final String BUSSINESS_IM_GROUP_SEARCH = "18038";
	/** 查询未读群聊记录 */
	public static final String BUSSINESS_IM_GROUP_LOGS = "18039";
	/** 查询离线记录 */
	public static final String BUSSINESS_IM_OFFLINE_MSG = "18040";
	/** 离线记录设置为已读 */
	public static final String BUSSINESS_IM_OFFLINE_SETREAD = "18041";
	/** 新版附近人接口 */
	public static final String BUSINESS_ZX_18042 = "18042";

	/** 新版 创建群接口 */
	public static final String BUSINESS_ZX_18043 = "18043";
	/** 申请加入群接口 */
	public static final String BUSINESS_ZX_18048 = "18048";
	/** 群成员加入申请列表 */
	public static final String BUSINESS_ZX_18049 = "18049";
	/** 操作入群申请接口 */
	// groupId,userId,leanCloudUserName,type (1同意 2拒绝)
	public static final String BUSINESS_ZX_18050 = "18050";
	/** 查询申请入群的成员信息接口 */
	public static final String BUSINESS_ZX_18051 = "18051";
	/** 查询群信息接口 android专用 */
	public static final String GP_18052 = "18052";
	// ==========================个人主页请求==================================
	/**
	 * 个人主页RequestMapping
	 */
	public final static String PERSONAL_REQUEST_MAPPING = "apiUserHome.action";
	/** 查询新版本用户信息(个人主页跳转编辑)接口 */
	public static final String BUSINESS_PHOTO = "16013";
	/** 查询用户个人主页(20160516版)(个人主页跳转编辑)接口 */
	public static final String BUSINESS_PHOTO_NEW = "16016";
	/** 用户个人主页分享接口 */
	public static final String BUSINESS_PEESON_SHARE = "16017";
	/**
	 * 个人主页和他人主页
	 */
	public final static String BUSINESS_PERSONAL_MAIN = "16001";
	/**
	 * 全部视频列表
	 */
	public final static String BUSINESS_ALLVEDIO = "16002";
	/**
	 * 全部相册列表
	 */
	public final static String BUSSINESS_ALLIMAGE = "16003";
	/**
	 * 全部帖子列表
	 */
	public final static String BUSINESS_ALLPOST = "16004";
	/**
	 * 全部收藏列表
	 */
	public final static String BUSINESS_ALLCOLLECT = "16005";
	/**
	 * 粉丝列表
	 */
	public final static String BUSINESS_FANS = "16006";
	/**
	 * 社区列表
	 */
	public final static String BUSINESS_COMM = "16007";
	/**
	 * 添加关注
	 */
	public final static String BUSINESS_ADDFOCUS = "16008";
	/**
	 * 取消关注
	 */
	public final static String BUSINESS_CANCELFOCUS = "16009";
	/**
	 * 关注列表
	 */
	public final static String BUSINESS_FOCUS = "16010";
	/**
	 * 隐私权限查询
	 */
	public final static String BUSINESS_PRIVACY = "16011";
	/**
	 * 查询个人主页
	 */
	public final static String BUSINESS_USER_SHOW = "16012";
	/** 查询用户个人主页(20160516版)接口 */
	public static final String BUSINESS_USER_SHOW_NEW = "16015";
	/** 查询个性标签接口 */
	public static final String BUSINESS_PERSION_TAGS = "16014";

	/** 修改隐私接口 */
	public final static String BUSINESS_UPDATE_PRIVACY = "20040";

	/** 服务主页接口 */
	public static final String BUSINESS_SERVICEFUNCTION = "17001";
	/** 服务主页新接口 */
	public static final String BUSINESS_SERVICEFUNCTION_NEW = "17002";
	// ==========================秀场请求==================================
	/** 查询秀场(带分页)列表接口 */
	public static final String BUSINESS_SEARCH_SHOW = "15039";
	/** 查询秀场搜索(带分页)列表接口 */
	public static final String BUSINESS_SOUSUO_SHOW = "15040";
	/** 查询赛事(带分页)列表接口 */
	public static final String BUSINESS_SEARCH_EVENT_SHOW = "15041";
	/** 查询赛事搜索(带分页)列表接口 */
	public static final String BUSINESS_SOUSUO_EVENT_SHOW = "15042";
	/** 查询活动贴信息(首页)接口 */
	public static final String BUSINESS_SEARCH_INFORMATION_HOMEPAGE = "15043";
	/** 查询活动贴信息(介绍)接口 */
	public static final String BUSINESS_SEARCH_INFORMATION_INTRODUCE = "15044";
	/** 查询活动贴信息(奖项设置)接口 */
	public static final String BUSINESS_SEARCH_INFORMATION_AWARD = "15045";
	/** 查询活动贴信息(参赛作品)接口 */
	public static final String BUSINESS_SEARCH_INFORMATION_WORKS = "15046";
	/** 查询活动贴信息(排名)接口 */
	public static final String BUSINESS_SEARCH_INFORMATION_RANKING = "15047";
	/** 发布作品(普通作品)接口 */
	public static final String BUSINESS_PUBLISHED_WORKS_COMMON = "15048";
	/** 发布作品(参加比赛作品)接口 */
	public static final String BUSINESS_PUBLISHED_WORKS_GAME = "15049";
	/** 查询比赛邀约宣传(发布作品页面使用)接口 */
	public static final String BUSINESS_SEARCH_SOLICITATION_OF_PUBLICITY = "15050";
	/** 查询作品(普通作品)详情及评论接口 */
	public static final String BUSINESS_SEARCH_COMMON_WORK = "15051";
	/** 查询作品(活动作品)详情及评论接口 */
	public static final String BUSINESS_SEARCH_PARTY_WORK_STRING = "15052";
	/** 查询作品(普通作品OR活动作品)评论列表(带分页)接口 */
	public static final String BUSINESS_SEARCH_WORK_PINLUN_STRING = "15053";
	/** 新增作品评论接口 */
	public static final String BUSINESS_ADD_COMMENT = "15054";
	/** 验证用户是否已提交该活动贴报名作品接口 */
	public static final String BUSINESS_IS_ENROLL = "15055";
	/** 参赛作品投票接口 */
	public static final String BUSINESS_FORVOTE = "15056";
	/** 作品点赞接口 */
	public static final String BUSINESS_DIANZAN = "15058";
	/** 取消作品点赞接口 */
	public static final String BUSINESS_CANCEL_DIANZAN = "15059";
	/** 新增检索记录接口 15060 */
	public static final String BUSINESS_SEARCH_INSERT_CITIAO = "15060";
	/** 删除检索记录接口 15061 */
	public static final String BUSINESS_SEARCH_DELETE_CITIAO = "15061";
	/** 查询热门搜索及用户搜索记录接口 15062 */
	public static final String BUSINESS_SEARCH_GET_CITIAO = "15062";
	/** 查询与搜索词相匹配搜索数据列表接口 15063 */
	public static final String BUSINESS_SEARCH_MATCH = "15063";
	/** 通过搜索词查询相匹配作品列表接口 15064 */
	public static final String BUSINESS_SEARCH_GET_MATHCHWORKS = "15064";
	/** 清空检索记录接口 15065 */
	public static final String BUSINESS_SEARCH_CLEAR = "15065";
	/** 删除作品接口 15067 */
	public static final String BUSINESS_EDIT_WORK = "15067";
	/** 删除作品接口 15068 */
	public static final String BUSINESS_DELETE_WORK = "15068";
	/** 收藏作品接口 15069 */
	public static final String BUSINESS_COLLECT_WORK = "15069";
	/** 查询赛事(参赛作品)接口 (赛事-首页) */
	public static final String BUSINESS_MATCH_WORKS = "15070";
	/** 查询赛事赛区接口 (赛事-赛区) */
	public static final String BUSINESS_MATCH_AREA = "15072";
	/** 查询冠军排名接口 (赛事-冠军排名) */
	public static final String BUSINESS_MATCH_RAINKING = "15073";
	/** 查询赛事检索数据接口 (赛事-检索框) */
	public static final String BUSINESS_MATCH_WORKSSEARCH = "15075";
	/** 秀场首页进入赛事 查询matchId 接口 */
	public static final String BUSINESS_MATCH_ID = "15076";
	/** 删除作品(参赛作品) 接口 */
	public static final String BUSINESS_DELETE_MATCHWORK = "15077";
	/** 查询赛事介绍H5对应URL接口 (赛事-赛事介绍) 15078 */
	public static final String BUSINESS_MATCH_INTRODUCE = "15078";

	/** 查询与搜索词相匹配搜索数据列表接口(赛事页面搜索) */
	public static final String BUSINESS_MATCH_SEARCH_CITIAO = "15079";
	/** 往期赛事结果(首页) */
	public static final String BUSINESS_PAST_MATCH = "15080";
	/** 我的-我的秀场列表数据接口 15081 */
	public static final String BUSINESS_MINE_MYSHOW = "15081";
	/** 我的-我的秀场删除数据接口 15082 */
	public static final String BUSINESS_MINE_DELETE_MYSHOW = "15082";
	/** 我的-我的收藏列表数据接口 15083 */
	public static final String BUSINESS_MINE_MYCOLLECT = "15083";
	/** 我的-我的收藏删除数据接口 15084 */
	public static final String BUSINESS_MINE_DELETE_MYCOLLECT = "15084";

	/** 用户发布帖子接口(新开2016-03-10号 */
	public static final String BUSINESS_COMMUNITY_PUBLISHNEWPOST = "15086";
	/** 查询社区版块话题列表by boardId and roleTypes(发布帖子时使用)接口 */
	public static final String BUSINESS_COMMUNITY_TOPIC = "15087";
	/** 查询赛事帖子列表 */
	public static final String BUSINESS_SEARCH_MATCH_INTRODUCE = "15090";
	/** 我的社区-我发布帖子(帖子列表)接口 */
	public static final String BUSINESS_MYCOMMUNITYPOST = "15093";
	/** 我的社区-我的回复(帖子列表)接口 */
	public static final String BUSINESS_MYCOMMUNITYREPLY = "15094";
	/** 我的社区-我的收藏(帖子列表)接口 */
	public static final String BUSINESS_MYCOMMUNCOLLECT = "15095";
	// ==========================美库百科==================================
	/** 发布个人百科接口 */
	public static final String BUSINESS_PERSONBAIKE = "21001";
	/** 发布企业百科接口 */
	public static final String BUSINESS_COMPANYBAIKE = "21002";
	/** 公共词条百科接口 */
	public static final String BUSINESS_COMMONBAIKE = "21003";
	/** 根据条件查询百科列表 */
	public static final String BUSINESS_SEARCHBAIKE = "21004";
	/** 查询百科详情接口 */
	public static final String BUSINESS_BAIKEINFO = "21005";
	/** 查询我的百科词条 */
	public static final String BUSINESS_MYCITIAO = "21006";
	/** 查询我的收藏 */
	public static final String BUSINESS_CITIAOCOLLECT = "21007";
	/** 收藏一条词条 */
	public static final String BUSINESS_COLLECT = "21008";
	/** 取消收藏词条 */
	public static final String BUSINESS_CANCEL = "21009";
	/** 修改词条的概述图 */
	public static final String BUSINESS_BK_GAISHUPIC = "21010";
	/** 删除词条的一张自定义相片 */
	public static final String BUSINESS_BK_XC = "21011";
	/** 新增词条的一张自定义相片 */
	public static final String BUSINESS_BK_ADDXCPIC = "21012";
	/** 新增词条的一条自定义内容 */
	public static final String BUSINESS_BK_ADDONEDETAIL = "21013";
	/** 修改词条的一条自定义内容 */
	public static final String BUSINESS_BK_DITONEDETAIL = "21014";
	/** 删除词条的一条自定义内容 */
	public static final String BUSINESS_BK_DELETEONEDETAIL = "21015";
	/** 修改词条首页内容 */
	public static final String BUSINESS_BK_EDITFIRSTPAGE = "21016";
	/** 修改词条第二页内容 */
	public static final String BUSINESS_BK_EDITSECONDPAGE = "21017";
	/** 新增词条的一条自定义基本内容 */
	public static final String BUSINESS_BK_ADDONEBASE = "21018";
	/** 修改词条的一条自定义基本内容 */
	public static final String BUSINESS_BK_EDITONEBASE = "21019";
	/** 删除词条的一条自定义基本内容 */
	public static final String BUSINESS_BK_DELETEONEBASE = "21020";
	/** 删除一个百科词条 */
	public static final String BUSINESS_DELETEBAIKE = "21021";
	/** 检测用户是否可以继续创建词条 */
	public static final String BUSINESS_STATUSISCREATE = "21022";

	/** 付费前下单 */
	public static final String BUSINESS_PAY_ORDER = "22002";
	/** 生成三方平台预定订单 */
	public static final String BUSINESS_THREEDINGDAN = "22000";
	/** 查询支付结果接口 */
	public static final String BUSINESS_CHECKPAYRESULT = "22003";
	/** 删除订单接口 */
	public static final String BUSINESS_ZX_22004 = "22004";
	/** 支付订单列表 */
	public static final String BUSINESS_ZX_22005 = "22005";
	/** 根据订单编号查询订单详情接口 */
	public static final String BUSINESS_ZX_22006 = "22006";
	/** 取消订单接口 */
	public static final String BUSINESS_ZX_22007 = "22007";
	/** 查询美库余额 */
	public static final String BUSINESS_ZX_22008 = "22008";
	/** —————————————————————————— 找装修接口——————————————————————— */
	public final static String PUBLICK_DECORATION = "apiDecorate.action";
	/** 在线问答发帖接口 **/
	public static final String BUSINESS_PUBLISHPOST = "300000";
	/** 在线问答发帖时加载数据(话题数据)接口 **/
	public static final String BUSINESS_POSTTOPIC = "300001";
	/** 装修攻略列表接口 **/
	public static final String BUSINESS_STRATEGYList = "300002";
	/** 根据商铺类型查询案例 **/
	public static final String BUSINESS_ZX_ZXANLI = "300003";
	/** 找装修首页配置数据 **/
	public static final String BUSINESS_ZX_HOME = "300004";
	/** 找装修公司或案例搜索列表接口 **/
	public static final String BUSINESS_ZX_300005 = "300005";
	/** 找装修设计公司推荐接口 **/
	public static final String BUSINESS_ZX_300006 = "300006";
	/** 找装修公司详情接口 **/
	public static final String BUSINESS_ZX_300007 = "300007";
	/** 找装修公司对应的案例列表接口 **/
	public static final String BUSINESS_ZX_300008 = "300008";
	/** 找装修智能报价接口 **/
	public static final String BUSINESS_ZX_ZNBJ = "300009";
	/** 找装修美库装修保申请接口 **/
	public static final String BUSINESS_ZX_MKZXB = "300010";
	/** 找装修公司认证接口 **/
	public static final String BUSINESS_ZX_300011 = "300011";
	/** 找装修我的发布中我的案例列表接口 **/
	public static final String BUSINESS_ZX_300012 = "300012";
	/** 找装修我的发布中添加案例接口 **/
	public static final String BUSINESS_ZX_300013 = "300013";
	/** 找装修我的发布中发布地区列表接口 **/
	public static final String BUSINESS_ZX_300014 = "300014";
	/** 找装修发起意向申请接口 **/
	public static final String BUSINESS_ZX_300015 = "300015";
	/** 找装修我的发布中收到意向列表接口 **/
	public static final String BUSINESS_ZX_300016 = "300016";
	/** 找装修我的发布中装修优惠详情(编辑或详情用)接口 **/
	public static final String BUSINESS_ZX_300017 = "300017";
	/** 找装修我的发布中装修优惠修改接口 **/
	public static final String BUSINESS_ZX_300018 = "300018";
	/** 找装修我的发布中装修优惠列表接口 **/
	public static final String BUSINESS_ZX_300019 = "300019";
	/** 找装修我的发布中装修优惠开启关闭控制接口 **/
	// 开启发布地区 1 装修案例 2 装修优惠3
	public static final String BUSINESS_ZX_300020 = "300020";
	/** 找装修我的店家对案例或公司收藏接口 **/
	public static final String BUSINESS_ZX_300021 = "300021";
	/** 找装修我的店家对案例或公司取消收藏接口 **/
	public static final String BUSINESS_ZX_300022 = "300022";
	/** 找装修我的发布中刷新(包含发布地区 我的案例 )接口 **/
	// 刷新类型 包含发布地区1 我的案例2
	public static final String BUSINESS_ZX_300023 = "300023";
	/** 找装修我的发布中(包含发布地区 我的案例 收到意向 公司优惠等)删除接口 **/
	// 删除类型 包含发布地区 1 我的案例 2 收到意向3 公司优惠 4等
	public static final String ZX_300024 = "300024";
	/** 找装修查询案例详情接口 **/
	public static final String BUSINESS_ZX_300025 = "300025";
	/** 找装修发布装修优惠详情接口 **/
	public static final String BUSINESS_ZX_300026 = "300026";
	public static final String BUSINESS_ZX_300027 = "300027";
	/** 免费设计公司列表 **/
	public static final String BUSINESS_ZX_300028 = "300028";
	/** 找装修购买地区未到期列表接口 **/
	public static final String BUSINESS_ZX_300029 = "300029";
	/** 找装修购买地区未到期置顶城市列表接口 **/
	public static final String BUSINESS_ZX_300030 = "300030";
	/** 编辑案例主体 **/
	public static final String BUSINESS_ZX_300031 = "300031";
	/** 删除案例详情 **/
	public static final String BUSINESS_ZX_300032 = "300032";
	/** 编辑案例详情 **/
	public static final String BUSINESS_ZX_300033 = "300033";
	/** 找装修用于地区续费查询地区接口 **/
	public static final String BUSINESS_ZX_300034 = "300034";
	/** 编辑添加案例详情 **/
	public static final String BUSINESS_ZX_300035 = "300035";
	/** 评论公司 **/
	public static final String BUSINESS_ZX_300036 = "300036";
	/** 找装修计算根节点 计算费用 **/
	public static final String BUSINESS_ZX_300039 = "300039";
	/** 找装修计算根节点 计算续费 **/
	public static final String BUSINESS_ZX_300040 = "300040";
	/** 发布装修申请 **/
	public static final String BUSINESS_ZX_300041 = "300041";
	/** 查询申请装修需求详情 **/
	public static final String BUSINESS_ZX_300042 = "300042";
	/** 根据id编辑发布需求 **/
	public static final String BUSINESS_ZX_300043 = "300043";
	/** 根据用户编号查询申请的列表 **/
	public static final String BUSINESS_ZX_300044 = "300044";
	/** 根据地区查询申请的列表 **/
	public static final String BUSINESS_ZX_300045 = "300045";
	/** 关闭或开启申请 **/
	public static final String BUSINESS_ZX_300046 = "300046";
	/** 删除申请 **/
	public static final String BUSINESS_ZX_300047 = "300047";
	/** 发布规则h5,一周店铺 **/
	public static final String BUSINESS_ZX_300048 = "300048";
	/** 用于终端信息与服务器校验群信息是否一致 新版 2016年11月8日 15:52:52 jsg */
	public static final String BUSINESS_GP_18046 = "18046";

	/** ------------找策划RequestMapping---------------- */
	public final static String PLAN_REQUEST_MAPPING = "apiPlan.action";
	/** 首页搜索接口 **/
	public static final String PLAN_500001 = "500001";
	/** 找策划列表集合查询接口 **/
	public static final String PLAN_500002 = "500002";
	/** 找策划案例评论添加接口 **/
	public static final String PLAN_500004 = "500004";
}
