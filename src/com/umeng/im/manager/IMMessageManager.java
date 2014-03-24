/**
 * 
 */
package com.umeng.im.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.text.TextUtils;

import com.umeng.im.common.DebugLog;
import com.umeng.im.entity.IMMessage;
import com.umeng.im.utils.IMFileUtils;
import com.umeng.im.utils.IMStringUtils;

/**
 * 
 */
public class IMMessageManager {

	private static final String TAG = IMMessageManager.class.getName();
	private static BlockingQueue<IMMessage> mMessageQueue = new ArrayBlockingQueue<IMMessage>(100, true);
	private static ExecutorService mExecutorService = Executors.newFixedThreadPool(3);
	private static volatile boolean done = false;
	private static IMMessageManager instance = new IMMessageManager();
	
	static{
		loop();
	}

	/**
	 * 
	 *</br>get IMMessage instance</br>
	 * @return
	 */
	public static IMMessageManager getInstance(){
		return instance;
	}
	/**
	 * 
	 *</br>save message</br>
	 * @param message
	 */
	public void saveIMMessage(IMMessage message) {
		if (message == null || TextUtils.isEmpty(message.user)) {
			return;
		}
		synchronized (mMessageQueue) {
			mMessageQueue.add(message);		
			System.out.println("###### add a message");
			mMessageQueue.notify();
		}
	}

	/**
	 * 
	 *</br>close save message thread</br>
	 */
	public void shutdown(){
		mExecutorService.shutdown();
		done = true;
	}
	
	/**
	 * 
	 *</br>启动保存消息的循环</br>
	 */
	private static void loop() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (!done) {
					final IMMessage message = getNext();
					mExecutorService.execute(new Runnable() {
						
						@Override
						public void run() {
							String path = IMFileUtils.getMessageFilePath();
							String user = message.user;
							String fileName = IMStringUtils.md5(user);
							File file = new File(path, fileName);
							if (!file.exists()) {
								try {
									file.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							saveIMMessage(file, message);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * 
	 *</br>get next message to be saved to SD card</br>
	 * @return
	 * 			IMMessage object
	 */
	private static IMMessage getNext(){
		synchronized (mMessageQueue) {
			if(mMessageQueue.size() == 0){
				try {
					mMessageQueue.wait();
					System.out.println("message queue hasn't message ,wait...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("wake up....");
			return mMessageQueue.poll();
		}
	}
	/**
	 * 
	 * </br>将消息保存在SD卡上面</br>
	 * 
	 * @param file
	 *            保存的文件
	 * @param message
	 *            需要保存的消息
	 */
	private static void saveIMMessage(final File file, final IMMessage message) {
		System.out.println("save message...");
		String json = message.convertJsonData();
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file,true);
			fileOutputStream.write(json.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(fileOutputStream);
		}
	}
	
	/**
	 * 
	 *</br>obtain message that is storaged to sdCard According to user name.Notes : this operation should be executed on non UI Thread</br>
	 * @param user
	 * 					user name
	 * @return a list of IMMessage.
	 */
	public List<IMMessage> getIMMessage(String user){
		if(TextUtils.isEmpty(user)){
			DebugLog.i(TAG, "user name is null...");
			return null;
		}
		String path = IMFileUtils.getMessageFilePath();
		String fileName = IMStringUtils.md5(user);
		File file = new File(path,fileName);
		if(!file.exists()){
			DebugLog.i(TAG, "message record is null...");
			return null;
		}
		BufferedReader reader = null;
		try {
		 reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String result = "";
			String line  = "";
			while ((line = reader.readLine()) != null ) {
				result  += line;
			}
			return parseMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
			DebugLog.w(TAG, "read message record error...");
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	/**
	 * 
	 *</br>translate string to IMMessage object</br>
	 * @param result
	 * @return
	 */
	private List<IMMessage> parseMessage(String result){
		List<String> lists = new ArrayList<String>();
		char divider = '}';
		int index = result.indexOf(divider);
		String tmpString = "";
		while (index > 0) {
			tmpString = result.substring(0, index+1);
			lists.add(tmpString);
			result = result.substring(index+1);
			index = result.indexOf(divider);
		}
		List<IMMessage> messages = new ArrayList<IMMessage>();
		for(String json : lists){
			IMMessage message = IMMessage.parseMessage(json);
			messages.add(message);
		}
		return messages;
	}
	
	/**
	 * 
	 *</br>delete message record according to user name</br>
	 * @param user user name
	 */
	public void deleteIMMessage(String user){
		if(TextUtils.isEmpty(user)){
			return;
		}
		String path = IMFileUtils.getMessageFilePath();
		String fileName = IMStringUtils.md5(user);
		File file = new File(path,fileName);
		if(file.exists()){
			file.delete();
		}
	}
	/**
	 * 
	 * </br>close OutputStream</br>
	 * 
	 * @param outputStream
	 *            will require to be closed stream
	 */
	private static void closeOutputStream(OutputStream outputStream) {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * </br>close input stream.</br>
	 * 
	 * @param inputStream
	 */
//	private static void closeInputStream(InputStream inputStream) {
//		if (inputStream != null) {
//			try {
//				inputStream.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
