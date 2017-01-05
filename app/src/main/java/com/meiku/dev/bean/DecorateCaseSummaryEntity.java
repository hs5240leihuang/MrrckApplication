package com.meiku.dev.bean;

import java.util.List;


/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：找装修概述描述
 * 类名称：com.mrrck.ap.decorate.entity.DecorateCaseSummaryEntity     
 * 创建人：陈卫
 * 创建时间：2016-8-30 下午08:22:02   
 * @version V1.0
 */
public class DecorateCaseSummaryEntity extends MkDecorateCaseSummary{

	/** 描述附件集合 */
	private List<MkDecorateAttachment> daList;

	public List<MkDecorateAttachment> getDaList() {
		return daList;
	}

	public void setDaList(List<MkDecorateAttachment> daList) {
		this.daList = daList;
	}
	
	
}
