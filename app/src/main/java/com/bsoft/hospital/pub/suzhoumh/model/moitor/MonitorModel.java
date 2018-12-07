package com.bsoft.hospital.pub.suzhoumh.model.moitor;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class MonitorModel extends AbsBaseVoSerializ implements
		Comparable<MonitorModel> {

	public Long id;

	public String monitorinfo;

	public int monitortype;

	public int monitorsource;

	public Long uid;

	public long createdate;

	@Override
	public void buideJson(JSONObject job) throws JSONException {

	}

	@Override
	public JSONObject toJson() {
		return null;
	}

	@Override
	public int compareTo(MonitorModel another) {
		return createdate > another.createdate ? 1 : 0;
	}

}
