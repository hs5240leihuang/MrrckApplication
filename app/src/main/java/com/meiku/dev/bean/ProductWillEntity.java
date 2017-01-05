package com.meiku.dev.bean;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：产品意向申请Entity
 * 类名称：com.mrrck.ap.pd.entity.ProductWillEntity     
 * 创建人：仲崇生
 * 创建时间：2016-3-2 下午08:37:07   
 * @version V1.0
 */
public class ProductWillEntity extends MkProductWill{
    
    /** 意向编号 重命名 */
    private Integer productWillId;
    
    /** 页码 */
    private Integer offset;
    
    /** 每页条数 */
    private Integer pageNum;
    
    /** 创建时间(客户端显示时间) */
    private String clientCreateDate;

    public Integer getProductWillId() {
        return this.productWillId;
    }

    public void setProductWillId(Integer productWillId) {
        this.productWillId = productWillId;
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

    public String getClientCreateDate() {
        return this.clientCreateDate;
    }

    public void setClientCreateDate(String clientCreateDate) {
        this.clientCreateDate = clientCreateDate;
    }
    
}
