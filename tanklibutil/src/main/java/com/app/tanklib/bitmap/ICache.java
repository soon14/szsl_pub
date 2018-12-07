package com.app.tanklib.bitmap;


import android.graphics.Bitmap;

public interface ICache {
	public void put(String key, Bitmap data);
	
	public Bitmap getBitmap(String key);
	
	public void clearCache();
	
	public boolean containsKey(String key);
}
