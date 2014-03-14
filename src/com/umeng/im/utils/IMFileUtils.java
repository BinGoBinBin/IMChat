/**
 * 
 */
package com.umeng.im.utils;

import java.io.File;
import java.util.UUID;

import android.os.Environment;
import android.text.TextUtils;

/**
 * 
 */
public class IMFileUtils {

	private static String FOLDER = "im_cache";
	public static String PATH = "";
	private static final String FOLDER_FILE = "img_file";
	private static final String FOLDER_VOICE = "voice";
	private static final String FOLDER_MSG = "msg_file";
	private static String imageFilePath = "";
	private static String voiceFilePath = "";
	private static String msgFilePath = "";

	static {
		init();
	}

	private static void init() {
		// 判断sd卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			PATH = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + FOLDER + File.separator;

		} else {
			PATH = Environment.getDataDirectory().getPath() + File.separator
					+ FOLDER + File.separator;
		}
		imageFilePath = PATH + FOLDER_FILE + File.separator;
		voiceFilePath = PATH + FOLDER_VOICE + File.separator;
		msgFilePath = PATH + FOLDER_MSG + File.separator;

		File file = new File(imageFilePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(voiceFilePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(msgFilePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = null;
	}

	/**
	 * 
	 * </br>判断文件是否读有效</br>
	 * 
	 * @param path
	 *            file path
	 * @return return true if file exists and has read permission,otherwise
	 *         return false
	 */
	public static boolean isValid(String path) {
		boolean valid = false;
		if (TextUtils.isEmpty(path)) {
			return valid;
		}

		File file = new File(path);
		if (!file.exists() || !file.canRead()) {
			return valid;
		}

		return true;
	}

	/**
	 * 
	 * </br> 根据UUID获取文件名</br>
	 * 
	 * @return 文件名
	 */
	public synchronized static String getFileName() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String result = str.replaceAll("-", "");
		return result;
	}

	/**
	 * 
	 * </br>获取音频文件保持目录</br>
	 * 
	 * @return
	 */
	public static String getVoiceFilePath() {
		return voiceFilePath;
	}

	/**
	 * 
	 * </br>获取普通文件保持目录</br>
	 * 
	 * @return
	 */
	public static String getFilePath() {
		return imageFilePath;
	}

	public static String getMessageFilePath(){
		return msgFilePath;
	}
}
