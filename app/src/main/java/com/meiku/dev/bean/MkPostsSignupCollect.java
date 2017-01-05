package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：收藏作品entity
 * 类名称：com.mrrck.db.entity.MkPostsSignupCollect     
 * 创建人：吉陈明
 * 创建时间：2016-2-19 下午04:48:27   
 * @version V1.0
 */
public class MkPostsSignupCollect{

 	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 作品编号*/
	private Integer signupId;

 	/** 创建时间(收藏时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除状态0正常1:逻辑删除 */
	private Integer delStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSignupId() {
		return signupId;
	}

	public void setSignupId(Integer signupId) {
		this.signupId = signupId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}


}
