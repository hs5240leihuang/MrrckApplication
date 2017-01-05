package com.meiku.dev.bean;



/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：视频entity
 * 类名称：com.mrrck.ap.uh.entity.UserVedioEntity     
 * 创建人：韩非
 * 创建时间：2015-11-2 下午03:19:02   
 * @version V1.0
 */
public class UserVedioEntity {
	
	/** 页码 */
    private Integer offset;
    
    /** 每页条数 */
    private Integer pageNum;
    
    private String vedioUrl;//视频url
    
    private String clientVedioUrl;//客户端用视频url
    
    private String title;//标题
    /**项目发布时间与当前时间的间隔*/
    private String intervalTime;
    
    private int likeNum;//点赞数
    
    private int commentNum;//回复数
    
    private String createDate;//创建时间
    
    private int postsId;//帖子id

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

	public String getVedioUrl() {
		return vedioUrl;
	}

	public void setVedioUrl(String vedioUrl) {
		this.vedioUrl = vedioUrl;
	}

	public String getClientVedioUrl() {
            return this.clientVedioUrl;
	}

	public void setClientVedioUrl(String clientVedioUrl) {
		this.clientVedioUrl = clientVedioUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntervalTime() {
            return this.intervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getPostsId() {
		return postsId;
	}

	public void setPostsId(int postsId) {
		this.postsId = postsId;
	}

}
