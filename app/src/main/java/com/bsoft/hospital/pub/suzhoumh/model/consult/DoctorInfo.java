package com.bsoft.hospital.pub.suzhoumh.model.consult;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class DoctorInfo extends AbsBaseVoSerializ{

	public String id;
	public String uid;
	public String name;
	public String deptid;
	public String deptname;
	public String professionaltitle;
	public String introduce;
	public String sexcode;
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
