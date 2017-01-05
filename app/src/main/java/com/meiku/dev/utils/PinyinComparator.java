package com.meiku.dev.utils;

import java.util.Comparator;

import com.meiku.dev.bean.FriendEntity;

/**
 * a到z排序
 *
 */
public class PinyinComparator implements Comparator<FriendEntity> {  
  
    public int compare(FriendEntity o1, FriendEntity o2) {  
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序  
        if (o2.getNameFirstChar().equals("#")) {  
            return -1;  
        } else if (o1.getNameFirstChar().equals("#")) {  
            return 1;  
        } else {  
            return o1.getNameFirstChar().compareTo(o2.getNameFirstChar());  
        }  
    }  
}  