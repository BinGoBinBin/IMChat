package com.umeng.im.service;

import java.util.List;

import com.umeng.im.entity.Friend;
import com.umeng.im.entity.Group;
import com.umeng.im.entity.IMMessage;
import com.umeng.im.listener.OnAddFriendListener;
import com.umeng.im.listener.OnAddGroupListener;
import com.umeng.im.listener.OnLoginListener;

public interface IMService {

	/**
	 * </br>用户登录</br>
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param listener
	 *            登录回调函数
	 */
	public void login(String userName, String password, OnLoginListener listener);

	/**
	 * 
	 * </br> 添加一个新用户</br>
	 * 
	 * @param friend
	 *            添加的新用户
	 * @param listener
	 *            添加用户回调函数
	 */
	public void addFriend(Friend friend, OnAddFriendListener listener);

	/**
	 * 
	 * </br>添加一个用户组 </br>
	 * 
	 * @param groupName
	 *            组名称
	 * @param listener
	 *            回调函数
	 */
	public void addGroup(String groupName, OnAddGroupListener listener);

	/**
	 * 
	 * </br> 返回用户的好友</br>
	 * 
	 * @return 如果用户登录，将返回用户的所有好友；否则返回null
	 */
	public List<Friend> getFriends();

	/**
	 * 
	 * </br> 返回用户的好友</br>
	 * 
	 * @param groupName
	 *            组名
	 * @return 如果用户登录，获取groupName的所有好友；否则返回null
	 */
	public List<Friend> getFriends(String groupName);

	/**
	 * 
	 * </br>获取登录用户的所有分组 </br>
	 * 
	 * @return 返回登录用户的所有用户组；如果用户未登录，返回null
	 */
	public List<Group> getGroups();

	/**
	 * 
	 * </br> 根据group name获取用户组</br>
	 * 
	 * @param groupName
	 *            组名称
	 * @return group name对应的用户组；如果用户未登录或者group name不存在，返回null
	 */
	public Group getGroup(String groupName);

	/**
	 * 
	 * </br>发送一条消息</br>
	 * 
	 * @param user
	 *            接受消息的用户
	 * @param message
	 *            消息
	 */
	public void sendMessage(IMMessage message);
}
