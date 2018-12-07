package com.bsoft.hospital.pub.suzhoumh.model.my;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 9:31
 */
public class ChargeVo extends AbsBaseVoSerializ {

    public String fphm;
    public String sfrq;
    public String rylb;
    public String fpsm;
    public List<ChargeDetailVo> fpxq;

    @Override
    public void buideJson(JSONObject job) {
        try {
            if (!job.isNull("fphm")) {
                this.fphm = job.getString("fphm");
            }
            if (!job.isNull("sfrq")) {
                this.sfrq = job.getString("sfrq");
            }
            if (!job.isNull("rylb")) {
                this.rylb = job.getString("rylb");
            }
            if (!job.isNull("fpsm")) {
                this.fpsm = job.getString("fpsm");
            }
            if (!job.isNull("fpxq")) {
                try {
                    JSONArray array = job.getJSONArray("fpxq");
                    if (null != array && array.length() > 0) {
                        this.fpxq = new ArrayList<ChargeDetailVo>();
                        for (int i = 0; i < array.length(); i++) {
                            ChargeDetailVo vo = new ChargeDetailVo();
                            vo.buideJson(array.getJSONObject(i));
                            fpxq.add(vo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

