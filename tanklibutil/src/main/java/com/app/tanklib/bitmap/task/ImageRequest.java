package com.app.tanklib.bitmap.task;

import android.content.Context;
import android.graphics.BitmapFactory;

public final class ImageRequest {
	public Context context;
	public String imageUrl;
	public Listener listener;
	//生成图片的规格
	public BitmapFactory.Options loadOptions;
	//图片类别：大图,头像..
	public int imageType;
	// 是否是进度条显示
	public boolean isProgess = false;

	public interface Listener {
		public void onBitmapLoaded(final RequestResponse requestResponse);

		public void onBitmapLoadError(String message);

		public void onBitmapLoadCancelled();
	}

	public ImageRequest(final Context context, String imageUrl,
			Listener listener, BitmapFactory.Options options, int imageType) {
		this(context, imageUrl, listener, options, imageType, false);
	}

	public ImageRequest(final Context context, String imageUrl,
			Listener listener, BitmapFactory.Options options, int imageType,
			boolean isProgess) {
		this.context = context;
		this.imageUrl = imageUrl;
		this.listener = listener;
		this.loadOptions = options;
		this.imageType = imageType;
		this.isProgess = isProgess;
	}
}
