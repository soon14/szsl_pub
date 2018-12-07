package com.bsoft.hospital.pub.suzhoumh.model.app.queue;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 排队情况
 * @author Administrator
 *
 */
public class PDQKVo extends AbsBaseVoSerializ{

	public String ksdm;
	public String ksmc;
	public String zssl;
	public String dqxh;
	public String zdxh;
	public String ygxm;
	public String syhys;
	public String ddrs;
	//我的排队新增的字段
	public String jzkh;
	public String wdxh;
	public String zsmc;
	public String zslcxx;
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
	}

	@Override
	public JSONObject toJson() {
		return null;
	}
}
