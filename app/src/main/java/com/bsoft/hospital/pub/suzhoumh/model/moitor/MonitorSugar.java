package com.bsoft.hospital.pub.suzhoumh.model.moitor;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 血糖
 * @author Administrator
 *
 */
public class MonitorSugar extends AbsBaseVoSerializ{

	public String unit;
	public float sugar;
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
