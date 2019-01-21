package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author :lizhengcao
 * @date :2019/1/21
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室预约记录实体类
 */
public class CloudAppointmentRecordModel extends AbsBaseVoSerializ {

    public String jzwz;
    public String yyzt;
    public String dqrq;
    public String zblb;
    public String yyrq;
    public String ksmc;
    public String sjd;
    public String ysid;
    public String ksid;
    public String jzxh;
    public String djsj;
    public String yylx;
    public String ysxm;
    public String id;
    public String brxz;
    public String brxm;

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
