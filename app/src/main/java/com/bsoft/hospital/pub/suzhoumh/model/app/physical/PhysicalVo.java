package com.bsoft.hospital.pub.suzhoumh.model.app.physical;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

public class PhysicalVo extends AbsBaseVoSerializ {

    public String tjrq; //体检日期（yyyy-MM-dd）
    public String tjdh; //体检单号

    @Override
    public void buideJson(JSONObject job) throws JSONException {
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
