package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class MedicDetail extends AbsBaseVoSerializ{

	// 详细内容

	//id
	public String id;
	
	//标题
	public String title;
	// 适应症
	public String healfun;

	// 用法用量
	public String usagedosage;

	// 不良反应
	public String adversereactions;

	// 禁忌
	public String taboo;

	// 批准文号
	public String approvaldoc;

	// 功能主治
	public String indfun;
	
	//英文名
	public String englishname;
	
	//别名
	public String alias;
	
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
