package com.bsoft.hospital.pub.suzhoumh.model;

import android.text.TextUtils;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 诊间支付，预支付VO
 * Created by Administrator on 2016/5/11.
 */
public class PayPreVo extends AbsBaseVoSerializ {
    public String jssm;//结算说明,
    public String xjje;//现金金额，
    public String fphm;//发票号码

    public PayPreSZYBVo szyb;
    public PayPreYQYBVo yqyb;

    /**
     * 判断是否有其他支付
     *
     * @return
     */
    public boolean isOtherPay() {
        return !(TextUtils.isEmpty(xjje) || TextUtils.equals("0.00", xjje));
    }

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
