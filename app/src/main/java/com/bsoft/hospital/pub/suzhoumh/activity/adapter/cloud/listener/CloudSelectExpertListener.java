package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener;

import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;

/**
 * @author :lizhengcao
 * @date :2019/1/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室中专家列表中预约的监听
 */
public interface CloudSelectExpertListener {
    void onCloudSelectExpertAppointmentORIntroduceListener(CloudSelectExpertModel expert, int pos);
}
