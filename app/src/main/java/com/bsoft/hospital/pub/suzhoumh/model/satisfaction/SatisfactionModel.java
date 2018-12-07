package com.bsoft.hospital.pub.suzhoumh.model.satisfaction;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class SatisfactionModel extends AbsBaseVoSerializ{

	public String id;//编号id
	public String busdate;//业务时间
	public String deptid;//科室id
	public String deptidname;//科室名称
	public String sug;//意见建议
	public String diagnosis;//诊断信息
	public String createtime;
	public String uid;
	
	public ArrayList<SectionVo> busSatisfactionExs;//评价环节和指标明细数组
	
	
	public String time;//时间
	public String ks;//科室
	public String content;//内容
	public String jzxh;//就诊序号
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
