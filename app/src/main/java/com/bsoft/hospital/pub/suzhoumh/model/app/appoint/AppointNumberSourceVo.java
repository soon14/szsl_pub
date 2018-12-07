package com.bsoft.hospital.pub.suzhoumh.model.app.appoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 预约号源
 * @author Administrator
 *
 */
public class AppointNumberSourceVo extends AbsBaseVoSerializ{

	public String jzxh;
//	public String sjd;
	public String qssj;//开始时间
	public String jzsj;//结束时间
	public String syhy;//剩余号源
	
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
