package com.bsoft.hospital.pub.suzhoumh.model;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class OneDayFeeVo extends AbsBaseVoSerializ {
    public String totalCost;
    public List<MainCost> mainCostList;
    @Override
    public void buideJson(JSONObject job) throws JSONException {
        if(!job.isNull("totalCost"))
        {
            totalCost = job.getString("totalCost");
        }
        if(!job.isNull("mainCostList"))
        {
            JSONArray array = job.getJSONArray("mainCostList");
            if (null != array && array.length() > 0) {
                mainCostList = new ArrayList<MainCost>();
                for (int i = 0; i < array.length(); i++) {
                    MainCost vo = new MainCost();
                    vo.buideJson(array.getJSONObject(i));
                    mainCostList.add(vo);
                }
            }
        }
    }

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        return null;
    }

    public static class MainCost extends AbsBaseVoSerializ
    {
        public String sfzm;
        public String sfmc;
        public String zjje;
        public List<Cost> costList;

        @Override
        public void buideJson(JSONObject job) throws JSONException {
            if(!job.isNull("sfzm"))
            {
                sfzm = job.getString("sfzm");
            }
            if(!job.isNull("sfmc"))
            {
                sfmc = job.getString("sfmc");
            }
            if(!job.isNull("zjje"))
            {
                zjje = job.getString("zjje");
            }
            if(!job.isNull("costList"))
            {
                JSONArray array = job.getJSONArray("costList");
                if (null != array && array.length() > 0) {
                    costList = new ArrayList<Cost>();
                    for (int i = 0; i < array.length(); i++) {
                        Cost vo = new Cost();
                        vo.buideJson(array.getJSONObject(i));
                        costList.add(vo);
                    }
                }
            }
        }

        @Override
        public JSONObject toJson() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public static class Cost extends AbsBaseVoSerializ
    {
        public String fyxm;
        public String fyrq;
        public String fymc;
        public String fydj;
        public String fysl;
        public String zjje;

        @Override
        public void buideJson(JSONObject job) throws JSONException {
            if(!job.isNull("fyxm"))
            {
                fyxm = job.getString("fyxm");
            }
            if(!job.isNull("fyrq"))
            {
                fyrq = job.getString("fyrq");
            }
            if(!job.isNull("fymc"))
            {
                fymc = job.getString("fymc");
            }
            if(!job.isNull("fydj"))
            {
                fydj = job.getString("fydj");
            }
            if(!job.isNull("fysl"))
            {
                fysl = job.getString("fysl");
            }
            if(!job.isNull("zjje"))
            {
                zjje = job.getString("zjje");
            }
        }

        @Override
        public JSONObject toJson() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
