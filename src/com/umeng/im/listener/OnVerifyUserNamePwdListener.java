/**
 * 
 */
package com.umeng.im.listener;

/**
 * 验证用户名，密码是否合法，开发者可以根据自己的需求来验证。比如是否含有字母，下滑划线等.
 */
public interface OnVerifyUserNamePwdListener {

	/**
	 * 
	 *</br> 验证用户名，密码是否合法</br>
	 * @param userName
	 * 			用户名
	 * @param pwd
	 * 			密码
	 * @return 验证结果.如果验证结果返回false，程序将中断，不会进行login操作。
	 */
	public boolean verify(String userName, String pwd);
}
