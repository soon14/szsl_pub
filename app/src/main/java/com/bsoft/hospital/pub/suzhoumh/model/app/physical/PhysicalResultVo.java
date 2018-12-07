package com.bsoft.hospital.pub.suzhoumh.model.app.physical;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhysicalResultVo extends AbsBaseVoSerializ {

    public String tjrq; //体检日期（yyyy-MM-dd）
    public String xm; //姓名
    public String xb; //性别
    public String nl; //年龄
    public String zs; //综述
    public String jy; //建议
    public String dwbh; //单位编号
    public String dwmc; //单位名称
    public ArrayList<PhysicalDetailVo> tjxq; //体检详情

    @Override
    public void buideJson(JSONObject job) throws JSONException {
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
