/**
 * 
 */
package com.umeng.im.listener;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.umeng.im.entity.BaseContextEntity;
import com.umeng.im.entity.IMMessage;
import com.umeng.im.utils.IMServiceUtils;

/**
 * 接受消息的监听器
 */
public class OnReceiveMessageListener implements MessageListener {

	private static OnReceiveMessageListener receiveMessageListener = new OnReceiveMessageListener();
	private BaseContextEntity baseContextEntity;
	private OnReceiveMessageListener() {
		baseContextEntity = BaseContextEntity.getInstance();
	}

	/**
	 * 
	 *</br>获取消息监听器。全局唯一的消息监听器</br>
	 * @return 全局唯一的消息监听器
	 */
	public static OnReceiveMessageListener getReceiveMessageListener() {
		return receiveMessageListener;
	}

	@Override
	public void processMessage(Chat chat, Message message) {
		String serverUser = message.getFrom();
		String user = IMServiceUtils.parseUser(serverUser);
		String body = message.getBody();
		IMMessage msg = IMMessage.parseMessage(body);
		OnMessageListener messageListener = baseContextEntity.getMessageListener();
		if(messageListener != null){
			messageListener.processMessage(user, msg);
		}
	}

}
