package com.bsoft.hospital.pub.suzhoumh.model.pay;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 支付的业务
 * Created by Administrator on 2016/1/8.
 */
public class BusTypeVo extends AbsBaseVoSerializ {

    public int bustype;
    public String orgid;
    public String orgname;

    public String jzkh;//充值 用到的参数
    public String fphm;//诊间支付 用到的参数
    public String yyxh;//预约挂号 用到的参数
    public String zyh;//住院预缴金

    public String sfzh;
    public String yyid;
    public String rylb;


    public String sbxhs;



    public BusTypeVo()
    {

    }

    public BusTypeVo(int bustype, String orgid, String orgname) {
        this.bustype = bustype;
        this.orgid = orgid;
        this.orgname = orgname;
    }

    //预约取号
    public BusTypeVo(int bustype, String orgid, String orgname, String yyid, String rylb, String sfzh, String fphm) {
        this.bustype = bustype;
        this.orgid = orgid;
        this.orgname = orgname;
        this.yyid = yyid;
        this.rylb = rylb;
        this.sfzh = sfzh;
        this.fphm = fphm;
    }



    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public String toJsonString() {
        return super.toJsonString();
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
