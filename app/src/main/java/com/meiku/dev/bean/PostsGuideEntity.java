package com.meiku.dev.bean;

public class PostsGuideEntity extends MkPostsGuide {
	/** 活动贴图片(客户端使用) */
	private String clientImgUrl;

	/** 活动贴活动规则URL(客户端使用) */
	private String clientActiveRuleUrl;

	/** 活动贴奖项设置URL(客户端使用) */
	private String clientActivePrizeUrl;

	/** 报名状态 0:报名尚未开始 1:报名进行中 2:报名已结束 */
	private String signupFlag;

	/** 投票状态 0:投票尚未开始 1:投票进行中 2:投票已结束 */
	private String voteFlag;

	/** 推荐图片URL(客户端使用) */
	private String clientRecommendImgUrl;

	/** 活动贴标题 */
	private String title;

	/** 赛区code */
	private Integer matchCityCode;

	/** 页码 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	public String getClientImgUrl() {

		return this.clientImgUrl;

	}

	public void setClientImgUrl(String clientImgUrl) {
		this.clientImgUrl = clientImgUrl;
	}

	public String getClientActiveRuleUrl() {

		return this.clientActiveRuleUrl;

	}

	public void setClientActiveRuleUrl(String clientActiveRuleUrl) {
		this.clientActiveRuleUrl = clientActiveRuleUrl;
	}

	public String getClientActivePrizeUrl() {

		return this.clientActivePrizeUrl;

	}

	public void setClientActivePrizeUrl(String clientActivePrizeUrl) {
		this.clientActivePrizeUrl = clientActivePrizeUrl;
	}

	public String getSignupFlag() {
		return this.signupFlag;
	}

	public void setSignupFlag(String signupFlag) {
		this.signupFlag = signupFlag;
	}

	public String getVoteFlag() {
		return this.voteFlag;
	}

	public void setVoteFlag(String voteFlag) {
		this.voteFlag = voteFlag;
	}

	public String getClientRecommendImgUrl() {

		return this.clientRecommendImgUrl;

	}

	public void setClientRecommendImgUrl(String clientRecommendImgUrl) {
		this.clientRecommendImgUrl = clientRecommendImgUrl;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMatchCityCode() {
		return matchCityCode;
	}

	public void setMatchCityCode(Integer matchCityCode) {
		this.matchCityCode = matchCityCode;
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
}
