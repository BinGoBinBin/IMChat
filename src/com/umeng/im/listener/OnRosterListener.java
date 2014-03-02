/**
 * 
 */
package com.umeng.im.listener;

import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import com.umeng.im.entity.Friend;
import com.umeng.im.utils.IMServiceUtils;

/**
 * 处理当Roster的状态发生改变的时候相应的状态
 */
public class OnRosterListener implements RosterListener {

	private Roster mRoster;
	private OnFriendStatusChangeListener mListener;

	public OnRosterListener(Roster roster, OnFriendStatusChangeListener listener) {
		this.mRoster = roster;
		this.mListener = listener;
	}

	/**
	 * 添加好友
	 */
	@Override
	public void entriesAdded(Collection<String> entries) {
		List<Friend> friends = IMServiceUtils.getFriends(entries, mRoster);
		if (mListener != null) {
			mListener.onFriendsAdd(friends);
		}
	}

	/**
	 * 删除好友
	 */
	public void entriesDeleted(Collection<String> entries) {
		List<Friend> friends = IMServiceUtils.getFriends(entries, mRoster);
		if (mListener != null) {
			mListener.onFriendsDelete(friends);
		}
	}

	/**
	 * 更新好友
	 */
	@Override
	public void entriesUpdated(Collection<String> entries) {
		List<Friend> friends = IMServiceUtils.getFriends(entries, mRoster);
		if (mListener != null) {
			mListener.onFriendsUpdate(friends);
		}
	}

	/**
	 * 好友状态更新
	 */
	@Override
	public void presenceChanged(Presence presence) {
		String entry = presence.getFrom();
		Friend friend = IMServiceUtils.getFriend(entry, mRoster);
		if (mListener != null) {
			mListener.onFriendChange(friend);
		}
	}
}
