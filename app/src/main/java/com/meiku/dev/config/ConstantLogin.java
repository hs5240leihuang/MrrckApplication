package com.meiku.dev.config;
/**
 * 第三方登陆的常量定义
 * @author 库
 *
 */
public class ConstantLogin {
    public static final String DESCRIPTOR = "com.umeng.share";
    
    //QQ开发者APPID 和KEY "1104776283////"HeTke7nvnfjDtuRb"
 
    public static final String QQ_APPId = "1104776283";
    public static final String QQ_APPKEY ="HeTke7nvnfjDtuRb" ;
    
    /**微信开发者APPID和SECRET*/
    public static final String WECHAT_APPId = "wx3ef182c439eeea1c";
    public static final String WECHAT_APPSECRET ="9be57d46788039a8153472bf8a2c55f7" ;
    
    /**微信登录 */
    public static final String  LOGIN_WECHAT="1"; 
    /**qq登录 */
    public static final String  LOGIN_QQ="2"; 
    
    /**第三方账号是否已经注册*/
    public static final String LOGIN_OTHER_SUCCESS="0";
    
    /**根据手机号和微信或QQ验证返回    2  未识别类型 
     *  0 根据手机号码更新微信或qq成功 
     *  3 根据手机号码更新微信或qq失败  
     *  1 未注册*/
    public static final String LOGIN_VERIFY_CODE_SUCCESS="0";
    public static final String LOGIN_VERIFY_CODE_NOPHONE="1";
    

}
