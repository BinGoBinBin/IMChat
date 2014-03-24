package com.umeng.im.manager;

import java.util.List;

import org.jivesoftware.smack.SmackAndroid;

import android.content.Context;

import com.umeng.im.common.DebugLog;
import com.umeng.im.entity.BaseContextEntity;
import com.umeng.im.entity.IMMessage;
import com.umeng.im.listener.OnFileTransferListener;
import com.umeng.im.listener.OnFriendStatusChangeListener;
import com.umeng.im.listener.OnLoginListener;
import com.umeng.im.listener.OnMessageListener;
import com.umeng.im.listener.OnVerifyUserNamePwdListener;
import com.umeng.im.utils.IMFileUtils;

/**
 * IMManager主要管理IM的基本操作(登录，发送消息，添加用户等)进行管理.
 */
public class IMManager {

	private static final String TAG = IMManager.class.getName();
	private BaseContextEntity mBaseContextEntity;
	private IMServiceManager mIMServiceManager;
	private static IMManager instance = null;
	private SmackAndroid mSmackAndroid = null;

	private IMManager(Context context) {
		mBaseContextEntity = BaseContextEntity.getInstance();
		mIMServiceManager = IMServiceManager.getInstance();
		mSmackAndroid = SmackAndroid.init(context);
		init();
	}

	/**
	 * 
	 * </br>获取全局唯一的IMManager实例.在获取前，请先调用
	 * {@link #IMManager.initIMManager(Context context)} 初始化相关的资源</br>
	 * 
	 * @return 全局唯一的IMManager实例
	 */
	public static IMManager getInstance() {
		if (instance == null) {
			DebugLog.e(
					TAG,
					"please init IMManager first,invoke method is \"IMManager.initIMManager(Context context)\"");
		}
		return instance;
	}

	/**
	 * 
	 * </br>初始化IMManager对象以及Smack的相关配置</br>
	 * 
	 * @param context
	 */
	public synchronized static void initIMManager(Context context) {
		if (instance != null) {
			return;
		}
		if (context == null) {
			DebugLog.w(TAG, " context is null,init failed...");
			return;
		}
		instance = new IMManager(context);
	}

	/**
	 * 
	 * </br>释放Smack Android的相关资源</br>
	 */
	public void onDestroyIMManager() {
		if (mSmackAndroid != null) {
			mSmackAndroid.onDestroy();
		}
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
		mIMServiceManager.login(userName, pwd, listener);
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
		mIMServiceManager.sendMessage(user, content);
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
		mIMServiceManager.sendMessage(user, fileName, path);
	}

	/**
	 * 
	 * </br>obtain message that is storaged to sdCard According to user
	 * name.Notes : this operation should be executed on non UI Thread</br>
	 * 
	 * @param user
	 *            user name
	 * @return a list of IMMessage.
	 */
	public List<IMMessage> getIMMessage(String user) {
		return IMMessageManager.getInstance().getIMMessage(user);
	}

	/**
	 * 
	 * </br>delete message record according to user name</br>
	 * 
	 * @param user
	 *            user name
	 */
	public void deleteIMMessage(String user) {
		IMMessageManager.getInstance().deleteIMMessage(user);
	}

	/**
	 * 
	 * </br>设置好友状态改变的监听器。好友状态改变包括添加好友， 删除好友，更新好友以及某个好友的状态（在线or离线）改变 </br>
	 * 
	 * @param listener
	 *            好友状态改变监听器
	 */
	public void registerOnFriendStatusChangeListener(
			OnFriendStatusChangeListener listener) {
		if (listener != null) {
			mBaseContextEntity.setFriendStatusChangeListener(listener);
		}
	}

	/**
	 * 
	 * </br>设置验证用户名密码的监听器，开发者可以根据自己的需求来做相应的验证策略。该方法在UI线程执行 </br>
	 * 
	 * @param listener
	 *            验证用户名，密码监听器
	 */
	public void registerVerifyUserNamePwdListener(
			OnVerifyUserNamePwdListener listener) {
		if (listener != null) {
			mBaseContextEntity.setVerifyUserNamePwdListener(listener);
		}
	}

	/**
	 * 
	 * </br>注册消息监听器</br>
	 * 
	 * @param listener
	 *            消息监听器
	 */
	public void registerMessageListener(OnMessageListener listener) {
		if (listener != null) {
			mBaseContextEntity.setMessageListener(listener);
		}
	}

	public void registerFileTransferListener(OnFileTransferListener listener) {
		if (listener != null) {
			mBaseContextEntity.setFileTransferListener(listener);
		}
	}

	private void init() {
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			Class.forName(IMMessageManager.class.getName(), true, classLoader);
			Class.forName(IMFileUtils.class.getName(), true, classLoader);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
