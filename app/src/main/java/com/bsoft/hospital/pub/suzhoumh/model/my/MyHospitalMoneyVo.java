package com.bsoft.hospital.pub.suzhoumh.model.my;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 */
public class MyHospitalMoneyVo extends AbsBaseVoSerializ {

    public String jkhjcn;
    public String jkhj;
    public String zyhm;
    public String zyh;
    public String brxm;
    public String sfzh;
    public List<Record> list;

    @Override
    public void buideJson(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    public class Record extends AbsBaseVoSerializ
    {
        public String zyhm;//住院号码
        public String zyh;//住院号
        public String brxm;//病人姓名
        public String brch;//病人床号
        public String jkxh;//缴款序号
        public String jkrq;//缴款日期
        public String jkje;//缴款金额
        public String jkfs;//缴款方式
        public String sjhm;//收据号码
        public String zfpb;//作废判别（1作废）
        public String czgh;//操作工号
        public String jscs;//结算次数
        public String ksmc;//科室名称
        public String jkjecn;//结算金额(中文）

        @Override
        public void buideJson(JSONObject jsonObject) throws JSONException {

        }

        @Override
        public JSONObject toJson() {
            return null;
        }
    }
}
