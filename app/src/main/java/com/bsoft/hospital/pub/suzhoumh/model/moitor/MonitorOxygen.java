package com.bsoft.hospital.pub.suzhoumh.model.moitor;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 血氧
 * @author Administrator
 *
 */
public class MonitorOxygen extends AbsBaseVoSerializ{

	public String unit;
	public float oxygen;
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
