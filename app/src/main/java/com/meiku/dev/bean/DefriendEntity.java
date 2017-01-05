package com.meiku.dev.bean;

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：拉黑表(找同行)
 * 类名称：com.mrrck.ap.gp.entity.DefriendEntity   
 * 创建人：仲崇生
 * 创建时间：2015-12-10 下午06:41:36   
 * @version V1.0
 */
public class DefriendEntity extends MkDefriend {
	
    /** 用户头像(120X120) */
    private String headPicUrl;
    
    /** 用户头像(120X120) (客户端使用)*/
    private String clientHeadPicUrl;
    
    /** 用户昵称 */
    private String nickName;

    public String getHeadPicUrl() {
        return this.headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getClientHeadPicUrl() {
        
            return this.clientHeadPicUrl;
    }

    public void setClientHeadPicUrl(String clientHeadPicUrl) {
        this.clientHeadPicUrl = clientHeadPicUrl;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
	
}

