package com.app.tanklib.util;

import android.app.Activity;
import android.widget.Toast;

public class ExitUtil {

	private static long exitTime = 0;

	public static void ExitApp(Activity activity) {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			activity.finish();
		}

	}

}
