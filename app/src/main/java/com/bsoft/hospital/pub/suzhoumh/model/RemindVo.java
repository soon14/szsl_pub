package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 */

public class RemindVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public long pam1;
	public String name;
//	public String remark;
	public int kinds;
	public long pam2;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("pam1")) {
				this.pam1 = job.getLong("pam1");
			}
			if (!job.isNull("pam2")) {
				this.pam2 = job.getLong("pam2");
			}
			if (!job.isNull("name")) {
				this.name = job.getString("name");
			}
			if (!job.isNull("kinds")) {
				this.kinds = job.getInt("kinds");
			}
//			if (!job.isNull("remark")) {
//				this.remark = job.getString("remark");
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
