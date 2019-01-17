package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/1/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室选择科室实体类
 */
public class SelectDeptModel extends AbsBaseVoSerializ {

    public String ksdm; //科室代码
    public String ksmc; //科室名称
    public String zjks;//特色门诊中特有参数  1-专科 0-专家

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
