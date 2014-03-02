package com.umeng.im.common;

import com.umeng.im.utils.IMFileUtils;

/**
 * IM配置相关
 */
public class IMConfig {

	// 全局唯一config
	private static IMConfig instance = new IMConfig();
	// openfir地址
	private String mHost;
	private int mPort;
	private boolean mSASLAuthenticationEnabled = true;
	private boolean isDebug;
	private String filePath = IMFileUtils.PATH;

	private IMConfig(){}
	
	/**
	 * </br>获取IMConfig对象 </br>
	 * 
	 * @return IMConfig实例对象
	 */
	public static IMConfig getInstance() {
		return instance;
	}

	/**
	 * 
	 * </br>设置服务器地址</br>
	 * 
	 * @param host
	 *            服务器地址
	 */
	public void setHost(String host) {
		this.mHost = host;
	}

	/**
	 * 
	 * </br> 返回服务器地址</br>
	 * 
	 * @return 服务器地址
	 */
	public String getHost() {
		return this.mHost;
	}

	/**
	 * 
	 * </br>设置服务器端口</br>
	 * 
	 * @param port
	 *            端口
	 */
	public void setPort(int port) {
		this.mPort = port;
	}

	/**
	 * 
	 * </br> 返回端口号</br>
	 * 
	 * @return 端口号
	 */
	public int getPort() {
		return this.mPort;
	}

	/**
	 * </br>获取是否采用安全认证</br>
	 * 
	 * @return the mSASLAuthenticationEnabled
	 */
	public boolean isSASLAuthenticationEnabled() {
		return mSASLAuthenticationEnabled;
	}

	/**
	 * </br>设置是否采用安全认证</br>
	 * 
	 * @param mSASLAuthenticationEnabled
	 *            是否启用安全认证.默认启用安全模式连接。
	 */
	public void setSASLAuthenticationEnabled(boolean mSASLAuthenticationEnabled) {
		this.mSASLAuthenticationEnabled = mSASLAuthenticationEnabled;
	}

	/**
	 * </br>返回是否开启debug模式</br>
	 * 
	 * @return the isDebug
	 */
	public boolean isDebug() {
		return isDebug;
	}

	/**
	 * </br>设置是否开启debug模式</br>
	 * 
	 * @param isDebug
	 *            设置是否开启debug模式。默认不开启debug模式
	 */
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	/**
	 *</br> </br>
	 * @return the fileFolder
	 */
	public String getFileSavePath() {
		return filePath;
	}

	/**
	 *</br> </br>
	 * @param fileFolder the fileFolder to set
	 */
	public void setFileSavePath(String path) {
		this.filePath = path;
	}

	
}
