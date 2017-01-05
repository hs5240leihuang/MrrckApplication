package com.meiku.dev.config;

/**
 * 广播Action常量
 * 
 * @author Administrator
 * 
 */
public class BroadCastAction {
	/** 登录成功（包括三方） */
	public static final String ACTION_LOGIN_SUCCESS = "com.meiku.dev.action.loginsuccess";
	public static final String ACTION_REFRESH_BOARDLIST = "com.redcos.mrrck.action.refreshboardlist";
	/** 退出登录 */
	public static final String ACTION_MAIN_LOGOUT = "com.meiku.dev.action.logout";
	/** 退出登录 刷新社区主页面 */
	public static final String ACTION_BROAD_REFRESH_HOMEBOARD = "com.meiku.dev.action.refresh.homeboardlist";
	/** 刷新社区首页帖子 */
	public static final String ACTION_BROAD_REFRESH_HOMEPOST = "com.meiku.dev.action.refresh.homeposts";
	/** 报名作品后,广播刷新参赛选手列表 */
	public static final String ACTION_VOTE_REFRESH = "com.meiku.dev.action.refresh.vote";

	/** 完善资料后发广播 */
	public static final String ACTION_PERFECT_INFO = "com.meiku.dev.action.perfectinfo";

	/** 操作关注/未关注后需要刷新更多版中的版列表数据(刷新关注状态) */
	public static final String ACTION_BOARD_REFRESH_GUANZHU = "com.meiku.dev.action.refresh.guanzhu";

	/** 刷新消息页面的广播 */
	public static final String ACTION_IM_REFRESH_MESSAGE_PAGE = "com.meiku.dev.action.refreshmessagepage";
	/** 退群或解散群*/
	public static final String ACTION_IM_REFRESHRECENT_LEAVEGROUP = "com.meiku.dev.action.leavegroup";
	/** 群/通讯录更新*/
	public static final String ACTION_IM_GROUP_UPDATE = "com.meiku.dev.action.updategroup";
	/** 群/通讯录更新*/
	public static final String ACTION_IM_TXL_UPDATE = "com.meiku.dev.action.updatetxl";
	/** 清空群消息 */
	public static final String ACTION_IM_CLEAR_GROUPMESSAGE = "com.meiku.dev.action.cleargroupmessage";
	/** 岗位选择后广播刷新 附近同行 */
	public static final String ACTION_NEARBY_PEOPLE = "com.meiku.dev.action.im.nearbyperson";
	/** 定位改变后广播刷新 附近同行 */
	public static final String ACTION_NEARBY_PEOPLE_BYLOCATION = "com.meiku.dev.action.im.nearbyperson.location";
	/** 刷新黑名单广播 */
	public static final String ACTION_IM_BLACKLIST = "com.meiku.dev.action.im.blacklist";
	/** 刷新通讯录和群组广播 */
	public static final String ACTION_IM_REFRESH_CHANGEBEIZHU = "com.meiku.dev.action.im.beizhu";
	/** 刷新筛选的广播 */
	public static final String ACTION_IM_REFRESH_SHAIXUAN = "com.meiku.dev.action.im.shaixuan";
	/** 刷新企业信息广播 */
	public static final String ACTION_IM_REFRESH_COMPANY = "com.meiku.dev.action.mine.company";
	/** 刷新我的秀场广播 */
	public static final String ACTION_MYSHOW_CITY = "com.meiku.dev.action.myshow.city";
	/** 库女郎才艺秀首页广播 */
	public static final String ACTION_MYSHOW_POSTSID = "com.meiku.dev.action.myshow.postsid";
	/** 点击通知打开时的广播action */
	public static final String ACTION_OPEN_MESSAGENOTIFICATION = "com.meiku.dev.action.openmessagenotification";

	/** 我的产品页面刷新action */
	public static final String ACTION_MYPRODUCT = "com.meiku.dev.action.myproductaction";

	/** 更新单聊消息结果类型 */
	public static final String ACTION_MESSAGE_CHANGERESULT = "com.meiku.dev.action.message.changeresult";
	/** 更新群消息结果类型 */
	public static final String ACTION_GROUPMESSAGE_CHANGERESULT = "com.meiku.dev.action.groupmessage.changeresult";
	/** 更新群消息结果类型 */
	public static final String ACTION_CHANGE_AVATAR = "com.meiku.dev.action.changeavatar";
	/** 重复登录，退出本机登录广播 */
	public static final String ACTION_RELOGINT_DOLOUOUT = "com.meiku.dev.action.tipdologout";
	/** 踢出群，解散群，发广播 */
	public static final String ACTION_GROUP_DISMISS = "com.meiku.dev.action.group.dismiss";
	/** 发布职位，发广播 */
	public static final String ACTION_PUBLIC_POSITION = "com.meiku.dev.action.position.shuaxin";
	/** 搜索简历，职位发广播 */
	public static final String ACTION_PUBLIC_SEARCH_RESUME = "com.meiku.dev.action.resume.getdata";
	/** 绑定手机，已注册广播 */
	public static final String ACTION_PUBLIC_BANGDING_PHONE = "com.meiku.dev.action.bangding.phone";

	/** 微信支付返回 */
	public static final String ACTION_WX_PAYRESULT = "android.com.meiku.dev.action.wxpayresult";
	/** 结算成功广播 */
	public static final String ACTION_PAYRESULT = "android.com.meiku.dev.action.payresult";
	/** 招聘保新增城市广播 */
	public static final String ACTION_RECRUIT_ADD_CITY = "android.com.meiku.dev.action.recuitaddcity";
	/** 我的招聘切换tab */
	public static final String ACTION_RECRUIT_CHANGETAB = "android.com.meiku.dev.action.recuit.changetab";
}
