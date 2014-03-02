package com.umeng.im.common;

import com.umeng.im.manager.IMManager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @功能描述 : 自定义异步任务类, 封装HandlerThread实现异步线程
 */
public abstract class IMAsyncTask<Result> {
	// HandlerThread内部封装了自己的Handler和Thead，有单独的Looper和消息队列
	private static final HandlerThread HT = new HandlerThread(
			IMManager.class.getName(),
			android.os.Process.THREAD_PRIORITY_BACKGROUND);
	static {
		HT.start();
	}

	/**
	 * @功能描述 : onPreExecute任务执行之前的初始化操作等
	 */
	protected void onPreExecute() {

	}

	/**
	 * @功能描述 : doInBackground后台执行任务
	 * @return
	 */
	protected abstract Result doInBackground();

	/**
	 * @功能描述 : doInBackground返回结果，在onPostExecute更新UI线程
	 * @param result
	 */
	protected void onPostExecute(Result result) {
	}

	/**
	 * @功能描述 : execute方法
	 * @return
	 */
	public final IMAsyncTask<Result> execute() {
		// 获取调用execute的线程的Looper, 构建Handler
		Looper looper = Looper.myLooper();
		if(looper == null){
			Looper.prepare();
			looper = Looper.myLooper();
		}
		final Handler parentHandler = new Handler(looper);
		// 获取mHt的Looper, 并且构造Handler, 注意的是Looper与ch的是不一样的.
		Handler htHandler = new Handler(HT.getLooper());

		onPreExecute();
		htHandler.post(new Runnable() {
			@Override
			public void run() {
				// 后台执行任务
				final Result result = doInBackground();
				// 向UI线程post数据，用以更新UI等操作
				parentHandler.post(new Runnable() {
					@Override
					public void run() {
						onPostExecute(result);
					}
				});
			}
		});

		return this;
	}

}
