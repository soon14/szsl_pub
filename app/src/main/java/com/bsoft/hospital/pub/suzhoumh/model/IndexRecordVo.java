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

public class IndexRecordVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String chiefcomplaint;
	public String hospname;
	public String treatopinion;
	public long visitdatetime;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("chiefcomplaint")) {
				this.chiefcomplaint = job.getString("chiefcomplaint");
			}
			if (!job.isNull("hospname")) {
				this.hospname = job.getString("hospname");
			}
			if (!job.isNull("treatopinion")) {
				this.treatopinion = job.getString("treatopinion");
			}
			if (!job.isNull("visitdatetime")) {
				this.visitdatetime = job.getLong("visitdatetime");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("chiefcomplaint", chiefcomplaint);
			ob.put("hospname", hospname);
			ob.put("treatopinion", treatopinion);
			ob.put("visitdatetime", visitdatetime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

}
