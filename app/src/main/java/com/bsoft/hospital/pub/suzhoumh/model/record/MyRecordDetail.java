package com.bsoft.hospital.pub.suzhoumh.model.record;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;



public class MyRecordDetail extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String orid;
	public String chiefcomplaint;
	public String clinicid;
	public String deptcode;
	public String deptname;
	public String dtorname;
	public String hospcode;
	public String hospname;
	public String treatopinion;
	public String vdate;


	@Override
	public void buideJson(JSONObject job) {
		
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

	// @Override
	// public boolean equals(Object obj) {
	// return this.showId == ((DynamicShow) obj).showId;
	// }

}
