package com.bsoft.hospital.pub.suzhoumh.util;

import android.app.Activity;
import android.view.WindowManager;

/**
 * @author :lizhengcao
 * @date :2018/6/5
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class DeviceUtil {

    /**
     * 设置安全窗口，禁用系统截屏。防止 App 中的一些界面被截屏，并显示在其他设备中造成信息泄漏。
     * （常见手机设备系统截屏操作方式为：同时按下电源键和音量键。）
     *
     * @param activity
     */
    public static void noScreenshots(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }
}
