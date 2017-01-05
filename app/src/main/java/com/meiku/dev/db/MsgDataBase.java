package com.meiku.dev.db;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.bean.IMDraft;
import com.meiku.dev.bean.IMYxUserInfo;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.XmppConstant;
import com.meiku.dev.utils.LogUtil;
import com.meiku.dev.utils.Tool;

/**
 * 即时通讯数据库
 * 
 * @author 库
 * 
 */
public class MsgDataBase {
	private final static String MSGDB_NAME = "mrrck_msg.db"; // 消息数据库名称
	private final static int MSGDB_VERSION = 3; // 当前消息数据库版本号
	private DbUtils dataBase;

	// 私有的默认构造子
	private MsgDataBase() {
	}

	// 已经自行实例化
	private static MsgDataBase msgsingle = new MsgDataBase();

	// 静态工厂方法
	public static MsgDataBase getInstance() {
		return msgsingle;
	}

	/**
	 * 获取DB
	 * 
	 * @return DbUtils
	 */
	public DbUtils getDB() {
		if (msgsingle == null) {
			msgsingle = new MsgDataBase();
		}
		if (msgsingle.dataBase == null) {
			initDataBase(MrrckApplication.getInstance());
		}
		// 标示开启事务，这样多个线程操作数据库时就不会出现问题了。
		msgsingle.dataBase.configAllowTransaction(true);
		return msgsingle.dataBase;
	}

	/**
	 * 初始化DB
	 * 
	 * @param dbPath
	 *            数据库Path
	 * @param dbName
	 *            数据库名称
	 */
	@SuppressWarnings("static-access")
	private void initDataBase(Context context) {
		msgsingle.dataBase = DbUtils.create(context, MSGDB_NAME, MSGDB_VERSION,
				new DbUpgradeListener() {
					@Override
					public void onUpgrade(DbUtils db, int oldversion,
							int newVersion) {
						if (newVersion > oldversion) {
						}
					}
				}).create(context);
	}

	/**
	 * 初始化消息数据库
	 */
	public void initMsgDb() {
		createIMTable();
	}

	/**
	 * 创建即时通讯 消息记录表和通知表
	 */
	private void createIMTable() {
		try {
			getDB().createTableIfNotExist(IMYxUserInfo.class);
			// getDB().createTableIfNotExist(Notice.class);
			// getDB().createTableIfNotExist(IMDraft.class);
			// getDB().createTableIfNotExist(IMMsgTip.class);
			// getDB().createTableIfNotExist(IMGroupTime.class);
			// getDB().createTableIfNotExist(IMMsgTop.class);
		} catch (DbException e) {
			LogUtil.d("IM", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 保存或修改云信用户信息
	 */
	public boolean saveOrUpdateYxUser(IMYxUserInfo bean) {
		try {
			if (bean == null || bean.getYxAccount() == null) {
				return false;
			}
			if (getYXUserById(bean.getYxAccount()) == null) {// 没有则保存
				getDB().save(bean);
			} else {// 否则更新
				getDB().execNonQuery(
						" update " + XmppConstant.IM_YX_USER_INFO
								+ " set nickName= '" + bean.getNickName()
								+ "', userHeadImg='" + bean.getUserHeadImg()
								+ "' Where yxAccount ='" + bean.getYxAccount()
								+ "' ");
			}
		} catch (Exception e) {
			LogUtil.d("IM", e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据用户ID查询对应信息
	 */
	public IMYxUserInfo getYXUserById(int userId) {
		IMYxUserInfo bean = null;
		try {
			bean = getDB().findFirst(
					Selector.from(IMYxUserInfo.class).where("userId", "=",
							userId));
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return bean;
	}

	/**
	 * 根据用户云信号查询对应信息
	 */
	public IMYxUserInfo getYXUserById(String yxAccount) {
		IMYxUserInfo bean = null;
		try {
			bean = getDB().findFirst(
					Selector.from(IMYxUserInfo.class).where("yxAccount", "=",
							yxAccount));
		} catch (DbException e) {
			e.printStackTrace();
			return null;
		}
		return bean;
	}

	/**
	 * 根据云信账号查询对应信息
	 */
	// public IMYxUserInfo getYXUserByYx(String yxAccount) {
	// IMYxUserInfo bean = null;
	// try {
	// bean = getDB().findFirst(
	// Selector.from(IMDraft.class)
	// .where("yxAccount", "=", yxAccount)
	// .and("curUserId",
	// "=",
	// AppContext.getInstance().getUserInfo()
	// .getId()));
	// } catch (DbException e) {
	// e.printStackTrace();
	// return null;
	// }
	// return bean;
	// }

	/**
	 * 根据条件查询草稿
	 */
	public IMDraft queryDraft(int isRoom, int friendId) {
		IMDraft draft = new IMDraft();
		try {
			draft = getDB().findFirst(
					Selector.from(IMDraft.class)
							.where("friendSubJid", "=", friendId)
							.and("isRoom", "=", isRoom)
							.and("mySubJid",
									"=",
									AppContext.getInstance().getUserInfo()
											.getId()));
		} catch (DbException e) {
			e.printStackTrace();
			return draft;
		}
		return draft;
	}

	/**
	 * 保存草稿
	 */
	public boolean saveIMDraft(IMDraft draft) {
		try {
			if (draft == null || Tool.isEmpty(draft.getContent())) {
				return false;
			}
			delDraftRecord(draft.getIsRoom(), draft.getFriendSubJid());
			getDB().save(draft);
		} catch (DbException e) {
			LogUtil.d("IM", e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 删除草稿 friendSubJid 好友的ID,为聊天群时是该群的ID
	 */
	public boolean delDraftRecord(int isRoom, int friendId) {
		try {
			getDB().delete(
					IMDraft.class,
					WhereBuilder
							.b("friendSubJid", "=", friendId)
							.and("isRoom", "=", isRoom)
							.and("mySubJid",
									"=",
									AppContext.getInstance().getUserInfo()
											.getId()));

		} catch (DbException e) {
			LogUtil.d("IM", e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
