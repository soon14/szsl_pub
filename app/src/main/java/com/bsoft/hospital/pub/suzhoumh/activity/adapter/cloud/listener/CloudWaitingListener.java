package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener;

import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudWaitingModel;

/**
 * @author :lizhengcao
 * @date :2019/3/5
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云候诊刷新接口回调
 */
public interface CloudWaitingListener {
    void onCloudWaitingRefresh();

    void onCloudWaitingShowDialog(CloudWaitingModel data);
}
