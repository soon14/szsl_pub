package com.bsoft.hospital.pub.suzhoumh.model.my;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class PubNoteVo extends AbsBaseVoSerializ {

	public long id;
	public long uid;
	public String name;
	public long orgid;
	public String orgname;
	public long deptid;
	public String deptname;
	public long doctorid;
	public String doctorname;
	public int appointstatus;
	public Date appointtime;
	public long appointpay;
	public Date startdate;
	public Date enddate;
	public Date createdate;

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
