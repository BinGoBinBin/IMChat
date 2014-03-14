/**
 * 
 */
package com.umeng.im.utils;

import java.security.MessageDigest;

/**
 * 
 */
public class IMStringUtils {

	/**
	 *  MD5 加密
	 * @param str
	 * @return 32位16进制 密文 或 ""
	 */
	public static String md5(String str) {
		if (str == null) {
		    return null;
		}
		try {
			byte[] defaultBytes = str.getBytes();
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte[] messageDigest = algorithm.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(String.format("%02X", messageDigest[i]));
			}

			return hexString.toString();

		} catch (Exception e) {
			return str.replaceAll("[^[a-z][A-Z][0-9][.][_]]", "");
		}
	}
}
