package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * 区域表
 * <p>
 * Created by xiekun on 2015/7/31.
 */
public class AreaEntity {
    private int id;//行政区划id
    private int cityCode;//行政区划编码
    private String cityName;//行政区划名称
    private int parentCode;//上级编码,0：直辖市，null代表省份
    private int level;//级别 1：省、直辖市，2：市，3：区县
    private int sortNo;//排序
    private String createDate;//创建时间
    private int delStatus;//删除状态,0:正常，1:删除
    private int isHot;//是否是热门城市 0:普通 1:热门

    public AreaEntity() {}

    public AreaEntity(DbModel model) {
        super();
        this.setId(model.getInt("id"));
        this.setCityCode(model.getInt("cityCode"));
        this.setCityName(model.getString("cityName"));
        this.setParentCode(model.getInt("parentCode"));
        this.setLevel(model.getInt("level"));
        this.setSortNo(model.getInt("sortNo"));
        this.setCreateDate(model.getString("createDate"));
        this.setDelStatus(model.getInt("delStatus"));
        this.setIsHot(model.getInt("isHot"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getParentCode() {
        return parentCode;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }
}
