package com.umeng.im.service.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import com.umeng.im.common.Constants;
import com.umeng.im.common.DebugLog;
import com.umeng.im.common.IMAsyncTask;
import com.umeng.im.common.IMConfig;
import com.umeng.im.entity.BaseContextEntity;
import com.umeng.im.entity.Friend;
import com.umeng.im.entity.Group;
import com.umeng.im.entity.IMMessage;
import com.umeng.im.entity.MessageType;
import com.umeng.im.listener.OnAddFriendListener;
import com.umeng.im.listener.OnAddGroupListener;
import com.umeng.im.listener.OnFileTransferListener;
import com.umeng.im.listener.OnLoginListener;
import com.umeng.im.listener.OnReceiveMessageListener;
import com.umeng.im.service.IMService;
import com.umeng.im.utils.IMServiceUtils;

public class IMServiceImp implements IMService {

	private static final String TAG = IMServiceImp.class.getName();
	private static IMService instance = new IMServiceImp();
	private IMConfig mIMConfig = null;
	private XMPPConnection mConnection;
	private BaseContextEntity mBaseContextEntity;
	private static Map<String, Chat> mChats = new HashMap<String, Chat>();

	private IMServiceImp() {
		mIMConfig = IMConfig.getInstance();
		mBaseContextEntity = BaseContextEntity.getInstance();
	}

	/**
	 * 
	 * </br>获取全局唯一变量</br>
	 * 
	 * @returnIMServiceImp对象
	 */
	public static IMService getInstance() {
		return instance;
	}

	/**
	 * </br>用户登录</br>
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param listener
	 *            登录回调函数
	 */
	@Override
	public void login(String userName, String password,
			final OnLoginListener listener) {

		final String name = userName;
		final String pwd = password;

		new IMAsyncTask<Integer>() {

			protected void onPreExecute() {
				if (listener != null) {
					listener.onStart();
				}
			};

			@Override
			protected Integer doInBackground() {
				boolean isConnection = isConnection();
				if (!isConnection) {
					getConnection();
				}
				if (mConnection == null) {
					return Constants.IM_FAILED;
				}
				int code = Constants.IM_SUCCESS;
				try {
					mConnection.login(name, pwd);
				} catch (XMPPException e) {
					DebugLog.i(TAG, "login failed...");
					e.printStackTrace();
					code = Constants.IM_FAILED;
				}
				return code;
			}

			protected void onPostExecute(Integer result) {
				if (listener != null) {
					listener.onComplete(result);
				}

			};
		}.execute();
	}

	/*
	 * 
	 * </br> 添加一个新用户</br>
	 * 
	 * @param user 添加的新用户
	 */
	@Override
	public void addFriend(Friend friend, OnAddFriendListener listener) {
		final Roster roster = getRoster();
		if (roster == null) {
			if (listener != null) {
				listener.onStart();
				listener.onComplete(Constants.IM_FAILED);
			}
			return;
		}
		final OnAddFriendListener addUserListener = listener;
		final String userName = friend.getName();
		final String nikeName = friend.getNikeName();
		final String[] groups = friend.getGroups();
		new IMAsyncTask<Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (addUserListener != null) {
					addUserListener.onStart();
				}
			}

			@Override
			protected Integer doInBackground() {
				int code = Constants.IM_SUCCESS;
				try {
					roster.createEntry(userName, nikeName, groups);
				} catch (XMPPException e) {
					DebugLog.e(TAG, "add user fail...");
					e.printStackTrace();
					code = Constants.IM_FAILED;
				}
				return code;
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				if (addUserListener != null) {
					addUserListener.onComplete(result);
				}
			}
		};
	}

	/**
	 * 
	 * </br>添加一个用户组 </br>
	 * 
	 * @param groupName
	 *            组名称
	 * @param listener
	 *            回调函数
	 */
	@Override
	public void addGroup(String groupName, OnAddGroupListener listener) {
		final String name = groupName;
		final OnAddGroupListener addGroupListener = listener;
		new IMAsyncTask<RosterGroup>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (addGroupListener != null) {
					addGroupListener.onStart();
				}
			}

			@Override
			protected RosterGroup doInBackground() {
				RosterGroup rosterGroup = null;
				Roster roster = getRoster();
				if (roster == null) {
					return null;
				}
				try {
					rosterGroup = roster.createGroup(name);
				} catch (Exception e) {
					DebugLog.e(TAG, "add group fail...");
					e.printStackTrace();
				}
				return rosterGroup;
			}

			@Override
			protected void onPostExecute(RosterGroup result) {
				super.onPostExecute(result);
				int code = Constants.IM_SUCCESS;
				Group group = null;
				if (result == null) {
					code = Constants.IM_FAILED;
				} else {
					Roster roster = getRoster();
					if (roster != null) {
						group = IMServiceUtils.convertToGroup(result, roster);
					}
				}

				if (addGroupListener != null) {
					addGroupListener.onComplete(code, group);
				}
			}
		};
	}

	/**
	 * 
	 * </br> 返回用户的好友 </br>
	 * 
	 * @return 如果用户登录，将返回用户的所有好友；否则返回null
	 */
	@Override
	public List<Friend> getFriends() {
		Roster roster = getRoster();
		if (roster == null) {
			return null;
		}
		Collection<RosterEntry> entries = roster.getEntries();
		List<Friend> friends = IMServiceUtils.convertToFriends(entries, roster);
		return friends;
	}

	/**
	 * 
	 * </br> 根据指定的组名获取好友列表</br>
	 * 
	 * @param groupName
	 *            组名
	 * @return 如果已登录，返回该组名下得所有好友;如果用户没有登录或者group name不存在
	 */
	@Override
	public List<Friend> getFriends(String groupName) {
		Roster roster = getRoster();
		if (roster == null) {
			return null;
		}
		RosterGroup rosterGroup = roster.getGroup(groupName);
		if (rosterGroup == null) {
			return null;
		}
		Collection<RosterEntry> rosterEntries = rosterGroup.getEntries();
		List<Friend> friends = IMServiceUtils.convertToFriends(rosterEntries,
				roster);
		return friends;
	}

	/**
	 * 
	 * </br> 获取用户所有的组</br>
	 * 
	 * @return 返回用户所有的组
	 */
	@Override
	public List<Group> getGroups() {
		Roster roster = getRoster();
		if (roster == null) {
			return null;
		}
		Collection<RosterGroup> rosterGroups = roster.getGroups();
		List<Group> groups = IMServiceUtils.convertToGroups(rosterGroups,
				roster);
		return groups;
	}

	/**
	 * 
	 * </br> 根据group name获取用户组</br>
	 * 
	 * @param groupName
	 *            组名称
	 * @return group name对应的用户组；如果用户未登录或者group name不存在，返回null
	 */
	@Override
	public Group getGroup(String groupName) {
		Roster roster = getRoster();
		if (roster == null) {
			return null;
		}
		RosterGroup rosterGroup = roster.getGroup(groupName);
		if (rosterGroup == null) {
			return null;
		}
		Group group = IMServiceUtils.convertToGroup(rosterGroup, roster);
		return group;
	}

	/**
	 * 
	 * </br>发送消息</br>
	 * 
	 * @param message
	 *            消息
	 */
	@Override
	public void sendMessage(IMMessage message) {
		MessageType type = message.type;
		if (type == MessageType.TEXT) {
			sendTextMessage(message);
		} else if (type == MessageType.FILE) {
			sendFileMessage(message);
		}
	}

	/**
	 * 
	 * </br>发送文件</br>
	 * 
	 * @param message
	 *            文件
	 */
	private void sendFileMessage(IMMessage message) {
		boolean isLogin = isLogin();
		if (!isLogin) {
			DebugLog.e(TAG, "user don't login...");
			return;
		}
		String user = message.user;
		String path = message.path;
		File file = new File(path);
		FileTransferManager fileTransferManager = new FileTransferManager(
				mConnection);
		// 注册一个文件接收监听器
		
		
		String serverUser = IMServiceUtils.convertUser(user, mConnection);
		OutgoingFileTransfer fileTransfer = fileTransferManager
				.createOutgoingFileTransfer(serverUser);
		fileTransfer.setFileTranslateProgressListener(IMServiceUtils.getFileTranslateProgressListener());
		try {
			fileTransfer.sendFile(file, file.getName());
		} catch (XMPPException e) {
			e.printStackTrace();
			DebugLog.e(TAG, "send file fail...");
		}
	}

	/**
	 * 
	 * </br>发送文本消息</br>
	 * 
	 * @param message
	 *            消息内容
	 */
	private void sendTextMessage(IMMessage message) {
		Chat chat = null;
		String user = message.user;
		if (!mChats.containsKey(user)) {
			Chat tmpChat = getChat(user);
			if (tmpChat == null) {
				DebugLog.e(TAG, "create chat fail...");
				return;
			}
			mChats.put(user, tmpChat);
			chat = tmpChat;
		} else {
			chat = mChats.get(user);
		}
		Message msg = new Message();
		msg.setFrom(user);
		String body = message.convertJsonData();
		msg.setBody(body);
		try {
			chat.sendMessage(msg);
		} catch (XMPPException e) {
			e.printStackTrace();
			DebugLog.e(TAG, "send message fail...");
		}
		// TODO消息发送状态需要回调
	}

	/**
	 * 
	 * </br>同用户user建立一个聊天chat</br>
	 * 
	 * @param user
	 *            用户
	 * @return
	 */
	private Chat getChat(String user) {
		ChatManager chatManager = getChatManager();
		if (chatManager == null) {
			return null;
		}
		String serverUser = IMServiceUtils.convertUser(user, mConnection);
		Chat chat = chatManager.createChat(serverUser,
				OnReceiveMessageListener.getReceiveMessageListener());
		chatManager.addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean flag) {
				chat.addMessageListener(OnReceiveMessageListener
						.getReceiveMessageListener());
			}
		});
		return chat;
	}

	/**
	 * 
	 * </br>获取Roster对象 </br>
	 * 
	 * @return Roster实例.如果用户没有登录，返回null.
	 */
	private Roster getRoster() {
		boolean isLogin = isLogin();
		if (!isLogin) {
			DebugLog.e(TAG, "please login...");
			return null;
		}
		Roster roster = mConnection.getRoster();
		return roster;
	}

	/**
	 * 
	 * </br>获取ChatManager对象</br>
	 * 
	 * @return ChatManager实例。如果没有登录，返回null
	 */
	private ChatManager getChatManager() {
		boolean isLogin = isLogin();
		if (!isLogin) {
			DebugLog.e(TAG, "please login ...");
			return null;
		}
		ChatManager chatManager = mConnection.getChatManager();
		return chatManager;
	}

	/**
	 * 
	 * </br>获取链接 </br>
	 * 
	 * @return XMPConnection链接对象
	 */
	private XMPPConnection getConnection() {
		String host = mIMConfig.getHost();
		int port = mIMConfig.getPort();
		boolean isSASLAuthenticationEnabled = mIMConfig
				.isSASLAuthenticationEnabled();
		boolean isDebug = mIMConfig.isDebug();
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				host, port);
		configuration.setSASLAuthenticationEnabled(isSASLAuthenticationEnabled);
		configuration.setDebuggerEnabled(isDebug);
//		configure(ProviderManager.getInstance());
		mConnection = new XMPPConnection(configuration);
		try {
			mConnection.connect();
			registerFileTransferListener();
		} catch (XMPPException e) {
			DebugLog.e(TAG, "connection fail...");
			e.printStackTrace();
			mConnection = null;
		}
		return mConnection;
	}

	public void registerFileTransferListener(){
		FileTransferManager fileTransferManager = new FileTransferManager(mConnection);
		String savePath = mIMConfig.getFileSavePath();
		OnFileTransferListener fileTransferListener = mBaseContextEntity
				.getFileTransferListener();
		FileTransferListener listener = IMServiceUtils.getFileTransferListener(fileTransferListener, savePath);
		fileTransferManager.addFileTransferListener(listener);
	}
	/**
	 * 
	 * </br>是否建立了连接 </br>
	 * 
	 * @return
	 */
	private boolean isConnection() {
		if (mConnection == null || !mConnection.isConnected()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * </br> 判断是否已经登录</br>
	 * 
	 * @return 是否已经登录
	 */
	private boolean isLogin() {
		if (mConnection == null || !mConnection.isConnected()
		/** || !mConnection.isAuthenticated() */
		) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * </br>设置相关的配置，防止在手机上无法接受文件</br>
	 * 
	 * @param pm
	 */
	private void configure(ProviderManager pm) {
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			DebugLog.w(TAG,
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		pm.addIQProvider("open", "http://jabber.org/protocol/ibb",
				new OpenIQProvider());
		pm.addIQProvider("close", "http://jabber.org/protocol/ibb",
				new CloseIQProvider());
		pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
				new DataPacketProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			DebugLog.w(TAG,
					"Can't load class for org.jivesoftware.smackx.packet.Version");
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
	}

}
