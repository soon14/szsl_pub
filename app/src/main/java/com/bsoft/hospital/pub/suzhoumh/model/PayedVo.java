package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 诊间支付,已支付项目vo
 * Created by Administrator on 2016/5/10.
 */
public class PayedVo extends AbsBaseVoSerializ {

    public String fphm = "";//发票号码,
    public String lb = "";//类别,
    public String xm = "";//姓名，
    public String grbh = "";//个人编号，
    public String sfxm = "";//收费项目，
    public String xmje = "";//合计金额,
    public String rq = "";//日期，
    public String czgh = "";//收款员
    public String bz = "";//备注
    public String zxpb = "";// 0未扫码 1已取药 2已执行

    public String ksid;

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
