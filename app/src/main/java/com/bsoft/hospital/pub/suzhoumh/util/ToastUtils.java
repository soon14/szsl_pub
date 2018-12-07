package com.bsoft.hospital.pub.suzhoumh.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * 吐司工具类
 */
@SuppressLint("ShowToast")
public class ToastUtils {
    private static Toast mToast;

    private static Context getContext() {
        return ContextUtils.getContext();
    }


    public static void showToastShort(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    public static void showToastLong(int messageResId) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), messageResId, Toast.LENGTH_LONG);
        } else {
            mToast.setText(messageResId);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
}
