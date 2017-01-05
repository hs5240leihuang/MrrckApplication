package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：赛区结果公布Entity
 * 类名称：com.mrrck.ap.bd.entity.MatchResultEntity     
 * 创建人：仲崇生
 * 创建时间：2016-2-24 下午05:13:32   
 * @version V1.0
 */
public class MatchResultEntity extends MkMatchResult{
    
    /** 赛事赛区对应赛事贴编号 */
    private Integer postsId;

    /** 页码 */
    private Integer offset;
    
    /** 每页条数 */
    private Integer pageNum;
    
    /** 参赛作品数据list */
    private List<PostsSignupEntity> postsSignupList;
    
    /** 赛事编号 */
    private Integer matchId;

    /** 赛区名称 */
    private String matchCityName;
    
    /** 赛区code代码 */
    private String matchCityCode;
    
    /** 赛区比赛年 */
    private String matchYear;
    
    /** 比赛月份 */
    private String matchMonth;
    
    /** 赛区活动贴图片 */
    private String imgUrl;
    
    /** 赛区活动贴图片(客户端使用) */
    private String clientThumbImgUrl;

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

    public List<PostsSignupEntity> getPostsSignupList() {
        return this.postsSignupList;
    }

    public void setPostsSignupList(List<PostsSignupEntity> postsSignupList) {
        this.postsSignupList = postsSignupList;
    }

    public Integer getMatchId() {
        return this.matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public String getMatchCityName() {
        return this.matchCityName;
    }

    public void setMatchCityName(String matchCityName) {
        this.matchCityName = matchCityName;
    }

    public String getMatchCityCode() {
        return this.matchCityCode;
    }

    public void setMatchCityCode(String matchCityCode) {
        this.matchCityCode = matchCityCode;
    }

    public String getMatchYear() {
        return this.matchYear;
    }

    public void setMatchYear(String matchYear) {
        this.matchYear = matchYear;
    }

    public String getMatchMonth() {
        return this.matchMonth;
    }

    public void setMatchMonth(String matchMonth) {
        this.matchMonth = matchMonth;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getClientThumbImgUrl() {
        return this.clientThumbImgUrl;
    }

    public void setClientThumbImgUrl(String clientThumbImgUrl) {
        this.clientThumbImgUrl = clientThumbImgUrl;
    }
    
}
