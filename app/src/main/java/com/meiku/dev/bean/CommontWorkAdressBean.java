package com.meiku.dev.bean;


/**
 * Created by Administrator on 2015/8/31.
 */
public class CommontWorkAdressBean {
    private String name;
    private String cityid;
    private String uid;
    private String district;
    private String city;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CommontWorkAdressBean(){

    }
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
