package com.meiku.dev.bean;

import java.io.Serializable;


/**
 *
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web
 *
 * 类描述：
 * 类名称：com.mrrck.db.entity.MkMrrckBaikeContent
 * 创建人：仲崇生
 * 创建时间：Fri Jun 03 20:05:47 CST 2016
 * @version V1.0
 */ 
public class MkMrrckBaikeContent implements Serializable{

 	/** 编号 */
	private Integer id;

 	/** 用户编号(百科内容发布者编号) */
	private Integer userId;

 	/** 百科编号 */
	private Integer baiKeId;

 	/** 标题 */
	private String title;

 	/** 内容 */
	private String content;

 	/** 排序号 默认:999 */
	private Integer sortNo;

 	/** 创建时间 */
	private String createDate;

 	/** 更新时间 */
	private String updateDate;

 	/** 删除标识 0:正常,1:删除 */
	private String delStatus;

	public void setId(Integer id){
		this.id=id;
	}

	public Integer getId(){
		return id;
	}

	public void setUserId(Integer userId){
		this.userId=userId;
	}

	public Integer getUserId(){
		return userId;
	}

	public void setBaiKeId(Integer baiKeId){
		this.baiKeId=baiKeId;
	}

	public Integer getBaiKeId(){
		return baiKeId;
	}

	public void setTitle(String title){
		this.title=title;
	}

	public String getTitle(){
		return title;
	}

	public void setContent(String content){
		this.content=content;
	}

	public String getContent(){
		return content;
	}

	public void setSortNo(Integer sortNo){
		this.sortNo=sortNo;
	}

	public Integer getSortNo(){
		return sortNo;
	}

	public void setCreateDate(String createDate){
		this.createDate=createDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setUpdateDate(String updateDate){
		this.updateDate=updateDate;
	}

	public String getUpdateDate(){
		return updateDate;
	}

	public void setDelStatus(String delStatus){
		this.delStatus=delStatus;
	}

	public String getDelStatus(){
		return delStatus;
	}

}

