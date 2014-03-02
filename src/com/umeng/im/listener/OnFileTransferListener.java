/**
 * 
 */
package com.umeng.im.listener;

/**
 * 
 */
public interface OnFileTransferListener extends BaseListener{

	public boolean isReceive(String user, String fileName, long size);
	
	public void updateProgress(double progress);
	
	public void onComplete(int result);

}
