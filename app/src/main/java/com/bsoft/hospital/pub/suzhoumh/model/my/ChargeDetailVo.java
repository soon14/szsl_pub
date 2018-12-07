package com.bsoft.hospital.pub.suzhoumh.model.my;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 10:17
 */
public class ChargeDetailVo extends AbsBaseVoSerializ {

    public String xmmc;
    public String zjje;
    public String zfje;

    @Override
    public void buideJson(JSONObject job) {
        try {
            if (!job.isNull("xmmc")) {
                this.xmmc = job.getString("xmmc");
            }
            if (!job.isNull("zjje")) {
                this.zjje = job.getString("zjje");
            }
            if (!job.isNull("zfje")) {
                this.zfje = job.getString("zfje");
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

