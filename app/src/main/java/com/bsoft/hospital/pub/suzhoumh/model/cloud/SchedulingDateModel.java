package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/1/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室获取可预约日期的实体类
 */
public class SchedulingDateModel extends AbsBaseVoSerializ {

    public String month;
    public String year;
    public String weekDate;
    public String day;
    public String completeDate;

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
