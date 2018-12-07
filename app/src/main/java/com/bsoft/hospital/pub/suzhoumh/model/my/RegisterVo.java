package com.bsoft.hospital.pub.suzhoumh.model.my;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 9:22
 */
public class RegisterVo extends AbsBaseVoSerializ {

    public String ghsj;
    public String rylb;
    public String jzks;
    public String jzys;
    public String zlf;
    public String zhzf;
    public String grzfu;
    public String grzfei;
    public String xjzf;

    @Override
    public void buideJson(JSONObject job) {
        try {
            if (!job.isNull("ghsj")) {
                this.ghsj = job.getString("ghsj");
            }
            if (!job.isNull("rylb")) {
                this.rylb = job.getString("rylb");
            }
            if (!job.isNull("jzks")) {
                this.jzks = job.getString("jzks");
            }
            if (!job.isNull("jzys")) {
                this.jzys = job.getString("jzys");
            }
            if (!job.isNull("zlf")) {
                this.zlf = job.getString("zlf");
            }
            if (!job.isNull("zhzf")) {
                this.zhzf = job.getString("zhzf");
            }
            if (!job.isNull("grzfu")) {
                this.grzfu = job.getString("grzfu");
            }
            if (!job.isNull("grzfei")) {
                this.grzfei = job.getString("grzfei");
            }
            if (!job.isNull("xjzf")) {
                this.xjzf = job.getString("xjzf");
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

