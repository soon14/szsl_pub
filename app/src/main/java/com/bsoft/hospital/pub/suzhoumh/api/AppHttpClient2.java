package com.bsoft.hospital.pub.suzhoumh.api;

import android.util.Log;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudConditionDescriptionModel;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author :lizhengcao
 * @date :2019/3/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */
public class AppHttpClient2 {

    private static final int TIMEOUT = 30;
    private OkHttpClient okHttpClient;

    public static AppHttpClient2 getInstance() {
        return new AppHttpClient2();
    }

    private AppHttpClient2() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient = builder.build();
    }

    private void proceedRequest(OkHttpClient client, Request request, HttpPostFilesListener listener) {
        try {
            Response temp = client.newCall(request).execute();
            ResponseBody body = temp.body();
            if (temp.isSuccessful()) {
                listener.onSuccess(body.string());
            } else {
                listener.onFail();
                temp.body().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doHttpPostFiles(String url, List<File> filePaths, String fileNames, ArrayList<BsoftNameValuePair> pairs, HttpPostFilesListener listener) {
        MultipartBody.Builder builder = new MultipartBody.Builder(); //建立请求的内容
        if (pairs != null) {
            for (BsoftNameValuePair pair : pairs) {
                if (!StringUtil.isEmpty(pair.getValue())) {
                    try {
                        builder.addFormDataPart(pair.getName(), URLEncoder.encode(pair.getValue(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
        MediaType mediaType = MediaType.parse("image/jpeg; charset=utf-8");
        if (null != filePaths && filePaths.size() > 0) {
            for (File f : filePaths) {
                //第一个参数是服务器接收的名称，第二个是上传文件的名字，第三个是上传的文件
                builder.addFormDataPart(fileNames, f.getName(), RequestBody.create(mediaType, f));
                Log.e("files", f.getName());

            }
        }


        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        proceedRequest(okHttpClient, request, listener);
    }

}
