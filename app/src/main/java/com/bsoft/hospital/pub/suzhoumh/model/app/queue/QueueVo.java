package com.bsoft.hospital.pub.suzhoumh.model.app.queue;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 排队屏幕
 * @author Administrator
 *
 */
public class QueueVo extends AbsBaseVoSerializ{

	public String pmmc;
	public ArrayList<PDQKVo> pdqk;
	
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
