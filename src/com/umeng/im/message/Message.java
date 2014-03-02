package com.umeng.im.message;

public class Message {

	public Object obj;
	protected IMMessageHandler target;
	
	/**
	 * 
	 *</br>获取一条空消息</br>
	 * @return
	 */
	public Message obtain(){
		return new Message();
	} 
	
	/**
	 * 
	 *</br>根据obj获取一条消息</br>
	 * @param obj
	 * @return
	 */
	public Message obtain(Object obj){
		Message message = new Message();
		message.obj = obj;
		return message;
	}
}
