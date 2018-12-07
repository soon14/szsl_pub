package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class FeeDetailVo extends AbsBaseVoSerializ {

    public String total;
    public String brid;//   病人编号    string
    public String brxm;//   病人姓名    string
    public String mzhm;//   门诊号码     string

    public List<FeeDetailItem> fyxqlist;

    @Override
    public void buideJson(JSONObject job) throws JSONException {
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    public class FeeDetailItem {
        public String fymc;// 费用名称    string
        public String sl;//    数量    String
        public String dj;//    单价    String
        public String je;//    金额    String
        public String zf;//    自费金额    String
        public String dw;//    单位    String
        public String gg;//     规格    String
        public String zfbl;//   自费比例    String

    }
}
