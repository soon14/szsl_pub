package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener;

import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudScheduleModel;

/**
 * @author :lizhengcao
 * @date :2019/1/17
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室排班表监听
 */
public interface CloudScheduleListener {
    void onCloudScheduleAppointListener(CloudScheduleModel schedule);
}
