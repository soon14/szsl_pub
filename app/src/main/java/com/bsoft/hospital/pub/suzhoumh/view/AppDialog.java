package com.bsoft.hospital.pub.suzhoumh.view;

import android.app.AlertDialog;
import android.content.Context;

public class AppDialog {

	public static void dialog(Context context, String title, String message,
			String btnText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setMessage(message);
		btnText = (btnText == null) ? (btnText = "确认") : btnText;
		builder.setPositiveButton(btnText, null);
		builder.create().show();
	}

	public static void dialogInfo(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("提示");
		builder.setMessage(message);
		builder.setPositiveButton("确认", null);
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
}
