package com.meiku.dev.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 版权所有：2016-美库网 项目名称：mrrck-web
 * 
 * 类描述：产品自定义描述Entity 类名称：com.mrrck.ap.pd.entity.ProductDetailEntity 创建人：仲崇生
 * 创建时间：2016-3-1 下午05:22:27
 * 
 * @version V1.0
 */
public class ProductDetailEntity extends MkProductDetail implements Serializable{
	/** 附件数据列表 */
	private List<UserAttachmentEntity> attachmentList;

	public List<UserAttachmentEntity> getAttachmentList() {
		return this.attachmentList;
	}

	public void setAttachmentList(List<UserAttachmentEntity> attachmentList) {
		this.attachmentList = attachmentList;
	}
}
