package com.bsoft.hospital.pub.suzhoumh.model.app.appoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 预约科室
 * @author Administrator
 *
 */
public class AppointDeptVo extends AbsBaseVoSerializ{

	//his接口返回的字段
	public String ksdm;
	public String ksmc;

	//平台接口返回的字段
	public String intro;//介绍

	public String pinyin;//拼音
	//在特色门诊中 0为普通科室，1为专家科室
	public String zjks;

	@Override
	public void buideJson(JSONObject job) throws JSONException {

	}

	@Override
	public JSONObject toJson() {
		return null;
	}
}
