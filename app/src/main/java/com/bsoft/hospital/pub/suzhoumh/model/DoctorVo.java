package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class DoctorVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;
	public String id;
	public String uid;
	public String name;
	public String deptid;
	public String deptname;
	public String professionaltitle;
	public String expert;
	public String introduce;
	public String header;
	public String spell;
	public String orgid;
	public String orgname;
	public String sexcode;
	public String code;
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("uid")) {
				this.uid = job.getString("uid");
			}
			if (!job.isNull("name")) {
				this.name = job.getString("name");
			}
			if (!job.isNull("deptid")) {
				this.deptid = job.getString("deptid");
			}
			if (!job.isNull("deptname")) {
				this.deptname = job.getString("deptname");
			}
			if (!job.isNull("professionaltitle")) {
				this.professionaltitle = job.getString("professionaltitle");
			}
			if (!job.isNull("expert")) {
				this.expert = job.getString("expert");
			}
			if (!job.isNull("introduce")) {
				this.introduce = job.getString("introduce");
			}
			if (!job.isNull("header")) {
				this.header = job.getString("header");
			}
			if (!job.isNull("spell")) {
				this.spell = job.getString("spell");
			}
			if (!job.isNull("orgid")) {
				this.orgid = job.getString("orgid");
			}
			if (!job.isNull("orgname")) {
				this.orgname = job.getString("orgname");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public JSONObject toJson() {
		return null;
	}


}
