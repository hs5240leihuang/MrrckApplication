package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：找装修 发布需求(正对C端) 类名称：com.mrrck.ap.decorate.entity.DecorateNeedEntity 创建人：陈卫
 * 创建时间：2016-9-15 上午09:58:06
 * 
 * @version V1.0
 */
public class DecorateNeedEntity extends MkDecorateNeed {
	/** 分页页数 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 规范页面 */
	private String h5;

	/** 商铺名称 */
	private String categoryName;

	/** 昵称 */
	private String nickName;

	/** 头像 */
	private String headPicUrl;

	/** 头像绝对地址 */
	private String clientHeadPicUrl;

	/** 头像缩略图 */
	private String clientThumbHeadPicUrl;

	/** 汉字日期 */
	private String clientCreateDate;

	/** 终端用需求名称 */
	private String clientNeedDetail;
	/** 阅读数 */
	private String viewNum;

	/** 列表用到的缩略图片 */
	private String clientMainThumbPicUrl;
	/** 附件图片集合 */
	private List<PostsAttachment> postsAttachmentList;
	/** 载入详情h5 */
	private String loadUrl;

	public String getViewNum() {
		return viewNum;
	}

	public void setViewNum(String viewNum) {
		this.viewNum = viewNum;
	}

	public String getClientMainThumbPicUrl() {
		return clientMainThumbPicUrl;
	}

	public void setClientMainThumbPicUrl(String clientMainThumbPicUrl) {
		this.clientMainThumbPicUrl = clientMainThumbPicUrl;
	}

	public List<PostsAttachment> getPostsAttachmentList() {
		return postsAttachmentList;
	}

	public void setPostsAttachmentList(List<PostsAttachment> postsAttachmentList) {
		this.postsAttachmentList = postsAttachmentList;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}

	public String getClientCreateDate() {
		return clientCreateDate;
	}

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	public String getClientNeedDetail() {
		return clientNeedDetail;
	}

	public void setClientNeedDetail(String clientNeedDetail) {
		this.clientNeedDetail = clientNeedDetail;
	}

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

	public String getH5() {
		return h5;
	}

	public void setH5(String h5) {
		this.h5 = h5;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPicUrl() {
		return headPicUrl;
	}

	public void setHeadPicUrl(String headPicUrl) {
		this.headPicUrl = headPicUrl;
	}

	public String getClientHeadPicUrl() {
		return clientHeadPicUrl;
	}

	public void setClientHeadPicUrl(String clientHeadPicUrl) {
		this.clientHeadPicUrl = clientHeadPicUrl;
	}

	public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

}
