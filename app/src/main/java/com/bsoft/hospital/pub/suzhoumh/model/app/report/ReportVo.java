package com.bsoft.hospital.pub.suzhoumh.model.app.report;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class ReportVo  extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String sqdh;
	public String sqks;
	public String sqys;
	public String jcks;
	public String jcys;
	public String jcxm;
	public String jcsj;
	public String wcbz;

	@Override
	public void buideJson(JSONObject job) {
	}

	@Override
	public JSONObject toJson() {

		return null;
	}


}
