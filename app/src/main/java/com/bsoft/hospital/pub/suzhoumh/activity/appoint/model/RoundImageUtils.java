package com.bsoft.hospital.pub.suzhoumh.activity.appoint.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.app.tanklib.bitmap.view.RoundImageView;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * @author :lizhengcao
 * @date :2017/3/27
 * E-mail:lizc@bsoft.com.cn
 * @类说明 圆形图片设置工具类
 */

public class RoundImageUtils {

    public static void setRoundImageView(final RoundImageView riv, final String url, final Activity activity, final ImageLoader imageLoader) {
        riv.setImageResource(R.drawable.doc_header);
        riv.setTag(url);
        // 显示图片的配置
        try {
            new Thread() {
                public void run() {
                    //这儿是耗时操作，完成之后更新UI；
                    if (Utility.checkURL(url)) {
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //更新UI
                                DisplayImageOptions options = new DisplayImageOptions.Builder()
                                        .cacheInMemory(true).cacheOnDisk(true)
                                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                                imageLoader.loadImage(url, options,
                                        new SimpleImageLoadingListener() {

                                            @Override
                                            public void onLoadingComplete(String imageUri,
                                                                          View view, Bitmap loadedImage) {
                                                super.onLoadingComplete(imageUri, view,
                                                        loadedImage);
                                                if (riv.getTag() != null
                                                        && riv.getTag().equals(url)) {
                                                    Drawable drawable = new BitmapDrawable(
                                                            loadedImage);
                                                    riv.setImageDrawable(drawable);
                                                }
                                            }

                                        });
                            }

                        });
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
