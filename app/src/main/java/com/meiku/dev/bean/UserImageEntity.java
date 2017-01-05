package com.meiku.dev.bean;

 

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：相册entity
 * 类名称：com.mrrck.ap.uh.entity.UserImageEntity     
 * 创建人：韩非
 * 创建时间：2015-11-2 下午03:16:30   
 * @version V1.0
 */
public class UserImageEntity {
	
	/** 页码 */
    private Integer offset;
    
    /** 每页条数 */
    private Integer pageNum;
    
    private int imageCount;
    
    private String picUrl;//图片url
    
    private String clientPicUrl;//客户端用图片url

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getClientPicUrl() {
            return this.clientPicUrl;
	}

	public void setClientPicUrl(String clientPicUrl) {
		this.clientPicUrl = clientPicUrl;
	}
	
}
