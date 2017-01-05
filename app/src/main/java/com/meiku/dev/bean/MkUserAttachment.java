package com.meiku.dev.bean;



/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：图片视频公共表
 * 类名称：com.mrrck.db.entity.MkUserAttachment     
 * 创建人：仲崇生
 * 创建时间：2015-6-18 上午10:01:04   
 * @version V1.0
 */
public class MkUserAttachment{
	
 	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 所属模块：1:帖子(秀才艺showPostsId) 2:活动(同城活动activityId) 3:快捷发布(个人中心) 4:公司介绍附件(公司companyId)  5:简历音视频(简历) 6:动态圈(动态表主键编号) */
	private Integer moduleType;

 	/** 所属模块对应主键ID编号 */
	private Integer moduleId;

 	/** 附件类型 0:图片 1:视频 2音频 */
	private String fileType;

 	/** 图片多媒体路径 */
	private String fileUrl;
	
	/** 音视频长度（秒数）附件类型是1,2有值 */
    private Integer fileSeconds;
    
    /** 标题 */
    private String title;
    
    /** 描述 */
    private String remark;

 	/** 排序 默认999 */
	private Integer sortNo;

 	/** 图片宽度（文件为非图片时此字段为空） */
	private String width;

 	/** 图片高度（文件为非图片时此字段为空）*/
	private String height;
	
	/** 父级附件编号 默认:0,引用图片值>0 */
	private Integer parentId;
	
	/** 创建时间*/
    private String createDate;
    
    /** 更新时间*/
    private String updateDate;

 	/** 删除标记0:正常1:删除*/
	private String delStatus;
	
	/** 初始图片是否存在标识 0:正常 1删除*/
    private String delFlag;
    
    /** 用户个人中心(入口)删除标识 0:正常 1:删除 */
    private String delUserPortal;
    
    /** 是否公开 0:公开 1:不公开 默认0 */
    private String isPublic;
    
    /** 是否设为封面 0:不是封面 1:是封面 默认0 */
    private String isCover;

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

    public Integer getModuleType() {
        return this.moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }

    public Integer getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getFileSeconds() {
        return this.fileSeconds;
    }

    public void setFileSeconds(Integer fileSeconds) {
        this.fileSeconds = fileSeconds;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSortNo() {
        return this.sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getDelStatus() {
        return this.delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelUserPortal() {
        return this.delUserPortal;
    }

    public void setDelUserPortal(String delUserPortal) {
        this.delUserPortal = delUserPortal;
    }

    public String getIsPublic() {
        return this.isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getIsCover() {
        return this.isCover;
    }

    public void setIsCover(String isCover) {
        this.isCover = isCover;
    }

}

