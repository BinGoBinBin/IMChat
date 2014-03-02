package com.umeng.im.message;

import java.util.ArrayList;

public class IMLooper {

	private IMLooper() {
	}

	private static ThreadLocal<IMLooper> localThread = new ThreadLocal<IMLooper>();
	private static IMLooper mMainLooper = null;
	protected static MessageQueue mMessageQueue = MessageQueue.getInstance();

	/**
	 * 
	 * </br>创建一个全局唯一的Looper </br>
	 */
	public static void prepare() {
		if (localThread.get() != null) {
			throw new RuntimeException("only allow one ");
		}
		setMainLooper(new IMLooper());
	}

	/**
	 * 
	 * </br>设置主Looper </br>
	 * 
	 * @param origin
	 */
	public static void setMainLooper(IMLooper origin) {
		if (localThread.get() == null) {
			localThread.set(origin);
			mMainLooper = origin;
		}
	}

	/**
	 * 
	 * </br>获取消息处理looper</br>
	 * 
	 * @return 消息处理looper
	 */
	public static synchronized IMLooper getMainLooper() {
		return mMainLooper;
	}

	/**
	 * 
	 * </br>返回当前线程绑定的Looper对象</br>
	 * 
	 * @return
	 */
	public static IMLooper myLooper() {
		return localThread.get();
	}

	/**
	 * 
	 * </br>使当前线程进入消息循环</br>
	 */
	public static void loop() {
		while (true) {
			Message message = mMessageQueue.getMessage();
			if (message != null) {
				message.target.handlerMessage(message);
			}
			try {
				synchronized (mMessageQueue) {
					mMessageQueue.wait();	
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class MessageQueue {
		ArrayList<Message> mQueue = new ArrayList<Message>();

		public MessageQueue() {
		}

		public static MessageQueue getInstance() {
			return new MessageQueue();
		}

		/**
		 * 
		 * </br> add a message to message quene </br>
		 * 
		 * @param msg
		 */
		public void add(Message msg) {
			mQueue.add(msg);
			synchronized (mMessageQueue) {
				mMessageQueue.notify();
			}
		}

		/**
		 * 
		 * </br>get first messsage that is undealed </br>
		 * 
		 * @return Message
		 */
		public Message getMessage() {
			if (mQueue.size() == 0) {
				return null;
			}
			return mQueue.remove(0);
		}
	}
}
