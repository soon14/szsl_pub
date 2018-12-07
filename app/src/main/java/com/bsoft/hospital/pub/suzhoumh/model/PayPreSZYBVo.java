package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;


public class PayPreSZYBVo extends AbsBaseVoSerializ {


    /**
     * appId : FF5FA626F005773C77B87C796CA72DC2
     * personName : 葛添义
     * socialCardNum : 0003269326
     * hospitalNum : 3005
     * lshNum : 3005003086791210
     * totalMoney : 0.16
     * personCardNum : 320504198605273014
     */

    public String appId;
    public String personName;
    public String socialCardNum;
    public String hospitalNum;
    public String lshNum;
    public String totalMoney;
    public String personCardNum;


    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
