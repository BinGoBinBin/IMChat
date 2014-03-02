/**
 * 
 */
package com.umeng.im.listener;


import com.umeng.im.entity.IMMessage;

/**
 * 
 */
public interface OnMessageListener{

	/**
	 * 
	 *</br>用户消息监听器</br>
	 * @param user 
	 * 			发出消息的用户
	 * @param message
	 * 			消息内容
	 */
	public void processMessage(String user, IMMessage message);

}
