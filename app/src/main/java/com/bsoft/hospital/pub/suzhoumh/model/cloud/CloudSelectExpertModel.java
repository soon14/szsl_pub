package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/1/14
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室选择专家
 */
public class CloudSelectExpertModel extends AbsBaseVoSerializ {

    public String ysjj; //医生简介
    public String ksdm; //科室代码
    public String ygzw; //员工职位
    public String zjfy; //专家费用
    public String ygxm; //员工姓名
    public String ygjb; //员工级别
    public String ygdm; //员工代码

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
