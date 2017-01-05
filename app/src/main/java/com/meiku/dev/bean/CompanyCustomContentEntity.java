package com.meiku.dev.bean;

import java.util.ArrayList;
import java.util.List;

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
public class CompanyCustomContentEntity extends MkCompanyCustomContent{
	List<UserAttachmentEntity> customFileList;
	
	JsonArray fileUrlJSONArray;
	private ArrayList<String> pics = new ArrayList<String>();
	private ArrayList<String> ids = new ArrayList<String>();
	public List<UserAttachmentEntity> getFileList() {
		return customFileList;
	}

	public void setFileList(List<UserAttachmentEntity> fileList) {
		this.customFileList = fileList;
	}

	public ArrayList<String> getPics() {
		return pics;
	}

	public void setPics(ArrayList<String> pics) {
		this.pics = pics;
	}

	public JsonArray getFileUrlJSONArray() {
		return fileUrlJSONArray;
	}

	public void setFileUrlJSONArray(JsonArray fileUrlJSONArray) {
		this.fileUrlJSONArray = fileUrlJSONArray;
	}

	public ArrayList<String> getIds() {
		return ids;
	}

	public void setIds(ArrayList<String> ids) {
		this.ids = ids;
	}
	

}

