package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * Created by 库 on 2015/9/25.
 */
public class GroupClassEntity {
    private int  id;
    private String type;
    private String name;
    private String headPhoto;
    private String notice;
    private int groupNum;
    private int sortNo;
    private String createDate;
    private String updateDate;
    private String delStatus;

    public GroupClassEntity () {super();}
    public GroupClassEntity   (DbModel model){
        super();
        this.setId(model.getInt("id"));
        this.setType(model.getString("type"));
        this.setHeadPhoto(model.getString("headPhoto"));
        this.setName(model.getString("name"));
        this.setNotice(model.getString("notice"));
        this.setDelStatus(model.getString("delStatus"));
        this.setGroupNum(model.getInt("groupNum"));
        this.setSortNo(model.getInt("sortNo"));
        this.setCreateDate(model.getString("createDate"));
        this.setUpdateDate(model.getString("updateDate"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
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

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }
}
