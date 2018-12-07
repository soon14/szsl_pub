package com.app.tanklib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.app.tanklib.R;

public class AlertDialog {

	Context context;
	LayoutInflater mInflater;
	Dialog builder;
	View viewDialog;
	TextView title, msg, okText;
	View driver;

	public AlertDialog(Context context) {
		this.context = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public AlertDialog setButton(String text, OnClickListener click) {
		okText.setText(text);
		okText.setOnClickListener(click);
		return this;
	}
	
	public AlertDialog setButton(String text) {
		okText.setText(text);
		okText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				builder.dismiss();
			}
		});
		return this;
	}


	public AlertDialog title(String text) {
		title.setText(text);
		return this;
	}

	public AlertDialog message(String text) {
		msg.setText(text);
		return this;
	}

	public AlertDialog color(int color) {
		title.setTextColor(context.getResources().getColor(color));
		driver.setBackgroundColor(context.getResources().getColor(color));
		return this;
	}

	public AlertDialog build(boolean cancelable, int widthpx) {
		builder = new Dialog(context, R.style.alertDialogTheme);
		builder.setCancelable(cancelable);
		viewDialog = mInflater.inflate(R.layout.dialog_alert, null);
		// 设置对话框的宽高
		LayoutParams layoutParams = new LayoutParams(widthpx,
				LayoutParams.WRAP_CONTENT);
		builder.setContentView(viewDialog, layoutParams);
		title = (TextView) viewDialog.findViewById(R.id.title);
		msg = (TextView) viewDialog.findViewById(R.id.msg);
		okText = (TextView) viewDialog.findViewById(R.id.ok);
		driver = viewDialog.findViewById(R.id.driver);
		return this;
	}

	public void show() {
		builder.show();
	}

	public void dismiss() {
		builder.dismiss();
	}

}
