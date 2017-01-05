package com.meiku.dev.bean;



/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：拉黑表(找同行)
 * 类名称：com.mrrck.db.entity.MkDefriend     
 * 创建人：仲崇生
 * 创建时间：2015-12-10 下午05:46:03   
 * @version V1.0
 */
public class MkDefriend  {
	
 	/** 编号 */
	private Integer id;

 	/** 本人用户编号 */
	private Integer userId;

 	/** 黑名单用户编号 */
	private Integer deFriendId;

 	/** 创建时间(拉黑时间) */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除状态 0:正常 1:解除 */
	private Integer delStatus;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public Integer getDeFriendId() {
        return this.deFriendId;
    }

    public void setDeFriendId(Integer deFriendId) {
        this.deFriendId = deFriendId;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getDelStatus() {
        return this.delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

}
