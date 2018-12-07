package com.bsoft.hospital.pub.suzhoumh.model.app.appoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 预约医生
 * @author Administrator
 *
 */
public class AppointDoctorVo extends AbsBaseVoSerializ{

	public String ksdm;
	public String ygdm;
	public String yggh;
	public String ygxm;
	public String ysjj;
	public String ygzw;
	public String ygjb;
	public String sex;
	public String zjfy;
	
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
