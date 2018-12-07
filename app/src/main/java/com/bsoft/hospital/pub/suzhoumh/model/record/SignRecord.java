package com.bsoft.hospital.pub.suzhoumh.model.record;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class SignRecord extends AbsBaseVoSerializ {

	public Long id;

	public Long uid;

	public String name;

	public String recordid;

	public long signdate;

	public long expiredate;

	public Long doctorid;

	public String doctorname;

	public Long orgid;

	public String orgname;

	public String servercontent;

	public long canceldate;

	public Byte category;

	public Byte status;

	public long createdate;

	@Override
	public void buideJson(JSONObject job) throws JSONException {

	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
