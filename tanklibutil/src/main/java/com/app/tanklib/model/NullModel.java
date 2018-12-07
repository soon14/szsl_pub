package com.app.tanklib.model;

import org.json.JSONException;
import org.json.JSONObject;

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
