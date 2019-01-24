package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author :lizhengcao
 * @date :2019/1/23
 * E-mail:lizc@bsoft.com.cn
 * @类说明 极光推送实体类
 */
public class CloudJpushModel extends AbsBaseVoSerializ {

    public String brxm;
    public String jzxh;

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
