package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/26.
 */
public class PayedGroupVo extends AbsBaseVoSerializ {

    public String jfrq="";//缴费日期
    public ArrayList<PayedVo> jfxq;

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
