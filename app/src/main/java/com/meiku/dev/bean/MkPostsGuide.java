package com.meiku.dev.bean;

public class MkPostsGuide {
	/** 编号 */
	private Integer id;

 	/** 用户编号 */
	private Integer userId;

 	/** 帖子编号 */
	private Integer postsId;
	
	/** 活动贴图片 */
	private String imgUrl;

 	/** 活动投票开始时间 */
	private String startDate;

 	/** 活动投票结束时间 */
	private String endDate;
	
	/** 报名开始时间 */
    private String applyStartDate;

 	/** 报名截止时间 */
	private String applyEndDate;

 	/** 已报名人数 */
	private Integer applyNum;

 	/** 活动最大上限人数 */
	private Integer applyMaxNum;

 	/** 活动规则 */
	private String activeRule;

 	/** 奖项设置 */
	private String activePrize;

 	/** 是否外链URL 0:不是 1:是 默认0 */
	private Integer isChain;

 	/** 外部链接URL */
	private String chainURL;

 	/** 创建时间 */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除状态 0:正常1:逻辑删除 */
	private Integer delStatus;
	
	/** 活动类型，0:图片 1:视频 */
	private String activeType;
	
	/** 全国code(固定值100000) */
    private Integer wholeCode;
    
    /** 行政区划编码(省份code,多个逗号分隔) */
    private String activeProvinceCode;
    
    /** 行政区划编码(城市code,多个逗号分隔) */
    private String activeCityCode;
    
    /** 行政区划城市名称(城市code对应名称,多个逗号分隔) */
    private String activeCityName;
    
    /** 是否是推荐活动标识  0:不是 1:是 */
    private Integer recommendFlag;
    
    /** 推荐图片URL */
    private String recommendImgUrl;
    
    /** 活动允许报名分类(多个逗号分隔) */
    private String categoryId;
    
    /** 赛事编号 */
    private Integer matchId;
    
    /** 赛事赛区编号 */
    private Integer matchCityId;
    
    /** 赛区比赛年 */
    private String matchYear;
    
    /** 比赛月份 */
    private String matchMonth;
    /** 内容 */
    private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return id;
	}

	public void setUserId(Integer userId){
		this.userId=userId;
	}

	public Integer getUserId(){
		return userId;
	}

	public void setPostsId(Integer postsId){
		this.postsId=postsId;
	}

	public Integer getPostsId(){
		return postsId;
	}

	public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setStartDate(String startDate){
		this.startDate=startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setEndDate(String endDate){
		this.endDate=endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public String getApplyStartDate() {
        return this.applyStartDate;
    }

    public void setApplyStartDate(String applyStartDate) {
        this.applyStartDate = applyStartDate;
    }

    public void setApplyEndDate(String applyEndDate){
		this.applyEndDate=applyEndDate;
	}

	public String getApplyEndDate(){
		return applyEndDate;
	}

	public void setApplyNum(Integer applyNum){
		this.applyNum=applyNum;
	}

	public Integer getApplyNum(){
		return applyNum;
	}

	public void setApplyMaxNum(Integer applyMaxNum){
		this.applyMaxNum=applyMaxNum;
	}

	public Integer getApplyMaxNum(){
		return applyMaxNum;
	}

	public void setActiveRule(String activeRule){
		this.activeRule=activeRule;
	}

	public String getActiveRule(){
		return activeRule;
	}

	public void setActivePrize(String activePrize){
		this.activePrize=activePrize;
	}

	public String getActivePrize(){
		return activePrize;
	}

	public void setIsChain(Integer isChain){
		this.isChain=isChain;
	}

	public Integer getIsChain(){
		return isChain;
	}

	public void setChainURL(String chainURL){
		this.chainURL=chainURL;
	}

	public String getChainURL(){
		return chainURL;
	}

	public void setCreateDate(String createDate){
		this.createDate=createDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setUpdateDate(String updateDate){
		this.updateDate=updateDate;
	}

	public String getUpdateDate(){
		return updateDate;
	}

	public void setDelStatus(Integer delStatus){
		this.delStatus=delStatus;
	}

	public Integer getDelStatus(){
		return delStatus;
	}

	public String getActiveType() {
		return activeType;
	}

	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}

    public Integer getWholeCode() {
        return this.wholeCode;
    }

    public void setWholeCode(Integer wholeCode) {
        this.wholeCode = wholeCode;
    }

    public String getActiveProvinceCode() {
        return this.activeProvinceCode;
    }

    public void setActiveProvinceCode(String activeProvinceCode) {
        this.activeProvinceCode = activeProvinceCode;
    }

    public String getActiveCityCode() {
        return this.activeCityCode;
    }

    public void setActiveCityCode(String activeCityCode) {
        this.activeCityCode = activeCityCode;
    }

    public String getActiveCityName() {
        return this.activeCityName;
    }

    public void setActiveCityName(String activeCityName) {
        this.activeCityName = activeCityName;
    }

    public Integer getRecommendFlag() {
        return this.recommendFlag;
    }

    public void setRecommendFlag(Integer recommendFlag) {
        this.recommendFlag = recommendFlag;
    }

    public String getRecommendImgUrl() {
        return this.recommendImgUrl;
    }

    public void setRecommendImgUrl(String recommendImgUrl) {
        this.recommendImgUrl = recommendImgUrl;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getMatchId() {
        return this.matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Integer getMatchCityId() {
        return this.matchCityId;
    }

    public void setMatchCityId(Integer matchCityId) {
        this.matchCityId = matchCityId;
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
}
