package com.meiku.dev.bean;

public class MkDecorateCasePriceApply {
	/**编号*/
	private Integer id;
	/**用户编号*/
	private Integer userId;
	/**'装修公司认证id 关联表'*/
	private Integer companyId;
	/**所属省份*/
	private String provinceName;
	/**所属省份code*/
	private Integer provinceCode;
	/**城市名称*/
	private String cityName;
	/**城市code*/
	private Integer cityCode;
	/**当前申请的姓名*/
	private String name;
	/**当前申请手机号码*/
	private String phone;
	/**更新时间*/
	private String updateDate;
	/**创建时间*/
	private String createDate;
	/**删除标记  0 正常 1删除*/
	private Integer delStatus;
	/**用户头像*/
	private String headPicUrl;
	/**用户头像缩略图*/
	private String clientHeadPicUrl;
	/**是否已读 0 已经读  1 未读*/
	private Integer readFlag;
	/**通知用户*/
	private String title;
	/**公司名称*/
	private String companyName;
	/**地址*/
	private String address;
	/**用户昵称*/
	private String nickName;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Integer getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
}
