/**
 * 
 */
package com.umeng.im.entity;

import android.text.TextUtils;

/**
 * 
 */
public enum MessageType {
	TEXT{
		@Override
		public String toString() {
			return "text";
		}
	} ,
	FILE {
		@Override
		public String toString() {
			return "file";
		}
	};
	
	/**
	 * 
	 *</br>according to type convert to enum type. </br>
	 * @param type type
	 * @return return {@link #MessageType} or null if type is null or ""
	 */
	public static MessageType convertMessageType(String type){
		if(TextUtils.isEmpty(type)){
			return null;
		}
		
		if(type.equals(TEXT.toString())){
			return TEXT;
		} else if (type.equals(FILE.toString())){
			return FILE;
		}
		return null;
	}
}
