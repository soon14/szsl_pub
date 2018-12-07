package com.qiangxi.checkupdatelibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @类说明 设备尺寸
 */

public class ScreenUtil {
    /**
     * @return 显示指标
     */
    private static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * @return 获得屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * @return 获得屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }
}
