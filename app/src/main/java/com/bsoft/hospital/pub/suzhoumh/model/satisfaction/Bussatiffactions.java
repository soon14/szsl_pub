package com.bsoft.hospital.pub.suzhoumh.model.satisfaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class Bussatiffactions extends AbsBaseVoSerializ{

	public String typecode;//调查维度id
	public String xxid;//细项id
	public String tachename;//流程环节名称
	public String degree;//满意度
	public String docid;//医生id
	public String docname;//医生姓名
	public String deptid;//医生科室id
	public String deptname;//科室名称
	public String bustime;//业务时间
	public String createtime;//评价时间
	public String uid;//用户id
	public String jzxh;//就诊序号
	public String typetitle;//评价指标名称
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("typecode", typecode);
			ob.put("xxid", xxid);
			ob.put("tachename", tachename);
			ob.put("degree", degree);
			ob.put("docid", docid);
			ob.put("docname", docname);
			ob.put("deptid", deptid);
			ob.put("deptname",deptname);
			ob.put("bustime", bustime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

}
