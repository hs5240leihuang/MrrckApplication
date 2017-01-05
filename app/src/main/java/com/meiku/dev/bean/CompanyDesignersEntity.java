package com.meiku.dev.bean;

import com.google.gson.JsonArray;


/**
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：全部功能表
 * 类名称：com.mrrck.db.entity.MkAllFunction     
 * 创建人：仲崇生
 * 创建时间：2015-7-21 上午09:42:19   
 * @version V1.0
 */
public class CompanyDesignersEntity extends MkCompanyDesigners{
	JsonArray fileUrlJSONArray;

	public JsonArray getFileUrlJSONArray() {
		return fileUrlJSONArray;
	}

	public void setFileUrlJSONArray(JsonArray fileUrlJSONArray) {
		this.fileUrlJSONArray = fileUrlJSONArray;
	}

}

