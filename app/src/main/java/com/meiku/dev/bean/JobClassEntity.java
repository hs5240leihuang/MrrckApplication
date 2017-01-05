package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 岗位分类DAO
 *
 * Created by zjh on 15/6/17.
 */
public class JobClassEntity {

    /**
     * 主键
     */
    private int id;
    /**
     * 父类ID
     */
    private int pid;
    /**
     * 岗位库对应ID
     */
    private int groupid;
    /**
     * 岗位名称
     */
    private String name;
    /**
     * 层级
     */
    private int level;
    /**
     * 删除状态  0 正常使用  1 非正常使用
     */
    private int delStatus;

    private String type;
    
    private String jobFlag;
    private String jobImgUrl;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JobClassEntity(){
        super();
    }

    public JobClassEntity(DbModel model){
        super();
        this.setId(model.getInt("id"));
        this.setPid(model.getInt("pid"));
        this.setGroupid(model.getInt("groupId"));
        this.setName(model.getString("name"));
        this.setLevel(model.getInt("level"));
        this.setDelStatus(model.getInt("delStatus"));
        this.setType(model.getString("type"));
        this.setJobFlag(model.getString("jobFlag"));
        this.setJobImgUrl(model.getString("jobImgUrl"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

	public String getJobFlag() {
		return jobFlag;
	}

	public void setJobFlag(String jobFlag) {
		this.jobFlag = jobFlag;
	}

	public String getJobImgUrl() {
		return jobImgUrl;
	}

	public void setJobImgUrl(String jobImgUrl) {
		this.jobImgUrl = jobImgUrl;
	}

}
