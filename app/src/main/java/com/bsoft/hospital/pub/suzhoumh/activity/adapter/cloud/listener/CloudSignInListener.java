package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener;

import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;

/**
 * @author :lizhengcao
 * @date :2019/2/26
 * E-mail:lizc@bsoft.com.cn
 * @类说明 互联网诊室签到取号、挂号单监听
 */
public interface CloudSignInListener {
    void onCloudSignListener(CloudAppointmentRecordModel data);

    void onCloudRegistrationFormListener(CloudAppointmentRecordModel data);
}
