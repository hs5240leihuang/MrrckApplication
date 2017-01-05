package com.meiku.dev.bean;

import com.lidroid.xutils.db.table.DbModel;

/**
 * Created by Administrator on 2015/8/25.
 */
public class DataconfigEntity {
    private int id;
    private String code;
    private String value;
    private String minimum;
    private String maximum;
    private String extra;
    private String extra2;
    private String codeId;
    private String sortNo;
    private String createDate;
    private String updateDate;
    private String delStatus;


    public DataconfigEntity(DbModel model) {
        super();
        this.setId(model.getInt("id"));
        this.setCode(model.getString("code"));
        this.setValue(model.getString("value"));
        this.setMinimum(model.getString("minimum"));
        this.setMaximum(model.getString("maximum"));
        this.setExtra(model.getString("extra"));
        this.setExtra2(model.getString("extra2"));
        this.setCodeId(model.getString("codeId"));
        this.setSortNo(model.getString("sortNo"));
        this.setCreateDate(model.getString("createDate"));
        this.setUpdateDate(model.getString("updateDate"));
        this.setDelStatus(model.getString("delStatus"));
    }

    public DataconfigEntity() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
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

