package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * modle基础类，只须实现buideJson和toJson 方法
 * 
 * @author zkl
 * 
 */
public class BaseVo extends AbsBaseVoSerializ {

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

	public void buideJson(JSONObject job) throws JSONException {

	}

	public String toJsonString() {
		JSONObject ob = toJson();
		if (null != ob) {
			return ob.toString();
		}
		return null;
	}

	public JSONObject toJson() {
		return null;
	}
}
