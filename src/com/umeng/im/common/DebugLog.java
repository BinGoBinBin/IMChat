/**
 * 
 */
package com.umeng.im.common;

import android.util.Log;

/**
 * 调试Log
 */
public class DebugLog {
	public static boolean DEBUG = true;

	/**
	 * 
	 * </br> verbose log</br>
	 * 
	 * @param tag
	 *            Log的标签
	 * @param message
	 *            Log信息
	 */
	public static void v(String tag, String message) {
		if (DEBUG) {
			Log.v(tag, message);
		}
	}

	/**
	 * 
	 * </br> info log</br>
	 * 
	 * @param tag
	 *            Log的标签
	 * @param message
	 *            Log信息
	 */
	public static void i(String tag, String message) {
		if (DEBUG) {
			Log.i(tag, message);
		}
	}

	/**
	 * 
	 * </br> warn log</br>
	 * 
	 * @param tag
	 *            Log的标签
	 * @param message
	 *            Log信息
	 */
	public static void w(String tag, String message) {
		if (DEBUG) {
			Log.w(tag, message);
		}
	}

	/**
	 * 
	 * </br> error log</br>
	 * 
	 * @param tag
	 *            Log的标签
	 * @param message
	 *            Log信息
	 */
	public static void e(String tag, String message) {
		if (DEBUG) {
			Log.e(tag, message);
		}
	}

}
