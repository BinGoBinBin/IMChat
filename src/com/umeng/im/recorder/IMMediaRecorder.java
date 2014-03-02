/**
 * 
 */
package com.umeng.im.recorder;

import java.io.File;
import java.io.IOException;

import com.umeng.im.utils.IMFileUtils;

import android.media.MediaRecorder;

/**
 * 
 */
public class IMMediaRecorder {

	private MediaRecorder mMediaRecorder;
	private String savePath = null;

	/**
	 * 
	 *</br>为录音做相关的准备工作</br>
	 */
	public void prepare() {
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		//设置音频文件的保存地址
		savePath = IMFileUtils.getVoiceFilePath() + File.separator
				+ IMFileUtils.getFileName() + ".amr";
		mMediaRecorder.setOutputFile(savePath);
		
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * </br>开始录音</br>
	 */
	public void start() {
		mMediaRecorder.start();
	}

	/**
	 * 
	 * </br>停止录音</br>
	 */
	public void stop() {
		mMediaRecorder.stop();
	}

	/**
	 * 
	 * </br>关闭并释放资源</br>
	 */
	public void shutown() {
		if (mMediaRecorder != null) {
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}

	/**
	 * 
	 *</br>获取音频文件的保存路径</br>
	 * @return
	 */
	public String getPath() {
		return savePath;
	}
}
