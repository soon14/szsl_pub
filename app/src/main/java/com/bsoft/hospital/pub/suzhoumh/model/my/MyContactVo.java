package com.bsoft.hospital.pub.suzhoumh.model.my;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl {"id":"9","mobile":"2323","name":"1","relation":"1"}
 */

public class MyContactVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public String mobile;
	public String name;
	public String relation;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("mobile")) {
				this.mobile = job.getString("mobile");
			}
			if (!job.isNull("relation")) {
				this.relation = job.getString("relation");
			}
			if (!job.isNull("name")) {
				this.name = job.getString("name");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((MyContactVo) obj).id);
	}

}
