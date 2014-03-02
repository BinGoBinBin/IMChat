/**
 * 
 */
package com.umeng.im.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.umeng.im.common.DebugLog;
import com.umeng.im.listener.Listener;

/**
 * 
 */
public class IMListenerManager {

	private static final String TAG = IMListenerManager.class.getName();
	private static IMListenerManager instance = new IMListenerManager();
	@SuppressWarnings("rawtypes")
	private static Map<Class, List<Listener>> listenerMaps = new HashMap<Class, List<Listener>>();

	private IMListenerManager() {
	}

	/**
	 * 
	 * </br>获取监听器管理者 </br>
	 * 
	 * @return 全局唯一的监听器管理者
	 */
	public static IMListenerManager getInstance() {
		return instance;
	}

	/**
	 * 
	 * </br>添加一个Listener </br>
	 * 
	 * @param listener
	 *            实现了Listener的实例
	 */
	public void addListener(Listener listener) {
		if (listener == null) {
			return;
		}
		if (listenerMaps.containsKey(listener.getClass())) {
			List<Listener> listeners = listenerMaps.get(listener.getClass());
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		} else {
			List<Listener> listeners = new ArrayList<Listener>();
			listeners.add(listener);
			listenerMaps.put(listener.getClass(), listeners);
		}
	}

	/**
	 * 
	 * </br>添加多个监听器 </br>
	 * 
	 * @param collections
	 *            实现了Listener实例的集合
	 */
	public void addListener(Collection<Listener> collections) {
		if (collections == null || collections.size() == 0) {
			return;
		}
		for (Listener listener : collections) {
			addListener(listener);
		}
	}

//	public <T> List<T> getListeners(Class<T> clz){
//		if(listenerMaps.containsKey(clz)){
//			List<T> listeners = listenerMaps.get(clz);
//			if(){
//				
//			}
//		}
//		return null;
//	}
	/**
	 * 
	 * </br> 移除一个监听器</br>
	 * 
	 * @param listener
	 *            需要移除的监听器
	 * @return 是否移除成功。如果移除成功，返回true；否则返回false
	 */
	public boolean removeListener(Listener listener) {
		if (listener == null) {
			return false;
		}
		if (listenerMaps.containsKey(listener.getClass())) {
			boolean flag = listenerMaps.get(listener.getClass()).remove(
					listener);
			return flag;
		}
		return false;
	}

	/**
	 * 
	 * </br> 移除一组监听器</br>
	 * 
	 * @param listeners
	 *            需要移除的监听器
	 * @return 集合中有一个或者多个移除成功，返回true；否则返回false
	 */
	public boolean removeListener(Collection<Listener> listeners) {
		if (listeners == null || listeners.size() == 0) {
			return false;
		}
		boolean result = false;
		for (Listener listener : listeners) {
			boolean flag = removeListener(listener);
			result |= flag;
			if (flag) {
				DebugLog.w(TAG, "移除" + listener.getClass().getName() + "监听器失败");
			}
		}
		return result;
	}
	
	/**
	 * 
	 *</br> 移除clz类型的所有监听器</br>
	 * @param clz
	 * 			集成自Listener的Class类
	 * @return 如果移除成功，返回true；否则返回false
	 */
	public boolean removeListener(Class<? extends Listener> clz){
		if(clz == null){
			return false;
		}
		boolean result = false;
		if(listenerMaps.containsKey(clz)){
			List<Listener> listeners = listenerMaps.remove(clz);
			if(listeners != null){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 
	 *</br>清除注册的所有监听器 </br>
	 */
	public void cleanListener(){
		listenerMaps.clear();
	}
}
