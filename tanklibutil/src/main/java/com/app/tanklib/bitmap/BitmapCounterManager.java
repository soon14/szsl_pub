package com.app.tanklib.bitmap;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class BitmapCounterManager implements ICache {
	// 图片bitmap计数器
	HashMap<String, BitmapCounter> bitCounterMap;

	public BitmapCounterManager() {
		this.bitCounterMap = new HashMap<String, BitmapCounter>();
	}

	@Override
	public void put(String key, Bitmap data) {
		System.out.println("BitmapCounterManager put "+key);
		if (containsKey(key)) {
			bitCounterMap.get(key).countAdd();
		} else {
			BitmapCounter counter = new BitmapCounter(data);
			bitCounterMap.put(key, counter);
		}
	}

	@Override
	public Bitmap getBitmap(String key) {
		if (containsKey(key)) {
			return bitCounterMap.get(key).bitmap;
		}
		return null;
	}

	@Override
	public void clearCache() {
		if (null != bitCounterMap && bitCounterMap.size() > 0) {
			Iterator iter = bitCounterMap.entrySet().iterator();
			BitmapCounter bi = null;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				bi = (BitmapCounter) entry.getValue();
				if (null != bi) {
					if (null != bi.bitmap && !bi.bitmap.isRecycled()) {
						System.out.println("bitmapcounter clearCache  "
								+ bi.count);
						bi.bitmap.recycle();
						bi.bitmap = null;
						bi = null;
					}
				}
			}
			bitCounterMap.clear();
		}

	}

	public void release(String key) {
		if (bitCounterMap.containsKey(key)) {
			bitCounterMap.get(key).countSub();
			if (bitCounterMap.get(key).canRecycle()) {
				System.out.println("bitmapcounter release  " + key);
				bitCounterMap.get(key).recycle();
				bitCounterMap.remove(key);
			}
		}
	}

	@Override
	public boolean containsKey(String key) {
		return bitCounterMap.containsKey(key)
				&& (null != bitCounterMap.get(key).bitmap);
	}

}
