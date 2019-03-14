package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener;

import com.bsoft.hospital.pub.suzhoumh.model.CloudExpressReceiverModel;

/**
 * @author :lizhengcao
 * @date :2019/3/14
 * E-mail:lizc@bsoft.com.cn
 * @类说明 收件人地址监听
 */
public interface CloudExpressReceiverListener {
    void onEdit(CloudExpressReceiverModel data);

    void onDefaultAddress(CloudExpressReceiverModel data);
}
