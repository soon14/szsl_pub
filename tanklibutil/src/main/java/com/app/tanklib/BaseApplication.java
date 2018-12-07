package com.app.tanklib;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.task.DownloadThreadPool;

import java.io.File;
import java.util.HashMap;

public abstract class BaseApplication extends Application {
    public abstract String getTag();

    private static LocalBroadcastManager mLocalBroadcastManager;

    // 保存地址
    public String storeDir, storeImageUrl;

    public HashMap<String, Object> serviceMap;

    // 分辨率-宽带
    private static int widthPixels;
    private static int heightPixels;

    public static int getWidthPixels() {
        if (0 == widthPixels) {
            WindowManager localWindowManager = (WindowManager) AppContext
                    .getContext().getSystemService("window");
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            localWindowManager.getDefaultDisplay().getMetrics(
                    localDisplayMetrics);
            widthPixels = localDisplayMetrics.widthPixels;
            heightPixels = localDisplayMetrics.heightPixels;
        }
        return widthPixels;
    }

    public static int getHeightPixels() {
        if (0 == heightPixels) {
            WindowManager localWindowManager = (WindowManager) AppContext
                    .getContext().getSystemService("window");
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            localWindowManager.getDefaultDisplay().getMetrics(
                    localDisplayMetrics);
            widthPixels = localDisplayMetrics.widthPixels;
            heightPixels = localDisplayMetrics.heightPixels;
        }
        return heightPixels;
    }

    // 得到高宽比
    public float getWH() {
        int w = getWidthPixels();
        if (w <= 0) {
            return 1;
        }
        int h = getHeightPixels();
        if (h <= 0) {
            return 1;
        }
        return (float) w / h;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 注册App异常崩溃处理器 (开发时，最好注释一下，影响错误信息打印)
        // Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        AppContext.setContext(getApplicationContext());

        serviceMap = new HashMap<String, Object>();

        CacheManage cache = new CacheManage(this);
        Preferences preferences = new Preferences(this);
        serviceMap.put("com.szzy.app.preferences", preferences);
        serviceMap.put("com.szzy.app.service.cachemanage", cache);
    }

    public Object getSystemService(String key) {
        if (null != serviceMap) {
            if (this.serviceMap.containsKey(key)) {
                return serviceMap.get(key);
            }
        }
        return super.getSystemService(key);
    }

    public String getStoreDir() {
        if (null == storeDir) {
            File f = Environment.getExternalStorageDirectory();
            if (null != f) {
                StringBuffer path = new StringBuffer();
                path.append(f.getPath()).append("/.").append(getTag())
                        .append("/");
                File dir = new File(path.toString());
                if (!dir.exists()) {
                    dir.mkdir();
                }
                this.storeDir = path.toString();
            }
        }
        return storeDir;
    }

    public String getStoreImageUrl() {
        if (null == storeImageUrl) {
            if (null == getStoreDir()) {
                return null;
            } else {
                this.storeImageUrl = new StringBuffer(getStoreDir()).append(
                        "show.jpg").toString();
            }
        }
        return this.storeImageUrl;
    }

    public synchronized void resetCacheManage() {
        DownloadThreadPool.getInstance().cleanCache();
        // 新清除缓存
        CacheManage cacheManage = (CacheManage) serviceMap
                .get("com.szzy.app.service.cachemanage");
        // cacheManage.cleanCache();
        cacheManage = new CacheManage(this);
        // 重置DirskCache
        serviceMap.put("com.szzy.app.service.cachemanage", cacheManage);
    }

    public static void sendProcessBroadcast(Intent intent) {
        if (null == mLocalBroadcastManager) {
            mLocalBroadcastManager = LocalBroadcastManager
                    .getInstance(AppContext.getContext());
        }
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    public static void registerProcessReceiver(BroadcastReceiver mReceiver,
                                               IntentFilter filter) {
        if (null == mLocalBroadcastManager) {
            mLocalBroadcastManager = LocalBroadcastManager
                    .getInstance(AppContext.getContext());
        }
        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    public static void unregisterProcessReceiver(BroadcastReceiver mReceiver) {
        if (null == mLocalBroadcastManager) {
            mLocalBroadcastManager = LocalBroadcastManager
                    .getInstance(AppContext.getContext());
        }
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.serviceMap.clear();
        AppContext.setContext(null);
    }
}
