package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl

 */

public class CodeModel extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id;
	public String msg="操作失败";
	public String hascard;
	public String mobile;
	public String sign;
	public String phrid;

	@Override
	public void buideJson(JSONObject job) {
//		try {
//			if (!job.isNull("id")) {
//				this.id = job.getString("id");
//			}
//			if (!job.isNull("msg")) {
//				this.msg = job.getString("msg");
//			}
//			if (!job.isNull("hascard")) {
//				this.hascard = job.getBoolean("hascard");
//			}
//			if (!job.isNull("phrid")) {
//				this.phrid = job.getString("phrid");
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
