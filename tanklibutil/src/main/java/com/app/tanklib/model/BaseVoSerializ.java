package com.app.tanklib.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * modle接口类
 * @author zkl
 *
 */
public interface BaseVoSerializ extends Serializable{

	public void buideJson(String josnString);
	
	public void buideJson(JSONObject job)  throws JSONException;
	
	public String toJsonString();
	
	public JSONObject toJson();
}
