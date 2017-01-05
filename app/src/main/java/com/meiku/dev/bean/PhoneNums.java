package com.meiku.dev.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2015/9/29.
 */
public class PhoneNums {
    public String initial;
    public String user_id;// 可添加用户的ID
    public String user_name;// 可添加用户的名字
    public String key;//
    public String phone_number;// 手机号
    public String result;
    public Bitmap icon;//头像
    public String nickName;
    public String isFriend;
    public String  headPicUrl;//头像
    public String clientThumbHeadPicUrl;

    public String getClientThumbHeadPicUrl() {
		return clientThumbHeadPicUrl;
	}

	public void setClientThumbHeadPicUrl(String clientThumbHeadPicUrl) {
		this.clientThumbHeadPicUrl = clientThumbHeadPicUrl;
	}

	public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "PhoneNums [key=" + key + ",phone_number=" + phone_number
                + ",initial=" + initial + "]";
    }
}
