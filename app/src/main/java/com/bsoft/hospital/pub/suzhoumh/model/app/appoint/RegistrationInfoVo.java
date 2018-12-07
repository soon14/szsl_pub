package com.bsoft.hospital.pub.suzhoumh.model.app.appoint;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 挂号信息
 * Created by Administrator on 2016/5/23.
 */
public class RegistrationInfoVo extends AbsBaseVoSerializ {

    /**
     * 就诊序号
     */
    public String jzxh;
    /**
     * 挂号金额
     */
    public String ghje;
    /**
     * 性质
     */
    public String xzmc;
    /**
     * 门诊号码
     */
    public String mzhm;
    /**
     * 性别
     */
    public String brxb;
    /**
     * 就诊位置
     */
    public String jzwz;
    /**
     * 年龄
     */
    public String brnl;
    /**
     * 姓名
     */
    public String brxm;


    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
