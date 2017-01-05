package com.meiku.dev.utils;

import java.security.InvalidKeyException;
import java.security.Key;  
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESedeKeySpec;  
import javax.crypto.spec.IvParameterSpec;

import com.meiku.dev.bean.ReqBase;
import com.meiku.dev.config.ConstantKey;
/**
 * 3DES加解密
 * @author 库
 *
 */
public class Des3Util {
	 // 密钥  
    private final static String secretKey =  "mrrck@123456789012345678";  
    // 向量  
    private final static String iv = "01234567";  

    
    //加密模式
    private final static String algorithmMode = "desede/CBC/PKCS5Padding";
  
    /** 
     * 3DES加密 
     *  
     * @param plainText 普通文本 
     * @return 
     * @throws Exception  
     */  
    public static String encode(String plainText)   {  
        Key deskey = null;  
        DESedeKeySpec spec;
		try {
			spec = new DESedeKeySpec(secretKey.getBytes(ConstantKey.encoding));
		
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");  
        deskey = keyfactory.generateSecret(spec);  
  
        Cipher cipher = Cipher.getInstance(algorithmMode);  
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());  
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);  
        byte[] encryptData = cipher.doFinal(plainText.getBytes(ConstantKey.encoding));  
        return Base64.encode(encryptData); 
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
         
    }  
  
    /** 
     * 3DES解密 
     *  
     * @param encryptText 加密文本 
     * @return 
     * @throws Exception 
     */  
    public static String decode(String encryptText) throws Exception {  
        Key deskey = null;  
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes(ConstantKey.encoding));  
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");  
        deskey = keyfactory.generateSecret(spec);  
        Cipher cipher = Cipher.getInstance(algorithmMode);  
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());  
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);  
  
        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));  
  
        return new String(decryptData, ConstantKey.encoding);  
    } 
    
	/**
	 * REQBASE通过MAP转成加密的字符串
	 */
	public static String reqToSecret(ReqBase req) {
		Map<String,Object> reqmap = new HashMap<String,Object>();
		req.getHeader().setSecretFlag(ConstantKey.SECRET_FLAG);
		reqmap.put("header", JsonUtil.objToJson(req.getHeader()));
		if(req.getBody().isJsonNull()) {
			reqmap.put("body", "{}");
		} else {
			String plaintxt =  req.getBody().toString();
			reqmap.put("body",  encode(plaintxt));
		}

        return JsonUtil.hashMapToJson(reqmap);
	}
	
 

}
