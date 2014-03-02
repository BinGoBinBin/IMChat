/**
 * 
 */
package com.umeng.im.player;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * 
 */
public class IMMediaPlayer {

	private MediaPlayer mMediaPlayer = null;
	
	public void prepare(String path){
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(path);
			OnCompletionListener listener = getOnCompletionListener();
			mMediaPlayer.setOnCompletionListener(listener);
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(){
		mMediaPlayer.start();
	}
	
	public void stop(){
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer =null;
	}
	
	private OnCompletionListener getOnCompletionListener(){
		OnCompletionListener listener = new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				stop();
			}
		};
		return listener;
	}
}
