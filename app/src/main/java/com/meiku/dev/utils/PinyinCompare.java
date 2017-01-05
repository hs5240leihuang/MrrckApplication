package com.meiku.dev.utils;

import java.util.Comparator;

import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.bean.FriendEntity;

/**
 * a到z排序
 *
 */
public class PinyinCompare implements Comparator<AreaEntity> {  
  
    public int compare(AreaEntity o1, AreaEntity o2) {  
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
      
    	return (PinyinUtil.getTerm(o1.getCityName())).compareTo(PinyinUtil.getTerm(o2.getCityName()));  
   
    }  
}  