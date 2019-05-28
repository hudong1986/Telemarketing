package telemarketing.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.sun.istack.internal.FinalArrayList;

public class SecretHelper {

	/**
	 * @param str
	 * @return
	 * @Description: 32位小写MD5
	 */
	public static String parseStrToMd5L32(String str) {
		String reStr = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(str.getBytes());
			StringBuffer stringBuffer = new StringBuffer();
			for (byte b : bytes) {
				int bt = b & 0xff;
				if (bt < 16) {
					stringBuffer.append(0);
				}
				stringBuffer.append(Integer.toHexString(bt));
			}
			reStr = stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return reStr;
	}

	/**
	 * @param str
	 * @return
	 * @Description: 32位大写MD5
	 */
	public static String parseStrToMd5U32(String str) {
		String reStr = parseStrToMd5L32(str);
		if (reStr != null) {
			reStr = reStr.toUpperCase();
		}
		return reStr;
	}

	/**
	 * @param str
	 * @return
	 * @Description: 16位小写MD5
	 */
	public static String parseStrToMd5U16(String str) {
		String reStr = parseStrToMd5L32(str);
		if (reStr != null) {
			reStr = reStr.toUpperCase().substring(8, 24);
		}
		return reStr;
	}

	/**
	 * @param str
	 * @return
	 * @Description: 16位大写MD5
	 */
	public static String parseStrToMd5L16(String str) {
		String reStr = parseStrToMd5L32(str);
		if (reStr != null) {
			reStr = reStr.substring(8, 24);
		}
		return reStr;
	}

	
	/** 
     * @param bytes 
     * @return 
     */  
    public static byte[] decodeBase64(final byte[] bytes) {  
        return Base64.decodeBase64(bytes);  
    }  
    
    public static String  decodeBase64(final String bytes) {  
        return new String(Base64.decodeBase64(bytes));
    } 
  
    /** 
     * 二进制数据编码为BASE64字符串 
     * 
     * @param bytes 
     * @return 
     * @throws Exception 
     */  
    public static String encodeBase64(final byte[] bytes) {  
        return new String(Base64.encodeBase64(bytes));  
    }  
    
    public static String encodeBase64(final String bytes) {  
        return new String(Base64.encodeBase64(bytes.getBytes()));  
    } 
}
