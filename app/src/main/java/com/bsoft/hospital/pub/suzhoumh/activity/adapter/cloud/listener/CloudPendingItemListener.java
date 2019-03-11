package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener;

import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudPendingItemModel;

/**
 * @author :lizhengcao
 * @date :2019/3/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 待支付项目内部事件监听
 */
public interface CloudPendingItemListener {
    void onSelect(CloudPendingItemModel data);

    void onPendingItemClick(CloudPendingItemModel data);
}
