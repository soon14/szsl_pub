package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 苏州医保短信验证Vo
 *
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-8-14 10:28
 */
public class CheckCodeVo extends AbsBaseVoSerializ {

    public String transcode;
    public String unitno;
    public String workdate;
    public String worktime;
    public String msgid;
    public String errorcode;
    public String errormsg;
    public String extension;
    public String messageflown;
    public String subuserno;
    public String userno;

    @Override
    public void buideJson(JSONObject job) throws JSONException {
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
