/**
 * 
 */
package com.umeng.im.manager;

import com.umeng.im.recorder.IMMediaRecorder;

/**
 * 管理录音的Manager
 */
public class IMMediaRecorderManager {
	
	private IMMediaRecorder mIMMediaRecorder;
	
	/**
	 * 
	 *</br>获取一个MediaRecorder管理对象</br>
	 * @return MediaRecorderManager对象
	 */
	public static IMMediaRecorderManager getInstance(){
		return new IMMediaRecorderManager();
	}
	
	/**
	 * 
	 *</br><录音的相关准备，设置编码，输出格式等信息/br>
	 */
	public void prepare(){
		mIMMediaRecorder = new IMMediaRecorder();
		mIMMediaRecorder.prepare();
	}
	
	/**
	 * 
	 *</br>开始录音</br>
	 */
	public void start(){
		if(mIMMediaRecorder != null){
			mIMMediaRecorder.start();
		}
	}
	
	/**
	 * 
	 *</br>停止录音</br>
	 */
	public void stop(){
		mIMMediaRecorder.stop();
	}
	
	/**
	 * 
	 *</br>释放相关的资源</br>
	 */
	public void shutDown(){
		mIMMediaRecorder.shutown();		
	}
	
	/**
	 * 
	 *</br>获取音频文件保存的地址</br>
	 * @return
	 */
	public String getPath(){
		return mIMMediaRecorder.getPath();
	}
}
