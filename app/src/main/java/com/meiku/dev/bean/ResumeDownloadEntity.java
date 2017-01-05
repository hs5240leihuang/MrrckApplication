package com.meiku.dev.bean;

public class ResumeDownloadEntity extends MkResumeDownload{
	 /** 简历人真实姓名 */
    private String resumeRealName;
    
    /**起始数*/
    private Integer offset;
    
    /**每页条数*/
    private Integer pageNum;

    public String getResumeRealName() {
        return this.resumeRealName;
    }

    public void setResumeRealName(String resumeRealName) {
        this.resumeRealName = resumeRealName;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
