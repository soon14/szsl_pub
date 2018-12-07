package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 扫描
 * Created by Administrator on 2016/5/17.
 */
public class ScanVo extends AbsBaseVoSerializ {

    public String YWLX;//业务类型
    public String JQBH;//机器编号


    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
