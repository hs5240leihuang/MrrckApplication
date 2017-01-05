package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：产品收藏Entity
 * 类名称：com.mrrck.ap.pd.entity.ProductCollectEntity     
 * 创建人：仲崇生
 * 创建时间：2016-3-2 下午07:20:31   
 * @version V1.0
 */
public class ProductCollectEntity extends MkProductCollect{
    
    /** 收藏编号 重命名 */
    private Integer productCollectId;
    
    /** 页码 */
    private Integer offset;
    
    /** 每页条数 */
    private Integer pageNum;
    
    /** 产品名称 */
    private String productName;

    /** 海报主图 */
    private String posterMain;

    /** 海报缩略图 */
    private String posterThum;

    /** 招商省份编码(多个逗号分隔) */
    private String provinceCodes;

    /** 招商省份名称(多个逗号分隔) */
    private String provinceNames;

    /** 公司编号(个人认证时为0) */
    private Integer companyId;
    
    /** 公司名称 */
    private String companyName;
    
    /** 海报主图(客户端使用) */
    private String clientPosterMain;
    
    /** 海报缩略图(客户端使用) */
    private String clientPosterThum;
    
    public String getClientCreateDate() {
		return clientCreateDate;
	}

	public void setClientCreateDate(String clientCreateDate) {
		this.clientCreateDate = clientCreateDate;
	}

	/** 客户段用的日期*/
    private String clientCreateDate;

    public Integer getProductCollectId() {
        return this.productCollectId;
    }

    public void setProductCollectId(Integer productCollectId) {
        this.productCollectId = productCollectId;
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

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPosterMain() {
        return this.posterMain;
    }

    public void setPosterMain(String posterMain) {
        this.posterMain = posterMain;
    }

    public String getPosterThum() {
        return this.posterThum;
    }

    public void setPosterThum(String posterThum) {
        this.posterThum = posterThum;
    }

    public String getProvinceCodes() {
        return this.provinceCodes;
    }

    public void setProvinceCodes(String provinceCodes) {
        this.provinceCodes = provinceCodes;
    }

    public String getProvinceNames() {
        return this.provinceNames;
    }

    public void setProvinceNames(String provinceNames) {
        this.provinceNames = provinceNames;
    }

    public Integer getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getClientPosterMain() {
            return this.clientPosterMain;
    }

    public void setClientPosterMain(String clientPosterMain) {
        this.clientPosterMain = clientPosterMain;
    }

    public String getClientPosterThum() {
            return this.clientPosterThum;
    }

    public void setClientPosterThum(String clientPosterThum) {
        this.clientPosterThum = clientPosterThum;
    }
    
}
