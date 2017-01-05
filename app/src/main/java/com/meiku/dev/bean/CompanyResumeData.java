package com.meiku.dev.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjh on 2015/8/25.
 */
public class CompanyResumeData implements Serializable{
    public String scale;
    public String videoSeconds;
    public String phone;
    public String clientCompanyLogo;
    public String cityName;
    public String bossTypeName;
    public String introduce;
    public String clientVideo;
    public String cityCode;
    public String videoPhoto;
    public String provinceName;
    public String bossType;
    public String type;
    public String authResult;
    public int id;
    public String brands;
    public String userId;
    public String name;
    public String clientVideoPhoto;
    public String longitude;
    public String license;
    public String createDate;
    public String video;
    public String nameFirstChar;
    public String delStatus;
    public String topFlag;
    public List<CompanyAttachList> attachmentList;
    public List<String> attachmentIds;
    public String clientLicense;
    public String updateDate;
    public String typeName;
    public String authPass;
    public String email;
    public String address;
    public String scaleName;
    public String companyLogo;
    public String goodFlag;
    public String latitude;
    public String website;
    public String qq;
    public String weiXin;


    public static class CompanyResumeReq {
        public CompanyResumeData company;
    }

    public class CompanyAttachList {
        public String clientFileUrl;
        public String remark;
        public String moduleType;
        public String sortNo;
        public String pageNum;
        public String id;
        public String parentId;
        public String title;
        public String height;
        public String attachmentId;
        public String userId;
        public List<String> moduleIds;
        public String offset;
        public String createDate;
        public String fileSeconds;
        public String delStatus;
        public String fileType;
        public String delUserPortal;
        public String moduleId;
        public String width;
        public String isPublic;
        public String isResume;
        public String updateDate;
        public String fileUrl;
        public String isCover;
        public String delFlag;
    }

}
