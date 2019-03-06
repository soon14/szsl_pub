package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/1/21
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室预约确认实体类
 */
public class CloudAppointmentConfirmModel extends AbsBaseVoSerializ {
    public String success;
    public String regID;

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
