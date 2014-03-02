package com.umeng.im.manager;

import com.umeng.im.entity.BaseContextEntity;
import com.umeng.im.listener.OnFileTransferListener;
import com.umeng.im.listener.OnFriendStatusChangeListener;
import com.umeng.im.listener.OnLoginListener;
import com.umeng.im.listener.OnMessageListener;
import com.umeng.im.listener.OnVerifyUserNamePwdListener;
import com.umeng.im.message.IMLooper;

/**
 * IMManager主要管理IM的基本操作(登录，发送消息，添加用户等)进行管理.
 */
public class IMManager {

	private BaseContextEntity mBaseContextEntity;
	private IMServiceManager mIMServiceManager;
	private IMMediaRecorderManager mRecorderManager = null;
	private IMMediaPlayerManager mMediaPlayerManager = null;
	private static IMManager instance = new IMManager();

	private IMManager() {
		mBaseContextEntity = BaseContextEntity.getInstance();
		mIMServiceManager = IMServiceManager.getInstance();
		startMessageLooper();
	}

	/**
	 * 
	 * </br>获取全局唯一的IMManager实例</br>
	 * 
	 * @return 全局唯一的IMManager实例
	 */
	public static IMManager getInstance() {
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

	/**
	 * 
	 * </br>启动录音功能</br>
	 */
	public void startRecorder() {
		mRecorderManager = IMMediaRecorderManager.getInstance();
		mRecorderManager.prepare();
		mRecorderManager.start();
	}

	/**
	 * 
	 * </br>停止录音</br>
	 */
	public void stopRecorder() {
		if (mRecorderManager != null) {
			mRecorderManager.stop();
			mRecorderManager.shutDown();
		}
	}

	/**
	 * 
	 * </br>播放录音</br>
	 */
	public void play(String path) {
		mMediaPlayerManager = IMMediaPlayerManager.getInstance();
		mMediaPlayerManager.play(path);
	}

	/**
	 * 
	 * </br>启动一个消息循环来发送消息</br>
	 */
	private void startMessageLooper() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				IMLooper.prepare();
				IMLooper.loop();
			}
		}).start();
	}
}
