package com.app.tanklib.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * modle基础类，只须实现buideJson和toJson 方法
 * 
 * @author zkl
 * 
 */
public abstract class AbsBaseVoSerializ implements BaseVoSerializ {

	private static final long serialVersionUID = 1L;

	public void buideJson(String josnString) {
		if (null != josnString) {
			try {
				JSONObject job = new JSONObject(josnString);
				buideJson(job);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void buideJson(JSONObject job) throws JSONException;

	public String toJsonString() {
		JSONObject ob = toJson();
		if (null != ob) {
			return ob.toString();
		}
		return null;
	}

	public abstract JSONObject toJson();
}
