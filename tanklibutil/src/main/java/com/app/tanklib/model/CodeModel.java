package com.app.tanklib.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * 
 * @author zkl

 */

public class CodeModel extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public String msg="操作失败";
	public boolean hascard;
	public String phrid;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("msg")) {
				this.msg = job.getString("msg");
			}
			if (!job.isNull("hascard")) {
				this.hascard = job.getBoolean("hascard");
			}
			if (!job.isNull("phrid")) {
				this.phrid = job.getString("phrid");
			}
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
