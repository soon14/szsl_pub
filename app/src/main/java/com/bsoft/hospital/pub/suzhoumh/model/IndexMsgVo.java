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

public class IndexMsgVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	// 主
	public String type;
	public String title;
	public String date;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("type")) {
				this.type = job.getString("type");
			}
			if (!job.isNull("title")) {
				this.title = job.getString("title");
			}
			if (!job.isNull("date")) {
				this.date = job.getString("date");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("type", type);
			ob.put("title", title);
			ob.put("date", date);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

}
