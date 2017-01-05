package com.meiku.dev.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiekun on 2015/7/6.
 */
public class AttachmentListDTO implements Parcelable {
    public String updateDate;
    public int moduleType;
    public int offset;
    public String remark;
    public String delFlag;
    public String title;
    public String isResume;
    public int pageNum;
    public int userId;
    public int parentId;
    public int sortNo;
    public String delUserPortal;
    public String delStatus;
    public String width;
    public int fileSeconds;
    public String isPublic;
    public String fileUrl;
    public int attachmentId;
    public int id;
    public int moduleId;
    public String clientFileUrl;
    public String fileType;
    public String createDate;
    public String height;

    private boolean isChecked;

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsResume(String isResume) {
        this.isResume = isResume;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public void setDelUserPortal(String delUserPortal) {
        this.delUserPortal = delUserPortal;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setFileSeconds(int fileSeconds) {
        this.fileSeconds = fileSeconds;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public void setClientFileUrl(String clientFileUrl) {
        this.clientFileUrl = clientFileUrl;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public int getModuleType() {
        return moduleType;
    }

    public int getOffset() {
        return offset;
    }

    public String getRemark() {
        return remark;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getTitle() {
        return title;
    }

    public String getIsResume() {
        return isResume;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getUserId() {
        return userId;
    }

    public int getParentId() {
        return parentId;
    }

    public int getSortNo() {
        return sortNo;
    }

    public String getDelUserPortal() {
        return delUserPortal;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public String getWidth() {
        return width;
    }

    public int getFileSeconds() {
        return fileSeconds;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public int getId() {
        return id;
    }

    public int getModuleId() {
        return moduleId;
    }

    public String getClientFileUrl() {
        return clientFileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getHeight() {
        return height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.updateDate);
        dest.writeInt(this.moduleType);
        dest.writeInt(this.offset);
        dest.writeString(this.remark);
        dest.writeString(this.delFlag);
        dest.writeString(this.title);
        dest.writeString(this.isResume);
        dest.writeInt(this.pageNum);
        dest.writeInt(this.userId);
        dest.writeInt(this.parentId);
        dest.writeInt(this.sortNo);
        dest.writeString(this.delUserPortal);
        dest.writeString(this.delStatus);
        dest.writeString(this.width);
        dest.writeInt(this.fileSeconds);
        dest.writeString(this.isPublic);
        dest.writeString(this.fileUrl);
        dest.writeInt(this.attachmentId);
        dest.writeInt(this.id);
        dest.writeInt(this.moduleId);
        dest.writeString(this.clientFileUrl);
        dest.writeString(this.fileType);
        dest.writeString(this.createDate);
        dest.writeString(this.height);
    }

    public AttachmentListDTO() {
    }

    protected AttachmentListDTO(Parcel in) {
        this.updateDate = in.readString();
        this.moduleType = in.readInt();
        this.offset = in.readInt();
        this.remark = in.readString();
        this.delFlag = in.readString();
        this.title = in.readString();
        this.isResume = in.readString();
        this.pageNum = in.readInt();
        this.userId = in.readInt();
        this.parentId = in.readInt();
        this.sortNo = in.readInt();
        this.delUserPortal = in.readString();
        this.delStatus = in.readString();
        this.width = in.readString();
        this.fileSeconds = in.readInt();
        this.isPublic = in.readString();
        this.fileUrl = in.readString();
        this.attachmentId = in.readInt();
        this.id = in.readInt();
        this.moduleId = in.readInt();
        this.clientFileUrl = in.readString();
        this.fileType = in.readString();
        this.createDate = in.readString();
        this.height = in.readString();
    }

    public static final Parcelable.Creator<AttachmentListDTO> CREATOR = new Parcelable.Creator<AttachmentListDTO>() {
        public AttachmentListDTO createFromParcel(Parcel source) {
            return new AttachmentListDTO(source);
        }

        public AttachmentListDTO[] newArray(int size) {
            return new AttachmentListDTO[size];
        }
    };
}
