package com.umeng.im.message;

import android.text.TextUtils;

import com.umeng.im.common.DebugLog;
import com.umeng.im.entity.IMMessage;
import com.umeng.im.message.IMLooper.MessageQueue;
import com.umeng.im.service.IMService;
import com.umeng.im.service.impl.IMServiceImp;

public class IMMessageHandler {

	private static final String TAG = IMMessageHandler.class.getName();
	private MessageQueue mMessageQueue = null;
	private IMService mIMService = IMServiceImp.getInstance();
	public IMMessageHandler() {
		IMLooper looper = IMLooper.getMainLooper();
		if(looper == null){
			new RuntimeException("no main looper");
		}
		mMessageQueue = IMLooper.mMessageQueue;
	}
	
	/**
	 * 
	 *</br>具体的消息处理</br>
	 * @param message
	 * 			需要处理的消息
	 */
	public void handlerMessage(Message message) {
		Object obj = message.obj;
		if (obj == null) {
			return;
		}

		IMMessage msg = (IMMessage) obj;
		String user = msg.user;
		if (TextUtils.isEmpty(user)) {
			DebugLog.w(TAG, "user is null...");
			return;
		}
		mIMService.sendMessage(msg);
	}
	
	/**
	 * 
	 *</br>添加一条消息到消息队列的末尾</br>
	 * @param message
	 * 			需要处理的消息
	 */
	public void sendMessage(Message message){
		message.target = this;
		mMessageQueue.add(message);
	}
	
}
