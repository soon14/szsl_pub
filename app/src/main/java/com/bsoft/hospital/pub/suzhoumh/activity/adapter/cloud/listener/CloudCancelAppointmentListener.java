package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener;

import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;

/**
 * @author :lizhengcao
 * @date :2019/1/21
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室取消预约接口监听
 */
public interface CloudCancelAppointmentListener {
    void onCloudCancelAppointmentListener(CloudAppointmentRecordModel record, int pos);
}
