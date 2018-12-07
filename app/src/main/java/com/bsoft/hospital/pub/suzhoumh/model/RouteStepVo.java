package com.bsoft.hospital.pub.suzhoumh.model;

import java.util.ArrayList;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 * 
 *         "content": "测试测试", "createdate": 1419321316000, "drid": 5, "drrid":
 *         18, "kinds": 1, "status": 1, "uid": 1, "uname": "测试1"
 */

public class RouteStepVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String time;
	public String distance;
	public String endtext;
	public ArrayList<String> steps=new ArrayList<String>();;

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
//			if (!job.isNull("uheadUrl")) {
//				this.uheadUrl = job.getString("uheadUrl");
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
