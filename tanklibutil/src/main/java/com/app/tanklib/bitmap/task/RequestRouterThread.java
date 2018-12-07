package com.app.tanklib.bitmap.task;


import android.graphics.Bitmap;

import com.app.tanklib.bitmap.CacheManage;

/**
 * 图片请求路由
 * 
 * @author zkl
 * 
 */
public class RequestRouterThread extends TaskQueueThread {
	protected static RequestRouterThread staticInstance;

	public static RequestRouterThread getInstance() {
		if (staticInstance == null) {
			staticInstance = new RequestRouterThread();
		}
		return staticInstance;
	}

	private RequestRouterThread() {
		super("RequestRouter");
		setPriority(Thread.NORM_PRIORITY - 1);
	}

	/**
	 * 缓存及下载入口
	 */
	@Override
	protected Bitmap processRequest(ImageRequest request) {
		if (CacheManage.getInstance().containsKey(request.imageUrl,
				request.imageType)) {
			return CacheManage.getInstance().getBitmap(request);
			// FileLoaderThread.getInstance().addTask(request);
		} else {
			DownloadThreadPool.getInstance().addTask(request);
		}
		return null;
	}

	@Override
	protected void onRequestComplete(RequestResponse response) {
		response.originalRequest.listener.onBitmapLoaded(response);
	}

	@Override
	protected void onRequestCancelled(ImageRequest request) {
		request.listener.onBitmapLoadCancelled();
	}
}
