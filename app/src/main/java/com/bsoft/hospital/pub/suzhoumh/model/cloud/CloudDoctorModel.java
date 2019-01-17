package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/1/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 获取云诊室医生按日期)
 */
public class CloudDoctorModel extends AbsBaseVoSerializ {
    public String yssc;
    public String ygzw;  //员工职位
    public String ygxm; //员工姓名
    public String ygdm; //员工代码

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
