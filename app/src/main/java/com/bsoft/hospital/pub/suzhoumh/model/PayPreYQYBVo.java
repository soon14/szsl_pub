package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 诊间支付，园区医保预支付VO
 * Created by Administrator on 2016/5/12.
 */
public class PayPreYQYBVo extends AbsBaseVoSerializ {
    public String hospId;//医疗机构编号
    public String membIdSocial;//个人编号
    public String hic_sn;//交易号
    public String invoiceMoney;//费用总额
    public String selfMoney;//自费总额
    public String zlMoney;//自理总额
    public String zfuMoney;//自负总额
    public String A1Money;//个人账户支付
    public String A4Money;//大病统筹支付
    public String A6Money;//大病补充支付
    public String missMoney;//个人账户不足现金
    public String cashMoney;//个人现金支付
    public String clinicSpecMoney;//特定门诊费用
    public String medicalSpecMoney;//特殊医疗费用

    public String interfaceType;//接口类型

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
