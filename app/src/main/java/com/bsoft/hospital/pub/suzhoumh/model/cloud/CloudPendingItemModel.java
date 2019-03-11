package com.bsoft.hospital.pub.suzhoumh.model.cloud;

import android.text.TextUtils;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/3/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 互联网待支付项目实体类
 */
public class CloudPendingItemModel extends AbsBaseVoSerializ {

    public String mzhm;
    public String sfgh;
    public String fyrq;
    public String brid;
    public String sbxh;
    public String brxm;
    public String ksmc;
    public String hjje;
    public String sfzh;
    public String ysxm;
    public String xmlx;
    public String cfyj;
    public String id;
    public String brxz;
    public boolean isSelected = false;

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    public String xzmc() {
        String xzmc = "";
        if (TextUtils.equals("1000", brxz)) {
            xzmc = "自费";
        } else if (TextUtils.equals("2000", brxz)) {
            xzmc = "公费";
        } else if (TextUtils.equals("3000", brxz)) {
            xzmc = "参保";
        } else if (TextUtils.equals("5000", brxz)) {
            xzmc = "记账";
        } else if (TextUtils.equals("5001", brxz)) {
            xzmc = "本院职工";
        } else if (TextUtils.equals("6000", brxz)) {
            xzmc = "园区医保";
        } else if (TextUtils.equals("7000", brxz)) {
            xzmc = "医疗保险";
        } else if (TextUtils.equals("7001", brxz)) {
            xzmc = "苏州离休";
        } else if (TextUtils.equals("8000", brxz)) {
            xzmc = "吴中医保";
        } else if (TextUtils.equals("8001", brxz)) {
            xzmc = "相城医保";
        } else if (TextUtils.equals("8003", brxz)) {
            xzmc = "吴江医保";
        }

        return xzmc;
    }
}
