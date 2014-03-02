/**
 * 
 */
package com.umeng.im.listener;

import java.util.List;

import com.umeng.im.entity.Friend;

/**
 * 
 */
public interface OnFriendStatusChangeListener {

	/**
	 * 
	 * </br> 添加好友</br>
	 * 
	 * @param friends
	 *            好友列表
	 */
	public void onFriendsAdd(List<Friend> friends);

	/**
	 * 
	 * </br> 删除好友</br>
	 * 
	 * @param friends
	 *            需要删除的好友列表
	 */
	public void onFriendsDelete(List<Friend> friends);

	/**
	 * 
	 * </br> 更新好友</br>
	 * 
	 * @param friends
	 *            跟新后的好友
	 */
	public void onFriendsUpdate(List<Friend> friends);

	/**
	 * 
	 * </br> 好友的状态发生改变</br>
	 * 
	 * @param friend
	 *            friend
	 */
	public void onFriendChange(Friend friend);
}
