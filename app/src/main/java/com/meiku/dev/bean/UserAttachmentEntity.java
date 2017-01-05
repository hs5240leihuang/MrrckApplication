package com.meiku.dev.bean;

 

import java.io.Serializable;
import java.util.List;

 

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：用户附件实体
 * 类名称：com.mrrck.ap.us.entity.UserAttachmentEntity     
 * 创建人：刘茂林
 * 创建时间：2015-6-18 下午03:52:21   
 * @version V1.0
 */
public class UserAttachmentEntity extends MkUserAttachment implements Serializable{
	
	/**（终端用）附件地址URL*/
	private String clientFileUrl;
	
	/**分页每页数量*/
	private Integer pageNum;
	
	/**分页偏移量*/
	private Integer offset;

	/**是否已设置为简历标识位*/
	private String isResume;
	
    /** 附件编号(重命名) */
    private Integer attachmentId;
    
    private List<Integer> moduleIds;

	public String getClientFileUrl() {
			return clientFileUrl;
	}

	public void setClientFileUrl(String clientFileUrl) {
		this.clientFileUrl = clientFileUrl;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getIsResume() {
		return isResume;
	}

	public void setIsResume(String isResume) {
		this.isResume = isResume;
	}

    public Integer getAttachmentId() {
        return this.attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public List<Integer> getModuleIds() {
        return this.moduleIds;
    }

    public void setModuleIds(List<Integer> moduleIds) {
        this.moduleIds = moduleIds;
    }

}
