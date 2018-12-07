package com.bsoft.hospital.pub.suzhoumh.model.satisfaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 满意度列表
 * @author Administrator
 *
 */
public class SatisfactionListVo extends AbsBaseVoSerializ{

	public String jzkh;
	public String ksmc;
	public String ksdm;
	public String ygxm;
	public String yggh;
	public String ygzc;
	public String zdjg;
	public String fwsj;
	public String jzxh;
	
	public int flag = 0;//0未评价1已评价
	
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
