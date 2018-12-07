package com.app.tanklib.bitmap;

import android.graphics.Bitmap;

public class BitmapCounter {

	public Bitmap bitmap;
	public int count;

	public BitmapCounter(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.count = 1;
	}

	// 加
	public void countAdd() {
		System.out.println("countAdd "+count);
		count++;
		System.out.println("countAdd "+count);
	}

	// 减
	public void countSub() {
		count--;
	}

	public boolean canRecycle() {
		return count <= 0;
	}

	public void recycle() {
		if (null != bitmap) {
			if (null != bitmap && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
				count = 0;
			}
		}
	}

}
