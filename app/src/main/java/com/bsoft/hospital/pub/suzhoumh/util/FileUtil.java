package com.bsoft.hospital.pub.suzhoumh.util;

import android.os.Environment;

import java.io.File;

/**
 * @author :lizhengcao
 * @date :2019/3/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明 文件工具类
 */
public class FileUtil {


    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath() {
        File path = null;

        if (sdCardIsAvailable()) {
            path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }
}
