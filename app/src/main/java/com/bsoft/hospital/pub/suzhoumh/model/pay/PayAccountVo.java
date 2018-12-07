package com.bsoft.hospital.pub.suzhoumh.model.pay;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 支付账户信息
 * Created by Administrator on 2016/1/8.
 */
public class PayAccountVo extends AbsBaseVoSerializ {

    public String orgid;
    public String orgname;

    public String accountname;//机构的支付宝账号
    public String publickey;//支付宝的公钥
    public String privatekey;//商户的私钥
    public String pid;//商户号
    public String notifyUrl;//服务器异步通知的地址

    public String subject;//商品名称
    public String body;//商品的业务类型
    public String price;//商品价格
    public String tradeno;//商品订单号

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
