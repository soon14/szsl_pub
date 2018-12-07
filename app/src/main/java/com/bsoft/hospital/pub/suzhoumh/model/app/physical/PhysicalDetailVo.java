package com.bsoft.hospital.pub.suzhoumh.model.app.physical;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhysicalDetailVo extends AbsBaseVoSerializ {

    public String zhxm; //综合项目
    public ArrayList<PhysicalItemVo> zx; //子项

    @Override
    public void buideJson(JSONObject job) throws JSONException {
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}

