package com.app.tanklib.util;

import android.graphics.Bitmap;

public class BitMapUtil {

	public static void recycle(Bitmap bitMap) {
		if (null != bitMap && !bitMap.isRecycled()) {
			bitMap.recycle();
			bitMap = null;
		}
	}

}
