package com.app.tanklib.bitmap.task;


import android.graphics.Bitmap;

import com.app.tanklib.bitmap.CacheManage;



/**
 * 缓存及本地图片获取及图片替换
 * @author zkl
 *
 */
public class FileLoaderThread extends TaskQueueThread {
	static FileLoaderThread staticInstance;

	public static FileLoaderThread getInstance() {
		if (staticInstance == null) {
			staticInstance = new FileLoaderThread();
		}
		return staticInstance;
	}

	private FileLoaderThread() {
		super("FileLoader");
		setPriority(Thread.NORM_PRIORITY - 1);
	}

	@Override
	protected Bitmap processRequest(ImageRequest request) {
		return CacheManage.getInstance().getBitmap(request);
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
