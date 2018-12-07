package com.app.tanklib.bitmap.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class DownloadThreadPool {

	private static final int CONNECTION_TYPE_MOBILE = 0;
	private static final int CONNECTION_TYPE_WIFI = 1;
	private static final int CONNECTION_TYPE_ETHERNET = 9;
	private static final int DEFAULT_MAX_THREADS = 6;

	static DownloadThreadPool staticInstance;
	private static int maxThreads = DEFAULT_MAX_THREADS;
	private DownloadThread[] downloadThreads;
	private int numActiveThreads = 0;
	private int currentThread = 0;

	// 做同步线程
	private ConcurrentHashMap<Object, Future> cache = new ConcurrentHashMap<Object, Future>();

	public Future put(Object key, Future value) {
		return cache.putIfAbsent(key, value);
	}

	public Object get(Object key) {
		return cache.get(key);
	}

	public void cleanCache() {
		cache.clear();
	}

	public static class ConnectivityChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			DownloadThreadPool.resizeThreadPool(context);
		}

		public static IntentFilter getIntentFilter() {
			return new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		}
	}

	public static DownloadThreadPool getInstance() {
		if (staticInstance == null) {
			staticInstance = new DownloadThreadPool();
		}
		return staticInstance;
	}

	public static void setMaxThreads(int value) {
		maxThreads = value;
	}

	private DownloadThreadPool() {
		downloadThreads = new DownloadThread[maxThreads];
		for (int i = 0; i < maxThreads; i++) {
			downloadThreads[i] = new DownloadThread();
		}
	}

	public void addTask(ImageRequest request) {
		DownloadThreadPool downloadThreadPool = getInstance();
		if (downloadThreadPool.numActiveThreads == 1) {
			downloadThreads[0].addTask(request);
		} else {
			downloadThreadPool.currentThread++;
			if (downloadThreadPool.currentThread >= downloadThreadPool.numActiveThreads) {
				downloadThreadPool.currentThread = 0;
			}
			downloadThreads[downloadThreadPool.currentThread].addTask(request);
		}
	}

	public void start(Context context) {
		for (int i = 0; i < maxThreads; i++) {
			downloadThreads[i].start();
		}
		numActiveThreads = getBestThreadPoolSize(context);
	}

	public static void resizeThreadPool(Context context) {
		final DownloadThreadPool downloadThreadPool = getInstance();
		if (downloadThreadPool.numActiveThreads == 0) {
			return;
		}

		downloadThreadPool.currentThread = 0;
		downloadThreadPool.numActiveThreads = downloadThreadPool
				.getBestThreadPoolSize(context);
	}

	/**
	 * 根据Wifi，2G，3G 网络情况决定下载线程数，
	 * 
	 * @param context
	 * @return
	 */
	private int getBestThreadPoolSize(final Context context) {
		if (null == context) {
			return 1;
		}
		// 2.1版本性能不好，建议使用单线程
		if (Build.VERSION.SDK_INT < 8) {
			return 1;
		}

		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null) {
				final int type = networkInfo.getType();
				switch (type) {
				case CONNECTION_TYPE_MOBILE:
					if (networkInfo.getSubtype() >= 3) {
						return maxThreads / 2;
					} else {
						return 1;
					}
				case CONNECTION_TYPE_WIFI:
					return maxThreads;
				case CONNECTION_TYPE_ETHERNET:
					return maxThreads;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public void cancelAllRequests() {
		for (int i = 0; i < maxThreads; i++) {
			downloadThreads[i].cancelAllRequests();
		}
	}

	public void shutdown() {
		for (int i = 0; i < maxThreads; i++) {
			downloadThreads[i].shutdown();
		}
	}
}
