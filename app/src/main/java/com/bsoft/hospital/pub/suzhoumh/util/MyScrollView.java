package com.bsoft.hospital.pub.suzhoumh.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MyScrollView extends HorizontalScrollView{
	//是否第一次初始化界面
		private boolean once;

		//屏幕宽度
		private int mScreenWidth;

		//可以显示下一个界面的最小滑动距离
		private int mScrollWidth;

		//下一个界面的宽度
		private int mExtraViewWidth = 150;

		public MyScrollView(Context context, AttributeSet attrs) {
			super(context, attrs);
			mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			/**
			 * 显示的设置一个宽度
			 */
			if (!once) {
				LinearLayout wrapper = (LinearLayout) getChildAt(0);
				ViewGroup contentView = (ViewGroup) wrapper.getChildAt(0);
				ViewGroup extraView = (ViewGroup) wrapper.getChildAt(1);
				mScrollWidth = mExtraViewWidth / 2;
				contentView.getLayoutParams().width = mScreenWidth;
				extraView.getLayoutParams().width = mExtraViewWidth;
			}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			super.onLayout(changed, l, t, r, b);
			if (changed) {
				// 隐藏下一个界面
				this.scrollTo(0, 0);
				once = true;
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			int action = ev.getAction();
			switch (action) {
			// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
			case MotionEvent.ACTION_UP:
				int scrollX = getScrollX();
				if (scrollX > mScrollWidth)
					this.smoothScrollTo(mScreenWidth - mExtraViewWidth, 0);
				else
					this.smoothScrollTo(0, 0);
				return true;
			}
			return super.onTouchEvent(ev);
		}
}
