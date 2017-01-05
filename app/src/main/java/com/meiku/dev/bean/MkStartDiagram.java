package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：app启动图配置信息表Entity
 * 类名称：com.mrrck.db.entity.MkStartDiagram     
 * 创建人：仲崇生
 * 创建时间：2016-5-23 下午03:49:53   
 * @version V1.0
 */
public class MkStartDiagram {

 	/** 编号 */
	private Integer id;
	
	/** 创建人编号 */
    private Integer userId;
    
    /** 启动图版本编号 */
    private Integer versionId;

 	/** 启动图配置下载地址 */
	private String picUrl;

	/** 图片规格IOS 1:640*960 2:640*1136 3:750*1334 4:1242*2208
            ANDROID 1:320*480 2:480*800 3:720*960 4:720*1280*/
    private Integer specType;

 	/** 创建时间 */
	private String createDate;
	
	/** 更新时间 */
	private String updateDate;

 	/** 删除标记 0:正常1:删除 */
	private Integer delStatus;

	private String clientPicUrl;
	
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

    public Integer getVersionId() {
        return this.versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getSpecType() {
        return this.specType;
    }

    public void setSpecType(Integer specType) {
        this.specType = specType;
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

	public String getClientPicUrl() {
		return clientPicUrl;
	}

	public void setClientPicUrl(String clientPicUrl) {
		this.clientPicUrl = clientPicUrl;
	}
	
}
