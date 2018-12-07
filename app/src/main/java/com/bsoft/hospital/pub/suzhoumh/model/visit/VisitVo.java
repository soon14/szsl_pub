package com.bsoft.hospital.pub.suzhoumh.model.visit;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/29.
 */
public class VisitVo extends AbsBaseVoSerializ {

    public String ksdm;
    public String ksmc;
    public String ygdm;
    public String ygxm;
    public String zdmc;
    public String jzxh;
    public String jzsj;
    public String brid;//病人id

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
