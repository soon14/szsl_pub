package com.bsoft.hospital.pub.suzhoumh.model.my;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 11:20
 */
public class InsuranceVo extends AbsBaseVoSerializ {

    public String brxm;
    public String grbh;
    public String wnzh;
    public String dnzh;

    @Override
    public void buideJson(JSONObject job) {
        try {
            if (!job.isNull("brxm")) {
                this.brxm = job.getString("brxm");
            }
            if (!job.isNull("grbh")) {
                this.grbh = job.getString("grbh");
            }
            if (!job.isNull("wnzh")) {
                this.wnzh = job.getString("wnzh");
            }
            if (!job.isNull("dnzh")) {
                this.dnzh = job.getString("dnzh");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}