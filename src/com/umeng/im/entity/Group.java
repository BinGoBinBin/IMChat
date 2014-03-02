/**
 * 
 */
package com.umeng.im.entity;

import java.util.List;

/**
 * 用户组类，包含基本的用户组信息
 */
public class Group {

	private String name;
	private int count;
	private List<Friend> friends;

	/**
	 * </br>获取组的名称 </br>
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * </br> 设置组的名称</br>
	 * 
	 * @param name
	 *            组名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * </br> 获取组中用户数</br>
	 * 
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * </br> 设置族中用户数</br>
	 * 
	 * @param count
	 *            用户数
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 *</br> </br>
	 * @return the friends
	 */
	public List<Friend> getFriends() {
		return friends;
	}

	/**
	 *</br> </br>
	 * @param friends the friends to set
	 */
	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	
}
