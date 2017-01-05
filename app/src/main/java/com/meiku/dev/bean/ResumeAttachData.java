package com.meiku.dev.bean;

import java.util.List;

/**
 * 简历视音频
 * <p/>
 * Created by zjh on 2015/8/27.
 */
public class ResumeAttachData {

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
    /** 0未设为简历 1已设为简历 */
    public String isResume;
    public String updateDate;
    public String fileUrl;
    public String isCover;
    public String delFlag;

    public static class ResumeAttachReq{
        public List<ResumeAttachData> attachmentList;
    }

}
