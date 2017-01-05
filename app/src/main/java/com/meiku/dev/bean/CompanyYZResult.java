package com.meiku.dev.bean;

import com.google.gson.JsonArray;

public class CompanyYZResult {

	JsonArray logo;
	JsonArray license;
	JsonArray photo;
	JsonArray designersList;
	JsonArray customCententList;
	JsonArray companyVideo;
	JsonArray companyVideoPhoto;
	JsonArray qualityHonorPhoto;

	public JsonArray getLogo() {
		return logo;
	}

	public void setLogo(JsonArray logo) {
		this.logo = logo;
	}

	public JsonArray getLicense() {
		return license;
	}

	public void setLicense(JsonArray license) {
		this.license = license;
	}

	public JsonArray getPhoto() {
		return photo;
	}

	public void setPhoto(JsonArray photo) {
		this.photo = photo;
	}

	public JsonArray getDesignersList() {
		return designersList;
	}

	public void setDesignersList(JsonArray designersList) {
		this.designersList = designersList;
	}

	public JsonArray getCustomCententList() {
		return customCententList;
	}

	public void setCustomCententList(JsonArray customCententList) {
		this.customCententList = customCententList;
	}

	public JsonArray getCompanyVideo() {
		return companyVideo;
	}

	public void setCompanyVideo(JsonArray companyVideo) {
		this.companyVideo = companyVideo;
	}

	public JsonArray getCompanyVideoPhoto() {
		return companyVideoPhoto;
	}

	public void setCompanyVideoPhoto(JsonArray companyVideoPhoto) {
		this.companyVideoPhoto = companyVideoPhoto;
	}

	public JsonArray getQualityHonorPhoto() {
		return qualityHonorPhoto;
	}

	public void setQualityHonorPhoto(JsonArray qualityHonorPhoto) {
		this.qualityHonorPhoto = qualityHonorPhoto;
	}

}
