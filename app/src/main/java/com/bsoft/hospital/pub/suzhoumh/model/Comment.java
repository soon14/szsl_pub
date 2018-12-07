package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 */

public class Comment extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String content;
	public long createdate;
	public String realname;
	public String header;
	public String drid;

	@Override
	public void buideJson(JSONObject job) {
//		try {
//			if (!job.isNull("content")) {
//				this.content = job.getString("content");
//			}
//			if (!job.isNull("createdate")) {
//				this.createdate = job.getLong("createdate");
//			}
//			if (!job.isNull("uname")) {
//				this.uname = job.getString("uname");
//			}
//			if (!job.isNull("upic")) {
//				this.upic = job.getString("upic");
//			}
//			if (!job.isNull("drid")) {
//				this.drid = job.getString("drid");
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
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
