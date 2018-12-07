package com.app.tanklib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.app.tanklib.R;

public class ProgressDialog {

	Context context;
	LayoutInflater mInflater;
	Dialog builder;
	View viewDialog;
	TextView msg;

	public ProgressDialog(Context context) {
		this.context = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ProgressDialog message(String text) {
		msg.setText(text);
		return this;
	}

	public ProgressDialog build(boolean cancelable, int widthpx) {
		builder = new Dialog(context, R.style.alertDialogTheme);
		builder.setCancelable(cancelable);
		viewDialog = mInflater.inflate(R.layout.dialog_progress, null);
		// 设置对话框的宽高
		LayoutParams layoutParams = new LayoutParams(widthpx,
				LayoutParams.WRAP_CONTENT);
		builder.setContentView(viewDialog, layoutParams);
		msg = (TextView) viewDialog.findViewById(R.id.msg);
		return this;
	}

	public void show() {
		builder.show();
	}

	public void dismiss() {
		builder.dismiss();
	}

}
