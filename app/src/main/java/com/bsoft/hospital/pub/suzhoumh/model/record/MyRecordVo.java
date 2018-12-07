package com.bsoft.hospital.pub.suzhoumh.model.record;

import java.util.ArrayList;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 * 
 */

public class MyRecordVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String title;
	public ArrayList<RecordVo> records;
	public int totalCount;

	@Override
	public void buideJson(JSONObject job) {
		
	}

	@Override
	public JSONObject toJson() {
		return null;
	}


}
