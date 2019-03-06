package com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog.listener;

import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudWaitingModel;

/**
 * @author :lizhengcao
 * @date :2019/3/6
 * E-mail:lizc@bsoft.com.cn
 * @类说明 候诊对话框监听
 */
public interface CloudWaitingDialogListener {

    void onWaitingAccept(CloudWaitingModel data);

    void onWaitingRefuse(CloudWaitingModel data);
}
