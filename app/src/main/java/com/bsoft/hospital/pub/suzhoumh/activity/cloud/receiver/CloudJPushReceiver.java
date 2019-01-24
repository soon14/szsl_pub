package com.bsoft.hospital.pub.suzhoumh.activity.cloud.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudClinicActivity;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudJpushModel;
import com.bsoft.hospital.pub.suzhoumh.util.JsonUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * @author :lizhengcao
 * @date :2019/1/22
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */
public class CloudJPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e("CloudJPushReceiver", "通知进来了");
            Intent mIntent = new Intent(context, CloudClinicActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            CloudJpushModel jpush = JsonUtil.jsonToObject(json, CloudJpushModel.class);
            Log.e("jpush传过来的字符串：", "姓名：" + jpush.brxm + "就诊序号：" + jpush.jzxh);
            context.startActivity(mIntent);
        }
    }
}