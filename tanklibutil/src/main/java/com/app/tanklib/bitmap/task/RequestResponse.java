package com.app.tanklib.bitmap.task;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

public class RequestResponse {
	public WeakReference<Bitmap> bitmapReference;
	public ImageRequest originalRequest;

	public RequestResponse(Bitmap bitmap, ImageRequest originalRequest) {
		this.bitmapReference = new WeakReference<Bitmap>(bitmap);
		this.originalRequest = originalRequest;
	}
}
