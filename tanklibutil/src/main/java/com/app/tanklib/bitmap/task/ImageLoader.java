package com.app.tanklib.bitmap.task;

import android.content.Context;
import android.graphics.BitmapFactory;

public class ImageLoader {
	private static ImageLoader staticInstance;

	private RequestRouterThread requestRouterThread;
	private FileLoaderThread fileLoaderThread;
	private DownloadThreadPool downloadThreadPool;

	public static ImageLoader getInstance(Context context) {
		if (staticInstance == null) {
			staticInstance = new ImageLoader(context);
		}
		return staticInstance;
	}

	private ImageLoader(final Context context) {
		fileLoaderThread = FileLoaderThread.getInstance();
		fileLoaderThread.start();
		downloadThreadPool = DownloadThreadPool.getInstance();
		downloadThreadPool.start(context);
		requestRouterThread = RequestRouterThread.getInstance();
		requestRouterThread.start();
	}

	public static void load(final Context context, String imageUrl,
			ImageRequest.Listener listener, BitmapFactory.Options options,
			int imageType) {
		final ImageLoader instance = getInstance(context);
		instance.requestRouterThread.addTask(new ImageRequest(context,
				imageUrl, listener, options, imageType));
	}

	public static void cancelAllRequests() {
		final ImageLoader imageLoader = getInstance(null);
		imageLoader.requestRouterThread.cancelAllRequests();
		imageLoader.fileLoaderThread.cancelAllRequests();
		imageLoader.downloadThreadPool.cancelAllRequests();
	}

	public static void shutdown() {
		final ImageLoader imageLoader = getInstance(null);
		imageLoader.requestRouterThread.shutdown();
		RequestRouterThread.staticInstance = null;
		imageLoader.fileLoaderThread.shutdown();
		FileLoaderThread.staticInstance = null;
		imageLoader.downloadThreadPool.shutdown();
		DownloadThreadPool.staticInstance = null;
		staticInstance = null;
	}
}
