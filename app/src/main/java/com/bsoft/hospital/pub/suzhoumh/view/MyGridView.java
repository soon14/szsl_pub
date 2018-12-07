package com.bsoft.hospital.pub.suzhoumh.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author :lizhengcao
 * @date :2017/3/15
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
