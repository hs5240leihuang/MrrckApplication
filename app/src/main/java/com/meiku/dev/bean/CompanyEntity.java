package com.meiku.dev.bean;

import java.util.List;

/**
 * 
 * 版权所有：2015-美库网 项目名称：mrrck-web
 * 
 * 类描述：公司信息Entity 类名称：com.mrrck.ap.rt.entity.CompanyEntity 创建人：仲崇生
 * 创建时间：2015-5-20 下午04:03:37
 * 
 * @version V1.0
 */
public class CompanyEntity extends MkCompany {

	/** 企业介绍附件List */
	private List<UserAttachmentEntity> attachmentList;

	/** 企业介绍编号List */
	private List<Integer> attachmentIds;

	/** 视频缩略图-客户端使用 */
	private String clientVideoPhoto;

	/** 视频-客户端使用 */
	private String clientVideo;

	/** 企业照片logo-客户端使用 */
	private String clientCompanyLogo;

	/** 企业营业执照-客户端使用 */
	private String clientLicense;

	/** 企业类型-中文名称 */
	private String typeName;

	/** 企业规模-中文名称 */
	private String scaleName;

	/** 所在省份-中文名称 */
	private String provinceName;

	/** 所在城市-中文名称 */
	private String cityName;

	private String contactName;
	private String collectFlag;
	/** 企业缩略照片logo-客户端使用 */
	private String clientThumbCompanyLogo;

	private String qualityHonor;
	
	public String getClientThumbCompanyLogo() {
		return clientThumbCompanyLogo;
	}

	public void setClientThumbCompanyLogo(String clientThumbCompanyLogo) {
		this.clientThumbCompanyLogo = clientThumbCompanyLogo;
	}

	public String getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(String collectFlag) {
		this.collectFlag = collectFlag;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public List<UserAttachmentEntity> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<UserAttachmentEntity> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<Integer> getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(List<Integer> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public String getClientVideoPhoto() {

		return clientVideoPhoto;

	}

	public void setClientVideoPhoto(String clientVideoPhoto) {
		this.clientVideoPhoto = clientVideoPhoto;
	}

	public void setClientVideo(String clientVideo) {
		this.clientVideo = clientVideo;
	}

	public void setClientCompanyLogo(String clientCompanyLogo) {
		this.clientCompanyLogo = clientCompanyLogo;
	}

	public void setClientLicense(String clientLicense) {
		this.clientLicense = clientLicense;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getScaleName() {
		return this.scaleName;
	}

	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityCodeName(String cityName) {
		this.cityName = cityName;
	}

	public String getClientVideo() {
		return clientVideo;
	}

	public String getClientCompanyLogo() {
		return clientCompanyLogo;
	}

	public String getClientLicense() {
		return clientLicense;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getQualityHonor() {
		return qualityHonor;
	}

	public void setQualityHonor(String qualityHonor) {
		this.qualityHonor = qualityHonor;
	}

}
