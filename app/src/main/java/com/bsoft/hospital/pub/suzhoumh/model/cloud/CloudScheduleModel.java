package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/1/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室排班实体类
 */
public class CloudScheduleModel extends AbsBaseVoSerializ {

    public String syhy; //剩余号源
    public String jzxh; //就诊序号
    public String jzsj; //截止时间
    public String qssj; //起始时间

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
