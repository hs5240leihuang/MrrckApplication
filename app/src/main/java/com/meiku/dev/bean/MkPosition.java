package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：岗位表 类名称：com.mrrck.db.entity.MkPosition 创建人：仲崇生 创建时间：2015-5-12 下午03:23:54
 * 
 * @version V1.0
 */
public class MkPosition {

	/** 编号 */
	private Integer id;

	/** 父级ID */
	private Integer pid;

	/** 对应库群编号(ID) */
	private Integer groupId;

	/** 用户身份：1.美业从业者2.美业经营者3.美业爱好者 */
	private String type;

	/** 岗位名称 */
	private String name;

	/** 层级 */
	private String level;

	/** 排序 */
	private Integer sortNo;

	/** 创建时间 */
	private String createDate;

	/** 更新时间 */
	private String updateDate;

	/** 删除标记:0:正常1:删除 */
	private String delStatus;

	/** 图标URL */
	private String imgUrl;

	/** 排序 */
	private Integer jobFlag;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getPid() {
		return pid;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getJobFlag() {
		return this.jobFlag;
	}

	public void setJobFlag(Integer jobFlag) {
		this.jobFlag = jobFlag;
	}

}
