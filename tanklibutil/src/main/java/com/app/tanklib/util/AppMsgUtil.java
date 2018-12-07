package com.app.tanklib.util;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.tanklib.R;
import com.app.tanklib.appmsg.AppMsg;

import static android.view.Gravity.BOTTOM;

public class AppMsgUtil {
	
	
	public static void showInfoAppMsg(Activity activity, String msg) {
		showInfoAppMsg(activity, msg, true);
	}

	public static void showStickyAppMsg(Activity activity, String msg) {
		showStickyAppMsg(activity, msg, true);
	}

	public static void showInfoAppMsg(Activity activity, String msg,
			boolean isBottom) {
		AppMsg.Style style = AppMsg.STYLE_ALERT;
		AppMsg appMsg = AppMsg.makeText(activity, msg, style);
		if (isBottom) {
			appMsg.setLayoutGravity(BOTTOM);
		}
		appMsg.show();
	}

	public static void showStickyAppMsg(Activity activity, String msg,
			boolean isBottom) {
		AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_STICKY,
				R.color.sticky);
		final AppMsg appMsg = AppMsg.makeText(activity, msg, style,
				R.layout.sticky);
		appMsg.getView().findViewById(R.id.remove_btn)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						appMsg.cancel();
					}
				});
		if (isBottom) {
			appMsg.setLayoutGravity(BOTTOM);
		}
		appMsg.show();
	}

	public static void cancelAll(Activity activity) {
		AppMsg.cancelAll(activity);
	}

}
