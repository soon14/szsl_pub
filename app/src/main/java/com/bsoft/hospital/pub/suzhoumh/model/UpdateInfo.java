package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 */

public class UpdateInfo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public int code;
	public String descr;
	public String message;
	public String url;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("code")) {
				this.code = job.getInt("code");
			}
			if (!job.isNull("descr")) {
				this.descr = job.getString("descr");
			}
			if (!job.isNull("message")) {
				this.message = job.getString("message");
			}
			if (!job.isNull("newUrl")) {
				this.url = job.getString("newUrl");
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
