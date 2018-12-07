package com.bsoft.hospital.pub.suzhoumh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 12:38
 */
public class UnscrollableGridView extends GridView {
    public UnscrollableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnscrollableGridView(Context context) {
        super(context);
    }

    public UnscrollableGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}