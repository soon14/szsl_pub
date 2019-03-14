package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author :lizhengcao
 * @date :2019/3/13
 * E-mail:lizc@bsoft.com.cn
 * @类说明 收件人地址实体类
 */
public class CloudExpressReceiverModel extends AbsBaseVoSerializ {

    public String addrId;
    public String userId;
    public String collectName;
    public String collectPhone;
    public String collectProvincial;
    public String provinceCode;
    public String collectCity;
    public String cityCode;
    public String collectArea;
    public String areaCode;
    public String collectDetailAdress;
    public String postCode;
    public String tolerate;
    public boolean isEdit;
    public boolean select;

    @Override
    public void buideJson(JSONObject job) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
