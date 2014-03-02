/**
 * 登录监听器
 */
package com.umeng.im.listener;

/**
 * @author liubin
 *
 */
public interface OnLoginListener extends BaseListener {

	/**
	 * 执行结束
	 * @param code 返回码
	 */
	
	public void onComplete(int code);
}
