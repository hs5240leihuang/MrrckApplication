package com.meiku.dev.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 版权所有：2016-美库网
 * 项目名称：mrrck-web   
 *
 * 类描述：个性标签配置表entity
 * 类名称：com.mrrck.ap.us.entity.PersonalTagEntity     
 * 创建人：陈卫
 * 创建时间：2016-5-13 下午07:53:24   
 * @version V1.0
 */
public class PersonalTagEntity extends MkPersonalTag {
    
	/** 小分类集合 */
    private List<PersonalTagEntity> personalList = new ArrayList<PersonalTagEntity>() ;

	public List<PersonalTagEntity> getPersonalList() {
		return personalList;
	}

	public void setPersonalList(List<PersonalTagEntity> personalList) {
		this.personalList = personalList;
	}

    
    
}
