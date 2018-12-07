package com.bsoft.hospital.pub.suzhoumh.model.satisfaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 环节
 * @author Administrator
 *
 */
public class SectionVo extends AbsBaseVoSerializ{

	public String iid;//评价指标id
	public String title;
	public String idx;
	public String sort;
	public int degree;
	
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
