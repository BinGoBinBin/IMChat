/**
 * 
 */
package com.umeng.im.listener;

import com.umeng.im.entity.Group;

/**
 * 
 */
public interface OnAddGroupListener extends BaseListener {

	/**
	 * 
	 * </br>添加组完成 </br>
	 * 
	 * @param code
	 *            返回码
	 * @param group
	 *            组的相关信息
	 */
	public void onComplete(int code, Group group);
}
