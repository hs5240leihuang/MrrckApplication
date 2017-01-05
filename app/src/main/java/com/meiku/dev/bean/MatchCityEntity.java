package com.meiku.dev.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：赛事赛区Entity 类名称：com.mrrck.ap.bd.entity.MatchCityEntity 创建人：仲崇生
 * 创建时间：2016-2-22 下午04:22:12
 * 
 * @version V1.0
 */
public class MatchCityEntity extends MkMatchCity implements Serializable {
	/** 赛事赛区编号 重命名 */
	private Integer matchCityId;

	/** 赛事赛区对应赛事贴编号 */
	private Integer postsId;

	/** 页码 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;
	
	

	/** 活动允许报名分类(多个逗号分隔) */
    private String categoryId;
    
    /** 参赛作品数据list */
    private List<PostsSignupEntity> postsSignupList;
    
    /** 赛事赛区冠军名称 */
    private String matchCityRankName;
    
    /** 赛事名称 */
    private String matchName;

    public List<PostsSignupEntity> getPostsSignupList() {
		return postsSignupList;
	}

	public void setPostsSignupList(List<PostsSignupEntity> postsSignupList) {
		this.postsSignupList = postsSignupList;
	}

	public String getMatchCityRankName() {
		return matchCityRankName;
	}

	public void setMatchCityRankName(String matchCityRankName) {
		this.matchCityRankName = matchCityRankName;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getPostsId() {
		return this.postsId;
	}

	public void setPostsId(Integer postsId) {
		this.postsId = postsId;
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
	public Integer getMatchCityId() {
		return matchCityId;
	}

	public void setMatchCityId(Integer matchCityId) {
		this.matchCityId = matchCityId;
	}
}
