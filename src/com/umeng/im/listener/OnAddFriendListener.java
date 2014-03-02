/**
 * 
 */
package com.umeng.im.listener;

/**
 * 添加用户监听器
 */
public interface OnAddFriendListener extends BaseListener {

	/**
	 * 
	 * </br>添加用户完成 </br>
	 * 
	 * @param code
	 *            返回码
	 */
	public void onComplete(int code);
}
