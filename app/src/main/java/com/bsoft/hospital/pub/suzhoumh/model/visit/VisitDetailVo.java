package com.bsoft.hospital.pub.suzhoumh.model.visit;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/1.
 */
public class VisitDetailVo extends AbsBaseVoSerializ {

    public String xmmc;
    public String hjje;
    public List<JZXQ> jzxq = new ArrayList<JZXQ>();
    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    public class JZXQ extends AbsBaseVoSerializ
    {
        public String fymc;
        public String sl;
        public String dj;
        public String je;
        public String zf;
        public String cfts;
        public String dw;
        public String gg;

        @Override
        public void buideJson(JSONObject jsonObject) throws JSONException {

        }

        @Override
        public JSONObject toJson() {
            return null;
        }
    }
}
