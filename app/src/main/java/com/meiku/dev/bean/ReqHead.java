package com.meiku.dev.bean;

import com.meiku.dev.MrrckApplication;
import com.meiku.dev.config.AppConfig;
import com.meiku.dev.config.AppContext;
import com.meiku.dev.config.ConstantKey;
import com.meiku.dev.utils.VersionUtils;

//请求报文头
public class ReqHead {
	private String retStatus;
	private String retMessage;
	private String devId;
	private String devType;
	private String userType;
	private int userId;
	private String appVersion;
	private String osVersion;
	private String businessId;
	private String header;
	private String secretFlag;
	/**用户生成的唯一标识ID */
    private String jsessionId;
    /**是否是压缩 1:表示压缩 */
    private String zipFlag;
    
	public ReqHead(String businessId) {
		this.retStatus = "1";
        this.retMessage="1";
        this.devId=VersionUtils.getDeviceId(); //AppData.getInstance().getDevUUID());
        this.devType=ConstantKey.DEV_TYPE;//Android手机统一是1
        this.userType="1";
        this.userId = AppContext.getInstance().getUserInfo().getId();
        this.appVersion=VersionUtils.getVersionName(MrrckApplication.getInstance());//AppData.getInstance().getAppVersion());
        this.osVersion=""; //AppData.getInstance().getSystemVersion());
        this.businessId = businessId;
        this.jsessionId = AppContext.getInstance().getJsessionId();
        this.zipFlag = AppConfig.NOT_COMPRESS;//默认不压缩
	}
	
	public String getRetStatus() {
		return retStatus;
	}
	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus;
	}
	public String getRetMessage() {
		return retMessage;
	}
	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getDevType() {
		return devType;
	}
	public void setDevType(String devType) {
		this.devType = devType;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}

	public String getSecretFlag() {
		return secretFlag;
	}

	public void setSecretFlag(String secretFlag) {
		this.secretFlag = secretFlag;
	}

	public String getJsessionId() {
		return jsessionId;
	}

	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}

	public String getZipFlag() {
		return zipFlag;
	}

	public void setZipFlag(String zipFlag) {
		this.zipFlag = zipFlag;
	}

}
