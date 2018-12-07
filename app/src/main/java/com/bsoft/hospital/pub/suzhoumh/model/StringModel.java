package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/26.
 */
public class StringModel extends AbsBaseVoSerializ {

    public String message;

    @Override
    public void buideJson(JSONObject job) throws JSONException {
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

}
