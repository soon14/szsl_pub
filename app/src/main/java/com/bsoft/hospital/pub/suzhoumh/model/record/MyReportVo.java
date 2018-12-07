package com.bsoft.hospital.pub.suzhoumh.model.record;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 * 
 */

public class MyReportVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String pic;
	public String id;
	

	@Override
	public void buideJson(JSONObject job) {
	}

	@Override
	public JSONObject toJson() {
		return null;
	}


}
