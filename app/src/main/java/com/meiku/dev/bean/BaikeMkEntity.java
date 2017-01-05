package com.meiku.dev.bean;

import java.util.List;

/**
 * 美库百科新实体类
 * 
 * @author 库
 * 
 */
public class BaikeMkEntity extends MkMrrckBaike {
	/** 分页页数 */
	private Integer offset;

	/** 每页条数 */
	private Integer pageNum;

	/** 基本信息自定义项 */
	private List<MkMrrckBaikeBasic> listBkBasic;

	/** 内容自定义项 */
	private List<MkMrrckBaikeContent> listBkContent;

	/** 相册图片 */
	private List<MkMrrckBaikeAttachment> listBkAttachment;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public List<MkMrrckBaikeBasic> getListBkBasic() {
		return listBkBasic;
	}

	public void setListBkBasic(List<MkMrrckBaikeBasic> listBkBasic) {
		this.listBkBasic = listBkBasic;
	}

	public List<MkMrrckBaikeContent> getListBkContent() {
		return listBkContent;
	}

	public void setListBkContent(List<MkMrrckBaikeContent> listBkContent) {
		this.listBkContent = listBkContent;
	}

	public List<MkMrrckBaikeAttachment> getListBkAttachment() {
		return listBkAttachment;
	}

	public void setListBkAttachment(
			List<MkMrrckBaikeAttachment> listBkAttachment) {
		this.listBkAttachment = listBkAttachment;
	}

}
