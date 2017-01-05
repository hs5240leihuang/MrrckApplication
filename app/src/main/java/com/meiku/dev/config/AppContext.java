package com.meiku.dev.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.text.TextUtils;

import com.meiku.dev.bean.FriendEntity;
import com.meiku.dev.bean.GroupEntity;
import com.meiku.dev.bean.GroupUserEntity;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.bean.MkUser;
import com.meiku.dev.db.MsgDataBase;
import com.meiku.dev.utils.JsonUtil;
import com.meiku.dev.utils.PreferHelper;

/**
 * 全局用户信息
 * 
 * @author 库
 * 
 */
public class AppContext {

	private static MkUser loginUser; // 放在缓存中的用户信息

	// 已经自行实例化
	private static final AppContext single = new AppContext();

	private AppContext() {

	}

	// 静态工厂方法
	public static AppContext getInstance() {
		return single;
	}

	/**
	 * 返回当前登录人信息;未登录，会返回游客信息
	 * 
	 * @return
	 */
	public MkUser getUserInfo() {
		if (loginUser == null) {
			initUser();
			if (loginUser == null) {
				MkUser user = new MkUser();
				user.setId(-1);
				user.setUserId(-1);
				user.setNickName("游客");
				user.setHeadPicUrl("");
				loginUser = user;
			}
		}
		return loginUser;
	}

	public String getJsessionId() {
		return (String) PreferHelper.getSharedParam(ConstantKey.JSESSIONID, "");
	}

	public void setJsessionId(String jsessionId) {
		PreferHelper.setSharedParam(ConstantKey.JSESSIONID, jsessionId);
	}

	/**
	 * 判断当前是否登陆,true=已登陆
	 */
	public boolean isHasLogined() {
		initUser();
		if (loginUser == null) {
			return false;
		} else {
			if (loginUser.getId() == -1) { // 为游客登录
				return false;
			}
			return true;
		}
	}

	/**
	 * 是否登录并且资料完善
	 * 
	 * @return
	 */
	public boolean isLoginedAndInfoPerfect() {
		// return isHasLogined() && !Tool.isEmpty(loginUser.getNickName())
		// && !Tool.isEmpty(loginUser.getPosition())
		// && !Tool.isEmpty(loginUser.getClientThumbHeadPicUrl())
		// && !Tool.isEmpty(loginUser.getBirthDate())
		// && !Tool.isEmpty(loginUser.getGender());
		return isHasLogined();
	}

	// 把最新用户信息设置到本地
	public void setLocalUser(MkUser bean) {
		loginUser = bean;
		PreferHelper.setSharedParam(ConstantKey.USER, JsonUtil.objToJson(bean));
	}

	// 设置当前用户信息为空
	public void setLocalUserEmpty() {
		loginUser = null;
		PreferHelper.setSharedParam(ConstantKey.USER, "");
		PreferHelper.setSharedParam(ConstantKey.JSESSIONID, "");
		PreferHelper.setSharedParam(ConstantKey.KEY_USER_ACCOUNT, "");
		PreferHelper.setSharedParam(ConstantKey.KEY_USER_TOKEN, "");
	}

	// 初始化用户信息到缓存
	public void initUser() {
		String gsuser = (String) PreferHelper.getSharedParam(ConstantKey.USER,
				"");
		if (!TextUtils.isEmpty(gsuser)) {
			loginUser = (MkUser) JsonUtil.jsonToObj(MkUser.class, gsuser);
		} else {
			loginUser = null;
		}
	}

	/**
	 * 设置数据库版本信息
	 */
	public void setAppDBVersion(String appDBVersion) {
		PreferHelper.setSharedParam(ConstantKey.DB_VERSION, appDBVersion);
	}

	/**
	 * 获取数据库版本信息
	 */
	public String getAppDBVersion() {
		return (String) PreferHelper.getSharedParam(ConstantKey.DB_VERSION, "");
	}

	/**
	 * 设置采用新版本消息数据库标识
	 */
	// public void setMsgDbFlag(boolean flag) {
	// PreferHelper.setSharedParam(ConstantKey.MSG_DB_VERSION, flag);
	// }

	/**
	 * 当前APP是否已采用新版本数据库 true=已采用；false=未采用新版本
	 */
	// public boolean getMsgDbFlag() {
	// return (Boolean) PreferHelper.getSharedParam(
	// ConstantKey.MSG_DB_VERSION, false);
	// }

	private static List<FriendEntity> addressList;// 放在缓存的通讯录
	private static List<GroupEntity> groupList = new ArrayList<GroupEntity>();// 放在缓存的群组
	// private static List<DefriendEntity> blackList;// 放在缓存的黑名单

	// 群组缓存
	private static Map<Integer, String> groupMap = new HashMap<Integer, String>();// 群组头像
	private static Map<String, Integer> groupTalkIDMap = new HashMap<String, Integer>();// 群组聊天ID对应群组ID
	private static Map<Integer, String> myGroupNickNameMap = new HashMap<Integer, String>();// 我在各个群组的备注昵称
	private static Map<Integer, Map<String, String>> groupMemberNickNameMap = new HashMap<Integer, Map<String, String>>();// 群组对应群成员再此群的昵称的
	private static Map<Integer, String> groupNameMap = new HashMap<Integer, String>();// 群组ID对应群组名称的映射
	// 好友缓存
	private static Map<Integer, String> remarkMap = new HashMap<Integer, String>();// 备注名和用户ID的映射
	private static Map<Integer, String> headImgMap = new HashMap<Integer, String>();// 备注名和用户ID的映射
	private static Set<Integer> friendSet = new HashSet<Integer>();// 缓存好友的ID
	private static Map<String, Integer> friendTalkIdMap = new HashMap<String, Integer>();// 好友聊天ID对应好友ID的映射
	private static Set<Integer> blackSet = new HashSet<Integer>();// 缓存黑名单的ID

	public static Set<Integer> getFriendSet() {
		return friendSet;
	}

	public static List<FriendEntity> getAddressList() {
		return addressList;
	}

	// 初始化好友ID的集合和昵称映射
	public static void setAddressList(List<FriendEntity> addressList) {
		AppContext.addressList = addressList;
		for (FriendEntity bean : addressList) {
			friendTalkIdMap
					.put(bean.getLeanCloudUserName(), bean.getFriendId());
			remarkMap.put(bean.getFriendId(), bean.getAliasName());
			headImgMap.put(bean.getFriendId(), bean.getClientThumbHeadPicUrl());
			friendSet.add(bean.getFriendId());

			// 存、更新人员数据库
			IMYxUserInfo userInfo = new IMYxUserInfo();
			userInfo.setNickName(bean.getAliasName());
			userInfo.setUserHeadImg(bean.getClientThumbHeadPicUrl());
			userInfo.setYxAccount(bean.getLeanCloudUserName());
			userInfo.setUserId(bean.getFriendId());
			MsgDataBase.getInstance().saveOrUpdateYxUser(userInfo);
		}
	}

	public static List<GroupEntity> getGroupList() {
		return groupList;
	}

	public static void setGroupList(List<GroupEntity> list) {
		groupList.clear();
		groupList.addAll(list);
		groupMap.clear();
		for (GroupEntity bean : groupList) {
			groupTalkIDMap.put(bean.getOtherId(), bean.getId());
			groupMap.put(bean.getId(), bean.getClientThumbGroupPhoto());
			myGroupNickNameMap.put(bean.getId(), bean.getNickName());
			groupNameMap.put(bean.getId(), bean.getGroupName());
			Map<String, String> nickMap = new HashMap<String, String>();
			for (GroupUserEntity user : bean.getGroupUserList()) {
				nickMap.put(user.getLeanCloudUserName(), user.getNickName());
			}
			if (nickMap!=null) {
				groupMemberNickNameMap.put(bean.getId(), nickMap);
			}
		}
	}

	public static Map<Integer, String> getRemarkMap() {
		return remarkMap;
	}

	public static Map<Integer, String> getGroupMap() {
		return groupMap;
	}

	public static void setGroupMap(Map<Integer, String> map) {
		groupMap = map;
	}

	public static Set<Integer> getBlackSet() {
		return blackSet;
	}

	public static Map<Integer, String> getMyGroupNickNameMap() {
		return myGroupNickNameMap;
	}

	public static Map<String, Integer> getGroupTalkIDMap() {
		return groupTalkIDMap;
	}

	public static void setGroupTalkIDMap(Map<String, Integer> groupTalkIDMap) {
		AppContext.groupTalkIDMap = groupTalkIDMap;
	}

	public static Map<String, Integer> getFriendTalkIdMap() {
		return friendTalkIdMap;
	}

	public static void setFriendTalkIdMap(Map<String, Integer> friendTalkIdMap) {
		AppContext.friendTalkIdMap = friendTalkIdMap;
	}

	public static Map<Integer, String> getHeadImgMap() {
		return headImgMap;
	}

	public static void setHeadImgMap(Map<Integer, String> headImgMap) {
		AppContext.headImgMap = headImgMap;
	}

	public static Map<Integer, String> getGroupNameMap() {
		return groupNameMap;
	}

	public static void setGroupNameMap(Map<Integer, String> groupNameMap) {
		AppContext.groupNameMap = groupNameMap;
	}

	public static Map<Integer, Map<String, String>> getGroupMemberNickNameMap() {
		return groupMemberNickNameMap;
	}

	public static void setGroupMemberNickNameMap(
			Map<Integer, Map<String, String>> groupMemberNickNameMap) {
		AppContext.groupMemberNickNameMap = groupMemberNickNameMap;
	}

}
