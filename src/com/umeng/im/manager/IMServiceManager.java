/**
 * 
 */
package com.umeng.im.manager;

import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.umeng.im.common.DebugLog;
import com.umeng.im.entity.BaseContextEntity;
import com.umeng.im.entity.Friend;
import com.umeng.im.entity.Group;
import com.umeng.im.entity.IMMessage;
import com.umeng.im.entity.MessageType;
import com.umeng.im.listener.OnAddFriendListener;
import com.umeng.im.listener.OnAddGroupListener;
import com.umeng.im.listener.OnLoginListener;
import com.umeng.im.listener.OnVerifyUserNamePwdListener;
import com.umeng.im.service.IMService;
import com.umeng.im.service.impl.IMServiceImp;

/**
 * 
 */
public class IMServiceManager {

	private static final String TAG = IMManager.class.getName();
	private IMService mIMService;
	private BaseContextEntity mBaseContextEntity;
	private static IMServiceManager instance = new IMServiceManager();
	
	private IMServiceManager() {
		mIMService = IMServiceImp.getInstance();
		mBaseContextEntity = BaseContextEntity.getInstance();
	}

	public static IMServiceManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * </br> 登录</br>
	 * 
	 * @param friendName
	 *            用户名
	 * @param pwd
	 *            密码
	 * @param listener
	 *            登录的回调函数
	 */
	public void login(String userName, String pwd, OnLoginListener listener) {

		// 验证用户名是否合法
		if (TextUtils.isEmpty(userName)) {
			Log.e(TAG, "userName is null...");
			return;
		}

		// 验证密码是否合法
		if (TextUtils.isEmpty(pwd)) {
			Log.e(TAG, "password is null...");
		} 

		// 调用开发者对用户名，密码的验证监听器
		OnVerifyUserNamePwdListener verifyfriendNamePwdListener = mBaseContextEntity
				.getVerifyUserNamePwdListener();
		boolean illegal = true;
		if (verifyfriendNamePwdListener != null) {
			illegal = verifyfriendNamePwdListener.verify(userName, pwd);
		}
		if (!illegal) {
			return;
		}
		// 进行登录操作
		mIMService.login(userName, pwd, listener);
	}

	/**
	 * 
	 * </br> 添加一个用户</br>
	 * 
	 * @param friend
	 *            需要添加的用户
	 * @param listener
	 *            添加用户的回调函数
	 */
	public void addfriend(Friend friend, OnAddFriendListener listener) {

		if (friend == null || TextUtils.isEmpty(friend.getName())) {
			Log.e(TAG, "friend or friendName is null...");
			return;
		}
		mIMService.addFriend(friend, listener);
	}

	/**
	 * 
	 * </br> 添加用户组</br>
	 * 
	 * @param groupName
	 *            组名称
	 * @param listener
	 *            回调函数
	 */
	public void addGroup(String groupName, OnAddGroupListener listener) {

		if (TextUtils.isEmpty(groupName)) {
			Log.e(TAG, "group name is null...");
			return;
		}

		mIMService.addGroup(groupName, listener);
	}

	/**
	 * 
	 * </br> 返回登录用户的所有组</br>
	 * 
	 * @return 用户组集合
	 */
	public List<Group> getGroups() {
		List<Group> groups = mIMService.getGroups();
		return groups;
	}

	/**
	 * 
	 * </br>发送一条文本消息</br>
	 * 
	 * @param user
	 *            消息接收者
	 * @param content
	 *            消息内容
	 */
	public void sendMessage(String user, String content) {
		if (TextUtils.isEmpty(user)) {
			DebugLog.e(TAG, "user is null...");
			return;
		}

		if (TextUtils.isEmpty(content)) {
			DebugLog.e(TAG, "content is null...");
			return;
		}
		IMMessage message = new IMMessage();
		message.content = content;
		message.type = MessageType.TEXT;
		message.user = user;
		mIMService.sendMessage(message);
	}

	/**
	 * 
	 * </br>发送文件</br>
	 * 
	 * @param user
	 *            消息接收者
	 * @param fileName
	 *            文件名
	 * @param path
	 *            文件路径
	 */
	public void sendMessage(String user, String fileName, String path) {
		if(TextUtils.isEmpty(user)){
			DebugLog.e(TAG, "user is null...");
			return ;
		}
		
		if(TextUtils.isEmpty(fileName)){
			DebugLog.e(TAG, "file name is null...");
			return ;
		}
		
		if(TextUtils.isEmpty(path)){
			DebugLog.e(TAG, "file path is null...");
		}
		
		IMMessage message = IMMessage.getIMMessage();
		message.user = user;
		message.type = MessageType.FILE;
		message.fileName = fileName;
		message.path = path;
		mIMService.sendMessage(message);
	}
}
