package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/1/15
 * E-mail:lizc@bsoft.com.cn
 * @类说明 获取排班信息
 */
public class CloudGetWorkDateModel extends AbsBaseVoSerializ {

    public String gzrq; //工作日期
    public String dqrq; //当前日期
    public String zblb; //作班类别
    public String ygdm; //员工代码

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
