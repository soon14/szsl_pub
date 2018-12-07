package com.bsoft.hospital.pub.suzhoumh.activity.appoint.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2017/3/8
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class AppointExpertSelectDate extends AbsBaseVoSerializ {

    public String zblb;
    public String gzrq;
    public String dqrq;

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
