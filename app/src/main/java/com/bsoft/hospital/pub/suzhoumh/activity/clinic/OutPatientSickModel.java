package com.bsoft.hospital.pub.suzhoumh.activity.clinic;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2017/3/10
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class OutPatientSickModel extends AbsBaseVoSerializ {
    public String mzhm;         //门诊号码
    public String brxm;         //病人姓名
    public String jlsj;         //记录时间
    public String blxq;         //病例详情

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
