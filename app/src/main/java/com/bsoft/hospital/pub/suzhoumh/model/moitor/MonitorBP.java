package com.bsoft.hospital.pub.suzhoumh.model.moitor;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 血压
 * @author Administrator
 *
 */
public class MonitorBP extends AbsBaseVoSerializ{
	
	public String unit;
	public float dbp;//舒张压
	public float sbp;//收缩压

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
