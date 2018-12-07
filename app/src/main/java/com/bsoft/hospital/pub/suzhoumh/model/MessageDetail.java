package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 */

public class MessageDetail extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public int kinds;
	public String content;
	public long date;
	public int mrid;
	public int msgtype;
	public int status;
	public String uid;

	@Override
	public void buideJson(JSONObject job) {
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
