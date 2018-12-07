package com.bsoft.hospital.pub.suzhoumh.model;


import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 *         "id": 1, "intro": "", "modifydate": 1408080093000, "modifyuser": 2,
 *         "oid": 4896, "pid": 0, "property": "bgs", "spell": "bgs", "status":
 *         1, "tel": "0571-88925672", "title": "办公室"
 */

public class DeptModelVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public String oid;
	public String title;
	public String pid;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("oid")) {
				this.oid = job.getString("oid");
			}
			if (!job.isNull("title")) {
				this.title = job.getString("title");
			}
			if (!job.isNull("pid")) {
				this.pid = job.getString("pid");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("id", id);
			ob.put("oid", oid);
			ob.put("title", title);
			ob.put("pid", pid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

	// @Override
	// public boolean equals(Object obj) {
	// return this.showId == ((DynamicShow) obj).showId;
	// }

}
