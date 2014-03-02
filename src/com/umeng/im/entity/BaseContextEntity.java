/**
 * 
 */
package com.umeng.im.entity;

import com.umeng.im.listener.OnFileTransferListener;
import com.umeng.im.listener.OnFriendStatusChangeListener;
import com.umeng.im.listener.OnMessageListener;
import com.umeng.im.listener.OnVerifyUserNamePwdListener;

/**
 * 
 */
public class BaseContextEntity {

	private static BaseContextEntity instanc = new BaseContextEntity(); 
	private OnVerifyUserNamePwdListener mVerifyUserNamePwdListener;
	private OnFriendStatusChangeListener mFriendStatusChangeListener;
	private OnMessageListener mMessageListener;
	private OnFileTransferListener mFileTransferListener;
	
	public static BaseContextEntity getInstance(){
		return instanc;
	}
	/**
	 *</br> 获取验证用户名，密码的监听器</br>
	 * @return the mVerifyUserNamePwdListener
	 */
	public OnVerifyUserNamePwdListener getVerifyUserNamePwdListener() {
		return mVerifyUserNamePwdListener;
	}

	/**
	 *</br> 设置验证用户名，密码的监听器</br>
	 * @param verifyUserNamePwdListener t
	 * 			验证用户名，密码的监听器。此监听器在UI线程中执行，请不要在监听器中进行耗时操作。
	 */
	public void setVerifyUserNamePwdListener(
			OnVerifyUserNamePwdListener verifyUserNamePwdListener) {
		this.mVerifyUserNamePwdListener = verifyUserNamePwdListener;
	}

	/**
	 *</br> </br>
	 * @return the FriendStatusChangeListener
	 */
	public OnFriendStatusChangeListener getmFriendStatusChangeListener() {
		return mFriendStatusChangeListener;
	}

	/**
	 *</br> </br>
	 * @param friendStatusChangeListener the mFriendStatusChangeListener to set
	 */
	public void setFriendStatusChangeListener(
			OnFriendStatusChangeListener friendStatusChangeListener) {
		this.mFriendStatusChangeListener = friendStatusChangeListener;
	}
	/**
	 *</br> </br>
	 * @return the mMessageListener
	 */
	public OnMessageListener getMessageListener() {
		return this.mMessageListener;
	}
	/**
	 *</br> </br>
	 * @param mMessageListener the mMessageListener to set
	 */
	public void setMessageListener(OnMessageListener messageListener) {
		this.mMessageListener = messageListener;
	}
	/**
	 *</br> </br>
	 * @return the mFileTransferListener
	 */
	public OnFileTransferListener getFileTransferListener() {
		return mFileTransferListener;
	}
	/**
	 *</br> </br>
	 * @param mFileTransferListener the mFileTransferListener to set
	 */
	public void setFileTransferListener(
			OnFileTransferListener mFileTransferListener) {
		this.mFileTransferListener = mFileTransferListener;
	}
	
	
}
