/**
 * 
 */
package com.umeng.im.entity;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.umeng.im.common.DebugLog;

import android.text.TextUtils;

/**
 * 发送的消息实体
 */
public class IMMessage{
	
	private static final String TAG = IMMessage.class.getName();
	public String content;
	public String fileName;
	public String path;
	public long date = new Date().getTime();;
	public MessageType type = MessageType.TEXT;
	public String user;
	
	/**
	 * 
	 *</br>将当前对象转换成json串</br>
	 * @return
	 */
	public String convertJsonData(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("content", content);
			jsonObject.put("date", date);
			jsonObject.put("fileName", fileName);
			jsonObject.put("path", path);
			jsonObject.put("type", type.toString());
			jsonObject.put("user", user);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String body = jsonObject.toString();
		DebugLog.i(TAG, "message body:" + body);
		return body;
	}
	
	/**
	 * 
	 *</br>解析返回的消息</br>
	 * @param json
	 * 			json数据
	 * @return 根据Json数据解析
	 */
	public static IMMessage parseMessage(String json){
		if(TextUtils.isEmpty(json)){
			return null;
		}
		JSONObject jsonObject = null;
		IMMessage message = null;
		try {
			jsonObject = new JSONObject(json);
			message = new IMMessage();
			String content = (String) jsonObject.opt("content");
			long date = jsonObject.optLong("date");
			String fileName = jsonObject.optString("fileName");
			String path = jsonObject.optString("path");
			MessageType type = MessageType.convertMessageType(jsonObject.optString("type"));
			String user = jsonObject.optString("user"); 
			message.content = content;
			message.date = date;
			message.fileName = fileName;
			message.path = path;
			message.type = type;
			message.user = user;
		} catch (Exception e) {
			e.printStackTrace();
			DebugLog.e(TAG, "parse message fail,message body:" + json);
			return null;
		}
		return message;
	}
	
	/**
	 * 
	 *</br>获取一条空消息</br>
	 * @return
	 */
	public static IMMessage getIMMessage(){
		return new IMMessage();
	}

	@Override
	public String toString() {
		return "IMMessage [content=" + content + ", fileName=" + fileName
				+ ", path=" + path + ", date=" + date + ", type=" + type
				+ ", user=" + user + "]";
	}
	
}
