/**
 * 
 */
package com.umeng.im.manager;

import com.umeng.im.common.DebugLog;
import com.umeng.im.player.IMMediaPlayer;
import com.umeng.im.utils.IMFileUtils;

/**
 * 
 */
public class IMMediaPlayerManager {

	private static final String TAG = IMMediaPlayerManager.class.getName();
	private IMMediaPlayer mIMMediaPlayer = null;
	
	private IMMediaPlayerManager(){}
	
	public static IMMediaPlayerManager getInstance(){
		return new IMMediaPlayerManager();
	}
	public void play(String path){
		if(!IMFileUtils.isValid(path)){
			DebugLog.e(TAG, "file path is invalid or file has no reading permission...");
			return ;
		}
		mIMMediaPlayer = new IMMediaPlayer();
		mIMMediaPlayer.prepare(path);
		mIMMediaPlayer.play();
	}
	
	public void stop(){
		mIMMediaPlayer.stop();
	}
}
