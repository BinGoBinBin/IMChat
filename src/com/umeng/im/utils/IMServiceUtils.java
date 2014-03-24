/**
 * 
 */
package com.umeng.im.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.FileTranslateProgressListener;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import android.text.TextUtils;

import com.umeng.im.common.DebugLog;
import com.umeng.im.common.UserStatus;
import com.umeng.im.entity.BaseContextEntity;
import com.umeng.im.entity.Friend;
import com.umeng.im.entity.Group;
import com.umeng.im.listener.OnFileTransferListener;

/**
 * IMService的工具类
 */
public class IMServiceUtils {

	private static final String TAG = IMServiceUtils.class.getName();

	/**
	 * 
	 * </br> 将RosterEntry转换为Friend类型</br>
	 * 
	 * @param entries
	 *            RosterEntry
	 * @param roster
	 *            roster
	 * @return
	 */
	public static List<Friend> convertToFriends(
			Collection<RosterEntry> entries, Roster roster) {
		if (entries == null) {
			return null;
		}
		List<Friend> friends = new ArrayList<Friend>();
		Iterator<RosterEntry> iterator = entries.iterator();
		Friend friend = null;
		while (iterator.hasNext()) {
			RosterEntry entry = iterator.next();
			friend = convertToFriend(entry, roster);
			friends.add(friend);
		}

		return friends;
	}

	/**
	 * 
	 * </br>根据好友名获取好友 </br>
	 * 
	 * @param entries
	 *            user the XMPP address of the user
	 * @param roster
	 *            roster
	 * @return 好友列表
	 */
	public static List<Friend> getFriends(Collection<String> entries,
			Roster roster) {
		RosterEntry rosterEntry = null;
		Friend friend = null;
		List<Friend> friends = new ArrayList<Friend>();
		for (String entry : entries) {
			rosterEntry = roster.getEntry(entry);
			friend = convertToFriend(rosterEntry, roster);
			friends.add(friend);
		}
		return friends;
	}

	/**
	 * 
	 * </br>根据好友的XMPP 地址获取好友信息 </br>
	 * 
	 * @param entry
	 *            好友的XMPP地址
	 * @param roster
	 *            roster
	 * @return 对应的好友，如果好友的XMPP地址不存在，返回null
	 */
	public static Friend getFriend(String entry, Roster roster) {
		RosterEntry rosterEntry = roster.getEntry(entry);
		Friend friend = convertToFriend(rosterEntry, roster);
		return friend;
	}

	/**
	 * 
	 * </br> 将RosterEntry转换为Friend类型</br>
	 * 
	 * @param entries
	 *            RosterEntry
	 * @param roster
	 *            roster
	 * @return
	 */
	public static Friend convertToFriend(RosterEntry entry, Roster roster) {
		Friend friend = new Friend();
		String user = entry.getUser();
		friend.setUser(user);

		// 好友是否在线
		Presence presence = roster.getPresence(user);
		if (presence != null) {
			friend.setUserStatus(UserStatus.ONLINE);
		}

		friend.setItemType(entry.getType());
		friend.setName(entry.getName());

		// 获取组名称
		Collection<RosterGroup> rosterGroups = entry.getGroups();
		Iterator<RosterGroup> groupIterator = rosterGroups.iterator();
		String groupName = null;
		Set<String> sets = new HashSet<String>();
		while (groupIterator.hasNext()) {
			RosterGroup rosterGroup = groupIterator.next();
			groupName = rosterGroup.getName();
			sets.add(groupName);
		}
		String[] groups = new String[sets.size()];
		sets.toArray(groups);
		friend.setGroups(groups);

		return friend;
	}

	/**
	 * 
	 * </br> 将RosterGroup转换为List<Friend>对象</br>
	 * 
	 * @param rosterGroup
	 *            IM用户组
	 * @param roster
	 *            roster
	 * @return 将RosterGroup转换后的Group对象
	 */
	public static List<Group> convertToGroups(
			Collection<RosterGroup> rosterGroups, Roster roster) {
		List<Group> groups = new ArrayList<Group>();
		Iterator<RosterGroup> iterator = rosterGroups.iterator();
		RosterGroup rosterGroup = null;
		Group group = null;
		while (iterator.hasNext()) {
			rosterGroup = iterator.next();
			group = convertToGroup(rosterGroup, roster);
			groups.add(group);
		}
		return groups;
	}

	/**
	 * 
	 * </br> 将RosterGroup转换为Friend对象</br>
	 * 
	 * @param rosterGroup
	 *            IM用户组
	 * @param roster
	 *            roster
	 * @return 将RosterGroup转换后的Group对象
	 */
	public static Group convertToGroup(RosterGroup rosterGroup, Roster roster) {
		Group group = new Group();
		group.setName(rosterGroup.getName());
		group.setCount(rosterGroup.getEntryCount());
		Collection<RosterEntry> rosterEntries = rosterGroup.getEntries();
		List<Friend> friends = IMServiceUtils.convertToFriends(rosterEntries,
				roster);
		group.setFriends(friends);
		return group;
	}

	/**
	 * 
	 * </b转换成服务器识别的user.转换的时候需要添加Resource，否则传输文件的时候将出现503（）</br>
	 * 
	 * @param user
	 *            用户名
	 * @param connection
	 *            建立的xmpp连接
	 * @return 转换后的user
	 */
	public static String convertUser(String user, XMPPConnection connection) {
		String tmpUser = user + "@" + connection.getServiceName();
		String resource = StringUtils.parseResource(tmpUser);
		if(TextUtils.isEmpty(resource)){
			tmpUser+="/Smack";
		}
		return tmpUser;
	}

	/**
	 * 
	 * </br>将服务器的user转换成普通的user</br>
	 * 
	 * @param user
	 *            服务器端user
	 * @return 普通的用户名。如果user为空或者非法user(不含有@)，将返回空字符串。
	 */
	public static String parseUser(String user) {
		String tmpUser = user;
		if (!TextUtils.isEmpty(tmpUser) && user.contains("@")) {
			int index = tmpUser.indexOf("@");
			tmpUser = tmpUser.substring(0, index);
			return tmpUser;
		}
		DebugLog.e(TAG, "illegal user :" + user);
		return "";
	}

	/**
	 * 
	 *</br>获取文件接收监听器</br>
	 * @param fileTransferListener
	 * @param path
	 * @return
	 */
	public static FileTransferListener getFileTransferListener(
			final OnFileTransferListener fileTransferListener, final String path) {
		FileTransferListener listener = new FileTransferListener() {

			@Override
			public void fileTransferRequest(FileTransferRequest request) {
				String fileName = request.getFileName();
				long fileSize = request.getFileSize();
				String user = request.getRequestor();
				if (fileTransferListener != null
						&& fileTransferListener.isReceive(user, fileName,
								fileSize)) {
					DebugLog.i(TAG, "receive file.file.name = " + fileName
							+ "file.size = " + fileSize);
					IncomingFileTransfer incomingFileTransfer = request
							.accept();
					try {
						incomingFileTransfer.recieveFile(new File(path,
								fileName));
					} catch (XMPPException e) {
						DebugLog.e(TAG, "receive file fial...");
						e.printStackTrace();
					}
				} else {
					DebugLog.w(TAG, "reject receive file...");
					request.reject();
				}
			}
		};
		return listener;
	}
	
	public static FileTranslateProgressListener getFileTranslateProgressListener(){
		final OnFileTransferListener listener = BaseContextEntity.getInstance().getFileTransferListener();
		if(listener == null){
			return null;
		}
		FileTranslateProgressListener progressListener = new FileTranslateProgressListener() {
			
			@Override
			public void updateProgress(double progress) {
				listener.onUpdateProgress(progress);
			}
			
			@Override
			public void onStart(String fileName) {
				listener.onStart();
			}
			
			@Override
			public void onError(String message) {
				listener.onError(message);
			}
			
			@Override
			public void onComplete() {
				listener.onComplete();
			}
			
			@Override
			public boolean isReceive(String user, String fileName, long size) {
				return false;
			}
		};
		return progressListener;
	}
}
