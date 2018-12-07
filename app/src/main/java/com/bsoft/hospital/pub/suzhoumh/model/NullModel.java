package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * @author Tank   E-mail:zkljxq@126.com
 * @类说明 药品
 */
public class NullModel  extends AbsBaseVoSerializ {
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
	}

	@Override
	public JSONObject toJson() {
		return null;
	}
	

}
