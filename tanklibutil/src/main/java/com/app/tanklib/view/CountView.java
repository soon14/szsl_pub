package com.app.tanklib.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.app.tanklib.R;
import com.app.tanklib.util.DensityUtil;

public class CountView extends TextView {

	private int count;

	public CountView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public CountView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CountView(Context context) {
		super(context);

		init();
	}

	public void setCount(int count) {
		if (count > 99) {
			setText("99+");
			setBackgroundResource(R.drawable.count_ic);
			setVisibility(View.VISIBLE);
		} else if (count <= 0) {
			setText("");
			setVisibility(View.GONE);
		} else if(count>=10){
			setText(String.valueOf(count));
			setBackgroundResource(R.drawable.count_ic);
			setVisibility(View.VISIBLE);
		}else{
			setText(String.valueOf(count));
			setVisibility(View.VISIBLE);
		}

		invalidate();
	}

	private void init() {
		setBackgroundResource(R.drawable.count1_ic);
		// 重心中间
		setGravity(Gravity.CENTER);
//		// 下边距4dp
		setPadding(DensityUtil.getPixelsFromDip(getResources(), 8.0f), 0,
				DensityUtil.getPixelsFromDip(getResources(), 8.0f),
				DensityUtil.getPixelsFromDip(getResources(), 4.0f));
		// 白色
		setTextColor(Color.WHITE);
//		// 黑色阴影
//		setShadowLayer(1.0f, 1.0f, 1.0f, Color.BLACK);
		// 粗体
		setTypeface(null, Typeface.BOLD);
	}

}
