/**
 * 
 */
package com.umeng.im.entity;

import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import com.umeng.im.common.UserStatus;

/**
 * 用户类
 */
public class Friend{
	//对应openfire中用户的JID字段
	private String user;
	private String name;
	private String nikeName;
	//用户跟订阅者之间的关系
	private ItemType itemType;
	//好友所属于哪一个组
	private String[] groups;
	private UserStatus userStatus = UserStatus.OFFLINE;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param userName the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the nikeName
	 */
	public String getNikeName() {
		return nikeName;
	}
	
	/**
	 * @param nikeName the nikeName to set
	 */
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	
	/**
	 *</br> </br>
	 * @return the groups
	 */
	public String[] getGroups() {
		return groups;
	}

	/**
	 *</br> </br>
	 * @param groups the groups to set
	 */
	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	/**
	 *</br> </br>
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 *</br> </br>
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 *</br> </br>
	 * @return the itemType
	 */
	public ItemType getItemType() {
		return itemType;
	}

	/**
	 *</br> </br>
	 * @param itemType the itemType to set
	 */
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	/**
	 *</br> </br>
	 * @return the userStatus
	 */
	public UserStatus getUserStatus() {
		return userStatus;
	}

	/**
	 *</br> </br>
	 * @param userStatus the userStatus to set
	 */
	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	
}
