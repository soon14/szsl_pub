package com.bsoft.hospital.pub.suzhoumh.activity.app.report.reportimg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * @author :lizhengcao
 * @date :2017/3/17
 * E-mail:lizc@bsoft.com.cn
 * @类说明 图片工具类
 */

public class BitmapUtils {

    /**
     * 将字符串转换成Bitmap类型
     */
    public static Bitmap stringtoBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
