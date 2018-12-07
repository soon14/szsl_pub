package com.bsoft.hospital.pub.suzhoumh.model.my;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 * 
 */

public class StreetVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public String title;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("title")) {
				this.title = job.getString("title");
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
