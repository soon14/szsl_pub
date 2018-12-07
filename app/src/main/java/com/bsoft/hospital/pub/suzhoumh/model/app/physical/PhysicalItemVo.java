package com.bsoft.hospital.pub.suzhoumh.model.app.physical;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

public class PhysicalItemVo extends AbsBaseVoSerializ {

    public String jcxm; //检查项目
    public String jcjg; //检查结果
    public String dw; //单位
    public String ckfw; //参考范围
    public String ts; //提示

    @Override
    public void buideJson(JSONObject job) throws JSONException {
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
