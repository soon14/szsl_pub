package com.app.tanklib.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.R;

/**
 * 类说明
 * 
 * @author zkl
 * @version 2012-8-14 下午02:35:40
 * 
 */

public class FooterView extends LinearLayout {

	public TextView text;
	public View view;

	public FooterView(Context context) {
		super(context);
		init(context);
	}

	public FooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	void init(Context context) {
		LayoutInflater la = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = la.inflate(R.layout.footer_view, null);
		this.text = (TextView) view.findViewById(R.id.text);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		addView(view, params);
	}


//	public void loadMore() {
//		this.but.setClickable(true);
//		this.but.setBackgroundResource(R.drawable.footer_view_but_bt);
//		this.but.setText("加载更多");
//		this.but.setTextColor(Color.BLACK);
//	}
//
//	public void reLoadMore() {
//		this.but.setClickable(true);
//		this.but.setBackgroundResource(R.drawable.footer_view_but_bt);
//		this.but.setText("加载更多");
//		this.but.setTextColor(Color.BLACK);
//	}
//
//	public void loading() {
//		this.but.setClickable(false);
//		this.but.setBackgroundResource(R.drawable.footer_but_b);
//		this.but.setText("加载中...");
//		this.but.setTextColor(R.color.hint);
//	}
//
//	public void loaded() {
//		this.but.setClickable(false);
//		this.but.setBackgroundResource(R.drawable.footer_but_b);
//		this.but.setText("已加载全部");
//		this.but.setTextColor(R.color.hint);
//	}
	

	public void setAllVisibility(int visibility) {
		this.view.setVisibility(visibility);
	}

	public void setCommVisibility(int visibility) {
		super.setVisibility(visibility);
	}

}
