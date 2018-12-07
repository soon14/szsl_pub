package com.app.tanklib.view;

import android.app.ProgressDialog;
import android.content.Context;

public class AppProgressDialog {
	public ProgressDialog dialog;

	public AppProgressDialog(Context context) {
		this(context, null);
	}

	public AppProgressDialog(Context context, String message) {
		this.dialog = new ProgressDialog(context);
		this.dialog.setCancelable(false);
		message = (message == null) ? "请稍候..." : message;
		this.dialog.setMessage(message);
	}

	public void start() {
		this.dialog.show();
	}

	public void stop() {
		if (this.dialog != null) {
			this.dialog.dismiss();
			this.dialog.cancel();
		}
	}
}
